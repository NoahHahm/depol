package com.tacademy.depol.widget;

import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.util.GraphicsUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UserRoundedImageView extends MyRoundedImageView {

	public UserRoundedImageView(Context context) {
		super(context);
	}
	
	public UserRoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public UserRoundedImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (bitmap != null) {
			//Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap roundBitmap = GraphicsUtil.getCroppedBitmap(bitmap, getWidth(), color, 10.0f);	
			canvas.drawBitmap(roundBitmap, 0, 0, null);
			
		}	
	}
	
}
