package com.tacademy.depol.message;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.UserRoundedImageView;

public class MessageViewerFragment extends SherlockFragment {
	
	public final static String MESSAGE_RECEVIE_MODE = "MESSAGE_RECEVIE_MODE";
	
	MessageItem mData;
	TextView userNameView;
	TextView userJobView;
	TextView timeView;
	TextView descView;
	UserRoundedImageView imageView;
	DisplayImageOptions options;
	MessageDialogFragment dialog;
	boolean isRead = false;
	boolean isRecevie = false;
	ImageButton btnFollow;
	
	SimpleMessageListener messageListener;
	public void setOnSimpleMessageListener(SimpleMessageListener listener) {
		messageListener = listener;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();

		dialog = new MessageDialogFragment();
		dialog.setOnSimpleMessageListener(new SimpleMessageListener() {
			
			@Override
			public void onMessageRead() {
				mData.isRead = 1;
				descView.setText(mData.text);				
			}
			@Override
			public void onMessageReadFail(int statusCode) {
				mData.isRead = 0;
				getActivity().getSupportFragmentManager().popBackStack();
			}
			
		});
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			dialog.setArguments(bundle);
			mData = bundle.getParcelable(MessageInboxFragment.MESSAGE_OBJECT_KEY);	
			isRecevie = bundle.getBoolean(MESSAGE_RECEVIE_MODE);
			boolean isRead = mData.isRead == 1 ? true : false;
			if (!isRead) {
				dialog.show(getChildFragmentManager(), MessageDialogFragment.MESSAGE_READ);
			}

		}
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.message_inbox, container, false);
		
		imageView = (UserRoundedImageView)view.findViewById(R.id.message_in_userPictureView);
		userNameView = (TextView)view.findViewById(R.id.message_in_userNameView);
		userJobView = (TextView)view.findViewById(R.id.message_in_userJobView);
		timeView = (TextView)view.findViewById(R.id.message_in_timeView);
		descView = (TextView)view.findViewById(R.id.message_in_descView);
		if (mData != null) {
			userNameView.setText(mData.userName);
			userJobView.setText(mData.userPosition);
			timeView.setText(mData.timeStamp);	
			ImageLoader.getInstance().displayImage(mData.userPropicUri, imageView, options);
			imageView.setColor(GraphicsUtil.ConvertStrokeColor(mData.userRecruitStatus));
		}
		

		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		if (isRecevie) {
			actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.message_recevie));
		} else {
			actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.message_post));
		}
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		actionbar.setCustomView(actionBarView);
		
		
		
		
		
		if (mData != null) {
			boolean isRead = mData.isRead == 1 ? true : false;
			if (isRead) {
				descView.setText(mData.text);				
			}
		}

		

		ImageButton btn = (ImageButton)view.findViewById(R.id.btn_message_replay);
		if (!isRecevie) btn.setVisibility(View.GONE);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(MessageSendFragment.MESSAGE_SEND_MODE, MessageSendFragment.MESSAGE_REPLAY);
				bundle.putInt(MessageSendFragment.MESSAGE_USER_ID_KEY, mData.userId);
				bundle.putString(MessageSendFragment.MESSAGE_USER_NAME_KEY, mData.userName);
				MessageSendFragment sendFragment = new MessageSendFragment();
        		sendFragment.setArguments(bundle); 
        		sendFragment.setOnSimpleMessageListener(new SimpleMessageListener(){
        			
        			@Override
        			public void onMessageSend() {
        				if (messageListener != null) {
        					messageListener.onMessageSend();
        				}
        			}
        		});
        		

    			if (getActivity() instanceof ProfileActivity) {
    				getActivity().getSupportFragmentManager().beginTransaction()			
    				.replace(R.id.profile_content, sendFragment)
    				.addToBackStack(null)
    				.commit();		
    				return;
    			}
    			
    			getActivity().getSupportFragmentManager().beginTransaction()
    			.replace(R.id.content_frame_sub, sendFragment)
    			.addToBackStack(null)
    			.commit();
			}
			
		});
		btnFollow = (ImageButton)view.findViewById(R.id.btn_message_follow);
		if (!isRecevie) btnFollow.setVisibility(View.GONE);
			
		boolean isFollow = mData.isFollowed == 1 ? true : false;
		if (isFollow) {
			btnFollow.setImageResource(R.drawable.message_follow_act);
		} else {
			btnFollow.setImageResource(R.drawable.message_follow_btn);			
		}
		btnFollow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean isFollow = mData.isFollowed == 1 ? true : false;
				
				if (isFollow) {
					mData.isFollowed = 0;
					btnFollow.setImageResource(R.drawable.message_follow_btn);
					ServiceAPI.getInstance().RequestUnFollow(getActivity(), mData.userId, networkListener);						
				} else {
					mData.isFollowed = 1;
					btnFollow.setImageResource(R.drawable.message_follow_act);
					ServiceAPI.getInstance().RequestFollow(getActivity(), mData.userId, networkListener);							
				}
			}
			
		});
		
		
		return view;
	}
	
	
	SimpleServiceListener networkListener = new SimpleServiceListener(){
		
		@Override
		public void onUnFollowRequestFail(int statusCode) {

		}
		
		@Override
		public void onUnFollowRequestSuccess() {
			if (getActivity() == null) return;			
		}
		
		@Override
		public void onFollowRequestSuccess() {
			if (getActivity() == null) return;
		}
		
		@Override
		public void onFollowRequestFail(int statusCode) {
			
		}
	};
}
