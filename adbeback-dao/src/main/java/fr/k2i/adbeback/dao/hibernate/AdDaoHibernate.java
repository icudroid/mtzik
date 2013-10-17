package fr.k2i.adbeback.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.ad.Ad;
import fr.k2i.adbeback.dao.AdDao;

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
@Repository("adDao")
public class AdDaoHibernate extends GenericDaoHibernate<Ad, Long> implements AdDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public AdDaoHibernate() {
        super(Ad.class);
    }

	public List<Ad> getAll(Date date) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Ad.class);
		Criteria createAlias = criteria.createAlias("rules", "rule");
		createAlias.add(Restrictions.and(Restrictions.le("rule.startDate", date), Restrictions.gt("rule.endDate", date)));
		return criteria.list();
	}


}

