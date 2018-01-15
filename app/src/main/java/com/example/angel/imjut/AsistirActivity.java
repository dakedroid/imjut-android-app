package com.example.angel.imjut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.angel.imjut.Modelos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AsistirActivity extends AppCompatActivity {
    private Query mUser;

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

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null){
            llenarForm();
        }

        btn_registrarse = findViewById(R.id.registrarse_asistir);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarForm()){
                    registrarse();
                }
            }
        });
        tv_volver = findViewById(R.id.volver);
        tv_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void llenarForm(){
        String mCurrentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",");
        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentEmail);
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mCurrentUser = dataSnapshot.getValue(User.class);
                et_nombre.setText(mCurrentUser.getName());
                et_apellido.setText(mCurrentUser.getApellido());
                et_edad.setText(mCurrentUser.getEdad());
                et_correo.setText(mCurrentUser.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
