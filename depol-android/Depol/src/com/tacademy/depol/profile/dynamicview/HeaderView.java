package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;

public class HeaderView extends LinearLayout {
	
	private TextView titleView;
	private ImageButton btnEdit;
	private ImageButton btnAdd;
	
	public interface HeaderViewListener {
		public void onEditButtonClick(View v);
		public void onAddButtonClick(View v);
	}
	
	HeaderViewListener mListener;
	public void setOnHeaderViewListener(HeaderViewListener listener) {
		mListener = listener;
	}
	public HeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HeaderView(Context context) {
		super(context);
		init();
	}

	public void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_dynamicview_header, this);
		titleView = (TextView)view.findViewById(R.id.header_title);
		//titleView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
		Typeface typeface = Font.get(Font.NANUM_BARUN_GOTHIC_BOLD, getContext());
		titleView.setTypeface(typeface);
		btnEdit = (ImageButton)view.findViewById(R.id.header_info_edit);
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onEditButtonClick(v);
				}
			}
		});		
		btnAdd = (ImageButton)view.findViewById(R.id.header_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onAddButtonClick(v);
				}
			}
		});
	}
	
	public void setTitle(String title) {
		titleView.setText(title);
	}
	
	public void showMode() {
		btnAdd.setVisibility(View.VISIBLE);
	}

	public void hideMode() {
		btnAdd.setVisibility(View.GONE);
	}

	public void hideEditMode() {
		btnEdit.setVisibility(View.GONE);
	}
	
	public void showEditMode() {
		btnEdit.setVisibility(View.VISIBLE);
	}
	
	public void addViewImage(int resId) {
		btnAdd.setImageResource(resId);
	}
}
