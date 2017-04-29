package com.tacademy.depol.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.staggeredgridview.StaggeredGridView;
import com.staggeredgridview.StaggeredGridView.OnLoadmoreListener;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshBase;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.staggeredgridview.pulltorefresh.library.PullToRefreshStaggeredGridView;
import com.tacademy.depol.R;
import com.tacademy.depol.a.MyProjectAdapter;
import com.tacademy.depol.a.MyProjectAdapter.ImageClickListener;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.BasicInfo;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.data.UserItem;
import com.tacademy.depol.follow.FollowActivity;
import com.tacademy.depol.follow.FollowIngActivity;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.message.MessageFragment;
import com.tacademy.depol.message.MessageSendFragment;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.viewer.ChildFragment;
import com.tacademy.depol.viewer.ImageViewFragment;
import com.tacademy.depol.viewer.ImageViewFragment.IImageViewListener;
import com.tacademy.depol.widget.MyRoundedImageView;


public class ProfileActivity extends SherlockFragmentActivity {


	public static final int REQUEST_OTHER_TYPE = 1;
	public static final int REQUEST_PORTFOLIO_VIEW = 2;
	
	
	public static final String PORTFOLIO_ID_KEY = "IMAGE_KEY";
	public static final String POSITION_ID_KEY = "POSITION_ID_KEY";

	public final static String PORTFOLIO_USER_ID_KEY = "PORTFOLIO_USER_ID_KEY";
	public final static String PROFILE_TYPE_KEY = "PROFILE_KEY";
	public final static String PROFILE_OTHER_KEY = "PROFILE_OTHER_KEY";
	public final static String PROFILE_USER_ID_KEY = "PROFILE_USER_ID_KEY";
	
	private PullToRefreshStaggeredGridView stgv;
	private MyProjectAdapter mAdapter;
	private FragmentTabHost tabHost;
	private MyRoundedImageView iconView;
	private TextView followView;
	private TextView follwerView;
	private TextView nameView;
	private TextView jobView;
	private ProgressBar indicator;
	private UserItem userData;
	private UserBasicInfo info; //나의 데이터
	private NormalActionBar actionBarView;
	private boolean isMine = false;
	private DisplayImageOptions options;
	private ActionBar actionbar;
	private Bundle bundle;
	private int userId;
	private ImageView iconButton;
	
	
	
	private SimpleNetworkManagerListener<UserItem> mListener = new SimpleNetworkManagerListener<UserItem>() {
		
		public void onLoadDataListener(UserItem item) {
			userData = item;
			refresh(userData); 			
		}
		
		@Override
		public void onFailListener(int statusCode) {
			if (this != null) {
				finish();
				Toast.makeText(ProfileActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
			}
		}		
		
	};
	
	
	
	
	SherlockFragment fragment;
	public void currentFragment(SherlockFragment fragment) {
		this.fragment = fragment;	
	}
	
	@Override
	public void onBackPressed() {
		if (fragment != null) {
			if (fragment instanceof ChildFragment) {
				if (!((ChildFragment) fragment).onBackPressed()) {
					super.onBackPressed();
				}
			} else if (!fragment.getChildFragmentManager().popBackStackImmediate()) {
				super.onBackPressed();
			}
		}
		else {
			super.onBackPressed();
		}	
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_staggered_gridview_layout);
		ImageLoaderManager.getInstance().initialize(false, false, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
		
		bundle = new Bundle();
		Intent intent = getIntent();
		isMine = intent.getBooleanExtra(PROFILE_TYPE_KEY, false);
		userId = intent.getIntExtra(PROFILE_USER_ID_KEY, 0);
		bundle.putBoolean(PROFILE_TYPE_KEY, isMine);
		bundle.putInt(PROFILE_USER_ID_KEY, userId);
		
		info = PropertyManager.getInstance().getMyData();
		indicator = (ProgressBar)findViewById(R.id.loadindicator);		
		stgv = (PullToRefreshStaggeredGridView)findViewById(R.id.ptrstgv);		
		View profileView = LayoutInflater.from(this).inflate(R.layout.profile_my_layout, null, false);	

		
		nameView = (TextView)profileView.findViewById(R.id.profile_nameView);
		nameView.setTypeface(Typeface.createFromAsset(getAssets(), Font.ROBOTO_LITGT));
		jobView = (TextView)profileView.findViewById(R.id.profile_jobView);
		jobView.setTypeface(Typeface.createFromAsset(getAssets(), Font.ROBOTO_THIN));
		followView = (TextView)profileView.findViewById(R.id.profile_followView);
		followView.setTypeface(Typeface.createFromAsset(getAssets(), Font.ROBOTO_MEDIUM));
		follwerView = (TextView)profileView.findViewById(R.id.profile_follwerView);
		follwerView.setTypeface(Typeface.createFromAsset(getAssets(), Font.ROBOTO_MEDIUM));
		
		
		
		
		actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.BACK_MODE, getString(R.string.profile));
		actionBarView.setOnActionBarListener(actionbarListener);
		actionbar.setCustomView(actionBarView);
		
		
		tabHost = (FragmentTabHost) profileView.findViewById(R.id.p_tabhost);
		tabHost.setup(this, getSupportFragmentManager(), R.id.p_tabcontent);
		
		tabHost.addTab(tabHost.newTabSpec("project").setIndicator(""), MyPortfolioFragment.class, bundle);
		tabHost.addTab(tabHost.newTabSpec("info").setIndicator(""), ProfileInfoFragment.class, bundle);
		setCustomColorInit(tabHost);

		int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
		stgv.getRefreshableView().setItemMargin(margin);
		
		stgv.getRefreshableView().setHeaderView(profileView);
		mAdapter = new MyProjectAdapter(this);
		stgv.setAdapter(mAdapter);
		stgv.setOnLoadmoreListener(new OnLoadmoreListener() {
			
			@Override
			public void onLoadmore() {
				
			}
		});
		stgv.setOnRefreshListener(new OnRefreshListener<StaggeredGridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
				
				if (isMine) {
					 NetworkManager.getInstance().getUserProfile(ProfileActivity.this, info.userId, isMine, mListener);						
				} else {
					 NetworkManager.getInstance().getUserProfile(ProfileActivity.this, userId, isMine, mListener);					
				}
				
			}
			
		});
		
		mAdapter.setOnImageClickListener(new ImageClickListener() {

			@Override
			public void setOnImageClickListener(View v, PortfolioItem data, int position) {
				ImageViewFragment imageViewFragment = new ImageViewFragment();
				Bundle bundle = new Bundle();
				bundle.putInt(PORTFOLIO_ID_KEY, data.thumbPofolId);
				bundle.putInt(POSITION_ID_KEY, position);
				bundle.putBoolean(PROFILE_OTHER_KEY, true);
				imageViewFragment.setArguments(bundle);
				imageViewFragment.setOnImageViewListener(new IImageViewListener() {

					@Override
					public void onPortfolioRemove(int position) {
						mAdapter.clear();
						NetworkManager.getInstance().getUserProfile(ProfileActivity.this, info.userId, true, mListener);
					}
					
				});

				getSupportFragmentManager().beginTransaction()
						.replace(R.id.profile_content, imageViewFragment)
						.addToBackStack(null).commit();	
							
			}
		});
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("project")) {
					refresh(userData); 
					tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.profile_btn_projectk);
					tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.profile_btn_infok_non);
					stgv.getRefreshableView().setSelectionToTop();
				} else {
					tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.profile_btn_projectk_non);
					tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.profile_btn_infok);
					mAdapter.clear();
					stgv.getRefreshableView().setSelectionToTop();
				}
			}
		});

		iconView = (MyRoundedImageView) profileView.findViewById(R.id.profile_imageView);	
		iconButton = (ImageView)profileView.findViewById(R.id.icon_setting);

		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		} 
		
		
		//나 자신
		if (isMine) {
        	UserItem item = NetworkManager.getInstance().getCacheUserItem(ProfileActivity.this, info.userId, mListener);
        	if (item != null) {
        		userData = item;
        		refresh(item);
        	} else {
        		NetworkManager.getInstance().getUserProfile(this, info.userId, isMine, mListener);
        	}

            //에디터
    		iconButton.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {

    				ProfileEditFragment fragment = new ProfileEditFragment();
    				getSupportFragmentManager().beginTransaction()
					.replace(R.id.profile_content, fragment)
					.addToBackStack(null).commit();
    			}
    			
    		});	
    		
		} else {
			NetworkManager.getInstance().getUserProfile(ProfileActivity.this, userId, isMine, mListener);
        	//타 사용자
    		iconButton.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				if (userData == null) return;
    				
    				boolean isFollow = userData.isFollowed == 1 ? true : false;
    				if (isFollow) {
        				ServiceAPI.getInstance().RequestUnFollow(ProfileActivity.this, userId, serviceListener);	 					
    				} else {
        				ServiceAPI.getInstance().RequestFollow(ProfileActivity.this, userId, serviceListener);   		
    				}
    				
    			}
    		}); 
    		
    		
		}
        
        View btnView = (View)profileView.findViewById(R.id.profile_follow);
        btnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ProfileActivity.this, FollowIngActivity.class);
				if (isMine) {
					intent.putExtra(FollowActivity.USER_ID_KEY, info.userId);
				} else {
					intent.putExtra(FollowActivity.USER_ID_KEY, userId);					
				}
				startActivity(intent);	
			}
		});
        
        btnView = (View)profileView.findViewById(R.id.profile_follower);
        btnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
				if (isMine) {
					intent.putExtra(FollowActivity.USER_ID_KEY, info.userId);
				} else {
					intent.putExtra(FollowActivity.USER_ID_KEY, userId);					
				}
				startActivity(intent);	
			}
		});
        
        btnView = (View)profileView.findViewById(R.id.profile_message);
        btnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (userData == null) return;
				
				if (!isMine) {
					Bundle bundle = new Bundle();
					bundle.putInt(MessageSendFragment.MESSAGE_SEND_MODE, MessageSendFragment.MESSAGE_REPLAY);
					bundle.putInt(MessageSendFragment.MESSAGE_USER_ID_KEY, userId);
					bundle.putString(MessageSendFragment.MESSAGE_USER_NAME_KEY, userData.userName);
					
					MessageSendFragment fragment = new MessageSendFragment();
					fragment.setArguments(bundle);
					getSupportFragmentManager().beginTransaction()
					.replace(R.id.profile_content, fragment)
					.addToBackStack(null).commit();
				} else {
    				MessageFragment fragment = new MessageFragment();
    				getSupportFragmentManager().beginTransaction()
					.replace(R.id.profile_content, fragment)
					.addToBackStack(null).commit();
				}
			}
		});
        
        
        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
					if (iconView != null) {
						if (isMine) {
							try {
								Bitmap bm = BitmapFactory.decodeFile(UserDataManager.getInstance().getUserCropImageUri().getPath());
								if (bm != null) {
									iconView.setImageBitmap(bm);
									iconView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
								}
							} catch(OutOfMemoryError e) {
								
							}
						}
					}
					if (isMine) {
						String name = PropertyManager.getInstance().getMyData().userName;
						actionBarView.setMode(NormalActionBar.BACK_MODE, name);
					}
					actionBarView.setOnActionBarListener(actionbarListener);
					actionbar.setCustomView(actionBarView);
				}
			}
		});

	}
	
	private void refresh(UserItem item) {
		if (item == null) return;
		if (tabHost.getCurrentTabTag().equals("project")) {
			mAdapter.setData(item.pofol);			
		}
		follwerView.setText(String.format("%d", item.FollowerNum));
		followView.setText(String.format("%d", item.FollowingNum));
		nameView.setText(item.userName);
		jobView.setText(item.userPosition);
		
        stgv.setVisibility(View.VISIBLE);
        indicator.setVisibility(View.GONE);	 
        
		actionBarView.setCenterText(item.userName);
		
		if (isMine) {
    		if (iconView != null) {
    			Bitmap bm = BitmapFactory.decodeFile(UserDataManager.getInstance().getUserCropImageUri().getPath());
    			if (bm != null) {
    				iconView.setImageBitmap(bm);
    				iconView.setColor(GraphicsUtil.ConvertStrokeColor(PropertyManager.getInstance().getMyData().userRecruitStatus));
    			}
    		}
		} else {
			ImageLoader.getInstance().displayImage(item.userPropicUri, iconView, options);	
			iconView.setColor(GraphicsUtil.ConvertStrokeColor(item.userRecruitStatus));
			boolean isFollow = item.isFollowed == 1 ? true : false;
			if (isFollow) {
				iconButton.setImageResource(R.drawable.follow_check);
			} else {
				iconButton.setImageResource(R.drawable.follow_uncheck);
			}
		}
        stgv.onRefreshComplete();
	}
	
	public void refresh() {
		NetworkManager.getInstance().getUserProfile(ProfileActivity.this, info.userId, isMine, mListener);
	}
	
	public void basicRefresh(BasicInfo data) {
		nameView.setText(data.userName);
		jobView.setText(data.userPosition);
		actionBarView.setMode(NormalActionBar.BACK_MODE, data.userName);
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", tabHost.getCurrentTabTag());
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PORTFOLIO_VIEW && resultCode == Activity.RESULT_OK) {
			int position = data.getIntExtra(POSITION_ID_KEY, 0);
			int portfolioId = data.getIntExtra(PORTFOLIO_ID_KEY, 0);
			//NetworkManager.getInstance().getUserProfile(info.userId, true, mListener);
			//NetworkManager.getInstance().getCacheUserItem(ProfileActivity.this, info.userId, mListener).pofol.remove(position);
		}
	}
	
	@Override
	public void onDestroy() {
		ServiceAPI.getInstance().cancelNetwork(this);
		if (isMine) {
			NetworkManager.getInstance().unregister(mListener);
		}
		super.onDestroy();
	}
	
	private IActionBarListener actionbarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {
			//((SlidingFragmentActivity)getActivity()).showSecondaryMenu();
		}
		
		@Override
		public void onLeftButton(View v) {
			ProfileActivity.this.finish();
		}
		
	};
	
	private void setCustomColorInit(FragmentTabHost tabhost) {
		tabhost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.profile_btn_projectk);
		tabhost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.profile_btn_infok_non);
	}
	
	
	private SimpleServiceListener serviceListener =  new SimpleServiceListener(){
		
		@Override
		public void onFollowRequestSuccess() {
			if (userData != null) {
				userData.isFollowed = 1;
				if (iconButton != null) iconButton.setImageResource(R.drawable.follow_check);    								
				
			}
		}
		
		@Override
		public void onFollowRequestFail(int statusCode) {
			
		}

		@Override
		public void onFollowListRequestFail(int statusCode) {}
		
		@Override
		public void onUnFollowRequestSuccess() {
			if (userData != null) {
				userData.isFollowed = 0;
				if (iconButton != null) iconButton.setImageResource(R.drawable.follow_uncheck); 
			}
		}
		
	};
	
}
