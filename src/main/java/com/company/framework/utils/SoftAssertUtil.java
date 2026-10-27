package com.company.framework.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class SoftAssertUtil extends SoftAssert {

    private static final Logger log = LogManager.getLogger(SoftAssertUtil.class);

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String msg = assertCommand.getMessage() != null ? assertCommand.getMessage() : "Verification";
        String logText = String.format("✔ ASSERT PASS: %s | Expected: [%s], Actual: [%s]", 
                                       msg, assertCommand.getExpected(), assertCommand.getActual());
        
        log.info(logText);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().pass(logText);
        }
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String msg = assertCommand.getMessage() != null ? assertCommand.getMessage() : "Verification";
        String logText = String.format("❌ ASSERT FAIL: %s | Expected: [%s], Actual: [%s]", 
                                       msg, assertCommand.getExpected(), assertCommand.getActual());
        
        log.error(logText);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().fail(logText);
        }
    }
}