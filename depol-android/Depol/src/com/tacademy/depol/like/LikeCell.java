package com.tacademy.depol.like;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.LikeItem;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.UserRoundedImageView;

public class LikeCell extends RelativeLayout implements Checkable {
	
	private UserRoundedImageView userImageView;
	private ImageView portfolioImageView;
	private TextView descView;
	private TextView timeView;
	private LikeItem mData;
	private DisplayImageOptions options;
	boolean isChecked = false;
	boolean isRead;

	public LikeCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LikeCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LikeCell(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.like_listview_cell_item_layout, this);
		timeView = (TextView)view.findViewById(R.id.menu_like_timeView);
		portfolioImageView = (ImageView)view.findViewById(R.id.menu_like_imageView);
		userImageView = (UserRoundedImageView)view.findViewById(R.id.menu_like_userImageView);
		descView = (TextView)view.findViewById(R.id.menu_like_desc);

		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
		drawCheck();		
	}
	
	public void setData(LikeItem data) {
		mData = data;
		
		String desc = mData.userName + getResources().getString(R.string.like_user_type2);		
		SpannableStringBuilder sb = new SpannableStringBuilder(desc);
		int lengtn = mData.userName.length();
		sb.setSpan(new ForegroundColorSpan(Color.WHITE), 0, lengtn, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//		descView.append(sb);
//		descView.append(getResources().getString(R.string.like_user_type2));
		descView.setText(sb);
		isRead = data.isRead == 1 ? true : false;
		if (!isRead) {
			setBackgroundColor(getResources().getColor(R.color.C_353535));
		}
		ImageLoader.getInstance().displayImage(mData.thumbImgUri, portfolioImageView, options);
		ImageLoader.getInstance().displayImage(mData.userPropicUri, userImageView, options);
		userImageView.setColor(GraphicsUtil.ConvertStrokeColor(mData.userRecruitStatus));
		
		final int myId = PropertyManager.getInstance().getMyData().userId;
		userImageView.setOnClickListener(new OnClickListener() {
			
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
	

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setChecked(boolean checked) {
						
		if (isChecked != checked)  {
			isChecked = checked;
			drawCheck();
		}
		
	}

	@Override
	public void toggle() {
		isChecked = !isChecked;
		drawCheck();
	}
	
	
	private void drawCheck() {		
		if (isChecked) {
			setBackgroundColor(getResources().getColor(R.color.main_background_color));
		} else {
			setBackgroundColor(getResources().getColor(R.color.main_background_color));
		}
	}

}
