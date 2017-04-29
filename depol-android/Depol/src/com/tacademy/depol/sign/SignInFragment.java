package com.tacademy.depol.sign;

import java.io.File;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.main.MainActivity;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.util.Validation;

public class SignInFragment extends SherlockFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.sign_in_layout, container, false);
	    
	    final EditText emailView = (EditText)view.findViewById(R.id.email_edit_text);
	    final EditText passView = (EditText)view.findViewById(R.id.pass_edit_text);
	    
	    TextView signTitleView = (TextView)view.findViewById(R.id.sign_in_titleView);
	    emailView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    passView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    signTitleView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
	    
	    Button btn = (Button)view.findViewById(R.id.btn_sign_login);
	    btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
	    btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final String email = emailView.getText().toString();
				final String password = passView.getText().toString();
				
				boolean isValidation = Validation.isEmailValid(email);
				if (!isValidation) {
					Toast.makeText(getActivity(), getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				SignDialogFragment f = new SignDialogFragment(email, password);
				f.show(getChildFragmentManager(), SignDialogFragment.LOGIN_TAG);
				f.setOnSignListener(new SimpleSignDialogListener<UserBasicInfo>() {
					@Override
					public void onSignInSuccess(UserBasicInfo data) {
						PropertyManager.getInstance().setMyData(data);
						PropertyManager.getInstance().setMyLoginData(email, password);
						PropertyManager.getInstance().setDepolLogin(true);
						
						ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
						new ImageDownload().execute(data.userPropicUri);
					}

					@Override
					public void onSignInIdFail() {
						if(getActivity() != null) Toast.makeText(getActivity(), getString(R.string.id_invalid), Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onSignInPasswordFail() {
						if(getActivity() != null) Toast.makeText(getActivity(), getString(R.string.password_validation), Toast.LENGTH_SHORT).show();						
					}
					
				});
				
			}
		});
	    
	    ImageButton btnBack = (ImageButton)view.findViewById(R.id.sign_in_backView);
	    btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

	    TextView forgotView = (TextView)view.findViewById(R.id.forgot_passView);
	    forgotView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    forgotView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.login_content, new ForgetFragment())
				.addToBackStack(null).commit();
			}
		});
	    
	    return view;
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
