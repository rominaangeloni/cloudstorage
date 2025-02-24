package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SignUpTest extends BaseTest {
    @Test
    public void testSignUp() {
        doMockSignUp("Romi","Test","errorSignUp","12345");
        clickButton("signup-link");
        doSignUp("Romi","Test","errorSignUp","12345");
        Assertions.assertTrue(driver.findElement(By.id("error-msg")).getText().contains("The username already exists"));
    }

    void doSignUp(String firstName, String lastName, String userName, String password) {

        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        fillField("inputFirstName", firstName);
        fillField("inputLastName", lastName);
        fillField("inputUsername", userName);
        fillField("inputPassword", password);

        clickButton("buttonSignUp");
    }
}
