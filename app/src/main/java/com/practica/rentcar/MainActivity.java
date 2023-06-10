package com.practica.rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // INSTANCIA DE VARIABLES

    EditText login_usuario, login_contrasena;
    Button login_recuperar, login_registrar_usuario;
    ImageButton login_limpiar, login_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // INSTANCIA DE VARIABLES

        login_usuario = findViewById(R.id.etloginUser);
        login_contrasena = findViewById(R.id.etloginPassword);

        login_recuperar = findViewById(R.id.btnloginForgot);
        login_registrar_usuario = findViewById(R.id.btnloginUserRegister);

        login_limpiar = findViewById(R.id.ibloginClean);
        login_entrar = findViewById(R.id.ibloginEnter);

        // ----------------------

        // EVENTO PARA IR A RECUPERAR CONTRASEÑA
        login_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_recuperar.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA IR A REGISTRAR USUARIO
        login_registrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_usuario.class);
                startActivity(intent);
            }
        });

        // EVENTO PARA LIMPIAR CAMPOS
        login_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_usuario.setText("");
                login_contrasena.setText("");
            }
        });

        // EVENTO PARA INICIAR SESIÓN
        login_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!login_usuario.getText().toString().isEmpty() && !login_contrasena.getText().toString().isEmpty()) {

                    db.collection("Usuarios").whereEqualTo("usuario", login_usuario.getText().toString()).whereEqualTo("contraseña", login_contrasena.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().size() > 0) {

                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    boolean registrar_rol = document.getBoolean("rol");

                                    Intent intent;

                                    if (registrar_rol) {
                                        intent = new Intent(getApplicationContext(), MenuActivity_v1.class);

                                    } else {
                                        intent = new Intent(getApplicationContext(), MenuActivity_v2.class);
                                    }

                                    startActivity(intent);

                                    Toast.makeText(MainActivity.this, "INGRESO EXITOSO", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(MainActivity.this, "DATOS INCORRECTOS", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "DATOS INCORRECTOS", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "INGRESA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}