package fr.k2i.adbeback.core.business.media;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.compass.annotations.SearchableId;

import fr.k2i.adbeback.core.business.BaseObject;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "genre")
@DiscriminatorColumn(name = "classe", discriminatorType = DiscriminatorType.STRING)
public abstract class Genre extends BaseObject implements Serializable {

	private static final long serialVersionUID = 7015318296306104846L;
	protected Long id;
	protected String genre;
	protected String codeGenre; // Pour les traductions

//	@SequenceGenerator(name = "Genre_Gen", sequenceName = "Genre_Sequence")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Genre_Gen")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCodeGenre() {
		return codeGenre;
	}

	public void setCodeGenre(String codeGenre) {
		this.codeGenre = codeGenre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeGenre == null) ? 0 : codeGenre.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
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
		Genre other = (Genre) obj;
		if (codeGenre == null) {
			if (other.codeGenre != null)
				return false;
		} else if (!codeGenre.equals(other.codeGenre))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		return true;
	}
}
