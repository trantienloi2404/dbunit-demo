package com.demo;

import com.demo.dao.NhanVienDAO;
import com.demo.model.NhanVien;
import com.demo.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Main class demo các chức năng cơ bản của ứng dụng
 * Sử dụng để test manually trước khi chạy DBUnit tests
 */
public class DemoDBUnitMain {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            System.out.println("=== DEMO DBUNIT APPLICATION ===");
            System.out.println("Starting DBUnit demo application...");
            
            // Tạo kết nối database
            connection = DatabaseUtil.taoKetNoiH2();
            
            // Tạo DAO instance
            NhanVienDAO nhanVienDAO = new NhanVienDAO(connection);
            
            // Tạo bảng
            System.out.println("\n1. Creating database schema...");
            nhanVienDAO.taoBangNhanVien();
            
            // Demo thêm nhân viên
            System.out.println("\n2. Adding sample employees...");
            themNhanVienMau(nhanVienDAO);
            
            // Demo query dữ liệu
            System.out.println("\n3. Querying employees...");
            queryNhanVien(nhanVienDAO);
            
            // Demo update
            System.out.println("\n4. Updating employee data...");
            updateNhanVien(nhanVienDAO);
            
            // Demo delete
            System.out.println("\n5. Deleting employee...");
            deleteNhanVien(nhanVienDAO);
            
            // Final report
            System.out.println("\n6. Final employee count:");
            int finalCount = nhanVienDAO.demTongSoNhanVien();
            System.out.println("Total employees remaining: " + finalCount);
            
            System.out.println("\n=== DEMO COMPLETED SUCCESSFULLY ===");
            System.out.println("Now you can run the DBUnit tests to see advanced testing features!");
            
        } catch (Exception e) {
            System.err.println("Error during demo execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.dongKetNoi(connection);
        }
    }
    
    private static void themNhanVienMau(NhanVienDAO dao) throws Exception {
        // Nhân viên 1
        NhanVien nv1 = new NhanVien();
        nv1.setHoTen("Nguyễn Văn An");
        nv1.setEmail("an.nguyen@company.com");
        nv1.setPhongBan("IT");
        nv1.setChucVu("Senior Developer");
        nv1.setLuong(new BigDecimal("25000000"));
        nv1.setNgayVaoLam(dateFormat.parse("2020-01-15"));
        nv1.setTrangThaiHoatDong(true);
        dao.themNhanVien(nv1);
        
        // Nhân viên 2
        NhanVien nv2 = new NhanVien();
        nv2.setHoTen("Trần Thị Bình");
        nv2.setEmail("binh.tran@company.com");
        nv2.setPhongBan("HR");
        nv2.setChucVu("HR Manager");
        nv2.setLuong(new BigDecimal("30000000"));
        nv2.setNgayVaoLam(dateFormat.parse("2019-03-20"));
        nv2.setTrangThaiHoatDong(true);
        dao.themNhanVien(nv2);
        
        // Nhân viên 3
        NhanVien nv3 = new NhanVien();
        nv3.setHoTen("Lê Hoàng Cường");
        nv3.setEmail("cuong.le@company.com");
        nv3.setPhongBan("IT");
        nv3.setChucVu("Project Manager");
        nv3.setLuong(new BigDecimal("35000000"));
        nv3.setNgayVaoLam(dateFormat.parse("2018-06-10"));
        nv3.setTrangThaiHoatDong(true);
        dao.themNhanVien(nv3);
        
        System.out.println("Added 3 sample employees successfully");
    }
    
    private static void queryNhanVien(NhanVienDAO dao) throws Exception {
        // Lấy tất cả nhân viên
        List<NhanVien> tatCaNhanVien = dao.layTatCaNhanVien();
        System.out.println("Found " + tatCaNhanVien.size() + " employees:");
        
        for (NhanVien nv : tatCaNhanVien) {
            System.out.println("- ID: " + nv.getId() + ", Name: " + nv.getHoTen() + 
                ", Department: " + nv.getPhongBan() + ", Position: " + nv.getChucVu());
        }
        
        // Tìm theo phòng ban
        List<NhanVien> nhanVienIT = dao.timNhanVienTheoPhongBan("IT");
        System.out.println("IT Department has " + nhanVienIT.size() + " employees");
        
        // Tìm theo ID
        NhanVien nhanVien1 = dao.timNhanVienTheoId(1L);
        if (nhanVien1 != null) {
            System.out.println("Employee with ID 1: " + nhanVien1.getHoTen());
        }
    }
    
    private static void updateNhanVien(NhanVienDAO dao) throws Exception {
        // Lấy nhân viên đầu tiên để update
        NhanVien nhanVien = dao.timNhanVienTheoId(1L);
        if (nhanVien != null) {
            BigDecimal luongCu = nhanVien.getLuong();
            
            // Update lương và chức vụ
            nhanVien.setLuong(new BigDecimal("28000000"));
            nhanVien.setChucVu("Lead Developer");
            
            boolean success = dao.capNhatNhanVien(nhanVien);
            if (success) {
                System.out.println("Updated employee: " + nhanVien.getHoTen());
                System.out.println("Salary changed from " + luongCu + " to " + nhanVien.getLuong());
                System.out.println("New position: " + nhanVien.getChucVu());
            }
        }
    }
    
    private static void deleteNhanVien(NhanVienDAO dao) throws Exception {
        // Xóa nhân viên cuối cùng được thêm
        boolean success = dao.xoaNhanVien(3L);
        if (success) {
            System.out.println("Deleted employee with ID: 3");
        }
        
        // Verify xóa thành công
        NhanVien deletedEmployee = dao.timNhanVienTheoId(3L);
        if (deletedEmployee == null) {
            System.out.println("Employee deletion confirmed - employee no longer exists");
        }
    }
} 