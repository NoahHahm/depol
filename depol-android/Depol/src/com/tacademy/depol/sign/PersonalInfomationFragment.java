package com.tacademy.depol.sign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.R;

public class PersonalInfomationFragment extends SherlockFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.sign_personal_view, container, false);

			
		    ImageButton btnBack = (ImageButton)view.findViewById(R.id.personal_backView);
		    btnBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getActivity().getSupportFragmentManager().popBackStack();
				}
			});
		    
			TextView personalDescView = (TextView)view.findViewById(R.id.personal_desc);
			//tv_personal_desc
			return view;
		}
}
