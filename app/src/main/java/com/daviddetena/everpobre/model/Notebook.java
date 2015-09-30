package com.daviddetena.everpobre.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notebook {

    private long id;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private List<Note> notes;


    public Notebook(String name){
        this.name = name;
    }


    // Lazy getter (like a singleton)
    public List<Note> allNotes(){
        if(this.notes == null){
            this.notes = new ArrayList<Note>();
        }
        return this.notes;
    }


    /**
     * This method adds a non-null Note: linter warns in case of null value
     * @param note the note to add
     */
    public void addNote(@NonNull final Note note){
        if(note != null){
            // Always use lazy load of notes. 1 notebook -> N notes
            allNotes().add(note);
            note.setNotebook(this);
        }
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addNote(String text) {
        Note note = new Note(this, text);
        allNotes().add(note);
    }
}
