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

import com.example.angel.imjut.Modelos.Foto;
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

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubirGaleriaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private Bitmap mBitmap;
    private boolean mImagePicked = false;

    @BindView(R.id.titulo_subir_foto) EditText mTituloFoto;
    private ImageView mImagePicker;
    private TextView mFilePath;
    private ImageView mImagePreview;
    private Button mBtnSubirFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_galeria);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.subir_galeria);

        mImagePicker = findViewById(R.id.btn_subir_imagen);
        mImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        mFilePath = findViewById(R.id.filepath);
        mImagePreview = findViewById(R.id.image_preview);
        mBtnSubirFoto = findViewById(R.id.btn_subir_foto);
        mBtnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarForm()){
                    subirFoto();
                }
            }
        });
    }

    private void subirFoto(){
        Foto foto = new Foto();
        String folder = "galeria";
        String descripcion = mTituloFoto.getText().toString();
        String postId = getUid();

        foto.setDescripcion(descripcion);
        foto.setPostId(postId);


        String postImageUrl = "gs://imjut-ecdca.appspot.com/thumbs/" + folder + "_thumb/img_galeria" + foto.getPostId() + "_thumb.jpg";
        foto.setImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("galeria").child(postId).setValue(foto, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirGaleriaActivity.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(foto.getPostId(), folder);
    }

    private boolean validarForm(){
        boolean valido = true;

        String titulo_foto = mTituloFoto.getText().toString();

        if(TextUtils.isEmpty(titulo_foto)){
            valido = false;
            mTituloFoto.setError("Escribe un titulo");
        }else{
            mTituloFoto.setError(null);
        }

        if(!mImagePicked){
            valido = false;
            Toast.makeText(this, "Por favor eliga una foto", Toast.LENGTH_SHORT).show();
        }
        return valido;
    }

    public void uploadFile(String UID, String folder) {
        if (filePath != null) {
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + folder + "/img_galeria" + UID + ".jpg");

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
