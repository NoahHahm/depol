package com.tacademy.depol.profile.dynamicview;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tacademy.depol.R;
import com.tacademy.depol.data.DateInfo;

public class CertificationDialogFragment extends DynamicDialogFragment {
	
	public CertificationDialogFragment() {
		
	}
	public CertificationDialogFragment(DateInfo info) {
		mData = info;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View dialogView = inflater.inflate(R.layout.profile_certification_dialog_layout, container, false);
		final EditText mainText = (EditText)dialogView.findViewById(R.id.certification_mainEdit);
		
		if (getTag().equals(REQUEST_MODIFY_MODE)) {
			mainText.setText(mData.text);
		}		
		
		ImageButton btn = (ImageButton)dialogView.findViewById(R.id.certification_verify);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if (mListener != null) {
					DateInfo info = new DateInfo();
					info.pk = mData.pk;
					info.text = mainText.getText().toString();
					mListener.onDynamicDialogVerify(info);
				}
			}
		});
		
		btn = (ImageButton)dialogView.findViewById(R.id.certification_cancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});
		return dialogView;
	}
	
	
}
