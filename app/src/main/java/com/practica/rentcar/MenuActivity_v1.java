package com.practica.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity_v1 extends AppCompatActivity {

    // INSTANCIA DE VARIABLES

    ImageButton menu_v1_devolver_auto, menu_v1_registrar_auto, menu_v1_regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_v1);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        menu_v1_devolver_auto = findViewById(R.id.ibmenuv1ReturnCar);
        menu_v1_registrar_auto = findViewById(R.id.ibmenuv1RegisterCar);
        menu_v1_regresar = findViewById(R.id.ibmenuv1Back);

        // ----------------------

        // EVENTO PARA REGRESAR AL LOGIN
        menu_v1_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA IR A DEVOLVER AUTO
        menu_v1_devolver_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_devolucion.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA IR A REGISTRAR AUTO
        menu_v1_registrar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_auto.class);
                startActivity(intent);
            }
        });

    }
}