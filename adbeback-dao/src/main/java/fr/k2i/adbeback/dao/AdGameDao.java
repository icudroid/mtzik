package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.game.AdGame;
import fr.k2i.adbeback.core.business.game.AdGameMusic;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface AdGameDao extends GenericDao<AdGame, Long> {
	/**
	 * 
	 * @param idPlayLong
	 * @return
	 * @throws Exception
	 */
	List<AdGameMusic> findWonAdGameMusic(Long idPlayLong)throws Exception;
}

