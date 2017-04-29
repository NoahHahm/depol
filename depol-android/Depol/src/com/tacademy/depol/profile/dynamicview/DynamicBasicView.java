package com.tacademy.depol.profile.dynamicview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.profile.dynamicview.TailView.ITailViewListener;

public class DynamicBasicView extends DynamicView<BasicInfo> {

	private BasicInfo mData;
	private DynamicBasicCellView view;
	private String headerTitle;

	public DynamicBasicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DynamicBasicView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynamicBasicView(Context context) {
		super(context);
	}
	
	public void setData(BasicInfo data, String headerTitle) {
		this.mData = data;
		this.headerTitle = headerTitle;
	}
	
	@Override
	public void populate(boolean isEdit) {

		isEditMode = isEdit;
				
		removeAllViews();
		addHeaderView(headerTitle);	
		headerView.hideMode();	
		addChildView();
		addTailView();
		
		if (isEdit) {
			view.hideMode();
			view.showMode();
		}
	}
	
	@Override
	protected void addChildView() {
		// view...
		view = new DynamicBasicCellView(getContext());
		view.setData(mData);
		view.setEditMode(isEditMode);
		view.hideMode();
		addView(view);
	}
	
	@Override
	public void addTailView() {
		super.addTailView();
		tailView.setOnTailViewListener(new ITailViewListener() {
			
			@Override
			public void onSaveClickListener(View v) {
				if (mListener != null) {
					mListener.onSaveClickListener(v, view.getData());
				}
			}
			
			@Override
			public void onCancelClickListener(View v) {				
				if (mListener != null) {
					mListener.onCancelClickListener(v);
				}
			}
		});
	}
}
