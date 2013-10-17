package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.media.Artist;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface ArtistDao extends GenericDao<Artist, Long> {

	/**
	 * 
	 * @param name
	 * @param max
	 * @return
	 * @throws Exception
	 */
	List<Artist> find(String name,int max)throws Exception;

    
}

