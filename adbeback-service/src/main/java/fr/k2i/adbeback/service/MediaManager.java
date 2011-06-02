package fr.k2i.adbeback.service;

import java.util.List;

import fr.k2i.adbeback.bean.MediaBean;
import fr.k2i.adbeback.bean.MediaLineBean;
import fr.k2i.adbeback.bean.SearchBean;
import fr.k2i.adbeback.bean.SearchResponseBean;
import fr.k2i.adbeback.core.business.media.Media;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 */
public interface MediaManager extends GenericManager<Media, Long> {
	/**
	 * Recherche des medias
	 * @param str
	 * @param genreId 
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> find(SearchBean searchBean) throws Exception;
	
	/**
	 * 
	 * @param str
	 * @param genreId
	 * @return
	 * @throws Exception
	 */
	List<Media> find(String str, Long genreId) throws Exception;
	
	/**
	 * 
	 * @param idGenre
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> searchBestDownload(Long idGenre) throws Exception;

	/**
	 * 
	 * @param maxResult
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> getNewProducts(Integer maxResult)throws Exception;

	/**
	 * 
	 * @param idGenre
	 * @param maxResult
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> getNewProducts(Long idGenre, Integer maxResult)throws Exception;

	/**
	 * 
	 * @param searchBean
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> getNewProducts(SearchBean searchBean)throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<MediaBean> getHomeMedias()throws Exception;

	/**
	 * 
	 * @param idMedia
	 * @return
	 * @throws Exception
	 */
	MediaLineBean getMediaLineBean(Long idMedia)throws Exception;
	
	/**
	 * 
	 * @param searchBean
	 * @return
	 * @throws Exception
	 */
	List<SearchResponseBean>  findForAutoComplete(SearchBean searchBean)throws Exception;
}
