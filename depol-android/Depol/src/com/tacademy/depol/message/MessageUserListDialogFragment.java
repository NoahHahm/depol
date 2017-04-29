package com.tacademy.depol.message;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.follow.ParentFollowAdapter;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.LoadDialogFragment;

public class MessageUserListDialogFragment <T> extends LoadDialogFragment {
	
	SimpleMessageListener<ArrayList<PortfolioItem>> mListener;
	UserListAdapter adapter;
	LoadDialogFragment dialog;
	ListView listview;
	ArrayList<PortfolioItem> mData;

	public void setOnUserListDialogListener(SimpleMessageListener<ArrayList<PortfolioItem>> listener) {
		mListener = listener;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(LoadDialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View v = inflater.inflate(R.layout.message_userlist_dialog_layout, container, false);
		
		adapter = new UserListAdapter(getActivity());
		listview = (ListView) v.findViewById(R.id.message_send_userListView);
		listview.setAdapter(adapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		ImageButton btn = (ImageButton) v.findViewById(R.id.btn_userList_confirm);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mData == null) dismiss();
				
				SparseBooleanArray arr = listview.getCheckedItemPositions();
				ArrayList<PortfolioItem> tempData = new ArrayList<PortfolioItem>();
				for(int i=0;i<arr.size();i++) {
					if (arr.get(arr.keyAt(i))) {
						int key = arr.keyAt(i);
						tempData.add(mData.get(key));
					}
				}
				
				dismiss();				
				if (mListener != null) {
					mListener.onConfirmDialog(tempData);
				}
			}
		});
		btn = (ImageButton) v.findViewById(R.id.btn_userList_calcel);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		UserBasicInfo info = PropertyManager.getInstance().getMyData();
		dialog = new LoadDialogFragment();
		dialog.show(getChildFragmentManager(), "");
		ServiceAPI.getInstance().RequestFollowingList(getActivity(), info.userId, serviceListener);
		
		return v;
	}

	private SimpleServiceListener<ArrayList<PortfolioItem>> serviceListener = new SimpleServiceListener<ArrayList<PortfolioItem>>() {

		public void onFollowIngListRequestFail(int statusCode) {
			dialog.dismiss();
		}
		
		public void onFollowIngListRequestSuccess(ArrayList<PortfolioItem> data) {
			mData = data;
			dialog.dismiss();
			if (adapter != null) adapter.setData(data);			
		}

	};

}

class UserListAdapter extends ParentFollowAdapter<PortfolioItem> {

	public UserListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		MessageUserListCell view = null;
		if (convertView == null) {
			view = new MessageUserListCell(mContext);
		} else {
			view = (MessageUserListCell)convertView;
		}
		view.setData(position, mData.get(position));
		return view;
	}
	
}
