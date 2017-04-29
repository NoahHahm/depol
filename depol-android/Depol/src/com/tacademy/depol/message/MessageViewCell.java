package com.tacademy.depol.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.UserRoundedImageView;

public class MessageViewCell extends LinearLayout {

	TextView messageNameView;
	private TextView messageDescription;
	private MessageItem mData;
	private UserRoundedImageView userImageView;
	private DisplayImageOptions options;
	
	
	public MessageViewCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MessageViewCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MessageViewCell(Context context) {
		super(context);
		init();
	}
	View frontView;
	View view;
	private void init() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.message_listview_cell_layout, this);

        frontView = (View)view.findViewById(R.id.front);
        
        userImageView = (UserRoundedImageView)view.findViewById(R.id.message_profile_picture_view);
        messageNameView = (TextView)view.findViewById(R.id.message_username_textview);
        messageDescription = (TextView)view.findViewById(R.id.message_desc_textview);
        messageDescription = (TextView)view.findViewById(R.id.message_desc_textview);
        
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
	}
	
	public void setData(MessageItem data) {
		mData = data;
		
		messageNameView.setText(mData.userName);
		messageDescription.setText(mData.text);
		boolean isRead = data.isRead == 1 ? true : false;
		if (!isRead) {
			view.setBackgroundColor(getResources().getColor(R.color.C_353535));
		}
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
}
