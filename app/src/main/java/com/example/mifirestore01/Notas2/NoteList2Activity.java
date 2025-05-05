package com.example.mifirestore01.Notas2;

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
import android.widget.Toast;

import com.example.mifirestore01.Notas1.NoteListActivity;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class NoteList2Activity extends AppCompatActivity {


    final private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference;
    Query databaseReferenceQuery;

    //changes
    RecyclerView rv;
    List<Note> notas;
    NoteAdapter2 myAdapter;

    BottomNavigationView bottomNavigationView;

    //Compartida
    List<ShareNote> listaNotasCompartidas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list2);
        setTitle(R.string.titulo_Notas);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note2);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteList2Activity.this, NewNote2Activity.class));
            }
        });

        rv = findViewById(R.id.recycler_view2);
        rv.setLayoutManager(new LinearLayoutManager(this));

        notas = new ArrayList<>();
        myAdapter = new NoteAdapter2(this, notas);
        rv.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

//        databaseReference.child("Nutas2").child(id).setValue(map).addOnCompleteListener(

        databaseReference.child("Nutas2").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notas.removeAll(notas);

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Note nota = data.getValue(Note.class);

//                   int valorPriority = nota.getPriority();
//                   Log.d("VALOR_PRIORIDAD", String.valueOf(valorPriority));
                    notas.add(nota);
                }
                setUpRecyclerView();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_notes2);

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
                        startActivity(new Intent(getApplicationContext(), NoteListActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_reminder:
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_notes2:
//                        startActivity(new Intent(getApplicationContext(), NoteList2Activity.class));
//                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    public void recogerNotasCompartidas() {

        DatabaseReference dbReference = (DatabaseReference) getInstance().getReference("Compartido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaNotasCompartidas.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    ShareNote shareNote = data.getValue(ShareNote.class);
                    listaNotasCompartidas.add(shareNote);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        RecyclerView recyclerView = findViewById(R.id.recycler_view2);
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

                String id = myAdapter.deleteItem(viewHolder.getAdapterPosition());
                Log.d("String ID VALUE", id);

                //   databaseReference.getInstance().getReference("Notas").child(firebaseUser.getUid()).child(id).removeValue();


                //Log.d("INFO NoteListActivity","ARRAY NOTAS "+notas.size()+" Adapter Position "+viewHolder.getAdapterPosition());
                //String id = notas.get(viewHolder.getAdapterPosition()).getId();

                getInstance().getReference("Nutas2").child(firebaseUser.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(NoteList2Activity.this, R.string.toast_Delete, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);


        myAdapter.setOnItemClickListener(new NoteAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                final String id = documentSnapshot.getId();

                Intent intent = new Intent(NoteList2Activity.this, UpdateNote2Activity.class);
                intent.putExtra("noteId", id);
                startActivity(intent);

                Toast.makeText(NoteList2Activity.this, "Posicion: " + position + "\nID: " + id, Toast.LENGTH_SHORT).show();

                //String path = documentSnapshot.getReference().getPath();
                //para actualizar o borrar
                // documentSnapshot.getReference();

                // startActivity(new Intent(MainActivity.this, NewNoteActicity.class));
            }
        });
    }
}
