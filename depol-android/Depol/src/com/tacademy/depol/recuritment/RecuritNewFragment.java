package com.tacademy.depol.recuritment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshListView;
import com.tacademy.depol.R;

public class RecuritNewFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.incruit_board_new_layout, container, false);
		PullToRefreshListView listview = (PullToRefreshListView)view.findViewById(R.id.incruit_new_ListView);
		IncruitAdapter adapter = new IncruitAdapter(getActivity());
		listview.setAdapter(adapter);
		

		
		return view;
	}
	
	
}

class IncruitAdapter extends BaseAdapter {
	
	private Context mContext;
	private String[] data = {"data", "aaa"};
	
	public IncruitAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecuritBoardCellItem view = null;
		if (convertView == null) {
			view = new RecuritBoardCellItem(mContext);
		} else {
			view = (RecuritBoardCellItem)convertView;
		}
		return view;
	}
	
}
