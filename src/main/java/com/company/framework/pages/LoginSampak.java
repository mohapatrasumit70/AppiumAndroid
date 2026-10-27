package com.company.framework.pages;

import org.openqa.selenium.WebElement;
import com.company.framework.base.BasePage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginSampak extends BasePage {

    public LoginSampak(AndroidDriver driver) {
        super(driver);
    }

    @Override
    public String getPageName() {
        return "Sampark Login Page";
    }

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    private WebElement usernameField;
  
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "Login")
    private WebElement submit;

    public void EnterUserName(String username) {
        usernameField.click();
        usernameField.sendKeys(username);
    }

    public void EnterUserPassword(String password) {
        passwordField.click();
        passwordField.sendKeys(password);
    }

    public void submitButton() {
        submit.click();
    }

    // Reusable method for auto-login
    public void loginToSampark(String username, String password) {
        EnterUserName(username);
        EnterUserPassword(password);
        submitButton();
    }
}