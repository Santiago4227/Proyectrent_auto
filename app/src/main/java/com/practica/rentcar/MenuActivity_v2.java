package com.practica.rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity_v2 extends AppCompatActivity {

    // INSTANCIA DE VARIABLES

    ImageButton menu_v2_rentar_auto, menu_v2_regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_v2);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        menu_v2_rentar_auto = findViewById(R.id.ibmenuv2RentCar);
        menu_v2_regresar = findViewById(R.id.ibmenuv2Back);

        // ----------------------

        // EVENTO PARA REGRESAR AL LOGIN
        menu_v2_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA IR A RENTAR AUTO
        menu_v2_rentar_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_renta.class);
                startActivity(intent);
            }
        });

    }
}