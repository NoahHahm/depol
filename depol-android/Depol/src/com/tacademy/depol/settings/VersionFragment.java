package com.tacademy.depol.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class VersionFragment extends SherlockFragment {
	
	private TextView versionView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		ServiceAPI.getInstance().RequestVersion(getActivity(), new SimpleServiceListener<String>(){
			
			@Override
			public void onVersionRequestSuccess(String version) {
				if (versionView != null) versionView.setText(version.toString());
			}
			
			@Override
			public void onVersionRequestFail(int statusCode) {
			}
			
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_version_layout, container, false);
		versionView = (TextView)view.findViewById(R.id.versionNewView);
		versionView.setTypeface(Font.get(Font.NANUM_BARUN_GOTHIC_BOLD, getActivity()));
		TextView textView = (TextView)view.findViewById(R.id.versionOldView);
		textView.setTypeface(Font.get(Font.NANUM_BARUN_GOTHIC_BOLD, getActivity()));
		textView = (TextView)view.findViewById(R.id.oldView);
		textView.setTypeface(Font.get(Font.NANUM_BARUN_GOTHIC, getActivity()));
		textView = (TextView)view.findViewById(R.id.newView);
		textView.setTypeface(Font.get(Font.NANUM_BARUN_GOTHIC, getActivity()));
		
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.version));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		actionbar.setCustomView(actionBarView);
		

		Button btn = (Button)view.findViewById(R.id.btn_version_update);
		btn.setTypeface(Font.get(Font.NANUM_BARUN_GOTHIC_BOLD, getActivity()));
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.tacademy.depol"));
				startActivity(intent);
			}
		});
		
		return view;		
	}
}
