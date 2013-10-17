package fr.k2i.adbeback.core.business.media;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("album")
public class Album extends Media {
	private static final long serialVersionUID = -8195169031786628618L;

	private List<Music> musics;

    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        mappedBy = "albums",
        targetEntity = Music.class
    )
	public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((musics == null) ? 0 : musics.hashCode());
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
		Album other = (Album) obj;
		if (musics == null) {
			if (other.musics != null)
				return false;
		} else if (!musics.equals(other.musics))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Album [musics=" + musics + ", id=" + id + ", title=" + title
				+ ", productors=" + productors + ", genres=" + genres
				+ ", description=" + description + ", duration=" + duration
				+ ", jacket=" + jacket + ", releaseDate=" + releaseDate
				+ ", nbAdsNeeded=" + nbAdsNeeded + ", file=" + file + "]";
	}


}
