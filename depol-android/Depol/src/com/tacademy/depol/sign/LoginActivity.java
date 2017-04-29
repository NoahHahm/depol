package com.tacademy.depol.sign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tacademy.depol.R;

public class LoginActivity extends SherlockFragmentActivity {

	private Handler handler = new Handler(Looper.getMainLooper());
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login_main_layout);
	    		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.login_content, new LoginFragment())
		.commit();
	}	


    
}
