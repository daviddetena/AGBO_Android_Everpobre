package com.daviddetena.everpobre.model.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daviddetena.everpobre.model.Notebook;

// Genéricos para que se pueda usar con NoteDAO o NotebookDAO
// Métodos de interfaz son siempre públicos
public interface DAOPersistable<T> {
    public long insert(@NonNull T data);
    public void update(long id,@NonNull T data);
    public void delete(long id);
    public void delete(@NonNull T data);
    public void deleteAll();
    public @Nullable Cursor queryCursor();
    public T query(long id);
}
