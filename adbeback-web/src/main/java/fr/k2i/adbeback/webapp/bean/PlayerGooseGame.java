package fr.k2i.adbeback.webapp.bean;

import java.io.Serializable;

public class PlayerGooseGame implements Serializable {

	private static final long serialVersionUID = 5603916859817278592L;

	private Integer type;
	private Integer num;
	private Boolean isToken;

	public PlayerGooseGame(boolean isToken, Integer num, Integer type) {
		this.type = type;
		this.num = num;
		this.isToken = isToken;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Boolean getIsToken() {
		return isToken;
	}

	public void setIsToken(Boolean isToken) {
		this.isToken = isToken;
	}

}
