package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CredentialsTests extends BaseTest {

    @Test
    public void testAddACredential() {
        doMockSignUp("Romi","Test","romiAddCredential","12345");
        doLogIn("romiAddCredential", "12345");

        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickOnCredentialsTab();
        String url = "www.testpage.com";
        String user = "addcredentialuser";
        String password = "12345678";
        String successMessage = "Credential saved successfully!";
        completeCredentialValues(url,user, password);
        clickButton("save-changes-credential");
        verifyCredential(successMessage, url, user, password);
    }

    private void verifyCredential(String successMessage, String url, String user, String password) {
        Assertions.assertTrue(isTextPresent(successMessage));
        Assertions.assertTrue(isTextPresent(url));
        Assertions.assertTrue(isTextPresent(user));
        Assertions.assertFalse(isTextPresent(password));
    }

    @Test
    public void testEditACredential() {
        doMockSignUp("Romi","Test","romiEditCredential","12345");
        doLogIn("romiEditCredential", "12345");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickOnCredentialsTab();
        String url = "www.testeditcredentialpage";
        String user = "credentialuser";
        String password = "87654321";
        completeCredentialValues(url,user, password);
        clickButton("save-changes-credential");
        Assertions.assertFalse(isTextPresent(password));
        Assertions.assertTrue(isTextPresent("nYpeETurWYBf/a7VaHoHdg=="));
        clickEditButtonInTable(url,"#credentialTable","th#url-credential","#edit-credential");
        Assertions.assertEquals("87654321", getTextInField("credential-password"));
        completeCredentialValues(".com", "edited", "edited");
        Assertions.assertEquals("87654321edited", getTextInField("credential-password"));
        clickButton("save-changes-credential");
        String successMessage = "Credential saved successfully!";
        verifyCredential(successMessage, "www.testeditcredentialpage.com", "credentialuseredited", "87654321edited");
        Assertions.assertTrue(isTextPresent("1on74t9gpZ1XZ5HU"));
    }

    private void clickOnCredentialsTab() {
        clickButton("nav-credentials-tab");
        clickButton("add-new-credential");
    }

    @Test
    public void testDeleteACredential() {
       doMockSignUp("Romi","Test","romiDeleteCredential","12345");
        doLogIn("romiDeleteCredential", "12345");
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
        clickOnCredentialsTab();
        completeCredentialValues("ToDelete.com", "UserToDelete", "todelete");
        clickButton("save-changes-credential");
        findDeleteInTable("ToDelete.com", "#credentialTable","th#url-credential","#delete-credential");
        Assertions.assertTrue(isTextPresent("Credential deleted successfully!"));
        Assertions.assertFalse(isTextPresent("ToDelete"));
        Assertions.assertFalse(isTextPresent("Note to delete"));
    }

    public void completeCredentialValues(String url, String user, String password) {
        fillField("credential-url",url);
        fillField("credential-username",user);
        fillField("credential-password",password);
    }
}
