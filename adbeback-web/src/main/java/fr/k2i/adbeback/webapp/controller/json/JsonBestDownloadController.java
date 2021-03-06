package fr.k2i.adbeback.webapp.controller.json;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.k2i.adbeback.bean.MediaBean;
import fr.k2i.adbeback.service.MediaManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/json/best-download*")
public class JsonBestDownloadController{
	
	private MediaManager mediaManager;
	
	@Autowired
    public void setMediaManager(MediaManager mediaManager) {
		this.mediaManager = mediaManager;
	}


	@RequestMapping(value="/searchMusic.json",method = RequestMethod.POST)
    public  @ResponseBody List<MediaBean> searchMusic(@RequestParam(required=false) Long idGenre) throws Exception {
    	return  mediaManager.searchBestDownload(idGenre);
    }
	
}
