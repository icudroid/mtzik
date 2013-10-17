package fr.k2i.adbeback.core.business.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Table(name = "ad_response_player")
public class AdResponsePlayer extends BaseObject implements Serializable {

	private static final long serialVersionUID = -9114916487551116090L;
	private Long id;
	private Possibility response;
	private AdScore adScore;
	private Integer number; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "POSSIBILITY_ID")
	public Possibility getResponse() {
		return response;
	}
	
	public void setResponse(Possibility response) {
		this.response = response;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "AD_SCORE_ID")
	public AdScore getAdScore() {
		return adScore;
	}

	public void setAdScore(AdScore adScore) {
		this.adScore = adScore;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adScore == null) ? 0 : adScore.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((response == null) ? 0 : response.hashCode());
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
		AdResponsePlayer other = (AdResponsePlayer) obj;
		if (adScore == null) {
			if (other.adScore != null)
				return false;
		} else if (!adScore.equals(other.adScore))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AdResponsePlayer [id=" + id + ", response=" + response
				+ ", adScore=" + adScore + ", number=" + number + "]";
	}

}
