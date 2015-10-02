package com.daviddetena.everpobre.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.db.DBHelper;

import java.lang.ref.WeakReference;
import java.util.Date;

import static com.daviddetena.everpobre.model.db.DBConstants.*;  // importo estáticos

public class NotebookDAO implements DAOPersistable<Notebook>{

    // Referencia débil. Accedemos al elemento mediante .get() en vez de directamente
    private final WeakReference<Context> context;
    public static final String[] allColumns = {
            KEY_NOTEBOOK_ID,
            KEY_NOTEBOOK_NAME,
            KEY_NOTEBOOK_CREATION_DATE,
            KEY_NOTEBOOK_MODIFICATION_DATE
    };

    /**
     * Inyectamos dependencia del contexto
     * @param context
     */
    public NotebookDAO(@NonNull Context context) {
        this.context = new WeakReference<Context>(context);
    }


    @Override
    public long insert(@NonNull Notebook data) {
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
            id = db.insert(TABLE_NOTEBOOK, null, getContentValues(data));
            //data.setId(id);
            db.setTransactionSuccessful();
        }
        finally{
            db.endTransaction();
            dbHelper.close();
        }

        return id;
    }


    /**
     * Método de conveniencia para poner en el contexto los datos necesarios de una libreta
     * @param notebook
     * @return
     */
    private ContentValues getContentValues(Notebook notebook) {

        if(notebook.getCreationDate() == null){
            notebook.setCreationDate(new Date());
        }

        if(notebook.getModificationDate() == null){
            notebook.setModificationDate(new Date());
        }

        ContentValues content = new ContentValues();
        content.put(KEY_NOTEBOOK_NAME, notebook.getName());
        content.put(KEY_NOTEBOOK_CREATION_DATE, DBHelper.convertDateToLong(notebook.getCreationDate()));
        content.put(KEY_NOTEBOOK_MODIFICATION_DATE, DBHelper.convertDateToLong(notebook.getModificationDate()));

        return content;

    }

    @Override
    public void update(long id, @NonNull Notebook data) {
        if(data == null){
            return;
        }

        // Instancia compartida DBHelper para el contexto
        final DBHelper dbHelper = DBHelper.getInstance(context.get());

        // Convención para conectar con la DB a través del dbHelper
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        db.beginTransaction();

        try{
            //db.update(TABLE_NOTEBOOK, getContentValues(data), KEY_NOTEBOOK_ID + "=" + id, null);
            db.update(TABLE_NOTEBOOK, getContentValues(data), KEY_NOTEBOOK_ID + "=?", new String[]{"" + id});
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
            db.delete(TABLE_NOTEBOOK, null, null);
        }
        else{
            // Borramos la libreta del id indicado
            db.delete(TABLE_NOTEBOOK, KEY_NOTEBOOK_ID + "=?", new String[]{"" + id});
        }

        db.close();
    }

    /**
     * Método de utilidad para borrar una nota por su Notebook, llamando al delete(id)
     * @param data
     */
    @Override
    public void delete(@NonNull Notebook data) {
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
        Cursor cursor = db.query(TABLE_NOTEBOOK, allColumns, null, null, null, null, KEY_NOTEBOOK_ID);
        return cursor;
    }

    @Override
    public Notebook query(long id) {
        Notebook notebook = null;       // hay que inicializarla siempre

        final DBHelper dbHelper = DBHelper.getInstance(context.get());
        SQLiteDatabase db = DBHelper.getDb(dbHelper);

        final String whereClause = KEY_NOTEBOOK_ID + "=" + id;
        // Obtenemos cursor con query para la libreta que se busca
        Cursor cursor = db.query(TABLE_NOTEBOOK, allColumns, whereClause, null, null, null, KEY_NOTEBOOK_ID);

        if(cursor!=null){
            if(cursor.getCount() > 0){
                // Nos movemos al primer elemento del cursor
                cursor.moveToFirst();
                notebook = notebookFromCursor(cursor);
            }
        }

        cursor.close();
        db.close();

        return notebook;
    }

    /**
     * Método que crea un objeto Notebook a partir del elemento que apunta el cursor de la consulta
     * DB.
     * @param cursor
     * @return
     */
    @NonNull
    public static Notebook notebookFromCursor(Cursor cursor) {
        // Creamos nuevo notebook con nombre extrayendo del cursor la posición de la columna
        // name del mismo
        Notebook notebook;
        notebook = new Notebook(cursor.getString(cursor.getColumnIndex(KEY_NOTEBOOK_NAME)));
        notebook.setId(cursor.getLong(cursor.getColumnIndex(KEY_NOTEBOOK_ID)));

        long creationDate = cursor.getLong(cursor.getColumnIndex(KEY_NOTEBOOK_CREATION_DATE));
        long modificationDate = cursor.getLong(cursor.getColumnIndex(KEY_NOTEBOOK_MODIFICATION_DATE));

        // Convertimos de long a date a partir del método del DBHelpers
        notebook.setCreationDate(DBHelper.convertLongToDate(creationDate));
        notebook.setCreationDate(DBHelper.convertLongToDate(modificationDate));
        return notebook;
    }
}
