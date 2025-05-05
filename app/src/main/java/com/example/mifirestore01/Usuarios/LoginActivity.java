package com.example.mifirestore01.Usuarios;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mifirestore01.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText etx_Email;
    private EditText etx_Password;
    private Button btn_Ingresar;

    private String email = "";
    private String password = "";

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        etx_Email = findViewById(R.id.editTextEmail);
        etx_Password = findViewById(R.id.editTextPassword);
        btn_Ingresar = findViewById(R.id.buttonIngresar);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etx_Email.getText().toString();
                password = etx_Password.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){

                    if (password.length() >= 6){

                        iniciarUsuario();

                    }else{
                        Toast.makeText(LoginActivity.this, R.string.toast_passwCaracter, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, R.string.toast_CamposVacios, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void iniciarUsuario() {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, R.string.toast_loginFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

