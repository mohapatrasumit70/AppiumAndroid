package com.company.framework.utils;

import com.company.framework.config.ConfigManager;
import com.company.framework.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class DatabaseUtil {

    private static final Logger log = LogManager.getLogger(DatabaseUtil.class);

    private DatabaseUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates and returns a JDBC Connection using config.properties.
     */
    private static Connection getConnection() throws SQLException {
        FrameworkConfig cfg = ConfigManager.getConfig();
        return DriverManager.getConnection(cfg.dbUrl(), cfg.dbUsername(), cfg.dbPassword());
    }

    /**
     * Helper to bind parameters (?) into a PreparedStatement.
     */
    private static void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }
    }

    // =========================================================================
    // 1. UPDATE / INSERT / DELETE QUERY
    // =========================================================================

    /**
     * Executes an UPDATE, INSERT, or DELETE SQL query.
     *
     * @param sqlQuery The SQL query (can include '?' placeholders)
     * @param params   Optional parameter values for '?' placeholders
     * @return Number of rows affected (int)
     */
    public static int executeUpdateQuery(String sqlQuery, Object... params) {
        log.info("Executing Update Query: {} | Params: {}", sqlQuery, Arrays.toString(params));
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            setParameters(pstmt, params);
            int rowsAffected = pstmt.executeUpdate();
            log.info("Update Query executed successfully. Rows affected: {}", rowsAffected);
            return rowsAffected;

        } catch (SQLException e) {
            log.error("Failed to execute update query: {}", sqlQuery, e);
            throw new RuntimeException("Database Update Query Failed: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // 2. FETCH SINGLE VALUE (Primitive / Wrapper / String)
    // =========================================================================

    /**
     * Executes a SELECT query and returns a single cell value (first row, first column)
     * converted to the requested data type (String, Integer, Long, Double, Boolean).
     *
     * @param sqlQuery   The SELECT query
     * @param returnType The class type you want back (e.g., String.class, Integer.class)
     * @param params     Optional parameter values for '?' placeholders
     * @return Single value of type T, or null if no record found
     */
    public static <T> T fetchSingleValue(String sqlQuery, Class<T> returnType, Object... params) {
        log.info("Executing Fetch Single Value Query: {} | Params: {}", sqlQuery, Arrays.toString(params));
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Object rawValue = rs.getObject(1);
                    T convertedValue = convertValue(rawValue, returnType);
                    log.info("Fetched single value: {}", convertedValue);
                    return convertedValue;
                } else {
                    log.warn("No rows returned for query: {}", sqlQuery);
                    return null;
                }
            }

        } catch (SQLException e) {
            log.error("Failed to fetch single value for query: {}", sqlQuery, e);
            throw new RuntimeException("Database Fetch Single Value Failed: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // 3. FETCH MULTIPLE VALUES FROM A SINGLE COLUMN (List<T>)
    // =========================================================================

    /**
     * Executes a SELECT query and stores all values from the first column into a List<T>.
     *
     * @param sqlQuery    The SELECT query
     * @param elementType Class type of list elements (e.g., String.class, Integer.class)
     * @param params      Optional parameter values for '?' placeholders
     * @return List of values of type T
     */
    public static <T> List<T> fetchColumnList(String sqlQuery, Class<T> elementType, Object... params) {
        log.info("Executing Fetch Column List Query: {} | Params: {}", sqlQuery, Arrays.toString(params));
        List<T> resultList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object rawValue = rs.getObject(1);
                    resultList.add(convertValue(rawValue, elementType));
                }
            }
            log.info("Fetched {} values into List: {}", resultList.size(), resultList);
            return resultList;

        } catch (SQLException e) {
            log.error("Failed to fetch column list for query: {}", sqlQuery, e);
            throw new RuntimeException("Database Fetch Column List Failed: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // 4. FETCH SINGLE ROW WITH MULTIPLE COLUMNS (Map<String, Object>)
    // =========================================================================

    /**
     * Fetches a single row (multiple columns) as a Map where Key = Column Name, Value = Column Value.
     */
    public static Map<String, Object> fetchSingleRowAsMap(String sqlQuery, Object... params) {
        List<Map<String, Object>> rows = fetchAllRowsAsMapList(sqlQuery, params);
        return rows.isEmpty() ? Collections.emptyMap() : rows.get(0);
    }

    // =========================================================================
    // 5. FETCH MULTIPLE ROWS & MULTIPLE COLUMNS (List<Map<String, Object>>)
    // =========================================================================

    /**
     * Fetches multiple rows and columns as a List of Maps.
     * Each Map represents 1 row (Key = Column Name, Value = Column Value).
     */
    public static List<Map<String, Object>> fetchAllRowsAsMapList(String sqlQuery, Object... params) {
        log.info("Executing Fetch Multiple Rows Query: {} | Params: {}", sqlQuery, Arrays.toString(params));
        List<Map<String, Object>> rowList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> rowMap = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        rowMap.put(columnName, rs.getObject(i));
                    }
                    rowList.add(rowMap);
                }
            }
            log.info("Fetched {} rows from database.", rowList.size());
            return rowList;

        } catch (SQLException e) {
            log.error("Failed to fetch multiple rows for query: {}", sqlQuery, e);
            throw new RuntimeException("Database Fetch Multiple Rows Failed: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // TYPE CONVERSION HELPER
    // =========================================================================

    @SuppressWarnings("unchecked")
    private static <T> T convertValue(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType.isInstance(value)) {
            return (T) value;
        }
        String strVal = value.toString().trim();
        if (targetType == String.class) {
            return (T) strVal;
        } else if (targetType == Integer.class || targetType == int.class) {
            return (T) Integer.valueOf(strVal);
        } else if (targetType == Long.class || targetType == long.class) {
            return (T) Long.valueOf(strVal);
        } else if (targetType == Double.class || targetType == double.class) {
            return (T) Double.valueOf(strVal);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(strVal.equalsIgnoreCase("true") || strVal.equals("1"));
        }
        return (T) value;
    }
}