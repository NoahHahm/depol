package com.tacademy.depol.profile.dynamicview;

import java.util.ArrayList;

import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.DateInfo;

import android.view.View;


interface DynamicViewListener<T> {
	public void onEditClickListener(View v);
	public void onSaveClickListener(View v, T data);
	public void onSaveClickListener(View v, ArrayList<T> mData);
	public void onModifyClickListener(View v, T data);
	public void onRemoveClickListener(View v, T data);
	public void onAddClickListener(View v);
	public void onCancelClickListener(View v);
}
public class SimpleDynamicViewListener<T> implements DynamicViewListener<T> {

	@Override
	public void onEditClickListener(View v) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onAddClickListener(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelClickListener(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveClickListener(View v, ArrayList<T> mData) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSaveClickListener(View v, T data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onModifyClickListener(View v, T data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRemoveClickListener(View v, T data) {
		// TODO Auto-generated method stub
		
	}




}
