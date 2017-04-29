package com.tacademy.depol.profile;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.tacademy.depol.R;

public class WriteCategoryDialogFragment extends SherlockDialogFragment {
	
	private CategoryAdapter adapter;
	private ListView listView;
	private boolean isAllCheck = false;
	
	public interface IWriteCategoryFragmentListener {
		public void onConfirmListener(View v, ArrayList<Integer> data); 
	}
	
	public void setOnWriteCategoryFragmentListener(IWriteCategoryFragmentListener listener) {
		mListener = listener;
	}
	IWriteCategoryFragmentListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); 
        return dialog;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.write_category_layout, container, false);
		
		Bundle bundle = getArguments();

		
		adapter = new CategoryAdapter(getActivity());
		listView = (ListView)v.findViewById(R.id.write_category_listview);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//All 버튼 클릭시
				if (position == 0) {
					int idx = listView.getCount();					
					if (isAllCheck) {
						for (int i=0;i<idx;i++) {
							listView.setItemChecked(i, false);
						}
						isAllCheck = false;
					} else {
						for (int i=0;i<idx;i++) {
							listView.setItemChecked(i, true);
						}
						isAllCheck = true;
					}
					
				}
			}
		});
				
		int[] category = bundle.getIntArray(WriteActivity.PORTFOLIO_CATEGORY_KEY);
		if (category != null && category.length > 0) {
			for(int i=0;i<category.length;i++) {
				int position = category[i];
				listView.setItemChecked(position, true);
			}
		}
		
		
		ImageButton btn = (ImageButton)v.findViewById(R.id.write_category_confirm);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				SparseBooleanArray arr = listView.getCheckedItemPositions();
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(int i=0;i<arr.size();i++) {
					//if (i == 0) continue; // 0번은 All 버튼 이기때문에 무시
					if (arr.get(arr.keyAt(i))) {
						int key = arr.keyAt(i);
						if (key == 0) continue;
						list.add(key);
					}
				}					

				if (mListener != null) {
					mListener.onConfirmListener(v, list);
				}
				dismiss();
			}
			
		});
		btn = (ImageButton)v.findViewById(R.id.write_category_cancel);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dismiss();
			}
			
		});
		return v;
	}
}
class CategoryAdapter extends BaseAdapter {
	
	private Context mContext;
	private String[] category;
	
	public CategoryAdapter(Context context) {
		mContext = context;
		category = context.getResources().getStringArray(R.array.category);
	}

	@Override
	public int getCount() {
		return category.length;
	}

	@Override
	public Object getItem(int position) {
		return category[position];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		WriteCategoryListViewCell view = null;
		if (convertView == null) {
			view = new WriteCategoryListViewCell(mContext);
		} else {
			view = (WriteCategoryListViewCell)convertView;
		}
		view.setData(category[position]);
		return view;
	}
	
}