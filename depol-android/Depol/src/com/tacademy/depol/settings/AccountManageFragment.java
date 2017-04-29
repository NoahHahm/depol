package com.tacademy.depol.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.widget.LoginButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.account.ProfileEditEmailActivity;
import com.tacademy.depol.account.ProfilePasswordActivity;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.Facebook;
import com.tacademy.depol.model.Facebook.FacebookListener;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.MyRoundedImageView;

public class AccountManageFragment extends SherlockFragment {
	
	public final static int REQUEST_EMAIL_MODIFY = 1;
	public final static int REQUEST_PASSWORD_MODIFY = 2;
	
	private MyRoundedImageView imageView;
	private TextView nameView;
	private TextView emailView;
	private LoginButton btnFacebook;
	private UserBasicInfo info;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_account_management_layout, container, false);
		
		info = PropertyManager.getInstance().getMyData();
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		DisplayImageOptions option = ImageLoaderManager.getInstance().getDisplayImageOptions();

		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.account_info));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		actionbar.setCustomView(actionBarView);

		imageView = (MyRoundedImageView)view.findViewById(R.id.mess_profile_picture_view);
		ImageLoader.getInstance().displayImage(info.userPropicUri, imageView, option);
		imageView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
		
		
		nameView = (TextView)view.findViewById(R.id.acc_name);
		emailView = (TextView)view.findViewById(R.id.menu_like_timeView);
		
		UserBasicInfo info = PropertyManager.getInstance().getMyData();
		if (info != null) {
			nameView.setText(info.userName);
			emailView.setText(PropertyManager.getInstance().getMyEmail());	
		}
		
		
		btnFacebook = (LoginButton)view.findViewById(R.id.btn_version);
		btnFacebook.setFragment(this);
	    btnFacebook.setReadPermissions("basic_info", "email");
	    btnFacebook.setSessionStatusCallback(Facebook.getInstance().getStatusCallback());
	    Facebook.getInstance().setOnLoginSuccessListener(new FacebookListener() {
			
			@Override
			public void OnLoginSuccessListener(UserBasicInfo user) {
				PropertyManager.getInstance().setMyData(user);
				
				facebookButtonEnaled();
			}

			@Override
			public void OnLoginFail(int statusCode) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    facebookButtonEnaled();
	    
	    Button btn = (Button)view.findViewById(R.id.btn_account_email);
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProfileEditEmailActivity.class);
				startActivityForResult(intent, REQUEST_EMAIL_MODIFY);
			}
		});

	    btn = (Button)view.findViewById(R.id.btn_account_pass);
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProfilePasswordActivity.class);
				startActivityForResult(intent, REQUEST_PASSWORD_MODIFY);
			}
		});
		
		return view;
	}

	
	private void facebookButtonEnaled() {
	    boolean isFacebook = PropertyManager.getInstance().isFacebookLogin();
	    if (isFacebook) {
	    	btnFacebook.setEnabled(false);
	    } else {
	    	btnFacebook.setEnabled(true);
	    }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		btnFacebook.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_EMAIL_MODIFY && resultCode == Activity.RESULT_OK) {
			UserBasicInfo info = PropertyManager.getInstance().getMyData();
			if (info != null) {
				nameView.setText(info.userName);
				emailView.setText(PropertyManager.getInstance().getMyEmail());	
			}
		}
	}
}
