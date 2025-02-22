package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseTest{
    @Test
    public void testUnauthorizedUserCanAccessLoginAndSignupPages() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/files");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/notes");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/credentials");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testSignUpUserLoginViewHomeLogout() {
        doMockSignUp("Romi1","Test1","rominita1","12345");
        doLogIn("rominita1", "12345");

        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        doLogOut();
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }
}
