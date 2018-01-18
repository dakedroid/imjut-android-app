package com.example.angel.imjut.SubirContenido;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.imjut.HomeFragment;
import com.example.angel.imjut.R;

public class PanelSubirContenido extends AppCompatActivity {

    RecyclerView mSubirRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_subir_contenido);
        getSupportActionBar().setTitle("Subir Contenido");
        ContentAdapter mContentAdapter = new ContentAdapter(getApplicationContext(),PanelSubirContenido.this);

        mSubirRV = findViewById(R.id.lista_subir_rv);
        mSubirRV.setLayoutManager(new LinearLayoutManager(this));
        mSubirRV.setHasFixedSize(true);
        mSubirRV.setAdapter(mContentAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mIconos;
        TextView mTitulos;

        public ViewHolder(View itemView) {
            super(itemView);
            mIconos = itemView.findViewById(R.id.icono_subir);
            mTitulos = itemView.findViewById(R.id.tv_subir);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        private static final int LENGTH = 4;
        private String[] mTitulos;
        private Drawable[] mIconos;
        private Resources resources;
        private Context mContext;
        private Activity mActivity;

        public ContentAdapter(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
            resources = context.getResources();
            mTitulos = resources.getStringArray(R.array.titulos_carta);
            TypedArray a = resources.obtainTypedArray(R.array.iconos);
            mIconos = new Drawable[a.length()];
            for (int i = 0; i < mIconos.length; i++) {
                mIconos[i] = a.getDrawable(i);
            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_panel_subir, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTitulos.setText(mTitulos[position % mTitulos.length]);
            holder.mIconos.setImageDrawable(mIconos[position % mIconos.length]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position){
                        case 0:
                            Toast.makeText(mContext, "Prueba", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(mContext, "Prueba", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(mContext, "Prueba", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(mContext, "Prueba", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
