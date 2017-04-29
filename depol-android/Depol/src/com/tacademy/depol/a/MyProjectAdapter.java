package com.tacademy.depol.a;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.staggeredgridview.widght.STGVImageView;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;

public class MyProjectAdapter extends BaseAdapter {
	
    private ArrayList<PortfolioItem> mItems;
    private Context mContext;
    private DisplayImageOptions options;
    private ImageClickListener mListener;
    
    public interface ImageClickListener {
    	public void setOnImageClickListener(View v, PortfolioItem data, int position);
    }
        
    public void clear() {
    	if (mItems != null)
    		mItems.clear();
    	notifyDataSetChanged();
    }
    
    public void setOnImageClickListener(ImageClickListener listener) {
    	mListener = listener;
    }
    
    public MyProjectAdapter (Context context) {
    	imageLoaderInit();
    	mContext = context;
    	mItems = new ArrayList<PortfolioItem> ();
    }
    
    public void setData(ArrayList<PortfolioItem> item) {
    	clear();
    	if (mItems != null)
    		mItems.addAll(item);
    	notifyDataSetChanged();
    }
    
    public void remove(int index) {
    	mItems.remove(index);
    	notifyDataSetChanged();
    }
    
    private void imageLoaderInit() {

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.broken_image)
		.showImageOnFail(R.drawable.broken_image)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(ApplicationContext.getContext()));
    }
    
    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }    

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		final PortfolioItem item = mItems.get(position);
		String url = item.thumbImgUri;

		if (convertView == null) {
			Holder holder = new Holder();
			view = View.inflate(mContext, R.layout.my_project_layout, null);
			holder.img_content = (STGVImageView)view.findViewById(R.id.img_my_project);
			holder.titleView = (TextView) view.findViewById(R.id.menu_like_desc);
			holder.replayCountView = (TextView) view.findViewById(R.id.newView);
			holder.likeCountView = (TextView) view.findViewById(R.id.oldView);
			holder.likeView = (ImageView) view.findViewById(R.id.my_like);
			
			view.setTag(holder);			
		} else {
			view = convertView;
		}
		Holder holder = (Holder) view.getTag();
		
		holder.img_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.setOnImageClickListener(v, item, position);
				}
			}
		});
		
		holder.titleView.setText(item.pofolTitle);
		holder.replayCountView.setText(String.format("%d", item.commentNum));
		holder.likeCountView.setText(String.format("%d", item.likeNum));
		boolean isLiked = item.isLiked == 1 ? true : false;
		if (isLiked) {
			holder.likeView.setImageResource(R.drawable.profile_like_act_2);
		} else {
			holder.likeView.setImageResource(R.drawable.profile_like_1);
		}
		ImageLoader.getInstance().displayImage(url, holder.img_content, options);
		return view;
    }
    
    class Holder {
        STGVImageView img_content;
        TextView titleView;
        TextView replayCountView;
        TextView likeCountView;
        ImageView likeView;
    }
}
