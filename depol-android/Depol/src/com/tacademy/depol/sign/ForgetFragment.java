package com.tacademy.depol.sign;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;

public class ForgetFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sign_in_forgot_layout, container, false); 
		TextView titleView = (TextView)view.findViewById(R.id.sign_in_forgot_titleView);
		titleView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
		TextView descView = (TextView)view.findViewById(R.id.sign_in_forgot_desc);
		descView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC));
		EditText emailEditView = (EditText)view.findViewById(R.id.comment_editText);
		emailEditView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
		
		
		
		Button btn = (Button)view.findViewById(R.id.btn_comment_send);
		btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
		
		return view;
	}
}
