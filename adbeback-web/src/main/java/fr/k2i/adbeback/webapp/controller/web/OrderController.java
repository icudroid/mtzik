package fr.k2i.adbeback.webapp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/order.*")
public class OrderController {

	@RequestMapping(method=RequestMethod.GET)
	public String view() {
		return "order";
	}
	
}
