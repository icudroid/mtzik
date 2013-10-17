package fr.k2i.adbeback.dao.hibernate;

import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.goosegame.GooseToken;
import fr.k2i.adbeback.dao.GooseTokenDao;

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
@Repository("gooseTokenDao")
public class GooseTokenDaoHibernate extends GenericDaoHibernate<GooseToken, Long> implements GooseTokenDao {

    /**
     * Constructor that sets the entity to User.class.
     */
    public GooseTokenDaoHibernate() {
        super(GooseToken.class);
    }


}

