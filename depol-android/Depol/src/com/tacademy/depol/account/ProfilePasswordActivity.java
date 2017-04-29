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
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class ProfilePasswordActivity extends SherlockActivity {

	/** Called when the activity is first created. */
	String newPass;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_modify_password_layout);
		
	    ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 	
		
		NormalActionBar actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.password_modigy_request));
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
		
		final EditText oldPassword = (EditText)findViewById(R.id.oldpassEditView);
		final EditText newPassword = (EditText)findViewById(R.id.newpassEditView);
		
		Button btn = (Button)findViewById(R.id.btn_pass_modify_ok);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String oldPass = oldPassword.getText().toString();
				newPass = newPassword.getText().toString();
				
				if (oldPass == null || oldPass.equals("")) {
					Toast.makeText(ProfilePasswordActivity.this, getString(R.string.old_password_null), Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (newPass == null || newPass.equals("")) {
					Toast.makeText(ProfilePasswordActivity.this, getString(R.string.new_password_null), Toast.LENGTH_SHORT).show();
					return;					
				}
				
				ServiceAPI.getInstance().RequestPasswordModify(ProfilePasswordActivity.this, oldPass, newPass, new SimpleServiceListener(){
					@Override
					public void onPasswordModifyRequestFail(int statusCode) {
						
					}
					@Override
					public void onPasswordModifyRequestOldPassFail() {
						if (ProfilePasswordActivity.this != null) {
							Toast.makeText(ProfilePasswordActivity.this, getString(R.string.password_fail), Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onPasswordModifyRequestSuccess() {
						if (ProfilePasswordActivity.this != null) {
							Toast.makeText(ProfilePasswordActivity.this, getString(R.string.password_modigy_success), Toast.LENGTH_SHORT).show();
						}
						if (oldPassword != null) oldPassword.setText("");
						if (newPassword != null) newPassword.setText("");
						if (newPass != null) PropertyManager.getInstance().setMyPassword(newPass);
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
