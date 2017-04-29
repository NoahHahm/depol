package com.tacademy.depol.sign;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.tacademy.depol.R;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.main.MainActivity;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.util.Validation;

public class SignUpFragment extends SherlockFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sign_up_layout, container, false);
			    
	    final TextView emailView = (TextView)view.findViewById(R.id.sign_up_emailView);
	    emailView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    final TextView nameView = (TextView)view.findViewById(R.id.sign_up_nameView);
	    nameView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    final TextView passwordView = (TextView)view.findViewById(R.id.sign_up_passwordView);
	    passwordView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    final TextView confirmView = (TextView)view.findViewById(R.id.sign_up_confirm_passwordView);
	    confirmView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));
	    TextView titleView = (TextView)view.findViewById(R.id.sign_up_titleView);
	    titleView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
	    TextView personalView = (TextView)view.findViewById(R.id.tv_personal); 
	    personalView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
	    
	    
	    Button btn = (Button)view.findViewById(R.id.btn_sign_up);
	    btn.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.NANUM_BARUN_GOTHIC_BOLD));
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

			    final String email = emailView.getText().toString();
			    String name = nameView.getText().toString();
			    String confirmPass = confirmView.getText().toString();
			    final String password = passwordView.getText().toString();
			    
				boolean isValidation = Validation.isEmailValid(email);
				if (!isValidation) {
					Toast.makeText(getActivity(), getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
					return;
				}
			    
			    if (!checkPassword(password, confirmPass)) {
			    	Toast.makeText(getActivity(), getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    
			
				final SignDialogFragment f = new SignDialogFragment(email, password, name);
				f.show(getChildFragmentManager(), SignDialogFragment.SIGNUP_TAG);
				f.setOnSignListener(new SimpleSignDialogListener<UserBasicInfo>() {
					@Override
					public void onSignInSuccess(UserBasicInfo data) {
						PropertyManager.getInstance().setMyData(data);
						PropertyManager.getInstance().setMyLoginData(email, password);
						Intent intent = new Intent(getActivity(), MainActivity.class);
						startActivity(intent);
						getActivity().finish();
					}
					
					@Override
					public void onSignInFail() {
						
					}
				});
			    
			}
	    });
	    
	    ImageButton btnBack = (ImageButton)view.findViewById(R.id.sign_up_backView);
	    btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	    
	    personalView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.login_content, new PersonalInfomationFragment())
				.addToBackStack(null).commit();
			}
		});
	    
	    return view;
	}
	
	boolean checkPassword(String pass, String confirmPass) {
		if (pass.equals(confirmPass)) {
			return true;
		}
		return false;
	}
}
