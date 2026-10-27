package com.company.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.company.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager — Thread-safe Extent Reports 5 manager.
 * Generates an HTML Spark report under reports/<timestamp>/
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private static final String TIMESTAMP =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    private static final String REPORT_PATH =
            System.getProperty("user.dir") + "/reports/" + TIMESTAMP + "/TestReport.html";

    private ExtentReportManager() {}

    // ──────────────────────────────────────────────────────────────────────────
    // Initialise Reports
    // ──────────────────────────────────────────────────────────────────────────

    public static synchronized void initReports() {
        if (extent == null) {
            var cfg = ConfigManager.getConfig();

            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
            spark.config().setDocumentTitle(cfg.reportTitle());
            spark.config().setReportName(cfg.reportName());
            spark.config().setTheme(Theme.DARK);
            spark.config().setEncoding("UTF-8");
            spark.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", cfg.environment());
            extent.setSystemInfo("Tester", cfg.testerName());
            extent.setSystemInfo("Platform", cfg.platformName() + " " + cfg.platformVersion());
            extent.setSystemInfo("Device", cfg.deviceName());
            extent.setSystemInfo("Automation", cfg.automationName());
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            log.info("Extent Reports initialised. Report path: {}", REPORT_PATH);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Test node management
    // ──────────────────────────────────────────────────────────────────────────

    public static synchronized void createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName,
                (description != null && !description.isBlank()) ? description : testName);
        testThreadLocal.set(test);
        log.debug("ExtentTest created: {}", testName);
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Logging helpers
    // ──────────────────────────────────────────────────────────────────────────

    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) test.log(Status.INFO, message);
    }

    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) test.log(Status.PASS, message);
        log.info("[PASS] {}", message);
    }

    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) test.log(Status.FAIL, message);
        log.error("[FAIL] {}", message);
    }

    public static void logWarning(String message) {
        ExtentTest test = getTest();
        if (test != null) test.log(Status.WARNING, message);
        log.warn("[WARN] {}", message);
    }

    /**
     * Logs a failure with an embedded screenshot in the Extent report.
     *
     * @param message        failure description
     * @param screenshotPath absolute path to the screenshot file
     */
    public static void logFailWithScreenshot(String message, String screenshotPath) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                test.fail(message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                log.error("[FAIL] {} | Screenshot: {}", message, screenshotPath);
            } catch (Exception e) {
                log.warn("Could not attach screenshot to report: {}", e.getMessage());
                test.fail(message);
            }
        }
    }

    /**
     * Logs a pass with an embedded screenshot.
     */
    public static void logPassWithScreenshot(String message, String screenshotPath) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                test.pass(message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.pass(message);
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Flush
    // ──────────────────────────────────────────────────────────────────────────

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Extent Reports flushed. Open: {}", REPORT_PATH);
        }
    }

    public static String getReportPath() {
        return REPORT_PATH;
    }
}
