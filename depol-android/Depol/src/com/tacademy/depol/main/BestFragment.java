package com.tacademy.depol.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.staggeredgridview.StaggeredGridView;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshBase;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.data.MenuNotiInfo;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.PortfolioListItem;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;

public class BestFragment extends BaseFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ServiceAPI.getInstance().RequestNewPortfolio(getActivity(), BEST_TAG, 0, new SimpleServiceListener<PortfolioListItem>() {			
			
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

		final ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
//		ServiceAPI.getInstance().RequestNoti(getActivity(), new SimpleServiceListener<MenuNotiInfo>(){
//			@Override
//			public void onMenuNotiRequestSuccess(MenuNotiInfo data) {
//				if (data.likeNum > 0 || data.messageNum > 0) {
//					if (actionbar.getCustomView() instanceof NormalActionBar) {
//						((NormalActionBar)actionbar.getCustomView()).showNew();
//					}
//				}
//			}
//		});
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
        //위에서 당길때
        ptrstgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<StaggeredGridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
            	NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.BEST_TAG, BaseFragment.BEST_TAG, new SimpleNetworkManagerListener<ArrayList<PortfolioItem>>() {
            		
            		@Override
            		public void onLoadDataListener(ArrayList<PortfolioItem> item) {
            			mData = item;
                    	refresh(mData);
            		}
            		
            		@Override
            		public void onFailListener(int statusCode) {
            	        ptrstgv.onRefreshComplete();
            		}
            	});
            }
        });

        //아래에서 당길때
        ptrstgv.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
            	//NetworkManager.getInstance().getPortfolioMainList(getActivity(), BaseFragment.BEST_TAG, BaseFragment.BEST_TAG, listener);
            }
        });    
        if (mData != null) {
        	refresh(mData);
        }
		return view;
	}
	
}