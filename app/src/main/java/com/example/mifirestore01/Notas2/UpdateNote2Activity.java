package com.example.mifirestore01.Notas2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.mifirestore01.Note;
import com.example.mifirestore01.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class UpdateNote2Activity extends AppCompatActivity {

    private String noteId;
    private DatabaseReference databaseReference;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private Button buttonUpdate;

    Note miNota2 = new Note();

    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //compartir
    private Button btnCompartir;
    private EditText etCompartir;
    private DatabaseReference dbRefCompartir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note2);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.modificar_nota);

        editTextTitle = findViewById(R.id.edit_text_title2);
        editTextDescription = findViewById(R.id.edit_text_description2);
        numberPickerPriority = findViewById(R.id.number_picker_priority2);
//        buttonUpdate = findViewById(R.id.btn_update2);

        numberPickerPriority.setMaxValue(1);
        numberPickerPriority.setMaxValue(10);

        databaseReference = getInstance().getReference();
        noteId = getIntent().getStringExtra("noteId");


        obtainDataNotes();

        //comaprtir
        dbRefCompartir = getInstance().getReference("Compartido");
        etCompartir = findViewById(R.id.edit_text_emailTo);
        btnCompartir = findViewById(R.id.btnCompartirNota);
        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etCompartir.getText().toString().isEmpty()) {

                    String email = etCompartir.getText().toString();
                    ShareNote notaCompartida = new ShareNote(noteId, email, firebaseUser.getUid());
                    dbRefCompartir.child(getAlphaNumericString(16)).setValue(notaCompartida);

                }
            }
        });

//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateNotes();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:

                updateNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void obtainDataNotes() {

        getInstance().getReference("Nutas2").child(firebaseUser.getUid()).child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miNota2 = dataSnapshot.getValue(Note.class);
                if (miNota2 != null) {
                    editTextTitle.setText(miNota2.getTitle());
                    editTextDescription.setText(miNota2.getDescription());
                    numberPickerPriority.setValue(miNota2.getPriority());
                }
                //Note nota2 = metodoDatos(firebaseUser, noteId);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void updateNotes() {

        miNota2.setTitle(editTextTitle.getText().toString());
        miNota2.setDescription(editTextDescription.getText().toString());
        miNota2.setPriority(numberPickerPriority.getValue());

        String title = editTextTitle.getText().toString();
        if (title.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_TituloVacios, Toast.LENGTH_SHORT).show();
            return;
        }

        getInstance().getReference("Nutas2").child(firebaseUser.getUid()).child(noteId).setValue(miNota2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("DEBUG UpdateNotes", " Se ha actualizado los valores");
            }
        });

//        mFirestore.collection("Notebook").document(noteId).update(map);
        finish();
    }

    public static String getAlphaNumericString(int n) {


        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
