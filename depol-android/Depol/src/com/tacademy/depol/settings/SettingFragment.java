package com.tacademy.depol.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.model.Facebook;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.sign.LoginActivity;

public class SettingFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_menu_layout, container, false);
		
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.LEFT_MENU_MODE, getString(R.string.settings));
		actionBarView.setOnActionBarListener(actionbarListener);
		actionbar.setCustomView(actionBarView);
		
		Button btn = (Button)view.findViewById(R.id.btn_notice);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, new NoticeFragment())
				.addToBackStack(null).commit();
			}
		});
		
		btn = (Button)view.findViewById(R.id.btn_logout);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isFacebookLogin = PropertyManager.getInstance().isFacebookLogin();
				boolean isDepolLogin = PropertyManager.getInstance().isDepolLogin();
				
				if (isDepolLogin) {
					PropertyManager.getInstance().setDepolLogin(false);
				}
				
				if (isFacebookLogin) {
					Facebook.getInstance().sessionClear();					
					PropertyManager.getInstance().setFacebookLogin(false);
				}

				//캐시 클리어
			    PropertyManager.getInstance().clearMyData();
			    NetworkManager.getInstance().cacheUserItemClear();
			    UserDataManager.getInstance().clearDepolCache();
			    
			    startLoginActivity();
			}
		});
		
		btn = (Button)view.findViewById(R.id.btn_account);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, new AccountManageFragment())
				.addToBackStack(null).commit();
			}
		});
		
		btn = (Button)view.findViewById(R.id.btn_version);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, new VersionFragment())
				.addToBackStack(null).commit();
			}
		});
		return view;
	}
	
	private void startLoginActivity() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	

	private IActionBarListener actionbarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {
			((SlidingFragmentActivity)getActivity()).showSecondaryMenu();
		}
		
		@Override
		public void onLeftButton(View v) {
			((SlidingFragmentActivity)getActivity()).toggle();
		}
		
	};
}
