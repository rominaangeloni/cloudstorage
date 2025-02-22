package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @LocalServerPort
    protected int port;
    protected WebDriver driver;
    protected WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        fillField("inputFirstName", firstName);
        fillField("inputLastName", lastName);
        fillField("inputUsername", userName);
        fillField("inputPassword", password);

        // Attempt to sign up.
        clickButton("buttonSignUp");

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }

    protected void fillField(String fieldId, String value) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(fieldId)));
        WebElement fieldToFill = driver.findElement(By.id(fieldId));
        fieldToFill.click();
        fieldToFill.sendKeys(value);
    }

    protected void clickButton(String buttonId) {
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId))).click();
    }

    protected boolean isTextPresent(String text) {
        return driver.getPageSource().contains(text);
    }
    protected String getTextInField(String field) {
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(field)));
        WebElement fieldToGet = driver.findElement(By.id(field));
        return fieldToGet.getAttribute("value");
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    void doLogIn(String userName, String password)
    {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        fillField("inputUsername", userName);
        fillField("inputPassword", password);
        clickButton("login-button");

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    public void doLogOut(){
        clickButton("logout-button");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
    }

    public void findDeleteInTable(String value, String tableName, String fieldName, String buttonName) {
        List<WebElement> rows = driver.findElements(By.cssSelector(tableName +" tbody tr"));


        for (WebElement row : rows) {
            WebElement titleElement = webDriverWait.until(ExpectedConditions.visibilityOf(row.findElement(By.cssSelector(fieldName))));
            if (titleElement.getText().equals(value)) {
                WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(row.findElement(By.cssSelector(buttonName))));
                editButton.click();
                break;
            }
        }
    }

    public void clickEditButtonInTable(String value, String tableName, String fieldName, String buttonName) {
        List<WebElement> rows = driver.findElements(By.cssSelector(tableName + " tbody tr"));

        for (WebElement row : rows) {
            WebElement titleElement = webDriverWait.until(ExpectedConditions.visibilityOf(row.findElement(By.cssSelector(fieldName))));
            if (titleElement.getText().equals(value)) {
                WebElement editButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(row.findElement(By.cssSelector(buttonName))));
                editButton.click();
                break;
            }
        }
    }
}
