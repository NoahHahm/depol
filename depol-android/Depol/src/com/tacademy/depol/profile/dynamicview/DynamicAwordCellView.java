package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;

import com.tacademy.depol.data.DateInfo;
import com.tacademy.depol.util.DateUtil;

public class DynamicAwordCellView<T> extends DynamicCellView<DateInfo> {

	public DynamicAwordCellView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DynamicAwordCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DynamicAwordCellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setData(DateInfo info) {
		dateInfo = info;
		dateView.setText(DateUtil.convertYYYY(info.year));
		mainTitleView.setText(info.text);
		subTitleView.setText(info.subText);
	}

	

}
