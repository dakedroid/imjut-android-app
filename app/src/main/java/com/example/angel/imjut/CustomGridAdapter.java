package com.example.angel.imjut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Angel on 17/02/2018.
 */

public class CustomGridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mTitles;
    private int[] mImages;
    private int mCount;

    public CustomGridAdapter(Context mContext, String[] mTitles, int[] mImages, int mCount){
        this.mContext = mContext;
        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mCount = mCount;
    }


    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mGridView;


        if(view == null){
            mGridView = new View(mContext);
            mGridView = inflater.inflate(R.layout.patrocinadores_item, null);

            TextView titles = mGridView.findViewById(R.id.titlePatrocinio);
            titles.setText(mTitles[i]);
            ImageView thumbImages = mGridView.findViewById(R.id.imagePatrocinio);
            thumbImages.setImageResource(mImages[i]);

        }else{
            mGridView = view;
        }
        return mGridView;
    }
}
