package com.tacademy.depol.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tacademy.depol.R;

public class WriteCategoryListViewCell extends LinearLayout implements Checkable{

	private TextView titleView;
	private ImageView checkImageView;
	private boolean isChecked = false;
	
	public WriteCategoryListViewCell(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WriteCategoryListViewCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WriteCategoryListViewCell(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.write_category_listview_cell_layout, this);
		checkImageView = (ImageView)findViewById(R.id.write_category_cell_imageView);
		titleView = (TextView)findViewById(R.id.write_category_cell_title);
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
			checkImageView.setImageResource(R.drawable.popup_check_act);
		} else {
			checkImageView.setImageResource(R.drawable.popup_check_un);
		}
	}

}
