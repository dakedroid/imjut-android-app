package com.example.angel.imjut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AsistirActivity extends AppCompatActivity {


    private Button btn_registrarse;
    private TextView tv_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistir);

        btn_registrarse = (Button) findViewById(R.id.registrarse_asistir);
        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarForm()){

                }
            }
        });
        tv_volver = (TextView) findViewById(R.id.volver);
        tv_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public boolean validarForm(){
        boolean valido = true;

        return valido;
    }

}
