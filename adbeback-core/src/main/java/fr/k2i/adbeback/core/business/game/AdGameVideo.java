package fr.k2i.adbeback.core.business.game;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Video")
public class AdGameVideo extends AdGame {

	private static final long serialVersionUID = -4482422168793644454L;


	@Override
	public String toString() {
		return "AdGameVideo [id=" + id + ", choises=" + choises
				+ ", generated=" + generated + ", score=" + score + ", medias="
				+ medias + "]";
	}

}
