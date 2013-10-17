package fr.k2i.adbeback.core.business.media;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("Music")
public class Music extends Media {
	private static final long serialVersionUID = 2176672861946794968L;
	private List<Album> albums;
	private List<Artist> artists;
	private String mp3Sample;
	
	public String getMp3Sample() {
		return mp3Sample;
	}

	public void setMp3Sample(String mp3Sample) {
		this.mp3Sample = mp3Sample;
	}

	@ManyToMany(targetEntity = Music.class, cascade = { CascadeType.PERSIST,
		CascadeType.MERGE })
	@JoinTable(name = "album_artist", joinColumns = @JoinColumn(name = "MUSIC_ID"), inverseJoinColumns = @JoinColumn(name = "ALBUM_ID"))
	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	@ManyToMany(targetEntity = Artist.class, cascade = { CascadeType.PERSIST,
		CascadeType.MERGE })
	@JoinTable(name = "music_artist", joinColumns = @JoinColumn(name = "MUSIC_ID"), inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albums == null) ? 0 : albums.hashCode());
		result = prime * result + ((artists == null) ? 0 : artists.hashCode());
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
		Music other = (Music) obj;
		if (albums == null) {
			if (other.albums != null)
				return false;
		} else if (!albums.equals(other.albums))
			return false;
		if (artists == null) {
			if (other.artists != null)
				return false;
		} else if (!artists.equals(other.artists))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Music [albums=" + albums + ", artists=" + artists + ", id="
				+ id + ", title=" + title + ", productors=" + productors
				+ ", genres=" + genres + ", description=" + description
				+ ", duration=" + duration + ", jacket=" + jacket
				+ ", releaseDate=" + releaseDate + "]";
	}

}
