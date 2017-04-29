package com.tacademy.depol.viewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.UserRoundedImageView;

public class CommentCellItemView extends LinearLayout {
	
	private UserRoundedImageView userPictureView;
	PortfolioItem mData;
	TextView userName;
	TextView desc;
	TextView timeView;
	DisplayImageOptions option;
	ImageButton btnRemove;

	public interface ICommentCellItemViewListener {
		public void onCommentCellRemove(int commentId);
	}
	ICommentCellItemViewListener mListenr;
	
	public void setOnCommentCellItemViewListener(ICommentCellItemViewListener listener) {
		mListenr = listener;
	}
	
	public CommentCellItemView(Context context) {
		super(context);
		init();
	}	
	
	public CommentCellItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.viewer_comment_cellitem_layout, this);
		userPictureView = (UserRoundedImageView)view.findViewById(R.id.likeUserPictureView);
		userName = (TextView)view.findViewById(R.id.comment_userName);
		userName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_MEDIUM));
		desc = (TextView)view.findViewById(R.id.newView);
		desc.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_LITGT));
		timeView = (TextView)view.findViewById(R.id.versionOldView);
		timeView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_LITGT));
		btnRemove = (ImageButton)view.findViewById(R.id.btn_replay_remove);
		btnRemove.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mListenr != null) {
					mListenr.onCommentCellRemove(mData.commentId);
				}
			}
			
		});

		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		option = ImageLoaderManager.getInstance().getDisplayImageOptions();
	}	
	
	public void setData(PortfolioItem data) {
		mData = data;
		
		userName.setText(mData.userName);
		desc.setText(mData.text);
		timeView.setText(mData.timeStamp);
		
		ImageLoader.getInstance().displayImage(mData.userPropicUri, userPictureView, option);
		userPictureView.setColor(GraphicsUtil.ConvertStrokeColor(mData.userRecruitStatus));

		final int myId = PropertyManager.getInstance().getMyData().userId;
		if (mData.userId == myId) {
			btnRemove.setVisibility(View.VISIBLE);
		}
		userPictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getContext(), ProfileActivity.class);
				boolean isMine = false;
				if (mData.userId == myId) {
					isMine = true;
					intent.putExtra(ProfileActivity.PROFILE_USER_ID_KEY, myId);
				} else {
					intent.putExtra(ProfileActivity.PROFILE_USER_ID_KEY, mData.userId);
				}
				intent.putExtra(ProfileActivity.PROFILE_TYPE_KEY, isMine);
				getContext().startActivity(intent);
			}
		});
	}
}