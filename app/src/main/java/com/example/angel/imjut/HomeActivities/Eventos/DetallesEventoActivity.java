package com.example.angel.imjut.HomeActivities.Eventos;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.angel.imjut.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class DetallesEventoActivity extends AppCompatActivity {

    private ImageViewTouch mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        mImageView = findViewById(R.id.foto_evento_zoom);
        mProgressBar = findViewById(R.id.progress_bar);

        String postImageUrl = getIntent().getStringExtra("postImageUrl");

        StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(postImageUrl);

        load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso
                        .with(DetallesEventoActivity.this)
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .into(mImageView, new Callback() {
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
}
