package com.company.framework.base;

import com.company.framework.config.ConfigManager;
import com.company.framework.utils.ExtentReportManager;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.appium.java_client.support.*;

import java.time.Duration;


public abstract class BasePage {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;
    protected final Logger log = LogManager.getLogger(getClass());

    protected BasePage(AndroidDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                    ConfigManager.getConfig().explicitWait()
                )
        );

        PageFactory.initElements(new AppiumFieldDecorator(driver),this);}

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        String desc = locator.toString();
        log.info("Tapping element: {}", desc);
        waitForClickable(locator).click();
        ExtentReportManager.logInfo("Tapped: " + desc);
    }

    protected void tap(WebElement element) {
        String desc = element.getTagName();
        log.info("Tapping element: {}", desc);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        ExtentReportManager.logInfo("Tapped element: " + desc);
    }

    protected void sendKeys(By locator, String text) {
        log.info("Entering text '{}' into: {}", text, locator);
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
        ExtentReportManager.logInfo("Entered text '" + text + "' into: " + locator);
    }

    protected void sendKeys(WebElement element, String text) {
        log.info("Entering text '{}' into element", text);
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
        ExtentReportManager.logInfo("Entered text '" + text + "'");
    }

    protected String getText(By locator) {
        String text = waitForVisible(locator).getText();
        log.info("Got text '{}' from: {}", text, locator);
        return text;
    }

    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        String text = element.getText();
        log.info("Got text: '{}'", text);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void clearField(By locator) {
        waitForVisible(locator).clear();
        log.info("Cleared field: {}", locator);
    }

    protected void hideKeyboard() {
        try {
            driver.hideKeyboard();
            log.info("Keyboard hidden.");
        } catch (Exception e) {
            log.warn("Could not hide keyboard: {}", e.getMessage());
        }
    }

    protected void scrollToText(String text) {
        log.info("Scrolling to text: {}", text);
        driver.findElement(
                new io.appium.java_client.AppiumBy.ByAndroidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))"
                                + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"
                )
        );
    }

  
    public abstract String getPageName();
}
