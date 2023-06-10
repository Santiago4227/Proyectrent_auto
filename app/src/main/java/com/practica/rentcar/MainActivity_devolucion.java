package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity_devolucion extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText devolucion_numero, devolucion_fecha;
    Spinner devolucion_renta;
    ImageButton devolucion_volver, devolucion_guardar, devolucion_limpiar;

    Boolean isChecked = false;

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_devolucion);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        devolucion_numero = findViewById(R.id.etreturnNumber);
        devolucion_fecha = findViewById(R.id.etreturnDate);

        devolucion_renta = findViewById(R.id.spreturnNumberRent);

        devolucion_volver = findViewById(R.id.ibreturnBack);
        devolucion_guardar = findViewById(R.id.ibreturnSave);
        devolucion_limpiar = findViewById(R.id.ibreturnClean);

        // EVENTO PARA VOLVER AL MENÚ
        devolucion_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity_v1.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        devolucion_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolucion_numero.setText("");
                devolucion_fecha.setText("");
                devolucion_renta.setSelection(0);
            }
        });

        devolucion_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!devolucion_numero.getText().toString().isEmpty() && !devolucion_fecha.getText().toString().isEmpty() && devolucion_renta.getSelectedItem() != null){

                    db.collection("Autos").whereEqualTo("estado", isChecked).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().size() > 0) {

                                    final DocumentSnapshot autoSnapshot = task.getResult().getDocuments().get(0);

                                    db.collection("Renta").whereEqualTo("numero", devolucion_renta.getSelectedItem().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Map<String, Object> data = document.getData();

                                                    db.collection("Devolucion").whereEqualTo("numero", devolucion_numero.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {

                                                                if (task.getResult().isEmpty()) {

                                                                    Map<String, Object> Devolucion = new HashMap<>();
                                                                    Devolucion.put("numero", devolucion_numero.getText().toString());
                                                                    Devolucion.put("renta", devolucion_renta.getSelectedItem().toString());
                                                                    Devolucion.put("fecha", devolucion_fecha.getText().toString());
                                                                    Devolucion.putAll(data);

                                                                    String fechaDevolucion = devolucion_fecha.getText().toString();

                                                                    Date fechaDevolucionDate = parseDate(fechaDevolucion);

                                                                    Date fechaActual = new Date();

                                                                    if (fechaDevolucionDate != null && fechaActual != null) {

                                                                        if (fechaDevolucionDate.before(fechaActual)) {

                                                                            Toast.makeText(MainActivity_devolucion.this, "LA FECHA DE DEVOLUCION NO PUEDE SER MENOR A LA FECHA ACTUAL", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }

                                                                    } else {

                                                                        Toast.makeText(MainActivity_devolucion.this, "ERROR", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }

                                                                    db.collection("Renta").whereEqualTo("numero", devolucion_renta.getSelectedItem().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                documentSnapshot.getReference().delete();
                                                                            }

                                                                            Toast.makeText(MainActivity_devolucion.this, "RENTA ELIMINADA", Toast.LENGTH_LONG).show();
                                                                            Limpiar_campos();
                                                                        }

                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override

                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                    db.collection("Devolucion").add(Devolucion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {

                                                                            autoSnapshot.getReference().update("estado", true).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                                @Override
                                                                                public void onSuccess(Void unused) {

                                                                                    Toast.makeText(MainActivity_devolucion.this, "SE ACTUALIZO EL ESTADO DEL VEHICULO", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }) .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {

                                                                                    Toast.makeText(MainActivity_devolucion.this, "NO SE ACTUALIZO EL ESTADO DEL VEHICULO", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }

                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {

                                                                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "PLACA NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "PLACA NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "PLACA NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "PLACA NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "INGRESE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }

            }
        });

        db.collection("Renta").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    List<String> dataList = new ArrayList<>();

                    dataList.add("SELECCIONAR");

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String data = document.getString("numero");
                        dataList.add(data);

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity_devolucion.this,android.R.layout.simple_spinner_item, dataList);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    devolucion_renta.setAdapter(adapter);

                } else {

                    Toast.makeText(MainActivity_devolucion.this, "Error interno!", Toast.LENGTH_SHORT).show();
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
        devolucion_numero.setText("");
        devolucion_fecha.setText("");
        devolucion_renta.setSelection(0);
    }
}