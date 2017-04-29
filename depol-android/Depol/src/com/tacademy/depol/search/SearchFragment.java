package com.tacademy.depol.search;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.font.Font;

public class SearchFragment extends SherlockFragment {
	
	public final static int MAX_CATEGORY_COUNT = ApplicationContext.getContext().getResources().getStringArray(R.array.category).length;
	
	ListView listview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_search_layout, container, false);
		TextView categoryView = (TextView)v.findViewById(R.id.categoryView);
		final EditText searchEditView = (EditText)v.findViewById(R.id.search_edittext);

		categoryView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.ROBOTO_MEDIUM));
		searchEditView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), Font.NANUM_BARUN_GOTHIC));
		
		
		String[] str = getResources().getStringArray(R.array.category);
		CAdapter adapter = new CAdapter(getActivity(), str);
		listview = (ListView)v.findViewById(R.id.search_result_listview);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {	

				int idx = listview.getCount();
				int checkedCount = listview.getCheckedItemCount();
				//All 버튼 클릭시
				if (position == 0) {
					idx = listview.getCount();
					checkedCount = listview.getCheckedItemCount();
					if (idx == checkedCount+1) {
						for (int i=0;i<idx;i++) {
							listview.setItemChecked(i, false);
						}
						return;
					}
					
					for (int i=0;i<idx;i++) {
						listview.setItemChecked(i, true);
					}
				}
			}
		
		});
		
		ImageButton btn = (ImageButton)v.findViewById(R.id.btn_menu_search);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String keyword = searchEditView.getText().toString();
				SparseBooleanArray arr = listview.getCheckedItemPositions();
				ArrayList<Integer> categoryList = new ArrayList<Integer>();
				for(int i=0;i<arr.size();i++) {
					if (arr.get(arr.keyAt(i))) {
						int key = arr.keyAt(i);
						if (key == 0) continue;
						categoryList.add(key);
					}
				}
				
				//카테고리 선택 X / 키워드 없음
				if (categoryList.size() < 1 && keyword.equals("")) {
					Toast.makeText(getActivity(), getString(R.string.search_null), Toast.LENGTH_SHORT).show();
					return;
				}
				
				getActivity().getSupportFragmentManager().popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);

				
				Bundle bundle = new Bundle();
				bundle.putString(SearchResultFragment.SEARCH_KEYWORK_KEY, keyword);


				int category[] = ArrayUtils.toPrimitive(categoryList.toArray(new Integer[categoryList.size()]));
				bundle.putIntArray(SearchResultFragment.SEARCH_CATEGORY_KEY, category);
				SearchResultFragment searchResultFragment = new SearchResultFragment();
				searchResultFragment.setArguments(bundle);

				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame_sub, searchResultFragment)
						.addToBackStack(null).commit();
				
				((SlidingFragmentActivity) getActivity()).getSlidingMenu()
						.showContent();	
			}
		});
		
		return v;
		
	}
}
class CAdapter extends BaseAdapter {
	
	Context mContext;
	String[] mData;
	
	public CAdapter(Context context, String[] data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData[position];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchListViewCellItem view = null;
		if (convertView == null) {
			view = new SearchListViewCellItem(mContext);
		} else {
			view = (SearchListViewCellItem)convertView;
		}
		view.setData(mData[position]);
		return view;
	}
	
}
