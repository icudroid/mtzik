package fr.k2i.adbeback.core.business.goosegame;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.player.Player;

@Entity
@Table(name = "goose_win")
public class GooseWin extends BaseObject implements Serializable {
	private static final long serialVersionUID = -4925568112544086765L;
	private Long id;
	private GooseLevel gooseLevel;
	private Double value;
	private WinStatus status = WinStatus.NotTranfered;
	private Date windate = new Date();
	private Player player;
	
	//	@SequenceGenerator(name = "GooseWin_Gen", sequenceName = "GooseWin_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GooseWin_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "GOOSELEVEL_ID")
	public GooseLevel getGooseLevel() {
		return gooseLevel;
	}

	public void setGooseLevel(GooseLevel gooseLevel) {
		this.gooseLevel = gooseLevel;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Enumerated(EnumType.ORDINAL)
	public WinStatus getStatus() {
		return status;
	}

	public void setStatus(WinStatus status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getWindate() {
		return windate;
	}

	public void setWindate(Date windate) {
		this.windate = windate;
	}

	@ManyToOne
    @JoinColumn(name="PLAYER_ID")
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
				+ ((gooseLevel == null) ? 0 : gooseLevel.hashCode());
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
		GooseWin other = (GooseWin) obj;
		if (gooseLevel == null) {
			if (other.gooseLevel != null)
				return false;
		} else if (!gooseLevel.equals(other.gooseLevel))
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
		return "GooseWin [id=" + id + ", gooseLevel=" + gooseLevel + ", value="
				+ value + "]";
	}

}
