package com.daviddetena.everpobre;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.daviddetena.everpobre.model.Notebook;
import com.daviddetena.everpobre.model.dao.NotebookDAO;
import com.daviddetena.everpobre.model.db.DBHelper;

public class NotebookDAOTests extends AndroidTestCase {
    // Heredamos de AndroidTestCase para poder utilizar el Contexto

    public void testInsertNullNotebookReturnsInvalidId(){
        Notebook notebook = null;
        NotebookDAO notebookDAO = new NotebookDAO(getContext());

        long id = notebookDAO.insert(notebook);

        // Al insertar un notebook null debe devolver un -1. Es lo que se comprueba
        assertEquals(id, DBHelper.INVALID_ID);
    }

    /**
     * Test para comprobar que al insertar un notebook con nombre nulo devuelve -1
     */
    public void testInsertNotebookWithNullNameReturnsInvalidId(){
        Notebook notebook = new Notebook("");
        notebook.setName(null);
        NotebookDAO notebookDAO = new NotebookDAO(getContext());

        long id = notebookDAO.insert(notebook);

        // Al insertar un notebook null debe devolver un -1. Es lo que se comprueba
        assertEquals(id, DBHelper.INVALID_ID);
    }

    public void testInsertNotebookReturnsValidId(){
        Notebook notebook = new Notebook("Test title");
        NotebookDAO notebookDAO = new NotebookDAO(getContext());

        long id = notebookDAO.insert(notebook);

        // aseguro condición cierta (no igual). Ya tiene que ser id > 0
        assertTrue(id>0);
    }

    public void testQueryAllNotebooks(){

        // Insertamos 10 notebooks
        insertNotebookStubs(10);

        final NotebookDAO notebookDAO = new NotebookDAO(getContext());
        final Cursor cursor = notebookDAO.queryCursor();
        final int notebookCount = cursor.getCount();

        // Al añadir 10 testeamos que haya más de 9
        assertTrue(notebookCount > 9);
    }

    private void insertNotebookStubs(final int notebooksToInsert) {

        // Añadimos libretas
        final NotebookDAO notebookDAO = new NotebookDAO(getContext());

        for(int i=0; i<notebooksToInsert; i++){
            // Con esto optimizas el no crear dos objetos string para concatenar  "title" con i
            final String testTitle = String.format("%s %d", "Test title ", i);
            final Notebook notebook = new Notebook(testTitle);
            long id = notebookDAO.insert(notebook);
        }
    }

    public void testDeleteAllNotebooks(){

        // Esto es para asegurar que borra
        insertNotebookStubs(10);

        final NotebookDAO notebookDAO = new NotebookDAO(getContext());
        notebookDAO.deleteAll();

        final Cursor cursor = notebookDAO.queryCursor();
        final int notebookCount = cursor.getCount();

        // Testeo que al borrar todas las libretas, el cursor me devolverá 0 registros
        assertEquals(0, notebookCount);
    }

    public void testUpdateOneNotebook(){

        // Insertamos notebook y devolvemos id
        final NotebookDAO notebookDAO = new NotebookDAO(getContext());
        final Notebook notebook = new Notebook("Change me if you dare");
        final long id = notebookDAO.insert(notebook);

        // Recuperamos notebook insertado y comprobamos que su nombre es el esperado
        final Notebook originalCopyNotebook = notebookDAO.query(id);
        assertEquals("Change me if you dare", originalCopyNotebook.getName());

        // Actualizamos notebook en memoria y en la DB
        originalCopyNotebook.setName("Challenge accepted");
        notebookDAO.update(id, originalCopyNotebook);

        // Test con el que comprobamos que se ha actualizado el nombre
        final Notebook afterChangeNotebook = notebookDAO.query(id);
        assertEquals("Challenge accepted", afterChangeNotebook.getName());
    }
}
