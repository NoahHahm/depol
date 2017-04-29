package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tacademy.depol.data.DateInfo;

public class DynamicCertificationCellView extends DynamicCellView<DateInfo> {

	public DynamicCertificationCellView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DynamicCertificationCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DynamicCertificationCellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() {
		super.init();
		subTitleView.setVisibility(View.GONE);
	}

	@Override
	public void setData(DateInfo info) {
		dateInfo = info;
		dateView.setText(null);
		mainTitleView.setText(info.text);
		subTitleView.setText(null);
	}
	

}
