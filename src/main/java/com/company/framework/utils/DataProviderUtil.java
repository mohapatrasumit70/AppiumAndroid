package com.company.framework.utils;
import org.testng.annotations.DataProvider;

public class DataProviderUtil {

    private static final String TEST_DATA_PATH = "src/test/resources/testdata/TestData.xlsx";

    @DataProvider(name = "LoginDataProvider")
    public static Object[][] getLoginData() {
        ExcelUtil excel = new ExcelUtil(TEST_DATA_PATH, "LoginData");
        Object[][] data = excel.getAllData();
        excel.close();
        return data;
    }

   
    @DataProvider(name = "UserCreationProvider")
    public static Object[][] getNewUserData() {
        ExcelUtil excel = new ExcelUtil(TEST_DATA_PATH, "NewUsers");
        Object[][] data = excel.getAllData();
        excel.close();
        return data;
    }
}