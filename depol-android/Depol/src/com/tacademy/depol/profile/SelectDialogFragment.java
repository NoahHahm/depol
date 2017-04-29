package com.tacademy.depol.profile;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.model.UserDataManager;

public class SelectDialogFragment extends SherlockDialogFragment {

	public static final int CAMERA_CAPTURE = 1;
	public static final int PIC_CROP = 2;
	public static final int REQUEST_FILEUPLOAD = 4;
	public static final int REQUEST_CAMERA_FILEUPLOAD = 5;

	public static final String REQUEST_FILEUPLOAD_KEY = "REQUEST_FILEUPLOAD";
	public static final String REQUEST_EDIT_PHOTO = "REQUEST_EDIT_PHOTO";
	public static final String REQUEST_CAMERA_CAPTURE_PORTFOLIO = "REQUEST_CAMERA_CAPTURE_PORTFOLIO";
	
	public interface IDialogListener {
		public void onFinishDialog();
		public void onFileUploadFinishDialog();
		public void onProfileFailDialog();
	}
	
	IDialogListener mListener;
	
    public void setOnDialogListener(IDialogListener listener) {
    	mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); 
        return dialog;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View v = inflater.inflate(R.layout.profile_edit_select_layout, container, false);
	
		
		ImageButton btn = (ImageButton)v.findViewById(R.id.btn_sign_up);
		if (getTag().equals(REQUEST_EDIT_PHOTO)) {
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, UserDataManager.getInstance().getUserImage());
					startActivityForResult(captureIntent, CAMERA_CAPTURE);
				}
			});	
		}  else if (getTag().equals(REQUEST_FILEUPLOAD_KEY)) {
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, UserDataManager.getInstance().getPicPhoto());
					startActivityForResult(captureIntent, REQUEST_CAMERA_FILEUPLOAD);
				}
			});	
		}

		btn = (ImageButton)v.findViewById(R.id.btn_albums);	
		if (getTag().equals(REQUEST_EDIT_PHOTO)) {			
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					photoPickerIntent.setType("image/*");
					photoPickerIntent.putExtra("crop", "true");
					photoPickerIntent.putExtra("aspectX", 1);
					photoPickerIntent.putExtra("aspectY", 1);
					photoPickerIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, UserDataManager
									.getInstance().getUserCropImageUri());
					photoPickerIntent.putExtra("outputFormat",
							Bitmap.CompressFormat.JPEG.toString());
					startActivityForResult(photoPickerIntent, PIC_CROP);
				}
			});
		} else if (getTag().equals(REQUEST_FILEUPLOAD_KEY)) {
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), WriteActivity.class);
					startActivityForResult(intent, REQUEST_FILEUPLOAD);
				}
			});
		}
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
			performCrop();
		}
		else if (requestCode == PIC_CROP && resultCode == Activity.RESULT_OK) {
			ImageLoader.getInstance().clearDiscCache();
			File file = new File(UserDataManager.getInstance().getUserCropImageUri().getPath());
			
			ServiceAPI.getInstance().RequestProfilePicUpload(getActivity(), file, new SimpleServiceListener<UserBasicInfo>(){
			
				@Override
				public void onFileUploadSuccess() {
					dismiss();
					if (mListener != null) {
						mListener.onFinishDialog();
					}			
				}
				
				@Override
				public void onFileUploadFail(int statusCode) {
					dismiss();
					if (mListener != null) {
						mListener.onProfileFailDialog();
					}		
				}
				
			});
			
			//PropertyManager.getInstance().setCacheProfilePicture(true);
		}
		else if (requestCode == REQUEST_CAMERA_FILEUPLOAD && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(getActivity(), WriteActivity.class);
			String path = UserDataManager.getInstance().getPicPhoto().toString();
			intent.putExtra(REQUEST_CAMERA_CAPTURE_PORTFOLIO, path);
			startActivityForResult(intent, REQUEST_FILEUPLOAD);
		} 
		else if (requestCode == REQUEST_FILEUPLOAD && resultCode == Activity.RESULT_OK) {
			if (mListener != null) {
				mListener.onFileUploadFinishDialog();
			}	
			dismiss();
		}
	}	

	private void performCrop() {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(UserDataManager.getInstance().getUserImage(), "image/*");
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, UserDataManager.getInstance().getUserCropImageUri());
		cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());	
		startActivityForResult(cropIntent, PIC_CROP);
	}

	
	
}
