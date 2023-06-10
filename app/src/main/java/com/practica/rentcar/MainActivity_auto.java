package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity_auto extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText auto_placa, auto_marca, auto_valor;
    Switch auto_estado;
    ImageButton auto_regresar, auto_guardar, auto_limpiar, auto_buscar, auto_editar, auto_borrar;

    String auto_placa_old, auto_placa_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        auto_placa = findViewById(R.id.etcarPlateNumber);
        auto_marca = findViewById(R.id.etcarBrand);
        auto_valor = findViewById(R.id.etcarValue);

        auto_estado = findViewById(R.id.swcarStatus);

        auto_regresar = findViewById(R.id.ibcarBack);
        auto_limpiar = findViewById(R.id.ibcarClean);

        auto_guardar = findViewById(R.id.ibcarSave);
        auto_buscar = findViewById(R.id.ibcarSearch);
        auto_editar = findViewById(R.id.ibcarEdit);
        auto_borrar = findViewById(R.id.ibcarDelete);

        // ----------------------

        // EVENTO PARA VOLVER AL MENÚ
        auto_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity_v1.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        auto_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_placa.setText("");
                auto_marca.setText("");
                auto_valor.setText("");
                auto_estado.setChecked(false);
            }
        });

        // EVENTO PARA GUARDAR AUTO
        auto_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!auto_placa.getText().toString().isEmpty() && !auto_marca.getText().toString().isEmpty() && !auto_valor.getText().toString().isEmpty()) {

                    db.collection("Autos").whereEqualTo("placa", auto_placa.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {

                                    Map<String, Object> Autos = new HashMap<>();
                                    Autos.put("placa", auto_placa.getText().toString());
                                    Autos.put("marca", auto_marca.getText().toString());
                                    Autos.put("valor", auto_valor.getText().toString());
                                    boolean ischecked = auto_estado.isChecked();
                                    Autos.put("estado", ischecked);

                                    db.collection("Autos").add(Autos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Toast.makeText(MainActivity_auto.this, "AUTO INGRESADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                            Limpiar_campos();
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(MainActivity_auto.this, "ERROR AL REALIZAR EL REGISTRO", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {

                                    Toast.makeText(MainActivity_auto.this, "AUTO EXISTENTE, INTENTE DE NUEVO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity_auto.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EVENTO PARA BUSCAR AUTO
        auto_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!auto_placa.getText().toString().isEmpty()) {

                    db.collection("Autos").whereEqualTo("placa", auto_placa.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (!task.getResult().isEmpty()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        auto_placa_old = document.getString("placa");
                                        auto_placa_find = document.getId();
                                        auto_marca.setText(document.getString("marca"));
                                        auto_placa.setText(document.getString("placa"));
                                        auto_valor.setText(document.getString("valor"));
                                        boolean estado = document.getBoolean("estado");
                                        auto_estado.setChecked(estado);

                                        Toast.makeText(getApplicationContext(), "AUTO ECONTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(MainActivity_auto.this, "AUTO NO EXISTENTE", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity_auto.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EVENTO PARA EDITAR AUTO
        auto_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (auto_placa_find != null ) {

                    if (!auto_placa.getText().toString().isEmpty() && !auto_marca.getText().toString().isEmpty() && !auto_valor.getText().toString().isEmpty()) {

                        if (!auto_placa_old.equals(auto_placa.getText().toString())) {

                            db.collection("Autos").whereEqualTo("placa", auto_placa.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {

                                        if (task.getResult().isEmpty()) {

                                            Map<String, Object> Autos = new HashMap<>();
                                            Autos.put("marca", auto_marca.getText().toString());
                                            Autos.put("placa", auto_placa.getText().toString());
                                            Autos.put("valor", auto_valor.getText().toString());
                                            boolean ischecked = auto_estado.isChecked();
                                            Autos.put("estado", ischecked);

                                            db.collection("Autos").document(auto_placa_find).set(Autos).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                @Override
                                                public void onSuccess(Void unused) {

                                                    Toast.makeText(MainActivity_auto.this, "AUTO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                                    Limpiar_campos();
                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(MainActivity_auto.this, "ERROR AL EDITAR EL AUTO", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {
                                            Toast.makeText(MainActivity_auto.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                        } else {

                            Map<String, Object> Autos = new HashMap<>();
                            Autos.put("marca", auto_marca.getText().toString());
                            Autos.put("placa", auto_placa.getText().toString());
                            Autos.put("valor", auto_valor.getText().toString());
                            boolean ischecked = auto_estado.isChecked();
                            Autos.put("estado", ischecked);

                            db.collection("Autos").document(auto_placa_find).set(Autos).addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(MainActivity_auto.this, "AUTO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(MainActivity_auto.this, "ERROR AL EDITAR EL AUTO", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(MainActivity_auto.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity_auto.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // EVENTO PARA BORRAR AUTO
        auto_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!auto_placa.getText().toString().isEmpty()) {

                    db.collection("Autos").document(auto_placa_find).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(MainActivity_auto.this, "AUTO ELIMINADO", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    });

                } else {
                    Toast.makeText(MainActivity_auto.this, "INGRESA NUMERO DE PLACA", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Limpiar_campos(){
        auto_placa.setText("");
        auto_marca.setText("");
        auto_valor.setText("");
        auto_estado.setChecked(false);
    }
}