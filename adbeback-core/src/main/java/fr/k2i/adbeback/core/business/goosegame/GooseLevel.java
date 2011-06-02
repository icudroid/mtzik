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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Table(name = "goose_level")
public class GooseLevel extends BaseObject implements Serializable {

	private static final long serialVersionUID = 3339924782068634755L;
	private Long id;
	private Long level;
	private Double value;
	private StartLevelGooseCase startCase;
	private EndLevelGooseCase endCase;
	private List<GooseCase> gooseCases;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "GOOSELEVEL_ID")
	public List<GooseCase> getGooseCases() {
		return gooseCases;
	}

	public void setGooseCases(List<GooseCase> gooseCases) {
		this.gooseCases = gooseCases;
	}

//	@SequenceGenerator(name = "GooseLevel_Gen", sequenceName = "GooseLevel_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GooseLevel_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "START_ID")
	public StartLevelGooseCase getStartCase() {
		return startCase;
	}

	public void setStartCase(StartLevelGooseCase startCase) {
		this.startCase = startCase;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "END_ID")
	public EndLevelGooseCase getEndCase() {
		return endCase;
	}

	public void setEndCase(EndLevelGooseCase endCase) {
		this.endCase = endCase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endCase == null) ? 0 : endCase.hashCode());
		result = prime * result
				+ ((gooseCases == null) ? 0 : gooseCases.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result
				+ ((startCase == null) ? 0 : startCase.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		GooseLevel other = (GooseLevel) obj;
		if (endCase == null) {
			if (other.endCase != null)
				return false;
		} else if (!endCase.equals(other.endCase))
			return false;
		if (gooseCases == null) {
			if (other.gooseCases != null)
				return false;
		} else if (!gooseCases.equals(other.gooseCases))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (startCase == null) {
			if (other.startCase != null)
				return false;
		} else if (!startCase.equals(other.startCase))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GooseLevel [id=" + id + ", level=" + level + ", value=" + value
				+ ", startCase=" + startCase + ", endCase=" + endCase
				+ ", gooseCases=" + gooseCases + "]";
	}


}
