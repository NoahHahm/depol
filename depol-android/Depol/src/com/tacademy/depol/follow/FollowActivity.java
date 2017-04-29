package com.tacademy.depol.follow;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.follow.FollowAdapter.IFollowAdapterListener;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class FollowActivity extends ParentFollowActivity {
	
	FollowAdapter followAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    
	    Intent intent = getIntent();
	    if (intent != null) {
	    	userId = intent.getIntExtra(USER_ID_KEY, 0);
	    }
	    
	    followAdapter = new FollowAdapter(this);
	    followAdapter.setOnFollowAdapterListener(new IFollowAdapterListener() {
			
			@Override
			public void onFollowClick(View v, PortfolioItem data, int position) {
				boolean isFollow = data.isFollowed == 1 ? true : false;
				if (isFollow) {
					ServiceAPI.getInstance().RequestUnFollow(FollowActivity.this, data.userId, mListener);
				} else {
					ServiceAPI.getInstance().RequestFollow(FollowActivity.this, data.userId, mListener);
				}
			}
		});
	    

		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.follower));
	    listview.setAdapter(followAdapter);
	    ServiceAPI.getInstance().RequestFollowList(this, userId, mListener);
	}
	
	
	private SimpleServiceListener<ArrayList<PortfolioItem>> mListener = new SimpleServiceListener<ArrayList<PortfolioItem>>(){
		
	
    	
    	@Override
    	public void onFollowListRequestFail(int statusCode) {
    		dialog.dismiss();
    	}
    	
    	@Override
    	public void onFollowListRequestSuccess(ArrayList<PortfolioItem> data) {
    		followAdapter.setData(data);
    		dialog.dismiss();
    	}

    	@Override
    	public void onFollowRequestSuccess() {
    		dialog.dismiss();    		    		
    	}
    	

    	@Override
    	public void onUnFollowRequestFail(int statusCode) {
    		dialog.dismiss();    		
    	}
    	
    };

}

