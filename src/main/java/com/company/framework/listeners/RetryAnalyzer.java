package com.company.framework.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer — Retries failed tests up to MAX_RETRY_COUNT times.
 * Apply via @Test(retryAnalyzer = RetryAnalyzer.class)
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 1;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
