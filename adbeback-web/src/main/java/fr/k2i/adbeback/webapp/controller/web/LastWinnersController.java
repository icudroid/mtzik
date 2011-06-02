package fr.k2i.adbeback.webapp.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.k2i.adbeback.core.business.goosegame.GooseWin;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.GooseGameManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/last-winners.*")
public class LastWinnersController extends BaseFormController {

	private GooseGameManager gooseGameManager;

	@Autowired
	public void setGooseGameManager(GooseGameManager gooseGameManager) {
		this.gooseGameManager = gooseGameManager;
	}


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request) throws Exception {
    	ModelAndView modelAndView = new ModelAndView("last-winners");
    	List<GooseWin> lastWinners = gooseGameManager.getLastWinners();
    	modelAndView.addObject("lastWinners", lastWinners);
    	return modelAndView;
    	
    }
}
