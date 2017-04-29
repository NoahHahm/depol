package com.tacademy.depol.profile.dynamicview;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tacademy.depol.R;
import com.tacademy.depol.data.DateInfo;
import com.tacademy.depol.util.DateUtil;

public class AwordDialogFragment extends DynamicDialogFragment {

	public AwordDialogFragment() {
		
	}
	public AwordDialogFragment(DateInfo data) {
		mData = data;
	}
	
	TextView dialogTitle;
	EditText mainText;
	EditText subText;
	TextView dateView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View dialogView = inflater.inflate(R.layout.profile_aword_dialog_layout, container, false);

		dialogTitle = (TextView)dialogView.findViewById(R.id.aword_title);
		mainText = (EditText)dialogView.findViewById(R.id.aword_mainEdit);
		subText = (EditText)dialogView.findViewById(R.id.aword_subEdit);
		dateView = (TextView)dialogView.findViewById(R.id.aword_dateView);
		
		if (getTag().equals(REQUEST_MODIFY_MODE)) {
			dialogTitle.setText(getString(R.string.award_modify));
			dateView.setText(DateUtil.convertYYYY(mData.year));
			mainText.setText(mData.text);
			subText.setText(mData.subText);	
		}		
		
		dateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int y, int m, int d) {
						mData.year = y;
						
						dateView.setText(DateUtil.convertYYYY(mData.year));
					}
					
				}, mData.year, mData.startMonth, 1);

				DateUtil.disableMonthDayField(dialog);
				dialog.show();	
			}
		});
		
		ImageButton btn = (ImageButton)dialogView.findViewById(R.id.aword_verify);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				mData.text = mainText.getText().toString();
				mData.subText = subText.getText().toString();
				
				dismiss();
				
				if (mListener != null) {
					mListener.onDynamicDialogVerify(mData);
				}				
			}
		});
		
		btn = (ImageButton)dialogView.findViewById(R.id.aword_cancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});
		
		return dialogView;
	}

}
