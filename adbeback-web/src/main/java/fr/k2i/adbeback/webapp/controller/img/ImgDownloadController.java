package fr.k2i.adbeback.webapp.controller.img;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("*")
public class ImgDownloadController{
	
	private String imgPath;
	
	@Autowired
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@RequestMapping(value="{title}.{ext}",method=RequestMethod.GET)
    public  String handleRequest(@PathVariable String title, @PathVariable String ext,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			java.io.File file = new java.io.File(imgPath+title+"."+ext);
			
			if("png".equals(ext)){
				response.setContentType("image/png");	
			}else if("jpg".equals(ext)){
				response.setContentType("image/jpeg");	
			}else if("gif".equals(ext)){
				response.setContentType("image/gif");	
			}else if("flv".equals(ext)){
				response.setContentType("application/x-shockwave-flash");	
			}

			

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
		return null;
    }

	@RequestMapping(value="{path}/{title}.{ext}",method=RequestMethod.GET)
    public  String handleRequestThumb(@PathVariable String path,@PathVariable String title, @PathVariable String ext,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			java.io.File file = new java.io.File(imgPath+path+"/"+title+"."+ext);
			if("png".equals(ext)){
				response.setContentType("image/png");	
			}else if("jpg".equals(ext)){
				response.setContentType("image/jpeg");	
			}else if("gif".equals(ext)){
				response.setContentType("image/gif");	
			}else if("flv".equals(ext)){
				response.setContentType("application/x-shockwave-flash");	
			}

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
		return null;
    }
	
	
}
