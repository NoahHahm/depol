package com.tacademy.depol.actionbar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;


public class NormalActionBar extends RelativeLayout {
	
	public final static int BACK_MODE = 1;
	public final static int MAIN_MODE = 2;
	public final static int PROFILE_MODE = 3;
	public final static int LEFT_MENU_MODE = 4;
	public final static int LEFT_STRING_RIGHT_MODE = 5;
	public final static int LEFT_RIGHT_MESSAGE_WRITE_MODE = 6;

	private ImageView newView;
	private TextView centerView;
	private ImageView centerImageView;
	private ImageButton leftButton;
	private ImageButton rightButton;
	private Button rightStrButton;
	private View view;
	private int type;
			

	public interface IActionBarListener {
		public void onLeftButton(View v);
		public void onRightButton(View v);
	}
	IActionBarListener mListener;
	
	public void setOnActionBarListener(IActionBarListener listener) {
		mListener = listener;
	}

	public NormalActionBar(Context context) {
		super(context);
		init();
	}
	
	public NormalActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public NormalActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.main_actionbar_sub_layout, this);
		newView = (ImageView)view.findViewById(R.id.action_bar_new);
		centerView = (TextView)view.findViewById(R.id.action_center_textView);
		centerImageView = (ImageView)view.findViewById(R.id.action_center_imageView);
		centerView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
		rightStrButton = (Button)view.findViewById(R.id.btn_right_str);
		rightStrButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightButton(v);
				}
			}
		});
		
		leftButton = (ImageButton)view.findViewById(R.id.action_btn_left);
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onLeftButton(v);
				}
			}
		});
		rightButton = (ImageButton)view.findViewById(R.id.action_btn_right);
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightButton(v);
				}
			}
		});
	}

	private void leftHide() {
		leftButton.setVisibility(View.GONE);
	}
	
	private void rightHide() {
		rightButton.setVisibility(View.GONE);
	}
	
	public void setMode(int mode) {
		setMode(mode, "", "");
	}

	public void setMode(int mode, String title) {
		setMode(mode, title, "");
	}
	
	public void setMode(int mode, String title, String rightStr) {
		type = mode;
		
		if (mode == BACK_MODE) {
			centerImageView.setVisibility(View.GONE);
			centerView.setVisibility(View.VISIBLE);
			leftButton.setImageResource(R.drawable.btn_back);
			setCenterText(title);
			rightHide();
		} else if (mode == MAIN_MODE) {
			centerImageView.setVisibility(View.VISIBLE);
			centerView.setVisibility(View.GONE);
		} else if (mode == PROFILE_MODE) {	
			centerImageView.setVisibility(View.GONE);			
			centerView.setVisibility(View.VISIBLE);
			leftButton.setImageResource(R.drawable.btn_menu);
			rightButton.setImageResource(R.drawable.btn_search);
			setCenterText(title);		
		} else if (mode == LEFT_MENU_MODE) {	
			centerImageView.setVisibility(View.GONE);			
			centerView.setVisibility(View.VISIBLE);
			leftButton.setImageResource(R.drawable.btn_menu);
			rightHide();
			setCenterText(title);		
		} else if (mode == LEFT_STRING_RIGHT_MODE) {
			centerImageView.setVisibility(View.GONE);			
			centerView.setVisibility(View.VISIBLE);
			leftButton.setImageResource(R.drawable.btn_back);		
			rightStrButton.setVisibility(View.VISIBLE);
			rightStrButton.setText(rightStr);
			rightButton.setVisibility(View.GONE);
			setCenterText(title);
		} else if (mode == LEFT_RIGHT_MESSAGE_WRITE_MODE) {
			centerImageView.setVisibility(View.GONE);	
			rightStrButton.setVisibility(View.GONE);
			
			centerView.setVisibility(View.VISIBLE);
			setCenterText(title);
			
			leftButton.setImageResource(R.drawable.btn_menu);
			rightButton.setImageResource(R.drawable.btn_write);
			rightButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void setCenterText(String text) {
		centerView.setText(text);
	}
	
	public void showNew() {
		if (type == BACK_MODE || type == LEFT_RIGHT_MESSAGE_WRITE_MODE) {
			return;
		}
		newView.setVisibility(View.VISIBLE);
	}

	public void hideNew() {
		newView.setVisibility(View.GONE);
	}
	
}
