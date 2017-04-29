package com.tacademy.depol.viewer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.viewer.LikeAdapter.ILikeAdapterListener;
import com.tacademy.depol.viewer.LikeUserCellItemView.ILikeUserCellItemViewListener;

public class LikeUserFragment extends CommentFragment {

	private int portfolioId;
	private int userid;
	private LikeAdapter adapter;
	private ArrayList<PortfolioItem> mData;
	private ListView listView;
	private Bundle bundle;
	CommentFragment commentFragment;
	
	

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		bundle = getArguments();
		if (bundle != null) {
			portfolioId = bundle.getInt(PORTFOLIO_ID_KEY);
			userid = bundle.getInt(PORTFOLIO_USER_ID_KEY);
			likeCount = bundle.getInt(LIKE_COUNT_ID_KEY);
		}

		ServiceAPI.getInstance().RequestPortfolioLikeUser(getActivity(), portfolioId, new SimpleServiceListener<ArrayList<PortfolioItem>>() {
			
			@Override
			public void onPortfolioLikeUserRequestFail(int statusCode) {
				
			}
			
			@Override
			public void onPortfolioLikeUserRequestSuccess(ArrayList<PortfolioItem> data) {
				mData = data;
				adapter.setData(mData);
				adapter.notifyDataSetChanged();
			}
			
		});
		commentFragment = new CommentFragment();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.viewer_like_user_layout, container, false);
		TextView likeTitleView = (TextView)view.findViewById(R.id.like_title_view);
		likeTitleView.setText(String.format("%d¸í", likeCount));


		String like = String.format("%d", likeCount);
		String desc = getString(R.string.like_user_type1);
		int lengtn = like.length();

		SpannableStringBuilder sb = new SpannableStringBuilder(like + desc);
		sb.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, lengtn, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		likeTitleView.setText(sb);
		
		listView = (ListView)view.findViewById(R.id.like_user_listview);
		adapter = new LikeAdapter(getActivity());
		listView.setAdapter(adapter);

		adapter.setOnLikeAdapterListener(new ILikeAdapterListener() {
			
			@Override
			public void onFollowListener(View v, int position, PortfolioItem data) {
				boolean isFollow = data.isFollowed == 1 ? true : false;
				if (isFollow) {
					adapter.getData().get(position).isFollowed = 0;
					ServiceAPI.getInstance().RequestUnFollow(getActivity(), data.userId, mListener);
					adapter.notifyDataSetChanged();
				} else {
					adapter.getData().get(position).isFollowed = 1;
					ServiceAPI.getInstance().RequestFollow(getActivity(), data.userId, mListener);
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		ImageButton btnBack = (ImageButton)view.findViewById(R.id.btn_menu_search);
		btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

//				getChildFragmentManager().beginTransaction()
//				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_in_left)
//				.replace(R.id.comment_content, new CommentFragment())
//				.commit();
				
				getParentFragment().getChildFragmentManager().popBackStack();
			}
			
		});
		
		if (mData != null) {
			adapter.setData(mData);
			adapter.notifyDataSetChanged();
		}
		return view;
	}
	
	
	SimpleServiceListener mListener = new SimpleServiceListener(){
		
		@Override
		public void onFollowRequestSuccess() {
			
		}
		
		@Override
		public void onUnFollowRequestFail(int statusCode) {
			
		}	
		
	};
	
	
	
	
}

class LikeAdapter extends BaseAdapter {
	
	private Context mContext;	
	private ArrayList<PortfolioItem> mData;
	
	public interface ILikeAdapterListener {
		public void onFollowListener(View v, int position, PortfolioItem data);
	}
		
	ILikeAdapterListener mListener;
	public void setOnLikeAdapterListener(ILikeAdapterListener listener) {
		mListener = listener;
	}
	public LikeAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<PortfolioItem>();
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
	public long getItemId(int id) {
		return id;
	}
	
	public ArrayList<PortfolioItem> getData() {
		return mData;
	}
	
	public void setData(ArrayList<PortfolioItem> data) {
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		LikeUserCellItemView view = null;
		if (convertView == null) {
	        view = new LikeUserCellItemView(mContext);	
		} else {
			view = (LikeUserCellItemView) convertView;
		}
		view.setData(mData.get(position), position);
		view.setOnLikeUserCellViewListener(new ILikeUserCellItemViewListener() {
			
			@Override
			public void onFollowListener(View v, int position, PortfolioItem data) {
				if (mListener != null) {
					mListener.onFollowListener(v, position, data);
				}
			}
		});
		return view;
	}
	
}
