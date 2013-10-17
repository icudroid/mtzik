package fr.k2i.adbeback.core.business.game;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import fr.k2i.adbeback.core.business.ad.Brand;

@Entity
@DiscriminatorValue("Brand")
public class BrandPossibility extends Possibility {
	private static final long serialVersionUID = 1785460358105543429L;
	private Brand brand;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "BRAND_ID")
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
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
		BrandPossibility other = (BrandPossibility) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BrandPossibility [brand=" + brand + ", id=" + id + ", ad=" + ad
				+ "]";
	}
}
