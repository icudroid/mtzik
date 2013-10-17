package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.country.Country;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface CountryDao extends GenericDao<Country, Long> {

	Country getByCode(String code)throws Exception;
    
}

