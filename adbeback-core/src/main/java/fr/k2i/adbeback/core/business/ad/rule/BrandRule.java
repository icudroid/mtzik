package fr.k2i.adbeback.core.business.ad.rule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Brand")
public class BrandRule extends AdRule {
	private static final long serialVersionUID = 2387046492593489427L;

	@Override
	public String toString() {
		return "BrandRule [id=" + id + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}


}
