package fr.k2i.adbeback.webapp.controller.dnm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.k2i.adbeback.service.AdGameManager;
import fr.k2i.adbeback.webapp.controller.json.JsonAdGameController;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("*")
public class MediaDownloadController{
	
	private AdGameManager adGameManager;
	
	@Autowired
	public void setAdGameManager(AdGameManager adGameManager) {
		this.adGameManager = adGameManager;
	}

	@RequestMapping(method=RequestMethod.GET)
    public  String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long idGame = (Long) request.getSession().getAttribute(JsonAdGameController.ID_ADGAME);
		adGameManager.getMediasEnc(idGame, response);
		return null;
    }

	@RequestMapping(method=RequestMethod.POST)
    public  String handleRequest(@RequestParam Long idGame, HttpServletRequest request, HttpServletResponse response) throws Exception {
		adGameManager.getMedias(idGame, response);
		return null;
    }
	
}
