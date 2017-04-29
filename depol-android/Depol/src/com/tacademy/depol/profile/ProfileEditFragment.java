package com.tacademy.depol.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tacademy.depol.R;
import com.tacademy.depol.account.ProfileEditEmailActivity;
import com.tacademy.depol.account.ProfileEditJobActivity;
import com.tacademy.depol.account.ProfileEditNameActivity;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.profile.SelectDialogFragment.IDialogListener;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.util.ProfileUtil;
import com.tacademy.depol.widget.MyRoundedImageView;

public class ProfileEditFragment extends SherlockFragment {
	
	public static final int CAMERA_CAPTURE = 1;
	public static final int PIC_CROP = 2;
	

	public final static int REQUEST_EDIT_JOB = 10;
	public final static int REQUEST_EDIT_NAME = 11;
	public final static int REQUEST_EDIT_EMAIL = 12;
	
	
	private MyRoundedImageView iconView;
	ImageView iconEditView;
	private UserBasicInfo info;
	private DisplayImageOptions option;
	
	TextView nameView;
	TextView jobView;
	TextView emailView;
	TextView statusView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		info = PropertyManager.getInstance().getMyData();
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		option = ImageLoaderManager.getInstance().getDisplayImageOptions();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_edit_layout, container, false);	
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});

		nameView = (TextView)view.findViewById(R.id.proifle_edit_nameView);
		jobView = (TextView)view.findViewById(R.id.proifle_edit_jobView);
		emailView = (TextView)view.findViewById(R.id.proifle_edit_emailView);
		statusView = (TextView)view.findViewById(R.id.proifle_edit_statusView);
		update();
		
		iconView = (MyRoundedImageView)view.findViewById(R.id.icon_user);

		if (iconView != null) {
			Bitmap bm = BitmapFactory.decodeFile(UserDataManager.getInstance().getUserCropImageUri().getPath());
			if (bm != null) {
				iconView.setImageBitmap(bm);
				iconView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
			}
		}
		
		iconEditView = (ImageView)view.findViewById(R.id.icon_user_edit);
		iconEditView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SelectDialogFragment dialog = new SelectDialogFragment();
				dialog.setOnDialogListener(new IDialogListener() {
					
					@Override
					public void onFinishDialog() {
						Bitmap bm = BitmapFactory.decodeFile(UserDataManager.getInstance().getUserCropImageUri().getPath());
						if (bm != null) {
							iconView.setImageBitmap(bm);
						}
					}

					@Override
					public void onFileUploadFinishDialog() {
						
					}

					@Override
					public void onProfileFailDialog() {
						
					}
				});
				dialog.show(getActivity().getSupportFragmentManager(), SelectDialogFragment.REQUEST_EDIT_PHOTO);
			}
		});

		View btn = (View)view.findViewById(R.id.btn_job);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), IncruitListActivity.class);
				startActivityForResult(intent, IncruitListActivity.REQUEST_JOB_TYPE);
			}
		});

		btn = (View)view.findViewById(R.id.btn_edit_name);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProfileEditNameActivity.class);
				startActivityForResult(intent, REQUEST_EDIT_NAME);
			}
		});

		btn = (View)view.findViewById(R.id.btn_edit_job);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProfileEditJobActivity.class);
				startActivityForResult(intent, REQUEST_EDIT_JOB);
			}
		});

		btn = (View)view.findViewById(R.id.btn_profile_email);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ProfileEditEmailActivity.class);
				startActivityForResult(intent, REQUEST_EDIT_EMAIL);
			}
		});
		
		return view;
	}
	    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == IncruitListActivity.REQUEST_JOB_TYPE && resultCode == Activity.RESULT_OK) {
			iconView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
			update();
		} else if (requestCode == REQUEST_EDIT_JOB && resultCode == Activity.RESULT_OK) {
			update();
		} else if (requestCode == REQUEST_EDIT_NAME && resultCode == Activity.RESULT_OK) {
			update();
		} else if (requestCode == REQUEST_EDIT_EMAIL && resultCode == Activity.RESULT_OK) {
			update();
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		
		ActionBar actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		NormalActionBar actionBarView = new NormalActionBar(getActivity());
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.account_info));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				
			}
			
			@Override
			public void onLeftButton(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
			
		});
		actionbar.setCustomView(actionBarView);

	}
	
	private void update() {
		UserBasicInfo info = PropertyManager.getInstance().getMyData();
		nameView.setText(info.userName);
		jobView.setText(info.userPosition);
		emailView.setText(PropertyManager.getInstance().getMyEmail());
		statusView.setText(ProfileUtil.convertStatus(info.userRecruitStatus));
	}
	
	
}
