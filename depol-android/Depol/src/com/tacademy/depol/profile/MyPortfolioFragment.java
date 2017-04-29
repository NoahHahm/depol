package com.tacademy.depol.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.R;
import com.tacademy.depol.profile.SelectDialogFragment.IDialogListener;

public class MyPortfolioFragment extends SherlockFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {    	
    	View v = inflater.inflate(R.layout.portfolio_post_button_layout, container, false); 
    	ImageView upload = (ImageView)v.findViewById(R.id.my_like);

		Bundle bundle = getArguments();
		boolean isMine = bundle.getBoolean(ProfileActivity.PROFILE_TYPE_KEY);
		
		if (!isMine) {
			upload.setVisibility(View.GONE);
		}
		
    	v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectDialogFragment dialog = new SelectDialogFragment();
				dialog.setOnDialogListener(new IDialogListener() {
					
					@Override
					public void onFinishDialog() {
						
					}
					
					@Override
					public void onFileUploadFinishDialog() {
						((ProfileActivity)getActivity()).refresh();
					}

					@Override
					public void onProfileFailDialog() {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show(getActivity().getSupportFragmentManager(), SelectDialogFragment.REQUEST_FILEUPLOAD_KEY);
			}
		});
    	return v;
    }
    

}