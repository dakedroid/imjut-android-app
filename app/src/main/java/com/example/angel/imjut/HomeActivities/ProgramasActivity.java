package com.example.angel.imjut.HomeActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angel.imjut.AsistirActivity;
import com.example.angel.imjut.Modelos.Programa;
import com.example.angel.imjut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ProgramasActivity extends AppCompatActivity {

    private RecyclerView mProgramasRV;
    private static Context context;

    public static Context getAppContext() {
        return ProgramasActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);
        ProgramasActivity.context = getApplicationContext();

        getSupportActionBar().setTitle("Programas");

        mProgramasRV = findViewById(R.id.programasRV);
        mProgramasRV.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView iv_programa;
        TextView tv_objetivos;
        TextView tituloObjetivo;
        RelativeLayout layout_asistir;
        LinearLayout layout_objetivos;
        LinearLayout descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        ImageView info;
        int mode = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_programa = itemView.findViewById(R.id.imagenPrograma);
            tv_objetivos = itemView.findViewById(R.id.objetivos);
            tituloObjetivo = itemView.findViewById(R.id.Titulo_objetivos);
            layout_asistir = itemView.findViewById(R.id.layout_asistir);
            layout_asistir.setBackgroundResource(R.drawable.layout_circle);
            descriptionCardView = itemView.findViewById(R.id.cardViewProgramas);
            progressBar = itemView.findViewById(R.id.progress_bar);

            int height = (int) getAppContext().getResources().getDimension(R.dimen.card_size_prueba);
            ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
            layoutParams.height = height;
            descriptionCardView.setLayoutParams(layoutParams);

            layout_objetivos = itemView.findViewById(R.id.layout_objetivos);
            layout_objetivos.setBackgroundResource(R.drawable.layout_circle_objetivo);

            info = itemView.findViewById(R.id.info);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleProductDescriptionHeight();
                }
            });
        }

        private void toggleProductDescriptionHeight() {
            descriptionViewFullHeight = descriptionCardView.getHeight() + (int) getAppContext().getResources().getDimension(R.dimen.card_expand_places);
            descriptionViewMinHeight = descriptionCardView.getHeight();

            if (descriptionCardView.getHeight() == descriptionViewMinHeight && mode == 0) {
                // expand
                ValueAnimator anim = ValueAnimator.ofInt(descriptionCardView.getMeasuredHeightAndState(), descriptionViewFullHeight);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
                        layoutParams.height = val;
                        descriptionCardView.setLayoutParams(layoutParams);

                        mode = 1;
                    }
                });
                anim.start();
                layout_objetivos.setVisibility(View.VISIBLE);
            } else {
                // collapse
                descriptionViewMinHeight = (int) getAppContext().getResources().getDimension(R.dimen.card_size_prueba);
                ValueAnimator anim = ValueAnimator.ofInt(descriptionCardView.getMeasuredHeightAndState(),
                        descriptionViewMinHeight);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
                        layoutParams.height = val;
                        descriptionCardView.setLayoutParams(layoutParams);

                        mode = 0;
                    }
                });
                anim.start();
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout_objetivos.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private void setupAdapter(){
        FirebaseRecyclerAdapter<Programa, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Programa, ProgramasActivity.ViewHolder>(
                Programa.class,
                R.layout.modelo_programas_rv,
                ProgramasActivity.ViewHolder.class,
                FirebaseDatabase.getInstance().getReference("posts").child("programas")
        ) {
            @Override
            protected void populateViewHolder(final ViewHolder viewHolder, final Programa model, int position) {
                viewHolder.tv_objetivos.setText(model.getObjetivos());
                viewHolder.tituloObjetivo.setText(model.getTitulo());
                viewHolder.layout_asistir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProgramasActivity.this.startActivity(new Intent(ProgramasActivity.this, AsistirActivity.class));
                    }
                });

                if(model.getPostImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());
                    load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso
                                    .with(context)
                                    .load(uri.toString())
                                    .into(viewHolder.iv_programa, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            viewHolder.progressBar.setVisibility(View.GONE);
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
        mProgramasRV.setAdapter(firebaseRecyclerAdapter);
        mProgramasRV.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(0,R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(0,R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

