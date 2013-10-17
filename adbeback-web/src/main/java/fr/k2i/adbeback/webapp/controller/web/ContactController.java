package fr.k2i.adbeback.webapp.controller.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.k2i.adbeback.core.business.LabelValue;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.service.PlayerManager;
import fr.k2i.adbeback.webapp.bean.ContactForm;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/contact-form.*")
public class ContactController extends BaseFormController {

	private PlayerManager playerManager;
	
	@Autowired
    public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
	
	private static List<LabelValue> objects = new ArrayList<LabelValue>();
	static{
    	objects.add(new LabelValue("Service client", "1"));
    	objects.add(new LabelValue("WebMaster", "2"));
    	objects.add(new LabelValue("Marketing", "3"));
    	objects.add(new LabelValue("Autres", "4"));
	}

	@ModelAttribute
	@RequestMapping(method=RequestMethod.GET)
	public ContactForm showForm(HttpServletRequest request,
            HttpServletResponse response) {
    	
		request.setAttribute("objects", objects);
		ContactForm res = new ContactForm();
    	String remoteUser = request.getRemoteUser();
    	if(remoteUser != null){
	    	Player player = playerManager.getPlayerByUsername(request.getRemoteUser());
	    	res.setEmail(player.getEmail());
    	}
		return res;
	}
	
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(ContactForm contactForm, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail from user [" + contactForm.getEmail() + "]...");
        }

        message.setTo("contact@mtzik.fr");
        message.setSubject("Contact-Form");
        
        Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("email", contactForm.getEmail());
        model.put("message", contactForm.getMessage());
        model.put("object", ((LabelValue)objects.get(Integer.parseInt(contactForm.getObject())-1)).getLabel());
        
        mailEngine.sendMessage(message, "contactForm.vm", model);
    	return "redirect:home.html";
    }
	
}
