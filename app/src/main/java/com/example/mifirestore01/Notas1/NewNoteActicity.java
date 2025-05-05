package com.example.mifirestore01.Notas1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.DefaultTaskExecutor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.mifirestore01.Note;
import com.example.mifirestore01.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class NewNoteActicity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextEmailTo;
    private NumberPicker numberPickerPriority;
    final private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //cambios

    DatabaseReference databaseReference;

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageViewAdd;
    Uri imageUri;
    boolean isImageAdded = false;
    StorageReference StorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle(R.string.add_nota);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        editTextEmailTo = findViewById(R.id.edit_text_emailTo);

        numberPickerPriority.setMaxValue(1);
        numberPickerPriority.setMaxValue(10);

        //cambios

        databaseReference = FirebaseDatabase.getInstance().getReference();

        StorageRef = FirebaseStorage.getInstance().getReference().child("NoteImage");

        imageViewAdd = findViewById(R.id.imageView);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

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
                String emailTest = editTextEmailTo.getText().toString();
                if (emailTest.isEmpty() || emailTest == null) {

                    saveNote();
                } else {
                    sendMail();
                    saveNote();
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void sendMail() {

        String recipientList = editTextEmailTo.getText().toString();
        String[] recipients = recipientList.split(",");
        String subject = editTextTitle.getText().toString();
        String message = editTextDescription.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));

    }

    private String getDateTime () {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void saveNote() {
        final String title = editTextTitle.getText().toString();
        final String description = editTextDescription.getText().toString();
        final int priority = numberPickerPriority.getValue();




        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, R.string.toast_CamposVacios, Toast.LENGTH_SHORT).show();
            return;
        }

        final String idNota = getAlphaNumericString(16);


        //Parte de la imagen
        if (isImageAdded != false) {
//            databaseReference.child("Notas").child(firebaseUser.getUid()).child(idNota);
            final String key = databaseReference.child("Notas").child(firebaseUser.getUid()).child(idNota).push().getKey();
            StorageRef.child(firebaseUser.getUid()).child(idNota).child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageRef.child(firebaseUser.getUid()).child(idNota).child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            HashMap hashMap=new HashMap();
//                            hashMap.put("ImageName",getAlphaNumericString(6));
//                            hashMap.put("ImageUrl",uri.toString());

                            String imageUrl = uri.toString();

                            databaseReference.child("Notas").child(firebaseUser.getUid()).child(idNota).setValue(new Note(firebaseUser.getUid(), idNota, title, description, priority, imageUrl, key));
//                            databaseReference.child("Notas").child(firebaseUser.getUid()).child(idNota).child("imagen").setValue(imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    Toast.makeText(getApplicationContext(), "Imagen subida correctamente", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                        }
                    });

                }
            });
        } else {

            databaseReference.child("Notas").child(firebaseUser.getUid()).child(idNota).setValue(new Note(firebaseUser.getUid(), idNota, title, description, priority));
        }


        //cambios

//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","hidalgodamurgi@gmail.com", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
//        startActivity(Intent.createChooser(emailIntent,  getApplicationContext().getString(R.string.enviar_mail)));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            imageViewAdd.setImageURI(imageUri);
        }
    }
}
