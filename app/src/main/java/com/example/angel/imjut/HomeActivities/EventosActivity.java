package com.example.angel.imjut.HomeActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.angel.imjut.Modelos.Evento;
import com.example.angel.imjut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

        getSupportActionBar().setTitle("Eventos");

        mEventosRV = findViewById(R.id.eventosRV);
        //mProgramasRV.setHasFixedSize(true);
        mEventosRV.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView iv_evento;
        //TextView titulo;
        TextView tv_descripcion;
        TextView tituloEvento;
        RelativeLayout layout_asistir;
        //RelativeLayout layout_titulo;
        LinearLayout layout_descripcion;
        LinearLayout descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        ImageView info;
        int mode = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_evento = itemView.findViewById(R.id.imagenEvento);
            //titulo = itemView.findViewById(R.id.tv_titulo);
            tv_descripcion = itemView.findViewById(R.id.descripcionEvento);
            tituloEvento = itemView.findViewById(R.id.titulo_evento);
            layout_asistir = itemView.findViewById(R.id.layout_asistir);
            layout_asistir.setBackgroundResource(R.drawable.layout_circle);
            //layout_titulo = itemView.findViewById(R.id.layout_titulo);
            //layout_titulo.setBackgroundResource(R.drawable.layout_circle);
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

            // card_expand = 600
            // card_size_prueba = 750

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
                //ratingBar.setVisibility(View.VISIBLE);
                //title.setMaxLines(2);

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


                //ratingBar.setVisibility(View.INVISIBLE);
                //title.setMaxLines(1);

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
                viewHolder.iv_evento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(EventosActivity.this, FotoDetallesActivity.class);
                        mIntent.putExtra("postImageUrl", model.getPostImageUrl());
                        EventosActivity.this.startActivity(mIntent);
                    }
                });
                viewHolder.tituloEvento.setText(model.getTitulo());
                viewHolder.tv_descripcion.setText(model.getDescripcion());
                //viewHolder.titulo.setText(model.getTitulo());
                viewHolder.layout_asistir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
                        builder.setTitle("Aviso")
                                .setMessage("Se le notificara una hora antes del evento")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        builder.show();

                    }
                });

                if(model.getPostImageUrl() != null){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());
                    StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPostImageUrl());

                    load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            // Pass it to Picasso to download, show in ImageView and caching
                            Picasso
                                    .with(context)
                                    .load(uri.toString())
                                    .into(viewHolder.iv_evento, new Callback() {
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
