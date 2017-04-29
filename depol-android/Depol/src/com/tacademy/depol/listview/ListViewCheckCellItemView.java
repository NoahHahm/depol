package com.tacademy.depol.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.util.GraphicsUtil;

public class ListViewCheckCellItemView extends RelativeLayout implements Checkable {
	
	public static final int INCRUIT_STATE_TYPE = 0;
	public static final int INCRUIT_STATE_FOLLOWER = 1;
	public static final int INCRUIT_STATE_FOLLOW = 2;
	public static final int INCRUIT_STATE_INCRUIT = 3;
	
	private TextView titleView;
	private ImageView iv;
	boolean isChecked = false;
	private ImageView iconState;
	private int position;
	
	public ListViewCheckCellItemView(Context context) {
		super(context);
		init();
	}	
	
	public ListViewCheckCellItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		
		LayoutInflater.from(getContext()).inflate(R.layout.listview_check_item_layout, this);
		iconState = (ImageView)findViewById(R.id.profile_imageView);
		titleView = (TextView)findViewById(R.id.state_item_titleView);
		titleView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
		
		iv = (ImageView)findViewById(R.id.check_view);
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
			PropertyManager.getInstance().setRecruitStatus(position+1);
			iv.setImageResource(R.drawable.edit_select);
		} else {
			iv.setImageResource(R.drawable.edit_unselect);
		}
	}
	
	public void setTitleData(String data, int colorType, int position) {
		this.position = position;
		titleView.setText(data);
		
		BitmapDrawable drawable = (BitmapDrawable) iconState.getDrawable();
		Bitmap bitmap = drawable.getBitmap(); 
		iconState.setImageBitmap(GraphicsUtil.getCircleBitmap(bitmap, bitmap.getWidth(), colorType, 30));
	}
	
}