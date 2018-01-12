package com.example.angel.imjut.HomeActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.angel.imjut.Modelos.Foto;
import com.example.angel.imjut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class GaleriaActivity extends AppCompatActivity {

    private RecyclerView mGaleriaRV;
    private static Context context;
    private Random mRandom = new Random();

    public static Context getAppContext() {
        return GaleriaActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        GaleriaActivity.context = getApplicationContext();
        getSupportActionBar().setTitle("Galería");

        GridLayoutManager mGridLayout = new GridLayoutManager(this, 2);

        mGridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position % 3 == 0){
                    return 2;
                }else{
                    return 1;
                }
            }
        });

        mGaleriaRV = findViewById(R.id.galeriaRV);
        mGaleriaRV.setLayoutManager(mGridLayout);
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
            protected void populateViewHolder(final ViewHolder viewHolder, final Foto model, int position) {
                //viewHolder.contenedorFoto.getLayoutParams().height = getRandomIntInRange(230,130);
                viewHolder.contenedorFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(GaleriaActivity.this, FotoDetallesActivity.class);
                        mIntent.putExtra("postImageUrl", model.getImageUrl());
                        GaleriaActivity.this.startActivity(mIntent);
                    }
                });

                if(model.getImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());

                    load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            // Pass it to Picasso to download, show in ImageView and caching
                            Picasso
                                    .with(context)
                                    .load(uri.toString())
                                    .into(viewHolder.foto, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            viewHolder.mProgressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

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
            }
        };
        mGaleriaRV.setAdapter(firebaseRecyclerAdapter);
        mGaleriaRV.setNestedScrollingEnabled(false);
    }

    protected int getRandomIntInRange(int max, int min){
        return mRandom.nextInt((max-min)+min)+min;
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
