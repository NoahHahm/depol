package com.tacademy.depol.write;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tacademy.depol.R;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.model.ImageLoaderManager;

public class DynimicFileView extends LinearLayout {

	private ImageView firstImageView;
	private ImageView mainImageView;
	private DisplayImageOptions options;
	private MultplePhotoItem mData;
	
	public interface IDynimicFileViewListener {
		public void onClickListener(MultplePhotoItem data);
	}
	IDynimicFileViewListener mListener;
	
	public void setOnDynimicFileViewListener(IDynimicFileViewListener listener) {
		mListener = listener;
	}
	
	public DynimicFileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DynimicFileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DynimicFileView(Context context) {
		super(context);
		init();
	}

	private void init() {
		int width = (int) getResources().getDimension(R.dimen.photo_width);
		int height = (int) getResources().getDimension(R.dimen.photo_height);
		setLayoutParams(new LayoutParams(width, height));
		
		
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dynimic_file_view_layout, this);
		firstImageView = (ImageView)view.findViewById(R.id.imageView_first);
		mainImageView = (ImageView)view.findViewById(R.id.main_imageView);
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onClickListener(mData);
				}
			}
		});
	}
	
	public void showFirst() {
		firstImageView.setVisibility(View.VISIBLE);
	}
	
	public void setData(MultplePhotoItem data) {
		mData = data;
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(data.path, new ImageSize(150, 150), options);
		mainImageView.setImageBitmap(bitmap);
	}
}
