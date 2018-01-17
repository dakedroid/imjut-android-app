package com.example.angel.imjut.HomeActivities.Galeria;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.angel.imjut.R;

import java.util.ArrayList;

public class GaleriaDetallesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_detalles);

        ArrayList<String> images = getIntent().getStringArrayListExtra("ArrayImagenes");
        int posicion = getIntent().getIntExtra("posicion", 0);

        ViewPager mViewPager;
        ViewPagerAdapter mViewPagerAdapter;

        mViewPager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(GaleriaDetallesActivity.this,images, posicion);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(posicion,true);
        Log.d("viewPagePos", String.valueOf(posicion));
    }
}
