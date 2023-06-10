package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity_renta extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText renta_numero, renta_nombre_usuario, renta_fecha_inicial, renta_fecha_final;
    Spinner renta_autos_disponibles;
    ImageButton renta_lista, renta_volver, renta_guardar, renta_limpiar;

    Boolean isChecked = true;

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_renta);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        renta_numero = findViewById(R.id.etrentNumber);
        renta_nombre_usuario = findViewById(R.id.etrentUserName);
        renta_fecha_inicial = findViewById(R.id.etrentInitialDate);
        renta_fecha_final = findViewById(R.id.etrentEndDate);

        renta_autos_disponibles = findViewById(R.id.sprentCarsAvailable);

        renta_lista = findViewById(R.id.ibrentList);
        renta_volver = findViewById(R.id.ibrentBack);
        renta_guardar = findViewById(R.id.ibrentSave);
        renta_limpiar = findViewById(R.id.ibrentClean);

        // ----------------------

        // EVENTO PARA VOLVER AL MENÚ
        renta_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity_v2.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        renta_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renta_numero.setText("");
                renta_nombre_usuario.setText("");
                renta_fecha_inicial.setText("");
                renta_fecha_final.setText("");
                renta_autos_disponibles.setSelection(0);
            }
        });

        // EVENTO PARA IR A LA LISTA
        renta_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_renta_lista.class);
                startActivity(intent);
            }
        });

        // EVENTO REGISTRAR RENTA
        renta_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!renta_numero.getText().toString().isEmpty() && !renta_nombre_usuario.getText().toString().isEmpty() && !renta_fecha_inicial.getText().toString().isEmpty() && !renta_fecha_final.getText().toString().isEmpty() && !renta_autos_disponibles.getSelectedItem().toString().isEmpty()) {

                    db.collection("Usuarios").whereEqualTo("usuario", renta_nombre_usuario.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().size() > 0) {

                                    db.collection("Autos").whereEqualTo("placa", renta_autos_disponibles.getSelectedItem().toString()).whereEqualTo("estado", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {

                                                if (task.getResult().size() > 0) {

                                                    final DocumentSnapshot autoSnapshot = task.getResult().getDocuments().get(0);

                                                    db.collection("Renta").whereEqualTo("numero", renta_numero.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {

                                                                if (task.getResult().isEmpty()) {

                                                                    Map<String, Object> Renta = new HashMap<>();
                                                                    Renta.put("numero", renta_numero.getText().toString());
                                                                    Renta.put("placa", renta_autos_disponibles.getSelectedItem().toString());
                                                                    Renta.put("usuario", renta_nombre_usuario.getText().toString());
                                                                    Renta.put("fecha_inicial", renta_fecha_inicial.getText().toString());
                                                                    Renta.put("fecha_final", renta_fecha_final.getText().toString());

                                                                    String fecha_inicial = renta_fecha_inicial.getText().toString();
                                                                    String fecha_final = renta_fecha_final.getText().toString();

                                                                    Date fechaInicial = parseDate(fecha_inicial);
                                                                    Date fechaFinal = parseDate(fecha_final);

                                                                    Date fechaActual = new Date();

                                                                    if (fechaInicial != null && fechaFinal != null) {

                                                                        if (fechaInicial.before(fechaActual)) {

                                                                            Toast.makeText(MainActivity_renta.this, "LA FECHA INICIAL NO PUEDE SER MENOR A LA FECHA ACTUAL", Toast.LENGTH_LONG).show();
                                                                            return;

                                                                        } else if (fechaFinal.before(fechaInicial)) {

                                                                            Toast.makeText(MainActivity_renta.this, "LA FECHA FINAL NO PUEDE SER ANTERIOR A LA FECHA INICIAL", Toast.LENGTH_LONG).show();
                                                                            return;
                                                                        }

                                                                    } else {
                                                                        Toast.makeText(MainActivity_renta.this, "ERROR", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }

                                                                    db.collection("Renta").add(Renta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {

                                                                            Toast.makeText(getApplicationContext(), "RENTA INGRESADA CORRECTAMENTE", Toast.LENGTH_LONG).show();

                                                                            Limpiar_campos();

                                                                            autoSnapshot.getReference().update("estado", false).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                                @Override
                                                                                public void onSuccess(Void unused) {

                                                                                    Toast.makeText(MainActivity_renta.this, "SE ACTUALIZÓ EL ESTADO DEL VEHICULO", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }) .addOnFailureListener(new OnFailureListener() {

                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {

                                                                                    Toast.makeText(MainActivity_renta.this, "NO SE ACTUALIZÓ EL ESTADO DEL VEHICULO", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {

                                                                            Toast.makeText(getApplicationContext(), "NO SE PUDO REALIZAR EL REGISTRO", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "EL USUARIO YA POSEE RENTA", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getApplicationContext(), "INGRESA EL AUTO DISPONIBLE", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "AUTO NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            } else {

                                Toast.makeText(getApplicationContext(), "USUARIO NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                            }

                            } else {
                                Toast.makeText(getApplicationContext(), "USUARIO NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db.collection("Autos").whereEqualTo("estado", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    List<String> dataList = new ArrayList<>();

                    dataList.add("SELECCIONAR");

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String data = document.getString("placa");
                        dataList.add(data);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_renta.this, android.R.layout.simple_spinner_item, dataList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    renta_autos_disponibles.setAdapter(adapter);

                } else {
                    Toast.makeText(MainActivity_renta.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void Limpiar_campos() {
        renta_numero.setText("");
        renta_nombre_usuario.setText("");
        renta_fecha_inicial.setText("");
        renta_fecha_final.setText("");
        renta_autos_disponibles.setSelection(0);
    }
}