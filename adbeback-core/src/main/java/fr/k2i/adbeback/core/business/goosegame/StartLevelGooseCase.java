package fr.k2i.adbeback.core.business.goosegame;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("StartLevel")
public class StartLevelGooseCase extends GooseCase {

	private static final long serialVersionUID = -5268929254361161407L;

	@Override
	public String toString() {
		return "StartLevelGooseCase [id=" + id + ", level=" + level + "]";
	}

}
