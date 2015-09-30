package com.daviddetena.everpobre;

import android.test.AndroidTestCase;

import com.daviddetena.everpobre.model.Note;
import com.daviddetena.everpobre.model.Notebook;

public class NotebookTests extends AndroidTestCase {

    private final String name = "Test notebook title";
    private final String noteText = "Note text";

    public void testCanCreateANotebook(){
        Notebook notebook = new Notebook(name);

        // Make sure notebook is not null
        assertNotNull(notebook);
        assertEquals(name, notebook.getName());
    }

    public void testCanAddNotesToANotebook(){
        Notebook notebook = new Notebook(name);
        Note note = new Note(notebook, noteText);

        // Make sure that there is no note before adding one
        assertEquals(0, notebook.allNotes().size());

        notebook.addNote(note);

        // Make sure that there is one single note before adding one
        assertEquals(1, notebook.allNotes().size());
    }

    public void testCanAddNotesToANotebookWithNoteText(){
        Notebook notebook = new Notebook(name);

        assertEquals(0, notebook.allNotes().size());

        notebook.addNote("my note text");

        assertEquals(1, notebook.allNotes().size());

    }
}
