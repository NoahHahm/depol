package com.tacademy.depol.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.NoticeItem;

public class NoticeCellItem extends LinearLayout {
	
	private boolean isVisibility = false;
	private View titleView;
	private View descView;
	TextView descTextView;
	TextView titleTextView;
	
	public NoticeCellItem(Context context) {
		super(context);
		init();
	}
	
	public NoticeCellItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public NoticeCellItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.setting_menu_notice_cell_item_layout, this);
		titleView = (View)view.findViewById(R.id.notice_titleView);
		descView = (View)view.findViewById(R.id.notice_descView);
		descTextView = (TextView)view.findViewById(R.id.notice_desc);
		titleTextView = (TextView)view.findViewById(R.id.notice_title);
		titleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isVisibility) {
					descView.setVisibility(View.GONE);
					titleView.setBackgroundResource(R.drawable.notice_bar_non);
					isVisibility = false;
				} else {
					descView.setVisibility(View.VISIBLE);
					titleView.setBackgroundResource(R.drawable.notice_bar_active);
					isVisibility = true;
				}
			}
		});
	}
	
	public void setData(NoticeItem data) {
		titleTextView.setText(data.title);
		descTextView.setText(data.text);
	}
	
}
