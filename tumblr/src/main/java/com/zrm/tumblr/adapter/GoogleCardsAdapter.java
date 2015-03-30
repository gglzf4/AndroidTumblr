package com.zrm.tumblr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zrm.tumblr.R;
import com.zrm.tumblr.app.DeviceInfo;
import com.zrm.tumblr.model.Photo;

import java.util.List;

/**
 * Created by zhangrm on 2015/3/31 0031.
 */
public class GoogleCardsAdapter extends ArrayAdapter<Photo> {
    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private SimpleImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public GoogleCardsAdapter(Context context) {
        mContext = context;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_listviews_googlecards_card, parent, false);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.id_collect_checkbox);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setImageView(viewHolder, position);
        Photo photo = getItem(position);

        if(photo != null){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        return view;
    }
    private void setImageView(ViewHolder viewHolder, int position) {
        Photo photo = getItem(position);
        Bitmap bitmap = getBitmapFromMemCache(photo.getUrl());
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage(photo.getUrl(), viewHolder.imageView, animateFirstListener);
        }
        viewHolder.imageView.setImageBitmap(bitmap);
    }
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    private static class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }

    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null && view != null) {
                int phoneWidth = DeviceInfo.getPhoneWidth();
                int photoHeight = loadedImage.getHeight()*phoneWidth/loadedImage.getWidth();
                //int photoHeight = 380 * phoneWidth / 640;
                ImageView imageView = (ImageView) view;
                imageView.setMinimumWidth(phoneWidth);
                imageView.setMinimumHeight(photoHeight);
                //addBitmapToMemoryCache(imageUri, HotelUtils.getRoundedCornerBitmap(Bitmap.createScaledBitmap(loadedImage,phoneWidth,photoHeight,true), corner));
                addBitmapToMemoryCache(imageUri, loadedImage);
            }
        }
    }
}
