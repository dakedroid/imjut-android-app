package com.example.angel.imjut;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.angel.imjut.Modelos.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrarseActivity extends AppCompatActivity {

    @BindView(R.id.et_correo) EditText et_email;
    @BindView(R.id.et_contra) EditText et_pass;
    @BindView(R.id.et_confirmar_contra) EditText et_confirmarPass;
    @BindView(R.id.et_nombre) EditText et_nombre;
    @BindView(R.id.et_apellido) EditText et_apellido;
    @BindView(R.id.et_edad) EditText et_edad;
    private Button btnCrearCuenta;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUserRef;
    private String email;
    private String pass;
    private String nombre;
    private String apellidos;
    private String edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent homeIntent = new Intent(RegistrarseActivity.this, HomeActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeIntent);
                }
            }
        };

        btnCrearCuenta = findViewById(R.id.btn_crear_cuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarForm()){
                    Toast.makeText(RegistrarseActivity.this, "Registro valido", Toast.LENGTH_SHORT).show();
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                String uid = mUser.getUid();
                                saveUserToDatabase(nombre,email,uid,apellidos,edad, false);
                            }else{
                                Toast.makeText(RegistrarseActivity.this, R.string.error_registro, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private boolean validarForm(){
        boolean valido = true;
        apellidos = et_apellido.getText().toString();
        edad = et_edad.getText().toString();
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();
        nombre = et_nombre.getText().toString();
        String pass2 = et_confirmarPass.getText().toString();

        if(TextUtils.isEmpty(email)){
            valido = false;
            et_email.setError("Ingrese su correo electronico");
        }else{
            et_email.setError(null);
        }

        if(TextUtils.isEmpty(pass)){
            valido = false;
            et_pass.setError("Ingrese su contraseña");
        }else{
            et_pass.setError(null);
        }

        if (TextUtils.isEmpty(pass2)) {
            valido = false;
            et_confirmarPass.setError("Ingrese su contraseña");
        }else{
            et_confirmarPass.setError(null);
        }

        if(!pass.equals(pass2)){
            valido = false;
            Toast.makeText(this, R.string.contras_iguales, Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(nombre)){
            valido = false;
            et_nombre.setError("Ingrese su nombre");
        }else{
            et_nombre.setError(null);
        }

        if(TextUtils.isEmpty(apellidos)){
            valido = false;
            et_apellido.setError("Ingrese su apellido");
        }else{
            et_apellido.setError(null);
        }

        if(TextUtils.isEmpty(edad)){
            valido = false;
            et_edad.setError("Ingrese su edad");
        }else{
            et_edad.setError(null);
        }

        return valido;
    }

    private void saveUserToDatabase(String name, String email, String uid, String apellidos, String edad, boolean permisos){
        User user = new User(name,apellidos,edad,email,uid, permisos);
        mUserRef = FirebaseDatabase.getInstance().getReference();
        mUserRef.child("users")
                .child(email.replace(".",","))
                .setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(RegistrarseActivity.this, R.string.guardado, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
