package fr.k2i.adbeback.core.business.game;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OpenPossibility")
public class OpenPossibility extends Possibility {
	private static final long serialVersionUID = -5422694885484003177L;
	private String answer;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
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
		OpenPossibility other = (OpenPossibility) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OpenPossibility [answer=" + answer + ", id=" + id + ", ad="
				+ ad + "]";
	}

}
