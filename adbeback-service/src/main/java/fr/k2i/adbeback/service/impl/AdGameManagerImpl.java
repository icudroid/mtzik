package fr.k2i.adbeback.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.k2i.adbeback.bean.CartBean;
import fr.k2i.adbeback.bean.MediaLineBean;
import fr.k2i.adbeback.core.business.ad.Ad;
import fr.k2i.adbeback.core.business.ad.Brand;
import fr.k2i.adbeback.core.business.ad.Product;
import fr.k2i.adbeback.core.business.ad.rule.AdResponse;
import fr.k2i.adbeback.core.business.ad.rule.AdRule;
import fr.k2i.adbeback.core.business.ad.rule.BrandRule;
import fr.k2i.adbeback.core.business.ad.rule.OpenRule;
import fr.k2i.adbeback.core.business.ad.rule.ProductRule;
import fr.k2i.adbeback.core.business.game.AdChoise;
import fr.k2i.adbeback.core.business.game.AdGame;
import fr.k2i.adbeback.core.business.game.AdGameMusic;
import fr.k2i.adbeback.core.business.game.AdGameVideo;
import fr.k2i.adbeback.core.business.game.AdResponsePlayer;
import fr.k2i.adbeback.core.business.game.AdScore;
import fr.k2i.adbeback.core.business.game.BrandPossibility;
import fr.k2i.adbeback.core.business.game.OpenPossibility;
import fr.k2i.adbeback.core.business.game.Possibility;
import fr.k2i.adbeback.core.business.game.ProductPossibility;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.dao.AdDao;
import fr.k2i.adbeback.dao.AdGameDao;
import fr.k2i.adbeback.dao.BrandDao;
import fr.k2i.adbeback.dao.MediaDao;
import fr.k2i.adbeback.dao.PlayerDao;
import fr.k2i.adbeback.dao.PossibilityDao;
import fr.k2i.adbeback.service.AdGameManager;
import fr.k2i.tools.PasswordGenerator;

/**
 * Implementation of UserManager interface.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("adGameManager")
public class AdGameManagerImpl extends GenericManagerImpl<AdGame, Long>
		implements AdGameManager {

	private MediaDao mediaDao;

	private AdDao adDao;

	private BrandDao brandDao;
	
	private AdGameDao adGameDao;
	
	private PossibilityDao possibilityDao;
	
	private String musicPath;
	
	private String tmpPath;
	
	private PlayerDao playerDao;
	
	@Autowired
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Autowired
	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

	@Autowired
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	
	@Autowired
	public void setBrandDao(BrandDao brandDao) {
		this.brandDao = brandDao;
	}

	@Autowired
	public void setAdDao(AdDao adDao) {
		this.adDao = adDao;
	}

	@Autowired
	public void setMediaDao(MediaDao mediaDao) {
		this.mediaDao = mediaDao;
	}

	@Autowired
	public void setAdGameDao(AdGameDao adGameDao) {
		this.dao = adGameDao;
		this.adGameDao = adGameDao;
	}

	@Autowired
	public void setPossibilityDao(PossibilityDao possibilityDao) {
		this.possibilityDao = possibilityDao;
	}

	public AdGame generate(String typeGame, CartBean cart,
			Map<Integer, Long> correctResponse,Long idPlayer) throws Exception {
		List<Long> mediasId = new ArrayList<Long>();
		Set<MediaLineBean> lines = cart.getLines();
		for (MediaLineBean mediaLineBean : lines) {
			if(mediaLineBean.getType()==MediaLineBean.MUSIC_TYPE){
				mediasId.add(mediaLineBean.getIdMedia());	
			}else{
				for (MediaLineBean  musicAlbum: mediaLineBean.getMedias()) {
					mediasId.add(musicAlbum.getIdMedia());	
				}
			}
		}
		return  generate(mediasId, idPlayer);
		
	}
	
	public AdGame generate(List<Long> mediasId,Long idPlayer) throws Exception {
		List<Media> medias = mediaDao.getMedias(mediasId);
		AdGame game = null;
		if (medias.get(0) instanceof Music) {
			game = new AdGameMusic();
			((AdGameMusic) game).setUnlockCode(PasswordGenerator.getRandomString(16));
		} else {
			game = new AdGameVideo();
		}
		Integer nbAds = 0;

		for (Media media : medias) {
			nbAds += media.getNbAdsNeeded();
		}
		game.setMinScore(nbAds);
		nbAds += 6;

		game.setGenerated(new Date());
		game.setMedias(medias);

		AdScore score = new AdScore();
		score.setScore(0);
		game.setScore(score);

		game.setChoises(generateChoises(nbAds, game));
		Player player = playerDao.get(idPlayer);
		game.setPlayer(player);
		return adGameDao.save(game);
	}

	private Map<Integer, AdChoise> generateChoises(Integer nbAds, AdGame game) throws Exception {
		Map<Integer, AdChoise> res = new HashMap<Integer, AdChoise>();

		Map<Integer, Ad> mapTest = new HashMap<Integer, Ad>();
		List<Ad> allAds = adDao.getAll(new Date());
		Random ramRandom = new Random();

		int i = 0;
		while (i <= nbAds-1) {
			int nextInt = ramRandom.nextInt(allAds.size());
			Ad ad = mapTest.get(nextInt);
			if (ad == null) {
				ad = allAds.get(nextInt);
				mapTest.put(nextInt, ad);

				AdChoise choises = new AdChoise();
				int correct = ramRandom.nextInt(3);
				
				List<AdRule> rules = ad.getRules();
				AdRule rule = null;
				Date now = new Date();
				
				for (AdRule adRule : rules) {
					if(now.after(adRule.getStartDate()) && now.before(adRule.getEndDate()) ){
						rule = adRule;
						break;
					}
				}
				choises.setQuestion(rule.getQuestion());
				choises.setPossiblities(generatePossibilies(ad, correct,rule));
				choises.setCorrect(choises.getPossiblities().get(correct));
				choises.setNumber(i);
				res.put(i, choises);

				i++;
			}
		}

		return res;
	}

	private List<Possibility> generatePossibilies(Ad ad, int correct, AdRule rule) {
		List<Possibility> possibilities = new ArrayList<Possibility>();
		Random ramRandom = new Random();
		
		if (rule instanceof BrandRule) {
			List<Brand> brands = brandDao.getAll();
			int b = brands.indexOf(ad.getBrand());

			Set<Integer> answers = new HashSet<Integer>();
			answers.add(b);

			for (int i = 0; i < 3; i++) {
				BrandPossibility bp = new BrandPossibility();
				bp.setAd(ad);
				if (correct == i) {
					bp.setBrand(ad.getBrand());
				} else {
					int ramdom;
					do {
						ramdom = ramRandom.nextInt(brands.size());
					} while (answers.contains(ramdom));
					answers.add(ramdom);
					bp.setBrand(brands.get(ramdom));
				}
				possibilities.add(bp);
			}
		}else if (rule instanceof ProductRule) {
			List<Product> products = ad.getBrand().getProducts();
			int p = products.indexOf(ad.getProduct());

			Set<Integer> answers = new HashSet<Integer>();
			answers.add(p);

			for (int i = 0; i < 3; i++) {
				ProductPossibility prp = new ProductPossibility();
				prp.setAd(ad);
				if (correct == i) {
					prp.setProduct(ad.getProduct());
				} else {
					// choisir un produit aléatoire
					int ramdom;
					do {
						ramdom = ramRandom.nextInt(products.size());
					} while (answers.contains(ramdom));
					answers.add(ramdom);
					prp.setProduct(products.get(ramdom));
				}
				possibilities.add(prp);
			}
		}else if (rule instanceof OpenRule) {
			OpenRule or = (OpenRule) rule;
			List<AdResponse> responses = or.getResponses();
			int b = responses.indexOf(or.getCorrect());

			Set<Integer> answers = new HashSet<Integer>();
			answers.add(b);

			for (int i = 0; i < 3; i++) {
				OpenPossibility pp = new OpenPossibility();
				pp.setAd(ad);
				if (correct == i) {
					pp.setAnswer(or.getCorrect().getResponse());
				} else {
					int ramdom;
					do {
						ramdom = ramRandom.nextInt(responses.size());
					} while (answers.contains(ramdom));
					answers.add(ramdom);
					pp.setAnswer(responses.get(ramdom).getResponse());
				}
				possibilities.add(pp);
			}
		}

		return possibilities;
	}

	public String getUnlockCode(Long idAdGame) throws Exception {
		AdGame adGame = dao.get(idAdGame);
		if (adGame instanceof AdGameMusic) {
			AdGameMusic adM = (AdGameMusic) adGame;
			if (adM.getScore().getScore() >= adM.getMinScore()) {
				return adM.getUnlockCode();
			} else {
				return null;
			}
		}
		return null;
	}


	public String getMedias(Long idAdGame, HttpServletResponse response) throws Exception {
		
		AdGame adGame = dao.get(idAdGame);
		if (adGame instanceof AdGameMusic) {
			AdGameMusic adM = (AdGameMusic) adGame;
			// generation unlockcode
			String password = adM.getUnlockCode();
			
			// generation archive
			
			List<Media> medias = adGame.getMedias();
			String zipFileName = tmpPath+idAdGame+".zip";
			ZipFile zipFile;
			try {
				zipFile = new ZipFile(zipFileName);
				ArrayList<File> filesToAdd = new ArrayList<File> ();
				
				for (Media media : medias) {
					filesToAdd.add( new File(musicPath+media.getFile()));
				}

				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to store compression
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(password);
				zipFile.createZipFile(filesToAdd, parameters);
			} catch (ZipException e) {
				e.printStackTrace();
			}
			
			java.io.File file = new java.io.File(zipFileName);
			
			response.setContentType("application/zip");	
			response.setContentLength((int) file.length());
			ServletOutputStream outputStream = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(file);
			int read =0;
			byte []b = new byte[1024];
			while((read = fileInputStream.read(b, 0, 1024))>0){
				outputStream.write(b, 0, read);
				b = new byte[1024];
			}
			fileInputStream.close();
			
			file.delete();
			return null;
		} else {
			AdGameVideo adV = (AdGameVideo) adGame;
			return adV.getMedias().get(0).getFile();
		}
		
	}

	public void saveResponses(Long idAdGame, Integer score,
			Map<Integer, Long> answers) throws Exception {
		AdGame adGame = adGameDao.get(idAdGame);
		AdScore adScore = new AdScore();
		adScore.setScore(score);
		Map<Integer, AdResponsePlayer> answersPlayer = new HashMap<Integer, AdResponsePlayer>();
		for (Entry<Integer, Long> answer : answers.entrySet()) {
			AdResponsePlayer r = new AdResponsePlayer();
			r.setAdScore(adScore);
			r.setNumber(answer.getKey());
			if(answer.getValue()!=null && answer.getValue()!=-1){
				r.setResponse(possibilityDao.get(answer.getValue()));
			}
			answersPlayer.put(answer.getKey(), r);
		}
		adScore.setAnswers(answersPlayer);
		adGame.setScore(adScore);
		
		if(score>=adGame.getMinScore()){
			adGame.setStatusGame(fr.k2i.adbeback.core.business.game.StatusGame.Win);	
		}else{
			adGame.setStatusGame(fr.k2i.adbeback.core.business.game.StatusGame.Lost);
		}
		
		adGameDao.save(adGame);
	}

	public List<AdGameMusic> findWonAdGameMusicInfSevenDays(Long idPlayer)
			throws Exception {
		return adGameDao.findWonAdGameMusic(idPlayer);
	}

	public void getMediasEnc(Long idAdGame, HttpServletResponse response)
			throws Exception {
		AdGame adGame = dao.get(idAdGame);
		if (adGame instanceof AdGameMusic) {
			AdGameMusic adM = (AdGameMusic) adGame;
			// generation unlockcode
			String password = adM.getUnlockCode();
			
			// generation archive
			File fenc = new File(tmpPath+idAdGame+".zip");
			FileOutputStream baout = new FileOutputStream(fenc);
			List<Media> medias = adGame.getMedias();
			ZipOutputStream out = new ZipOutputStream(baout);
			
			for (Media media : medias) {
				out.putNextEntry(new ZipEntry(media.getTitle()+".mp3"));
				File ftoz = new File(musicPath+media.getFile());
				FileInputStream fi = new  FileInputStream(ftoz);
				byte[] b = new byte[(int) ftoz.length()];
				fi.read(b);
				out.write(b);
			}
			
			out.close();
			
			KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
			keyGen.init(128);
			SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
			String iv = "00000000";
			IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivs);
			
			FileInputStream in = new FileInputStream(fenc);
			byte[] bzp = new byte[(int) fenc.length()];
			in.read(bzp);
			in.close();
			fenc.delete();
			byte[] bout =  cipher.doFinal(bzp);
			

			response.setContentType("application/zip");	
			response.setContentLength(bout.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bout);
		} 
		
	}


}
