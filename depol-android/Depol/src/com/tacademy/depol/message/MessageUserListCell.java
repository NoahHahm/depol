package com.tacademy.depol.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.follow.FollowCellItem;

public class MessageUserListCell extends FollowCellItem implements Checkable {

	private boolean isChecked = false;
	
	public MessageUserListCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MessageUserListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MessageUserListCell(Context context) {
		super(context);
	}
	
	@Override
	protected void init() {
		super.init();
		followName.setTextColor(getResources().getColor(R.color.BLACK));
		followCheck.setVisibility(View.GONE);
		setSelectedColor();
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setChecked(boolean checked) {
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
			setBackgroundColor(ApplicationContext.getContext().getResources().getColor(R.color.C_bdc3c7));
		} else {
			setBackgroundColor(ApplicationContext.getContext().getResources().getColor(R.color.white_color));
		}
	}
	
}
