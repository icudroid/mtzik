package fr.k2i.adbeback.core.business.partener;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Table(name = "reduction")
public class Reduction extends BaseObject implements Serializable {
	private static final long serialVersionUID = 6039138990472415617L;
	private Long id;
	private Double value;
	private Double percentageValue;
	private String reductionCode;
	private String description;
	private Partener partener;

//	@SequenceGenerator(name = "Reduction_Gen", sequenceName = "Reduction_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Reduction_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getPercentageValue() {
		return percentageValue;
	}

	public void setPercentageValue(Double percentageValue) {
		this.percentageValue = percentageValue;
	}

	public String getReductionCode() {
		return reductionCode;
	}

	public void setReductionCode(String reductionCode) {
		this.reductionCode = reductionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PARTENER_ID")
	public Partener getPartener() {
		return partener;
	}

	public void setPartener(Partener partener) {
		this.partener = partener;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((partener == null) ? 0 : partener.hashCode());
		result = prime * result
				+ ((percentageValue == null) ? 0 : percentageValue.hashCode());
		result = prime * result
				+ ((reductionCode == null) ? 0 : reductionCode.hashCode());
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
		Reduction other = (Reduction) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (partener == null) {
			if (other.partener != null)
				return false;
		} else if (!partener.equals(other.partener))
			return false;
		if (percentageValue == null) {
			if (other.percentageValue != null)
				return false;
		} else if (!percentageValue.equals(other.percentageValue))
			return false;
		if (reductionCode == null) {
			if (other.reductionCode != null)
				return false;
		} else if (!reductionCode.equals(other.reductionCode))
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
		return "Reduction [id=" + id + ", value=" + value
				+ ", percentageValue=" + percentageValue + ", reductionCode="
				+ reductionCode + ", description=" + description
				+ ", partener=" + partener + "]";
	}

}
