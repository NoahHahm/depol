package com.tacademy.depol.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserInfo;
import com.tacademy.depol.sign.LoginActivity;
import com.tacademy.depol.util.GraphicsUtil;

public class UserDataManager {
	
	public static final int RESIZE_WIDTH = 100;
	public static final int RESIZE_HEIGHT = 100;	
	
	public static final String USER_IMAGE = "temp_depol.jpg";
	public static final String USER_CROP_IMAGE = "temp_depol_crop.jpg";
	public static final String USER_FACEBOOK_IMAGE = "temp_depol_facebook.jpg";
	
	
	private static UserDataManager instance;

	private UserDataManager() {
		
	}
	
	public static UserDataManager getInstance() {
		if (instance == null) {
			instance = new UserDataManager();
		}
		return instance;
	}
	
	public Uri getUserImage() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),USER_IMAGE));
	}
	
	public Uri getPicPhoto() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp_pic"));
	}

	public Uri getUserCropImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),USER_CROP_IMAGE));
	}

	public Uri getUserFacebookImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),USER_FACEBOOK_IMAGE));
	}

	public File getUserCropImageFile() {
		return new File(Environment.getExternalStorageDirectory(),USER_CROP_IMAGE);
	}

	
	public void saveImage(Bitmap bitmap, File file) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (IOException e) {

        } finally {
        	if (fOut != null) {
        		try {
					fOut.flush();
                	fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}    	
        }
	}
	
	public Bitmap getUserCropBitmap(int width, int height) {
		
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(getUserCropImageFile().getAbsolutePath(), bmOptions);
	     
	    int photoWidth  = bmOptions.outWidth;
	    int photoHeight = bmOptions.outHeight;
	   
	    int scaleFactor = Math.min(photoWidth / width, photoHeight / height);
	   
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	     
	    Bitmap result = BitmapFactory.decodeFile(getUserCropImageFile().getAbsolutePath(), bmOptions);
	     
	    return result;
	}
			
	public void clearDepolCache() {
		File file = new File(Environment.getExternalStorageDirectory(), USER_CROP_IMAGE);
		file.delete();
		file = new File(Environment.getExternalStorageDirectory(), USER_FACEBOOK_IMAGE);
		file.delete();
		file = new File(Environment.getExternalStorageDirectory(), USER_IMAGE);
		file.delete();
	}
	


	
}
