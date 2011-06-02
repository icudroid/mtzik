package fr.k2i.adbeback.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MediaLineBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7127790773994895099L;
	private String title;
	private Integer adNeeded;
	private Long idMedia;
	public final static int MUSIC_TYPE = 1;
	public final static int ALBUM_TYPE = 0;
	
	private Integer type;
	private List<MediaLineBean> medias = new ArrayList<MediaLineBean>();
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getAdNeeded() {
		return adNeeded;
	}
	public void setAdNeeded(Integer adNeeded) {
		this.adNeeded = adNeeded;
	}
	public Long getIdMedia() {
		return idMedia;
	}
	public void setIdMedia(Long idMedia) {
		this.idMedia = idMedia;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<MediaLineBean> getMedias() {
		return medias;
	}
	public void setMedias(List<MediaLineBean> medias) {
		this.medias = medias;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedia == null) ? 0 : idMedia.hashCode());
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
		MediaLineBean other = (MediaLineBean) obj;
		if (idMedia == null) {
			if (other.idMedia != null)
				return false;
		} else if (!idMedia.equals(other.idMedia))
			return false;
		return true;
	}
	
}
