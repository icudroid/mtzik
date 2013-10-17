package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.media.Media;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface MediaDao extends GenericDao<Media, Long> {

	/**
	 * Récupérer la liste des medias par rapport à ces ids
	 * @param mediasId
	 * @return
	 * @throws Exception
	 */
	List<Media> getMedias(List<Long> mediasId)throws Exception;

	/**
	 * Recherche
	 * @param str
	 * @param genreId 
	 * @return
	 * @throws Exception
	 */
	List<Media> find(String str, Long genreId)throws Exception;

	/**
	 * 
	 * @param idGenre
	 * @return
	 * @throws Exception
	 */
	List<Media> searchBestDownload(Long idGenre)throws Exception;

	/**
	 * 
	 * @param genreId
	 * @param search
	 * @param max
	 * @return
	 * @throws Exception
	 */
	List<Media> searchNew(Long genreId, String search, int max)throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Media> getHomeMedias()throws Exception;
    
}

