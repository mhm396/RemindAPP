package com.example.mifirestore01.Notas1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;

import com.example.mifirestore01.Note;
import com.example.mifirestore01.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class UpdateNoteActivity extends AppCompatActivity {

    private String noteId;
    private DatabaseReference databaseReference;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private Button buttonUpdate;

    //imagen update
    private ImageView imageView;
    Uri imageUri;
    boolean isImageAdded = false;
    StorageReference StorageRef;

    Note miNota = new Note();

    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.modificar_nota);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        //buttonUpdate = findViewById(R.id.btn_update);
        imageView = findViewById(R.id.imageView);

        numberPickerPriority.setMaxValue(1);
        numberPickerPriority.setMaxValue(10);

        databaseReference = getInstance().getReference("Notas");
        StorageRef = FirebaseStorage.getInstance().getReference();
        noteId = getIntent().getStringExtra("noteId");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        obtainDataNotes();

//        if (isImageAdded) {
//            obtainImageNote();
//        }


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

    private void obtainImageNote() {

        getInstance().getReference("Notas").child(firebaseUser.getUid()).child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miNota = dataSnapshot.getValue(Note.class);
                if (miNota != null) {
//                    String ImageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                    String ImageUrl = miNota.getImageUrl();
                    Log.i("IMAGEN_URL", ImageUrl);
//                    Picasso.get().load(ImageUrl).into(imageView);
//                    Picasso.with(getApplicationContext()).load(ImageUrl).into(imageView);
//                    imageView.setImageURI(imageUri);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtainDataNotes() {

        databaseReference.child(firebaseUser.getUid()).child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                miNota = dataSnapshot.getValue(Note.class);
                if (miNota != null) {
                    editTextTitle.setText(miNota.getTitle());
                    editTextDescription.setText(miNota.getDescription());
                    numberPickerPriority.setValue(miNota.getPriority());
//                    imageView.setImageURI(Uri.parse(miNota.getImageUrl()));

                    if (miNota.getImageUrl() != null) {
//                        isImageAdded = true;
                        Picasso.with(getApplicationContext()).load(miNota.getImageUrl()).into(imageView);
                    } else {
                        isImageAdded = false;
                    }

                }
                //Note nota2 = metodoDatos(firebaseUser, noteId);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /*
    public static Note metodoDatos(FirebaseUser firebaseUser, String noteId){



        return miNota;
    }

     */

    private void updateNotes() {

        miNota.setTitle(editTextTitle.getText().toString());
        miNota.setDescription(editTextDescription.getText().toString());
        miNota.setPriority(numberPickerPriority.getValue());

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_CamposVacios, Toast.LENGTH_SHORT).show();
            return;
        }

        getInstance().getReference("Notas").child(firebaseUser.getUid()).child(noteId).setValue(miNota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("DEBUG UpdateNotes", " Se ha actualizado los valores");
            }
        });

//        mFirestore.collection("Notebook").document(noteId).update(map);
        finish();
    }

    public void uploadImage(Uri imageUri) {

        final StorageReference storageUserProfileRef = StorageRef.child("NoteImage").child(firebaseUser.getUid()).child(miNota.getIdNote());
        UploadTask uploadTask = storageUserProfileRef.putFile(imageUri);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageUserProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        // getting image uri and converting into string
                        miNota.setImageUrl(downloadUrl.toString());
                        databaseReference.child(firebaseUser.getUid()).child(miNota.getIdNote()).setValue(miNota);
                    }
                });

            }
        });

//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                storageUserProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        // getting image uri and converting into string
//                        miNota.setImageUrl(uri);
//                        referenceUserDataBase.child(usuarioObject.getId()).setValue(usuarioObject).addOnCompleteListener(task -> {
//                            if(task.isSuccessful()){
//                                Log.d("DEBUG PerfilActivity","Se ha Actualizado la foto de perfil");
//                                Toast.makeText(getApplicationContext() getResources().getString(R.string.exitoSubirImagenPerfil), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

        StorageRef.child("NoteImage").child(firebaseUser.getUid()).child(miNota.getIdNote()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child("NoteImage").child(firebaseUser.getUid()).child(miNota.getIdNote()).child(miNota.getImageKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String imageUrl = uri.toString();
                        miNota.setImageUrl(imageUrl);
                        databaseReference.child("Notas").child(firebaseUser.getUid()).child(miNota.getIdNote()).setValue(miNota);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
//            isImageAdded = true;

//            if (isImageAdded == false) {
                imageView.setImageURI(imageUri);
                uploadImage(imageUri);
//            }


        }
    }
}
