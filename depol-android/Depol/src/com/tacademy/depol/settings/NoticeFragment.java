package com.tacademy.depol.settings;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.NoticeItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class NoticeFragment extends SherlockFragment {
	
	NoticeAdapter adapter;
	private ArrayList<NoticeItem> mData;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		ServiceAPI.getInstance().RequestNotice(getActivity(), new SimpleServiceListener<ArrayList<NoticeItem>>(){
			
			@Override
			public void onNoticeRequestFail(int statusCode) {
				
			}
			
			@Override
			public void onNoticeRequestSuccess(ArrayList<NoticeItem> data) {
				mData = data;
				adapter.setData(mData);
				
			}
			
		});
	
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_menu_notice, container, false);
		
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.setting_notice));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		actionbar.setCustomView(actionBarView);
		
		ListView listview = (ListView)view.findViewById(R.id.listView_notice);
		adapter = new NoticeAdapter(getActivity());
		listview.setAdapter(adapter);
		
		return view;
	}
	
}

class NoticeAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<NoticeItem> mData;
	
	public NoticeAdapter(Context context) {
		this.mContext = context;
		mData = new ArrayList<NoticeItem>();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	public void setData(ArrayList<NoticeItem> data) {
		mData.clear();
		mData.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoticeCellItem view = null;
		if (convertView == null) {
			view = new NoticeCellItem(mContext);
		} else {
			view = (NoticeCellItem)convertView;
		}
		view.setData(mData.get(position));
		return view;
	}
	
}