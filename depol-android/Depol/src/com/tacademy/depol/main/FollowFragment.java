package com.tacademy.depol.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.staggeredgridview.StaggeredGridView;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshBase;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshStaggeredGridView;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.PortfolioListItem;
import com.tacademy.depol.main.STGVAdapter.ImageClickListener;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.viewer.ImageViewFragment;

public class FollowFragment extends BaseFragment {

	STGVAdapter mAdapter;
	PullToRefreshStaggeredGridView ptrstgv;
	ArrayList<PortfolioItem> mData;
	ProgressBar indicator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    
		ServiceAPI.getInstance().RequestNewPortfolio(getActivity(), FOLLOWING_TAG, 0, new SimpleServiceListener<PortfolioListItem>() {			
				
				@Override
				public void onServiceSuccessListener(PortfolioListItem data) {
					mData = data.pofol;
	            	refresh(mData);
				}

				@Override
				public void onServiceFailListener(int statusCode) {
					ptrstgv.onRefreshComplete();
				}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
    	View view = inflater.inflate(R.layout.main_follow_layout, container, false);
		indicator = (ProgressBar)view.findViewById(R.id.main_follow_loadindicator);

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
        
    	ptrstgv = (PullToRefreshStaggeredGridView)view.findViewById(R.id.main_follw_staggered_gridview);
        ptrstgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StaggeredGridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
            	NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.FOLLOWING_TAG, BaseFragment.FOLLOWING_TAG, listener);
            }
        });
        ptrstgv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //아래에서 당길때
        ptrstgv.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
            	//NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.FOLLOWING_TAG, BaseFragment.FOLLOWING_TAG, listener);
            }
        });
        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
            
       // ptrstgv.setPadding(margin, 0, margin, 0);
       ptrstgv.getRefreshableView().setItemMargin(margin);
        if (mData != null) {
           	refresh(mData);
        }	
       
		return view;
	}
	
    SimpleNetworkManagerListener<ArrayList<PortfolioItem>> listener = new SimpleNetworkManagerListener<ArrayList<PortfolioItem>>() {
		
		@Override
		public void onLoadDataListener(ArrayList<PortfolioItem> item) {
			mData = item;
        	refresh(mData);
		}		
		
		@Override
		public void onFailListener(int statusCode) {
	        ptrstgv.onRefreshComplete();
		}
	};
	
    public void refresh(ArrayList<PortfolioItem> item) {
        indicator.setVisibility(View.GONE);
    	mAdapter.setNewItem(item);
    	ptrstgv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();       
        ptrstgv.setVisibility(View.VISIBLE);
        ptrstgv.onRefreshComplete();
    }
}