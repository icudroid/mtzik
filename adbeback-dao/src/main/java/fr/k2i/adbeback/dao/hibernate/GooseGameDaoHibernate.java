package fr.k2i.adbeback.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.goosegame.GooseGame;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;
import fr.k2i.adbeback.dao.GooseGameDao;

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
@Repository("gooseGameDao")
public class GooseGameDaoHibernate extends GenericDaoHibernate<GooseGame, Long> implements GooseGameDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public GooseGameDaoHibernate() {
        super(GooseGame.class);
    }

	public void addToLevel(GooseLevel level, Double value) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder query = new StringBuilder();
		query.append("UPDATE goose_level g SET value=value+");
		query.append(value);
		query.append(" where id = ");
		query.append(level.getId());
		session.createSQLQuery(query.toString()).executeUpdate();
	}

	public void resetLevelValue(GooseLevel level) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder query = new StringBuilder();
		query.append("UPDATE goose_level g SET value=0");
		query.append(" where id = ");
		query.append(level.getId());
		session.createSQLQuery(query.toString()).executeUpdate();
	}

	public GooseLevel getNextLevel(GooseLevel level) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GooseLevel.class);
		if(level==null){
			criteria.add(Restrictions.eq("level", 0L));
		}else{
			criteria.add(Restrictions.eq("level", level.getLevel()+1));
		}
		return (GooseLevel) criteria.uniqueResult();
	}


}

