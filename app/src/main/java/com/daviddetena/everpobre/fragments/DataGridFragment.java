package com.daviddetena.everpobre.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.adapters.DataGridAdapter;
import com.daviddetena.everpobre.model.dao.NotebookDAO;

public class DataGridFragment extends Fragment {

    /**
     * Creamos interfaz personalizada para detectar eventos sobre el Grid
     */
    public interface OnDataGridFragmentClickListener{

        // Métodos de interfaz para cuando se haga click y long click sobre el grid
        public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id);
        public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id);
    }

    // GridView que muestra el fragment y adapter que comunica los datos con el grid
    GridView gridView;
    DataGridAdapter adapter;
    private Cursor cursor;
    private int idLayout = R.layout.fragment_data_grid;
    private int idGridView;

    // Objeto de la interfaz definida anteriormente. Debería ser WeakReference
    private OnDataGridFragmentClickListener listener;


    /**
     * Método de factoría para crear el fragment desde código y no desde XML
     * @param cursor
     * @param idLayout
     * @param idGridView
     * @return
     */
    public static DataGridFragment createDataGridFragment(Cursor cursor, int idLayout, int idGridView){
        DataGridFragment fragment = new DataGridFragment();

        fragment.cursor = cursor;
        fragment.idLayout = idLayout;
        fragment.idGridView = idGridView;

        return fragment;
    }

    public DataGridFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(idLayout, container, false);
    }


    /**
     * Método donde queda ligado el fragment con la actividad. Después se llama a onCreateView()
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Asignamos el grid view del layout a nuestro variable
        gridView = (GridView) getActivity().findViewById(idGridView);

        if(gridView != null){
            refreshData();
        }

    }


    /**
     * Método para refrescar datos
     */
    public void refreshData() {

        if(cursor == null){
            return;
        }

        gridView = (GridView) getActivity().findViewById(idGridView);

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

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getIdGridView() {
        return idGridView;
    }

    public void setIdGridView(int idGridView) {
        this.idGridView = idGridView;
    }

    public int getIdLayout() {
        return idLayout;
    }

    public void setIdLayout(int idLayout) {
        this.idLayout = idLayout;
    }
}
