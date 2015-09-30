package com.daviddetena.everpobre.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daviddetena.everpobre.model.Note;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.db.DBHelper;

import java.lang.ref.WeakReference;
import java.util.Date;

import static com.daviddetena.everpobre.model.db.DBConstants.*;


public class NoteDAO implements DAOPersistable<Note>{

    // Referencia débil. Accedemos al elemento mediante .get() en vez de directamente
    private final WeakReference<Context> context;
    public static final String[] allColumns = {
            KEY_NOTE_ID,
            KEY_NOTE_TEXT,
            KEY_NOTE_CREATION_DATE,
            KEY_NOTE_MODIFICATION_DATE,
            KEY_NOTE_PHOTO_URL,
            KEY_NOTE_NOTEBOOK,
            KEY_NOTE_LATITUDE,
            KEY_NOTE_LONGITUDE,
            KEY_NOTE_HAS_COORDINATES,
            KEY_NOTE_ADDRESS
    };

    /**
     * Inyectamos dependencia del contexto
     * @param context
     */
    public NoteDAO(@NonNull Context context) {
        this.context = new WeakReference<Context>(context);
    }


    @Override
    public long insert(@NonNull Note data) {
        if(data == null){
            return DBHelper.INVALID_ID;
        }

        // Instancia compartida DBHelper para el contexto y obtención de la db
        final DBHelper dbHelper = DBHelper.getInstance(context.get());
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        db.beginTransaction();
        long id = DBHelper.INVALID_ID;

        try{
            // El null hack es para cuando se crea un registro totalmente vacío
            id = db.insert(TABLE_NOTE, null, getContentValues(data));
            //data.setId(id);
            db.setTransactionSuccessful();  // commit
        }
        finally{
            db.endTransaction();
            dbHelper.close();
        }

        return id;
    }


    /**
     * Método de conveniencia para poner en el contexto los datos necesarios de una libreta
     * @param note
     * @return
     */
    private ContentValues getContentValues(Note note) {

        if(note.getCreationDate() == null){
            note.setCreationDate(new Date());
        }

        if(note.getModificationDate() == null){
            note.setModificationDate(new Date());
        }

        ContentValues content = new ContentValues();
        content.put(KEY_NOTE_TEXT, note.getText());
        content.put(KEY_NOTE_CREATION_DATE, DBHelper.convertDateToLong(note.getCreationDate()));
        content.put(KEY_NOTE_MODIFICATION_DATE, DBHelper.convertDateToLong(note.getModificationDate()));
        content.put(KEY_NOTE_PHOTO_URL, note.getPhotoUrl());
        content.put(KEY_NOTE_NOTEBOOK, String.format("%d", note.getNotebook()));
        content.put(KEY_NOTE_LATITUDE, String.format("%f", note.getLatitude()));
        content.put(KEY_NOTE_LONGITUDE, String.format("%f", note.getLongitude()));

        boolean hasCoord = note.isHasCoordinates();
        content.put(KEY_NOTE_HAS_COORDINATES, String.format("%d", DBHelper.convertBooleanToInt(hasCoord)));
        content.put(KEY_NOTE_ADDRESS, note.getAddress());

        return content;

    }

    @Override
    public void update(long id, @NonNull Note data) {
        if(data == null){
            return;
        }

        // Instancia compartida DBHelper para el contexto
        final DBHelper dbHelper = DBHelper.getInstance(context.get());

        // Convención para conectar con la DB a través del dbHelper
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        db.beginTransaction();

        try{
            //db.update(TABLE_NOTEBOOK, getContentValues(data), KEY_NOTE_ID + "=" + id, null);
            db.update(TABLE_NOTE, getContentValues(data), KEY_NOTE_ID + "=?", new String[]{"" + id});
            db.setTransactionSuccessful();
        }
        finally{
            db.endTransaction();
            dbHelper.close();
        }
    }

    @Override
    public void delete(long id) {
        // Instancia compartida DBHelper para el contexto y obtención de la db
        final DBHelper dbHelper = DBHelper.getInstance(context.get());
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        if(id == DBHelper.INVALID_ID){
            // Borramos todos
            db.delete(TABLE_NOTE, null, null);
        }
        else{
            // Borramos la libreta del id indicado
            db.delete(TABLE_NOTE, KEY_NOTE_ID + "=?", new String[]{"" + id});
        }

        db.close();
    }

    /**
     * Método de utilidad para borrar una nota por su Notebook, llamando al delete(id)
     * @param data
     */
    @Override
    public void delete(@NonNull Note data) {
        if(data != null){
            delete(data.getId());
        }
    }

    @Override
    public void deleteAll() {
        delete(DBHelper.INVALID_ID);
    }

    @Nullable
    @Override
    public Cursor queryCursor() {
        final DBHelper dbHelper = DBHelper.getInstance(context.get());
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        // Obtenemos cursor con el query de todas las libretas. Orden obligatorio, en este caso, por ID
        Cursor cursor = db.query(TABLE_NOTE, allColumns, null, null, null, null, KEY_NOTE_ID);
        return cursor;
    }

    @Override
    public Note query(long id) {
        Note note = null;       // hay que inicializarla siempre

        final DBHelper dbHelper = DBHelper.getInstance(context.get());
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        final String whereClause = KEY_NOTE_ID + "=" + id;
        // Obtenemos cursor con query para la libreta que se busca
        Cursor cursor = db.query(TABLE_NOTE, allColumns, whereClause, null, null, null, KEY_NOTE_ID);

        if(cursor!=null){
            if(cursor.getCount() > 0){
                // Nos movemos al primer elemento del cursor
                cursor.moveToFirst();

                // Creamos nuevo notebook con nombre extrayendo del cursor la posición de la columna
                // name del mismo

                // Creamos notebook vacío con el id que tiene la FK de note para añadirle el notebook
                // a la nota
                Notebook notebook = new Notebook("");
                final String notebookId = cursor.getString(cursor.getColumnIndex(KEY_NOTE_NOTEBOOK));
                notebook.setId(Long.parseLong(notebookId));

                note = new Note(notebook, cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT)));
                note.setId(cursor.getLong(cursor.getColumnIndex(KEY_NOTE_ID)));

                long creationDate = cursor.getLong(cursor.getColumnIndex(KEY_NOTE_CREATION_DATE));
                long modificationDate = cursor.getLong(cursor.getColumnIndex(KEY_NOTE_MODIFICATION_DATE));

                note.setCreationDate(DBHelper.convertLongToDate(creationDate));
                note.setCreationDate(DBHelper.convertLongToDate(modificationDate));

                // TODO: terminar mapeo
                //note.setPhotoUrl(cursor.getLong(cursor.getColumnIndex(KEY_NOTE_PHOTO_URL)));



                // Convertimos de long a date a partir del método del DBHelpers


                /*
        content.put(KEY_NOTE_NOTEBOOK, String.format("%d", note.getNotebook()));
        content.put(KEY_NOTE_LATITUDE, String.format("%f", note.getLatitude()));
        content.put(KEY_NOTE_LONGITUDE, String.format("%f", note.getLongitude()));

        boolean hasCoord = note.isHasCoordinates();
        content.put(KEY_NOTE_HAS_COORDINATES, String.format("%d", DBHelper.convertBooleanToInt(hasCoord)));
        content.put(KEY_NOTE_ADDRESS, note.getAddress());
                 */
            }
        }

        cursor.close();
        db.close();

        return note;
    }
}
