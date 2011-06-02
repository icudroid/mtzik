package fr.k2i.adbeback.core.business.goosegame;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Dead")
public class DeadGooseCase extends GooseCase {

	private static final long serialVersionUID = 5373444898760166087L;


	@Override
	public String toString() {
		return "DeadGooseCase [id=" + id + ", level=" + level + "]";
	}

}
