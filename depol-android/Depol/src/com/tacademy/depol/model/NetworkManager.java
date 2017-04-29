package com.tacademy.depol.model;

import java.util.ArrayList;

import android.content.Context;

import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.PortfolioListItem;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserItem;

public class NetworkManager {
	
	private static NetworkManager instance;

	private NetworkManager() {
		
	}
	
	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}
	
	public void removePortfolio(Context context, int id, final SimpleNetworkManagerListener listener) {
		ServiceAPI.getInstance().RequestRemoveTest(context, id, new SimpleServiceListener(){
			
			@Override
			public void onPortfolioRemoveFail(int statusCode) {
				if (listener != null) {
					listener.onPortfolioRemoveFail(statusCode);
				}
			}
			
			@Override
			public void onPortfolioRemoveSuccess() {
				if (listener != null) {
					listener.onPortfolioRemoveSuccess();
				}
			}
		});
	}
	
	public void getPortfolioMainList(Context context, int tag, int pageNum, final SimpleNetworkManagerListener<ArrayList<PortfolioItem>> listener) {
		ServiceAPI.getInstance().RequestNewPortfolio(context, tag, 0, new SimpleServiceListener<PortfolioListItem>() {			
			
			@Override
			public void onServiceSuccessListener(PortfolioListItem data) {
				if (listener != null) {
					listener.onLoadDataListener(data.pofol);
				}
				
			}

			@Override
			public void onServiceFailListener(int statusCode) {

				if (listener != null) {
					listener.onFailListener(statusCode);
				}
			}
		});	
	}

	public void getPortfolio(Context context, final int id, final SimpleNetworkManagerListener<PortfolioItem> listener) {
		ServiceAPI.getInstance().RequestPortfolio(context, id, new SimpleServiceListener<PortfolioItem>() {

			@Override
			public void onServiceSuccessListener(PortfolioItem data) {
				if (listener != null) {
					listener.onLoadDataListener(data);
				}
			}

			@Override
			public void onServiceFailListener(int statusCode) {
				if (listener != null) {
					listener.onFailListener(statusCode);
				}
			}
		});
	}
	

	public void basicInfoModify(Context context, BasicInfo info, final SimpleNetworkManagerListener<PortfolioItem> listener) {
		ServiceAPI.getInstance().RequestBasicInfoModify(context, info, new SimpleServiceListener(){
			@Override
			public void onBasicInfoModifySuccess() {
				if (listener != null) {
					listener.onBasicInfoModifySuccess();
				}
			}
			
			@Override
			public void onBasicInfoModifyFail(int statusCode) {
				if (listener != null) {
					listener.onFailListener(statusCode);
				}
			}
		});
	}
	
	
	public void getLoginData(Context context, String id, String password, final SimpleNetworkManagerListener<UserBasicInfo> listener) {
		ServiceAPI.getInstance().RequestLogin(context, id, password, new SimpleServiceListener<UserBasicInfo>(){

			@Override
			public void onLoginSuccess(UserBasicInfo data) {
				if (listener != null) {
					listener.onLoginSuccess(data);
				}
			}
			
			@Override
			public void onLoginFail(int statusCode) {
				if (listener != null) {
					listener.onFailListener(statusCode);
				}
			}
			
			@Override
			public void onLoginIdFail(int statusCode) {
				if (listener != null) {
					listener.onLoginIdFail(statusCode);
				}
			}
			
			@Override
			public void onLoginPasswordFail(int statusCode) {
				if (listener != null) {
					listener.onLoginPasswordFail(statusCode);
				}
			}
			
			
		});
	}
	
	ArrayList<SimpleNetworkManagerListener<UserItem>> mListenerList = new ArrayList<SimpleNetworkManagerListener<UserItem>>();
	private UserItem mData = null;	
	boolean isRequest = false;
	
	public void cacheUserItemClear() {
		mData = null;
	}
	public UserItem getCacheUserItem(Context context, int id, SimpleNetworkManagerListener<UserItem> listener) {
		if (mData != null) {
			return mData;
		}
		register(listener);
		if (!isRequest) {
			isRequest = true;
			getUserProfile(context, id, true, null);
		}
		return null;
	}
	
	public void register(SimpleNetworkManagerListener<UserItem> listener) {
		mListenerList.add(listener);
	}
	
	public void unregister(SimpleNetworkManagerListener<UserItem> listener) {
		mListenerList.remove(listener);
	}
	
	public void getUserProfile(Context context, int id, final boolean isMine, final SimpleNetworkManagerListener<UserItem> listener) {
		ServiceAPI.getInstance().RequestUserProfile(context, id, new SimpleServiceListener<UserItem>() {
			
			@Override
			public void onServiceSuccessListener(UserItem data) {
				if (isMine) {
					mData = data;
					for (SimpleNetworkManagerListener<UserItem> reglistener : mListenerList) {
						reglistener.onLoadDataListener(data);
					}
				} else {
					userData = data;
				}
				if (listener != null) {
					listener.onLoadDataListener(data);
				}
			}
			
			@Override
			public void onServiceFailListener(int statusCode) {

				if (listener != null) {
					listener.onFailListener(statusCode);
				}
			}
		});	
	}
	
	private UserItem userData = null;
	public UserItem getCacheOtherProfile(Context context, int id) {
		if (userData != null) {
			return userData;
		}
		getUserProfile(context, id, false, null);		
		return userData;
	}

}
