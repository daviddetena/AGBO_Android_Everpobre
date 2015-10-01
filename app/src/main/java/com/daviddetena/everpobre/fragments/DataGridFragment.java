package com.daviddetena.everpobre.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.adapters.DataGridAdapter;
import com.daviddetena.everpobre.model.dao.NotebookDAO;

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
        // Activity hereda de context, qie es el que necesito para obtener cursor
        cursor = new NotebookDAO(getActivity()).queryCursor();
        adapter = new DataGridAdapter(getActivity(), cursor);

        // Asignamos adapter al grid para que pida y muestre datos
        gridView.setAdapter(adapter);
    }
}
