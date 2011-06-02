package fr.k2i.adbeback.core.business.media;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media")
@DiscriminatorColumn(name = "classe", discriminatorType = DiscriminatorType.STRING)
public abstract class Media extends BaseObject implements Serializable {
	private static final long serialVersionUID = -5332865080689901973L;
	protected Long id;
	protected String title;
	protected List<Productor> productors;
	protected List<Genre> genres;
	protected String description;
	protected Long duration;
	protected String jacket;
	protected String thumbJacket;
	protected String imgPresentation;
	protected Date releaseDate;
	protected Integer nbAdsNeeded; 
	protected String file;

//	@SequenceGenerator(name = "Media_Gen", sequenceName = "Media_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Media_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToMany(targetEntity = Productor.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "media_productor", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "PRODUCTOR_ID"))
	public List<Productor> getProductors() {
		return productors;
	}

	public void setProductors(List<Productor> productors) {
		this.productors = productors;
	}

	@ManyToMany(targetEntity = Genre.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "media_genre", joinColumns = @JoinColumn(name = "MEDIA_ID"), inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getJacket() {
		return jacket;
	}

	public void setJacket(String jacket) {
		this.jacket = jacket;
	}

	@Temporal(TemporalType.DATE)
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getNbAdsNeeded() {
		return nbAdsNeeded;
	}

	public void setNbAdsNeeded(Integer nbAdsNeeded) {
		this.nbAdsNeeded = nbAdsNeeded;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getThumbJacket() {
		return thumbJacket;
	}

	public void setThumbJacket(String thumbJacket) {
		this.thumbJacket = thumbJacket;
	}

	public String getImgPresentation() {
		return imgPresentation;
	}

	public void setImgPresentation(String imgPresentation) {
		this.imgPresentation = imgPresentation;
	}

}
