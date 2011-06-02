package fr.k2i.adbeback.webapp.controller.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fr.k2i.adbeback.bean.MediaBean;
import fr.k2i.adbeback.bean.PersonBean;
import fr.k2i.adbeback.bean.SearchBean;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Genre;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.media.MusicGenre;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.service.MediaManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/search*")
public class SearchController {
	private MediaManager mediaManager;
	
	@Autowired
    public void setMediaManager(MediaManager mediaManager) {
		this.mediaManager = mediaManager;
	}
	@RequestMapping
	public ModelAndView view(@RequestParam(required=false)String str,@RequestParam(required=false)Long cat) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("search");
    	List<Media> res = mediaManager.find(str,cat);
		
		modelAndView.addObject("medias", res);
    	if( cat != null && cat !=-1){
    		modelAndView.addObject("catSearch", true);
    		modelAndView.addObject("categorie", cat);
    	}else{
    		modelAndView.addObject("catSearch", false);
    	}
    	
    	if(str !=null){
    		modelAndView.addObject("strSearch", true);
    		modelAndView.addObject("str", str);
    	}else{
    		modelAndView.addObject("strSearch", false);
    	}
		
        return modelAndView;
    }

    
}
