package com.daviddetena.everpobre;

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
}
