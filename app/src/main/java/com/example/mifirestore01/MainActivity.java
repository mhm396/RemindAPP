package com.example.mifirestore01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mifirestore01.Usuarios.LoginActivity;
import com.example.mifirestore01.Usuarios.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText etx_Usuario;
    private EditText etx_Email;
    private EditText etx_Password;
    private Button btn_Aceptar;
    private Button btn_IniciarSesion;


    private String user = "";
    private String email = "";
    private String password = "";

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etx_Usuario = findViewById(R.id.editTextUsuario);
        etx_Email = findViewById(R.id.editTextEmail);
        etx_Password = findViewById(R.id.editTextPassword);
        btn_Aceptar = findViewById(R.id.buttonAceptar);
        btn_IniciarSesion = findViewById(R.id.buttonIniciarSesion);

        btn_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        auth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = etx_Usuario.getText().toString();
                email = etx_Email.getText().toString();
                password = etx_Password.getText().toString();

                if (!user.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    if (password.length() >= 6) {

                        registrarUsuario();

                    } else {
                        Toast.makeText(MainActivity.this, R.string.toast_passwCaracter, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_CamposVacios, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void registrarUsuario() {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    Map<String, Object> map = new HashMap<>();
                    map.put("user", user);
                    map.put("email", email);
                    map.put("password", password);

                    String id = auth.getCurrentUser().getUid();
                    databaseReference.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.toast_invalidData, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_loginFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();
        }
    }

}
