package fr.k2i.adbeback.core.business.home;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.media.Media;

@Entity
@Table(name = "home")
public class Home extends BaseObject implements Serializable {

	private static final long serialVersionUID = 6711696472191272787L;
	private Long id;
	private List<Media> medias;
	private Date drawDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToMany(targetEntity = Media.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinTable(name = "home_media", joinColumns = @JoinColumn(name = "HOME_ID"), inverseJoinColumns = @JoinColumn(name = "MEDIA_ID"))
	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	@Temporal(TemporalType.DATE)
	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((drawDate == null) ? 0 : drawDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Home other = (Home) obj;
		if (drawDate == null) {
			if (other.drawDate != null)
				return false;
		} else if (!drawDate.equals(other.drawDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Home [id=" + id + ", medias=" + medias + ", drawDate="
				+ drawDate + "]";
	}

}
