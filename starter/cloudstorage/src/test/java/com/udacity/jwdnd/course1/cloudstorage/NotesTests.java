package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotesTests extends BaseTest{
    @Test
    public void testAddANote() {
        doMockSignUp("Romi","Test","romiAddNotes","12345");
        doLogIn("romiNotes", "12345");

        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickButton("nav-notes-tab");
        clickButton("add-new-note");
        addNote("Supermarket", "Eggs, milk, salt, bread");
        Assertions.assertTrue(isTextPresent("Note saved successfully!"));
    }

    @Test
    public void testEditANote() {
        doMockSignUp("Romi","Test","romiEditNotes","12345");
        doLogIn("romiEditNotes", "12345");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickButton("nav-notes-tab");
        clickButton("add-new-note");
        addNote("Fruit", "Apples, oranges, bananas, lemmons");
        clickEditButtonInTable("Fruit","#userTable","th#note-title","#edit-note");
        addNote(" bio market", ", watermelon, apples");
        Assertions.assertTrue(isTextPresent("Note saved successfully!"));
        Assertions.assertTrue(isTextPresent("Fruit bio market"));
        Assertions.assertTrue(isTextPresent("Apples, oranges, bananas, lemmons, watermelon, apples"));
    }

    @Test
    public void testDeleteANote() {
        doMockSignUp("Romi","Test","romiDeleteNotes","12345");
        doLogIn("romiDeleteNotes", "12345");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickButton("nav-notes-tab");
        clickButton("add-new-note");
        addNote("ToDelete", "Note to delete");
        findDeleteInTable("ToDelete", "#userTable","th#note-title", "#delete-note");
        Assertions.assertTrue(isTextPresent("Note deleted successfully!"));
        Assertions.assertFalse(isTextPresent("ToDelete"));
        Assertions.assertFalse(isTextPresent("Note to delete"));
    }

    public void addNote(String title, String description) {
        fillField("title-note",title);
        fillField("note-description",description);
        clickButton("save-changes-note");
    }
}
