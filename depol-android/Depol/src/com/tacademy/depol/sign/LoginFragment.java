package com.tacademy.depol.sign;

import java.io.File;
import java.util.Arrays;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.widget.LoginButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.main.MainActivity;
import com.tacademy.depol.model.Facebook;
import com.tacademy.depol.model.Facebook.FacebookListener;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.UserDataManager;

public class LoginFragment extends SherlockFragment {


	private LoginButton btnFacebook;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.login_layout, container, false); 
	    
		TextView loginSubText = (TextView)view.findViewById(R.id.comment_userName);
		loginSubText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_MEDIUM));
 	
	    btnFacebook = (LoginButton)view.findViewById(R.id.btn_facebook);
	    btnFacebook.setFragment(this);
	    //btnFacebook.setPublishPermissions("publish_actions", "publish_stream");
	    btnFacebook.setReadPermissions(Arrays.asList("basic_info","email"));
	    btnFacebook.setSessionStatusCallback(Facebook.getInstance().getStatusCallback());
	    btnFacebook.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_BOLD));
//	    btnFacebook.setUserInfoChangedCallback(new UserInfoChangedCallback() {
//			
//			@Override
//			public void onUserInfoFetched(GraphUser user) {
//				if (user == null) return;				
////				Facebook.getInstance().setFacebookLogin(user);				
////				Intent intent = new Intent(getActivity(), MainActivity.class);
////				startActivity(intent);
////				getActivity().finish();	
//			}
//		});
	    Facebook.getInstance().setOnLoginSuccessListener(new FacebookListener() {
			
			@Override
			public void OnLoginSuccessListener(UserBasicInfo user) {
				PropertyManager.getInstance().setMyData(user);
				new ImageDownload().execute(user.userPropicUri);
			}
			
			@Override
			public void OnLoginFail(int statusCode) {
				
			}
		});
	    
	    ImageButton btn = (ImageButton)view.findViewById(R.id.btn_login);
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.login_content, new SignInFragment())
				.addToBackStack(null).commit();
			}
		});
	    
	    
	    btn = (ImageButton)view.findViewById(R.id.btn_regedit);
	    btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.login_content, new SignUpFragment())
				.addToBackStack(null).commit();
			}
		});  
	    
	    return view;
	}
	

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnFacebook.onActivityResult(requestCode, resultCode, data);
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
			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
	}
}
