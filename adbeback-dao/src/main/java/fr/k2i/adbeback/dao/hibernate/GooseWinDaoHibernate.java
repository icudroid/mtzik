package fr.k2i.adbeback.dao.hibernate;

import java.util.List;

import org.hibernate.classic.Session;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.goosegame.GooseWin;
import fr.k2i.adbeback.dao.GooseWinDao;

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
@Repository("gooseWinDao")
public class GooseWinDaoHibernate extends GenericDaoHibernate<GooseWin, Long> implements GooseWinDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public GooseWinDaoHibernate() {
        super(GooseWin.class);
    }

	public List<GooseWin> getLastWinners() throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(GooseWin.class)
		.addOrder(Property.forName("windate").desc())
		.setMaxResults(25).list();
	}

}

