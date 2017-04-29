package com.tacademy.depol.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.tacademy.depol.R;

public class EditDialogFragment extends SherlockDialogFragment {
	
	private int portfolioId;
	
	public interface IEditDialogListener {
		public void onRemove(int portfolioId);
		public void onModify(int portfolioId);
	}
	IEditDialogListener mListener;
	
	public void setOnEditDialogListener(IEditDialogListener listener) {
		mListener = listener;
	}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View v = inflater.inflate(R.layout.profile_portfolio_edit_dialog_layout, container, false);
		Bundle bundle = getArguments();
		portfolioId = bundle.getInt(ProfileActivity.PORTFOLIO_ID_KEY);
		
		ImageButton btn = (ImageButton)v.findViewById(R.id.btn_account_email);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dismiss();
				if (mListener != null) {
					mListener.onRemove(portfolioId);
				}
			}
		});
		
		btn = (ImageButton)v.findViewById(R.id.btn_dialog_modify);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dismiss();
				if (mListener != null) {
					mListener.onModify(portfolioId);
				}
			}
		});
		

		btn = (ImageButton)v.findViewById(R.id.btn_dialog_cancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				dismiss();
			}
		});
		return v;
	}
}
