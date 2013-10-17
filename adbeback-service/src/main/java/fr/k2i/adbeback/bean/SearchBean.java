package fr.k2i.adbeback.bean;

public class SearchBean {
	private String search;
	private Long genreId;
	public SearchBean(){}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public Long getGenreId() {
		return genreId;
	}
	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}
	public SearchBean( Long genreId,String search) {
		super();
		this.search = search;
		this.genreId = genreId;
	}
}
