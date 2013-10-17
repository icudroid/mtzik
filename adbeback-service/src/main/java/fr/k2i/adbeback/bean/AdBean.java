package fr.k2i.adbeback.bean;

import java.io.Serializable;
import java.util.List;

public class AdBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -427599826972908154L;
	private String url;
	private String question;
	private List<PossibilityBean> possibilities;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<PossibilityBean> getPossibilities() {
		return possibilities;
	}
	public void setPossibilities(List<PossibilityBean> possibilities) {
		this.possibilities = possibilities;
	}
}
