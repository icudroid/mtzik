package fr.k2i.adbeback.bean;

import java.io.Serializable;
import java.util.List;

public class AdGameBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7410887579362937509L;
	private Integer minScore;
	private Integer totalAds;
	private Long timeLimite;
	private List<AdBean> game;
	public Integer getMinScore() {
		return minScore;
	}
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	public Integer getTotalAds() {
		return totalAds;
	}
	public void setTotalAds(Integer totalAds) {
		this.totalAds = totalAds;
	}
	public Long getTimeLimite() {
		return timeLimite;
	}
	public void setTimeLimite(Long timeLimite) {
		this.timeLimite = timeLimite;
	}
	public List<AdBean> getGame() {
		return game;
	}
	public void setGame(List<AdBean> game) {
		this.game = game;
	}
}
