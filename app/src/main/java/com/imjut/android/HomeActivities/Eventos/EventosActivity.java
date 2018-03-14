package com.imjut.android.HomeActivities.Eventos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.imjut.android.Modelos.Evento;
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

public class EventosActivity extends AppCompatActivity {

    private RecyclerView mEventosRV;
    private static Context context;

    public static Context getAppContext() {
        return EventosActivity.context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        EventosActivity.context = getApplicationContext();

        getSupportActionBar().setTitle(R.string.eventos_title);

        mEventosRV = findViewById(R.id.eventosRV);
        mEventosRV.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView iv_evento;
        TextView tv_descripcion;
        TextView tituloEvento;
        RelativeLayout layout_titulo;
        RelativeLayout layout_asistir;
        LinearLayout layout_descripcion;
        LinearLayout descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        ImageView info;
        int mode = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            layout_titulo = itemView.findViewById(R.id.layout_titulo);
            iv_evento = itemView.findViewById(R.id.imagenEvento);
            tv_descripcion = itemView.findViewById(R.id.descripcionEvento);
            tituloEvento = itemView.findViewById(R.id.titulo_evento);
            layout_asistir = itemView.findViewById(R.id.layout_asistir);
            layout_asistir.setBackgroundResource(R.drawable.layout_circle);
            descriptionCardView = itemView.findViewById(R.id.cardViewEventos);
            progressBar = itemView.findViewById(R.id.progress_bar);

            int height = (int) getAppContext().getResources().getDimension(R.dimen.card_size_prueba);
            ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
            layoutParams.height = height;
            descriptionCardView.setLayoutParams(layoutParams);

            layout_descripcion = itemView.findViewById(R.id.layout_descripcion);
            layout_descripcion.setBackgroundResource(R.drawable.layout_circle_objetivo);


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
                layout_descripcion.setVisibility(View.VISIBLE);

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
                        layout_descripcion.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    private void setupAdapter(){
        FirebaseRecyclerAdapter<Evento, EventosActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Evento, EventosActivity.ViewHolder>(
                Evento.class,
                R.layout.modelo_eventos_rv,
                EventosActivity.ViewHolder.class,
                FirebaseDatabase.getInstance().getReference("posts").child("eventos")
        ) {
            @Override
            protected void populateViewHolder(final EventosActivity.ViewHolder viewHolder, final Evento model, int position) {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                final String mCurrentEmail = mUser.getEmail().replace(".",",");
                DatabaseReference mUserRecordEventos = FirebaseDatabase.getInstance().getReference()
                        .child("user_record")
                        .child(mCurrentEmail)
                        .child("eventos_asistir")
                        .child(model.getPostId());
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

                viewHolder.layout_titulo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(EventosActivity.this, DetallesEventoActivity.class);
                        mIntent.putExtra("postImageUrl", model.getPostImageUrl());
                        EventosActivity.this.startActivity(mIntent);
                    }
                });
                viewHolder.tituloEvento.setText(model.getTitulo());
                viewHolder.tv_descripcion.setText(model.getDescripcion());
                viewHolder.layout_asistir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
                        builder.setTitle("Aviso")
                                .setIcon(R.mipmap.ic_notification)
                                .setMessage("Se le notificara una hora antes del evento")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FirebaseMessaging.getInstance().subscribeToTopic(model.getPostId());
                                        DatabaseReference mUserRecord = FirebaseDatabase.getInstance().getReference()
                                                .child("user_record")
                                                .child(mCurrentEmail)
                                                .child("eventos_asistir")
                                                .child(model.getPostId());
                                        mUserRecord.setValue("true");

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();

                    }
                });

                if(model.getPostImageUrl() != null){
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());
                    Glide.with(EventosActivity.this)
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
                            .into(viewHolder.iv_evento);
                    viewHolder.iv_evento.setVisibility(View.VISIBLE);
                }
            }
        };
        mEventosRV.setAdapter(firebaseRecyclerAdapter);
        mEventosRV.setNestedScrollingEnabled(false);
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
