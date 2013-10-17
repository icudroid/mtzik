package fr.k2i.adbeback.core.business.media;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Music")
public class MusicGenre extends Genre {
	private static final long serialVersionUID = 7343096407501325625L;


	@Override
	public String toString() {
		return "MusicGenre [id=" + id + ", genre=" + genre + ", codeGenre="
				+ codeGenre + "]";
	}

}
