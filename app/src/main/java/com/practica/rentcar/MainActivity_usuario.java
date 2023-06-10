package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_usuario extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText registrar_usuario, registrar_nombre, registrar_contrasena, registrar_palabra_reservada;
    Switch registrar_rol;
    ImageButton registrar_regresar, registrar_guardar, registrar_limpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        registrar_usuario = findViewById(R.id.etregisterUser);
        registrar_nombre = findViewById(R.id.etregisterName);
        registrar_contrasena = findViewById(R.id.etregisterPassword);
        registrar_palabra_reservada = findViewById(R.id.etregisterWord);

        registrar_rol = findViewById(R.id.swregisterRol);

        registrar_regresar = findViewById(R.id.ibregisterBack);
        registrar_guardar = findViewById(R.id.ibregisterSave);
        registrar_limpiar = findViewById(R.id.ibregisterClean);

        // ----------------------

        // EVENTO PARA REGRESAR AL LOGIN
        registrar_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        registrar_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar_usuario.setText("");
                registrar_nombre.setText("");
                registrar_contrasena.setText("");
                registrar_palabra_reservada.setText("");
                registrar_rol.setChecked(false);
            }
        });

        // EVENTO PARA REGISTRAR USUARIO
        registrar_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!registrar_usuario.getText().toString().isEmpty() && !registrar_nombre.getText().toString().isEmpty() && !registrar_contrasena.getText().toString().isEmpty() && !registrar_palabra_reservada.getText().toString().isEmpty()) {

                    db.collection("Usuarios").whereEqualTo("usuario", registrar_usuario.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().isEmpty()) {

                                    Map<String, Object> Usuarios = new HashMap<>();
                                    Usuarios.put("usuario", registrar_usuario.getText().toString());
                                    Usuarios.put("nombre", registrar_nombre.getText().toString());
                                    Usuarios.put("contraseña", registrar_contrasena.getText().toString());
                                    Usuarios.put("palabra", registrar_palabra_reservada.getText().toString());
                                    boolean ischecked = registrar_rol.isChecked();
                                    Usuarios.put("rol", ischecked);

                                    db.collection("Usuarios").add(Usuarios).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Toast.makeText(MainActivity_usuario.this, "USUARIO REGISTRADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                            Limpiar_campos();
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity_usuario.this, "ERROR AL REALIZAR EL REGISTRO", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(MainActivity_usuario.this, "USUARIO EXISTENTE, INTENTE DE NUEVO", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity_usuario.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Limpiar_campos(){
        registrar_usuario.setText("");
        registrar_nombre.setText("");
        registrar_contrasena.setText("");
        registrar_palabra_reservada.setText("");
        registrar_rol.setChecked(false);
    }
}