package com.tacademy.depol.viewer;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.kakaolink.KakaoLink;
import com.tacademy.depol.model.Facebook;

public class ShareFragment extends SherlockFragment {
	
	public static final String IMAGE_URL = "IMAGE_URL";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.viewer_share_layout, container, false);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		final String url = getArguments().getString(IMAGE_URL);
		
		ImageView btn = (ImageView)view.findViewById(R.id.btn_share_kakao);
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
					        "이 디자인 어때요?", 
					        url, 
					        getActivity().getPackageName(), 
					        getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName, 
					        "디자이너를 위한 포트폴리오 관리 앱 DEPOL", 
					        "UTF-8");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		btn = (ImageView)view.findViewById(R.id.btn_share_facebook);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Facebook.getInstance().requestPhotoUpload(getActivity(), url, getString(R.string.depol_share));
			}
		});
		return view;
	}
	
	
}
