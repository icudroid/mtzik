package fr.k2i.adbeback.dao.hibernate;

import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.game.Possibility;
import fr.k2i.adbeback.dao.PossibilityDao;

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
@Repository("possibilityDao")
public class PossibilityDaoHibernate extends GenericDaoHibernate<Possibility, Long> implements PossibilityDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public PossibilityDaoHibernate() {
        super(Possibility.class);
    }


}

