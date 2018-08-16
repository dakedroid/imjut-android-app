package com.imjut.android.SubirContenido;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imjut.android.Modelos.Notificacion;
import com.imjut.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MandarNotificacion extends AppCompatActivity {

    @BindView(R.id.titulo_notificacion) EditText mTitleNotificacion;
    @BindView(R.id.descripcion_notificacion) EditText mDescriptionNotification;
    private Button mBtnSendNot;

    private String textTitle;
    private String textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandar_notificacion);
        ButterKnife.bind(this);

        mBtnSendNot = findViewById(R.id.btn_mandar_notificacion);
        mBtnSendNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarForm()){
                    sendNotification();
                }
            }
        });

    }

    private void sendNotification(){
        Toast.makeText(this, "Mandar Notificación", Toast.LENGTH_SHORT).show();
        String postId = getUid();
        Notificacion mNotificacion = new Notificacion();
        mNotificacion.setTitulo(textTitle);
        mNotificacion.setDescripcion(textDescription);
        mNotificacion.setTimeCreated(System.currentTimeMillis());
        mNotificacion.setPostId(postId);
        FirebaseDatabase.getInstance().getReference("posts").child("notificaciones").child(postId).setValue(mNotificacion, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(MandarNotificacion.this, "Notificacion Mandada", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean validarForm(){
        boolean valido = true;

        textTitle = mTitleNotificacion.getText().toString();
        textDescription = mDescriptionNotification.getText().toString();

        if(TextUtils.isEmpty(textTitle)){
            valido = false;
            mTitleNotificacion.setError("Introduzca un titulo");
        }else{
            mTitleNotificacion.setError(null);
        }

        if(TextUtils.isEmpty(textDescription)){
            valido = false;
            mDescriptionNotification.setError("Introduzca una descripcion");
        }else{
            mDescriptionNotification.setError(null);
        }
        return valido;
    }

    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
