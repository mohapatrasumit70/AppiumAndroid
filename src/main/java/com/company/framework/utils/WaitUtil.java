package com.company.framework.utils;

import com.company.framework.base.DriverManager;
import com.company.framework.config.ConfigManager;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtil — Static utility for explicit wait operations.
 * Can be used directly in test classes without a Page Object.
 */

public class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);

    private WaitUtil() {}

    // Dynamic explicit wait
    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(seconds)
        );
    }

    // Default explicit wait from your existing config
    private static WebDriverWait getDefaultWait() {
        return getWait(ConfigManager.getConfig().explicitWait());
    }

    // 1. Wait for visibility - default timeout
    public static WebElement waitForVisible(By locator) {
        log.debug("Waiting for visibility: {}", locator);
        return getDefaultWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    // 2. Wait for visibility - dynamic timeout
    public static WebElement waitForVisible(By locator, int seconds) {
        log.debug("Waiting {} seconds for visibility: {}", seconds, locator);
        return getWait(seconds).until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    // 3. Wait for clickability - default timeout
    public static WebElement waitForClickable(By locator) {
        log.debug("Waiting for clickability: {}", locator);
        return getDefaultWait().until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    // 4. Wait for clickability - dynamic timeout
    public static WebElement waitForClickable(By locator, int seconds) {
        log.debug("Waiting {} seconds for clickability: {}", seconds, locator);
        return getWait(seconds).until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    // 5. Wait for invisibility - default timeout
    public static boolean waitForInvisibility(By locator) {
        return getDefaultWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    // 6. Wait for invisibility - dynamic timeout
    public static boolean waitForInvisibility(By locator, int seconds) {
        return getWait(seconds).until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    // 7. Wait for text - default timeout
    public static boolean waitForTextPresent(By locator, String text) {
        return getDefaultWait().until(
                ExpectedConditions.textToBePresentInElementLocated(locator, text)
        );
    }

    // 8. Wait for text - dynamic timeout
    public static boolean waitForTextPresent(
            By locator, String text, int seconds) {
        return getWait(seconds).until(
                ExpectedConditions.textToBePresentInElementLocated(locator, text)
        );
    }

    // 9. Static sleep - use only when necessary
    public static void sleep(long millis) {
        try {
            log.debug("Sleeping {} ms", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
