package com.daviddetena.everpobre.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.activities.EditNotebookActivity;
import com.daviddetena.everpobre.activities.ShowNotebookActivity;
import com.daviddetena.everpobre.adapters.DataGridAdapter;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.util.Constants;

public class DataGridFragment extends Fragment {

    /**
     * Creamos interfaz personalizada para detectar eventos sobre el Grid
     */
    public interface OnDataGridFragmentClickListener{

        /**
         * Método cuando se hace click
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id);

        /**
         * Método cuando se hace long click
         * @param parent
         * @param view
         * @param position
         * @param id
         * @return
         */
        public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id);
    }

    // GridView que muestra el fragment y adapter que comunica los datos con el grid
    GridView gridView;
    DataGridAdapter adapter;
    private Cursor cursor;

    // Objeto de la interfaz definida anteriormente. Debería ser WeakReference
    private OnDataGridFragmentClickListener listener;


    public DataGridFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_grid, container, false);
    }


    /**
     * Método donde queda ligado el fragment con la actividad. Después se llama a onCreateView()
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Asignamos el grid view del layout a nuestro variable
        gridView = (GridView) getActivity().findViewById(R.id.grid_view);
        refreshData();
    }


    /**
     * Método para refrescar datos
     */
    public void refreshData() {
        // Activity hereda de context, qie es el que necesito para obtener cursor
        cursor = new NotebookDAO(getActivity()).queryCursor();
        adapter = new DataGridAdapter(getActivity(), cursor);

        // Asignamos adapter al grid para que pida y muestre datos
        gridView.setAdapter(adapter);

        // Listener para click corto sobre la celda X
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Comprobamos que no haya listener y llamamos al método de la interfaz para click
                if (listener != null) {
                    listener.dataGridElementClick(parent, view, position, id);
                }
            }
        });

        // Listener para pulsación larga sobre la celda X
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Comprobamos que no haya listener (como el delegado de Obj-C) y llamamos al método
                // de la interfaz para long click
                if (listener != null) {
                    listener.dataGridElementLongClick(parent, view, position, id);
                }

                return true;
            }
        });
    }


    // Añadimos getter y setter para el listener

    public OnDataGridFragmentClickListener getListener() {
        return listener;
    }

    public void setListener(OnDataGridFragmentClickListener listener) {
        this.listener = listener;
    }
}
