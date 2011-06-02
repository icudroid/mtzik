package fr.k2i.adbeback.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CartBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4552013561893862429L;
	private Set<MediaLineBean> lines = new HashSet<MediaLineBean>();
	private Integer minScore = 0;
	private Integer maxTime;
	private Integer nbProduct = 0;
	private String error;
	public Set<MediaLineBean> getLines() {
		return lines;
	}
	public void setLines(Set<MediaLineBean> lines) {
		this.lines = lines;
	}
	public Integer getMinScore() {
		return minScore;
	}
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	public Integer getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Integer getNbProduct() {
		return nbProduct;
	}
	public void setNbProduct(Integer nbProduct) {
		this.nbProduct = nbProduct;
	}
	
}
