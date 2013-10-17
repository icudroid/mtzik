package fr.k2i.adbeback.webapp.controller.json;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.k2i.adbeback.bean.CartBean;
import fr.k2i.adbeback.bean.MediaLineBean;
import fr.k2i.adbeback.service.MediaManager;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/json/cart*")
public class JsonCartController {
	 private MediaManager mediaManager = null;
	 public static final String CART = "cart";
    @Autowired
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @RequestMapping(value="/add.json",method = RequestMethod.POST)
    public  @ResponseBody CartBean addToCart(@RequestParam Long idMedia,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	CartBean cart = getCart(request, response);
    	cart.setError("");
    	if(cart.getNbProduct()==3){
    		cart.setError("Vous ne pouvez mettre que 3 musiques dans le panier");
    	}else{
	    	MediaLineBean line = mediaManager.getMediaLineBean(idMedia);
	    	if(cart.getLines().contains(line)){
	    		cart.setError("Cette musique est déjà dans le panier");
	    	}else{
	    		cart.getLines().add(line);	
	    		cart.setError("La musique a été ajoutée au panier");
	    	}
	    	//checkMusicDoublon(cart);
	    	recalculateAdNeeeded(cart);
    	}
    	
    	return cart;
    }

    /*private void checkMusicDoublon(CartBean cart) {
    	Set<MediaLineBean> lines = cart.getLines();
    	Map<Long,MediaLineBean>checkMap = new HashMap<Long, MediaLineBean>();
		for (MediaLineBean mediaLineBean : lines) {
			MediaLineBean line = checkMap.get(mediaLineBean.getIdMedia());
			if(line!=null){
				cart.getLines().remove(line);
			}else{
				checkMap.put(mediaLineBean.getIdMedia(), mediaLineBean);
			}
			
			if(!mediaLineBean.getMedias().isEmpty()){
				for (MediaLineBean lineAlbum : mediaLineBean.getMedias()) {
					MediaLineBean lineA = checkMap.get(lineAlbum.getIdMedia());
					if(lineA!=null){
						cart.getLines().remove(lineA);
					}else{
						checkMap.put(lineAlbum.getIdMedia(), lineAlbum);
					}
				}
			}
		}
		recalculateAdNeeeded(cart);
	}*/
    
    private void recalculateAdNeeeded(CartBean cart){
    	Set<MediaLineBean> lines = cart.getLines();
    	Integer nb = 0;
    	Integer nbMedias = 0;
		for (MediaLineBean mediaLineBean : lines) {
			if(!mediaLineBean.getMedias().isEmpty()){
				Integer nbAlbum = 0;
				for (MediaLineBean albumLine : mediaLineBean.getMedias()) {
					nbAlbum+=albumLine.getAdNeeded();
					nbMedias++;
				}
				mediaLineBean.setAdNeeded(nbAlbum);
				nb+=nbAlbum;
			}else if(MediaLineBean.MUSIC_TYPE == mediaLineBean.getType()){
				nb+=mediaLineBean.getAdNeeded();
				nbMedias++;
			}
		}
		cart.setNbProduct(nbMedias);
		cart.setMinScore(nb);
    }

	@RequestMapping(value="/remove.json",method = RequestMethod.POST)
    public  @ResponseBody CartBean removeFromCart(@RequestParam Long idMedia,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	CartBean cart = getCart(request, response);
    	cart.setError("");
    	Set<MediaLineBean> lines = cart.getLines();
    	MediaLineBean toRemove = null;
		for (MediaLineBean mediaLineBean : lines) {
			if(mediaLineBean.getIdMedia().equals(idMedia)){
				toRemove = mediaLineBean;
			}
			if(!mediaLineBean.getMedias().isEmpty()){
				for (MediaLineBean albumLine : mediaLineBean.getMedias()) {
					if(albumLine.getIdMedia().equals(idMedia)){
						toRemove = albumLine;
					}
				}
			}
		}
		lines.remove(toRemove);
		recalculateAdNeeeded(cart);
    	return cart;
    }

    
    @RequestMapping(value="/getCart.json",method = RequestMethod.POST)
    public  @ResponseBody CartBean getCart(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	CartBean cart = (CartBean) request.getSession().getAttribute(CART);
    	if(cart==null){
    		cart = new CartBean();
    		request.getSession().setAttribute(CART,cart);
    	}
    	return cart;
    }

    @RequestMapping(value="/empty.json",method = RequestMethod.GET)
    public  @ResponseBody String empty(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute(CART);
		request.getSession().removeAttribute(JsonAdGameController.USER_ANSWER);
		request.getSession().removeAttribute(JsonAdGameController.CORRECT_ANSWER);
		request.getSession().removeAttribute(JsonAdGameController.USER_SCORE);
		request.getSession().removeAttribute(JsonAdGameController.TYPE_GAME);
		request.getSession().removeAttribute(JsonAdGameController.ID_ADGAME);
    	return null;
    }

}
