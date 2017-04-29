package com.tacademy.depol.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.tacademy.depol.R;
import com.tacademy.depol.actionbar.NormalActionBar;
import com.tacademy.depol.actionbar.NormalActionBar.IActionBarListener;
import com.tacademy.depol.data.UserBasicInfo;
import com.tacademy.depol.listview.ListViewCheckCellItemView;
import com.tacademy.depol.model.PropertyManager;
import com.tacademy.depol.model.ServiceAPI;
import com.tacademy.depol.model.SimpleServiceListener;
import com.tacademy.depol.util.GraphicsUtil;

public class IncruitListActivity extends SherlockActivity {
	
	public static final int REQUEST_JOB_TYPE = 0;

	private ListView listview;
	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_check_layout);

		listview = (ListView) findViewById(R.id.incruit_state_listview);
		
		String[] typeName = { getResources().getString(R.string.job_normal),
				  getResources().getString(R.string.job_hunter),
				  getResources().getString(R.string.job_free),
				  getResources().getString(R.string.job_parttime) 
				};		
		MyAdapter adapter = new MyAdapter(this, typeName);

		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setAdapter(adapter);	
		listview.setItemChecked(PropertyManager.getInstance().getRecruitStatus()-1, true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				IncruitListActivity.this.position = position+1;
			}
			
		});
		
		// ¾×¼Ç¹Ù
		NormalActionBar actionBarView = new NormalActionBar(this);
		actionBarView.setMode(NormalActionBar.LEFT_STRING_RIGHT_MODE, getString(R.string.job_info), getString(R.string.ok_kor));
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionbar.setCustomView(actionBarView);
		actionBarView.setOnActionBarListener(new IActionBarListener() {
			
			@Override
			public void onRightButton(View v) {
				PropertyManager.getInstance().setRecruitStatus(position);
				ServiceAPI.getInstance().RequestUpdateProfile(IncruitListActivity.this, new SimpleServiceListener<UserBasicInfo>(){
					@Override
					public void onProfileUpdateSuccess(UserBasicInfo data) {
						setResult(RESULT_OK);
						finish();						
					}
					@Override
					public void onProfileUpdateFail(int statusCode) {
						if (IncruitListActivity.this != null) {
							finish();
						}						
					}					
				});			
			}
			
			@Override
			public void onLeftButton(View v) {
				finish();				
			}
		});
		
	}
	
}
class MyAdapter extends BaseAdapter {
	
	private Context mContext;
	private String[] mTypeName;

	
	public MyAdapter(Context context, String[] data) {
		this.mContext = context;
		this.mTypeName = data;
	}
	
	@Override
	public int getCount() {
		return mTypeName.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mTypeName[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListViewCheckCellItemView view = null;
		if (convertView == null) {
			view = new ListViewCheckCellItemView(mContext);
		} else {
			view = (ListViewCheckCellItemView)convertView;
		}
		//view.setData(position, ListViewCheckCellItemView.INCRUIT_STATE_INCRUIT);
		view.setTitleData(mTypeName[position], GraphicsUtil.ConvertStrokeColor(position+1), position);
		return view;
	}	
}
