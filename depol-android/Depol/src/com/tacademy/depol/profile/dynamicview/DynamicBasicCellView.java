package com.tacademy.depol.profile.dynamicview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tacademy.depol.R;
import com.tacademy.depol.data.BasicInfo;

public class DynamicBasicCellView extends LinearLayout {

	private EditText nameEditView;
	private EditText jobEditView;
	private EditText addressEditView;
	private EditText birthEditView;
	private EditText emailEditView;
	private EditText websiteEditView;
	
	private View nameView;
	private View jobView;
	private View addressView;
	private View birthView;
	private View emailView;
	private View webSiteView;
	
	private BasicInfo mData;
	
	public DynamicBasicCellView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DynamicBasicCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DynamicBasicCellView(Context context) {
		super(context);
		init();
	}	

	public void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_dynamicview_basic_cell_layout, this);
		
		nameEditView = (EditText)view.findViewById(R.id.basicinfo_nameEditView);
		jobEditView = (EditText)view.findViewById(R.id.basicinfo_jobEditView);
		addressEditView = (EditText)view.findViewById(R.id.basicinfo_addressEditView);
		birthEditView = (EditText)view.findViewById(R.id.basicinfo_birthEditView);
		emailEditView = (EditText)view.findViewById(R.id.basicinfo_emailEditView);
		websiteEditView = (EditText)view.findViewById(R.id.basicinfo_websiteEditView);
		

		nameView = (View)view.findViewById(R.id.basicinfo_nameView);
		jobView = (View)view.findViewById(R.id.basicinfo_jobView);
		addressView = (View)view.findViewById(R.id.basicinfo_addressView);
		birthView = (View)view.findViewById(R.id.basicinfo_birthView);
		emailView = (View)view.findViewById(R.id.basicinfo_emailView);
		webSiteView = (View)view.findViewById(R.id.basicinfo_websiteView);
		
		
	}
	
	public void setData(BasicInfo data) {
		mData = data;
		
		nameEditView.setText(data.userName);
		jobEditView.setText(data.userPosition);
		addressEditView.setText(data.location);
		birthEditView.setText(data.birth);
		emailEditView.setText(data.email);
		websiteEditView.setText(data.website);
	}
	
	public BasicInfo getData() {
		mData.userName = nameEditView.getText().toString();
		mData.userPosition = jobEditView.getText().toString();
		mData.location = addressEditView.getText().toString();
		mData.birth = birthEditView.getText().toString();
		mData.email = emailEditView.getText().toString();
		mData.website = websiteEditView.getText().toString();
		return mData;
	}
	
	public void setEditMode(boolean isEnabled) {
		nameEditView.setEnabled(isEnabled);
		jobEditView.setEnabled(isEnabled);
		addressEditView.setEnabled(isEnabled);
		birthEditView.setEnabled(isEnabled);
		emailEditView.setEnabled(isEnabled);
		websiteEditView.setEnabled(isEnabled);
		


	}

	public void hideMode() {
		if (mData.userName == null || mData.userName.equals("")) {
			nameView.setVisibility(View.GONE);
		}
		if (mData.userPosition == null || mData.userPosition.equals("")) {
			jobView.setVisibility(View.GONE);
		}
		if (mData.location == null || mData.location.equals("")) {
			addressView.setVisibility(View.GONE);
		}
		if (mData.birth == null || mData.birth.equals("")) {
			birthView.setVisibility(View.GONE);
		}
		if (mData.email == null || mData.email.equals("")) {
			emailView.setVisibility(View.GONE);
		}
		if (mData.website == null || mData.website.equals("")) {
			webSiteView.setVisibility(View.GONE);
		}
	}
	
	public void showMode() {

		if (mData.userName == null || mData.userName.equals("")) {
			nameView.setVisibility(View.VISIBLE);
		}
		if (mData.userPosition == null || mData.userPosition.equals("")) {
			jobView.setVisibility(View.VISIBLE);
		}
		if (mData.location == null || mData.location.equals("")) {
			addressView.setVisibility(View.VISIBLE);
		}
		if (mData.birth == null || mData.birth.equals("")) {
			birthView.setVisibility(View.VISIBLE);
		}
		if (mData.email == null || mData.email.equals("")) {
			emailView.setVisibility(View.VISIBLE);
		}
		if (mData.website == null || mData.website.equals("")) {
			webSiteView.setVisibility(View.VISIBLE);
		}
	}
}
