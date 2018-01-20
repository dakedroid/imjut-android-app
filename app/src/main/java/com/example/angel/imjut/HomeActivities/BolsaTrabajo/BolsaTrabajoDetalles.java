package com.example.angel.imjut.HomeActivities.BolsaTrabajo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angel.imjut.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BolsaTrabajoDetalles extends AppCompatActivity {

    ImageView mBarImage;
    CollapsingToolbarLayout mCollapsingToolbar;

    private TextView mMarcaBolsa;
    private TextView mTipoPuesto;
    private TextView mSueldo;
    private TextView mDescripcion;
    private TextView mRequisitos;
    private TextView mEducacion;
    private TextView mHabilidades;
    private TextView mExperiencia;
    private Button mBtnPostular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolsa_trabajo_detalles);

        mMarcaBolsa = findViewById(R.id.marca_bolsa_f);
        mTipoPuesto = findViewById(R.id.tipo_puesto_f);
        mSueldo = findViewById(R.id.sueldo_empleo_f);
        mDescripcion = findViewById(R.id.descripcion_gral_empleo_f);
        mRequisitos = findViewById(R.id.requisitos_gral_empleo_f);
        mEducacion = findViewById(R.id.educacion_empleo_f);
        mHabilidades = findViewById(R.id.habilidades_empleo_f);
        mExperiencia = findViewById(R.id.experiencia_empleo_f);

        mBtnPostular = findViewById(R.id.postularse);
        mBtnPostular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BolsaTrabajoDetalles.this, BolsaTrabajoPostularse.class));
            }
        });

        mBarImage = findViewById(R.id.image_bolsa_bar);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle("Requisitos");

        String url = getIntent().getStringExtra("PATH");
        StorageReference load = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso
                        .with(BolsaTrabajoDetalles.this.getApplicationContext())
                        .load(uri.toString())
                        .into(mBarImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        Intent mIntentDetalles = getIntent();
        Bundle mBundle = mIntentDetalles.getExtras();
        if(mBundle != null){
            Log.d("DATOS_PASADOS", "Datos pasados correctamente");
            String marca = mIntentDetalles.getStringExtra("MARCA");
            mMarcaBolsa.setText(marca);
            String tipo = mIntentDetalles.getStringExtra("TIPO_PUESTO");
            mTipoPuesto.setText(tipo);
            String descripcion = mIntentDetalles.getStringExtra("DESCRIPCION");
            mDescripcion.setText(descripcion);
            String sueldo = mIntentDetalles.getStringExtra("SUELDO");
            mSueldo.setText(sueldo);
            String requisitos = mIntentDetalles.getStringExtra("REQUISITOS");
            mRequisitos.setText(requisitos);
            String educacion = mIntentDetalles.getStringExtra("EDUCACION");
            mEducacion.setText(educacion);
            String habilidades = mIntentDetalles.getStringExtra("HABILIDADES");
            mHabilidades.setText(habilidades);
            String experiencia = mIntentDetalles.getStringExtra("EXPERIENCIA");
            mExperiencia.setText(experiencia);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(0,R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
