package fr.k2i.adbeback.webapp.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.k2i.adbeback.core.business.goosegame.GooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.GooseGameManager;
import fr.k2i.adbeback.service.PlayerManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/my-win-game.*")
public class MyWinController {
	private PlayerManager playerManager;
	private GooseGameManager gooseGameManager;

	@Autowired
	public void setGooseGameManager(GooseGameManager gooseGameManager) {
		this.gooseGameManager = gooseGameManager;
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request) throws Exception {
    	SecurityContext ctx = SecurityContextHolder.getContext();
    	Player player = null;
    	
    	if (ctx.getAuthentication().getPrincipal() instanceof String) {
    		player = playerManager.getPlayerByUsername(request.getRemoteUser());
		}else{
			player = (Player) ctx.getAuthentication().getPrincipal();
			player = playerManager.get(player.getId());
		}
    	
    	ModelAndView modelAndView = new ModelAndView("my-win-game");
    	modelAndView.addObject("player", player);
    	
    	return modelAndView;
    	
    }
    
}
