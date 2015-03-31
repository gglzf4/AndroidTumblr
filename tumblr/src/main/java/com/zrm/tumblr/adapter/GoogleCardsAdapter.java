package com.zrm.tumblr.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zrm.tumblr.R;
import com.zrm.tumblr.app.DeviceInfo;
import com.zrm.tumblr.app.Session;
import com.zrm.tumblr.model.Photo;
import com.zrm.tumblr.net.DataAcquire;
import com.zrm.tumblr.utils.BitmapUtils;
import com.zrm.tumblr.utils.Logger;
import com.zrm.tumblr.view.LoginActivity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrm on 2015/3/31 0031.
 */
public class GoogleCardsAdapter extends ArrayAdapter<Photo> {

    private static final String TAG = GoogleCardsAdapter.class.getSimpleName();

    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;

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
    public View getView(int position, View view, ViewGroup parent) {
        final Photo photo = getItem(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_listviews_googlecards_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.id_collect_checkbox);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Session.isLogin()){
                        String accessToken = Session.getAccessToken();
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(DataAcquire.ACCESS_TOKEN,accessToken);
                        map.put(DataAcquire.PID,String.valueOf(photo.getId()));
                        DataAcquire.collectPhoto(map, new JsonHttpResponseHandler(){

                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Logger.i(TAG,response.toString());
                                int status = response.optInt(DataAcquire.STATUS);
                                if(status == 200){
                                    Toast.makeText(mContext,"OK",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mContext,response.optString(DataAcquire.MSG),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Toast.makeText(mContext,"error",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRetry(int retryNo) {
                                super.onRetry(retryNo);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                            }

                        });
                    }else{
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setImageView(viewHolder, photo);
        return view;
    }
    private void setImageView(ViewHolder viewHolder, Photo photo) {

        if(photo != null){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        Bitmap bitmap = getBitmapFromMemCache(photo.getUrl());
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage(photo.getUrl(), viewHolder.imageView, new AnimateFirstDisplayListener());
        }else{
            viewHolder.imageView.setMinimumWidth(bitmap.getWidth());
            viewHolder.imageView.setMinimumHeight(bitmap.getHeight());
            viewHolder.imageView.setImageBitmap(bitmap);
        }
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

                /*Logger.i(TAG,"Dev--->"+DeviceInfo.getPhoneWidth()+","+DeviceInfo.getPhoneHeight());
                Logger.i(TAG,"Img--->"+loadedImage.getWidth()+","+loadedImage.getHeight());*/

                Bitmap bitmap = BitmapUtils.scaleWithWidth(loadedImage,DeviceInfo.getPhoneWidth());

                /*Logger.i(TAG,"Bit--->"+bitmap.getWidth()+","+bitmap.getHeight());
                Logger.i(TAG,"imageUri-->"+imageUri);*/

                ImageView imageview = (ImageView) view;

                imageview.setLayoutParams(new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                imageview.setMinimumWidth(bitmap.getWidth());
                imageview.setMinimumHeight(bitmap.getHeight());
                imageview.setImageBitmap(bitmap);
                addBitmapToMemoryCache(imageUri, bitmap);

                /*int photoWidth = DeviceInfo.getPhoneWidth();
                int photoHeight = loadedImage.getHeight()*photoWidth/loadedImage.getWidth();
                Logger.i(TAG,"Cov--->"+photoWidth+","+photoHeight);
                view.setLayoutParams(new RelativeLayout.LayoutParams(photoWidth, photoHeight));*/

                /*imageView.setMinimumWidth(phoneWidth);
                imageView.setMinimumHeight(photoHeight);*/
                //addBitmapToMemoryCache(imageUri, HotelUtils.getRoundedCornerBitmap(Bitmap.createScaledBitmap(loadedImage,phoneWidth,photoHeight,true), corner));

            }
        }
    }
}
