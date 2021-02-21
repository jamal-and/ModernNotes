package com.example.modernnotes.listeners;

import com.example.modernnotes.entites.Note;

public interface NotesListener {
    void onNoteClicked(Note note,int position);
}
