package com.tacademy.depol.message;

import java.util.ArrayList;

import com.tacademy.depol.data.PortfolioItem;

interface ISimpleMessageListener<T> {
	public void onMessageRead();
	public void onMessageReadFail(int statusCode);
	public void onMessageSend();
	public void onMessageSendFail(int statusCode);
	public void onConfirmDialog(T data);
}
public class SimpleMessageListener<T> implements ISimpleMessageListener<T> {

	@Override
	public void onMessageRead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReadFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageSend() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageSendFail(int statusCode) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConfirmDialog(T data) {
		// TODO Auto-generated method stub
		
	}


}
