package fr.k2i.adbeback.webapp.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/home*")
public class HomeController {
    private transient final Log log = LogFactory.getLog(HomeController.class);
    private MediaManager mediaManager = null;

    @Autowired
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view() throws Exception {
    	return new ModelAndView("home", "bestDownload", mediaManager.searchBestDownload(-1L));
    }
    
}
