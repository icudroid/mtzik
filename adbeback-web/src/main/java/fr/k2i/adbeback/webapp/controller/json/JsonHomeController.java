package fr.k2i.adbeback.webapp.controller.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fr.k2i.adbeback.bean.CagnotteBean;
import fr.k2i.adbeback.bean.CategorieBean;
import fr.k2i.adbeback.bean.MediaBean;
import fr.k2i.adbeback.bean.PersonBean;
import fr.k2i.adbeback.core.business.Constants;
import fr.k2i.adbeback.core.business.media.Genre;
import fr.k2i.adbeback.core.business.media.MusicGenre;
import fr.k2i.adbeback.service.GenreManager;
import fr.k2i.adbeback.service.GooseGameManager;
import fr.k2i.adbeback.service.MediaManager;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * <p>
 * <a href="UserController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/json/home*")
public class JsonHomeController {
    private transient final Log log = LogFactory.getLog(JsonHomeController.class);
    private MediaManager mediaManager = null;
    private GenreManager genreManger;
    private GooseGameManager gooseGameManager;
    
    private static Integer MAX_RESULT = 10;
    
    @Autowired
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }
    @Autowired
    public void setGenreManger(GenreManager genreManger) {
		this.genreManger = genreManger;
	}
    @Autowired
	public void setGooseGameManager(GooseGameManager gooseGameManager) {
		this.gooseGameManager = gooseGameManager;
	}
	@RequestMapping(value="/getNews.json",method = RequestMethod.POST)
    public  @ResponseBody List<MediaBean> getNews() throws Exception {
    	return  mediaManager.getNewProducts(MAX_RESULT);
    }
    
    @RequestMapping(value="/getCategories.json",method = RequestMethod.POST)
    public  @ResponseBody List<CategorieBean> getCategories() throws Exception {
    	return genreManger.all(MusicGenre.class);
    }
    
    @RequestMapping(value="/getCagnottes.json",method = RequestMethod.POST)
    public  @ResponseBody List<CagnotteBean> getCagnottes() throws Exception {
    	return gooseGameManager.getCagnottes();
    }
    
	@RequestMapping(value="/getHomeMedias.json",method = RequestMethod.POST)
    public  @ResponseBody List<MediaBean> getHomeMedias() throws Exception {
    	return  mediaManager.getHomeMedias();
    }
    
    
}
