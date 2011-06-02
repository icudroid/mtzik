package fr.k2i.adbeback.service;

import java.util.List;

import fr.k2i.adbeback.bean.CategorieBean;
import fr.k2i.adbeback.core.business.media.Genre;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 */
public interface GenreManager extends GenericManager<Genre, Long> {

	List<CategorieBean> all(Class genreType)throws Exception;
}
