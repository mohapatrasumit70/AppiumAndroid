package com.company.tests;

import com.company.framework.base.BaseTest;
import com.company.framework.utils.DatabaseUtil;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class LoginSamparkTestSQL extends BaseTest {

    @Test(description = "Verify Sampark Login and run SQL Update and Fetch queries")
    public void TestSampark() throws Exception {

        // 1. RUN UPDATE QUERY (e.g., reset OTP or activate user status)
        int updatedRows = DatabaseUtil.executeUpdateQuery(
                "UPDATE users SET status = ? WHERE emp_id = ?",
                "ACTIVE", "400001"
        );
        System.out.println("Rows Updated: " + updatedRows);

        // 2. FETCH SINGLE VALUE INTO A STRING OR PRIMITIVE (int, long, double, boolean)
        String otp = DatabaseUtil.fetchSingleValue(
                "SELECT otp_code FROM user_otp WHERE emp_id = ?",
                String.class,
                "400001"
        );
        System.out.println("Fetched OTP (String): " + otp);

        int loginAttempts = DatabaseUtil.fetchSingleValue(
                "SELECT login_attempts FROM users WHERE emp_id = ?",
                Integer.class,
                "400001"
        );
        System.out.println("Fetched Login Attempts (int): " + loginAttempts);

        // 3. FETCH MULTIPLE VALUES (Single Column) INTO A LIST
        List<String> customerIds = DatabaseUtil.fetchColumnList(
                "SELECT customer_id FROM leads WHERE assigned_rm = ?",
                String.class,
                "400001"
        );
        System.out.println("Fetched Customer IDs List: " + customerIds);

        // 4. FETCH MULTIPLE COLUMNS OF A SINGLE ROW INTO A MAP
        Map<String, Object> userDetails = DatabaseUtil.fetchSingleRowAsMap(
                "SELECT emp_name, branch_code, mobile_no FROM users WHERE emp_id = ?",
                "400001"
        );
        System.out.println("Employee Name: " + userDetails.get("emp_name"));
        System.out.println("Branch Code: " + userDetails.get("branch_code"));

        // 5. FETCH MULTIPLE ROWS AND MULTIPLE COLUMNS INTO A LIST OF MAPS
        List<Map<String, Object>> allLeads = DatabaseUtil.fetchAllRowsAsMapList(
                "SELECT lead_id, customer_name, loan_amount FROM leads WHERE status = ?",
                "PENDING"
        );
        for (Map<String, Object> row : allLeads) {
            System.out.println("Lead ID: " + row.get("lead_id") + " | Amount: " + row.get("loan_amount"));
        }
    }
}
