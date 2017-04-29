package com.tacademy.depol.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.net.sip.SipRegistrationListener;

public class UserItem implements Serializable {
	
	public String result;
	public String userName;
	public String userPosition;
	public String userPropicUri;
	public int userRecruitStatus;
	public int FollowingNum;
	public int FollowerNum;
	public ArrayList<PortfolioItem> pofol;
	public int isMine;
	public int isFollowed;
	
	public BasicInfo basicInfo;
	public ArrayList<DateInfo> educationInfo;
	public ArrayList<DateInfo> workInfo;
	public ArrayList<DateInfo> certificationInfo;
	public ArrayList<DateInfo> awardInfo;
	public ArrayList<AbilityInfo> abilityInfo;
}
