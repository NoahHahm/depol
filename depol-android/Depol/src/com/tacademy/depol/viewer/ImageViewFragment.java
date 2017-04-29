package com.tacademy.depol.viewer;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.HackyViewPager;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.main.MainActivity;
import com.tacademy.depol.model.ImageLoaderManager;
import com.tacademy.depol.model.NetworkManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleNetworkManagerListener;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.profile.EditDialogFragment;
import com.tacademy.depol.profile.EditDialogFragment.IEditDialogListener;
import com.tacademy.depol.profile.LoadDialogFragment;
import com.tacademy.depol.profile.ProfileActivity;
import com.tacademy.depol.profile.WriteActivity;
import com.tacademy.depol.util.GraphicsUtil;
import com.tacademy.depol.viewer.Adapter.ImageTapListener;
import com.tacademy.depol.widget.UserRoundedImageView;

public class ImageViewFragment extends ChildFragment {
	
	public final static int REQUEST_MODIFY_MODE = 1;
	public final static String PORTFOLIO_MAIN_MODE_KEY = "PORTFOLIO_MAIN_MODE_KEY";

	private LoadDialogFragment loadDialog;
	private TextView countView;
	private TextView userNameView;
	private TextView descView;
	private TextView titleView;
	private TextView replayCountView;
	private TextView likeCountView;
	
	
	private UserRoundedImageView imageView;
	private PortfolioItem data;
	private int portfolioId;
	private int position;
	private ImageButton btnLike;
	private Button btnEdit;
	private ActionBar actionBar;
	View bottomSubView;
	private boolean isMine = false;
	private boolean isLike = false;
	private boolean isMainMode = false;
	boolean isOther = false;
	View view;
	Adapter adapter;
	ViewPager viewPager;
	View detailView;
	boolean isSubMenuActive = false;
	
	private DisplayImageOptions options;	
	
	public interface IImageViewListener {
		public void onPortfolioRemove(int position);
	}
	public void setOnImageViewListener(IImageViewListener listener) {
		mListener = listener;
	}
	IImageViewListener mListener;
	
	
	private SimpleServiceListener likeListener = new SimpleServiceListener() {
		
		public void onPortfolioLikeRequestFail(int statusCode) {
			
		}
		public void onPortfolioLikeRequestSuccess() {
			if (btnLike != null) btnLike.setImageResource(R.drawable.detail_like_act2);
			
		}
		public void onPortfolioUnLikeRequestFail(int statusCode) {
			
		}
		public void onPortfolioUnLikeRequestSuccess() {
			if (btnLike != null) btnLike.setImageResource(R.drawable.detail_like);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.ARGB_8888);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();

		// 프로필에서 왔는지, 유저보기 에서 왔는지
		Bundle bundle = getArguments();
		portfolioId = bundle.getInt(ProfileActivity.PORTFOLIO_ID_KEY);
		if (portfolioId < 1) {
			getActivity().getSupportFragmentManager().popBackStack();
		}
		position = bundle.getInt(ProfileActivity.POSITION_ID_KEY);
		isMainMode = bundle.getBoolean(PORTFOLIO_MAIN_MODE_KEY, false);
		isOther = bundle.getBoolean(ProfileActivity.PROFILE_OTHER_KEY);
		
		loadDialog = new LoadDialogFragment();
		loadDialog.show(getChildFragmentManager(), null);
		NetworkManager.getInstance().getPortfolio(getActivity(), portfolioId, networkListener);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.viewer_fullscreen_image_layout, container, false);
		detailView = (View)view.findViewById(R.id.detailView);
		
		
		

		viewPager = (HackyViewPager)view.findViewById(R.id.pager_fullimage);
		final View subView = (View)view.findViewById(R.id.gallery_sub);

		likeCountView = (TextView)view.findViewById(R.id.like_count);
		likeCountView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_MEDIUM));	
		replayCountView = (TextView)view.findViewById(R.id.replay_count);	
		replayCountView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_MEDIUM));	
		
		countView = (TextView)view.findViewById(R.id.g_tv_count);	
		countView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_LITGT));	
		userNameView = (TextView)view.findViewById(R.id.g_nameView);		
		userNameView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_MEDIUM));
		descView = (TextView)view.findViewById(R.id.g_tv_desc);		
		descView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_THIN));
		titleView = (TextView)view.findViewById(R.id.g_tv_title);
		titleView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Font.ROBOTO_MEDIUM));
		imageView = (UserRoundedImageView)view.findViewById(R.id.picture_userview);
		bottomSubView = (View)view.findViewById(R.id.bottom_sub);

		
		btnEdit = (Button)view.findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt(ProfileActivity.PORTFOLIO_ID_KEY, portfolioId);
				EditDialogFragment dialog = new EditDialogFragment();
				dialog.setArguments(bundle);
				dialog.show(getChildFragmentManager(), "");
				dialog.setOnEditDialogListener(new IEditDialogListener() {
					
					@Override
					public void onRemove(final int portfolioId) {
						
						final LoadDialogFragment dialog = new LoadDialogFragment();
						dialog.show(getChildFragmentManager(), null);
						
						NetworkManager.getInstance().removePortfolio(getActivity(), portfolioId, new SimpleNetworkManagerListener(){
							
							@Override
							public void onPortfolioRemoveFail(int statusCode) {
								
							}
							
							@Override
							public void onPortfolioRemoveSuccess() {
								dialog.dismiss();
								if (mListener != null) {
									getActivity().getSupportFragmentManager().popBackStack();
									mListener.onPortfolioRemove(position);
								}
							}
						});					
						

					}

					@Override
					public void onModify(int portfolioId) {
						Intent intent = new Intent(getActivity(), WriteActivity.class);
						intent.putExtra(WriteActivity.PORTFOLIO_MODIFY_MODE_KEY, true);
						intent.putExtra(WriteActivity.PORTFOLIO_NAME_KEY, data.pofolTitle);
						intent.putExtra(WriteActivity.PORTFOLIO_DESC_KEY, data.pofolText);
						intent.putExtra(WriteActivity.PORTFOLIO_ID_KEY, portfolioId);
						intent.putExtra(WriteActivity.PORTFOLIO_URI_KEY, data.pofolImgUri);
						intent.putExtra(WriteActivity.PORTFOLIO_CATEGORY_KEY, data.category);
						startActivityForResult(intent, REQUEST_MODIFY_MODE);
					}
				});
			}
		});
		

		adapter = new Adapter(getActivity());
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//데이터 없는 경우
				if (data == null) {
					return;
				}
				
				isMine = data.isMine == 1 ? true : false;			
				
				//나 자신
				if (isMine) {
					return;
				}
				
				//다른사람 중복 클릭
				if (isOther) {
					return;
				}

				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				intent.putExtra(ProfileActivity.PROFILE_USER_ID_KEY, data.userId);
				intent.putExtra(ProfileActivity.PROFILE_TYPE_KEY, isMine);
				getActivity().startActivity(intent);
				
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				countView.setText(position+1+"/"+data.pofolImgUri.length+"");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {				
			
			}
			
		});
		adapter.setOnImageTapListener(new ImageTapListener() {
			
			@Override
			public void setOnImageTapListener(View view, float x, float y) {
				if (subView.getVisibility() == View.VISIBLE) {
					Animation ani = AnimationUtils.loadAnimation(getActivity(), R.anim.gone_fade);
					subView.setVisibility(View.GONE);
					subView.startAnimation(ani);					
				} else {
					Animation ani = AnimationUtils.loadAnimation(getActivity(), R.anim.visible_fade);
					subView.setVisibility(View.VISIBLE);
					subView.startAnimation(ani);								
				}
			}
		});
		
		ImageButton imgBtn = (ImageButton)view.findViewById(R.id.g_btn_back);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		imgBtn = (ImageButton)view.findViewById(R.id.btn_comment);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				if (data == null) return;
				
				getChildFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				
				Bundle bundle = new Bundle();
				bundle.putInt(CommentParentFragment.PORTFOLIO_ID_KEY, portfolioId);
				bundle.putInt(CommentParentFragment.PORTFOLIO_USER_ID_KEY, data.userId);
				bundle.putInt(CommentParentFragment.LIKE_COUNT_ID_KEY, data.likeNum);
				
				CommentParentFragment commentParentFragment = new CommentParentFragment();
				commentParentFragment.setArguments(bundle);
				
				getChildFragmentManager().beginTransaction()
				.replace(R.id.bottom_sub, commentParentFragment)
				.addToBackStack(null)
				.commit();
			}
		});
		imgBtn = (ImageButton)view.findViewById(R.id.btn_share);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (data == null) return;
				
				getChildFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);

				Bundle bundle = new Bundle();
				ShareFragment shareFragment = new ShareFragment();
				bundle.putString(ShareFragment.IMAGE_URL, data.pofolImgUri[0]);
				shareFragment.setArguments(bundle);
				getChildFragmentManager().beginTransaction()
				.replace(R.id.bottom_sub, shareFragment)
				.addToBackStack(null)
				.commit();
			}
		});

		btnLike = (ImageButton)view.findViewById(R.id.btn_like);
		btnLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (data == null) return;
				int count = 0;
				try {
					count = Integer.parseInt(likeCountView.getText().toString());
				} catch(NumberFormatException e) {
					
				}
				
				if (isLike) {
					isLike = false;
					likeCountView.setText(String.format("%d", count-1));
					ServiceAPI.getInstance().RequestPortfolioUnLike(getActivity(), data.userId, portfolioId, likeListener);
				} else {
					isLike = true;
					likeCountView.setText(String.format("%d", count+1));
					ServiceAPI.getInstance().RequestPortfolioLike(getActivity(), data.userId, portfolioId, likeListener);
				}
			}
		});

		return view;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (getActivity() instanceof MainActivity) {
			((MainActivity)getActivity()).currentFragment(this);			
		} else if (getActivity() instanceof ProfileActivity) {
			((ProfileActivity)getActivity()).currentFragment(this);
		}

		if (getActivity() instanceof SlidingFragmentActivity) {
			actionBar = ((SlidingFragmentActivity)getActivity()).getSupportActionBar();		
		} else if (getActivity() instanceof SherlockFragmentActivity) {
			actionBar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();		
		}
		
		if (actionBar.isShowing()) {
			actionBar.hide();
		}
	}
	
	@Override
	public void onPause() {
		if (getActivity() instanceof MainActivity) {
			((MainActivity)getActivity()).currentFragment(null);			
		} else if (getActivity() instanceof ProfileActivity) {
			((ProfileActivity)getActivity()).currentFragment(null);
		}
		super.onPause();
		
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (getActivity() instanceof SlidingFragmentActivity) {
			actionBar = ((SlidingFragmentActivity)getActivity()).getSupportActionBar();		
		} else if (getActivity() instanceof SherlockFragmentActivity) {
			actionBar = ((SherlockFragmentActivity)getActivity()).getSupportActionBar();		
		}
		
		if (!actionBar.isShowing()) {
			actionBar.show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if (requestCode == REQUEST_MODIFY_MODE && resultCode == Activity.RESULT_OK) {
			NetworkManager.getInstance().getPortfolio(getActivity(), portfolioId, networkListener);
		}
	}
	
	
	SimpleNetworkManagerListener<PortfolioItem> networkListener = new SimpleNetworkManagerListener<PortfolioItem>() {

		@Override
		public void onLoadDataListener(PortfolioItem item) {
			data = item;
			
			imageView.setVisibility(View.VISIBLE);
			countView.setText(1+"/"+item.pofolImgUri.length+"");
			userNameView.setText(item.userName);
			descView.setText(item.pofolText);
			titleView.setText(item.pofolTitle);
			ImageLoader.getInstance().displayImage(item.userPropicUri, imageView, options);
			imageView.setColor(GraphicsUtil.ConvertStrokeColor(item.userRecruitStatus));	
			adapter.setData(item);
			viewPager.setAdapter(adapter);
			
			isLike = item.isLiked == 1 ? true : false;
			if (isLike) {
				btnLike.setImageResource(R.drawable.detail_like_act2);
			} else {
				btnLike.setImageResource(R.drawable.detail_like);
			}
			likeCountView.setText(String.format("%d", item.likeNum));
			replayCountView.setText(String.format("%d", item.commentNum));

			isMine = item.isMine == 1 ? true : false;
			if (isMine && !isMainMode) {
				if (btnEdit.getVisibility() == View.GONE) {
					btnEdit.setVisibility(View.VISIBLE);
				}
			}
			

			loadDialog.dismiss();
		}
		
		@Override
		public void onFailListener(int statusCode) {
			if (getActivity() != null) {
				getActivity().getSupportFragmentManager().popBackStack();		
			}
			
			if (loadDialog != null) {
				loadDialog.dismiss();
			}
		}
		
	};
	
	
	
}

class Adapter extends PagerAdapter {	

	private Context mContext;
	private PortfolioItem item;	
    private DisplayImageOptions options;	
    
	public interface ImageTapListener {
		public void setOnImageTapListener(View view, float x, float y);
	}	
	
	ImageTapListener mListener;
	public void setOnImageTapListener(ImageTapListener listener) {
		this.mListener = listener;
	}
	
	public Adapter(Context context, PortfolioItem item) {
		this.mContext = context;
		this.item = item;
		ImageLoaderInitialize();
	}
	
	public Adapter(Context context) {
		this.mContext = context;
		ImageLoaderInitialize();
	}
	
	private void ImageLoaderInitialize() {
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.ARGB_8888);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
	}
	
	public void setData(PortfolioItem data) {
		item = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return item == null ? 0 : item.pofolImgUri.length;
	}
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		PhotoView photoView = new PhotoView(container.getContext());
		photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (mListener != null) {
					mListener.setOnImageTapListener(view, x, y);
				}
			}
		});
		ImageLoader.getInstance().displayImage(item.pofolImgUri[position], photoView, options);
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
		return photoView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	


}