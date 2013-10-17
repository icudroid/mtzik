package fr.k2i.adbeback.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import fr.k2i.adbeback.core.business.home.Home;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.player.Player;
import fr.k2i.adbeback.dao.MediaDao;

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
@Repository("mediaDao")
public class MediaDaoHibernate extends GenericDaoHibernate<Media, Long>
		implements MediaDao {

	/**
	 * Constructor that sets the entity to User.class.
	 */
	public MediaDaoHibernate() {
		super(Media.class);
	}

	@SuppressWarnings("unchecked")
	public List<Media> getMedias(List<Long> mediasId) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(Media.class)
				.add(Restrictions.in("id", mediasId)).list();
	}

	@SuppressWarnings("unchecked")
	public List<Media> find(String str, Long idGenre) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Music.class);
		
		if(str!=null){
			criteria.createAlias("artists", "artist");
			String[] split = str.split(" ");
			for (String search : split) {
				Criterion rl = Restrictions.ilike("artist.firstName", search,
						MatchMode.ANYWHERE);
				Criterion rf = Restrictions.ilike("artist.lastName", search,
						MatchMode.ANYWHERE);
				LogicalExpression or = Restrictions.or(rf, rl);
				criteria.add(Restrictions.or(
						Restrictions.ilike("title", search, MatchMode.ANYWHERE), or));
			}
			criteria.addOrder(Property.forName("title").desc() );
			criteria.addOrder(Property.forName("artist.firstName").desc() );
			criteria.addOrder(Property.forName("artist.lastName").desc() );
		}

		if (idGenre != null && idGenre != -1) {
			criteria.createAlias("genres", "genre");
			criteria.add(Restrictions.eq("genre.id", idGenre));
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Media> searchBestDownload(Long idGenre) throws Exception {

		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(
				SessionFactoryUtils.getDataSource(getSessionFactory()));
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("SELECT media_id,count(media_id) FROM adgame_media a group by media_id ORDER BY COUNT(media_id) DESC limit 0,25");

		List<Long> idMedias = new ArrayList<Long>();
		
		for (Map<String, Object> map : queryForList) {
			idMedias.add((Long) map.get("media_id"));
		}
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Music.class);
		if (idGenre != null && idGenre != -1) {
			criteria.createAlias("genres", "genre");
			criteria.add(Restrictions.eq("genre.id", idGenre));
		}
		if(idMedias.size()>0){
			criteria.add(Restrictions.in("id", idMedias));
		}
		
		criteria.addOrder(Property.forName("title").desc() );
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Media> searchNew(Long idGenre, String str, int max)
			throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Music.class);
		if (idGenre != null && idGenre != -1) {
			criteria.createAlias("genres", "genre");
			criteria.add(Restrictions.eq("genre.id", idGenre));
		}
		
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -7);
		
		criteria.add(Restrictions.ge("releaseDate",cal.getTime()));
		criteria.addOrder(Property.forName("releaseDate").desc() );
		if(str!=null && !str.isEmpty()){
			criteria.createAlias("artists", "artist");
			String[] split = str.split(" ");
			for (String search : split) {
				Criterion rl = Restrictions.ilike("artist.firstName", search,
						MatchMode.ANYWHERE);
				Criterion rf = Restrictions.ilike("artist.lastName", search,
						MatchMode.ANYWHERE);
				LogicalExpression or = Restrictions.or(rf, rl);
				criteria.add(Restrictions.or(
						Restrictions.ilike("title", search, MatchMode.ANYWHERE), or));
			}
			criteria.addOrder(Property.forName("title").desc() );
			criteria.addOrder(Property.forName("artist.firstName").desc() );
			criteria.addOrder(Property.forName("artist.lastName").desc() );
		}
		criteria.setMaxResults(max);
		return criteria.list();
	}

	public List<Media> getHomeMedias() throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		Criteria createCriteria = session.createCriteria(Home.class);
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		Date start = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date end = cal.getTime();
		createCriteria.add(Restrictions.and(Restrictions.ge("drawDate", start),
				Restrictions.lt("drawDate", end)));
		Home home = (Home) createCriteria.uniqueResult();
		if(home==null){
			return searchBestDownload(-1L);
		}
		return home.getMedias();
	}

}
