package fr.k2i.adbeback.core.business.goosegame;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.player.Player;

@Entity
@Table(name = "goose_token")
public class GooseToken extends BaseObject implements Serializable {
	private static final long serialVersionUID = -8708579369170723785L;
	private Long id;
	private GooseCase gooseCase;
	private Player player;

//	@SequenceGenerator(name = "GooseToken_Gen", sequenceName = "GooseToken_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GooseToken_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "GOOSECASE_ID")
	public GooseCase getGooseCase() {
		return gooseCase;
	}

	public void setGooseCase(GooseCase gooseCase) {
		this.gooseCase = gooseCase;
	}

	@OneToOne(mappedBy = "gooseToken")
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gooseCase == null) ? 0 : gooseCase.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		GooseToken other = (GooseToken) obj;
		if (gooseCase == null) {
			if (other.gooseCase != null)
				return false;
		} else if (!gooseCase.equals(other.gooseCase))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GooseToken [id=" + id + ", gooseCase=" + gooseCase
				+ ", player=" + player + "]";
	}

}
