package com.tacademy.depol.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.TabsAdapter;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.menu.MenuFragment;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.search.SearchFragment;
import com.tacademy.depol.viewer.ChildFragment;

public class MainActivity extends SlidingFragmentActivity {
	
	public static final int CUSTOM_ACTION_BAR_TYPE_1 = 1;
	public static final int CUSTOM_ACTION_BAR_TYPE_2 = 2;

	private TabHost tabhost;
	private TabsAdapter adapter;
	private ActionBar actionbar;
	private NormalActionBar actionBarView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main_layout);	
		setBehindContentView(R.layout.main_menu_layout_blunk);
		getSlidingMenu().setSecondaryMenu(R.layout.main_search_layout_blunk);
		Bundle arguments = new Bundle();
		MenuFragment menuView = new MenuFragment();
		menuView.setArguments(arguments);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, menuView)
		.commit();		
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.main_search_view, new SearchFragment())
		.commit();

		// customize the SlidingMenu
		final SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);		
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		// ╬в╪г╧ы
		actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		
		
		actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.MAIN_MODE);
		actionBarView.setOnActionBarListener(actionbarListener);
		actionbar.setCustomView(actionBarView);
		
		//ег
		final ViewPager pager = (ViewPager)findViewById(R.id.pager);
		tabhost = (TabHost)findViewById(R.id.tabhost);
		adapter = new TabsAdapter(this, tabhost, pager);
				
		tabhost.setup();
		adapter.addTab(tabhost.newTabSpec("new").setIndicator(getResources().getString(R.string.main_new)), NewFragment.class, null);
		adapter.addTab(tabhost.newTabSpec("best").setIndicator(getResources().getString(R.string.main_best)), BestFragment.class, null);
		adapter.addTab(tabhost.newTabSpec("follow").setIndicator(getResources().getString(R.string.main_follow)), FollowFragment.class, null);
		adapter.setCustomColorInit(getResources().getColor(R.color.tab_color), getResources().getColor(R.color.A9A9A9_color));
		pager.setAdapter(adapter);

		
	}
	
	private IActionBarListener actionbarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {
			showSecondaryMenu();
		}
		
		@Override
		public void onLeftButton(View v) {
			toggle();
		}
		
	};	
	

	SherlockFragment fragment;
	public void currentFragment(SherlockFragment fragment) {
		this.fragment = fragment;	
	}
	
	@Override
	public void onBackPressed() {
		
		if (fragment != null) {
			if (fragment instanceof ChildFragment) {
				if (!((ChildFragment) fragment).onBackPressed()) {
					super.onBackPressed();
				}
			} else if (!fragment.getChildFragmentManager().popBackStackImmediate()) {
				super.onBackPressed();
			}
		}
		else {
			super.onBackPressed();
		}

		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			actionBarView.setMode(NormalActionBar.MAIN_MODE);
			actionBarView.setOnActionBarListener(actionbarListener);
			actionbar.setCustomView(actionBarView);
			getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		}
	}
	
	@Override
	protected void onDestroy() {
		ServiceAPI.getInstance().cancelNetwork(this);
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode,
//				resultCode, data);
	}
}
