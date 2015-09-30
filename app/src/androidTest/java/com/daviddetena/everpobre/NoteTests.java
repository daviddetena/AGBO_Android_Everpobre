package com.daviddetena.everpobre;

import android.test.AndroidTestCase;

import com.daviddetena.everpobre.model.Note;
import com.daviddetena.everpobre.model.Notebook;

public class NoteTests extends AndroidTestCase {

    public void testCanCreateANote(){
        Note note = new Note(new Notebook("notebook"), "Note text");

        // Make sure a note can be created
        assertNotNull(note);
    }

}
