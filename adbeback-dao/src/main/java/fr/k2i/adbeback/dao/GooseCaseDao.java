package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.goosegame.GooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface GooseCaseDao extends GenericDao<GooseCase, Long> {


	/**
	 * 
	 * @param level
	 * @param start
	 * @param nb
	 * @return
	 * @throws Exception
	 */
	List<GooseCase> get(GooseLevel level, Integer start, Integer nb)throws Exception;

	/**
	 * 
	 * @param i
	 * @param level
	 * @throws Exception
	 */
	GooseCase getByNumber(Integer i, GooseLevel level)throws Exception;

	/**
	 * 
	 * @param level
	 * @return
	 * @throws Exception
	 */
	List<GooseCase> getCases(GooseLevel level)throws Exception;

    
}

