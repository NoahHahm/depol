package com.tacademy.depol.main;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.Facebook;
import com.tacademy.depol.model.Facebook.FacebookListener;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.sign.LoginActivity;
import com.tacademy.depol.sign.SignDialogFragment;
import com.tacademy.depol.sign.SimpleSignDialogListener;

public class SplashActivity extends SherlockFragmentActivity {
	SignDialogFragment signDialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash_layout); 
	    UserDataManager.getInstance().clearDepolCache();

		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
	    signDialog = new SignDialogFragment();
	    initialize();	    
	}

	private void initialize() {
		 Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {	
				
				boolean isDepolLogin = PropertyManager.getInstance().isDepolLogin();
				boolean isFacebookLogin = PropertyManager.getInstance().isFacebookLogin();
				

			    if (!isDepolLogin && !isFacebookLogin) {
			    	startLoginActivity();
					return;
			    }

			    Facebook.getInstance().setOnLoginSuccessListener(new FacebookListener() {	
			    	
					@Override
					public void OnLoginSuccessListener(UserBasicInfo user) {
						new ImageDownload().execute(user.userPropicUri);
					}

					@Override
					public void OnLoginFail(int statusCode) {
						if (signDialog != null) signDialog.dismiss();
						startLoginActivity();
					}
				});
			    
				//연동 사용자
				if (isDepolLogin && isFacebookLogin) {
					startLogin();
					return;
				}
				
				//디폴 사용자
				if (isDepolLogin) {
					startLogin();
					return;
				}
				
				//페북 사용자
			    if (isFacebookLogin) {
			    	signDialog.show(getSupportFragmentManager(), "");
			    	Facebook.getInstance().sessionLogin(SplashActivity.this);
					return;
			    }
			    
			    
			    
			}
		};
		handler.sendEmptyMessageDelayed(RESULT_OK, 1000);
	}
	
	private void startLoginActivity() {
    	Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
    	startActivity(intent);
    	finish();
	}
	

	private void startMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void startLogin() {
		String email = PropertyManager.getInstance().getMyEmail();
		String password = PropertyManager.getInstance().getMyPassword();
		signDialog = new SignDialogFragment(email, password);
		signDialog.show(getSupportFragmentManager(), SignDialogFragment.LOGIN_TAG);
		signDialog.setOnSignListener(new SimpleSignDialogListener<UserBasicInfo>(){
			
			@Override
			public void onSignInSuccess(UserBasicInfo data) {
				PropertyManager.getInstance().setMyData(data);
				PropertyManager.getInstance().setDepolLogin(true);
				new ImageDownload().execute(data.userPropicUri);				
			};
			
			@Override
			public void onSignInFail() {
				startLoginActivity();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		ServiceAPI.getInstance().cancelNetwork(this);
		super.onDestroy();
	}
	
	class ImageDownload extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			File file = new File(Environment.getExternalStorageDirectory(), UserDataManager.USER_CROP_IMAGE);
			Bitmap bitmap = ImageLoader.getInstance().loadImageSync(params[0].toString());
			if (bitmap != null) UserDataManager.getInstance().saveImage(bitmap, file);
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			startMainActivity();
		}
	}
}
