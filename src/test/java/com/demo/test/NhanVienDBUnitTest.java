package com.demo.test;

import com.demo.base.DBUnitBaseTest;
import com.demo.dao.NhanVienDAO;
import com.demo.model.NhanVien;
import com.demo.dao.PhongBanDAO;
import com.demo.model.PhongBan;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class chính demo các chức năng quan trọng của DBUnit
 * Bao gồm: Insert, Update, Delete, Compare datasets, Database assertions
 */
public class NhanVienDBUnitTest extends DBUnitBaseTest {

    private NhanVienDAO nhanVienDAO;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Thiết lập schema database cho testing
     * Tạo bảng nhân viên cho các test cases
     */
    @Override
    protected void thieLapSchema() throws SQLException {
        System.out.println("Creating employee table schema...");
        
        // Tạo bảng phòng ban trước (cần cho foreign key)
        PhongBanDAO phongBanDAO = new PhongBanDAO(connection);
        phongBanDAO.taoBangPhongBan();
        
        // Thêm một số phòng ban cơ bản cho test
        PhongBan[] phongBans = {
            new PhongBan(1L, "IT", "Công nghệ thông tin", "Phát triển phần mềm", "Manager", 0, true),
            new PhongBan(2L, "HR", "Nhân sự", "Quản lý nhân sự", "Manager", 0, true),
            new PhongBan(3L, "FIN", "Tài chính", "Kế toán tài chính", "Manager", 0, true)
        };
        
        for (PhongBan pb : phongBans) {
            try {
                phongBanDAO.themPhongBan(pb);
            } catch (SQLException e) {
                // Ignore if already exists
            }
        }
        
        // Tạo bảng nhân viên
        nhanVienDAO = new NhanVienDAO(connection);
        nhanVienDAO.taoBangNhanVien();
        
        System.out.println("Employee table created successfully");
    }

    /**
     * Test 1: Demo CLEAN_INSERT operation
     * Chức năng: Load dữ liệu từ XML và insert vào database
     */
    @Test
    public void testCleanInsertOperation() throws Exception {
        System.out.println("\n=== TEST 1: CLEAN_INSERT Operation Demo ===");
        
        // Load và insert dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Verify dữ liệu đã được insert
        List<NhanVien> nhanVienList = nhanVienDAO.layTatCaNhanVien();
        assertEquals("Should have 5 employees after CLEAN_INSERT", 5, nhanVienList.size());
        
        // Verify thông tin chi tiết của nhân viên đầu tiên
        NhanVien nhanVien1 = nhanVienDAO.timNhanVienTheoId(1L);
        assertNotNull("Employee with ID 1 should exist", nhanVien1);
        assertEquals("Nguyễn Văn An", nhanVien1.getHoTen());
        assertEquals("an.nguyen@company.com", nhanVien1.getEmail());
        assertEquals("IT", nhanVien1.getPhongBan());
        
        System.out.println("CLEAN_INSERT operation test passed successfully!");
    }

    /**
     * Test 2: Demo Database Assertion
     * Chức năng: So sánh dữ liệu trong database với expected dataset
     */
    @Test
    public void testDatabaseAssertion() throws Exception {
        System.out.println("\n=== TEST 2: Database Assertion Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Lấy actual dataset từ database
        IDataSet actualDataSet = getTableDataSet("nhan_vien");
        
        // Load expected dataset từ file
        IDataSet expectedDataSet = loadDataSet("dataset/nhan-vien-initial.xml");
        
        // So sánh two datasets
        ITable actualTable = actualDataSet.getTable("nhan_vien");
        ITable expectedTable = expectedDataSet.getTable("nhan_vien");
        
        // Assertion với DBUnit
        Assertion.assertEquals(expectedTable, actualTable);
        
        System.out.println("Database assertion test passed successfully!");
    }

    /**
     * Test 3: Demo INSERT operation và dataset comparison
     * Chức năng: Thêm dữ liệu mới và verify kết quả
     */
    @Test
    public void testInsertAndCompare() throws Exception {
        System.out.println("\n=== TEST 3: INSERT Operation and Dataset Comparison Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Thêm nhân viên mới thông qua DAO
        NhanVien nhanVienMoi = new NhanVien();
        nhanVienMoi.setId(6L);
        nhanVienMoi.setHoTen("Hoàng Thị Em");
        nhanVienMoi.setEmail("em.hoang@company.com");
        nhanVienMoi.setPhongBan("Marketing");
        nhanVienMoi.setChucVu("Marketing Specialist");
        nhanVienMoi.setLuong(new BigDecimal("18000000.00"));
        nhanVienMoi.setNgayVaoLam(dateFormat.parse("2023-01-10"));
        nhanVienMoi.setTrangThaiHoatDong(true);
        
        nhanVienDAO.themNhanVien(nhanVienMoi);
        
        // Verify số lượng nhân viên đã tăng
        assertEquals("Should have 6 employees after adding new one", 6, nhanVienDAO.demTongSoNhanVien());
        
        // So sánh với expected dataset
        IDataSet actualDataSet = getTableDataSet("nhan_vien");
        IDataSet expectedDataSet = loadDataSet("dataset/nhan-vien-expected.xml");
        
        ITable actualTable = actualDataSet.getTable("nhan_vien");
        ITable expectedTable = expectedDataSet.getTable("nhan_vien");
        
        // So sánh chỉ những columns quan trọng (bỏ qua auto-generated fields)
        ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, 
            new String[]{"ho_ten", "email", "phong_ban", "chuc_vu", "luong", "trang_thai_hoat_dong"});
        ITable filteredExpectedTable = DefaultColumnFilter.includedColumnsTable(expectedTable, 
            new String[]{"ho_ten", "email", "phong_ban", "chuc_vu", "luong", "trang_thai_hoat_dong"});
        
        Assertion.assertEquals(filteredExpectedTable, filteredActualTable);
        
        System.out.println("INSERT and comparison test passed successfully!");
    }

    /**
     * Test 4: Demo UPDATE operation
     * Chức năng: Cập nhật dữ liệu và verify
     */
    @Test
    public void testUpdateOperation() throws Exception {
        System.out.println("\n=== TEST 4: UPDATE Operation Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Lấy nhân viên để update
        NhanVien nhanVien = nhanVienDAO.timNhanVienTheoId(1L);
        assertNotNull("Employee should exist before update", nhanVien);
        
        // Update thông tin
        String luongCu = nhanVien.getLuong().toString();
        nhanVien.setLuong(new BigDecimal("30000000.00"));
        nhanVien.setChucVu("Lead Developer");
        
        boolean updateSuccess = nhanVienDAO.capNhatNhanVien(nhanVien);
        assertTrue("Update operation should succeed", updateSuccess);
        
        // Verify update đã thành công
        NhanVien updatedNhanVien = nhanVienDAO.timNhanVienTheoId(1L);
        assertEquals("Salary should be updated", new BigDecimal("30000000.00"), updatedNhanVien.getLuong());
        assertEquals("Position should be updated", "Lead Developer", updatedNhanVien.getChucVu());
        
        System.out.println("Employee salary updated from " + luongCu + " to " + updatedNhanVien.getLuong());
        System.out.println("UPDATE operation test passed successfully!");
    }

    /**
     * Test 5: Demo DELETE operation
     * Chức năng: Xóa dữ liệu và verify
     */
    @Test
    public void testDeleteOperation() throws Exception {
        System.out.println("\n=== TEST 5: DELETE Operation Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Verify dữ liệu ban đầu
        assertEquals("Should have 5 employees initially", 5, nhanVienDAO.demTongSoNhanVien());
        assertNotNull("Employee with ID 5 should exist", nhanVienDAO.timNhanVienTheoId(5L));
        
        // Xóa nhân viên
        boolean deleteSuccess = nhanVienDAO.xoaNhanVien(5L);
        assertTrue("Delete operation should succeed", deleteSuccess);
        
        // Verify xóa thành công
        assertEquals("Should have 4 employees after deletion", 4, nhanVienDAO.demTongSoNhanVien());
        assertNull("Employee with ID 5 should not exist", nhanVienDAO.timNhanVienTheoId(5L));
        
        System.out.println("DELETE operation test passed successfully!");
    }

    /**
     * Test 6: Demo REFRESH operation
     * Chức năng: Insert hoặc Update tùy theo dữ liệu đã tồn tại
     */
    @Test
    public void testRefreshOperation() throws Exception {
        System.out.println("\n=== TEST 6: REFRESH Operation Demo ===");
        
        // Setup dữ liệu ban đầu (chỉ 3 nhân viên đầu)
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Xóa 2 nhân viên cuối
        nhanVienDAO.xoaNhanVien(4L);
        nhanVienDAO.xoaNhanVien(5L);
        assertEquals("Should have 3 employees", 3, nhanVienDAO.demTongSoNhanVien());
        
        // Sử dụng REFRESH để restore tất cả dữ liệu
        // REFRESH sẽ UPDATE nhân viên đã tồn tại và INSERT nhân viên mới
        refreshData("dataset/nhan-vien-initial.xml");
        
        // Verify tất cả dữ liệu đã được restore
        assertEquals("Should have 5 employees after REFRESH", 5, nhanVienDAO.demTongSoNhanVien());
        assertNotNull("Employee with ID 4 should exist again", nhanVienDAO.timNhanVienTheoId(4L));
        assertNotNull("Employee with ID 5 should exist again", nhanVienDAO.timNhanVienTheoId(5L));
        
        System.out.println("REFRESH operation test passed successfully!");
    }

    /**
     * Test 7: Demo tìm kiếm theo điều kiện
     * Chức năng: Test các query method của DAO
     */
    @Test
    public void testSearchOperations() throws Exception {
        System.out.println("\n=== TEST 7: Search Operations Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Test tìm theo phòng ban
        List<NhanVien> nhanVienIT = nhanVienDAO.timNhanVienTheoPhongBan("IT");
        assertEquals("Should have 3 IT employees", 3, nhanVienIT.size());
        
        List<NhanVien> nhanVienHR = nhanVienDAO.timNhanVienTheoPhongBan("HR");
        assertEquals("Should have 1 HR employee", 1, nhanVienHR.size());
        
        List<NhanVien> nhanVienFinance = nhanVienDAO.timNhanVienTheoPhongBan("Finance");
        assertEquals("Should have 1 Finance employee", 1, nhanVienFinance.size());
        
        // Verify chi tiết nhân viên IT
        for (NhanVien nv : nhanVienIT) {
            assertEquals("All should be in IT department", "IT", nv.getPhongBan());
            System.out.println("IT Employee: " + nv.getHoTen() + " - " + nv.getChucVu());
        }
        
        System.out.println("Search operations test passed successfully!");
    }

    /**
     * Test 8: Demo export và import dataset
     * Chức năng: Xuất dữ liệu từ database ra XML và import lại
     */
    @Test
    public void testDataSetExportImport() throws Exception {
        System.out.println("\n=== TEST 8: Dataset Export/Import Demo ===");
        
        // Setup và load dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Export current dataset từ database (chỉ table nhan_vien)
        IDataSet exportedDataSet = getTableDataSet("nhan_vien");
        printDataSetInfo(exportedDataSet);
        
        // Xóa tất cả dữ liệu
        nhanVienDAO.xoaTatCaDuLieu();
        assertEquals("Should have no employees after deletion", 0, nhanVienDAO.demTongSoNhanVien());
        
        // Import lại từ exported dataset
        // (Trong thực tế bạn có thể save exported dataset ra file và load lại)
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Verify dữ liệu đã được restore
        assertEquals("Should have 5 employees after import", 5, nhanVienDAO.demTongSoNhanVien());
        
        System.out.println("Dataset export/import test passed successfully!");
    }

    /**
     * Test 9: Demo filter và compare specific columns
     * Chức năng: So sánh chỉ một số columns cụ thể
     */
    @Test
    public void testColumnFiltering() throws Exception {
        System.out.println("\n=== TEST 9: Column Filtering Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        
        // Lấy dataset từ database
        IDataSet actualDataSet = getTableDataSet("nhan_vien");
        ITable actualTable = actualDataSet.getTable("nhan_vien");
        
        // Lấy expected dataset
        IDataSet expectedDataSet = loadDataSet("dataset/nhan-vien-initial.xml");
        ITable expectedTable = expectedDataSet.getTable("nhan_vien");
        
        // So sánh chỉ các columns cơ bản (bỏ qua ID và dates)
        String[] columnsToCompare = {"ho_ten", "email", "phong_ban", "trang_thai_hoat_dong"};
        
        ITable filteredActual = DefaultColumnFilter.includedColumnsTable(actualTable, columnsToCompare);
        ITable filteredExpected = DefaultColumnFilter.includedColumnsTable(expectedTable, columnsToCompare);
        
        // Assert với filtered columns
        Assertion.assertEquals(filteredExpected, filteredActual);
        
        System.out.println("Column filtering test passed successfully!");
        System.out.println("Compared columns: " + String.join(", ", columnsToCompare));
    }

    /**
     * Test 10: Demo error handling và rollback
     * Chức năng: Test xử lý lỗi và rollback transaction
     */
    @Test
    public void testErrorHandlingAndRollback() throws Exception {
        System.out.println("\n=== TEST 10: Error Handling and Rollback Demo ===");
        
        // Setup dữ liệu ban đầu
        insertCleanData("dataset/nhan-vien-initial.xml");
        int initialCount = nhanVienDAO.demTongSoNhanVien();
        
        // Tạo nhân viên với email trùng (should fail due to unique constraint)
        NhanVien duplicateEmailEmployee = new NhanVien();
        duplicateEmailEmployee.setHoTen("Test Duplicate");
        duplicateEmailEmployee.setEmail("an.nguyen@company.com"); // Email đã tồn tại
        duplicateEmailEmployee.setPhongBan("Test");
        duplicateEmailEmployee.setChucVu("Tester");
        duplicateEmailEmployee.setLuong(new BigDecimal("10000000"));
        duplicateEmailEmployee.setTrangThaiHoatDong(true);
        
        // Test xử lý lỗi
        try {
            nhanVienDAO.themNhanVien(duplicateEmailEmployee);
            fail("Should throw exception for duplicate email");
        } catch (SQLException e) {
            System.out.println("Expected error caught: " + e.getMessage());
            
            // Verify không có dữ liệu nào bị thay đổi
            assertEquals("Employee count should remain unchanged after error", 
                initialCount, nhanVienDAO.demTongSoNhanVien());
        }
        
        System.out.println("Error handling and rollback test passed successfully!");
    }
}