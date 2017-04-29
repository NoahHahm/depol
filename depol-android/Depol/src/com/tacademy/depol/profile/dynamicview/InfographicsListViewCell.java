package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tacademy.depol.R;

public class InfographicsListViewCell extends LinearLayout implements Checkable {

	private boolean isChecked = false;
	private View view;
	private TextView titleView;
	private View checkView;
	
	public InfographicsListViewCell(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public InfographicsListViewCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InfographicsListViewCell(Context context) {
		super(context);
		init();
	}
	
	public void init() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.profile_infographics_cellitem_layout, this);
		titleView = (TextView)view.findViewById(R.id.infogriphis_title);
		checkView = (View)view.findViewById(R.id.infographics_checkView);
		
		drawCheck();
	}
	
	public void setData(String title) {
		titleView.setText(title);
	}


	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setChecked(boolean checked) {
						
		if (isChecked != checked)  {
			isChecked = checked;
			drawCheck();
		}
		
	}

	@Override
	public void toggle() {
		isChecked = !isChecked;
		drawCheck();
	}
	
	
	private void drawCheck() {		
		if (isChecked) {
			checkView.setBackgroundResource(R.drawable.profile_info2_skill_check);
		} else {
			checkView.setBackgroundResource(R.drawable.profile_info2_skill_un);
		}
	}
	
	
}
