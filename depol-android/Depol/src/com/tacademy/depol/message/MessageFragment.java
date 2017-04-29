package com.tacademy.depol.message;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.TabsAdapter;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.profile.ProfileActivity;

public class MessageFragment extends SherlockFragment {

	FragmentTabHost tabhost;
	TabsAdapter adapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.message_layout, container, false);
		
		tabhost = (FragmentTabHost)view.findViewById(R.id.message_tabhost);
		tabhost.setup(getActivity(), getChildFragmentManager(), R.id.message_pager);
		tabhost.addTab(tabhost.newTabSpec("indox").setIndicator(getResources().getString(R.string.message_inbox)), MessageInboxFragment.class, null);
		tabhost.addTab(tabhost.newTabSpec("sent").setIndicator(getResources().getString(R.string.message_sent)), MessageOutboxFragment.class, null);
		setCustomColorInit(tabhost,getResources().getColor(R.color.tab_color), getResources().getColor(R.color.A9A9A9_color));		
		tabhost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("indox")) {
					setTabColor(tabhost, 0, getResources().getColor(R.color.tab_color));
					setTabColor(tabhost, 1, getResources().getColor(R.color.A9A9A9_color));
				} else if (tabId.equals("sent")) {
					setTabColor(tabhost, 1, getResources().getColor(R.color.tab_color));
					setTabColor(tabhost, 0, getResources().getColor(R.color.A9A9A9_color));					
				}
			}
		});
		
		//¾×¼Ç¹Ù
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.LEFT_RIGHT_MESSAGE_WRITE_MODE, getString(R.string.message));
		actionBarView.setOnActionBarListener(actionbarListener);
		actionbar.setCustomView(actionBarView);
		
		return view;
	}
		
	
	private IActionBarListener actionbarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {

			Bundle bundle = new Bundle();
			bundle.putInt(MessageSendFragment.MESSAGE_SEND_MODE, MessageSendFragment.MESSAGE_WRITE);
			MessageSendFragment messageSendFragment = new MessageSendFragment();
			messageSendFragment.setArguments(bundle);

			if (getActivity() instanceof ProfileActivity) {
				getActivity().getSupportFragmentManager().beginTransaction()			
				.replace(R.id.profile_content, messageSendFragment)
				.addToBackStack(null)
				.commit();		
				return;
			}
			
			getActivity().getSupportFragmentManager().beginTransaction()			
			.replace(R.id.content_frame_sub, messageSendFragment)
			.addToBackStack(null)
			.commit();
		}
		
		@Override
		public void onLeftButton(View v) {

			if (getActivity() instanceof ProfileActivity) {
				getActivity().getSupportFragmentManager().popBackStack();		
				return;
			}
			((SlidingFragmentActivity)getActivity()).toggle();
		}
		
	};	
	
	private void setCustomColorInit(FragmentTabHost tabhost, int enableColor, int disableColor) {
		for(int i=0;i<tabhost.getTabWidget().getChildCount();i++) {
			tabhost.getTabWidget().getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT);
			setTabColor(tabhost, i, disableColor);
		}
		setTabColor(tabhost, 0, enableColor);
	}

	private void setTabColor(FragmentTabHost tabhost, int position, int color) {
		((TextView)tabhost.getTabWidget().getChildAt(position).findViewById(android.R.id.title)).setTextColor(color);
	}
}

