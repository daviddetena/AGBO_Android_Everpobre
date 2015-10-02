package com.daviddetena.everpobre.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.fragments.DataGridFragment;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;

public class MainActivity extends AppCompatActivity {

    DataGridFragment notebookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos referencia a nuestro Fragment de Notebook
        notebookFragment = (DataGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);

        // Metemos fake data para que haya datos tras lanzar la aplicación
        //insertNotebookStubs(10);
    }

    /**
     * Al volver al foreground, pedimos al fragment que se repinte para leer de nuevo los datos de
     * la DB.
     */
    @Override
    protected void onResume() {
        super.onResume();

        notebookFragment.refreshData();
    }

    private void insertNotebookStubs(final int notebooksToInsert) {

        // Añadimos libretas
        final NotebookDAO notebookDAO = new NotebookDAO(this);

        for(int i=0; i<notebooksToInsert; i++){
            // Con esto optimizas el no crear dos objetos string para concatenar  "title" con i
            final String testTitle = String.format("%s %d", "Notebook happy ", i);
            final Notebook notebook = new Notebook(testTitle);
            long id = notebookDAO.insert(notebook);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Cargamos el menú con el contenido cargando del xml de menu_main, y si se carga bien
        // devuelve true
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_notebook) {

            // Launch edit notebook activity from MainActivity to EditNotebookActivity
            Intent i = new Intent(this, EditNotebookActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
