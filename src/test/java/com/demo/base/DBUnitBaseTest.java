package com.demo.base;

import com.demo.util.DatabaseUtil;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Base test class cung cấp các chức năng DBUnit cơ bản
 * Tất cả test classes khác sẽ kế thừa từ class này
 */
public abstract class DBUnitBaseTest {
    
    protected Connection connection;
    protected IDatabaseConnection dbUnitConnection;

    /**
     * Setup trước mỗi test case
     * Tạo kết nối database và DBUnit connection
     */
    @Before
    public void setUp() throws Exception {
        System.out.println("Setting up database connection for test...");
        
        // Tạo kết nối MySQL database (ưu tiên)
        connection = DatabaseUtil.taoKetNoiMacDinh();
        
        // Tạo DBUnit connection wrapper
        dbUnitConnection = new DatabaseConnection(connection);
        
        // Thiết lập database schema nếu cần
        thieLapSchema();
        
        System.out.println("Database setup completed successfully");
    }

    /**
     * Cleanup sau mỗi test case
     * Đóng kết nối và dọn dẹp tài nguyên
     */
    @After
    public void tearDown() throws Exception {
        System.out.println("Cleaning up database connection...");
        
        try {
            // Clean up dữ liệu test (quan trọng cho MySQL persistent database)
            if (connection != null && !connection.isClosed()) {
                // Xóa dữ liệu test theo thứ tự (con trước, cha sau)
                try {
                    connection.createStatement().executeUpdate("DELETE FROM nhan_vien WHERE 1=1");
                    connection.createStatement().executeUpdate("DELETE FROM phong_ban WHERE 1=1");
                    // Reset auto increment cho MySQL
                    connection.createStatement().executeUpdate("ALTER TABLE nhan_vien AUTO_INCREMENT = 1");
                    connection.createStatement().executeUpdate("ALTER TABLE phong_ban AUTO_INCREMENT = 1");
                } catch (SQLException e) {
                    // Ignore cleanup errors in case tables don't exist
                    System.out.println("Cleanup warning (ignored): " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
        
        // Đóng DBUnit connection
        if (dbUnitConnection != null) {
            dbUnitConnection.close();
            dbUnitConnection = null;
        }
        
        // Đóng database connection
        DatabaseUtil.dongKetNoi(connection);
        connection = null;
        
        System.out.println("Database cleanup completed");
    }

    /**
     * Thiết lập schema database (abstract method)
     * Các subclass sẽ implement method này để tạo tables
     */
    protected abstract void thieLapSchema() throws SQLException;

    /**
     * Load dataset từ XML file
     */
    protected IDataSet loadDataSet(String filePath) throws Exception {
        System.out.println("Loading dataset from: " + filePath);
        
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            // Thử load từ file system nếu không tìm thấy trong classpath
            inputStream = new FileInputStream(filePath);
        }
        
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true); // Tự động detect columns
        
        IDataSet dataSet = builder.build(inputStream);
        System.out.println("Dataset loaded successfully");
        
        return dataSet;
    }

    /**
     * Insert dữ liệu vào database sử dụng CLEAN_INSERT operation
     * Xóa tất cả dữ liệu hiện tại và insert dữ liệu mới
     */
    protected void insertCleanData(String datasetPath) throws Exception {
        System.out.println("Performing CLEAN_INSERT operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
        
        System.out.println("CLEAN_INSERT operation completed");
    }

    /**
     * Insert dữ liệu vào database sử dụng INSERT operation
     * Chỉ insert dữ liệu mới, không xóa dữ liệu hiện tại
     */
    protected void insertData(String datasetPath) throws Exception {
        System.out.println("Performing INSERT operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.INSERT.execute(dbUnitConnection, dataSet);
        
        System.out.println("INSERT operation completed");
    }

    /**
     * Update dữ liệu trong database
     */
    protected void updateData(String datasetPath) throws Exception {
        System.out.println("Performing UPDATE operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.UPDATE.execute(dbUnitConnection, dataSet);
        
        System.out.println("UPDATE operation completed");
    }

    /**
     * Refresh dữ liệu trong database (INSERT hoặc UPDATE)
     */
    protected void refreshData(String datasetPath) throws Exception {
        System.out.println("Performing REFRESH operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.REFRESH.execute(dbUnitConnection, dataSet);
        
        System.out.println("REFRESH operation completed");
    }

    /**
     * Xóa dữ liệu khỏi database
     */
    protected void deleteData(String datasetPath) throws Exception {
        System.out.println("Performing DELETE operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.DELETE.execute(dbUnitConnection, dataSet);
        
        System.out.println("DELETE operation completed");
    }

    /**
     * Xóa tất cả dữ liệu khỏi database
     */
    protected void deleteAllData(String datasetPath) throws Exception {
        System.out.println("Performing DELETE_ALL operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.DELETE_ALL.execute(dbUnitConnection, dataSet);
        
        System.out.println("DELETE_ALL operation completed");
    }

    /**
     * Truncate tất cả tables trong dataset
     */
    protected void truncateData(String datasetPath) throws Exception {
        System.out.println("Performing TRUNCATE_TABLE operation...");
        
        IDataSet dataSet = loadDataSet(datasetPath);
        DatabaseOperation.TRUNCATE_TABLE.execute(dbUnitConnection, dataSet);
        
        System.out.println("TRUNCATE_TABLE operation completed");
    }

    /**
     * Lấy current dataset từ database
     */
    protected IDataSet getCurrentDataSet() throws DatabaseUnitException, SQLException {
        System.out.println("Retrieving current dataset from database...");
        
        IDataSet dataSet = dbUnitConnection.createDataSet();
        
        System.out.println("Current dataset retrieved successfully");
        return dataSet;
    }

    /**
     * Lấy dataset của một table cụ thể
     */
    protected IDataSet getTableDataSet(String tableName) throws DatabaseUnitException, SQLException {
        System.out.println("Retrieving dataset for table: " + tableName);
        
        IDataSet dataSet = dbUnitConnection.createDataSet(new String[]{tableName});
        
        System.out.println("Table dataset retrieved successfully");
        return dataSet;
    }

    /**
     * Utility method để in thông tin dataset ra console
     */
    protected void printDataSetInfo(IDataSet dataSet) throws Exception {
        System.out.println("=== Dataset Information ===");
        String[] tableNames = dataSet.getTableNames();
        
        for (String tableName : tableNames) {
            System.out.println("Table: " + tableName);
            System.out.println("Row count: " + dataSet.getTable(tableName).getRowCount());
        }
        System.out.println("===========================");
    }
} 