package fr.k2i.adbeback.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import fr.k2i.adbeback.core.business.Constants;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.core.business.player.Role;

/**
 * This advice is responsible for enforcing security and only allowing administrators
 * to modify users. Users are allowed to modify themselves.
 *
 * @author mraible
 */
public class PlayerSecurityAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    /**
     * Default "Access Denied" error message (not i18n-ized).
     */
    public static final String ACCESS_DENIED = "Access Denied: Only administrators are allowed to modify other users.";
    private final Log log = LogFactory.getLog(PlayerSecurityAdvice.class);

    /**
     * Method to enforce security and only allow administrators to modify users. Regular
     * users are allowed to modify themselves.
     *
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when args[0] is null or not a User object
     */
    public void before(Method method, Object[] args, Object target) throws Throwable {
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            boolean administrator = false;
            Collection<GrantedAuthority> roles = auth.getAuthorities();
            for (GrantedAuthority role1 : roles) {
                if (role1.getAuthority().equals(Constants.ADMIN_ROLE)) {
                    administrator = true;
                    break;
                }
            }

            Player player = (Player) args[0];

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            // allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
            boolean signupUser = resolver.isAnonymous(auth);

            if (!signupUser) {
                Player currentPlayer = getCurrentPlayer(auth);

                if (player.getId() != null && !player.getId().equals(currentPlayer.getId()) && !administrator) {
                    log.warn("Access Denied: '" + currentPlayer.getUsername() + "' tried to modify '" + player.getUsername() + "'!");
                    throw new AccessDeniedException(ACCESS_DENIED);
                } else if (player.getId() != null && player.getId().equals(currentPlayer.getId()) && !administrator) {
                    // get the list of roles the user is trying add
                    Set<String> userRoles = new HashSet<String>();
                    if (player.getRoles() != null) {
                        for (Object o : player.getRoles()) {
                            Role role = (Role) o;
                            userRoles.add(role.getName());
                        }
                    }

                    // get the list of roles the user currently has
                    Set<String> authorizedRoles = new HashSet<String>();
                    for (GrantedAuthority role : roles) {
                        authorizedRoles.add(role.getAuthority());
                    }

                    // if they don't match - access denied
                    // regular users aren't allowed to change their roles
                    if (!CollectionUtils.isEqualCollection(userRoles, authorizedRoles)) {
                        log.warn("Access Denied: '" + currentPlayer.getUsername() + "' tried to change their role(s)!");
                        throw new AccessDeniedException(ACCESS_DENIED);
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Registering new user '" + player.getUsername() + "'");
                }
            }
        }
    }

    /**
     * After returning, grab the user, check if they've been modified and reset the SecurityContext if they have.
     * @param returnValue the user object
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when args[0] is null or not a User object
     */
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
    throws Throwable {
        Player player = (Player) args[0];

        if (player.getVersion() != null) {
            // reset the authentication object if current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            // allow new users to signup - this is OK b/c Signup doesn't allow setting of roles
            boolean signupUser = resolver.isAnonymous(auth);
            if (auth != null && !signupUser) {
                Player currentPlayer = getCurrentPlayer(auth);
                if (currentPlayer.getId().equals(player.getId())) {
                    auth = new UsernamePasswordAuthenticationToken(player, player.getPassword(), player.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
    }

    private Player getCurrentPlayer(Authentication auth) {
        Player currentPlayer;
        if (auth.getPrincipal() instanceof UserDetails) {
            currentPlayer = (Player) auth.getPrincipal();
        } else if (auth.getDetails() instanceof UserDetails) {
            currentPlayer = (Player) auth.getDetails();
        } else {
            throw new AccessDeniedException("User not properly authenticated.");
        }
        return currentPlayer;
    }
}
