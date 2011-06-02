package fr.k2i.adbeback.dao.hibernate;

import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.country.Country;
import fr.k2i.adbeback.dao.CountryDao;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
@Repository("countryDao")
public class CountryDaoHibernate extends GenericDaoHibernate<Country, Long> implements CountryDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public CountryDaoHibernate() {
        super(Country.class);
    }

	public Country getByCode(String code) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		return (Country) session.createCriteria(Country.class)
				.add(Restrictions.eq("code", code))
				.uniqueResult();

	}


}

