package com.tacademy.depol.model;

import android.graphics.Bitmap.Config;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;

public class ImageLoaderManager {
	
	private DisplayImageOptions options;
	
	private static ImageLoaderManager instance;
	public static ImageLoaderManager getInstance() {
		if (instance == null) {
			ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ApplicationContext.getContext()));
			instance = new ImageLoaderManager();
		}
		return instance;
	}
	
	public void initialize(boolean isMemoryCache, boolean isDiscCache, Config bitmapConfig) {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.broken_image)
		.showImageOnFail(R.drawable.broken_image)
		.cacheInMemory(isMemoryCache)
		.cacheOnDisc(isDiscCache)
		.bitmapConfig(bitmapConfig)
		.build();		
	}
	
	public DisplayImageOptions getDisplayImageOptions() {
		return options;
	}
	
	
}
