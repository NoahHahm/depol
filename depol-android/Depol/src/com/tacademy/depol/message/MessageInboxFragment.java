package com.tacademy.depol.message;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.tacademy.depol.R;
import com.tacademy.depol.data.MessageItem;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.ProfileActivity;

public class MessageInboxFragment extends SherlockFragment {

	public final static String MESSAGE_OBJECT_KEY = "MESSAGE_OBJECT_KEY";
	
	private Bundle bundle;
	private ArrayList<MessageItem> mData;
	private SimpleServiceListener netWorkListener = new SimpleServiceListener<ArrayList<MessageItem>>(){
    	
    	@Override
    	public void onMessageListRequestFail(int statusCode) {
    		
    	}
    	
    	@Override
    	public void onMessageListRequestSuccess(ArrayList<MessageItem> data) {
    		mData = data;
    		adapter.setData(mData);
    	}
    	
    	
    };
	
	

	@Override
    public void onResume() {
    	super.onResume();
    	ServiceAPI.getInstance().RequestMessageInBoxList(getActivity(), netWorkListener);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = new Bundle();
        
    }
	
	View view;
	MessageAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.message_listview_layout, container, false);
		
		
		final SwipeListView listview = (SwipeListView)view.findViewById(R.id.message_inbox_listView);

		adapter = new MessageAdapter(getActivity());
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

        		if (mData.get(position).userId < 1) {
        			return;
        		}
        		
        		bundle.putParcelable(MESSAGE_OBJECT_KEY, mData.get(position));
        		bundle.putInt(MessageSendFragment.MESSAGE_SEND_MODE, MessageSendFragment.MESSAGE_WRITE);
        		bundle.putBoolean(MessageViewerFragment.MESSAGE_RECEVIE_MODE, true);
        		
        		MessageViewerFragment myMessageFragment = new MessageViewerFragment();
        		myMessageFragment.setArguments(bundle);
        		myMessageFragment.setOnSimpleMessageListener(new SimpleMessageListener(){
        			@Override
        			public void onMessageSend() {
        		        ServiceAPI.getInstance().RequestMessageInBoxList(getActivity(), netWorkListener);        				
        			}
        		});

    			if (getActivity() instanceof ProfileActivity) {
    				getActivity().getSupportFragmentManager().beginTransaction()			
    				.replace(R.id.profile_content, myMessageFragment)
    				.addToBackStack(null)
    				.commit();		
    				return;
    			}
    			
    			getActivity().getSupportFragmentManager().beginTransaction()
    			.replace(R.id.content_frame_sub, myMessageFragment)
    			.addToBackStack(null)
    			.commit();
        	}
			
		
		});
		
        listview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);   
        listview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS);
        listview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_NONE);
    
        listview.setSwipeListViewListener(new BaseSwipeListViewListener(){

        	@Override
        	public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                	try {
                		ServiceAPI.getInstance().RequestMessageRemove(getActivity(), adapter.getData().get(position).messageId, netWorkListener);
                		adapter.remove(position);
                	} catch (IndexOutOfBoundsException e) {
                		e.getStackTrace();
                	}
                }
                adapter.notifyDataSetChanged();
        	}
        	
   
        	@Override
        	public void onClickFrontView(int position) {
        		super.onClickFrontView(position);
        		if (mData.get(position).userId < 1) {
        			return;
        		}
        		bundle.putParcelable(MESSAGE_OBJECT_KEY, mData.get(position));
        		bundle.putInt(MessageSendFragment.MESSAGE_SEND_MODE, MessageSendFragment.MESSAGE_WRITE);
        		bundle.putBoolean(MessageViewerFragment.MESSAGE_RECEVIE_MODE, true);
        		
        		MessageViewerFragment myMessageFragment = new MessageViewerFragment();
        		myMessageFragment.setArguments(bundle);
        		myMessageFragment.setOnSimpleMessageListener(new SimpleMessageListener(){
        			@Override
        			public void onMessageSend() {
        		        ServiceAPI.getInstance().RequestMessageInBoxList(getActivity(), netWorkListener);        				
        			}
        		});

    			if (getActivity() instanceof ProfileActivity) {
    				getActivity().getSupportFragmentManager().beginTransaction()			
    				.replace(R.id.profile_content, myMessageFragment)
    				.addToBackStack(null)
    				.commit();		
    				return;
    			}
    			getActivity().getSupportFragmentManager().beginTransaction()
    			.replace(R.id.content_frame_sub, myMessageFragment)
    			.addToBackStack(null)
    			.commit();
        	}
        	
        });
        
        if (mData != null) {
        	adapter.setData(mData);
        }
    	
        
		return view;
	}
}

