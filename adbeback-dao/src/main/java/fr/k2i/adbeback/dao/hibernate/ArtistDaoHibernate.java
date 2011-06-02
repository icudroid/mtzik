package fr.k2i.adbeback.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.ad.Ad;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.dao.AdDao;
import fr.k2i.adbeback.dao.ArtistDao;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler</a> Extended to
 *         implement Acegi UserDetailsService interface by David Carter
 *         david@carter.net Modified by <a href="mailto:bwnoll@gmail.com">Bryan
 *         Noll</a> to work with the new BaseDaoHibernate implementation that
 *         uses generics.
 */
@Repository("artistDao")
public class ArtistDaoHibernate extends GenericDaoHibernate<Artist, Long>
		implements ArtistDao {

	/**
	 * Constructor that sets the entity to User.class.
	 */
	public ArtistDaoHibernate() {
		super(Artist.class);
	}

	public List<Artist> find(String name,int max) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Artist.class);

		if (name != null) {
			String[] split = name.split(" ");
			for (String search : split) {
				Criterion rl = Restrictions.ilike("firstName", search,
						MatchMode.ANYWHERE);
				Criterion rf = Restrictions.ilike("lastName", search,
						MatchMode.ANYWHERE);
				criteria.add(Restrictions.or(rf, rl));
			}
		}
		
		if(max>0)criteria.setMaxResults(max);

		return criteria.list();
	}

}
