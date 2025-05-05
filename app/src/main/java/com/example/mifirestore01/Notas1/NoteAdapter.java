package com.example.mifirestore01.Notas1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mifirestore01.Note;
import com.example.mifirestore01.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>{

    private OnItemClickListener listener;

    private Context contexto;
    List<Note> notas;

    public NoteAdapter(Context context, List<Note> notas){
        this.contexto = context;
        this.notas = notas;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent,false);
        NoteHolder holder = new NoteHolder(vista);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteHolder holder, final int position) {
        final Note nota = notas.get(position);
        holder.textViewTitle.setText(nota.getTitle());
        holder.textViewDescription.setText(nota.getDescription());
        holder.getTextViewPriority.setText(String.valueOf(nota.getPriority()));
//        Picasso.get().load(nota.getImageUrl()).into(holder.imageView);
        Picasso.with(contexto).load(nota.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.i("DEBUG RECYCLER VIEW", "Titulo: "+nota.getTitle());
                Intent i = new Intent(contexto, UpdateNoteActivity.class);
                i.putExtra("noteId", nota.getIdNote());
                contexto.startActivity(i);
            }
        });

    }
    //Funciones.setActividadEnUso(true);
//            Intent intentChat = new Intent(this, UpdateNoteActivity.class);
//            intentChat.putExtra("userID",usuario.getId());
//            context.startActivity(intentChat);

    @Override
    public int getItemCount() {

        if(notas != null){
            return notas.size();
        }else{
            return 0;
        }

    }

    public String deleteItem(int position){
        // getSnapshots().getSnapshot(position).getReference().delete();
        String id = notas.get(position).getIdNote();
        notas.remove(position);
        notifyItemRemoved(position);

        return id;
    }

//    public String deleteImage(int position){
//        final Note nota = notas.get(position);
//        String idImage = nota.getImageUrl();
////        notas.remove(position);
////        notifyItemRemoved(po);
//        return idImage;
//    }

    public class NoteHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewDescription;
        TextView getTextViewPriority;

        //imagen
        ImageView imageView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            getTextViewPriority = itemView.findViewById(R.id.text_view_priority);
            imageView = itemView.findViewById(R.id.imageNoteItem);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
////                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
////                    if (position != RecyclerView.NO_POSITION && listener != null){
////                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
////                    }
//                }
//            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
