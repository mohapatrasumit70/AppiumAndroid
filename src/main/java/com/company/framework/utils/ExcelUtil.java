package com.company.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);
    private Workbook workbook;
    private Sheet sheet;
    private final DataFormatter formatter = new DataFormatter();

    /**
     * Initializes the Excel connection.
     * @param excelPath Relative path from project root (e.g., "src/test/resources/testdata/Data.xlsx")
     * @param sheetName Name of the sheet to read
     */
    public ExcelUtil(String excelPath, String sheetName) {
        try {
            File file = new File(excelPath);
            if (!file.exists()) {
                throw new RuntimeException("Excel file not found at: " + file.getAbsolutePath());
            }
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file.");
            }
        } catch (Exception e) {
            log.error("Failed to load Excel file: {}", excelPath, e);
            throw new RuntimeException(e);
        }
    }

    // =========================================================================
    // 1. FETCH ALL DATA (For TestNG DataProvider - Returns Object[][])
    // =========================================================================
    
    /**
     * Fetches all rows (skipping the header) into a 2D Array for TestNG DataProvider.
     */
    public Object[][] getAllData() {
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        // Object[][] size: Rows (excluding header) x Columns
        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip header
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = getCellData(i, j);
            }
        }
        return data;
    }

    // =========================================================================
    // 2. FETCH ALL DATA AS LIST OF MAPS (Great for fetching specific sets via Keys)
    // =========================================================================
    
    /**
     * Fetches all rows as a List of Maps. Key = Header Name, Value = Cell Value.
     */
    public List<Map<String, String>> getAllDataAsMapList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        List<String> headers = getRowData(0); // Row 0 is header

        for (int i = 1; i < rowCount; i++) {
            Map<String, String> rowMap = new HashMap<>();
            for (int j = 0; j < colCount; j++) {
                rowMap.put(headers.get(j), getCellData(i, j));
            }
            dataList.add(rowMap);
        }
        return dataList;
    }

    // =========================================================================
    // 3. FETCH SINGLE CELL DATA
    // =========================================================================
    
    /**
     * Fetches data from a specific cell using Row and Column Index.
     */
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) return "";
            Cell cell = row.getCell(colNum);
            return formatter.formatCellValue(cell).trim();
        } catch (Exception e) {
            log.error("Error reading cell [{}, {}]", rowNum, colNum, e);
            return "";
        }
    }

    // =========================================================================
    // 4. FETCH ENTIRE ROW DATA
    // =========================================================================
    
    /**
     * Fetches all columns for a specific row index.
     */
    public List<String> getRowData(int rowNum) {
        List<String> rowData = new ArrayList<>();
        try {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                for (int j = 0; j < getColumnCount(); j++) {
                    rowData.add(formatter.formatCellValue(row.getCell(j)).trim());
                }
            }
        } catch (Exception e) {
            log.error("Error reading row [{}]", rowNum, e);
        }
        return rowData;
    }

    // =========================================================================
    // 5. FETCH ENTIRE COLUMN DATA
    // =========================================================================
    
    /**
     * Fetches all rows for a specific column index (skipping header).
     */
    public List<String> getColumnData(int colNum) {
        List<String> colData = new ArrayList<>();
        try {
            for (int i = 1; i < getRowCount(); i++) { // Skip header
                colData.add(getCellData(i, colNum));
            }
        } catch (Exception e) {
            log.error("Error reading column [{}]", colNum, e);
        }
        return colData;
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    public int getColumnCount() {
        Row row = sheet.getRow(0);
        return (row == null) ? 0 : row.getLastCellNum();
    }

    public void close() {
        try {
            if (workbook != null) workbook.close();
        } catch (Exception e) {
            log.warn("Error closing workbook", e);
        }
    }
}