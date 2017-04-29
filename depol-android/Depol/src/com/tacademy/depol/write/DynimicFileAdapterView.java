package com.tacademy.depol.write;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tacademy.depol.R;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.write.DynimicFileView.IDynimicFileViewListener;

public class DynimicFileAdapterView extends LinearLayout {
	
	ArrayList<MultplePhotoItem> mData;
	
	public interface IDynimicFileAdapterListener {
		public void onAddViewClickListener();
		public void onRemoveListener(MultplePhotoItem data);
	}
	IDynimicFileAdapterListener mListener;
	
	public void setOnDynimicFileAdapterListener(IDynimicFileAdapterListener listener) {
		mListener = listener;
	}

	public DynimicFileAdapterView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public DynimicFileAdapterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynimicFileAdapterView(Context context) {
		super(context);
	}
	
	public void setData(ArrayList<MultplePhotoItem> data) {
		mData = data;
	}
	
	public ArrayList<MultplePhotoItem> getData() {
		return mData;		
	}
	
	public void populate() {
		removeAllViews();		
		addChildView();
	}
	
	public void addChildView() {
		for(int i=0;i<mData.size();i++) {
			DynimicFileView view = new DynimicFileView(getContext());
			view.setOnDynimicFileViewListener(new IDynimicFileViewListener() {
				
				@Override
				public void onClickListener(MultplePhotoItem data) {
					if (mListener != null) {
						mListener.onRemoveListener(data);
					}
				}
			});
			if (i == 0) {
				view.showFirst();
			}
			view.setData(mData.get(i));
			addView(view);
		}

		if (mData.size() < 10) {
			ImageView addView = new ImageView(getContext());
			addView.setImageResource(R.drawable.port_th_add);
			addView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						mListener.onAddViewClickListener();
					}
				}
			});
			addView(addView);
			ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(addView.getLayoutParams());
			margin.setMargins(0, 0, (int) getResources().getDimension(R.dimen.photo_margin_right), 0);
			addView.setLayoutParams(new LinearLayout.LayoutParams(margin));
		}
	}

}
