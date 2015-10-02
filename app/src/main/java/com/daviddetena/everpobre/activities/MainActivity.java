package com.daviddetena.everpobre.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.fragments.DataGridFragment;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.util.Constants;

public class MainActivity extends AppCompatActivity {

    DataGridFragment notebookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos referencia a nuestro DataGridFragment de Notebook
        notebookFragment = (DataGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);

        // Personalizamos los métodos de la interfaz de DataGridFragment
        notebookFragment.setListener(new DataGridFragment.OnDataGridFragmentClickListener() {
            @Override
            public void dataGridElementClick(AdapterView<?> parent, View view, int position, long id) {
                // Lanzamos activity de vista de detalle
                // No podemos poner this ya que en este ámbito sería OnDataGridFragmentClickListener la clase
                // También podríamos poner los métodos de la interfaz fuera, si MainActivity
                // implementara la interfaz OnDataGridFragmentClickListener
                Intent i = new Intent(MainActivity.this, ShowNotebookActivity.class);
                startActivity(i);
            }

            @Override
            public boolean dataGridElementLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Para pasar el notebook podemos tener dos casos:
                // 1) genero un  objeto notebook que implemente serializale y parcelable para poder
                // incluirlo en el putExtra del Intent como un objeto

                // Creamos un nuevo objeto Notebook con el notebook de la DB cuyo id me pasan
                NotebookDAO notebookDAO = new NotebookDAO(MainActivity.this);
                Notebook notebook = notebookDAO.query(id);

                // Mostramos pantalla de edición de Notebook mediante Intent (Notebook con Parcelable)
                Intent i = new Intent(MainActivity.this, EditNotebookActivity.class);
                i.putExtra(Constants.intent_key_notebook, notebook);

                // 2) paso el id del objeto y en la vista detalle ya obtengo el objeto de la DB
                //i.putExtra("com.daviddetena.everpobre.notebook", id);

                // Arrancamos nueva actividad
                startActivity(i);


                return true;
            }
        });

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
