package fr.k2i.adbeback.dao.hibernate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.game.AdGame;
import fr.k2i.adbeback.core.business.game.AdGameMusic;
import fr.k2i.adbeback.core.business.game.StatusGame;
import fr.k2i.adbeback.dao.AdGameDao;

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
@Repository("adGameDao")
public class AdGameDaoHibernate extends GenericDaoHibernate<AdGame, Long> implements AdGameDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public AdGameDaoHibernate() {
        super(AdGame.class);
    }

	@SuppressWarnings("unchecked")
	public List<AdGameMusic> findWonAdGameMusic(Long idPlayer) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AdGameMusic.class);
		criteria.add(Restrictions.eq("player.id", idPlayer));
		criteria.add(Restrictions.eq("statusGame", StatusGame.Win));
		Calendar now = new GregorianCalendar();
		now.add(Calendar.DATE, -7);
		criteria.add(Restrictions.ge("generated", now.getTime()));
		criteria.addOrder(Property.forName("generated").desc() );
		return criteria.list();
	}


}

