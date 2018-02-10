package com.example.angel.imjut.HomeActivities.BolsaTrabajo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angel.imjut.Modelos.BolsaTrabajo;
import com.example.angel.imjut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BolsaTrabajoActivity extends AppCompatActivity {

    /*
    private RecyclerView mBolsaTrabajoRV;
    private static Context mContext;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolsa_trabajo);
        //BolsaTrabajoActivity.mContext = getApplicationContext();

        getSupportActionBar().setTitle(R.string.bolsa_title);

        /*
        mBolsaTrabajoRV = findViewById(R.id.bolsa_trabajo_rv);
        mBolsaTrabajoRV.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        */
    }

    /*

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ProgressBar mProgressBar;
        ImageView mImageBolsa;
        TextView mTextBolsa;
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mImageBolsa = itemView.findViewById(R.id.image_bolsa_trabajo);
            mTextBolsa = itemView.findViewById(R.id.tv_bolsa_titulo);
            mRelativeLayout = itemView.findViewById(R.id.layout_bolsa);
        }
    }

    private void setupAdapter(){
        FirebaseRecyclerAdapter<BolsaTrabajo, BolsaTrabajoActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BolsaTrabajo, BolsaTrabajoActivity.ViewHolder>(
                BolsaTrabajo.class,
                R.layout.modelo_bolsa_trabajo,
                BolsaTrabajoActivity.ViewHolder.class,
                FirebaseDatabase.getInstance().getReference("posts").child("bolsa")
        ) {
            @Override
            protected void populateViewHolder(final BolsaTrabajoActivity.ViewHolder viewHolder, final BolsaTrabajo model, int position) {
                viewHolder.mTextBolsa.setText(model.getMarca());
                viewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mDetallesIntent = new Intent(BolsaTrabajoActivity.this, BolsaTrabajoDetalles.class);
                        mDetallesIntent.putExtra("TITLE", model.getMarca());
                        mDetallesIntent.putExtra("PATH", model.getPostImageUrl());
                        mDetallesIntent.putExtra("MARCA", model.getMarca());
                        mDetallesIntent.putExtra("TIPO_PUESTO", model.getTipoPuesto());
                        mDetallesIntent.putExtra("DESCRIPCION",model.getDescripcionGeneral());
                        mDetallesIntent.putExtra("SUELDO", model.getSueldo());
                        mDetallesIntent.putExtra("REQUISITOS", model.getRequisitosGenerales());
                        mDetallesIntent.putExtra("EDUCACION", model.getEducacion());
                        mDetallesIntent.putExtra("HABILIDADES", model.getHabilidades());
                        mDetallesIntent.putExtra("EXPERIENCIA", model.getExperiencia());
                        BolsaTrabajoActivity.this.startActivity(mDetallesIntent);
                    }
                });

                if(model.getPostImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());
                    load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso
                                    .with(mContext)
                                    .load(uri.toString())
                                    .into(viewHolder.mImageBolsa, new Callback() {
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

                        }
                    });
                }
            }
        };
        mBolsaTrabajoRV.setAdapter(firebaseRecyclerAdapter);
        mBolsaTrabajoRV.setNestedScrollingEnabled(false);
    }
    */

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
