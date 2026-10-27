package com.company.framework.base;

import com.company.framework.pages.LoginSampak;
import com.company.framework.utils.ExtentReportManager;
import com.company.framework.utils.HardAssertUtil;
import com.company.framework.utils.ScreenshotUtil;
import com.company.framework.utils.SoftAssertUtil;
import com.company.framework.utils.VideoRecordUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;



public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    protected AndroidDriver driver;
    protected LoginSampak loginPage;
    protected SoftAssertUtil softAssert;
    protected HardAssertUtil hardAssert;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        log.info("========== TEST SUITE STARTED ==========");
        ExtentReportManager.initReports();

        // Start Appium Server automatically if auto.start.appium=true in config.properties
        AppiumServerManager.startServerIfEnabled();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTearDown() {
        // Stop Appium Server if it was started automatically
        AppiumServerManager.stopServerIfRunning();

        ExtentReportManager.flushReports();
        log.info("========== TEST SUITE FINISHED ==========");
    }

    @BeforeMethod(alwaysRun = true)
    public void testSetup(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        log.info("---------- Starting Test: {} ----------", testName);
        ExtentReportManager.createTest(testName, description);

        // 1. Initialize Driver
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        ExtentReportManager.getTest().info("Driver initialised — App launched.");

        // ---> ADD IT RIGHT HERE <---
        // Initialize a fresh SoftAssert for the current test
        softAssert = new SoftAssertUtil();
        hardAssert = new HardAssertUtil();

        // 2. Start Video Recording (captures login + test execution)
        VideoRecordUtil.startRecording();

        // 3. Automatically Login before every test
        log.info("Performing automatic login to Sampark...");
        loginPage = new LoginSampak(driver);
        loginPage.loginToSampark("400001", "Welcome@1111");
        ExtentReportManager.getTest().info("Logged in automatically with user: 400001");
    }

    @AfterMethod(alwaysRun = true)
    public void testTearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        boolean isFailed = (result.getStatus() == ITestResult.FAILURE);

        // 1. Handle Test Status & Capture Failure Screenshot IMMEDIATELY
        if (isFailed) {
            log.error("TEST FAILED: {}", testName);
            String screenshotPath = ScreenshotUtil.captureAndSave(testName);
            ExtentReportManager.logFailWithScreenshot(
                    "Test Failed: " + result.getThrowable().getMessage(), screenshotPath);
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.warn("TEST SKIPPED: {}", testName);
            ExtentReportManager.getTest().log(Status.SKIP, "Test skipped: " +
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason"));
        } else {
            log.info("TEST PASSED: {}", testName);
            ExtentReportManager.getTest().log(Status.PASS, "Test Passed ✔");
        }

        // 2. Stop Video Recording & Attach .mp4 Link (still before quitDriver)
     // 2. Stop Video Recording & Embed Video Player Directly Inside Extent Report
        String videoPath = VideoRecordUtil.stopAndSaveRecording(testName, isFailed);
        if (videoPath != null) {
            String videoHtml = "<b>🎥 Execution Video:</b><br>"
                    + "<video width='320' height='568' controls>"
                    + "<source src='file:///" + videoPath.replace("\\", "/") + "' type='video/mp4'>"
                    + "Your browser does not support the video tag."
                    + "</video>";
            ExtentReportManager.getTest().info(videoHtml);
        }
        

        // 3. Quit Driver
        DriverManager.quitDriver();
        log.info("---------- Test Finished: {} ----------", testName);
    }
    
}