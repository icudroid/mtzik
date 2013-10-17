package fr.k2i.adbeback.core.business.media;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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


@Entity
@Table(name = "viewed_media")
public class ViewedMedia extends BaseObject implements Serializable {
	private static final long serialVersionUID = -925714084631112751L;
	private Long id;
	private List<Media> medias;
	private Date viewed;
	private Boolean won;

//	@SequenceGenerator(name = "ViewedMedia_Gen", sequenceName = "ViewedMedia_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ViewedMedia_Gen")
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
	@JoinTable(name = "view_media", joinColumns = @JoinColumn(name = "VIEW_ID"), inverseJoinColumns = @JoinColumn(name = "MEDIA_ID"))
	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getViewed() {
		return viewed;
	}

	public void setViewed(Date viewed) {
		this.viewed = viewed;
	}

	@Column(name = "is_win_view")
	public Boolean getWon() {
		return won;
	}

	public void setWon(Boolean won) {
		this.won = won;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((medias == null) ? 0 : medias.hashCode());
		result = prime * result + ((viewed == null) ? 0 : viewed.hashCode());
		result = prime * result + ((won == null) ? 0 : won.hashCode());
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
		ViewedMedia other = (ViewedMedia) obj;
		if (medias == null) {
			if (other.medias != null)
				return false;
		} else if (!medias.equals(other.medias))
			return false;
		if (viewed == null) {
			if (other.viewed != null)
				return false;
		} else if (!viewed.equals(other.viewed))
			return false;
		if (won == null) {
			if (other.won != null)
				return false;
		} else if (!won.equals(other.won))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ViewedMedia [id=" + id + ", medias=" + medias + ", viewed="
				+ viewed + ", won=" + won + "]";
	}

}
