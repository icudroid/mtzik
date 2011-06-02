package fr.k2i.adbeback.webapp.controller.flv;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.k2i.adbeback.webapp.bean.ByteArray;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("*")
public class FlvDownloadController{
	
	private String flvPath;
	
	@Autowired
	public void setFlvPath(String flvPath) {
		this.flvPath = flvPath;
	}
	
	public static Integer BYTES_TO_SEND = 250*1024;
	
	@RequestMapping(value="{title}.flv/{part}",method=RequestMethod.GET)
    public  String handleRequest(@PathVariable String title, @PathVariable Integer part,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
			ByteArrayInputStream bi = null;
			
			java.io.File file = new java.io.File(flvPath+title+".flv");
			response.setContentType("application/x-shockwave-flash");
			
			if(part ==0){
				FileInputStream fileInputStream = new FileInputStream(file);
				byte []bForIn = new byte[(int) file.length()];
				fileInputStream.read(bForIn, 0, (int) file.length());
				bi = new ByteArrayInputStream(bForIn);
				request.getSession().setAttribute("inputStream", new ByteArray(bForIn));
			}else{
				bi = new ByteArrayInputStream( ((ByteArray)request.getSession().getAttribute("inputStream")).getBytes());
			}
			
			byte []b = new byte[BYTES_TO_SEND];
			int read = 0;
			for (int i = 0; i <= part; i++) {
				b = new byte[BYTES_TO_SEND];
				read = bi.read(b, 0, BYTES_TO_SEND);
			}
			response.setContentLength(read);
			response.getOutputStream().write(b, 0, read);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
}
