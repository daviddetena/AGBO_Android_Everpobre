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

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataGridAdapter extends CursorAdapter{

    // Butterknife hace el findViewById de forma automática con el @Bind. Como los @IBOutlet
    @Bind(R.id.txt_notebook_name) TextView txtTitle;
    @Bind(R.id.icon_notebook) ImageView itemImage;

    private LayoutInflater layoutInflater;
    private Cursor dataCursor;

    public DataGridAdapter(Context context, Cursor c) {
        super(context, c);

        // Relleno el layoutInflater a partir del contexto que se me pasa
        this.layoutInflater = LayoutInflater.from(context);
        this.dataCursor = c;
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
        // Vista nula. La creo
        // Cargamos la vista personalizada definida para la celda e "inflamos" la vista con este
        // layout personalizado. El viewGroup en este caso es el grid
        // true sirve para añadir un xml a un LinearLayout, es decir, lo estoy añadiendo al fragmento
        // false si estoy utilizando un listView o GridView con adapter que ya maneja el pintado
        // de los widgets
        View view = layoutInflater.inflate(R.layout.view_notebook, viewGroup, false);

        // Inyectamos contenido de los widgets con Butterknife
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Cursor posicionado en el registro i-esimo. Creamos objeto Notebook a partir del cursor
        // que apunta al registro de la DB y que me da el adapter
        Notebook notebook = NotebookDAO.notebookFromCursor(cursor);

        // Pintamos en pantalla el texto del notebook
        txtTitle.setText(notebook.getName());
    }



}
