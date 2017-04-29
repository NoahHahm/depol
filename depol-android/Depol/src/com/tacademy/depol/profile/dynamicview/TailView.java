package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tacademy.depol.R;

public class TailView extends LinearLayout {
	
	public interface ITailViewListener {
		public void onSaveClickListener(View v);
		public void onCancelClickListener(View v);
	}
	
	ITailViewListener mListener;
	
	public void setOnTailViewListener(ITailViewListener listener) {
		mListener = listener;
	}

	public TailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TailView(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_dynamicview_tail, this);
		ImageButton btnSave = (ImageButton)view.findViewById(R.id.info_save);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onSaveClickListener(v);
				}
			}
		});
		ImageButton btnCancel = (ImageButton)view.findViewById(R.id.info_cancle);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onCancelClickListener(v);
				}
			}
		});
	}
	
	public void hideMode() {
		setVisibility(View.GONE);
	}

	public void showMode() {
		setVisibility(View.VISIBLE);
	}
}
