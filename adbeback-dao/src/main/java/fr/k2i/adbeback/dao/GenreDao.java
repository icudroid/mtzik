package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.media.Genre;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface GenreDao extends GenericDao<Genre, Long> {

	List<Genre> getAll(Class<Genre> genreType)throws Exception;

    
}

