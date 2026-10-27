package com.company.framework.constants;

/**
 * AppConstants — Central place for all string constants used in tests.
 * Avoids magic strings scattered across test classes.
 */
public final class AppConstants {

    private AppConstants() {}

    // ── Timeouts ────────────────────────────────────────────────────────────
    public static final int DEFAULT_TIMEOUT   = 20;
    public static final int SHORT_TIMEOUT     = 5;
    public static final int LONG_TIMEOUT      = 60;

    // ── File Paths ──────────────────────────────────────────────────────────
    public static final String REPORTS_DIR      = System.getProperty("user.dir") + "/reports/";
    public static final String SCREENSHOTS_DIR  = System.getProperty("user.dir") + "/screenshots/";
    public static final String LOGS_DIR         = System.getProperty("user.dir") + "/logs/";
    public static final String TEST_DATA_DIR    = System.getProperty("user.dir") + "/src/test/resources/testdata/";

    // ── Toast / Alert Messages ───────────────────────────────────────────────
    public static final String LOGIN_SUCCESS_MSG  = "Login Successful";
    public static final String LOGIN_FAILED_MSG   = "Invalid credentials";
    public static final String NETWORK_ERROR_MSG  = "No internet connection";

    // ── Add your app-specific constants below ───────────────────────────────
}
