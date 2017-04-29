package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;

import com.tacademy.depol.R;
import com.tacademy.depol.data.DateInfo;

public class DynamicCareerView extends DynamicView<DateInfo> {

	public DynamicCareerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DynamicCareerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynamicCareerView(Context context) {
		super(context);
	}
	
	@Override
	public void headerImageChange() {
		headerView.addViewImage(R.drawable.info_add_ex);
	}
	@Override
	protected void addChildView() {
		
		for (int i = 0; i < mData.size(); i++) {
			// view...
			DynamicCareerCellView view = new DynamicCareerCellView(getContext());
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
	
}
