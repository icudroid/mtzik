package fr.k2i.adbeback.webapp.controller.web;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.k2i.adbeback.core.business.Constants;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.RoleManager;
import fr.k2i.adbeback.service.UserExistsException;
import fr.k2i.adbeback.webapp.util.RequestUtil;
import fr.k2i.tools.PasswordGenerator;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/signup.*")
public class SignupController extends BaseFormController {
    private RoleManager roleManager;

    @Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    public Player showForm(@RequestParam String email_create) {
    	Player player = new Player();
    	player.setEmail(email_create);
    	return player;  
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Player player, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }
        Locale locale = request.getLocale();
        
        if(player.getPassword()==null || player.getPassword().trim().isEmpty() || player.getPassword().length()<6){
        	errors.rejectValue("password", "errors.minlength",
                    new Object[]{"Le mot de passe", 6},"password length");
        }
        
        if(player.getPassword()!=null && !player.getPassword().equals(player.getConfirmPassword())){
        	errors.rejectValue("confirmPassword", "errors.twofields",
                    new Object[]{"mot de passe", "confirmation de mot de passe"},"no matching password");
        }
        
        Date birthday = player.getBirthday();
        Calendar eighteen = new GregorianCalendar();
        eighteen.add(Calendar.YEAR, -18);
        if(birthday==null){
        	errors.rejectValue("birthday", "errors.required",
                    new Object[]{"Anniversaire"},"required");
        }else{
            if(birthday.after(eighteen.getTime())){
            	errors.rejectValue("birthday", "errors.major",
                        new Object[]{},"Minor peron");
            }
        }
        
        if(player.getEmail() == null ||player.getEmail().trim().isEmpty()){
        	errors.rejectValue("email", "errors.required",
                    new Object[]{"Email"},"required");
        }else{
        	 if(this.getPlayerManager().getPlayerByEmail(player.getEmail())!=null){
                 errors.rejectValue("email", "errors.existing.user.email",
                         new Object[]{player.getUsername(), player.getEmail()}, "duplicate user");
        	 }
        }
        
        if(player.getFirstName()==null || player.getFirstName().trim().isEmpty()){
        	errors.rejectValue("firstName", "errors.required",
                    new Object[]{"Prénom"},"required");
        }

        if(player.getLastName()==null || player.getLastName().trim().isEmpty()){
        	errors.rejectValue("lastName", "errors.required",
                    new Object[]{"Nom"},"required");
        }
        
        if(player.getUsername() == null ||player.getUsername().trim().isEmpty()){
        	errors.rejectValue("username", "errors.required",
                    new Object[]{"Pseudo"},"required");
        }else{
        	try{
           	 if(this.getPlayerManager().getPlayerByUsername(player.getUsername())!=null){
                 errors.rejectValue("username", "errors.existing.user.username",
                         new Object[]{player.getUsername()}, "duplicate user");
        	 }
        	}catch (UsernameNotFoundException e) {
			}
        }

        if(player.getSex()==null){
        	errors.rejectValue("sex", "errors.required",
                    new Object[]{"Sex"},"required");
        }

        if(errors.hasErrors()){
        	return "signup"; 
        }
        
        player.setEnabled(false);
        player.setPasswordHint(PasswordGenerator.getRandomPasswordHint(16));
        // Set the default user role on this new user
        player.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            this.getPlayerManager().savePlayer(player);
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
            return "signup";
        }

        saveMessage(request, getText("user.registered", player.getUsername(), locale));

        // log user in automatically
        /*UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                player.getUsername(), player.getConfirmPassword(), player.getAuthorities());
        auth.setDetails(player);
        SecurityContextHolder.getContext().setAuthentication(auth);*/

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + player.getUsername() + "' an account information e-mail");
        }

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject", locale));

        try {
            sendUserMessage(player, getText("signup.email.message", locale), RequestUtil.getAppURL(request));
        } catch (MailException me) {
            saveError(request, me.getMostSpecificCause().getMessage());
        }
        
        return "register";
    }
}
