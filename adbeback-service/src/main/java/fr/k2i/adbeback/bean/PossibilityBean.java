package fr.k2i.adbeback.bean;

import java.io.Serializable;

public class PossibilityBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6889109272308668073L;
	private Long id;
	private Integer type;
	private String answer;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
