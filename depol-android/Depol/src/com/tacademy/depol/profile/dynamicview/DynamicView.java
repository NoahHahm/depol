package com.tacademy.depol.profile.dynamicview;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tacademy.depol.data.AbilityInfo;
import com.tacademy.depol.profile.dynamicview.DynamicCellView.IDynamicCellListener;
import com.tacademy.depol.profile.dynamicview.HeaderView.HeaderViewListener;
import com.tacademy.depol.profile.dynamicview.TailView.ITailViewListener;

public class DynamicView<T> extends LinearLayout {
	
	public final static boolean EDIT_MODE = true;
	public final static boolean INIT_MODE = false;
	
	boolean isEditMode = false;
	
	protected TailView tailView;
	protected HeaderView headerView;
	
	DynamicViewListener<T> mListener;
	
	protected IDynamicCellListener<T> dynamicCellListener = new IDynamicCellListener<T>() {
		
		@Override
		public void onRemoveClickListener(View v, T data) {
			if (mListener != null) {
				mListener.onRemoveClickListener(v, data);
			}
		}
		
		@Override
		public void onModifyClickListener(View v, T data) {
			if (mListener != null) {
				mListener.onModifyClickListener(v, data);
			}
		}
	};
	
	public void setOnDynamicViewListener(DynamicViewListener<T> listener) {
		mListener = listener;
	}
	
	public DynamicView(Context context) {
		super(context);
		init();
	}

	public DynamicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DynamicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	protected void init() {
		mData = new ArrayList<T>();
		headerView = new HeaderView(getContext());
		tailView = new TailView(getContext());
	}
	
	ArrayList<T> mData;
	String headerTitle;
 
	public void setData(ArrayList<T> data, String headerTitle) {
		mData.clear();
		mData.addAll(data);
		this.headerTitle = headerTitle;
	}
	
	
	public ArrayList<T> getData() {
		return mData;
	}
	
	public void populate(boolean isEdit) {
		isEditMode = isEdit;
				
		removeAllViews();
		addHeaderView(headerTitle);		
		addChildView();
		addTailView();
		headerImageChange();
	}
	
	public void headerImageChange() {
		
	}
	
	
	public void addHeaderView(String headerTitle) {
		headerView.setTitle(headerTitle);
		headerView.showEditMode();
		headerView.setOnHeaderViewListener(new HeaderViewListener() {
			
			@Override
			public void onEditButtonClick(View v) {
				hideEditButton();
				if (mListener != null) {
					mListener.onEditClickListener(v);
				}
			}

			@Override
			public void onAddButtonClick(View v) {
				if (mListener != null) {
					mListener.onAddClickListener(v);
				}
			}
		});
		addView(headerView);
		
		if (isEditMode) {
			headerView.showMode();
		} else {
			headerView.hideMode();
		}
	}
	
	protected void addChildView() {
		
		for (int i = 0; i < mData.size(); i++) {
			// view...
			DynamicCellView<T> view = new DynamicCellView<T>(getContext());
			if (isEditMode) {
				// add editView
				view.setOnDynamicCellListener(dynamicCellListener);
				view.setData(mData.get(i));
				view.showMode();
				addView(view); 
			} else {
				// add showview
				view.setOnDynamicCellListener(dynamicCellListener);
				view.setData(mData.get(i));
				view.hideMode();
				addView(view);
			}
		}
	}
	
	public void addTailView() {
		if (isEditMode) {
			tailView.showMode();
		} else {
			tailView.hideMode();
		}
		tailView.setOnTailViewListener(new ITailViewListener() {
			
			@Override
			public void onSaveClickListener(View v) {
				if (mListener != null) {
					mListener.onSaveClickListener(v, mData);
				}
			}
			
			@Override
			public void onCancelClickListener(View v) {	
				headerView.showEditMode();
				if (mListener != null) {
					mListener.onCancelClickListener(v);
				}
			}
		});
		addView(tailView);
	}
	
	public void hideEditButton() {
		headerView.hideEditMode();		
	}
}
