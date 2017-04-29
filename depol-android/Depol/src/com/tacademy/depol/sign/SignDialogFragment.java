package com.tacademy.depol.sign;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;

public class SignDialogFragment extends SherlockDialogFragment {
	
	public static final String LOGIN_TAG = "LOGIN_TAG";
	public static final String SIGNUP_TAG = "SIGNUP_TAG";
	
	private String email;
	private String password;
	private String name;
	
	public interface IDialogSignListener<T> {
		public void onLoginSuccess(T data); 
		public void onFail(); 
	}
	
	public SignDialogFragment() {
		
	}
	public SignDialogFragment (String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public SignDialogFragment (String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	SimpleSignDialogListener mSignListener;
	
	SimpleNetworkManagerListener<UserBasicInfo> mListener = new SimpleNetworkManagerListener<UserBasicInfo>(){
		
		@Override
		public void onLoginSuccess(UserBasicInfo data) {
			dismiss();
			if (mSignListener != null) {
				mSignListener.onSignInSuccess(data);
			}
		}
		
		@Override
		public void onFailListener(int statusCode) {
			dismiss();
			if (mSignListener != null) {
				mSignListener.onSignInFail();
			}
		}
		@Override
		public void onLoginIdFail(int statusCode) {
			dismiss();
			if (mSignListener != null) {
				mSignListener.onSignInIdFail();
			}
		}
		@Override
		public void onLoginPasswordFail(int statusCode) {
			dismiss();
			if (mSignListener != null) {
				mSignListener.onSignInPasswordFail();
			}
		}
		
	};
	
	public void setOnSignListener (SimpleSignDialogListener listener) {
		mSignListener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
		if (getTag().equals(LOGIN_TAG)) {
			NetworkManager.getInstance().getLoginData(getActivity(), email, password, mListener);
		} else if (getTag().equals(SIGNUP_TAG)) {
			ServiceAPI.getInstance().RequestSignUp(getActivity(), email, password, name, new SimpleServiceListener() {
				@Override
				public void onSiginUpSuccess() {
					NetworkManager.getInstance().getLoginData(getActivity(), email, password, mListener);
				}
				@Override
				public void onSiginUpFail(int statusCode) {
					if (mSignListener != null) {
						mSignListener.onSignUpFail();
					}
					dismiss();
				}
			});
		}
	}
		
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setStyle(SignDialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		
		View v = inflater.inflate(R.layout.loading_indicator, container, false);
		return v;    	
    };
	
}
