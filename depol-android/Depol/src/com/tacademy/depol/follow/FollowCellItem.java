package com.tacademy.depol.follow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class FollowCellItem extends RelativeLayout {
	
	protected TextView followName;
	protected TextView followJob;
	protected ImageView followCheck;
	protected PortfolioItem mData;
	protected boolean isFollow;
	protected int position;
	protected UserRoundedImageView followUserImageView;
	protected DisplayImageOptions option;
	
	public interface IFollowCellListener {
		public void onFollowClick(View v, PortfolioItem data, int position);
	}
	
	IFollowCellListener mListener;
	public void setOnFollowListener(IFollowCellListener listener) {
		mListener = listener;
	}
	
	public FollowCellItem(Context context) {
		super(context);
		init();
	}
	
	public FollowCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public FollowCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	protected void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_follow_cellitem_layout, this);
		followUserImageView = (UserRoundedImageView)view.findViewById(R.id.follow_imageView);		
		
		followCheck = (ImageView)view.findViewById(R.id.my_like);
		followName = (TextView)view.findViewById(R.id.follow_name);
		followName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_LITGT));
		followJob = (TextView)view.findViewById(R.id.follow_job);
		followJob.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_LITGT));
		
		followCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isFollow) {
					followCheck.setImageResource(R.drawable.follow_uncheck);					
					isFollow = false;
				} else {
					followCheck.setImageResource(R.drawable.follow_check);	
					isFollow = true;
				}
				if (mListener != null) {
					mListener.onFollowClick(v, mData, position);
				}				
			}
			
		});
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		option = ImageLoaderManager.getInstance().getDisplayImageOptions();
		//setBackgroundColor(getResources().getColor(R.color.main_background_color));
	}
	
	public void setData(int position, PortfolioItem data) {
		mData = data;
		this.position = position;
		followName.setText(mData.userName);
		followJob.setText(mData.userPosition);	
		
		isFollow = mData.isFollowed == 1 ? true : false;
		
		if (isFollow) {
			followCheck.setImageResource(R.drawable.follow_check);
		} else {
			followCheck.setImageResource(R.drawable.follow_uncheck);
		}
		
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
