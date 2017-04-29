package com.tacademy.depol.viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.tacademy.depol.R;

public class CommentParentFragment extends ShareFragment {

	public final static String LIKE_COUNT_ID_KEY = "LIKE_COUNT_ID_KEY";
	public final static String PORTFOLIO_ID_KEY = "PORTFOLIO_KEY";
	public final static String PORTFOLIO_USER_ID_KEY = "PORTFOLIO_USER_ID_KEY";
	public boolean isLikeUserActive = false;
	
	int id;
	int userid;
	int likeCount;
	private Bundle bundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.viewer_comment_parent_layout, container, false);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		bundle = getArguments();
		if (bundle != null) {			
			id = bundle.getInt(PORTFOLIO_ID_KEY);
			userid = bundle.getInt(PORTFOLIO_USER_ID_KEY);
			likeCount = bundle.getInt(LIKE_COUNT_ID_KEY);
			
			replaceCommentFragment(bundle);
		}		
		return view;
	}

	private void replaceCommentFragment(Bundle bundle) {
		CommentFragment commentFragment = new CommentFragment();
		commentFragment.setArguments(bundle);
		
		getChildFragmentManager().beginTransaction()
		.replace(R.id.comment_content, commentFragment)
		.commit();
	}
	
}
