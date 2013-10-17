package fr.k2i.adbeback.core.business.ad;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.ad.rule.AdRule;
import fr.k2i.adbeback.core.business.country.Country;

@Entity
@Table(name = "ad")
public class Ad extends BaseObject implements Serializable {
	private static final long serialVersionUID = -8627592656680311906L;
	private Long id;
	private Brand brand;
	private Product product;
	private List<AdRule> rules;
	private String video;
	private AdType type;
	private List<Country> aForCountries;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "BRAND_ID")
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "AD_ID")
	public List<AdRule> getRules() {
		return rules;
	}

	public void setRules(List<AdRule> rules) {
		this.rules = rules;
	}
	
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	@Enumerated(EnumType.ORDINAL)
	public AdType getType() {
		return type;
	}

	public void setType(AdType type) {
		this.type = type;
	}

	@ManyToMany(targetEntity = Country.class, cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	@JoinTable(name = "ad_country", joinColumns = @JoinColumn(name = "AD_ID"), inverseJoinColumns = @JoinColumn(name = "COUNTRY_ID"))
	public List<Country> getaForCountries() {
		return aForCountries;
	}

	public void setaForCountries(List<Country> aForCountries) {
		this.aForCountries = aForCountries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		Ad other = (Ad) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ad [id=" + id + ", brand=" + brand + ", product=" + product
				+ "]";
	}

}
