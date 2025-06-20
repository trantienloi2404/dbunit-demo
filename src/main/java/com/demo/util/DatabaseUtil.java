package com.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối database
 * Hỗ trợ cả H2 in-memory database cho testing và MySQL cho production
 */
public class DatabaseUtil {
    
    // Cấu hình H2 Database cho testing
    private static final String H2_DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    // Cấu hình MySQL Database
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/dbunit_demo";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "";

    /**
     * Tạo kết nối H2 in-memory database cho testing
     */
    public static Connection taoKetNoiH2() throws SQLException {
        try {
            Class.forName(H2_DRIVER);
            Connection connection = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
            System.out.println("Connected to H2 in-memory database successfully");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 Driver not found", e);
        }
    }

    /**
     * Tạo kết nối MySQL database
     */
    public static Connection taoKetNoiMySQL() throws SQLException {
        try {
            Class.forName(MYSQL_DRIVER);
            Connection connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
            System.out.println("Connected to MySQL database successfully");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    /**
     * Tạo kết nối H2 file-based database
     */
    public static Connection taoKetNoiH2File(String filePath) throws SQLException {
        try {
            Class.forName(H2_DRIVER);
            String url = "jdbc:h2:file:" + filePath + ";MODE=MySQL";
            Connection connection = DriverManager.getConnection(url, H2_USER, H2_PASSWORD);
            System.out.println("Connected to H2 file database: " + filePath);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 Driver not found", e);
        }
    }

    /**
     * Đóng kết nối database một cách an toàn
     */
    public static void dongKetNoi(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed successfully");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Kiểm tra kết nối database có hoạt động không
     */
    public static boolean kiemTraKetNoi(Connection connection) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            System.err.println("Error checking database connection: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tạo kết nối với URL tùy chỉnh
     */
    public static Connection taoKetNoiTuyChon(String url, String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to custom database: " + url);
        return connection;
    }
} 
 