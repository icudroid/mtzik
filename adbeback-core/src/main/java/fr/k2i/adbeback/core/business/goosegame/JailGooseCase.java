package fr.k2i.adbeback.core.business.goosegame;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Jail")
public class JailGooseCase extends GooseCase {

	private static final long serialVersionUID = 142765339504599666L;

	@Override
	public String toString() {
		return "JailGooseCase [id=" + id + ", level=" + level + ", number="
				+ number + "]";
	}


}
