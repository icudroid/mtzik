package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.goosegame.GooseGame;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface GooseGameDao extends GenericDao<GooseGame, Long> {

	/**
	 * 
	 * @param level
	 * @param value
	 * @throws Exception
	 */
	void addToLevel(GooseLevel level, Double value)throws Exception;

	/**
	 * 
	 * @param level
	 * @throws Exception
	 */
	void resetLevelValue(GooseLevel level)throws Exception;

	/**
	 * 
	 * @param level
	 * @return
	 * @throws Exception
	 */
	GooseLevel getNextLevel(GooseLevel level)throws Exception;

    
}

