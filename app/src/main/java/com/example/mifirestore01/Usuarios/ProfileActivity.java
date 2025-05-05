package com.example.mifirestore01.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mifirestore01.MainActivity;
import com.example.mifirestore01.Notas1.NoteListActivity;
import com.example.mifirestore01.Notas2.NoteList2Activity;
import com.example.mifirestore01.R;
import com.example.mifirestore01.RemindTest.MainPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView tx_Usuario;
    private TextView tx_Email;

    private Button btn_CerraSesion;
    private FirebaseAuth auth;

    //Atributo para coger datos de firebase
    private DatabaseReference databaseReference;
    Button btn_ListarNotas;
    Button btn_ListarNotas2;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tx_Usuario = findViewById(R.id.textViewUsuario);
        tx_Email = findViewById(R.id.textViewEmail);
        btn_CerraSesion = findViewById(R.id.buttonCerrarSesion);

//        btn_ListarNotas = findViewById(R.id.buttonListarNotas);
//        btn_ListarNotas2 = findViewById(R.id.buttonListarNotas2);

//        btn_ListarNotas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, NoteListActivity.class));
//            }
//        });
//
//        btn_ListarNotas2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, NoteList2Activity.class));
//            }
//        });


        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_CerraSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();

            }
        });

        getUserInfo();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
//                        startActivity(new Intent(ProfileActivity.this, NoteListActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_notes1:
                        startActivity(new Intent(ProfileActivity.this, NoteListActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.navigation_reminder:
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.navigation_notes2:
                        startActivity(new Intent(ProfileActivity.this, NoteList2Activity.class));
                        overridePendingTransition(0,0);
                       return true;

                }
                return false;
            }
        });
    }

    private void getUserInfo() {

        String id = auth.getCurrentUser().getUid();
        databaseReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("user").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    tx_Usuario.setText(name);
                    tx_Email.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
