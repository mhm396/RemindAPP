package com.example.mifirestore01.Notas1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mifirestore01.Notas2.NoteList2Activity;
import com.example.mifirestore01.Note;
import com.example.mifirestore01.R;
import com.example.mifirestore01.RemindTest.MainPage;
import com.example.mifirestore01.Usuarios.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class NoteListActivity extends AppCompatActivity {

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private CollectionReference notebookRef = db.collection("Notebook");

    // NoteAdapter adapter;
    DatabaseReference databaseReference;
    final private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    //changes
    RecyclerView rv;
    List<Note> notas;
    NoteAdapter myAdapter;

    BottomNavigationView bottomNavigationView;

    private ImageView imageView;
    StorageReference StorageRef;

    Note noteDelete;
    String imageStorageRefDelete = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        setTitle(R.string.titulo_Prestamos);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteListActivity.this, NewNoteActicity.class));
            }
        });

        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));

        notas = new ArrayList<>();
        myAdapter = new NoteAdapter(this, notas);
        rv.setAdapter(myAdapter);

        getInstance().getReference("Notas").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notas.removeAll(notas);

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Note nota = data.getValue(Note.class);
                    notas.add(nota);

//                    String carName = dataSnapshot.child("ImageName").getValue().toString();
//                    String ImageUrl = dataSnapshot.child("imagen").child("ImageUrl").getValue().toString();
//                    Picasso.get().load(ImageUrl).into(imageView);

                }
                setUpRecyclerView();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StorageRef = FirebaseStorage.getInstance().getReference();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_notes1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_notes1:
//                        startActivity(new Intent(getApplicationContext(), NoteListActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_reminder:
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_notes2:
                        startActivity(new Intent(getApplicationContext(), NoteList2Activity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }
                return false;
            }
        });

    }

    private void setUpRecyclerView() {
//        Query query  = notebookRef.orderBy("priority", Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
//                .setQuery(query, Note.class)
//                .build();

        // adapter = new NoteAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(myAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final String id = myAdapter.deleteItem(viewHolder.getAdapterPosition());

//                String idImage = myAdapter.deleteImage(viewHolder.getAdapterPosition());

                getInstance().getReference("Notas").child(firebaseUser.getUid()).child(id).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        noteDelete = new Note();
                        noteDelete = dataSnapshot.getValue(Note.class);
//                        String noteImageKeyAux = null;
//                        try {
//
//
//
//                            noteImageKeyAux = noteDelete.getImageKey();
//
//                        }catch (NullPointerException ioe){
//                            Log.d("EXCEPCION",ioe.toString());
//                        }


                        assert noteDelete != null;
//                        Log.d("VALOR KEY IMAGE", noteImageKeyAux);

                        if (noteDelete != null) {
//                            imageStorageRefDelete = noteImageKeyAux;
                            imageStorageRefDelete = noteDelete.getImageKey();
                        }

                        if (imageStorageRefDelete != null) {
                            imageStorageRefDelete += ".jpg";
                            StorageRef.child("NoteImage").child(firebaseUser.getUid()).child(id).child(imageStorageRefDelete).delete();
                        }

                        getInstance().getReference("Notas").child(firebaseUser.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(NoteListActivity.this, R.string.toast_Delete, Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("EXCEPCION", databaseError.toException());
                    }


                });


//                Log.d("String_ID_VALUE",idImage);

                //   databaseReference.getInstance().getReference("Notas").child(firebaseUser.getUid()).child(id).removeValue();

//                imageStorageRefDelete[0] += ".jpg";
//                Log.d("String_ID_VALUE", imageStorageRefDelete[0]);
                //Log.d("INFO NoteListActivity","ARRAY NOTAS "+notas.size()+" Adapter Position "+viewHolder.getAdapterPosition());
                //String id = notas.get(viewHolder.getAdapterPosition()).getId();


            }
        }).attachToRecyclerView(recyclerView);

        myAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                final String id = documentSnapshot.getId();

                Intent intent = new Intent(NoteListActivity.this, UpdateNoteActivity.class);
                intent.putExtra("noteId", id);
                startActivity(intent);

//                Toast.makeText(NoteListActivity.this, "Posicion: "+position+"\nID: "+id, Toast.LENGTH_SHORT).show();

                //String path = documentSnapshot.getReference().getPath();
                //para actualizar o borrar
                // documentSnapshot.getReference();

                // startActivity(new Intent(MainActivity.this, NewNoteActicity.class));
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        myAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        myAdapter.stopListening();
//    }
}
