package fr.k2i.adbeback.webapp.bean;

import java.io.Serializable;

import fr.k2i.adbeback.bean.StatusGame;

public class LimiteTimeAdGameBean implements Serializable{
	private static final long serialVersionUID = 583944580843347770L;
	private Integer score;
	private StatusGame status;
	private String message;
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public StatusGame getStatus() {
		return status;
	}
	public void setStatus(StatusGame status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
