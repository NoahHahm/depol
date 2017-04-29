package com.tacademy.depol.like;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.LikeItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.viewer.ImageViewFragment;

public class LikeFragment extends SherlockFragment {
	LikeAdapter adapter;
	ArrayList<LikeItem> mData;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new LikeAdapter(getActivity());
		
		ServiceAPI.getInstance().RequestLikeList(getActivity(), new SimpleServiceListener<ArrayList<LikeItem>>(){
			@Override
			public void onLikeListRequestSuccess(ArrayList<LikeItem> data) {
				mData = data;
				adapter.setData(mData);
			}
			
			@Override
			public void onLikeListRequestFail(int statusCode) {
				
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.like_listview_layout, container, false);
		ListView listView = (ListView)view.findViewById(R.id.like_listview);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				if (mData == null) return;
				
				boolean isRead = mData.get(position).isRead == 1 ? true : false;
				if (!isRead) {
					ServiceAPI.getInstance().RequestLikeRead(getActivity(), mData.get(position).likeId, new SimpleServiceListener(){
						
						@Override
						public void onLikeReadRequestSuccess(int likeId) {
							if (mData != null) {
								for (LikeItem item : mData) {
									if (item.likeId == likeId) {
										item.isRead = 1;
										adapter.setData(mData);
										break;
									}
								}
							}				
						}
						
						@Override
						public void onLikeReadRequestFail(int statusCode) {

						}
					});
				}
				
				ImageViewFragment imageViewFragment = new ImageViewFragment();
				Bundle bundle = new Bundle();
				bundle.putBoolean(ImageViewFragment.PORTFOLIO_MAIN_MODE_KEY, true);
				bundle.putInt(ProfileActivity.PORTFOLIO_USER_ID_KEY, mData.get(position).userId);
				bundle.putInt(ProfileActivity.PORTFOLIO_ID_KEY, mData.get(position).pofolId);
				bundle.putBoolean(ProfileActivity.PROFILE_OTHER_KEY, true);
				imageViewFragment.setArguments(bundle);
				

				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, imageViewFragment)
				.addToBackStack(null)
				.commit();
				
			}
		});
		

		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.LEFT_MENU_MODE, getString(R.string.like));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				((SlidingFragmentActivity)getActivity()).toggle();
			}
			
		});
		actionbar.setCustomView(actionBarView);

		if (mData != null) {
			adapter.setData(mData);
		}
		
		return view;
	}
	
	
	
}
class LikeAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<LikeItem> mData;
	
	public LikeAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<LikeItem>();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	public void setData(ArrayList<LikeItem> data) {
		mData = data;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		LikeCell view = null;
		if (convertView == null) {
			view = new LikeCell(mContext);
		}
		else {
			view = (LikeCell)convertView;
		}
		view.setData(mData.get(position));
		return view;
	}
	
}
