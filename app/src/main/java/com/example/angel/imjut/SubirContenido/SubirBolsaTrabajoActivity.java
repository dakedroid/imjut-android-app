package com.example.angel.imjut.SubirContenido;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.imjut.Modelos.BolsaTrabajo;
import com.example.angel.imjut.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubirBolsaTrabajoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private Bitmap mBitmap;
    private boolean mImagePicked = false;

    @BindView(R.id.nombre_marca) EditText mNombreMarca;
    @BindView(R.id.descripcion_gral_empleo) EditText mDescripcionGral;
    @BindView(R.id.tipo_empleo) EditText mTipoEmpleo;
    @BindView(R.id.requisitos_gral_empleo) EditText mRequisitos;
    @BindView(R.id.sueldo_empleo) EditText mSueldo;
    @BindView(R.id.educacion_empleo) EditText mEducacion;
    @BindView(R.id.habilidades_empleo) EditText mHabilidades;
    @BindView(R.id.experiencia_empleo) EditText mExperiencia;

    private Button btnSendEmpleo;
    private TextView mFilePath;
    private ImageView mImagePicker;
    private ImageView mImagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_bolsa_trabajo);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.subir_bolsa_trabajo);

        btnSendEmpleo = findViewById(R.id.btn_subir_empleo);
        btnSendEmpleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarForm()){
                    sendBolsa();
                }
            }
        });
        mFilePath = findViewById(R.id.filepath);
        mImagePicker = findViewById(R.id.btn_subir_imagen);
        mImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        mImagePreview = findViewById(R.id.image_preview);
    }

    private boolean validarForm(){
        boolean valido = true;
        String tituloMarca = mNombreMarca.getText().toString();
        String descripcion = mDescripcionGral.getText().toString();
        String tipoEmpleo = mTipoEmpleo.getText().toString();
        String requisitos = mRequisitos.getText().toString();
        String sueldo = mSueldo.getText().toString();
        String educacion = mEducacion.getText().toString();
        String habilidades = mHabilidades.getText().toString();
        String experiencia = mExperiencia.getText().toString();

        if(TextUtils.isEmpty(tituloMarca)){
            valido = false;
            mNombreMarca.setError("Escriba un nombre");
        }else{
            mNombreMarca.setError(null);
        }
        if(TextUtils.isEmpty(descripcion)){
            valido = false;
            mDescripcionGral.setError("Escriba una descripcion");
        }else{
            mDescripcionGral.setError(null);
        }
        if(TextUtils.isEmpty(tipoEmpleo)){
            valido = false;
            mTipoEmpleo.setError("Escriba un tipo de empleo");
        }else{
            mTipoEmpleo.setError(null);
        }
        if(TextUtils.isEmpty(requisitos)){
            valido = false;
            mRequisitos.setError("Escriba los requisitos");
        }else{
            mRequisitos.setError(null);
        }
        if(TextUtils.isEmpty(sueldo)){
            valido = false;
            mSueldo.setError("Escriba un sueldo");
        }else{
            mSueldo.setError(null);
        }
        if(TextUtils.isEmpty(educacion)){
            valido = false;
            mEducacion.setError("Escriba la educacion necesaria para el empleo");
        }else{
            mEducacion.setError(null);
        }
        if(TextUtils.isEmpty(habilidades)){
            valido = false;
            mHabilidades.setError("Escriba las habilidades necesarias para el empleo");
        }else{
            mHabilidades.setError(null);
        }
        if(TextUtils.isEmpty(experiencia)){
            valido = false;
            mExperiencia.setError("Escriba la experiencia necesaria para el empleo");
        }else{
            mExperiencia.setError(null);
        }
        if(!mImagePicked){
            valido = false;
            Toast.makeText(this, "Eliga una imagen por favor", Toast.LENGTH_SHORT).show();
        }
        return valido;
    }

    private void sendBolsa(){
        BolsaTrabajo bolsaTrabajo = new BolsaTrabajo();
        String folder = "bolsaTrabajo";

        String tituloMarca = mNombreMarca.getText().toString();
        String descripcion = mDescripcionGral.getText().toString();
        String tipoEmpleo = mTipoEmpleo.getText().toString();
        String requisitos = mRequisitos.getText().toString();
        String sueldo = mSueldo.getText().toString();
        String educacion = mEducacion.getText().toString();
        String habilidades = mHabilidades.getText().toString();
        String experiencia = mExperiencia.getText().toString();
        String postId = getUid();
        bolsaTrabajo.setPostId(postId);
        bolsaTrabajo.setMarca(tituloMarca);
        String postImageUrl = "gs://imjut-ecdca.appspot.com/thumbs/" + folder + "_thumb/img_bolsa" + bolsaTrabajo.getPostId() + "_thumb.jpg";
        bolsaTrabajo.setPostImageUrl(postImageUrl);

        bolsaTrabajo.setTimeCreated(System.currentTimeMillis());
        bolsaTrabajo.setEducacion(educacion);
        bolsaTrabajo.setDescripcionGeneral(descripcion);
        bolsaTrabajo.setTipoPuesto(tipoEmpleo);
        bolsaTrabajo.setSueldo(sueldo);
        bolsaTrabajo.setExperiencia(experiencia);
        bolsaTrabajo.setHabilidades(habilidades);
        bolsaTrabajo.setRequisitosGenerales(requisitos);

        FirebaseDatabase.getInstance().getReference("posts").child("bolsa").child(postId).setValue(bolsaTrabajo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirBolsaTrabajoActivity.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(bolsaTrabajo.getPostId(), folder);
    }

    public void uploadFile(String UID, String folder) {
        if (filePath != null) {
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + folder + "/img_bolsa" + UID + ".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Imagen subida correctamente", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //progressDialog.setMessage("Subiendo " + ((int) progress) + "%...");
                        }
                    });
        }
        else {
            Toast.makeText(this, "Error al subir el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escojer una Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                mFilePath.setText(filePath.toString());
                mImagePicked = true;
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImagePreview.setImageBitmap(mBitmap);
                mImagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
