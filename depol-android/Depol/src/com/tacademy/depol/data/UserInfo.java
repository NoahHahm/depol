package com.tacademy.depol.data;

import java.util.ArrayList;



public class UserInfo {

	private String id;
	private String name;
	private String link;
	private String email;
	private int jobType;
	private int follwerCount;
	private int followCount;
	private PortfolioItem portfolioitem;
	private ArrayList<FollowItem> followList;	

    public UserInfo() {
    	
    }
    
    public UserInfo(String username, int jobtype) {
    	this.name = username;
    	this.jobType = jobtype;
    }
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public int getJobType() {
		return jobType;
	}
	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getFollwerCount() {
		return follwerCount;
	}

	public void setFollwerCount(int follwerCount) {
		this.follwerCount = follwerCount;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}

	public PortfolioItem getPortfolioitem() {
		return portfolioitem;
	}

	public void setPortfolioitem(PortfolioItem portfolioitem) {
		this.portfolioitem = portfolioitem;
	}

	public ArrayList<FollowItem> getFollowList() {
		return followList;
	}

	public void setFollowList(ArrayList<FollowItem> followList) {
		this.followList = followList;
	}	
	
}
