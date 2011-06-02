package fr.k2i.adbeback.core.business.goosegame;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Table(name = "goose_game")
public class GooseGame extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5989474986523224103L;
	private Long id;
	private List<GooseLevel> levels;

//	@SequenceGenerator(name = "GooseGame_Gen", sequenceName = "GooseGame_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GooseGame_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "GOOSE_GAME_ID")
	public List<GooseLevel> getLevels() {
		return levels;
	}

	public void setLevels(List<GooseLevel> levels) {
		this.levels = levels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((levels == null) ? 0 : levels.hashCode());
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
		GooseGame other = (GooseGame) obj;
		if (levels == null) {
			if (other.levels != null)
				return false;
		} else if (!levels.equals(other.levels))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GooseGame [id=" + id + ", levels=" + levels + "]";
	}




}
