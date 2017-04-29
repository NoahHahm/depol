package com.tacademy.depol.follow;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.follow.FollowCellItem.IFollowCellListener;

public class FollowAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected ArrayList<PortfolioItem> mData;
	
	public interface IFollowAdapterListener {
		public void onFollowClick(View v, PortfolioItem data, int position);
	}
	IFollowAdapterListener mListener;
	
	public void setOnFollowAdapterListener(IFollowAdapterListener listener) {
		mListener = listener;
	}
	public FollowAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<PortfolioItem>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	public void setData(ArrayList<PortfolioItem> data) {
		mData = data;
		notifyDataSetChanged();
	}
	
	public ArrayList<PortfolioItem> getData() {
		return mData;
	}
	
	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		FollowCellItem view = null;
		if (convertView == null) {
			view = new FollowCellItem(mContext);
		} else {
			view = (FollowCellItem)convertView;
		}
		view.setData(position, mData.get(position));
		view.setOnFollowListener(new IFollowCellListener() {
			
			@Override
			public void onFollowClick(View v, PortfolioItem data, int position) {
				if (mListener != null) {
					mListener.onFollowClick(v, data, position);
				}
			}
		});
		return view;
	}
	
}