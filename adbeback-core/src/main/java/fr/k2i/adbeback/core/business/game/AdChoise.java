package fr.k2i.adbeback.core.business.game;

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
@Table(name = "adchoise")
public class AdChoise extends BaseObject implements Serializable {
	private static final long serialVersionUID = -7659738703107950065L;
	private Long id;
	private List<Possibility> possiblities;
	private AdGame adGame;
	private Possibility correct;
	private String question;
	private Integer number;
	
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "POSSIBILITY_ID")
	public List<Possibility> getPossiblities() {
		return possiblities;
	}

	public void setPossiblities(List<Possibility> possiblities) {
		this.possiblities = possiblities;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "AD_GAME_ID")
	public AdGame getAdGame() {
		return adGame;
	}

	public void setAdGame(AdGame adGame) {
		this.adGame = adGame;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CORRECT_ID")
	public Possibility getCorrect() {
		return correct;
	}

	public void setCorrect(Possibility correct) {
		this.correct = correct;
	}
	
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correct == null) ? 0 : correct.hashCode());
		result = prime * result
				+ ((possiblities == null) ? 0 : possiblities.hashCode());
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
		AdChoise other = (AdChoise) obj;
		if (correct == null) {
			if (other.correct != null)
				return false;
		} else if (!correct.equals(other.correct))
			return false;
		if (possiblities == null) {
			if (other.possiblities != null)
				return false;
		} else if (!possiblities.equals(other.possiblities))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AdChoise [id=" + id + ", possiblities=" + possiblities
				+ ", correct=" + correct + "]";
	}

}
