package fr.k2i.adbeback.webapp.controller.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.MailEngine;
import fr.k2i.adbeback.service.PlayerManager;
import fr.k2i.adbeback.webapp.util.RequestUtil;

/**
 * Simple class to retrieve and send a password hint to users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/password.*")
public class ForgotPasswordController {
    private final Log log = LogFactory.getLog(ForgotPasswordController.class);
    private PlayerManager playerManager = null;
    private MessageSource messageSource = null;
    protected MailEngine mailEngine = null;
    protected SimpleMailMessage message = null;

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    @Autowired
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> handleRequest(HttpServletRequest request)
    throws Exception {
        log.debug("entering 'handleRequest' method...");
        Map<String,Object> res = new HashMap<String, Object>();
        String username = request.getParameter("username");
        MessageSourceAccessor text = new MessageSourceAccessor(messageSource, request.getLocale());

        // ensure that the username has been sent
        if (username == null) {
            log.warn("Username not specified, notifying user that it's a required field.");
            res.put("msg", text.getMessage("errors.required", text.getMessage("user.username")));
        }

        log.debug("Processing Password Hint...");

        // look up the user's information
        try {
            Player player = playerManager.getPlayerByUsername(username);
            player.setPassword(getRandomString(16));
            StringBuffer msg = new StringBuffer();
            msg.append("Votre nouveau mot de passe est : ").append(player.getPassword());
            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(request));

            message.setTo(player.getEmail());
            String subject = '[' + text.getMessage("webapp.name") + "] " + 
                             text.getMessage("user.forgotPassword");
            message.setSubject(subject);
            message.setText(msg.toString());
            mailEngine.send(message);
            playerManager.savePlayer(player);
            res.put("msg", "Votre nouveau mot de passe vous a été anvoyé par email");
        } catch (UsernameNotFoundException e) {
            log.warn(e.getMessage());
            res.put("msg",  text.getMessage("login.forgotPassword.error", new Object[] { username }));
        } catch (MailException me) {
        	res.put("msg",  me.getCause().getLocalizedMessage());
        }

        return res;
    }

	private String getRandomString(int length) {
		String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789&$!_#";
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
}
