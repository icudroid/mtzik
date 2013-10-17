package fr.k2i.adbeback.core.business.ad.rule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Product")
public class ProductRule extends AdRule {
	private static final long serialVersionUID = 6708314171621564778L;

	@Override
	public String toString() {
		return "ProductRule [id=" + id + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
