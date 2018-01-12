package com.example.angel.imjut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AsistirActivity extends AppCompatActivity {


    private Button btn_registrarse;
    private TextView tv_volver;

    @BindView(R.id.asistir_nombre) EditText et_nombre;
    @BindView(R.id.asistir_apellidos) EditText et_apellido;
    @BindView(R.id.asistir_ciudad) EditText et_ciudad;
    @BindView(R.id.asistir_comunidad) EditText et_comunidad;
    @BindView(R.id.asistir_correo) EditText et_correo;
    @BindView(R.id.asistir_facebook) EditText et_facebook;
    @BindView(R.id.asistir_numero) EditText et_numero;
    @BindView(R.id.asistir_edad) EditText et_edad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistir);
        ButterKnife.bind(this);

        btn_registrarse = (Button) findViewById(R.id.registrarse_asistir);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarForm()){
                    registrarse();
                }
            }
        });
        tv_volver = (TextView) findViewById(R.id.volver);
        tv_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void registrarse(){
        //Registro Programa

    }

    public boolean validarForm(){
        boolean valido = true;

        String nombre = et_nombre.getText().toString();
        String apellidos = et_apellido.getText().toString();
        String correo = et_correo.getText().toString();
        String comunidad = et_comunidad.getText().toString();
        String ciudad = et_ciudad.getText().toString();
        String numero = et_numero.getText().toString();
        String facebook = et_facebook.getText().toString();
        String edad = et_edad.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            valido = false;
            et_nombre.setError("Introduzca un nombre");
        }else{
            et_nombre.setError(null);
        }

        if(TextUtils.isEmpty(apellidos)){
            valido = false;
            et_apellido.setError("Introduzca un apellido");
        }else{
            et_apellido.setError(null);
        }

        if(TextUtils.isEmpty(edad)){
            valido = false;
            et_edad.setError("Introduzca su edad");
        }else{
            et_edad.setError(null);
        }

        if(TextUtils.isEmpty(correo)){
            valido = false;
            et_correo.setError("Introduzca un correo");
        }else{
            et_correo.setError(null);
        }

        if(TextUtils.isEmpty(comunidad)){
            valido = false;
            et_comunidad.setError("Introduzca una comunidad");
        }else{
            et_comunidad.setError(null);
        }

        if(TextUtils.isEmpty(ciudad)){
            valido = false;
            et_ciudad.setError("Introduzca una ciudad");
        }else{
            et_ciudad.setError(null);
        }

        if(TextUtils.isEmpty(numero)){
            valido = false;
            et_numero.setError("Introduzca un numero telefonico");
        }else{
            et_numero.setError(null);
        }

        if(TextUtils.isEmpty(facebook)){
            valido = false;
            et_facebook.setError("Introduzca un facebook");
        }else{
            et_facebook.setError(null);
        }
        return valido;
    }

}
