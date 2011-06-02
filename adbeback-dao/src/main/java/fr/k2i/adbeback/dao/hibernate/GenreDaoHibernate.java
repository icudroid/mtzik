package fr.k2i.adbeback.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.ad.Ad;
import fr.k2i.adbeback.core.business.media.Genre;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.dao.GenreDao;

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
@Repository("genreDao")
public class GenreDaoHibernate extends GenericDaoHibernate<Genre, Long> implements GenreDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public GenreDaoHibernate() {
        super(Genre.class);
    }

	@SuppressWarnings("unchecked")
	public List<Genre> getAll(Class<Genre> genreType) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(genreType);
		return criteria.list();
	}


}

