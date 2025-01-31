package com.example.modernnotes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modernnotes.R;
import com.example.modernnotes.entites.Note;
import com.example.modernnotes.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    List<Note>notes;
    NotesListener notesListener;
    Timer timer;
    List<Note> notesSource;

    public NoteAdapter(List<Note> notes,NotesListener notesListener) {
        this.notes = notes;
        this.notesListener=notesListener;
        notesSource=notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(notes.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textSubtitle,textDateTime;
        LinearLayout layoutNote;
        ImageView imageNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textSubtitle=itemView.findViewById(R.id.textSubtitle);
            textDateTime=itemView.findViewById(R.id.textDateTime);
            layoutNote=itemView.findViewById(R.id.layoutNote);
            imageNote=itemView.findViewById(R.id.imageNote);

        }
        void setNote(Note note){
            textTitle.setText(note.getTitle());
            if (note.getNoteText().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }else {
                textSubtitle.setText(note.getNoteText());
            }
            if (note.getColor().equals("#fafafa")){
                textTitle.setTextColor(Color.parseColor("#1f1f1f"));
                textSubtitle.setTextColor(Color.parseColor("#1f1f1f"));
                textDateTime.setTextColor(Color.parseColor("#1f1f1f"));
            }else{
                textTitle.setTextColor(Color.parseColor("#fafafa"));
                textSubtitle.setTextColor(Color.parseColor("#fafafa"));
                textDateTime.setTextColor(Color.parseColor("#fafafa"));
            }
            textDateTime.setText(note.getDateTime());
            GradientDrawable gradientDrawable=(GradientDrawable)layoutNote.getBackground();
            if (note.getColor()!=null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath()!=null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else {
                imageNote.setVisibility(View.GONE);
            }
        }

    }
    public void searchNotes(final String searchKeyWord){
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyWord.trim().isEmpty()){
                    notes=notesSource;
                }else {
                    ArrayList<Note> temp=new ArrayList<>();
                    for (Note note: notesSource){
                        if (note.getTitle().toLowerCase().contains(searchKeyWord.toLowerCase())
                        ||  note.getSubtitle().toLowerCase().contains(searchKeyWord.toLowerCase())
                        ||  note.getNoteText().toLowerCase().contains(searchKeyWord.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes=temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }
    public void cancelTimer(){
        if (timer!=null){
            timer.cancel();
        }
    }
}
