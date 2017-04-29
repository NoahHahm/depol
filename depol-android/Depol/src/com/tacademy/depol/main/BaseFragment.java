package com.tacademy.depol.main;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshStaggeredGridView;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.main.STGVAdapter.ImageClickListener;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.viewer.ImageViewFragment;

public class BaseFragment extends SherlockFragment {

	public static final int NEW_TAG = 1;
	public static final int BEST_TAG = 2;
	public static final int FOLLOWING_TAG = 3;
	
	
	protected PullToRefreshStaggeredGridView ptrstgv;
	protected STGVAdapter mAdapter;
	protected ProgressBar indicator;
	protected ArrayList<PortfolioItem> mData;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  	
    	View v = inflater.inflate(R.layout.main_staggered_gridview_layout, container, false);
		indicator = (ProgressBar)v.findViewById(R.id.main_loadindicator);
        ptrstgv = (PullToRefreshStaggeredGridView) v.findViewById(R.id.main_staggered_gridview);     
        ptrstgv.setVisibility(View.GONE);
        mAdapter = new STGVAdapter(getActivity());
        mAdapter.setOnImageClickListener(new ImageClickListener() {
			
			@Override
			public void setOnImageClickListener(View v, PortfolioItem data) {	
				
				ImageViewFragment imageViewFragment = new ImageViewFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean(ImageViewFragment.PORTFOLIO_MAIN_MODE_KEY, true);
				bundle.putInt(ProfileActivity.PORTFOLIO_USER_ID_KEY, data.userId);
				bundle.putInt(ProfileActivity.PORTFOLIO_ID_KEY, data.thumbPofolId);
				imageViewFragment.setArguments(bundle);

				getActivity().getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_in_left)
				.replace(R.id.content_frame_sub, imageViewFragment)
				.addToBackStack(null)
				.commit();
				
			}
		});
        //ptrstgv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
        //ptrstgv.getRefreshableView().setItemMargin(margin);
        //ptrstgv.setPadding(margin, 0, margin, 0);
        ptrstgv.getRefreshableView().setItemMargin(margin);
        
        View footerView;
        LayoutInflater inflaters = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = inflaters.inflate(R.layout.layout_loading_footer, null);
        //ptrstgv.getRefreshableView().setFooterView(footerView);
               
    	return v;    	
    }
    
    public void refresh(ArrayList<PortfolioItem> item) {
        indicator.setVisibility(View.GONE);
    	mAdapter.setNewItem(item);
    	ptrstgv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();       
        ptrstgv.setVisibility(View.VISIBLE);
        ptrstgv.onRefreshComplete();
    }
    
}
