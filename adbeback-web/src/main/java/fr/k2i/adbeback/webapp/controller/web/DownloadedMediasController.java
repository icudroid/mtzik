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

import fr.k2i.adbeback.core.business.game.AdGameMusic;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.AdGameManager;
import fr.k2i.adbeback.service.PlayerManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/my-musics.*")
public class DownloadedMediasController {
	private AdGameManager adGameManager;
	private PlayerManager playerManager;
	
	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}


	@Autowired
    public void setAdGameManager(AdGameManager adGameManager) {
		this.adGameManager = adGameManager;
	}


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showForm(HttpServletRequest request) throws Exception {
    	SecurityContext ctx = SecurityContextHolder.getContext();
    	Player player = null;
    	
    	if (ctx.getAuthentication().getPrincipal() instanceof String) {
    		player = playerManager.getPlayerByUsername(request.getRemoteUser());
		}else{
			player = (Player) ctx.getAuthentication().getPrincipal();
		}
    	
    	List<AdGameMusic> adgames = adGameManager.findWonAdGameMusicInfSevenDays(player.getId());
    	return new ModelAndView("my-musics","adgames",adgames);
    	
    }
    
}
