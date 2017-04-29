package com.tacademy.depol.recuritment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tacademy.depol.R;

public class RecuritBoardCellItem extends RelativeLayout {
	
	public RecuritBoardCellItem(Context context) {
		super(context);
		init();
	}
	
	public RecuritBoardCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public RecuritBoardCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.incruit_listview_cell_item_layout, this);
	}
	
}
