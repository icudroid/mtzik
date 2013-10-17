package fr.k2i.adbeback.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import fr.k2i.adbeback.bean.CartBean;
import fr.k2i.adbeback.core.business.game.AdGame;
import fr.k2i.adbeback.core.business.game.AdGameMusic;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 */
public interface AdGameManager extends GenericManager<AdGame, Long> {
	/**
	 * Generation du jeu	
	 * @param mediasId
	 * @return
	 * @throws Exception
	 */
	AdGame generate(List<Long>mediasId,Long idPlayer)throws Exception;
	
	/**
	 * Donner le code de dévérouillage
	 * @param idAdGame
	 * @return
	 * @throws Exception
	 */
	String getUnlockCode(Long idAdGame)throws Exception;
	
	/**
	 * Génération du jeu
	 * @param typeGame
	 * @param cart
	 * @param correctResponse
	 * @return
	 * @throws Exception
	 */
	AdGame generate(String typeGame, CartBean cart,
			Map<Integer, Long> correctResponse,Long idPlayer)throws Exception;
	
	/**
	 * 
	 * @param idAdGame
	 * @param response
	 * @return
	 * @throws Exception
	 */
	String getMedias(Long idAdGame,HttpServletResponse response) throws Exception;

	/**
	 * 
	 * @param idAdGame
	 * @param score
	 * @param answers
	 * @throws Exception
	 */
	void saveResponses(Long idAdGame, Integer score, Map<Integer, Long> answers)throws Exception;
	
	/**
	 *  
	 * @param idPlayer
	 * @return
	 * @throws Exception
	 */
	List<AdGameMusic> findWonAdGameMusicInfSevenDays(Long idPlayer) throws Exception;

	/**
	 * 
	 * @param idGame
	 * @param response
	 * @throws Exception
	 */
	void getMediasEnc(Long idGame, HttpServletResponse response) throws Exception;

}
