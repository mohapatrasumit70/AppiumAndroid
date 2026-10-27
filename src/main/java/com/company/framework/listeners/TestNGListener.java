package com.company.framework.listeners;

import com.aventstack.extentreports.Status;
import com.company.framework.utils.ExtentReportManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNGListener — Hooks into TestNG events for centralised reporting.
 * Register in testng.xml under <listeners> block.
 */
public class TestNGListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestNGListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> TEST STARTED: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<< TEST PASSED: {}", result.getName());
        ExtentReportManager.logPass("Test Passed ✔");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("<<< TEST FAILED: {} — {}", result.getName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("<<< TEST SKIPPED: {}", result.getName());
        ExtentReportManager.getTest().log(Status.SKIP, "Skipped: " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        log.error("<<< TEST TIMED OUT: {}", result.getName());
    }
}
