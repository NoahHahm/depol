package com.tacademy.depol.message;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tacademy.depol.data.MessageItem;

public class MessageAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected ArrayList<MessageItem> mData;
	
	public MessageAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<MessageItem>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}
	
	public void setData(ArrayList<MessageItem> data) {
		mData = data;
		notifyDataSetChanged();
	}
	

	public ArrayList<MessageItem> getData() {
		return mData;
	}
	
	public void remove(int position) {
		mData.remove(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		MessageViewCell view = null;
		if (convertView == null) {
			view = new MessageViewCell(mContext);
		} else {
			view = (MessageViewCell)convertView;
		}
		view.setData(mData.get(position));
		return view;
	}
	
}
