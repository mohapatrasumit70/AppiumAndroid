package com.company.framework.base;

import com.company.framework.config.ConfigManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;


public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

  
    public static void initDriver() {
        if (getDriver() != null) {
            log.warn("Driver already initialised for this thread. Skipping.");
            return;
        }

        var cfg = ConfigManager.getConfig();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(cfg.platformName())
                .setDeviceName(cfg.deviceName())
                .setUdid(cfg.udid())
                .setPlatformVersion(cfg.platformVersion())
                .setAutomationName(cfg.automationName())
                .setAppPackage(cfg.appPackage())
                .setAppActivity(cfg.appActivity())
                .setNoReset(false)
                .setAutoGrantPermissions(true).
                setEnsureWebviewsHavePages(true)
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAndroidInstallTimeout(Duration.ofMillis(90000));
        
        

        // Set app path only if provided; otherwise launch an already-installed app
        String appPath = cfg.appPath();
        if (appPath != null && !appPath.isBlank()) {
            options.setApp(Paths.get(appPath).toAbsolutePath().toString());
            log.info("App path set: {}", appPath);
        }

        try {
            URL serverUrl = new URL(cfg.appiumServerUrl());
            AndroidDriver driver = new AndroidDriver(serverUrl, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(cfg.implicitWait()));
            driverThreadLocal.set(driver);
            log.info("AndroidDriver initialised. Session: {}", driver.getSessionId());
        } catch (MalformedURLException e) {
            log.error("Invalid Appium server URL: {}", cfg.appiumServerUrl(), e);
            throw new RuntimeException("Failed to initialise AndroidDriver", e);
        }
    }

    /**
     * Returns the AndroidDriver for the current thread.
     */
    public static AndroidDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits and removes the driver for the current thread.
     */
    public static void quitDriver() {
        AndroidDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                log.info("AndroidDriver quit successfully.");
            } catch (Exception e) {
                log.warn("Error while quitting driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
