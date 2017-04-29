package com.tacademy.depol.follow;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;

public class FollowIngCellItem extends FollowCellItem {

	public FollowIngCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FollowIngCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FollowIngCellItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		super.init();		
		
		followCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onFollowClick(v, mData, position);
				}				
			}
			
		});
	}
	
	@Override
	public void setData(int position, PortfolioItem data) {
		mData = data;
		this.position = position;
		followName.setText(mData.userName);
		followJob.setText(mData.userPosition);	
		followCheck.setImageResource(R.drawable.follow_check);

		if (mData.userId == PropertyManager.getInstance().getMyData().userId) {
			followCheck.setVisibility(View.GONE);
		}

		ImageLoader.getInstance().displayImage(mData.userPropicUri, followUserImageView, option);
		followUserImageView.setColor(GraphicsUtil.ConvertStrokeColor(mData.userRecruitStatus));
		
		final int myId = PropertyManager.getInstance().getMyData().userId;
		followUserImageView.setOnClickListener(new OnClickListener() {
			
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
