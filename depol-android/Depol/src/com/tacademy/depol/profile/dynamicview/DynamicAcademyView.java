package com.tacademy.depol.profile.dynamicview;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.DateInfo;
import com.tacademy.depol.profile.dynamicview.DynamicCellView.IDynamicCellListener;
import com.tacademy.depol.profile.dynamicview.HeaderView.HeaderViewListener;
import com.tacademy.depol.profile.dynamicview.TailView.ITailViewListener;

public class DynamicAcademyView extends DynamicView<DateInfo> {

	public DynamicAcademyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DynamicAcademyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DynamicAcademyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void addChildView() {
		
		for (int i = 0; i < mData.size(); i++) {
			// view...
			DynamicAcademyCellView view = new DynamicAcademyCellView(getContext());
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
