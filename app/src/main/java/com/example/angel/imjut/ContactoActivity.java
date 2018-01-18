package com.example.angel.imjut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().setTitle(R.string.contacto);
    }
}
