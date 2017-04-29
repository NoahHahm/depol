package com.tacademy.depol.follow;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.follow.FollowAdapter.IFollowAdapterListener;
import com.tacademy.depol.follow.FollowCellItem.IFollowCellListener;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class FollowIngActivity extends ParentFollowActivity {
		 
	FollowingAdapter followAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Intent intent = getIntent();
	    if (intent != null) {
	    	userId = intent.getIntExtra(USER_ID_KEY, 0);
	    }
	    
	    followAdapter = new FollowingAdapter(this);
	    followAdapter.setOnFollowAdapterListener(new IFollowAdapterListener() {
			
			@Override
			public void onFollowClick(View v, PortfolioItem data, int position) {
				ServiceAPI.getInstance().RequestUnFollow(FollowIngActivity.this, data.userId, mListener);
			}
		});

		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.main_follow));
	    listview.setAdapter(followAdapter); 	    
	    ServiceAPI.getInstance().RequestFollowingList(this, userId, mListener);
	}
	
	SimpleServiceListener<ArrayList<PortfolioItem>> mListener = new SimpleServiceListener<ArrayList<PortfolioItem>>(){
		
	
		public void onFollowIngListRequestSuccess(java.util.ArrayList<PortfolioItem> data) {
    		followAdapter.setData(data);
    		dialog.dismiss();	
		}
    	
		public void onFollowIngListRequestFail(int statusCode) {
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

class FollowingAdapter extends FollowAdapter {

	private Context mContext;
	
	public FollowingAdapter(Context context) {
		super(context);
		mContext = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		FollowIngCellItem view = null;
		if (convertView == null) {
			view = new FollowIngCellItem(mContext);
		} else {
			view = (FollowIngCellItem)convertView;
		}
		view.setData(position, mData.get(position));
		view.setOnFollowListener(new IFollowCellListener() {
			
			@Override
			public void onFollowClick(View v, PortfolioItem data, int position) {
				if (mListener != null) {
					mData.remove(position);
					notifyDataSetChanged();
					mListener.onFollowClick(v, data, position);
				}
			}
		});
		return view;
	}	
	
}