package com.tacademy.depol.follow;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.profile.LoadDialogFragment;

public class ParentFollowActivity extends SherlockFragmentActivity {

	public final static String USER_ID_KEY = "USER_ID_KEY";
	
	int userId;
	int cellPosition;
	protected ListView listview;
	protected LoadDialogFragment dialog;
	protected ActionBar actionbar;
	protected NormalActionBar actionBarView;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.profile_follow_layout);	   	    
	    listview = (ListView)findViewById(R.id.follow_listview);

	    dialog = new LoadDialogFragment();
	    dialog.show(getSupportFragmentManager(), "");
	    
		actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBarView = new NormalActionBar(this);
		actionBarView.setOnActionBarListener(ationBarListener);
		actionbar.setCustomView(actionBarView);
	}
    
	
	protected IActionBarListener ationBarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLeftButton(View v) {
			finish();
		}
	};
}
