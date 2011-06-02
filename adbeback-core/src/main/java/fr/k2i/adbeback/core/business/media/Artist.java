package fr.k2i.adbeback.core.business.media;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("Artist")
public class Artist extends Person {
	public Artist() {

	}

	public Artist(String firstName, String lastName) {
		super(firstName, lastName);
	}

	private static final long serialVersionUID = -8371684556048514484L;
	private List<Music> musics;


    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "artists",
            targetEntity = Music.class
        )
    public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
	}

	@Override
	public String toString() {
		return "Artist [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", website=" + website + ", version=" + version
				+ "]";
	}


}
