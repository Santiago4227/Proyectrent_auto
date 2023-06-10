package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity_recuperar extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText recuperar_usuario, recuperar_palabra_reservada, recuperar_nueva_contrasena;
    ImageButton recuperar_regresar, recuperar_guardar, recuperar_limpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recuperar);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        recuperar_usuario = findViewById(R.id.etrecuUser);
        recuperar_palabra_reservada = findViewById(R.id.etrecuWord);
        recuperar_nueva_contrasena = findViewById(R.id.etrecuNewPassword);

        recuperar_regresar = findViewById(R.id.ibrecuBack);
        recuperar_guardar = findViewById(R.id.ibrecuSave);
        recuperar_limpiar = findViewById(R.id.ibrecuClean);

        // ----------------------

        // EVENTO PARA REGRESAR AL LOGIN
        recuperar_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        recuperar_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperar_usuario.setText("");
                recuperar_palabra_reservada.setText("");
                recuperar_nueva_contrasena.setText("");
            }
        });

        // EVENTO PARA RECUPERAR CONTRASEÑA
        recuperar_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!recuperar_usuario.getText().toString().isEmpty() && !recuperar_palabra_reservada.getText().toString().isEmpty() && !recuperar_nueva_contrasena.getText().toString().isEmpty()){

                    db.collection("Usuarios").whereEqualTo("usuario", recuperar_usuario.getText().toString()).whereEqualTo("palabra", recuperar_palabra_reservada.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().size() > 0) {

                                    task.getResult().getDocuments().get(0).getReference().update("contraseña", recuperar_nueva_contrasena.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void unused) {

                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                            String contraseña = document.getString("contraseña");

                                            if (Objects.equals(contraseña, recuperar_nueva_contrasena.getText().toString())) {

                                                Toast.makeText(MainActivity_recuperar.this, "CONTRASEÑA YA EXISTENTE", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(MainActivity_recuperar.this, "CAMBIO DE CONTRASEÑA EXITOSO", Toast.LENGTH_SHORT).show();
                                                Limpiar_campos();
                                            }
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_recuperar.this, "ERROR AL CAMBIAR LA CONTRASEÑA", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(MainActivity_recuperar.this, "ERROR EN USUARIO O PALABRA", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity_recuperar.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Limpiar_campos(){
        recuperar_usuario.setText("");
        recuperar_palabra_reservada.setText("");
        recuperar_nueva_contrasena.setText("");
    }
}