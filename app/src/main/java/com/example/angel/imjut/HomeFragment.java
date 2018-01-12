package com.example.angel.imjut;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angel.imjut.HomeActivities.BolsaTrabajoActivity;
import com.example.angel.imjut.HomeActivities.EventosActivity;
import com.example.angel.imjut.HomeActivities.GaleriaActivity;
import com.example.angel.imjut.HomeActivities.ProgramasActivity;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

public class HomeFragment extends Fragment{

    private RecyclerView mInicioRV;
    private Animator spruceAnimator;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mInicioRV = (RecyclerView) rootView.findViewById(R.id.inicio_rv);
        mInicioRV.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext()){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                spruceAnimator = new Spruce.SpruceBuilder(mInicioRV)
                        .sortWith(new DefaultSort(100))
                        .animateWith(DefaultAnimations.shrinkAnimator(mInicioRV, 800),
                                ObjectAnimator.ofFloat(mInicioRV, "translationX", -mInicioRV.getWidth(), 0f).setDuration(800))
                        .start();
            }
        };
        mInicioRV.setLayoutManager(mLinearLayoutManager);
        ContentAdapter adapter = new ContentAdapter(mInicioRV.getContext(), getActivity());
        mInicioRV.setAdapter(adapter);
        return rootView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_titulo;
        public LinearLayout layout;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.modelo_cardview_inicio, parent, false));
            tv_titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            layout = (LinearLayout) itemView.findViewById(R.id.cardView);
        }

    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;
        private final String[] mPlaces;
        private Resources resources;
        private DisplayMetrics metrics;
        private Context mContext;
        private Activity mActivity;

        public ContentAdapter(Context context, Activity activity) {
            resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.titulos_carta);
            metrics = new DisplayMetrics();
            mContext = context;
            mActivity = activity;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
            holder.tv_titulo.setText(mPlaces[position % mPlaces.length]);

            switch (position){
                case 0:
                    holder.layout.setBackgroundColor(resources.getColor(R.color.color));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mActivity, ProgramasActivity.class));
                            mActivity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                    break;
                case 1:
                    holder.layout.setBackgroundColor(resources.getColor(R.color.color2));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mActivity, EventosActivity.class));
                            mActivity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                    break;
                case 2:
                    holder.layout.setBackgroundColor(resources.getColor(R.color.color3));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mActivity, BolsaTrabajoActivity.class));
                            mActivity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                    break;
                case 3:
                    holder.layout.setBackgroundColor(resources.getColor(R.color.color4));
                    holder.layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mActivity, GaleriaActivity.class));
                            mActivity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                    break;
            }
        }
        @Override
        public int getItemCount() {return LENGTH;}
    }
}
