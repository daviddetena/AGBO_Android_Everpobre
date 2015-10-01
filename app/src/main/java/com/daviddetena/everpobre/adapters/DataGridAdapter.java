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

import butterknife.Bind;
import butterknife.ButterKnife;

public class DataGridAdapter extends CursorAdapter{

    private LayoutInflater layoutInflater;
    private Cursor cursor;

    public DataGridAdapter(Context context, Cursor c) {
        super(context, c);

        // Relleno el layoutInflater a partir del contexto que se me pasa
        this.layoutInflater = LayoutInflater.from(context);
        this.cursor = c;
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
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }


    /**
     * Con este método tendríamos la unión de newView + bindView. De esta forma puedo comprobar si
     * ya hay una vista anterior para recuperarla y no tenerla que repintarla
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Comprobamos si hay ViewHolder utilizado previamente.
        ViewHolder holder;

        if(view != null){
            // Si ya existe, recupero el ViewHolder que guardé la primera vez
            holder = (ViewHolder) view.getTag();
        }
        else{
            // Vista nula. La creo
            // Cargamos la vista personalizada definida para la celda e "inflamos" la vista con este
            // layout personalizado. El viewGroup en este caso es el grid
            // true sirve para añadir un xml a un LinearLayout, es decir, lo estoy añadiendo al fragmento
            // false si estoy utilizando un listView o GridView con adapter que ya maneja el pintado
            // de los widgets
            view = layoutInflater.inflate(R.layout.view_notebook, parent, false);
            holder = new ViewHolder(view);

            // Se pinta por primera vez la celda y se le asigna un tag para saberlo
            view.setTag(holder);
        }

        // Cursor posicionado en el registro i-esimo. Creamos objeto Notebook a partir del cursor
        // que apunta al registro de la DB
        Notebook notebook = NotebookDAO.notebookFromCursor(cursor);

        // Pintamos en pantalla el texto del notebook que maneja el ViewHolder
        holder.txtTitle.setText(notebook.getName());

        return view;
    }



    /**
     * Utilizamos ViewHolder como un "truco" para mejorar el rendimiento del pintado de celdas.
     * Todos los widgets o controles visuales los manejará esta clase
     */
    static class ViewHolder{

        // Butterknife hace el findViewById de forma automática con el @Bind. Como los @IBOutlet
        @Bind(R.id.txt_notebook_name) TextView txtTitle;
        @Bind(R.id.icon_notebook) ImageView itemImage;

        /**
         * Vista donde se está creando el viewHolder
         * @param view
         */
        public ViewHolder(View view) {
            // Inyectamos vista con butterknife. Dentro de la vista V va a buscar los id de dicha vista
            // y los enlaza
            ButterKnife.bind(this, view);
        }
    }

}
