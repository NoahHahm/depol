package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.AbilityInfo;

public class DynamicInfographicsCellView extends DynamicCellView<AbilityInfo> {

	int currentCount = 0;
	int level = currentCount*2000;
	Drawable drawable;
	TextView text;
	AbilityInfo data;
	ImageView infoView;
	
	public interface IDynamicInfographicsListener {
		public void onAbilityListener(AbilityInfo data);
	}
	IDynamicInfographicsListener infoListener;
	
	public void setOnDynamicInfographicsListener(IDynamicInfographicsListener listener) {
		infoListener = listener;
	}
	

	public DynamicInfographicsCellView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DynamicInfographicsCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DynamicInfographicsCellView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_dynamicview_infographics_layout, this);	
		infoView = (ImageView)view.findViewById(R.id.clip_ImageView);
		text = (TextView)view.findViewById(R.id.infographics_text);
		drawable = infoView.getDrawable();
		infoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentCount++;
				level = currentCount*2000;
				if (currentCount > 5) {
					currentCount = 0;
					level = 0;
				}
				drawable.setLevel(level);
				data.level = currentCount;
				if (infoListener!=null) {
					infoListener.onAbilityListener(data);
				}
			}
		});
		infoView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (mListener != null) {
					mListener.onRemoveClickListener(v, data);
					return true;
				}
				return false;
			}
		});
	}
	
	public void setData(AbilityInfo info) {
		data = info;
		text.setText(info.program);
		drawable.setLevel(info.level*2000);
		currentCount = info.level;
	}
	
	@Override
	public void showMode() {
		
	}
	
	@Override
	public void hideMode() {
		infoView.setEnabled(false);
	}
	
	
	
}
