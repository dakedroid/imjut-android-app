package com.example.angel.imjut.HomeActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.angel.imjut.AsistirActivity;
import com.example.angel.imjut.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class ProgramasActivity extends AppCompatActivity {

    //private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView programasRV;
    private static Context context;

    public static Context getAppContext() {
        return ProgramasActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);
        ProgramasActivity.context = getApplicationContext();

        programasRV = (RecyclerView) findViewById(R.id.programasRV);
        programasRV.setHasFixedSize(true);
        programasRV.setLayoutManager(new LinearLayoutManager(this));
        ContentAdapter adapter = new ContentAdapter(programasRV.getContext(), this);
        programasRV.setAdapter(adapter);
        programasRV.setNestedScrollingEnabled(false);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //TextView titulo;
        TextView tv_objetivos;
        TextView tituloObjetivo;
        RelativeLayout layout_asistir;
        RelativeLayout layout_titulo;
        LinearLayout layout_objetivos;
        LinearLayout descriptionCardView;
        private int descriptionViewFullHeight;
        private int descriptionViewMinHeight;
        ImageView info;
        int mode = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            //titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            tv_objetivos = (TextView) itemView.findViewById(R.id.objetivos);
            tituloObjetivo = (TextView) itemView.findViewById(R.id.Titulo_objetivos);
            layout_asistir = (RelativeLayout) itemView.findViewById(R.id.layout_asistir);
            layout_asistir.setBackgroundResource(R.drawable.layout_circle);
            layout_titulo = (RelativeLayout) itemView.findViewById(R.id.layout_titulo);
            layout_titulo.setBackgroundResource(R.drawable.layout_circle);
            descriptionCardView = (LinearLayout) itemView.findViewById(R.id.cardViewProgramas);

            int height = (int) getAppContext().getResources().getDimension(R.dimen.card_size_prueba);
            ViewGroup.LayoutParams layoutParams = descriptionCardView.getLayoutParams();
            layoutParams.height = height;
            descriptionCardView.setLayoutParams(layoutParams);

            layout_objetivos = (LinearLayout) itemView.findViewById(R.id.layout_objetivos);
            layout_objetivos.setBackgroundResource(R.drawable.layout_circle_objetivo);


            info = (ImageView) itemView.findViewById(R.id.info);
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
                layout_objetivos.setVisibility(View.VISIBLE);
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
                        layout_objetivos.setVisibility(View.INVISIBLE);
                    }
                });


                //ratingBar.setVisibility(View.INVISIBLE);
                //title.setMaxLines(1);

            }
        }


    }

    private void setupAdapter(){
        /*
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",");
        if(userEmail != null){

        }
        */



    }


    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final int LENGTH = 6;
        //private final String[] mPlaces;
        private Resources resources;
        private Context mContext;
        private Activity mActivity;


        public ContentAdapter(Context context, Activity activity) {
            resources = context.getResources();
            //mPlaces = resources.getStringArray(R.array.programas);
            mContext = context;
            mActivity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.modelo_programas_rv, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //holder.titulo.setText(mPlaces[position % mPlaces.length]);
            holder.tv_objetivos.setText(R.string.objetivo);
            holder.tituloObjetivo.setText(R.string.titulo);
            holder.layout_asistir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mActivity, AsistirActivity.class));
                }
            });

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
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
}

