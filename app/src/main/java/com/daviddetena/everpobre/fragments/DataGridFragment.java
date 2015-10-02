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
import com.daviddetena.everpobre.adapters.DataGridAdapter;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.util.Constants;

public class DataGridFragment extends Fragment {

    // GridView que muestra el fragment y adapter que comunica los datos con el grid
    GridView gridView;
    DataGridAdapter adapter;
    private Cursor cursor;

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
                Toast.makeText(getActivity(), "Single click", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para pulsación larga sobre la celda X
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Long click", Toast.LENGTH_SHORT).show();

                // Para pasar el notebook podemos tener dos casos:
                // 1) genero un  objeto notebook que implemente serializale y parcelable para poder
                // incluirlo en el putExtra del Intent como un objeto

                // Creamos un nuevo objeto Notebook con el notebook de la DB cuyo id me pasan
                NotebookDAO notebookDAO = new NotebookDAO(getActivity());
                Notebook notebook = notebookDAO.query(id);

                // Mostramos pantalla de edición de Notebook mediante Intent (Notebook con Parcelable)
                Intent i = new Intent(getActivity(), EditNotebookActivity.class);
                i.putExtra(Constants.intent_key_notebook, notebook);

                // 2) paso el id del objeto y en la vista detalle ya obtengo el objeto de la DB
                //i.putExtra("com.daviddetena.everpobre.notebook", id);

                // Arrancamos nueva actividad
                startActivity(i);

                // True no detecta el single click. Con false sí, y haría long click + single click
                return true;
            }
        });
    }
}
