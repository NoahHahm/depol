package com.tacademy.depol.invitation;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.kakaolink.KakaoLink;
import com.tacademy.depol.model.Facebook;

public class InvitationFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.invitation_layout, container, false);
		

		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.LEFT_MENU_MODE, getString(R.string.invitation));
		actionBarView.setOnActionBarListener(actionbarListener);
		actionbar.setCustomView(actionBarView);
		
		
		Button btn = (Button)v.findViewById(R.id.btn_facebook_invite);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Facebook.getInstance().sendRequestDialog(getActivity());
			}
		});
		
		btn = (Button)v.findViewById(R.id.btn_kakaolink);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Recommended: Use application context for parameter.
				KakaoLink kakaoLink = KakaoLink.getLink(ApplicationContext.getContext());

				// check, intent is available.
				if (!kakaoLink.isAvailableIntent())
				  return;

				/**
				 * @param activity
				 * @param url
				 * @param message
				 * @param appId
				 * @param appVer
				 * @param appName
				 * @param encoding
				 */
				try {
					kakaoLink.openKakaoLink(getActivity(), 
					        "당신을 초대합니다.", 
					        "http://play.google.com/store/apps/details?id=com.tacademy.depol", 
					        getActivity().getPackageName(), 
					        getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName, 
					        "디자이너를 위한 포트폴리오 관리 앱 DEPOL", 
					        "UTF-8");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		return v;
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
