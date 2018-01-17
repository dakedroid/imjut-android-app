package com.example.angel.imjut.SubirContenido;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.angel.imjut.Modelos.BolsaTrabajo;
import com.example.angel.imjut.Modelos.Evento;
import com.example.angel.imjut.Modelos.Foto;
import com.example.angel.imjut.Modelos.Programa;
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

public class SubirContenidoPrueba extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    Bitmap bitmap;
    private Uri filePath;
    Boolean imagePicked;

    private ImageView iv_subirImagen;
    private EditText et_programa;
    private EditText et_objetivo;
    private EditText et_clase;
    private Button btn_subir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_contenido_prueba);

        et_programa = findViewById(R.id.et_programa);
        et_objetivo = findViewById(R.id.et_objetivos);
        et_clase = findViewById(R.id.clase);
        btn_subir = findViewById(R.id.btn_subirPrograma);
        iv_subirImagen = findViewById(R.id.subir_imagen);
        iv_subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clase = et_clase.getText().toString();
                if(!clase.isEmpty()){
                    switch (clase){
                        case "programas":
                            sendPrograma();
                            break;
                        case "eventos":
                            sendEvento();
                            break;
                        case "galeria":
                            sendFotoToGalery();
                            break;
                        case "bolsa":
                            sendBolsa();
                            break;
                        default:
                            Toast.makeText(SubirContenidoPrueba.this, "Clase no valida", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    private void sendBolsa(){
        BolsaTrabajo bolsaTrabajo = new BolsaTrabajo();
        String folder = "bolsaTrabajo";

        String tituloMarca = et_programa.getText().toString();
        String requisitos = et_objetivo.getText().toString();
        String postId = getUid();

        bolsaTrabajo.setMarca(tituloMarca);
        bolsaTrabajo.setRequisitos(requisitos);
        bolsaTrabajo.setPostId(postId);

        String postImageUrl = "gs://imjut-ecdca.appspot.com/imagenes/" + folder + "/img" + bolsaTrabajo.getPostId()+ ".jpg";


        bolsaTrabajo.setPostImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("bolsa").child(postId).setValue(bolsaTrabajo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirContenidoPrueba.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(bolsaTrabajo.getPostId(), folder);

    }

    private void sendFotoToGalery(){
        Foto foto = new Foto();
        String folder = "galeria";
        String descripcion = et_objetivo.getText().toString();
        String postId = getUid();

        foto.setDescripcion(descripcion);
        foto.setPostId(postId);

        String postImageUrl = "gs://imjut-ecdca.appspot.com/imagenes/" + folder + "/img" + foto.getPostId()+ ".jpg";
        foto.setImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("galeria").child(postId).setValue(foto, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirContenidoPrueba.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(foto.getPostId(), folder);

    }

    private void sendEvento(){
        Evento evento = new Evento();
        String folder = "eventos";
        String titulo_programa = et_programa.getText().toString();
        String titulo_objetivos = et_objetivo.getText().toString();
        String postId = getUid();

        evento.setPostId(postId);
        evento.setDescripcion(titulo_objetivos);
        evento.setTitulo(titulo_programa);

        String postImageUrl = "gs://imjut-ecdca.appspot.com/imagenes/" + folder + "/img" + evento.getPostId()+ ".jpg";

        evento.setPostImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("eventos").child(postId).setValue(evento, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirContenidoPrueba.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(evento.getPostId(), "eventos");
    }

    private void sendPrograma(){
        Programa programa = new Programa();
        String folder = "programas";
        String titulo_programa = et_programa.getText().toString();
        String titulo_objetivos = et_objetivo.getText().toString();
        String postId = getUid();

        programa.setUid(postId);
        programa.setObjetivos(titulo_objetivos);
        programa.setTitulo(titulo_programa);

        String postImageUrl = "gs://imjut-ecdca.appspot.com/imagenes/" + folder + "/img" + programa.getUid()+ ".jpg";

        programa.setPostImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("programas").child(postId).setValue(programa, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirContenidoPrueba.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(programa.getUid(), "programas");
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
                imagePicked = true;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_subirImagen.setImageBitmap(bitmap);
                iv_subirImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadFile(String UID, String folder) {
        if (filePath != null) {
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + folder + "/img" + UID + ".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Archivo Subido", Toast.LENGTH_LONG).show();
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
}
