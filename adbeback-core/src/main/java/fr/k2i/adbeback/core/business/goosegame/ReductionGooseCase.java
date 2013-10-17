package fr.k2i.adbeback.core.business.goosegame;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import fr.k2i.adbeback.core.business.partener.Reduction;

@Entity
@DiscriminatorValue("Reduction")
public class ReductionGooseCase extends GooseCase {
	private static final long serialVersionUID = -6050652995509263007L;
	private Reduction reduction;


	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "REDUCTION_ID")
	public Reduction getReduction() {
		return reduction;
	}

	public void setReduction(Reduction reduction) {
		this.reduction = reduction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((reduction == null) ? 0 : reduction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReductionGooseCase other = (ReductionGooseCase) obj;
		if (reduction == null) {
			if (other.reduction != null)
				return false;
		} else if (!reduction.equals(other.reduction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReductionGooseCase [reduction=" + reduction + ", id=" + id
				+ ", level=" + level + "]";
	}

}
