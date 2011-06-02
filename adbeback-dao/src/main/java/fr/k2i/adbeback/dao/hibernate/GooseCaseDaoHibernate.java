package fr.k2i.adbeback.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.goosegame.GooseCase;
import fr.k2i.adbeback.core.business.goosegame.GooseLevel;
import fr.k2i.adbeback.dao.GooseCaseDao;

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
@Repository("gooseCaseDao")
public class GooseCaseDaoHibernate extends GenericDaoHibernate<GooseCase, Long> implements GooseCaseDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public GooseCaseDaoHibernate() {
        super(GooseCase.class);
    }

	public GooseCase getByNumber(Integer number,GooseLevel level) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		return (GooseCase) session.createCriteria(GooseCase.class)
						.add(Restrictions.eq("number", number))
						.add(Restrictions.eq("level",level))
						.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<GooseCase> get(GooseLevel level, Integer start, Integer nb)
			throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GooseCase.class);
		criteria.add(Restrictions.eq("level",level));
		List<Integer> nums = new ArrayList<Integer>();
		for (int i = 0; i < nb; i++) {
			nums.add(start+i);
		}
		criteria.add(Restrictions.in("number",nums));
		criteria.addOrder(Property.forName("number").asc());
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<GooseCase> getCases(GooseLevel level) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(GooseCase.class)
						.add(Restrictions.eq("level",level))
						.addOrder(Property.forName("number").asc())
						.list();
	}

	
}

