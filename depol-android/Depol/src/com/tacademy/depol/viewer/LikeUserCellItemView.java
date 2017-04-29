package com.tacademy.depol.viewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.UserRoundedImageView;

public class LikeUserCellItemView extends LinearLayout {
	
	public interface ILikeUserCellItemViewListener {
		public void onFollowListener(View v, int position, PortfolioItem data);
	}
	ILikeUserCellItemViewListener mListener;
	
	public void setOnLikeUserCellViewListener(ILikeUserCellItemViewListener listener) {
		mListener = listener;
	}
	
	private UserRoundedImageView userPictureView;
	PortfolioItem mData;
	TextView userName;
	TextView userJob;
	ImageButton followButton;
	int position;
	
	DisplayImageOptions option;

	public LikeUserCellItemView(Context context) {
		super(context);
		init();
	}	
	
	public LikeUserCellItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.viewer_likeuser_listview_cell_layout, this);
		userPictureView = (UserRoundedImageView)view.findViewById(R.id.likeUserPictureView);

		userName = (TextView)view.findViewById(R.id.like_user_title);
		userJob = (TextView)view.findViewById(R.id.info_content);

		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		option = ImageLoaderManager.getInstance().getDisplayImageOptions();
		
		followButton = (ImageButton)view.findViewById(R.id.follow_imageView);
		followButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onFollowListener(v, position, mData);
				}
			}
			
		});
	}	
	
	public void setData(PortfolioItem data, int position) {
		mData = data;
		this.position = position;
		userName.setText(mData.userName);
		userJob.setText(mData.userPosition);
		boolean isFollow = mData.isFollowed == 1 ? true : false;
		if (isFollow) {
			followButton.setImageResource(R.drawable.follow_check);
		} else {
			followButton.setImageResource(R.drawable.follow_uncheck);			
		}
		
		if (mData.userId == PropertyManager.getInstance().getMyData().userId) {
			followButton.setVisibility(View.GONE);
		}
		ImageLoader.getInstance().displayImage(mData.userPropicUri, userPictureView, option);
		userPictureView.setColor(GraphicsUtil.ConvertStrokeColor(mData.userRecruitStatus));

		final int myId = PropertyManager.getInstance().getMyData().userId;
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