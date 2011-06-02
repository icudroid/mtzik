package fr.k2i.adbeback.webapp.controller.json;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.PlayerManager;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/json/account*")
public class JsonAccountController {
    private transient final Log log = LogFactory.getLog(JsonAccountController.class);
    private PlayerManager playerManager;

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@RequestMapping(value="/delete.json",method = RequestMethod.GET)
    public  @ResponseBody Map<String,Object> disableAccount(HttpServletRequest request) throws Exception {
		SecurityContext ctx = SecurityContextHolder.getContext();
		Player player = null;

		if (ctx.getAuthentication().getPrincipal() instanceof String) {
			player = playerManager.getPlayerByUsername(request.getRemoteUser());
		} else {
			player = (Player) ctx.getAuthentication().getPrincipal();
			player = playerManager.get(player.getId());
		}
		log.info("Suppression du joueur :"+player.getUsername());
		player.setEnabled(false);
		playerManager.save(player);
		request.getSession().invalidate();
    	return  null;
    }
    
}
