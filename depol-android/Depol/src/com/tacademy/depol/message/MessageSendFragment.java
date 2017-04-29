package com.tacademy.depol.message;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.data.PortfolioItem;

public class MessageSendFragment extends SherlockFragment {

	public final static int MESSAGE_REPLAY = 1;
	public final static int MESSAGE_WRITE = 2;
	public final static int MESSAGE_PROFILE = 3;
	
	public final static String MESSAGE_SEND_MODE = "MESSAGE_SEND_MODE";
	public final static String MESSAGE_USER_ID_KEY = "MESSAGE_USER_ID_KEY";
	public final static String MESSAGE_USER_NAME_KEY = "MESSAGE_USER_NAME_KEY";
	public final static String MESSAGE_REQUEST = "MESSAGE_REQUEST";
	
	
	ArrayList<PortfolioItem> mData;
	TextView usersView;
	EditText writeEditText;
	int replayUserId;
	int sendMode;
	
	public void setOnSimpleMessageListener(SimpleMessageListener listener) {
		messageListener = listener;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.message_send, container, false);
		usersView = (TextView)view.findViewById(R.id.message_usersView);
		writeEditText = (EditText)view.findViewById(R.id.message_writeEditText);

		Bundle bundle = getArguments();
		sendMode = bundle.getInt(MESSAGE_SEND_MODE);
		

		//액션바
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.message_send), getString(R.string.ok_kor));
		actionbar.setCustomView(actionBarView);
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {

				String desc = writeEditText.getText().toString();
				if (desc == null || desc.equals("")) {
					return;
				}
				
				
				//리플 모드
				if (sendMode == MESSAGE_REPLAY) {
					int[] id = {replayUserId};

					Bundle bundle = new Bundle();
					bundle.putIntArray(MessageDialogFragment.MESSAGE_USER_ID_KEY, id);
					bundle.putString(MessageDialogFragment.MESSAGE_TEXT_KEY, desc);
					
					MessageDialogFragment dialog = new MessageDialogFragment();
					dialog.setArguments(bundle);
					dialog.setOnSimpleMessageListener(new SimpleMessageListener(){
						@Override
						public void onMessageSend() {
							if (MessageSendFragment.this == null || getActivity() == null) return;
							
							mData = null;
							writeEditText.setText("");	
							usersView.setText("");
							
//							if (messageListener != null) {
//								messageListener.onMessageRead();
//							}
							
							getActivity().getSupportFragmentManager().popBackStack();
						}
					});
					dialog.show(getChildFragmentManager(), MessageDialogFragment.MESSAGE_SEND);
					return;
				}
				
				
				
				
				//다중 사용자
				if (mData == null) return;			
				int[] id = new int[mData.size()];			
				for(int i=0;i<mData.size();i++) {					
					id[i] = mData.get(i).userId;
				}

				Bundle bundle = new Bundle();
				bundle.putIntArray(MessageDialogFragment.MESSAGE_USER_ID_KEY, id);
				bundle.putString(MessageDialogFragment.MESSAGE_TEXT_KEY, desc);
				
				MessageDialogFragment dialog = new MessageDialogFragment();
				dialog.setArguments(bundle);
				dialog.show(getChildFragmentManager(), MessageDialogFragment.MESSAGE_SEND);
				dialog.setOnSimpleMessageListener(messageListener);
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		
		ImageButton btn = (ImageButton)view.findViewById(R.id.btn_message_send_list);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MessageUserListDialogFragment<ArrayList<MessageItem>> dialog = new MessageUserListDialogFragment<ArrayList<MessageItem>>();
				dialog.show(getChildFragmentManager(), "");
				dialog.setOnUserListDialogListener(new SimpleMessageListener<ArrayList<PortfolioItem>>() {
					
					@Override
					public void onConfirmDialog(ArrayList<PortfolioItem> data) {
						usersView.setText("");
						
						mData = data;
						StringBuffer buf = new StringBuffer();
						for(PortfolioItem item : data) {
							buf.append(item.userName + ", ");
						}
						usersView.setText(buf.toString());
					}
				});
			}			
		});

		if (sendMode == MESSAGE_REPLAY) {
			replayUserId = bundle.getInt(MESSAGE_USER_ID_KEY);
			String name = bundle.getString(MESSAGE_USER_NAME_KEY);	
			usersView.setText(name);		
			btn.setVisibility(View.GONE);
		}
		
		return view;
	}
	
	
	SimpleMessageListener messageListener = new SimpleMessageListener() {
		
		@Override
		public void onMessageSend() {
			if (MessageSendFragment.this == null || getActivity() == null) return;
			
			mData = null;
			writeEditText.setText("");	
			usersView.setText("");
			
//			if (messageListener != null) {
//				messageListener.onMessageRead();
//			}
			
			getActivity().getSupportFragmentManager().popBackStack();
			
		}
		
		@Override
		public void onMessageSendFail(int statusCode) {
			if (MessageSendFragment.this == null || getActivity() == null) return;
			
			Toast.makeText(getActivity(), getResources().getString(R.string.message_send_fail), Toast.LENGTH_SHORT).show();
		}
		
	};
}
