package com.tacademy.depol.profile;

import java.io.File;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.ImageUploadData;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;

public class ProgressDialogFragment extends LoadDialogFragment {
	
	ImageUploadData imageUploadDto;
	File[] files;
	
	public interface IProgressDialogListener {
		public void onFileUploadFail(int statusCode);
		public void onFileUploadSuccess();
		public void onFileUpdateFail(int statusCode);
		public void onFileUpdateSuccess();
	}
	IProgressDialogListener mListener;
	public void setOnProgressDialogListener(IProgressDialogListener listener){
		mListener = listener;
	}
	
	public ProgressDialogFragment(ImageUploadData imageUploadDto, File[] files) {
		this.imageUploadDto = imageUploadDto;
		this.files = files;
	}
	
	private TextView progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setStyle(LoadDialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View v = inflater.inflate(R.layout.profile_upload_dialog, container, false);
		progressView = (TextView)v.findViewById(R.id.progressView);
		
		if (getTag().equals("UPLOAD")) {
			ServiceAPI.getInstance().RequestFileUploadTest(getActivity(), imageUploadDto, files, networkListener);
		} else if (getTag().equals("UPDATE")) {
			ServiceAPI.getInstance().RequestUpdateTest(getActivity(), imageUploadDto, files, networkListener);
		}
		
		
		return v;    	
    };
    
    
    
    
    
    SimpleServiceListener networkListener = new SimpleServiceListener(){
		
		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			int progress = bytesWritten*100/totalSize;
			progressView.setText(progress+"/100%");
		}
		
		@Override
		public void onFileUploadSuccess() {
			dismiss();
			if (mListener != null) {
				mListener.onFileUploadSuccess();
			}
		}
		
		@Override
		public void onFileUploadFail(int statusCode) {
			dismiss();
			if (mListener != null) {
				mListener.onFileUploadFail(statusCode);
			}
		}
		
		@Override
		public void onPortfolioUpdateSuccess() {

			dismiss();
			if (mListener != null) {
				mListener.onFileUpdateSuccess();
			}
		}

		@Override
		public void onPortfolioUpdateFail(int statusCode) {

			dismiss();
			if (mListener != null) {
				mListener.onFileUpdateSuccess();
			}
		}
		
	};
    
}
