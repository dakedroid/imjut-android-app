package com.imjut.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class AcercaDeActivity extends AppCompatActivity {

    final String[] titles = {"Emporio Taco", "Pinturas de la cuenta", "Fhucup"};
    final int[] thumbs = {R.drawable.emporio, R.drawable.pinturas, R.drawable.fhucup};
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerda_de);
        getSupportActionBar().setTitle(R.string.acerca);

        mGridView = findViewById(R.id.gridView1);
        mGridView.setAdapter(new CustomGridAdapter(this, titles, thumbs, 3));
    }
}
