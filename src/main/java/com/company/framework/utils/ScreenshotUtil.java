package com.company.framework.utils;

import com.company.framework.base.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil — Captures screenshots from the active AndroidDriver session.
 * Saves to: screenshots/<testName>_<timestamp>.png
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR =
            System.getProperty("user.dir") + File.separator + "screenshots";

    private ScreenshotUtil() {}

    /**
     * Captures a screenshot and saves it locally.
     *
     * @param testName name of the test (used in filename)
     * @return absolute path of saved screenshot, or empty string on failure
     */
    public static String captureAndSave(String testName) {
        try {
            var driver = DriverManager.getDriver();
            if (driver == null) {
                log.warn("Cannot capture screenshot — driver is null.");
                return "";
            }

            // Ensure directory exists
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = sanitise(testName) + "_" + timestamp + ".png";
            Path destPath = dir.resolve(fileName);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destPath);

            log.info("Screenshot saved: {}", destPath.toAbsolutePath());
            return destPath.toAbsolutePath().toString();

        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * Returns screenshot as Base64 string (useful for embedding directly).
     */
    public static String captureAsBase64() {
        try {
            var driver = DriverManager.getDriver();
            if (driver == null) return "";
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as Base64: {}", e.getMessage());
            return "";
        }
    }

    private static String sanitise(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
