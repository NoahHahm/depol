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

public class DynamicCellView<T> extends LinearLayout {

	private ImageButton btnModify;
	private ImageButton btnRemove;
	protected TextView dateView;
	protected TextView mainTitleView;
	protected TextView subTitleView;
	protected T dateInfo;
	
	public interface IDynamicCellListener<T> {
		public void onModifyClickListener(View v, T data);
		public void onRemoveClickListener(View v, T data);
	}
	IDynamicCellListener<T> mListener;
	
	public void setOnDynamicCellListener (IDynamicCellListener<T> listener) {
		mListener = listener;
	}
	
	public DynamicCellView(Context context) {
		super(context);
		init();
	}

	public DynamicCellView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DynamicCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_dynamicview, this);
		
		dateView = (TextView)view.findViewById(R.id.dynamic_dateView);
		Typeface typeface = Font.get(Font.NANUM_BARUN_GOTHIC, getContext());
		dateView.setTypeface(typeface);
		
		mainTitleView = (TextView)view.findViewById(R.id.dynamic_mainTitle);
		typeface = Font.get(Font.NANUM_BARUN_GOTHIC, getContext());
		mainTitleView.setTypeface(typeface);
		
		subTitleView = (TextView)view.findViewById(R.id.dynamic_subTitle);
		typeface = Font.get(Font.NANUM_BARUN_GOTHIC, getContext());
		subTitleView.setTypeface(typeface);
		
		
		btnModify = (ImageButton)view.findViewById(R.id.dynamic_modify);
		btnModify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onModifyClickListener(v, dateInfo);
				}
			}
		});
		btnRemove = (ImageButton)view.findViewById(R.id.dynamic_remove);
		btnRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRemoveClickListener(v, dateInfo);
				}
			}
		});
	}
	
	public void setData(T info) {
		/*
		 * 
		dateInfo = info;
		String dateText = DateUtil.convertYYYYMM(info.startYear, info.startMonth) + " - " + DateUtil.convertYYYYMM(info.endYear, info.endMonth);
		dateView.setText(dateText);
		mainTitleView.setText(info.text);
		subTitleView.setText(info.subText);
		 */
	}
	
	public void hideMode() {
		btnModify.setVisibility(View.GONE);
		btnRemove.setVisibility(View.GONE);
		mainTitleView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		subTitleView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void showMode() {
		btnModify.setVisibility(View.VISIBLE);
		btnRemove.setVisibility(View.VISIBLE);
	}

}
