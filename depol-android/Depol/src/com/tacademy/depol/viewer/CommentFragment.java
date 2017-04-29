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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.viewer.CommentAdapter.ICommentAdapterListener;
import com.tacademy.depol.viewer.CommentCellItemView.ICommentCellItemViewListener;

public class CommentFragment extends CommentParentFragment {

	EditText editText;
	ListView listview;
	
	CommentAdapter adapter;
	ArrayList<PortfolioItem> mData;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new CommentAdapter(getActivity());

		Bundle bundle = getArguments();
		if (bundle != null) {
			id = bundle.getInt(PORTFOLIO_ID_KEY);
			userid = bundle.getInt(PORTFOLIO_USER_ID_KEY);
			likeCount = bundle.getInt(LIKE_COUNT_ID_KEY);
		}
		
		ServiceAPI.getInstance().RequestComment(getActivity(), id, new SimpleServiceListener<ArrayList<PortfolioItem>>(){
				
				@Override
				public void onPortfolioCommentRequestFail(int statusCode) {
					
				}
				
				@Override
				public void onPortfolioCommentRequestSuccess(ArrayList<PortfolioItem> data) {
					mData = data;
					adapter.setData(mData);
				}
				
		});

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.viewer_comment_layout, container, false);
		
		editText = (EditText)view.findViewById(R.id.comment_editText);
		TextView commentTitle= (TextView)view.findViewById(R.id.comment_title);
		
		String like = String.format("%d", likeCount);
		String desc = getString(R.string.like_user_type1);
		int lengtn = like.length();

		SpannableStringBuilder sb = new SpannableStringBuilder(like + desc);
		sb.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, lengtn, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		commentTitle.setText(sb);
		listview = (ListView)view.findViewById(R.id.comment_listview);
		listview.setAdapter(adapter);
		adapter.setOnCommentAdapterListener(new ICommentAdapterListener(){

			@Override
			public void onCommentAdapterRemove(int commentId) {
				ServiceAPI.getInstance().RequestCommentRemove(getActivity(), commentId, id, new SimpleServiceListener<ArrayList<PortfolioItem>>(){
					@Override
					public void onPortfolioCommentRemoveRequestSuccess(ArrayList<PortfolioItem> data) {

						mData = data;
						adapter.setData(mData);
					}
					
					@Override
					public void onPortfolioCommentRemoveRequestFail(
							int statusCode) {
						
					}
				});
				
			}
			
		});
		
	

		
		ImageButton btnLikeUser = (ImageButton)view.findViewById(R.id.btn_message_replay);
		btnLikeUser.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Bundle bundle = new Bundle();
				bundle.putInt(PORTFOLIO_ID_KEY, id);
				bundle.putInt(PORTFOLIO_USER_ID_KEY, userid);
				bundle.putInt(LIKE_COUNT_ID_KEY, likeCount);
				
				LikeUserFragment likeUserFragment = new LikeUserFragment();
				likeUserFragment.setArguments(bundle);
				
				getParentFragment().getChildFragmentManager().beginTransaction()
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
				.replace(R.id.comment_content, likeUserFragment)
				.addToBackStack(null)
				.commit();
			}
			
		});

		ImageButton btn = (ImageButton)view.findViewById(R.id.btn_comment_send);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String text = editText.getText().toString();
				editText.setText("");
				listview.setSelection(adapter.getCount()-1);
				ServiceAPI.getInstance().RequestCommentWrite(getActivity(), id, userid, text,  new SimpleServiceListener<ArrayList<PortfolioItem>>(){
					
					@Override
					public void onPortfolioCommentWriteRequestSuccess(ArrayList<PortfolioItem> data) {
						mData = data;
						adapter.setData(mData);
					}
					
					@Override
					public void onPortfolioCommentWriteRequestFail(
							int statusCode) {
						super.onPortfolioCommentWriteRequestFail(statusCode);
					}
				});
			}
		});
		

		return view;
	}
	
	
	
	
}

class CommentAdapter extends BaseAdapter {
	
	private Context mContext;	
	private ArrayList<PortfolioItem> mData;
	
	public interface ICommentAdapterListener {
		public void onCommentAdapterRemove(int commentId);
	}
	ICommentAdapterListener mListenr;
	public void setOnCommentAdapterListener(ICommentAdapterListener listener) {
		mListenr = listener;
	}
	
	public CommentAdapter(Context context) {
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
	
	public void setData(ArrayList<PortfolioItem> data) {
		mData = data;
		notifyDataSetChanged();
	}
	
	public void add(PortfolioItem data) {
		mData.add(data);
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		CommentCellItemView view = null;
		if (convertView == null) {
	        view = new CommentCellItemView(mContext);	
		} else {
			view = (CommentCellItemView) convertView;
		}
		view.setData(mData.get(position));
		view.setOnCommentCellItemViewListener(new ICommentCellItemViewListener() {
			
			@Override
			public void onCommentCellRemove(int commentId) {
				if (mListenr != null) {
					mListenr.onCommentAdapterRemove(commentId);
				}
			}
		});
		
		return view;
	}
	
}
