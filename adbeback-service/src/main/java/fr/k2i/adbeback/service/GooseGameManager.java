package fr.k2i.adbeback.service;

import java.util.List;

import fr.k2i.adbeback.bean.CagnotteBean;
import fr.k2i.adbeback.core.business.goosegame.GooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseGame;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;
import fr.k2i.adbeback.core.business.goosegame.GooseWin;
import fr.k2i.adbeback.core.business.player.Player;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 */
public interface GooseGameManager extends GenericManager<GooseGame, Long> {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<CagnotteBean> getCagnottes()throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	GooseCase getCase(long id)throws Exception;

	/**
	 * 
	 * @param level
	 * @param startNumber
	 * @param nb
	 * @return
	 */
	List<GooseCase> getCases(GooseLevel level, Integer startNumber, int nb)throws Exception;

	/**
	 * 
	 * @param level
	 * @return
	 */
	GooseLevel getNextLevel(GooseLevel level)throws Exception;

	/**
	 * 
	 * @param num
	 * @param level
	 * @return
	 * @throws Exception
	 */
	GooseCase getCaseByNumber(Integer num, GooseLevel level)throws Exception;

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
	List<GooseCase> getLevelCases(GooseLevel level)throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<GooseWin> getLastWinners()throws Exception;
	
}
