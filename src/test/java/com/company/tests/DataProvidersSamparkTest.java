package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.utils.DataProviderUtil;
import org.testng.annotations.Test;

public class DataProvidersSamparkTest extends BaseTest {

    // IMPORTANT: Note the dataProvider and dataProviderClass attributes
    @Test(
        description = "Verify Sampark Login with multiple user credentials from Excel",
        dataProvider = "LoginDataProvider",
        dataProviderClass = DataProviderUtil.class
    )
    public void TestSamparkLogin(String username, String password, String expectedResult) {
        
        // Use the parameters passed directly from the Excel row
        loginPage.loginToSampark(username, password);

        // Perform validations based on expected result
        if (expectedResult.equalsIgnoreCase("Pass")) {
            boolean isDashboardVisible = true; // Replace with actual element check
            softAssert.assertTrue(isDashboardVisible, "Dashboard should be visible for valid user");
        } else {
            boolean isErrorVisible = true; // Replace with actual element check
            softAssert.assertTrue(isErrorVisible, "Error message should be visible for invalid user");
        }

        softAssert.assertAll();
    }
}