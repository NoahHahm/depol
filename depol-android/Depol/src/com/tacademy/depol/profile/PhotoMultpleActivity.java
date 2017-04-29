package com.tacademy.depol.profile;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.MultplePhotoItem;
import com.tacademy.depol.listview.PhotoMultpleCellItem;
import com.tacademy.depol.model.ImageLoaderManager;

public class PhotoMultpleActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor> {

	public final static int MAX_CHECK_COUNT = 10; //맥스 체크 데이터 갯수
	public final static String SELECT_COUNT = "SELECT_COUNT";
	
	
	private final static int NOT_FIXED = -1;
	private int idIndex = NOT_FIXED;
	
	private String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA };
	private GridView gridview;
	private SimpleCursorAdapter mAdapter;
	private String[] columns = { MediaStore.Images.Media._ID };
	private DisplayImageOptions options;
	private ArrayList<Integer> arrSelect = new ArrayList<Integer>();
	private ArrayList<MultplePhotoItem> mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.photo_multple_choice_layout);
	    
		ImageLoaderManager.getInstance().initialize(true, true, Bitmap.Config.RGB_565);
		options = ImageLoaderManager.getInstance().getDisplayImageOptions();
		

	    mData = new ArrayList<MultplePhotoItem>();
		gridview = (GridView)findViewById(R.id.gridView1);
		gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		
		// 액션바		
		NormalActionBar actionBarView = new NormalActionBar(this);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.photo), getString(R.string.ok));
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				for (int i=0;i<arrSelect.size();i++) {
					Cursor cursor = (Cursor)gridview.getItemAtPosition(arrSelect.get(i));
					int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
					long id = cursor.getLong(index);
					Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);		

					mData.add(new MultplePhotoItem(0, uri.toString()));
				}
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra(WriteActivity.RESPONSE_IMAGE_DATA_NAME, mData);
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onLeftButton(View v) {
				finish();
			}
		});
		actionbar.setCustomView(actionBarView);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int checkedCount = gridview.getCheckedItemCount();
				Intent data = getIntent();
				int imageCount = data.getIntExtra(WriteActivity.IMAGE_COUNT, 0);
				int current = MAX_CHECK_COUNT - imageCount;
				
				// N개 추가 선택
				if (current < checkedCount) {
					gridview.setItemChecked(position, false);
					Toast.makeText(PhotoMultpleActivity.this, current+getString(R.string.image_current_count), Toast.LENGTH_SHORT).show();
					return;
				}
				
				// 5개 선택 이상일경우
				if (checkedCount > MAX_CHECK_COUNT) {
					gridview.setItemChecked(position, false);
					Toast.makeText(PhotoMultpleActivity.this, current+getString(R.string.image_max_count), Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (((PhotoMultpleCellItem)view).isChecked()) {
					arrSelect.add(position);
				} else {
					for(int i=0;i<arrSelect.size();i++) {
						if (arrSelect.get(i) == position) {
							arrSelect.remove(i);
						}
					}
				}

			}
			
		});
		mAdapter = new SimpleCursorAdapter(this, R.layout.photo_item, null,
				columns, new int[] { R.id.photo_img }, 0);
		mAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor c, int columnIndex) {
				if (idIndex == NOT_FIXED) {
					idIndex = c.getColumnIndex(MediaStore.Images.Media._ID);
				}
				if (idIndex == columnIndex) {
					long id = c.getLong(columnIndex);
					ImageView iv = (ImageView) view;
					Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);					
					ImageLoader.getInstance().displayImage(uri.toString(), iv, options);
					return true;
				}
				return false;
			}
			
			
		});
		gridview.setAdapter(mAdapter);
		getSupportLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int code, Bundle b) {
		return new CursorLoader(this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Video.Media._ID + " desc");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursor, Cursor c) {
		mAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		mAdapter.swapCursor(null);
	}


}