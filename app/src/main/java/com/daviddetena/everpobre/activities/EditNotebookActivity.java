package com.daviddetena.everpobre.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.daviddetena.everpobre.R;
import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditNotebookActivity extends AppCompatActivity {

    // Notebook a editar
    private Notebook notebook;


    // Tres modos: uno de edición (si pasan datos) o uno de nuevo notebook si no pasan datos
    enum EditMode{
        ADDING,
        EDITING,
        DELETING
    }

    private EditMode editMode;

    @Bind(R.id.txt_notebook_name) EditText txtNotebookName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notebook);

        // Inyectamos mediante Butterknife los widgets
        ButterKnife.bind(this);

        // Recuperamos el notebook a partir del intent con el que se ha llamado esta Activity con
        // la constante definida
        Intent i = getIntent();
        notebook = i.getParcelableExtra(Constants.intent_key_notebook);

        // Comprobamos el objeto que llega a través del Intent
        if(notebook == null){
            // Estamos añadiendo
            editMode = EditMode.ADDING;
        }
        else{
            // Estamos editando => ponemos nombre en EditText
            editMode = EditMode.EDITING;
            txtNotebookName.setText(notebook.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id){
            // Opción seleccionada: guardar
            case R.id.action_save_notebook:
                saveNotebook();
                break;

            // Opción seleccionada: eliminar
            case R.id.action_delete_notebook:
                deleteNotebook();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveNotebook() {
        // Recuperamos texto
        final String notebookName = txtNotebookName.getText().toString();

        // Llega vacío
        if(notebookName.isEmpty()){
            return;
        }
        // No llega vacío
        final NotebookDAO notebookDAO = new NotebookDAO(this);

        if(editMode == EditMode.ADDING){

            // Modo añadir
            final Notebook notebookToAdd = new Notebook(notebookName);
            notebookDAO.insert(notebookToAdd);
        }
        else if(editMode == EditMode.EDITING){

            // Modo edición => Recupero el texto introducido y actualizamos datos
            this.notebook.setName(notebookName);
            notebookDAO.update(this.notebook.getId(), this.notebook);
        }

        // Cerramos actividad
        finish();
    }


    private void deleteNotebook() {

    }


}
