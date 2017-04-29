package com.tacademy.depol.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.UserBasicInfo;

public class PropertyManager {


	public static final String SHARED_PREFERENCES_LOGIN_INFO = "LOGIN_INFO";
	public static final String FACEBOOK_LOGIN = "FACEBOOK_LOGIN";
	public static final String DEPOL_LOGIN = "DEPOL_LOGIN";

	public static final String USER_ID = "USER_ID";
	public static final String USER_NAME = "USER_NAME";
	public static final String USER_JOB = "USER_JOB";
	public static final String USER_RECRUIT_STATUS = "USER_RECRUIT_STATUS";
	public static final String USER_PASSWORD = "USER_PASSWORD";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_PROFILE_PICTURE = "USER_PROFILE_PICTURE";
	
	
	public static final String USER_FACEBOOK_ID = "USER_FACEBOOK_ID";
	public static final String USER_FACEBOOK_NAME = "USER_FACEBOOK_NAME";
	public static final String USER_FACEBOOK_EMAIL = "USER_FACEBOOK_EMAIL";
	
	private static PropertyManager instance;
	
	public static PropertyManager getInstance() {
		if (instance == null) {
			instance = new PropertyManager();
		}
		return instance;
	}
	
	public boolean isDepolLogin() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		return pref.getBoolean(DEPOL_LOGIN, false);
	}
	
	public void setDepolLogin(boolean isLogin) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(DEPOL_LOGIN, isLogin);
		editor.commit();			
	}
	
	public boolean isFacebookLogin() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		return pref.getBoolean(FACEBOOK_LOGIN, false);
	}
	
	public void setFacebookLogin(boolean isLogin) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(FACEBOOK_LOGIN, isLogin);
		editor.commit();			
	}
	
	public void setMyData(UserBasicInfo info) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(USER_ID, info.userId);
		editor.putString(USER_NAME, info.userName);
		editor.putString(USER_JOB, info.userPosition);
		editor.putInt(USER_RECRUIT_STATUS, info.userRecruitStatus);
		editor.putString(USER_PROFILE_PICTURE, info.userPropicUri);
		editor.commit();			
	}
	
	public void setBasicMyData(BasicInfo basicinfo) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_NAME, basicinfo.userName);
		editor.putString(USER_JOB, basicinfo.userPosition);		
		editor.commit();		
	}
	public void setMyProfilePicture(String uri) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_PROFILE_PICTURE, uri);
		editor.commit();			
	}
	public void setMyProfileName(String name) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_NAME, name);
		editor.commit();			
	}

	public void setMyProfileJob(String job) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_JOB, job);
		editor.commit();			
	}
	public void clearMyData() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(USER_ID, 0);
		editor.putString(USER_NAME, "");
		editor.putString(USER_JOB, "");
		editor.putInt(USER_RECRUIT_STATUS, 0);
		editor.putString(USER_PROFILE_PICTURE, "");
		editor.commit();			
	}
	
	public void setRecruitStatus(int type) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(USER_RECRUIT_STATUS, type);
		editor.commit();
	}
	
	public int getRecruitStatus() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		int recruitStatus = pref.getInt(USER_RECRUIT_STATUS, 1);	
		return recruitStatus;
	}
	
	public void setMyLoginData(String email, String password) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_PASSWORD, password);
		editor.putString(USER_EMAIL, email);
		editor.commit();			
	}

	public String getMyEmail() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		String email = pref.getString(USER_EMAIL, "");	
		return email;
	}
	

	public void setMyEmail(String email) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_EMAIL, email);
		editor.commit();
	}

	public void setMyPassword(String pass) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_PASSWORD, pass);
		editor.commit();
	}
	
	public void setFacebookEmail(String email) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_FACEBOOK_EMAIL, email);
		editor.commit();			
	}

	public String getMyPassword() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		String password = pref.getString(USER_PASSWORD, "");	
		return password;
	}
	
	public UserBasicInfo getMyData() {
		UserBasicInfo info = new UserBasicInfo();
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		info.userId = pref.getInt(USER_ID, 0);
		info.userName = pref.getString(USER_NAME, "");
		info.userPosition = pref.getString(USER_JOB, "");
		info.userRecruitStatus = pref.getInt(USER_RECRUIT_STATUS, 0);
		info.userPropicUri = pref.getString(USER_PROFILE_PICTURE, "");
		return info;
	}
	
	public void setCacheProfilePicture(boolean isCacheProfilePicture) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(USER_PROFILE_PICTURE, isCacheProfilePicture);
		editor.commit();
	}
	

	public void setFacebookId(String facebookId) {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(USER_FACEBOOK_ID, facebookId);
		editor.commit();
	}

	public String getFacebookId() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		return pref.getString(USER_FACEBOOK_ID, "");
	}

	public boolean isCacheProfilePicture() {
		SharedPreferences pref = ApplicationContext.getContext().getSharedPreferences(SHARED_PREFERENCES_LOGIN_INFO, Context.MODE_PRIVATE);
		boolean result = pref.getBoolean(USER_PROFILE_PICTURE, false);	
		return result;
	}
}
