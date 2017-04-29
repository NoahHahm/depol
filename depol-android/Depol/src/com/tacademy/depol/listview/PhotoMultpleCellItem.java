package com.tacademy.depol.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tacademy.depol.R;

public class PhotoMultpleCellItem extends FrameLayout implements Checkable {

	private ImageView iconView;
	private boolean isChecked = false;
	
	public PhotoMultpleCellItem(Context context) {
		super(context);
		init();
	}
	
	public PhotoMultpleCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public PhotoMultpleCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.photo_multple_choice_item_layout, this);
		iconView = (ImageView)findViewById(R.id.select_icon);
		drawCheck();
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
			iconView.setImageResource(R.drawable.album_act);
		} else {
			iconView.setImageResource(R.drawable.album_non);
		}
	}

}
