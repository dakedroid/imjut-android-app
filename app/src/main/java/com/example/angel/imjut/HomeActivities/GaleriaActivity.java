package com.example.angel.imjut.HomeActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.angel.imjut.R;

public class GaleriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
