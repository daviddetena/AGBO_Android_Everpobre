package com.daviddetena.everpobre.model.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daviddetena.everpobre.model.Note;


public class NoteDAO implements DAOPersistable<Note>{

    @Override
    public long insert(@NonNull Note data) {
        return 0;
    }

    @Override
    public void update(long id, @NonNull Note data) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void delete(@NonNull Note data) {

    }

    @Override
    public void deleteAll() {

    }

    @Nullable
    @Override
    public Cursor queryCursor() {
        return null;
    }

    @Override
    public Note query(long id) {
        return null;
    }
}
