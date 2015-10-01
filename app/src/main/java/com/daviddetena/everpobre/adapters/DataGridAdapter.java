package com.daviddetena.everpobre.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.model.db.DBConstants;

public class DataGridAdapter extends CursorAdapter{

    private LayoutInflater layoutInflater;

    public DataGridAdapter(Context context, Cursor c) {
        super(context, c);

        // Relleno el layoutInflater a partir del contexto que se me pasa
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Como el cellForRowAtIndexPath. Creamos la vista de celda
     * @param context
     * @param cursor
     * @param viewGroup
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // Cargamos la vista personalizada definida para la celda e "inflamos" la vista con este
        // layout personalizado. El viewGroup en este caso es el grid
        // true sirve para añadir un xml a un LinearLayout, es decir, lo estoy añadiendo al fragmento
        // false si estoy utilizando un listView o GridView con adapter que ya maneja el pintado
        // de los widgets
        View v = layoutInflater.inflate(R.layout.view_notebook, viewGroup, false);
        return v;
    }


    /**
     * Rellena la celda con datos
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Obtenemos widgets del layout a partir de su id
        ImageView itemImage = (ImageView) view.findViewById(R.id.icon_notebook);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_notebook_name);

        // Cursor posicionado en el registro i-esimo. Creamos objeto Notebook a partir del cursor
        // que apunta al registro de la DB
        Notebook notebook = NotebookDAO.notebookFromCursor(cursor);

        // Pintamos en pantalla
        txtTitle.setText(notebook.getName());
    }

}
