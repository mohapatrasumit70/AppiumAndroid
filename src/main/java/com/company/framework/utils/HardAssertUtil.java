package com.company.framework.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

public class HardAssertUtil extends Assertion {

    private static final Logger log = LogManager.getLogger(HardAssertUtil.class);

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        String msg = assertCommand.getMessage() != null ? assertCommand.getMessage() : "Verification";
        String logText = String.format("✔ HARD ASSERT PASS: %s | Expected: [%s], Actual: [%s]", 
                                       msg, assertCommand.getExpected(), assertCommand.getActual());
        
        log.info(logText);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().pass(logText);
        }
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        String msg = assertCommand.getMessage() != null ? assertCommand.getMessage() : "Verification";
        String logText = String.format("❌ HARD ASSERT FAIL: %s | Expected: [%s], Actual: [%s]", 
                                       msg, assertCommand.getExpected(), assertCommand.getActual());
        
        log.error(logText);
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().fail(logText);
        }
        
        // Note: The parent Assertion class automatically throws the AssertionError 
        // immediately after this hook finishes, stopping the test execution.
    }
}