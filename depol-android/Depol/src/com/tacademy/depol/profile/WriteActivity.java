package com.tacademy.depol.profile;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.ImageUploadData;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.font.Font;
import com.tacademy.depol.model.UserDataManager;
import com.tacademy.depol.profile.ProgressDialogFragment.IProgressDialogListener;
import com.tacademy.depol.profile.WriteCategoryDialogFragment.IWriteCategoryFragmentListener;
import com.tacademy.depol.write.DynimicFileAdapterView;
import com.tacademy.depol.write.DynimicFileAdapterView.IDynimicFileAdapterListener;

public class WriteActivity extends SherlockFragmentActivity  {
	

	public static final String PORTFOLIO_MODIFY_MODE_KEY = "PORTFOLIO_MODIFY_MODE_KEY";
	public static final String PORTFOLIO_NAME_KEY = "PORTFOLIO_NAME_KEY";
	public static final String PORTFOLIO_DESC_KEY = "PORTFOLIO_DESC_KEY";
	public static final String PORTFOLIO_ID_KEY = "PORTFOLIO_ID_KEY";
	public static final String PORTFOLIO_CATEGORY_KEY = "PORTFOLIO_CATEGORY_KEY";
	public static final String PORTFOLIO_URI_KEY = "PORTFOLIO_URI_KEY";
	

	public static final int REQUEST_IMAGE_CODE = 0;
	public static final String RESPONSE_IMAGE_DATA_NAME = "DATA";
	public static final String IMAGE_COUNT = "IAMGE_COUNT";
	
	private int imageCount = 0;
	private int portfolioId;
	private ArrayList<MultplePhotoItem> mData;
	private DynimicFileAdapterView dynimicFileAdapterView;
	private WriteCategoryDialogFragment writeCategoryFragment;
	private ArrayList<Integer> categoryList;
	int[] category;
	private boolean isCategoryActive = false;
	View categoryView;
	
	EditText writeTitleView;
	EditText writeDescView;
	String modifyUri[];
	boolean modifyMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_title_layout);
		mData = new ArrayList<MultplePhotoItem>();
		categoryList = new ArrayList<Integer>();
		

		
		// 액션바
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		
		NormalActionBar actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.write), getString(R.string.ok_kor));
		actionBarView.setOnActionBarListener(actionBarListener);
		
		
		dynimicFileAdapterView = (DynimicFileAdapterView)findViewById(R.id.dynimicFileAdapterView);	
		categoryView = (View)findViewById(R.id.write_content);


		writeTitleView = (EditText)findViewById(R.id.write_title_edit_text);
		writeTitleView.setTypeface(Typeface.createFromAsset(getAssets(), Font.NANUM_BARUN_GOTHIC));
		writeDescView = (EditText)findViewById(R.id.write_desc_edit_text);
		writeDescView.setTypeface(Typeface.createFromAsset(getAssets(), Font.NANUM_BARUN_GOTHIC));
		
		Intent intent = getIntent();
		modifyMode = intent.getBooleanExtra(PORTFOLIO_MODIFY_MODE_KEY, false);
		if (!modifyMode) { // 수정모드 아님
			String path = intent.getStringExtra(SelectDialogFragment.REQUEST_CAMERA_CAPTURE_PORTFOLIO);
			if (path != null && !path.equals("")) {
				mData.add(new MultplePhotoItem(0, path));
			} else {
				if (imageCount <= 0) {
					Intent i = new Intent(this, PhotoMultpleActivity.class);			
					startActivityForResult(i, REQUEST_IMAGE_CODE);	
				}
			}
		} else {
			String title = intent.getStringExtra(PORTFOLIO_NAME_KEY);
			String desc = intent.getStringExtra(PORTFOLIO_DESC_KEY);
			portfolioId = intent.getIntExtra(PORTFOLIO_ID_KEY, -1);
			category = intent.getIntArrayExtra(PORTFOLIO_CATEGORY_KEY);
			
			modifyUri = intent.getStringArrayExtra(PORTFOLIO_URI_KEY);
			for(int i=0;i<modifyUri.length;i++) {
				mData.add(new MultplePhotoItem(-1, modifyUri[i]));
			}
			writeTitleView.setText(title);
			writeDescView.setText(desc);
			actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.write_modify), getString(R.string.ok_kor));
		}	
		imageinit(mData);	
		actionbar.setCustomView(actionBarView);

		
		
		
		dynimicFileAdapterView.setOnDynimicFileAdapterListener(new IDynimicFileAdapterListener() {
			
			@Override
			public void onAddViewClickListener() {
				Intent intent = new Intent(WriteActivity.this, PhotoMultpleActivity.class);	
				intent.putExtra(IMAGE_COUNT, imageCount);
				startActivityForResult(intent, REQUEST_IMAGE_CODE);	
			}

			@Override
			public void onRemoveListener(MultplePhotoItem data) {
				mData.remove(data);
				dynimicFileAdapterView.setData(mData);
				dynimicFileAdapterView.populate();				
			}
		});


		
		ImageButton btn = (ImageButton)findViewById(R.id.write_btn_category);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Bundle bundle = new Bundle();
				bundle.putIntArray(WriteActivity.PORTFOLIO_CATEGORY_KEY, category);
				writeCategoryFragment = new WriteCategoryDialogFragment();
				writeCategoryFragment.setArguments(bundle);
				writeCategoryFragment.setOnWriteCategoryFragmentListener(new IWriteCategoryFragmentListener() {
					
					@Override
					public void onConfirmListener(View v, ArrayList<Integer> data) {
						categoryList = data;
						category = ArrayUtils.toPrimitive(categoryList.toArray(new Integer[categoryList.size()]));
						if (isCategoryActive) {
							categoryView.setVisibility(View.GONE);
							isCategoryActive = false;
						}
					}
				});			
				writeCategoryFragment.show(getSupportFragmentManager(), "");
			}
		});
		
	}
	
	private void imageinit(ArrayList<MultplePhotoItem> data) {
		dynimicFileAdapterView.setData(data);
		dynimicFileAdapterView.populate();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//이미지 추가 작업
		if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {
			ArrayList<MultplePhotoItem> arr = data.getParcelableArrayListExtra(RESPONSE_IMAGE_DATA_NAME);
			mData.addAll(arr);
			imageinit(mData);
		}
	}
	
	
	
	
	private IActionBarListener actionBarListener = new IActionBarListener() {
		
		@Override
		public void onRightButton(View v) {
			
			String title = writeTitleView.getText().toString();
			String desc = writeDescView.getText().toString();
			if (title == null || title.equals("")) {
				Toast.makeText(WriteActivity.this, getString(R.string.write_title), Toast.LENGTH_SHORT).show();
				return;
			}
			if (desc == null || desc.equals("")) {
				Toast.makeText(WriteActivity.this, getString(R.string.write_desc), Toast.LENGTH_SHORT).show();
				return;				
			}
			

			String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
			File[] file = new File[mData.size()];
		
			ImageUploadData imageDto = new ImageUploadData();
			if (modifyMode) {
				imageDto.imgId = portfolioId;
			}
			imageDto.pofolTitle = writeTitleView.getText().toString();
			imageDto.pofolText = writeDescView.getText().toString();
			imageDto.category = ArrayUtils.toPrimitive(categoryList.toArray(new Integer[categoryList.size()]));			
							
			
			for(int i=0;i<mData.size();i++) {
				Uri uri = Uri.parse(mData.get(i).path);
				if (uri.getScheme().equals("http")) {
					Bitmap bitmap = ImageLoader.getInstance().loadImageSync(mData.get(i).path);
					File fileData = new File(Environment.getExternalStorageDirectory(), "temp_upload"+i+".jpg");
					if (bitmap != null) UserDataManager.getInstance().saveImage(bitmap, fileData);
					file[i] = new File(fileData.getPath());					
				}
				else if (uri.getScheme().equals("file")) {
					file[i] = new File(uri.getPath());
				} else if (uri.getScheme().equals("content")) {
					Cursor cursor = ApplicationContext.getContext().getContentResolver().query(uri, proj, null, null, null);
					if (cursor.moveToNext()) { //초기는 -1 주의
						int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						String dataPath = cursor.getString(column_index);
						file[i] = new File(dataPath.toString());
					}	
				}
			}
			
			ProgressDialogFragment dialog = new ProgressDialogFragment(imageDto, file);
			dialog.setOnProgressDialogListener(new IProgressDialogListener() {
				
				@Override
				public void onFileUploadSuccess() {
					setResult(RESULT_OK);
					finish();
				}
				
				@Override
				public void onFileUploadFail(int statusCode) {
					
				}

				@Override
				public void onFileUpdateFail(int statusCode) {
					
				}

				@Override
				public void onFileUpdateSuccess() {
					setResult(RESULT_OK);
					finish();	
				}
			});
			if (!modifyMode) {
				dialog.show(getSupportFragmentManager(), "UPLOAD");
			} else {
				dialog.show(getSupportFragmentManager(), "UPDATE");
			}
			
		}
		
		@Override
		public void onLeftButton(View v) {
			finish();
		}
	};
	
}
