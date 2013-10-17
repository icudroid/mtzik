package fr.k2i.adbeback.webapp.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.k2i.adbeback.core.business.media.Album;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.service.MediaManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/product*")
public class ProductController extends BaseFormController {

	private MediaManager mediaManager;
	
	@Autowired
	public void setMediaManager(MediaManager mediaManager) {
		this.mediaManager = mediaManager;
	}

	@RequestMapping(value = "/{title}_{id}.html")
	public ModelAndView show(@PathVariable String title,@PathVariable String id) {
		Media media = mediaManager.get(new Long(id));
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("product");
		modelAndView.addObject("media", media);
		if (media instanceof Album) {
			modelAndView.addObject("type", "Album");
		}else if (media instanceof Music) {
			modelAndView.addObject("type", "music");
		}
		return modelAndView;
	}
}
