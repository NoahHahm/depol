package com.tacademy.depol.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.MenuNotiInfo;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.invitation.InvitationFragment;
import com.tacademy.depol.like.LikeFragment;
import com.tacademy.depol.message.MessageFragment;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.recuritment.RecuritmentFragment;
import com.tacademy.depol.settings.SettingFragment;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.widget.MyRoundedImageView;

public class MenuFragment extends SherlockFragment {

	private MyRoundedImageView profileView;
	private ActionBar actionbar;
	private UserBasicInfo info;
	private DisplayImageOptions option;
	private TextView nameView;
	private TextView jogView;
	private TextView messageCount;
	private TextView likeCount;
	private MenuNotiInfo notiinfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		info = PropertyManager.getInstance().getMyData();
		
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		option = ImageLoaderManager.getInstance().getDisplayImageOptions();
		ImageLoader.getInstance().displayImage(info.userPropicUri, profileView, option);
		
		/*
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {
			
			@Override
			public void onOpened() {
				ServiceAPI.getInstance().RequestNoti(getActivity(), new SimpleServiceListener<MenuNotiInfo>(){
					
					@Override
					public void onMenuNotiRequestFail(int statusCode) {
						
					}
					
					@Override
					public void onMenuNotiRequestSuccess(MenuNotiInfo data) {
						notiinfo = data;
						
						if (notiinfo.likeNum > 0) {
							likeCount.setText(String.format("%d", notiinfo.likeNum));	
							likeCount.setVisibility(View.VISIBLE);						
						} else {
							likeCount.setVisibility(View.GONE);														
						}
						
						if (notiinfo.messageNum > 0) {
							messageCount.setText(String.format("%d", notiinfo.messageNum));
							messageCount.setVisibility(View.VISIBLE);							
						} else {
							messageCount.setVisibility(View.GONE);														
						}
						
						if (notiinfo.messageNum > 0 || notiinfo.likeNum > 0) {
							if (actionbar.getCustomView() instanceof NormalActionBar) {
								((NormalActionBar)actionbar.getCustomView()).showNew();
							}
						} else if (notiinfo.messageNum == 0 || notiinfo.likeNum == 0) {
							if (actionbar.getCustomView() instanceof NormalActionBar) {
								((NormalActionBar)actionbar.getCustomView()).hideNew();
							}
						}
					}
					
				});
			}
		});
		
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				if (notiinfo != null) {
					if (notiinfo.likeNum > 0 || notiinfo.messageNum > 0) {
						if (actionbar.getCustomView() instanceof NormalActionBar) {
							((NormalActionBar)actionbar.getCustomView()).showNew();
						}
					} else if (notiinfo.likeNum == 0 || notiinfo.messageNum == 0) {
						if (actionbar.getCustomView() instanceof NormalActionBar) {
							((NormalActionBar)actionbar.getCustomView()).hideNew();
						}
					}
				}
			}
		});
		*/
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_menu_layout, container, false);
		
		nameView = (TextView) v.findViewById(R.id.menu_nameView);
		jogView = (TextView) v.findViewById(R.id.menu_jobView);	
		messageCount = (TextView) v.findViewById(R.id.menu_message_count);	
		likeCount = (TextView) v.findViewById(R.id.menu_like_count);	
		actionbar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();
		messageCount.setVisibility(View.GONE);
		likeCount.setVisibility(View.GONE);
		
		nameView.setText(info.userName);	
		jogView.setText(info.userPosition);
		
		profileView = (MyRoundedImageView) v.findViewById(R.id.profile_picture_view);
		profileView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				actionBarShow();
				
//				int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
//				if (backStackCount > 0) {
//					BackStackEntry entry = getActivity().getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1);
//					if (entry.getName().equals("profile")) {
//						getActivity().getSupportFragmentManager().popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//					}
//				}

				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				intent.putExtra(ProfileActivity.PROFILE_TYPE_KEY, true);
				getActivity().startActivity(intent);
				
//				getActivity().getSupportFragmentManager().beginTransaction()
//						.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//						.replace(R.id.content_frame_sub, profileFragment)
//						.addToBackStack(null).commit();
				

				//메뉴
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});

		Button btn = (Button) v.findViewById(R.id.btn_main);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				actionBarShow();
				
				NormalActionBar actionBarView = new NormalActionBar(getActivity());
				actionBarView.setMode(NormalActionBar.MAIN_MODE);
				actionBarView.setOnActionBarListener(new IActionBarListener() {
					
					@Override
					public void onRightButton(View v) {
						((SlidingFragmentActivity)getActivity()).showSecondaryMenu();
					}
					
					@Override
					public void onLeftButton(View v) {
						((SlidingFragmentActivity)getActivity()).toggle();				
					}
				});
				actionbar.setCustomView(actionBarView);
				
				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				((SlidingFragmentActivity) getActivity()).getSlidingMenu()
						.showContent();
				

				//메뉴
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});

		btn = (Button) v.findViewById(R.id.btn_settings);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBarShow();
				
				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);

				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame_sub, new SettingFragment())
						.addToBackStack(null).commit();

				((SlidingFragmentActivity) getActivity()).getSlidingMenu()
						.showContent();
				

				//메뉴
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT);
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});

		btn = (Button) v.findViewById(R.id.btn_recruitinfo);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				actionBarShow();

				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.content_frame_sub,
								new RecuritmentFragment()).addToBackStack(null)
						.commit();

				((SlidingFragmentActivity) getActivity()).getSlidingMenu()
						.showContent();
				

				//메뉴
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT);
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});

		btn = (Button) v.findViewById(R.id.btn_message);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				actionBarShow();

				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame_sub, new MessageFragment())
						.addToBackStack(null).commit();

				((SlidingFragmentActivity) getActivity()).getSlidingMenu()
						.showContent();
				

				//메뉴
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT);
				((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});
		
		btn = (Button) v.findViewById(R.id.btn_invitation);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				actionBarShow();
				
				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, new InvitationFragment())
				.addToBackStack(null).commit();

		((SlidingFragmentActivity) getActivity()).getSlidingMenu()
				.showContent();
		

		//메뉴
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT);
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		});
		
		btn = (Button)v.findViewById(R.id.btn_like);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				actionBarShow();
				
				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_sub, new LikeFragment())
				.addToBackStack(null).commit();

		((SlidingFragmentActivity) getActivity()).getSlidingMenu()
				.showContent();
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setMode(SlidingMenu.LEFT);
		((SlidingFragmentActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					
			}
		});


		return v;		
	}
	
	@Override
	public void onResume() {
		if (profileView != null) {
			Bitmap bm = BitmapFactory.decodeFile(UserDataManager.getInstance().getUserCropImageUri().getPath());
			if (bm != null) {
				profileView.setImageBitmap(bm);
				profileView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
			}
		}
		info = PropertyManager.getInstance().getMyData();
		nameView.setText(info.userName);
		jogView.setText(info.userPosition);
		
		super.onResume();
	}

	private void actionBarShow() {
		if (!actionbar.isShowing()) {
			actionbar.show();
		}
	}

	
}
