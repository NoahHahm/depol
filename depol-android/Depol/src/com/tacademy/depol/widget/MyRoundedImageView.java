package com.tacademy.depol.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tacademy.depol.R;
import com.tacademy.depol.util.GraphicsUtil;

public class MyRoundedImageView extends ImageView {
	
	protected int color = getResources().getColor(R.color.stroke_color_type1);
	protected Bitmap bitmap;
	
	public MyRoundedImageView(Context context) {
		super(context);
	}

	public MyRoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
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
			Bitmap roundBitmap = GraphicsUtil.getCroppedBitmap(bitmap, getWidth(), color);	
			canvas.drawBitmap(roundBitmap, 0, 0, null);
			
		}	
	}
	
	public void setColor(int color) {
		this.color = color;
		invalidate();
	}

	public void setResourceImage(Drawable resource) {
		bitmap = ((BitmapDrawable) resource).getBitmap();
		invalidate();
	}
	
	public void setBitmapImage(Bitmap bitmap) {
		this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		invalidate();
	}
	

}