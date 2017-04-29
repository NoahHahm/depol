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
import android.widget.Toast;

import com.tacademy.depol.R;
import com.tacademy.depol.data.DateInfo;
import com.tacademy.depol.util.DateUtil;


public class AcademicDialogFragment extends DynamicDialogFragment {
	
	
	private TextView startDateView;
	private TextView endDateView;
	private EditText mainEditView;
	private EditText subEditView;
	private TextView titleView;
	
	
	public AcademicDialogFragment() {
		
	}
	
	public AcademicDialogFragment(DateInfo data) {
		mData = data;
	}
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View dialogView = inflater.inflate(R.layout.profile_academic_dialog_layout, container, false);
		
		startDateView = (TextView)dialogView.findViewById(R.id.academy_startDateView);
		endDateView = (TextView)dialogView.findViewById(R.id.academy_endDateView);
		mainEditView = (EditText)dialogView.findViewById(R.id.academy_mainEdit);
		subEditView = (EditText)dialogView.findViewById(R.id.academy_subEdit);
		titleView = (TextView)dialogView.findViewById(R.id.academy_title);
		
		if (getTag().equals(REQUEST_MODIFY_MODE)) {
			titleView.setText(getString(R.string.academic_modify));
			startDateView.setText(DateUtil.convertYYYYMM(mData.startYear, mData.startMonth));
			endDateView.setText(DateUtil.convertYYYYMM(mData.endYear, mData.endMonth));
			mainEditView.setText(mData.text);
			subEditView.setText(mData.subText);	
		}
		
		startDateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int y, int m, int d) {
						mData.startYear = y;
						mData.startMonth = m+1;
						startDateView.setText(DateUtil.convertYYYYMM(mData.startYear, mData.startMonth));
					}
					
				}, mData.startYear, mData.startMonth, 1);
						
				DateUtil.disableDayField(dialog);
				dialog.show();		
			}
		});
		
		
		endDateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int y, int m, int d) {
						mData.endYear = y;
						mData.endMonth = m+1;
						
						endDateView.setText(DateUtil.convertYYYYMM(mData.endYear, mData.endMonth));
					}
					
				}, mData.endYear, mData.endMonth, 1);

				DateUtil.disableDayField(dialog);
				dialog.show();	
			}
		});
		
		
		ImageButton btn = (ImageButton)dialogView.findViewById(R.id.academy_verify);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (mData.startYear > mData.endYear) {
					Toast.makeText(getActivity(), "종료일보다 시작일이 더 큽니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				mData.text = mainEditView.getText().toString();
				mData.subText = subEditView.getText().toString();
				
				dismiss();
								
				if (mListener != null) {
					mListener.onDynamicDialogVerify(mData);
				}				
			}
		});
		btn = (ImageButton)dialogView.findViewById(R.id.academy_cancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});
    	
    	return dialogView;
    }
    

}
