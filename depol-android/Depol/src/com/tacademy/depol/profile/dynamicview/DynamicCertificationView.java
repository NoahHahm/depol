package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;

import com.tacademy.depol.R;
import com.tacademy.depol.data.DateInfo;

public class DynamicCertificationView extends DynamicView<DateInfo> {

	public DynamicCertificationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DynamicCertificationView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynamicCertificationView(Context context) {
		super(context);
	}
		
	
	@Override
	public void headerImageChange() {
		headerView.addViewImage(R.drawable.info_add_license);
	}
	
	@Override
	protected void addChildView() {
		
		for (int i = 0; i < mData.size(); i++) {
			// view...
			DynamicCertificationCellView view = new DynamicCertificationCellView(getContext());
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
