package com.tacademy.depol.sign;

interface ISignDialogListener<T> {
	public void onSignInSuccess(T data); 
	public void onSignInFail(); 
	public void onSignInIdFail(); 
	public void onSignInPasswordFail(); 
	public void onSignUpSuccess(T data); 
	public void onSignUpFail(); 
}

public class SimpleSignDialogListener<T> implements ISignDialogListener<T> {

	@Override
	public void onSignInSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignUpSuccess(T data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignUpFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInIdFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInPasswordFail() {
		// TODO Auto-generated method stub
		
	}


}
