package com.example.angel.imjut.HomeActivities;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.angel.imjut.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by Angel on 13/01/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    //images;
    ArrayList<String> images = new ArrayList<>();
    LayoutInflater inflater;
    int posicion;

    public ViewPagerAdapter(Activity activity, ArrayList<String> images, int posicion) {
        this.activity = activity;
        this.images = images;
        this.posicion = posicion;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_viewpager,container,false);

        final ImageViewTouch image;
        image = itemView.findViewById(R.id.foto_galeria_zoom);
        final ProgressBar mProgressBar;
        mProgressBar = itemView.findViewById(R.id.progress);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        try{
            StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(images.get(position));

            load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching
                    Picasso
                            .with(activity.getApplicationContext())
                            .load(uri.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    Log.d("ImageNotFound", "Fallo al cargar la imagen");
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        catch (Exception ex){

        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}

