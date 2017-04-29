package com.tacademy.depol.recuritment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.R;
import com.tacademy.depol.TabsAdapter;

public class RecuritmentFragment extends SherlockFragment {

	TabHost tabhost;
	TabsAdapter adapter;
	ViewPager pager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.incruit_board_layout, container, false);
		
		//ег
		pager = (ViewPager)view.findViewById(R.id.incruit_pager);
		tabhost = (TabHost)view.findViewById(R.id.incruit_tabhost);
		adapter = new TabsAdapter(getActivity(), getChildFragmentManager(), tabhost, pager);

		tabhost.setup();
		adapter.addTab(tabhost.newTabSpec("new").setIndicator(getResources().getString(R.string.recurit_new)), RecuritNewFragment.class, null);
		adapter.addTab(tabhost.newTabSpec("end").setIndicator(getResources().getString(R.string.recurit_end)), RecuritNewFragment.class, null);
		adapter.addTab(tabhost.newTabSpec("interest").setIndicator(getResources().getString(R.string.recurit_interest)), RecuritNewFragment.class, null);
		
		pager.setAdapter(adapter);
		adapter.setCustomColorInit(Color.WHITE, Color.DKGRAY);

		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
}
