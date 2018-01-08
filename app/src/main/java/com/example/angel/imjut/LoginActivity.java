package com.example.angel.imjut;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_email) EditText et_email;
    @BindView(R.id.et_pass) EditText et_pass;
    private Button btnLogin;
    private TextView tv_registrarse;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeIntent);
                }
            }
        };

        btnLogin = (Button) findViewById(R.id.btn_ingresar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarForm()){
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        tv_registrarse = (TextView) findViewById(R.id.tv_registrarse);
        tv_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registroIntent = new Intent(LoginActivity.this, RegistrarseActivity.class);
                startActivity(registroIntent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private boolean validarForm(){
        Boolean valido = true;
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        if(TextUtils.isEmpty(email)){
            valido = false;
            et_email.setError("Introduzca su correo");
        }else{
            et_email.setError(null);
        }

        if(TextUtils.isEmpty(pass)){
            valido = false;
            et_pass.setError("Introduzca su contraseña");
        }else{
            et_pass.setError(null);
        }
        return valido;
    }
}
