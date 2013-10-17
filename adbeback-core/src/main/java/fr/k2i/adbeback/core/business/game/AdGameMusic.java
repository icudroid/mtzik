package fr.k2i.adbeback.core.business.game;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Music")
public class AdGameMusic extends AdGame {

	private static final long serialVersionUID = -4291589475824097583L;

	private String unlockCode;
	

	public String getUnlockCode() {
		return unlockCode;
	}

	public void setUnlockCode(String unlockCode) {
		this.unlockCode = unlockCode;
	}

	@Override
	public String toString() {
		return "AdGameMusic [id=" + id + ", choises=" + choises
				+ ", generated=" + generated + ", score=" + score + ", medias="
				+ medias + "]";
	}

}

