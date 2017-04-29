package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tacademy.depol.R;
import com.tacademy.depol.data.AbilityInfo;
import com.tacademy.depol.profile.dynamicview.TailView.ITailViewListener;

public class DynamicInfographicsView extends DynamicView<AbilityInfo> {
	
	public DynamicInfographicsView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public DynamicInfographicsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynamicInfographicsView(Context context) {
		super(context);
	}

	@Override
	public void headerImageChange() {
		headerView.addViewImage(R.drawable.info_add_skill);
	}
	
	@Override
	protected void addChildView() {
		
		for (int i = 0; i < mData.size(); i++) {
			// view...
			DynamicInfographicsCellView view = new DynamicInfographicsCellView(getContext());
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
}
