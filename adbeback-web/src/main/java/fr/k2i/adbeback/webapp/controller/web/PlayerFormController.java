package fr.k2i.adbeback.webapp.controller.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.k2i.adbeback.core.business.Constants;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.core.business.player.Role;
import fr.k2i.adbeback.service.RoleManager;
import fr.k2i.adbeback.service.UserExistsException;
import fr.k2i.adbeback.webapp.util.RequestUtil;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p><a href="UserFormController.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/playerform.*")
public class PlayerFormController extends BaseFormController {
    private RoleManager roleManager;

    @Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public PlayerFormController() {
        setCancelView("redirect:mainMenu.html");
        setSuccessView("redirect:admin/users.html");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Player player, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
            throws Exception {
        if (request.getParameter("cancel") != null) {
            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                return getCancelView();
            } else {
                return getSuccessView();
            }
        }

        if (validator != null) { // validator is null during testing
            validator.validate(player, errors);

            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "playerform";
            }
        }

        log.debug("entering 'onSubmit' method...");

        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            getPlayerManager().removePlayer(player.getId().toString());
            saveMessage(request, getText("user.deleted", player.getFullName(), locale));

            return getSuccessView();
        } else {

            // only attempt to change roles if user is admin for other users,
            // showForm() method will handle populating
            if (request.isUserInRole(Constants.ADMIN_ROLE)) {
                String[] userRoles = request.getParameterValues("userRoles");

                if (userRoles != null) {
                    player.getRoles().clear();
                    for (String roleName : userRoles) {
                        player.addRole(roleManager.getRole(roleName));
                    }
                }
            }

            Integer originalVersion = player.getVersion();

            try {
                getPlayerManager().savePlayer(player);
            } catch (AccessDeniedException ade) {
                // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
                log.warn(ade.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            } catch (UserExistsException e) {
                errors.rejectValue("username", "errors.existing.user",
                        new Object[]{player.getUsername(), player.getEmail()}, "duplicate user");

                // redisplay the unencrypted passwords
                player.setPassword(player.getConfirmPassword());
                // reset the version # to what was passed in
                player.setVersion(originalVersion);

                return "playerform";
            }

            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                saveMessage(request, getText("user.saved", player.getFullName(), locale));

                // return to main Menu
                return getCancelView();
            } else {
                if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request, getText("user.added", player.getFullName(), locale));

                    // Send an account information e-mail
                    message.setSubject(getText("signup.email.subject", locale));

                    try {
                        sendUserMessage(player, getText("newuser.email.message", player.getFullName(), locale),
                                RequestUtil.getAppURL(request));
                    } catch (MailException me) {
                        saveError(request, me.getCause().getLocalizedMessage());
                    }

                    return getSuccessView();
                } else {
                    saveMessage(request, getText("user.updated.byAdmin", player.getFullName(), locale));
                }
            }
        }

        return "playerform";
    }

    @ModelAttribute
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public Player showForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // If not an administrator, make sure user is not trying to add or edit another user
        if (!request.isUserInRole(Constants.ADMIN_ROLE) && !isFormSubmission(request)) {
            if (isAdd(request) || request.getParameter("id") != null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() + "' is trying to edit user with id '" +
                        request.getParameter("id") + "'");

                throw new AccessDeniedException("You do not have permission to modify other users.");
            }
        }

        if (!isFormSubmission(request)) {
            String playerId = request.getParameter("id");

            // if user logged in with remember me, display a warning that they can't change passwords
            log.debug("checking for remember me login...");

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            SecurityContext ctx = SecurityContextHolder.getContext();

            if (ctx.getAuthentication() != null) {
                Authentication auth = ctx.getAuthentication();

                if (resolver.isRememberMe(auth)) {
                    request.getSession().setAttribute("cookieLogin", "true");

                    // add warning message
                    saveMessage(request, getText("userProfile.cookieLogin", request.getLocale()));
                }
            }

            Player player;
            if (playerId == null && !isAdd(request)) {
                player = getPlayerManager().getPlayerByUsername(request.getRemoteUser());
            } else if (!StringUtils.isBlank(playerId) && !"".equals(request.getParameter("version"))) {
                player = getPlayerManager().getPlayer(playerId);
            } else {
                player = new Player();
                player.addRole(new Role(Constants.USER_ROLE));
            }

            player.setConfirmPassword(player.getPassword());

            return player;
        } else {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return getPlayerManager().getPlayer(request.getParameter("id"));
        }
    }

    private boolean isFormSubmission(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post");
    }

    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
}
