package com.tacademy.depol.profile.dynamicview;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tacademy.depol.R;
import com.tacademy.depol.data.AbilityInfo;
import com.tacademy.depol.font.Font;

public class InfographicsDialogFragment extends DynamicDialogFragment {
	
	public InfographicsDialogFragment() {
		
	}
	private ArrayList<AbilityInfo> mData;
	EditText editText;
	InfoAdapter adapter;
	public interface IDynamicDialogListener {
		public void onDynamicDialogVerify(ArrayList<AbilityInfo> data);
	}
	IDynamicDialogListener mListener;
	public void setOnDynamicDialogListener(IDynamicDialogListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new InfoAdapter(getActivity());
		mData = new ArrayList<AbilityInfo>();
		
		String[] skills = getResources().getStringArray(R.array.skill);
		for(int i=0;i<skills.length;i++) {
			mData.add(new AbilityInfo(skills[i]));
		}
		adapter.setData(mData);
	}
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
		View dialogView = inflater.inflate(R.layout.profile_infographics_dialog_layout, container, false);
		editText = (EditText)dialogView.findViewById(R.id.infographics_editText);
		TextView infoContentView = (TextView)dialogView.findViewById(R.id.info_content);
		infoContentView.setTypeface(Font.get(Font.ROBOTO_LITGT, getActivity()));
		TextView infoTitleView = (TextView)dialogView.findViewById(R.id.info_title);
		infoTitleView.setTypeface(Font.get(Font.ROBOTO_BOLD, getActivity()));

		final ListView listview = (ListView)dialogView.findViewById(R.id.infoListView);
		listview.setAdapter(adapter);
		
		ImageButton btn = (ImageButton)dialogView.findViewById(R.id.infograpgics_verity);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mData = new ArrayList<AbilityInfo>();
				SparseBooleanArray arr = listview.getCheckedItemPositions();
				for(int i=0;i<arr.size();i++) {
					if (arr.get(arr.keyAt(i))) {
						AbilityInfo item = (AbilityInfo) listview.getItemAtPosition(arr.keyAt(i));
						mData.add(item);
					}
				}
				
				dismiss();
				if (mListener != null) {
					mListener.onDynamicDialogVerify(mData);
				}
			}
		});
		
		btn = (ImageButton)dialogView.findViewById(R.id.infographics_add);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str = editText.getText().toString();
				if (str == null || str.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.infographics_str), Toast.LENGTH_SHORT).show();
					return;
				}
				editText.setText("");
				mData.add(new AbilityInfo(str));
				adapter.setData(mData);
				listview.setSelection(adapter.getCount()-1);
			}
			
		});
		btn = (ImageButton)dialogView.findViewById(R.id.infograpgics_cancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
			
		});
		
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		return dialogView;
    }
}

class InfoAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<AbilityInfo> mData;
	
	public InfoAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<AbilityInfo>();
	}
	
	public void setData(ArrayList<AbilityInfo> data) {
		mData.clear();
		mData.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}
	
	public void addAll(ArrayList<String> data) {
		for(int i=0;i<data.size();i++) {
			mData.add(new AbilityInfo(data.get(i)));		
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}
	
	public ArrayList<AbilityInfo> getData() {
		return mData;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		InfographicsListViewCell view = null;
		if (convertView == null) {
			view = new InfographicsListViewCell(mContext);
		} else {
			view = (InfographicsListViewCell)convertView;
		}
		view.setData(mData.get(position).program);
		return view;
	}
	
}
