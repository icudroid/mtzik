package fr.k2i.adbeback.bean;

import java.util.Date;
import java.util.List;

import fr.k2i.adbeback.core.business.media.Genre;

public class MediaBean {
	private Long id;
	private String title;
	private List<PersonBean> productors;
	private List<PersonBean> artists;
	private List<Genre> genres;
	private String description;
	private Long duration;
	private String jacket;
	private Date releaseDate;
	private String thumbJacket;
	
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
	public List<PersonBean> getProductors() {
		return productors;
	}
	public void setProductors(List<PersonBean> productors) {
		this.productors = productors;
	}
	public List<PersonBean> getArtists() {
		return artists;
	}
	public void setArtists(List<PersonBean> artists) {
		this.artists = artists;
	}
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
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getThumbJacket() {
		return thumbJacket;
	}
	public void setThumbJacket(String thumbJacket) {
		this.thumbJacket = thumbJacket;
	}
}
