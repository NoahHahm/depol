package com.tacademy.depol.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;


public class PortfolioItem implements Serializable {
	public String result;
	public String userName;
	public int userId;
	public String userPropicUri;
	public int userRecruitStatus;
	public String[] pofolImgUri;
	public String pofolTitle;
	public String pofolText;
	public int isLiked;
	public int isMine;	
	public int thumbPofolId;
	public String thumbImgUri;
	public int likeNum;
	public int commentNum;
	public int[] category;
	public String text;
	public String timeStamp;
	public int commentId;
	public int isFollowed;
	public String userPosition;
}
