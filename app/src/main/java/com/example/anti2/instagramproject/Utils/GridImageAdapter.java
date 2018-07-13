package com.example.anti2.instagramproject.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anti2.instagramproject.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private List<String> imgURLs;

    public GridImageAdapter(Context context, int layoutResource, String append, List<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.mAppend = append;
        this.imgURLs = imgURLs;
    }

    private static class ViewHolder {
        SquareImageView image;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
        viewholder build pattern(Similar to recyclerview)
         */
        final ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressBar);
            holder.image = (SquareImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imgURL, holder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(holder.mProgressBar != null) {
                    holder.mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(holder.mProgressBar != null) {
                    holder.mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(holder.mProgressBar != null) {
                    holder.mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(holder.mProgressBar != null) {
                    holder.mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        return convertView;
    }
}
