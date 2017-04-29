package com.tacademy.depol.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.staggeredgridview.widght.STGVImageView;
import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;
import com.tacademy.depol.data.PortfolioItem;
import com.tacademy.depol.font.Font;

public class STGVAdapter extends BaseAdapter {
	
    private Context mContext;
    private ArrayList<PortfolioItem> mItems = new ArrayList<PortfolioItem> ();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private ImageClickListener mListener;
    
    
    public interface ImageClickListener {
    	public void setOnImageClickListener(View v, PortfolioItem data);
    }
    
    public void setOnImageClickListener(ImageClickListener listener) {
    	mListener = listener;
    }

    public STGVAdapter(Context context, ArrayList<PortfolioItem> items) {
        mContext = context;
    	mItems = items;
        imageLoaderInit(); 
    }

    public STGVAdapter(Context context) {
        mContext = context;
        imageLoaderInit(); 
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

		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    public void getMoreItem(ArrayList<PortfolioItem> item) {
    	for (PortfolioItem i : item) {
    		mItems.add(i);
    	}
    	notifyDataSetChanged();
    }

    public void setNewItem(ArrayList<PortfolioItem> item) {
        mItems = item;
        notifyDataSetChanged();
    }
    
    public void getNewItem(PortfolioItem item) {
        mItems.add(0, item);
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
    public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		final PortfolioItem item = mItems.get(position);
		String url = item.thumbImgUri;

		if (convertView == null) {
			Holder holder = new Holder();
			view = View.inflate(mContext, R.layout.cell_stgv, null);
			// view.setBackgroundColor(R.color.BACKGROUND);
			holder.img_content = (STGVImageView) view
					.findViewById(R.id.img_content);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_designer = (TextView) view.findViewById(R.id.tv_designer);

			view.setTag(holder);
		} else {
			view = convertView;
		}

		Holder holder = (Holder) view.getTag();

		/**
		 * StaggeredGridView has bugs dealing with child TouchEvent You must
		 * deal TouchEvent in the child view itself
		 **/
		holder.img_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.setOnImageClickListener(v, item);
				}
			}
		});

		holder.tv_title.setText(item.pofolTitle);
		holder.tv_title.setTypeface(Typeface.createFromAsset(ApplicationContext.getContext().getAssets(), Font.ROBOTO_MEDIUM));
		holder.tv_designer.setText(item.userName);
		holder.tv_designer.setTypeface(Typeface.createFromAsset(ApplicationContext.getContext().getAssets(), Font.ROBOTO_MEDIUM));
		imageLoader.displayImage(url, holder.img_content, options);

        return view;
    }

    class Holder {
        STGVImageView img_content;
        TextView tv_title;
        TextView tv_designer;
    }
}
