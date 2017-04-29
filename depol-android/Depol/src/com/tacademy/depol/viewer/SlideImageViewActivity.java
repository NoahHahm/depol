package com.tacademy.depol.viewer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.model.ImageLoaderManager;

public class SlideImageViewActivity extends SherlockActivity {
	
	public final static String IMAGE_DATA = "IMAGE_DATA";
    private DisplayImageOptions options;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.viewslide_layout);
		
		ViewFlipper flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.ARGB_8888);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
		
		Intent intent = getIntent();
		String[] images = intent.getStringArrayExtra(IMAGE_DATA);

		for(int i=0;i<images.length;i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ImageLoader.getInstance().displayImage(images[i], imageView, options);		
			flipper.addView(imageView);	
		}
		
		flipper.setFlipInterval(2000);
		flipper.setAutoStart(true);
		
	}

}
