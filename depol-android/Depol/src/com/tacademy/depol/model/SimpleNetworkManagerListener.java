package com.tacademy.depol.model;

interface INetworkManagerListener<T>  {

	public void onLoadDataListener(T data);
	public void onMoreDataListener(T data);
	public void onFailListener(int statusCode);
	public void onLoginSuccess(T data);
	public void onLoginIdFail(int statusCode);	
	public void onLoginPasswordFail(int statusCode);
	public void onBasicInfoModifySuccess();
	public void onPortfolioRemoveSuccess();
	public void onPortfolioRemoveFail(int statusCode);
	
}

public class SimpleNetworkManagerListener<T> implements INetworkManagerListener<T> {

	@Override
	public void onLoadDataListener(T data) {
		
	}

	@Override
	public void onMoreDataListener(T data) {
		
	}

	@Override
	public void onFailListener(int statusCode) {
		
	}

	@Override
	public void onLoginSuccess(T data) {
		
	}

	@Override
	public void onBasicInfoModifySuccess() {
		
	}

	@Override
	public void onPortfolioRemoveSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPortfolioRemoveFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginIdFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoginPasswordFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}
	
	
}