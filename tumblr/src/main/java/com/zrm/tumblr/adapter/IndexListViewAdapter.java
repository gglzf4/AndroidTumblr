package com.zrm.tumblr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zrm.tumblr.R;
import com.zrm.tumblr.model.Photo;

import java.util.List;

/**
 * Created by zhangrm on 2015/3/16 0016.
 */
public class IndexListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photos;



    public IndexListViewAdapter(Context context,List<Photo> photos){
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if(convertView == null){
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_index,null);
            mViewHolder.roomNameTextView = (ImageView) convertView.findViewById(R.id.index_item_imageview);


            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        return  convertView;
    }

    class ViewHolder {

        public ImageView roomNameTextView;

    }
}
