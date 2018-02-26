package com.imjut.android.HomeActivities.Programas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.imjut.android.Modelos.Programa;
import com.imjut.android.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


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

        getSupportActionBar().setTitle(R.string.programas_title);

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
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                final String mCurrentEmail = mUser.getEmail().replace(".",",");
                DatabaseReference mUserRecordEventos = FirebaseDatabase.getInstance().getReference()
                        .child("user_record")
                        .child(mCurrentEmail)
                        .child("programas_asistir")
                        .child(model.getUid());
                mUserRecordEventos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            Log.e("TAG", "" + dataSnapshot.getValue());
                            String asistir = dataSnapshot.getValue(String.class);
                            if(TextUtils.equals(asistir, "true")){
                                viewHolder.layout_asistir.setBackgroundResource(R.drawable.layout_circle_marcado);
                            }else{
                                viewHolder.layout_asistir.setBackgroundResource(R.drawable.layout_circle);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                viewHolder.tv_objetivos.setText(model.getObjetivos());
                viewHolder.tituloObjetivo.setText(model.getTitulo());
                viewHolder.layout_asistir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProgramasActivity.this.startActivity(new Intent(ProgramasActivity.this, AsistirActivity.class).putExtra("POSTID", model.getUid()));
                    }
                });

                if(model.getPostImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());
                    Glide.with(ProgramasActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(load)
                            .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    viewHolder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.iv_programa);
                    viewHolder.iv_programa.setVisibility(View.VISIBLE);
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

