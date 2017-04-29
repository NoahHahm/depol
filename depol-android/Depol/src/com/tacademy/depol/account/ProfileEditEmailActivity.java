package com.tacademy.depol.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.Account;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.util.Validation;

public class ProfileEditEmailActivity extends SherlockActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_modify_email_layout);

		final EditText emailEditView = (EditText)findViewById(R.id.oldpassEditView);
		
	    ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 	
		
		NormalActionBar actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.profile_edit_email_modify));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				finish();
			}
		});
		actionbar.setCustomView(actionBarView);
		
		Button btn = (Button)findViewById(R.id.btn_pass_modify_ok);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String email = emailEditView.getText().toString();
				boolean isValidationEmail = Validation.isEmailValid(email);
				
				//유효 체크
				if (!isValidationEmail) {
					Toast.makeText(ProfileEditEmailActivity.this, getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (email == null || email.equals("")) {
					Toast.makeText(ProfileEditEmailActivity.this, getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
					return;
				}
				
				ServiceAPI.getInstance().RequestEmailModify(ProfileEditEmailActivity.this, email, new SimpleServiceListener<Account>(){
					@Override
					public void onEmailModifyRequestSuccess(Account data) {
						PropertyManager.getInstance().setMyEmail(data.email);
						if (ProfileEditEmailActivity.this != null) {
							setResult(RESULT_OK);							
							finish();
						}
					}
					
					@Override
					public void onEmailModifyRequestFail(int statusCode) {
						if (ProfileEditEmailActivity.this != null) {
							Toast.makeText(ProfileEditEmailActivity.this, getString(R.string.password_fail), Toast.LENGTH_SHORT).show();
						}
					}
					
				});
				
			}
		});
		
		
		
	}

	@Override
	protected void onDestroy() {
		ServiceAPI.getInstance().cancelNetwork(this);
		super.onDestroy();
	}
	
}
