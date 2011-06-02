package fr.k2i.adbeback.webapp.controller.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.k2i.adbeback.core.business.player.Player;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/account.*")
public class AccountController extends BaseFormController{

    public AccountController() {
        setCancelView("redirect:account.html");
        setSuccessView("redirect:account.html");
    }
    
    @ModelAttribute
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    protected Player showForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	 return getPlayerManager().getPlayerByUsername(request.getRemoteUser());
	}
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Player player, BindingResult errors, HttpServletRequest request, HttpServletResponse response)throws Exception {
    	Player playerToMod = getPlayerManager().getPlayerByUsername(request.getRemoteUser());
    	playerToMod.setAddress(player.getAddress());
    	playerToMod.setPassword(player.getPassword());
    	playerToMod.setConfirmPassword(player.getConfirmPassword());
    	playerToMod.setNewsletter(player.getNewsletter());
    	getPlayerManager().savePlayer(playerToMod);
		return "redirect:account.html";
    }

}
