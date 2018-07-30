package com.imjut.android.SubirContenido;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;


import com.imjut.android.Modelos.Evento;
import com.imjut.android.R;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubirEventoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private Bitmap mBitmap;
    private boolean mImagePicked = false;

    @BindView(R.id.titulo_subir_evento) EditText mTituloEvento;
    @BindView(R.id.descripcion_subir_evento) EditText mDescripcionEvento;
    @BindView(R.id.fecha_subir_evento) EditText mFechaEvento;
    @BindView(R.id.hora_subir_evento) EditText mHoraEvento;
    @BindView(R.id.direccion_subir_evento) EditText mDireccionEvento;
    private ImageView mImagePicker;
    private TextView mFilePath;
    private ImageView mImagePreview;
    private Button mBtnSubirEvento;

    //TIME STUFF
    private DatePickerFragment datePickerFragment;
    private TimePickerFragment timePickerFragment;
    private long timeInMillisPicked;
    private long dateInMillisPicked;
    private long dateAndTimeInMillis;
    private Calendar dateInMillis;
    private Calendar timeInMillis;

    private boolean datePicked;
    private boolean timePicked;
    private boolean valido;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_evento);
        getSupportActionBar().setTitle(R.string.subir_evento);
        ButterKnife.bind(this);

        mFechaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        mHoraEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
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
        mBtnSubirEvento = findViewById(R.id.btn_subir_evento);
        mBtnSubirEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarForm()){
                    dateInMillis.set(Calendar.HOUR_OF_DAY, timeInMillis.get(Calendar.HOUR_OF_DAY));
                    dateInMillis.set(Calendar.MINUTE, timeInMillis.get(Calendar.MINUTE));
                    dateAndTimeInMillis = dateInMillis.getTimeInMillis();
                    sendEvento();
                }
            }
        });
    }

    private void sendEvento(){
        Evento evento = new Evento();
        String folder = "eventos";
        String titulo_evento = mTituloEvento.getText().toString();
        String descripcion_evento = mDescripcionEvento.getText().toString();
        String direccion_evento = mDireccionEvento.getText().toString();
        String postId = getUid();

        evento.setPostId(postId);
        evento.setDescripcion(descripcion_evento);
        evento.setTitulo(titulo_evento);
        evento.setDireccion(direccion_evento);
        evento.setDate(dateInMillisPicked);
        evento.setHour(timeInMillisPicked);
        evento.setTimeEnd(dateAndTimeInMillis);
        evento.setTimeCreated(System.currentTimeMillis());


        String postImageUrl = "gs://imjut-ecdca.appspot.com/thumbs/" + folder + "_thumb/img_eventos" + evento.getPostId() + "_thumb.jpg";

        evento.setPostImageUrl(postImageUrl);

        FirebaseDatabase.getInstance().getReference("posts").child("eventos").child(postId).setValue(evento, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(SubirEventoActivity.this, R.string.guardado, Toast.LENGTH_SHORT).show();
            }
        });
        uploadFile(evento.getPostId(), "eventos");
    }

    public void uploadFile(String UID, String folder) {
        if (filePath != null) {
            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("imagenes/" + folder + "/img_eventos" + UID + ".jpg");

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

    private boolean validarForm(){
        boolean valido = true;

        String tituloEvento = mTituloEvento.getText().toString();
        String descripcionEvento = mDescripcionEvento.getText().toString();
        String direccionEvento = mDireccionEvento.getText().toString();

        if(!datePicked){
            mFechaEvento.setError("Elige una fecha");
            valido = false;
        }else{
            mFechaEvento.setError(null);
        }

        if(!timePicked){
            mHoraEvento.setError("Elige una hora");
            valido = false;
        }else{
            mHoraEvento.setError(null);
        }

        if(TextUtils.isEmpty(tituloEvento)){
            valido = false;
            mTituloEvento.setError("Introduzca un nombre");
        }else{
            mTituloEvento.setError(null);
        }

        if(TextUtils.isEmpty(descripcionEvento)){
            valido = false;
            mDescripcionEvento.setError("Introduzca una descripcion");
        }else{
            mDescripcionEvento.setError(null);
        }

        if(TextUtils.isEmpty(direccionEvento)){
            valido = false;
            mDireccionEvento.setError("Introduzca una dirección");
        }else{
            mDireccionEvento.setError(null);
        }

        if(!mImagePicked){
            valido = false;
            Toast.makeText(this, "Eliga una imagen porfavor", Toast.LENGTH_SHORT).show();
        }
        return valido;
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


    /********************DATEPICKER**************************/
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private DatePickerDialog.OnDateSetListener listener;

        public static SubirEventoActivity.DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            SubirEventoActivity.DatePickerFragment fragment = new SubirEventoActivity.DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, year,month,day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.setTitle("¿Cuando es tu evento?");
            // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    }

    public void showDatePickerDialog(View v) {
        datePickerFragment = SubirEventoActivity.DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datePicked = true;
                dateInMillis = Calendar.getInstance();
                dateInMillis.set(Calendar.YEAR, year);
                dateInMillis.set(Calendar.MONTH, month);
                dateInMillis.set(Calendar.DAY_OF_MONTH, day);
                dateInMillisPicked = dateInMillis.getTimeInMillis();

                String monthS = String.valueOf(month+1);
                String dayS = String.valueOf(day);

                if((month+1) < 10){
                    monthS = "0"+(month+1);
                }
                if(day < 10){
                    dayS = "0"+day;
                }
                // +1 because january is zero
                final String selectedDate = dayS + " / " + monthS + " / " + year;
                mFechaEvento.setText(selectedDate);

            }
        });
        datePickerFragment.setCancelable(false);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /****************TIMEPICKER***********************/
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private TimePickerDialog.OnTimeSetListener listener;

        public static SubirEventoActivity.TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
            SubirEventoActivity.TimePickerFragment fragment = new SubirEventoActivity.TimePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(TimePickerDialog.OnTimeSetListener listener) {
            this.listener = listener;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        }
    }

    public void showTimePickerDialog(View v) {
        timePickerFragment = SubirEventoActivity.TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timePicked = true;
                timeInMillis = Calendar.getInstance();
                timeInMillis.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timeInMillis.set(Calendar.MINUTE,minute);
                String hour = String.valueOf(hourOfDay);
                String minutes = String.valueOf(minute);
                if(hourOfDay < 10){
                    hour = "0"+hourOfDay;
                }
                if(minute < 10){
                    minutes = "0"+minute;
                }
                timeInMillisPicked = timeInMillis.getTimeInMillis();
                final String timeSelected = hour + ":"+minutes + " HRS";
                mHoraEvento.setText(timeSelected);
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
