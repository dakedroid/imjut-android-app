package com.imjut.android.HomeActivities.Galeria;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imjut.android.Modelos.Foto;
import com.imjut.android.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {

    private RecyclerView mGaleriaRV;
    private static Context context;
    CollapsingToolbarLayout mCollapsingToolbar;

    public static Context getAppContext() {
        return GaleriaActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        GaleriaActivity.context = getApplicationContext();
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle("Galeria");

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);

        mGaleriaRV = findViewById(R.id.galeriaRV);
        mGaleriaRV.setLayoutManager(mGridLayoutManager);
        setupAdapter();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout contenedorFoto;
        ImageView foto;
        ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto_galeria);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            contenedorFoto = itemView.findViewById(R.id.contenedorFoto);
        }
    }

    private void setupAdapter(){
        FirebaseRecyclerAdapter<Foto, GaleriaActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Foto, GaleriaActivity.ViewHolder>(
                Foto.class,
                R.layout.modelo_galeria_rv,
                GaleriaActivity.ViewHolder.class,
                FirebaseDatabase.getInstance().getReference("posts").child("galeria")
        ) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final Foto model,final int position) {
                viewHolder.contenedorFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Query mQuery = FirebaseDatabase.getInstance().getReference("posts").child("galeria");
                        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> images = new ArrayList<>();
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    images.add(dataSnapshot1.child("imageUrl").getValue(String.class));
                                }
                                Intent mIntent = new Intent(GaleriaActivity.this, GaleriaDetallesActivity.class);
                                if(!images.isEmpty()){
                                    mIntent.putStringArrayListExtra("ArrayImagenes", images);
                                    mIntent.putExtra("posicion", position);
                                    mIntent.putExtra("primero", true);
                                    Log.d("ArrayEnviado", images.get(0));
                                }
                                GaleriaActivity.this.startActivity(mIntent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                if(model.getImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                    Glide.with(GaleriaActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(load)
                            .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    viewHolder.mProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.mProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.foto);
                    viewHolder.foto.setVisibility(View.VISIBLE);
                }
            }
        };
        mGaleriaRV.setAdapter(firebaseRecyclerAdapter);
        mGaleriaRV.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.right_out);
    }
}
