package fr.k2i.adbeback.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.k2i.adbeback.bean.CategorieBean;
import fr.k2i.adbeback.core.business.media.Genre;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.MusicGenre;
import fr.k2i.adbeback.dao.GenreDao;
import fr.k2i.adbeback.dao.MediaDao;
import fr.k2i.adbeback.service.GenreManager;


/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("genreManager")
public class GenreManagerImpl extends GenericManagerImpl<Genre, Long> implements GenreManager {
	private GenreDao genreDao;
	
    @Autowired
	public void setGenreDao(GenreDao genreDao) {
		this.genreDao = genreDao;
	}

	public List<CategorieBean> all(Class genreType)
			throws Exception {
		List<CategorieBean> res = new ArrayList<CategorieBean>();
		List<Genre> genres = genreDao.getAll(genreType);
		for (Genre genre : genres) {
			CategorieBean cat = new CategorieBean();
			cat.setId(genre.getId());
			cat.setTitle(genre.getGenre());
			res.add(cat);
		}
		return res;
	}



}
