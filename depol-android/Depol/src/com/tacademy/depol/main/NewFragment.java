package com.tacademy.depol.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.staggeredgridview.StaggeredGridView;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshBase;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.PortfolioListItem;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;


public class NewFragment extends BaseFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

    	ServiceAPI.getInstance().RequestNewPortfolio(getActivity(), NEW_TAG, 0, new SimpleServiceListener<PortfolioListItem>() {			
			
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
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
        ptrstgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StaggeredGridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
             	NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.NEW_TAG, BaseFragment.NEW_TAG, listener);  
            }
        });

        //아래에서 당길때
        ptrstgv.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
            	//NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.NEW_TAG, BaseFragment.NEW_TAG, listener);
            }
        });        
        
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
}