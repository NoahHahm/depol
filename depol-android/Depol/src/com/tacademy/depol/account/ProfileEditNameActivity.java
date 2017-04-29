package com.tacademy.depol.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class ProfileEditNameActivity extends SherlockActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.profile_edit_modify_name_layout);
	    
	    final EditText nameView = (EditText)findViewById(R.id.nameEditView);
	    ImageButton btn = (ImageButton)findViewById(R.id.btnInit);
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nameView.setText("");
			}
		});

	    ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 	
		
		NormalActionBar actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.profile_edit), getString(R.string.ok));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				String str = nameView.getText().toString();
				if (str == null || str.equals("")) {
					Toast.makeText(ProfileEditNameActivity.this, getString(R.string.profile_edit_name_null), Toast.LENGTH_SHORT).show();
				}
				PropertyManager.getInstance().setMyProfileName(str);
				setResult(RESULT_OK);
				ServiceAPI.getInstance().RequestUpdateProfile(ProfileEditNameActivity.this, new SimpleServiceListener<UserBasicInfo>(){
					@Override
					public void onProfileUpdateSuccess(UserBasicInfo data) {
						PropertyManager.getInstance().setMyData(data);
					}
				});
				finish();
			}
			
			@Override
			public void onLeftButton(View v) {
				finish();
			}
		});
		actionbar.setCustomView(actionBarView);	    
	}

	@Override
	protected void onDestroy() {
		ServiceAPI.getInstance().cancelNetwork(this);
		super.onDestroy();
	}
	
}
