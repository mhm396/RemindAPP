package com.example.mifirestore01.Notas2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class NewNote2Activity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    final private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private Task<Void> databaseReference;
    Query databaseReferenceQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note2);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.add_nota);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMaxValue(1);
        numberPickerPriority.setMaxValue(10);


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
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_TituloVacios, Toast.LENGTH_SHORT).show();
            return;
        }

        String idNota = getAlphaNumericString(16);

//        CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("Notebook");
//        notebookRef.add(new Note(firebaseUser.getUid(), idNota, title, description, priority));
//        Toast.makeText(this, "Nota añadida correctamente", Toast.LENGTH_SHORT).show();
//        finish();


        //cambios
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Nutas2").child(firebaseUser.getUid()).child(idNota).setValue(new Note(firebaseUser.getUid(), idNota, title, description, priority));
//        databaseReferenceQuery = getInstance().getReference().child("Nutas2").child(firebaseUser.getUid()).child(idNota).orderByPriority();
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
