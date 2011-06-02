package fr.k2i.adbeback.core.business.media;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Productor")
public class Productor extends Person {
	public Productor(){
		
	}
	
	public Productor(String firstName, String lastName) {
		super(firstName, lastName);
	}


	private static final long serialVersionUID = -7459989016180487826L;


	@Override
	public String toString() {
		return "Productor [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", website=" + website
				+ ", version=" + version + "]";
	}

}
