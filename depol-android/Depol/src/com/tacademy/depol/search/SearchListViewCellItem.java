package com.tacademy.depol.search;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;

public class SearchListViewCellItem extends RelativeLayout implements Checkable {

	private TextView titleView;
	private boolean isChecked = false;
	private ImageView imageView;
	
	public SearchListViewCellItem(Context context) {
		super(context);
		init();
	}
	
	public SearchListViewCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SearchListViewCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.main_search_listview_cell_item_layout, this);
		titleView = (TextView)view.findViewById(R.id.category_title);
		titleView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_MEDIUM));
		imageView = (ImageView)view.findViewById(R.id.category_check_imageView);
		setSelectedColor();
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
		if (isChecked == checked) return;
		isChecked = checked;
		setSelectedColor();
	}

	@Override
	public void toggle() {
		isChecked = !isChecked;
		setSelectedColor();
	}
	
	private void setSelectedColor() {
		if (isChecked) {
			imageView.setImageResource(R.drawable.check_active);
		} else {
			imageView.setImageResource(R.drawable.check_un);
		}
	}
}
