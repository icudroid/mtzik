package fr.k2i.adbeback.webapp.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.PlayerManager;

@Controller
@RequestMapping("/validate.*")
public class ValidateControler {
	
	private PlayerManager playerManager;
	
	@Autowired
    public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@RequestMapping(method = RequestMethod.GET)
    public String validate(@RequestParam String key,@RequestParam String username) {
    	
		Player player = playerManager.getPlayerByUsername(username);
		
		if(player.getPasswordHint()!=null && !player.getPasswordHint().isEmpty()){
			if(player.getPasswordHint().equals(key)){
				player.setEnabled(true);
				player.setPasswordHint(null);
				playerManager.save(player);
				
		        // log user in automatically
		        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
		                player.getUsername(), player.getConfirmPassword(), player.getAuthorities());
		        auth.setDetails(player);
		        SecurityContextHolder.getContext().setAuthentication(auth);
				 return "redirect:account.html";
			}else{
				return null;
			}
	    	
		}else{
			return null;
		}
    }
}
