package com.example.angel.imjut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerda_de);
        getSupportActionBar().setTitle(R.string.acerca);
    }
}
