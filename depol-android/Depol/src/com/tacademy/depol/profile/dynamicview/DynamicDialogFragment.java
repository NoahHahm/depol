package com.tacademy.depol.profile.dynamicview;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.tacademy.depol.data.DateInfo;

public class DynamicDialogFragment extends SherlockDialogFragment {

	public final static String REQUEST_ADD_MODE = "ADD";
	public final static String REQUEST_MODIFY_MODE = "MODIFY";
	public final static int ADD_MODE = 1;
	public final static int MODIFY_MODE = 2;
	

	protected DateInfo mData;	

	public interface IDynamicDialogListener {
		public void onDynamicDialogVerify(DateInfo data);
	}
	
	IDynamicDialogListener mListener;
	
	public void setOnDynamicDialogListener (IDynamicDialogListener listener) {
		mListener = listener;
	}
	

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getTag().equals(REQUEST_ADD_MODE)) {
			mData = new DateInfo();		
			Calendar cal = new GregorianCalendar();
			mData.startYear = cal.get(Calendar.YEAR);
			mData.startMonth = cal.get(Calendar.MONTH);
			mData.endYear = cal.get(Calendar.YEAR);
			mData.endMonth = cal.get(Calendar.MONTH);
			mData.year = cal.get(Calendar.YEAR);
		}		
	}
}
