package fr.k2i.adbeback.webapp.controller.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.k2i.adbeback.bean.AdBean;
import fr.k2i.adbeback.bean.AdGameBean;
import fr.k2i.adbeback.bean.CartBean;
import fr.k2i.adbeback.bean.PossibilityBean;
import fr.k2i.adbeback.bean.ResponseAdGameBean;
import fr.k2i.adbeback.bean.StatusGame;
import fr.k2i.adbeback.core.business.game.AdChoise;
import fr.k2i.adbeback.core.business.game.AdGame;
import fr.k2i.adbeback.core.business.game.AdGameMusic;
import fr.k2i.adbeback.core.business.game.BrandPossibility;
import fr.k2i.adbeback.core.business.game.OpenPossibility;
import fr.k2i.adbeback.core.business.game.Possibility;
import fr.k2i.adbeback.core.business.game.ProductPossibility;
import fr.k2i.adbeback.core.business.goosegame.AddPotGooseCase;
import fr.k2i.adbeback.core.business.goosegame.DeadGooseCase;
import fr.k2i.adbeback.core.business.goosegame.EndLevelGooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;
import fr.k2i.adbeback.core.business.goosegame.GooseToken;
import fr.k2i.adbeback.core.business.goosegame.GooseWin;
import fr.k2i.adbeback.core.business.goosegame.JailGooseCase;
import fr.k2i.adbeback.core.business.goosegame.JumpGooseCase;
import fr.k2i.adbeback.core.business.goosegame.ReductionGooseCase;
import fr.k2i.adbeback.core.business.goosegame.StartLevelGooseCase;
import fr.k2i.adbeback.core.business.partener.Reduction;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.dao.GooseCaseDao;
import fr.k2i.adbeback.dao.GooseGameDao;
import fr.k2i.adbeback.service.AdGameManager;
import fr.k2i.adbeback.service.GooseGameManager;
import fr.k2i.adbeback.service.PlayerManager;
import fr.k2i.adbeback.webapp.bean.LimiteTimeAdGameBean;
import fr.k2i.adbeback.webapp.bean.PlayerGooseGame;
import fr.k2i.adbeback.webapp.controller.flv.FlvDownloadController;

/**
 * Controller to signup new users.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/json/adgame*")
public class JsonAdGameController {
	private AdGameManager adGameManager = null;

	private PlayerManager playerManager;

	private GooseGameManager gooseGameManager;
	
	
	private String flvPath;

	@Autowired
	public void setGooseGameManager(GooseGameManager gooseGameManager) {
		this.gooseGameManager = gooseGameManager;
	}

	@Autowired
	public void setFlvPath(String flvPath) {
		this.flvPath = flvPath;
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setAdGameManager(AdGameManager adGameManager) {
		this.adGameManager = adGameManager;
	}


	public static final String CORRECT_ANSWER = "correct";
	public static final String USER_ANSWER = "answer";
	public static final String USER_SCORE = "score";
	public static final String TYPE_GAME = "type";
	public static final String ID_ADGAME = "adGameId";
	public static final String NB_ERRORS = "errors";

	private static final String LIMITED_TIME_ADGAME = "TimeLimit";
	private static final String NO_LIMITED_TIME_ADGAME = "NoTimeLimit";

	@RequestMapping(value = "/create.json", method = RequestMethod.GET)
	public @ResponseBody
	AdGameBean create(@RequestParam String typeGame,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CartBean cart = (CartBean) request.getSession().getAttribute(
				JsonCartController.CART);
		Map<Integer, Long> correctResponse = new HashMap<Integer, Long>();
		SecurityContext ctx = SecurityContextHolder.getContext();
		Player player = null;

		if (ctx.getAuthentication().getPrincipal() instanceof String) {
			player = playerManager.getPlayerByUsername(request.getRemoteUser());
		} else {
			player = (Player) ctx.getAuthentication().getPrincipal();
		}
		AdGame generateAdGame = adGameManager.generate(typeGame, cart,
				correctResponse, player.getId());
		AdGameBean res = new AdGameBean();
		Map<Integer, AdChoise> choises = generateAdGame.getChoises();
		List<AdBean> game = new ArrayList<AdBean>();

		for (Entry<Integer, AdChoise> entry : choises.entrySet()) {
			Integer num = entry.getKey();
			AdChoise adChoise = entry.getValue();
			AdBean adBean = new AdBean();
			List<PossibilityBean> possibilities = new ArrayList<PossibilityBean>();

			for (Possibility possibility : adChoise.getPossiblities()) {
				PossibilityBean pb = new PossibilityBean();

				if (possibility instanceof BrandPossibility) {
					BrandPossibility p = (BrandPossibility) possibility;
					pb.setType(0);
					pb.setAnswer(p.getBrand().getLogo());
				}
				if (possibility instanceof ProductPossibility) {
					ProductPossibility p = (ProductPossibility) possibility;
					if (p.getProduct().getLogo() == null) {
						pb.setType(2);
						pb.setAnswer(p.getProduct().getName());
					} else {
						pb.setAnswer(p.getProduct().getLogo());
						pb.setType(1);
					}
				}
				if (possibility instanceof OpenPossibility) {
					OpenPossibility p = (OpenPossibility) possibility;
					pb.setType(3);
					pb.setAnswer(p.getAnswer());
				}

				pb.setId(possibility.getId());
				possibilities.add(pb);

			}
			adBean.setPossibilities(possibilities);
			adBean.setQuestion(adChoise.getQuestion());
			adBean.setUrl(adChoise.getCorrect().getAd().getVideo());

			correctResponse.put(num, adChoise.getCorrect().getId());
			game.add(adBean);
		}
		res.setGame(game);
		res.setMinScore(generateAdGame.getMinScore());
		res.setTimeLimite((long) (generateAdGame.getMinScore() * 20));
		res.setTotalAds(choises.size());

		Map<Integer, Long> answers = new HashMap<Integer, Long>();
		request.getSession().setAttribute(USER_ANSWER, answers);
		request.getSession().setAttribute(CORRECT_ANSWER, correctResponse);
		request.getSession().setAttribute(USER_SCORE, 0);
		request.getSession().setAttribute(NB_ERRORS, 0);
		request.getSession().setAttribute(TYPE_GAME, typeGame);
		request.getSession().setAttribute(ID_ADGAME, generateAdGame.getId());

		return res;
	}

	@RequestMapping(value = "/play.json", method = RequestMethod.GET)
	public @ResponseBody
	ResponseAdGameBean play(@RequestParam Integer num, @RequestParam Long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseAdGameBean res = new ResponseAdGameBean();
		Map<Integer, Long> correctResponse = (Map<Integer, Long>) request
				.getSession().getAttribute(CORRECT_ANSWER);
		Integer score = (Integer) request.getSession().getAttribute(USER_SCORE);
		Map<Integer, Long> answers = (Map<Integer, Long>) request.getSession()
				.getAttribute(USER_ANSWER);
		Long correctId = correctResponse.get(num);

		String typeGame = (String) request.getSession().getAttribute(TYPE_GAME);
		CartBean cart = (CartBean) request.getSession().getAttribute(
				JsonCartController.CART);
		Integer nbErrs = (Integer) request.getSession().getAttribute(NB_ERRORS);

		if (answers.get(num) == null && correctId.equals(id)) {
			res.setCorrect(true);
			score++;
			res.setScore(score);
			request.getSession().setAttribute(USER_SCORE, score);
			answers.put(num, id);
		} else {
			res.setCorrect(false);
			res.setScore(score);
			answers.put(num, -1L);
			nbErrs++;
			request.getSession().setAttribute(NB_ERRORS, nbErrs);
			if (nbErrs > 6) {
				res.setStatus(StatusGame.Lost);
				emptyGameSession(request);
				adGameManager.saveResponses((Long) request.getSession()
						.getAttribute(ID_ADGAME), score, answers);
				return res;
			}
		}

		if (typeGame.equals(NO_LIMITED_TIME_ADGAME)) {
			if (score.equals(cart.getMinScore())) {
				res.setStatus(StatusGame.Win);
				adGameManager.saveResponses((Long) request.getSession()
						.getAttribute(ID_ADGAME), score, answers);
			} else if (correctResponse.size() != num + 1) {
				res.setStatus(StatusGame.Playing);
			} else {
				res.setStatus(StatusGame.Lost);
				emptyGameSession(request);
				adGameManager.saveResponses((Long) request.getSession()
						.getAttribute(ID_ADGAME), score, answers);
			}
		} else {
			if (num < correctResponse.size()-1) {
				res.setStatus(StatusGame.Playing);
			} else {
				res.setStatus(StatusGame.WinLimitTime);
				adGameManager.saveResponses((Long) request.getSession()
						.getAttribute(ID_ADGAME), score, answers);
			}
		}

		return res;
	}

	@RequestMapping(value = "/noresponse.json", method = RequestMethod.GET)
	public @ResponseBody
	ResponseAdGameBean noResponse(@RequestParam Integer num,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseAdGameBean res = new ResponseAdGameBean();
		String typeGame = (String) request.getSession().getAttribute(TYPE_GAME);
		Integer score = (Integer) request.getSession().getAttribute(USER_SCORE);
		Map<Integer, Long> answers = (Map<Integer, Long>) request.getSession()
				.getAttribute(USER_ANSWER);
		CartBean cart = (CartBean) request.getSession().getAttribute(
				JsonCartController.CART);
		Map<Integer, Long> correctResponse = (Map<Integer, Long>) request
				.getSession().getAttribute(CORRECT_ANSWER);
		Integer nbErrs = (Integer) request.getSession().getAttribute(NB_ERRORS);

		answers.put(num, null);
			res.setCorrect(false);
			res.setScore(score);
			nbErrs++;
			request.getSession().setAttribute(USER_SCORE, score);
			request.getSession().setAttribute(NB_ERRORS, nbErrs);
			
		if (nbErrs > 6) {
			res.setStatus(StatusGame.Lost);
			emptyGameSession(request);
			adGameManager.saveResponses((Long) request.getSession()
					.getAttribute(ID_ADGAME), score, answers);
		} else if (score.equals(cart.getMinScore())&&NO_LIMITED_TIME_ADGAME.equals(typeGame)) {
			res.setStatus(StatusGame.Win);
			adGameManager.saveResponses((Long) request.getSession()
					.getAttribute(ID_ADGAME), score, answers);
		} else if (num+1 < correctResponse.size()) {
			res.setStatus(StatusGame.Playing);
		} else if(LIMITED_TIME_ADGAME.equals(typeGame)){
			res.setStatus(StatusGame.WinLimitTime);
			adGameManager.saveResponses((Long) request.getSession()
					.getAttribute(ID_ADGAME), score, answers);
		}else{
			res.setStatus(StatusGame.Lost);
			emptyGameSession(request);
			adGameManager.saveResponses((Long) request.getSession()
					.getAttribute(ID_ADGAME), score, answers);
		}
		
		return res;
	}

	@RequestMapping(value = "/getUnlockCode.json", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> getUnlockCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> res = new HashMap<String, String>();
		AdGame adGame = adGameManager.get((Long) request.getSession()
				.getAttribute(ID_ADGAME));
		if (fr.k2i.adbeback.core.business.game.StatusGame.Win.equals(adGame
				.getStatusGame())) {
			res.put("unlockCode", ((AdGameMusic) adGame).getUnlockCode());
		}
		emptyGameSession(request);
		return res;
	}

	private void emptyGameSession(HttpServletRequest request) {
		/*
		 * request.getSession().removeAttribute(USER_ANSWER);
		 * request.getSession().removeAttribute(CORRECT_ANSWER);
		 * request.getSession().removeAttribute(USER_SCORE);
		 * request.getSession().removeAttribute(ID_ADGAME);
		 */
	}

	@RequestMapping(value = "/nbParts/{title}.flv.json", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> getNbParts(@PathVariable String title) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		java.io.File file = new java.io.File(flvPath + title + ".flv");
		long length = file.length();
		long parts = (length / FlvDownloadController.BYTES_TO_SEND);
		res.put("parts", parts);
		return res;
	}

	@RequestMapping(value = "/getGooseGame.json", method = RequestMethod.GET)
	public @ResponseBody
	List<PlayerGooseGame> getGooseGame(HttpServletRequest request)
			throws Exception {
		List<PlayerGooseGame> res = new ArrayList<PlayerGooseGame>();
		SecurityContext ctx = SecurityContextHolder.getContext();
		Player player = null;

		if (ctx.getAuthentication().getPrincipal() instanceof String) {
			player = playerManager.getPlayerByUsername(request.getRemoteUser());
		} else {
			player = (Player) ctx.getAuthentication().getPrincipal();
			player = playerManager.get(player.getId());
		}
		GooseToken gooseToken = player.getGooseToken();
		if(gooseToken == null){
			gooseToken = new GooseToken();
			gooseToken.setGooseCase(gooseGameManager.getCase(1L));
			gooseToken.setPlayer(player);
			player.setGooseToken(gooseToken);
			playerManager.save(player);
		}
		GooseCase gooseCase = gooseToken.getGooseCase();
		Integer number = gooseCase.getNumber();
		GooseLevel level = gooseCase.getLevel();

		List<GooseCase> cases = gooseGameManager.getCases(level, number,  7);
		for (GooseCase c : cases) {
			Integer type = null;
			if (c instanceof AddPotGooseCase) {
				type = 5;
			}else if (c instanceof DeadGooseCase) {
				type = 4;
			}else if (c instanceof EndLevelGooseCase) {
				type = 3;
			}else if (c instanceof JumpGooseCase) {
				type = 1;
			}else if (c instanceof ReductionGooseCase) {
				type = 2;
			}else if (c instanceof StartLevelGooseCase) {
				type = 0;
			}else if (c instanceof JailGooseCase) {
				type = 6;
			}
			
			res.add(new PlayerGooseGame(c.getNumber().equals(number),c.getNumber(),type));
		}
		
		return res;
	}

	@RequestMapping(value = "/endLimitGame.json", method = RequestMethod.GET)
	public @ResponseBody
	LimiteTimeAdGameBean endLimitGame(HttpServletRequest request) throws Exception {
		LimiteTimeAdGameBean res = new LimiteTimeAdGameBean();
		String typeGame = (String) request.getSession().getAttribute(TYPE_GAME);
		Integer score = (Integer) request.getSession().getAttribute(USER_SCORE);
		Map<Integer, Long> answers = (Map<Integer, Long>) request.getSession()
				.getAttribute(USER_ANSWER);
		
		SecurityContext ctx = SecurityContextHolder.getContext();
		Player player = null;

		if (ctx.getAuthentication().getPrincipal() instanceof String) {
			player = playerManager.getPlayerByUsername(request.getRemoteUser());
		} else {
			player = (Player) ctx.getAuthentication().getPrincipal();
			player = playerManager.get(player.getId());
		}
		
		AdGame adGame = adGameManager.get((Long) request.getSession()
				.getAttribute(ID_ADGAME));
		if (LIMITED_TIME_ADGAME.equals(typeGame)) {
			
			if(adGame.getMinScore()<=score){
				adGameManager.saveResponses((Long) request.getSession()
						.getAttribute(ID_ADGAME), score, answers);
				//faire avancer le token
				int nbGo = score-adGame.getMinScore();
				GooseToken token = player.getGooseToken();
				GooseCase gooseCase = token.getGooseCase();
				GooseLevel level = gooseCase.getLevel();
				
				if (gooseCase instanceof EndLevelGooseCase) {
					level = gooseGameManager.getNextLevel(level);
					token.setGooseCase(level.getStartCase());
				}else if (gooseCase instanceof JailGooseCase && nbGo!=6) {
					StringBuilder sb = new StringBuilder();
					sb.append("Vous êtes en prison, pour sortir vous devez faire 6 points supplémentaires");
					res.setMessage(sb.toString());
					res.setStatus(StatusGame.WinLimitTime);
					res.setScore(score);
					return res;
				}

				
				if(nbGo>0){
					
					Integer endCase = level.getEndCase().getNumber();
					
					GooseCase byNumber = null;
					if(gooseCase.getNumber()+nbGo >endCase){
						byNumber = gooseGameManager.getCaseByNumber((2*endCase)-gooseCase.getNumber()-nbGo,level);
					}else{
						byNumber = gooseGameManager.getCaseByNumber(gooseCase.getNumber()+nbGo,level);
					}
					
					if (byNumber instanceof AddPotGooseCase) {
						AddPotGooseCase add = (AddPotGooseCase) byNumber;
						gooseGameManager.addToLevel(level,add.getValue());
						StringBuilder sb = new StringBuilder();
						sb.append("Vous venez d'ajouter ");
						sb.append(add.getValue());
						sb.append(" d'euros à la cagnotte.");
						res.setMessage(sb.toString());
					}else if (byNumber instanceof DeadGooseCase) {
						byNumber = level.getStartCase();
						res.setMessage("Vous allez à la case : "+byNumber.getNumber());
					}else if (byNumber instanceof EndLevelGooseCase) {
						StringBuilder sb = new StringBuilder();
						sb.append("Bravo vous venez de remporté la cagnotte d'une valeur de ");
						sb.append(level.getValue());
						sb.append(" euros");
						gooseGameManager.resetLevelValue(level);
						GooseWin win = new GooseWin();
						win.setGooseLevel(level);
						win.setValue(level.getValue());
						win.setWindate(new Date());
						win.setPlayer(player);
						player.getWins().add(win);
						playerManager.savePlayer(player);
						res.setMessage(sb.toString());
					}else if (byNumber instanceof JumpGooseCase) {
						JumpGooseCase jump = (JumpGooseCase) byNumber;
						byNumber = jump.getJumpTo();
						res.setMessage("Vous allez à la case : "+byNumber.getNumber());
					}else if (byNumber instanceof ReductionGooseCase) {
						ReductionGooseCase reduc = (ReductionGooseCase) byNumber;
						StringBuilder sb = new StringBuilder();
						sb.append("Vous venez de gagner un bon d'achat d'une valeur de ");
						Reduction reduction = reduc.getReduction();
						if(reduction.getPercentageValue()!=null){
							sb.append(reduction.getPercentageValue());
							sb.append(" %, chez ");
							sb.append(reduction.getPartener().getName());
						}else{
							sb.append(reduction.getPercentageValue());
							sb.append(" euros, chez ");
							sb.append(reduction.getPartener().getName());
						}
						res.setMessage(sb.toString());
					}else if (byNumber instanceof JailGooseCase) {
						StringBuilder sb = new StringBuilder();
						sb.append("Vous êtes en prison, pour sortir vous devez faire 6 points supplémentaires");
						res.setMessage(sb.toString());
					}
					
					token.setGooseCase(byNumber);
					playerManager.save(player);
				}
				res.setStatus(StatusGame.WinLimitTime);
			}else{
				res.setStatus(StatusGame.Lost);	
			}
			
			res.setScore(score);
		} 
		return res;
	}
}
