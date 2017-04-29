package com.tacademy.depol.util;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;

/**
 * GraphicsUtil an utility class which convert the 
 * image in circular shape
 * Author : Mukesh Yadav
 */
public class GraphicsUtil {
	
	public static final int STROKE_WIDTH_NORMAL = 10;
	public static final int COLOR_NORMAL = ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type1);

	/*
	 * Draw image in circular shape
	 * Note: change the pixel size if you want image small or
	 * large
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap, int radius, int color, float strokeWidth) {
		/*
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float) strokeWidth);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
*/

		Bitmap sbmp;
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
		else
			sbmp = bitmap;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.BLACK);
		
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);
		
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		
		canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);		
		
		return output;
	}
	
	public static Bitmap getCircleBitmap(File file, int radius, int color, float strokeWidth) {
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		bitmap = getCircleBitmap(bitmap, radius, color, strokeWidth);	
		return bitmap;
	}
	
	public static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        if (d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable)d).getBitmap();
            if (b != null) b.recycle();
        }
    }

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius, int color) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.BLACK);
		
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);
		
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(17.0f);
		
		canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);		
		
		return output;
	}
	

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius, int color, float strokeWidth) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.BLACK);
		
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);
		
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		
		canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);		
		
		return output;
	}
	
	public static int ConvertStrokeColor(int type) {
		switch(type) {
			case 1: return ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type1);
			case 2: return ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type2);
			case 3: return ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type3);
			case 4: return ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type4);
			default:
				return ApplicationContext.getContext().getResources().getColor(R.color.stroke_color_type1);
		}				
	}

}
