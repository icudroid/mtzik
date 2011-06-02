package fr.k2i.adbeback.webapp.controller.mp3;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.service.MediaManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("*")
public class PlayController {
	 private MediaManager mediaManager = null;
	 
		private String musicPath;
		
		@Autowired
		public void setMusicPath(String musicPath) {
			this.musicPath = musicPath;
		}
	 
	 
	 @Autowired
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }
    
    @RequestMapping(value="/{id}.mp3",method = RequestMethod.GET)
    public  void play(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String mp3Sample = ((Music) mediaManager.get(id)).getMp3Sample();
    	try {
    		java.io.File file = new java.io.File(musicPath+mp3Sample);
			response.setContentType("audio/mpeg");	
    		response.setContentLength((int) file.length());
    		ServletOutputStream outputStream = response.getOutputStream();
    		FileInputStream fileInputStream = new FileInputStream(file);
    		int read =0;
    		byte []b = new byte[1024];
    		while((read = fileInputStream.read(b, 0, 1024))>0){
    			outputStream.write(b, 0, read);
    			b = new byte[1024];
    		}
    	} catch (Exception e) {
    	}

    }
    
}
