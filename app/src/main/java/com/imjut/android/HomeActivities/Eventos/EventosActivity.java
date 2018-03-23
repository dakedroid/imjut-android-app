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
import android.support.v7.widget.CardView;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.FirebaseMessaging;
import com.imjut.android.HomeActivities.DetallesImagenActivity;
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

import java.util.Calendar;

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
        //TextView tituloEvento;
        RelativeLayout layout_titulo;
        RelativeLayout layout_asistir;
        CardView descriptionCardView;
        TextView tv_fecha;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_fecha = itemView.findViewById(R.id.fecha);
            layout_titulo = itemView.findViewById(R.id.layout_titulo);
            iv_evento = itemView.findViewById(R.id.imagenEvento);
            tv_descripcion = itemView.findViewById(R.id.descripcionEvento);
            layout_asistir = itemView.findViewById(R.id.layout_asistir);
            layout_asistir.setBackgroundResource(R.drawable.layout_circle);
            descriptionCardView = itemView.findViewById(R.id.cardViewEventos);
            progressBar = itemView.findViewById(R.id.progress_bar);
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

                String mFecha = "";

                Calendar mTimeEnd = Calendar.getInstance();
                mTimeEnd.setTimeInMillis(model.getTimeEnd());

                int año = mTimeEnd.get(Calendar.YEAR);
                int mes = mTimeEnd.get(Calendar.MONTH);
                int dia = mTimeEnd.get(Calendar.DAY_OF_MONTH);
                int hora = mTimeEnd.get(Calendar.HOUR_OF_DAY);
                int minutos = mTimeEnd.get(Calendar.MINUTE);

                String añoS = String.valueOf(año);
                String mesS = String.valueOf(mes+1);
                String diaS = String.valueOf(dia);
                String horaS = String.valueOf(hora);
                String minutosS = String.valueOf(minutos);


                if((mes+1) < 10){
                    mesS = "0"+(mes+1);
                }
                if(dia < 10){
                    diaS = "0"+dia;
                }

                if(hora < 10){
                    horaS = "0"+hora;
                }
                if(minutos < 10){
                    minutosS = "0"+minutos;
                }



                Log.i("Fecha", String.valueOf(año) + String.valueOf(mes) + String.valueOf(dia) +
                String.valueOf(hora) + String.valueOf(minutos));

                mFecha = diaS + "/" + mesS + "/" + añoS + " " + horaS + ":" + minutosS;

                viewHolder.tv_fecha.setText(mFecha);

                viewHolder.layout_titulo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(EventosActivity.this, DetallesImagenActivity.class);
                        mIntent.putExtra("postImageUrl", model.getPostImageUrl());
                        EventosActivity.this.startActivity(mIntent);
                    }
                });
                //viewHolder.tituloEvento.setText(model.getTitulo());
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
