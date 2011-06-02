package fr.k2i.adbeback.core.business.ad.rule;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
@DiscriminatorValue("Open")
public class OpenRule extends AdRule {
	private static final long serialVersionUID = 2723929702129656644L;
	private List<AdResponse>responses;
	private AdResponse correct;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CORRECT_ID")
	public AdResponse getCorrect() {
		return correct;
	}
	public void setCorrect(AdResponse correct) {
		this.correct = correct;
	}
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "RULE_ID")
	public List<AdResponse> getResponses() {
		return responses;
	}
	public void setResponses(List<AdResponse> responses) {
		this.responses = responses;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		result = prime * result
				+ ((responses == null) ? 0 : responses.hashCode());
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
		OpenRule other = (OpenRule) obj;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (responses == null) {
			if (other.responses != null)
				return false;
		} else if (!responses.equals(other.responses))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OpenRule [question=" + question + ", responses=" + responses
				+ ", id=" + id + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}

}
