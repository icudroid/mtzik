package fr.k2i.adbeback.dao.hibernate;

import java.util.List;

import javax.persistence.Table;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.dao.PlayerDao;

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
@Repository("playerDao")
public class PlayerDaoHibernate extends GenericDaoHibernate<Player, Long> implements PlayerDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public PlayerDaoHibernate() {
        super(Player.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Player> getPlayer() {
        return getHibernateTemplate().find("from Player u order by upper(u.username)");
    }

    /**
     * {@inheritDoc}
     */
    public Player savePlayer(Player player) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + player.getId());
        }
        getHibernateTemplate().saveOrUpdate(player);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getHibernateTemplate().flush();
        return player;
    }

    /**
     * Overridden simply to call the saveUser method. This is happenening 
     * because saveUser flushes the session and saveObject of BaseDaoHibernate 
     * does not.
     *
     * @param player the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public Player save(Player player) {
        return this.savePlayer(player);
    }

    /** 
     * {@inheritDoc}
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List users = getHibernateTemplate().find("from Player where username=?", username);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    /** 
     * {@inheritDoc}
    */
    public String getPlayerPassword(String username) {
        SimpleJdbcTemplate jdbcTemplate =
                new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(Player.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where username=?", String.class, username);

    }

	public Player loadUserByEmail(String email) {
        List users = getHibernateTemplate().find("from Player where email=?", email);
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            return (Player) users.get(0);
        }
	}


}

