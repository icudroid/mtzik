package fr.k2i.adbeback.webapp.controller.json;

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
import fr.k2i.adbeback.bean.SearchResponseBean;
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
@RequestMapping("/json/search*")
public class JsonSearchController {
	 private MediaManager mediaManager = null;
	 private static Integer MAX_RESULT = 10;
    @Autowired
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @RequestMapping(value="/searchMusic.json",method = RequestMethod.POST)
    public  @ResponseBody List<MediaBean> searchMusic(@RequestBody SearchBean searchBean) throws Exception {
    	return mediaManager.find(searchBean);
    }
    

    @RequestMapping(value="/searchMusicAutoComplete.json",method = RequestMethod.POST)
    public  @ResponseBody List<SearchResponseBean> searchAutoComplete(@RequestBody SearchBean searchBean) throws Exception {
    	return mediaManager.findForAutoComplete(searchBean);
    }
    
}
