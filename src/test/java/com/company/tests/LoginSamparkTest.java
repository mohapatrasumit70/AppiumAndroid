package com.company.tests;

import org.testng.annotations.Test;
import com.company.framework.base.BaseTest;

public class LoginSamparkTest extends BaseTest {

    @Test(description = "Verify Sampark Login and perform further actions")
    public void TestSampark() throws Exception {
        // Automatic login has ALREADY completed in BaseTest.testSetup()!
        
        // Wait or perform your next page actions here
        Thread.sleep(5000);
        
        // Example for your next steps after login:
        // DashboardPage dashboard = new DashboardPage(driver);
        // dashboard.clickLeadCreation();
    }
}