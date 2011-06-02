package fr.k2i.adbeback.core.business.goosegame;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EndLevel")
public class EndLevelGooseCase extends GooseCase {

	private static final long serialVersionUID = -1720562995087823116L;


	@Override
	public String toString() {
		return "EndLevelGooseCase [id=" + id + ", level=" + level + "]";
	}

}
