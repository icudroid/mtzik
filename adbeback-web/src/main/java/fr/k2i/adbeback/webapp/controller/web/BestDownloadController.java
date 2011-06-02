package fr.k2i.adbeback.webapp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/best-download.*")
public class BestDownloadController extends BaseFormController {

	@RequestMapping(method=RequestMethod.GET)
	public String show() {
		return "best-download";
	}
	
}
