package com.tacademy.depol.search;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.staggeredgridview.StaggeredGridView;
import com.staggeredgridview.StaggeredGridView.OnLoadmoreListener;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.main.STGVAdapter;
import com.tacademy.depol.main.STGVAdapter.ImageClickListener;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.viewer.ImageViewFragment;


public class SearchResultFragment extends SherlockFragment {
	
	public static String SEARCH_KEYWORK_KEY = "SEARCH_KEYWORK_KEY";
	public static String SEARCH_CATEGORY_KEY = "SEARCH_CATEGORY_KEY";

	String keyword;
	int[] category;
	private ArrayList<PortfolioItem> mData;
	STGVAdapter adapter;
	StringBuffer buf;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			keyword = bundle.getString(SEARCH_KEYWORK_KEY);
			category = bundle.getIntArray(SEARCH_CATEGORY_KEY);
		}

		String[] str;
		if (category.length+1 == SearchFragment.MAX_CATEGORY_COUNT) {
			str = new String[1];
			str[0] = "0";
		} else if (category.length < 1 && !keyword.equals("")) {
			str = new String[1];
			str[0] = "0";
		}
		else {
			str = new String[category.length];
			for (int i = 0; i < category.length; i++) {
				str[i] = new String(String.format("%d", category[i]));
			}
		}
		
		ServiceAPI.getInstance().RequestSearch(getActivity(), keyword, str, new SimpleServiceListener<ArrayList<PortfolioItem>>(){
			
			@Override
			public void onSearchRequestSuccess(ArrayList<PortfolioItem> data) {
				mData = data;
				adapter.setNewItem(data);
			}
			
			@Override
			public void onSearchRequestFail(int statusCode) {
				
			}
		});
		

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.search_result_layout, container, false);
		TextView searchResultView = (TextView)v.findViewById(R.id.search_resultView);

		ActionBar actionbar = ((SlidingFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.MAIN_MODE);
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				((SlidingFragmentActivity)getActivity()).showSecondaryMenu();
			}
			
			@Override
			public void onLeftButton(View v) {
				((SlidingFragmentActivity)getActivity()).toggle();				
			}
		});
		actionbar.setCustomView(actionBarView);

		if (keyword.equals("") && category.length > 0) {
			searchResultView.setText(getString(R.string.search_keywordnull));			
		} else {
			searchResultView.setText(keyword + getString(R.string.search_result));						
		}
		
		StaggeredGridView listView = (StaggeredGridView)v.findViewById(R.id.search_listView);
		adapter = new STGVAdapter(getActivity());
		listView.setAdapter(adapter);
        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
        listView.setItemMargin(margin);
		listView.setOnLoadmoreListener(new OnLoadmoreListener() {
			
			@Override
			public void onLoadmore() {
				
			}
		});
		adapter.setOnImageClickListener(new ImageClickListener() {
			
			@Override
			public void setOnImageClickListener(View v, PortfolioItem data) {	
				
				ImageViewFragment imageViewFragment = new ImageViewFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean(ImageViewFragment.PORTFOLIO_MAIN_MODE_KEY, true);
				bundle.putInt(ProfileActivity.PORTFOLIO_USER_ID_KEY, data.userId);
				bundle.putInt(ProfileActivity.PORTFOLIO_ID_KEY, data.thumbPofolId);
				imageViewFragment.setArguments(bundle);

				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, imageViewFragment)
				.addToBackStack(null)
				.commit();
				
			}
		});
		
		if (mData != null) {
			adapter.setNewItem(mData);
		}
		return v;
	}
	
}
