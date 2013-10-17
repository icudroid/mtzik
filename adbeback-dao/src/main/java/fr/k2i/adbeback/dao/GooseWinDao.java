package fr.k2i.adbeback.dao;

import java.util.List;

import fr.k2i.adbeback.core.business.goosegame.GooseWin;
import fr.k2i.adbeback.core.business.player.Player;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface GooseWinDao extends GenericDao<GooseWin, Long> {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<GooseWin> getLastWinners()throws Exception;

}

