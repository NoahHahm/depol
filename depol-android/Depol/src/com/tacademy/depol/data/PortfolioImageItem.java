package com.tacademy.depol.data;

public class PortfolioImageItem {
	
	private int id;
	private String uri;
	
	public PortfolioImageItem(int id, String url) {
		this.id = id;
		this.uri = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
