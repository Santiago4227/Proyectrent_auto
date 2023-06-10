package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity_renta_lista extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    ImageButton lista_volver;
    RecyclerView lista_renta;

    ArrayList<renta> RentaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta_lista);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        lista_volver = findViewById(R.id.iblistBack);
        lista_renta = findViewById(R.id.rvlistRent);

        RentaLista = new ArrayList<>();
        lista_renta.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        lista_renta.setHasFixedSize(true);

        CargarRenta();

        // EVENTO PARA VOLVER A RENTA AUTO
        lista_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_renta.class);
                startActivity(intent);
            }
        });
    }

    // EVENTO PARA CARGAR LISTA
    private void CargarRenta() {

        db.collection("Renta").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {

                    renta objeto_renta = new renta();

                    objeto_renta.setRenta_numero(document.getString("numero"));
                    objeto_renta.setRenta_placa(document.getString("placa"));
                    objeto_renta.setRenta_usuario(document.getString("usuario"));
                    objeto_renta.setFecha_inicial(document.getString("fecha_inicial"));
                    objeto_renta.setFecha_final(document.getString("fecha_final"));

                    RentaLista.add(objeto_renta);
                }

                renta_adaptador adaptador_renta = new renta_adaptador(RentaLista);

                lista_renta.setAdapter(adaptador_renta);
            }
        });

    }
}