package fr.k2i.adbeback.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.k2i.adbeback.bean.MediaBean;
import fr.k2i.adbeback.bean.MediaLineBean;
import fr.k2i.adbeback.bean.PersonBean;
import fr.k2i.adbeback.bean.SearchBean;
import fr.k2i.adbeback.bean.SearchResponseBean;
import fr.k2i.adbeback.core.business.media.Album;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.dao.ArtistDao;
import fr.k2i.adbeback.dao.MediaDao;
import fr.k2i.adbeback.service.MediaManager;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("mediaManager")
public class MediaManagerImpl extends GenericManagerImpl<Media, Long> implements MediaManager {
	private static final int MAX_RESPONSE_ARTIST = 5;
	private static final int MAX_RESPONSE_MEDIA = 10;
	
	private MediaDao mediaDao;
	private ArtistDao artistDao;
	
	@Autowired
    public void setArtistDao(ArtistDao artistDao) {
		this.artistDao = artistDao;
	}

	@Autowired
    public void setMediaDao(MediaDao mediaDao) {
        this.dao = mediaDao;
        this.mediaDao = mediaDao;
    }

    private List<MediaBean> construstBeanList(List<Media> medias)throws Exception {
    	List<MediaBean> res = new ArrayList<MediaBean>();
		for (Media media : medias) {
			MediaBean bean = new MediaBean();
			bean.setDescription(media.getDescription());
			bean.setDuration(media.getDuration());
			bean.setGenres(media.getGenres());
			bean.setId(media.getId());
			bean.setJacket(media.getJacket());
			bean.setReleaseDate(media.getReleaseDate());
			bean.setThumbJacket(media.getThumbJacket());
			bean.setTitle(media.getTitle());
			
			List<PersonBean> productors = new ArrayList<PersonBean>();
			for (Productor p : media.getProductors()) {
				productors.add(new PersonBean(p.getFirstName(), p.getLastName()));
			}
			bean.setProductors(productors);
			
			if (media instanceof Music) {
				Music music = (Music) media;
				List<PersonBean> artists = new ArrayList<PersonBean>();
				for (Artist a : music.getArtists()) {
					artists.add(new PersonBean(a.getFirstName(), a.getLastName()));
				}

				bean.setArtists(artists);
			}
			res.add(bean);
		}
		
		return res;
    }
    
	public List<MediaBean> searchBestDownload(Long idGenre) throws Exception {
		List<Media> medias = mediaDao.searchBestDownload(idGenre);
		return construstBeanList(medias);
	}

	public List<MediaBean> getNewProducts(Integer maxResult) throws Exception {
		return getNewProducts(-1L,maxResult);
	}

	public List<MediaBean> getNewProducts(Long idGenre, Integer maxResult)
			throws Exception {
		return getNewProducts(new SearchBean(idGenre,""));
	}

	public List<MediaBean> getNewProducts(SearchBean searchBean)
			throws Exception {
		List<Media> medias = mediaDao.searchNew(searchBean.getGenreId(),searchBean.getSearch(),25);
		return construstBeanList(medias);
	}

	public List<MediaBean> find(SearchBean searchBean) throws Exception {
		return construstBeanList(mediaDao.find(searchBean.getSearch(),searchBean.getGenreId())); 
	}

	public List<MediaBean> getHomeMedias() throws Exception {
		List<Media> medias = mediaDao.getHomeMedias();
		return construstBeanList(medias);
	}

	public MediaLineBean getMediaLineBean(Long idMedia) throws Exception {
		Media media = mediaDao.get(idMedia);
		
    	MediaLineBean line = new MediaLineBean();
    	
    	line.setIdMedia(idMedia);
    	line.setTitle(media.getTitle());
    	
    	if (media instanceof Album) {
			Album album = (Album) media;
			line.setType(MediaLineBean.ALBUM_TYPE);	
			List<Music> musics = album.getMusics();
			Integer nbAds = 0;
			for (Music music : musics) {
				MediaLineBean lineMusic = new MediaLineBean();
				lineMusic.setAdNeeded(music.getNbAdsNeeded());
				lineMusic.setIdMedia(music.getId());
				lineMusic.setTitle(music.getTitle());
				lineMusic.setType(MediaLineBean.MUSIC_TYPE);
				nbAds+=music.getNbAdsNeeded();
				line.getMedias().add(lineMusic);
			}
			line.setAdNeeded(media.getNbAdsNeeded());
		}else if (media instanceof Music) {
			line.setType(MediaLineBean.MUSIC_TYPE);
			line.setAdNeeded(media.getNbAdsNeeded());
		}
    	
    	
		return line;
	}

	public List<Media> find(String str, Long genreId) throws Exception {
		return mediaDao.find(str,genreId);
	}

	public List<SearchResponseBean> findForAutoComplete(SearchBean searchBean) throws Exception {
		List<SearchResponseBean>  res = new ArrayList<SearchResponseBean>();
		
		List<Artist> artists = artistDao.find(searchBean.getSearch(),MAX_RESPONSE_ARTIST);
		for (Artist artist : artists) {
			SearchResponseBean sr = new SearchResponseBean();
			sr.setFullName((artist.getFirstName()+" "+artist.getLastName()).trim());
			res.add(sr);
		}
		List<Media> media = mediaDao.find(searchBean.getSearch(),-1L);
		for (Media m : media) {
			SearchResponseBean sr = new SearchResponseBean();
			List<Artist> as = ((Music)m).getArtists();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < as.size(); i++) {
				Artist a = as.get(i);
				if(i!=0)sb.append(", ");
				sb.append((a.getFirstName()+" "+a.getLastName()).trim());
			}
			sr.setFullName(sb.toString());
			sr.setTitle(m.getTitle());
			res.add(sr);
		}
		return res;
	}


}
