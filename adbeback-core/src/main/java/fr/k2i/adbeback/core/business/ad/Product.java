package fr.k2i.adbeback.core.business.ad;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;

@Entity
@Table(name = "product")
public class Product extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8346229795953025008L;
	private Long id;
	private String name;
	private String description;
	private Double publicPrice;
	private Double adPrice;
	private String logo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Double publicPrice) {
		this.publicPrice = publicPrice;
	}

	public Double getAdPrice() {
		return adPrice;
	}

	public void setAdPrice(Double adPrice) {
		this.adPrice = adPrice;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adPrice == null) ? 0 : adPrice.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((publicPrice == null) ? 0 : publicPrice.hashCode());
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
		Product other = (Product) obj;
		if (adPrice == null) {
			if (other.adPrice != null)
				return false;
		} else if (!adPrice.equals(other.adPrice))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publicPrice == null) {
			if (other.publicPrice != null)
				return false;
		} else if (!publicPrice.equals(other.publicPrice))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description="
				+ description + ", publicPrice=" + publicPrice + ", adPrice="
				+ adPrice + "]";
	}

}
