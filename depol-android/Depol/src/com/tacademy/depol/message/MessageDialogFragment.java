package com.tacademy.depol.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tacademy.depol.R;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.LoadDialogFragment;

public class MessageDialogFragment extends LoadDialogFragment {

	public final static String MESSAGE_READ = "MESSAGE_READ";
	public final static String MESSAGE_SEND = "MESSAGE_SEND";

	public final static String MESSAGE_USER_ID_KEY = "MESSAGE_USER_ID_KEY";
	public final static String MESSAGE_TEXT_KEY = "MESSAGE_TEXT_KEY";
	MessageItem mData;
	
	
	SimpleMessageListener mListener;
	public void setOnSimpleMessageListener(SimpleMessageListener listener) {
		mListener = listener;
	}
	Bundle bundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = getArguments();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(LoadDialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View v = inflater.inflate(R.layout.loading_indicator, container, false);
		
		if (getTag().equals(MESSAGE_READ)) {
			mData = bundle.getParcelable(MessageInboxFragment.MESSAGE_OBJECT_KEY);
			ServiceAPI.getInstance().RequestMessageRead(getActivity(), mData.messageId, netWorkListener);
		} else if (getTag().equals(MESSAGE_SEND)) {
			int id[] = bundle.getIntArray(MESSAGE_USER_ID_KEY);
			String message = bundle.getString(MESSAGE_TEXT_KEY);
			
			for(int i=0;i<id.length;i++) {
				ServiceAPI.getInstance().RequestMessageSend(getActivity(), id[i], message, netWorkListener);
			}
		}
		
		return v; 	
	}
	
	

	private SimpleServiceListener netWorkListener = new SimpleServiceListener(){
    	    	
    	@Override
    	public void onMessageReadRequestFail(int statusCode) {
    		dismiss();
			if (mListener != null) {
				mListener.onMessageReadFail(statusCode);
			}
    	}
    	
    	@Override
    	public void onMessageReadRequestSuccess() {
    		dismiss();
			if (mListener != null) {
				mListener.onMessageRead();
			}
    	}

    	public void onMessageSendRequestFail(int statusCode) {

    		dismiss();
			if (mListener != null) {
				mListener.onMessageSendFail(statusCode);
			}
    	}
    	
    	public void onMessageSendRequestSuccess() {
    		Toast.makeText(getActivity(), getResources().getString(R.string.message_send_success), Toast.LENGTH_SHORT).show();
			
    		dismiss();
			if (mListener != null) {
				mListener.onMessageSend();
			}
    	}
    	
    };
}
