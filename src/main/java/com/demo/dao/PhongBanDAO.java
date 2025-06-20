package com.demo.dao;

import com.demo.model.PhongBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho thao tác với bảng phòng ban
 * Sử dụng cho demo DBUnit testing với khóa ngoại
 */
public class PhongBanDAO {
    private Connection connection;

    public PhongBanDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Tạo bảng phòng ban trong database
     */
    public void taoBangPhongBan() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS phong_ban (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "ma_phong_ban VARCHAR(10) UNIQUE NOT NULL, " +
                "ten_phong_ban VARCHAR(100) NOT NULL, " +
                "mo_ta TEXT, " +
                "truong_phong VARCHAR(100), " +
                "so_nhan_vien INT DEFAULT 0, " +
                "trang_thai_hoat_dong BOOLEAN DEFAULT TRUE" +
                ")";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("Department table created successfully");
        }
    }

    /**
     * Thêm phòng ban mới vào database
     */
    public void themPhongBan(PhongBan phongBan) throws SQLException {
        String sql = "INSERT INTO phong_ban (ma_phong_ban, ten_phong_ban, mo_ta, truong_phong, so_nhan_vien, trang_thai_hoat_dong) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, phongBan.getMaPhongBan());
            stmt.setString(2, phongBan.getTenPhongBan());
            stmt.setString(3, phongBan.getMoTa());
            stmt.setString(4, phongBan.getTruongPhong());
            stmt.setInt(5, phongBan.getSoNhanVien() != null ? phongBan.getSoNhanVien() : 0);
            stmt.setBoolean(6, phongBan.getTrangThaiHoatDong() != null ? phongBan.getTrangThaiHoatDong() : true);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        phongBan.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Department added successfully with ID: " + phongBan.getId());
            }
        }
    }

    /**
     * Tìm phòng ban theo ID
     */
    public PhongBan timPhongBanTheoId(Long id) throws SQLException {
        String sql = "SELECT * FROM phong_ban WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return taoPhongBanTuResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Tìm phòng ban theo mã phòng ban
     */
    public PhongBan timPhongBanTheoMa(String maPhongBan) throws SQLException {
        String sql = "SELECT * FROM phong_ban WHERE ma_phong_ban = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, maPhongBan);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return taoPhongBanTuResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy tất cả phòng ban
     */
    public List<PhongBan> layTatCaPhongBan() throws SQLException {
        List<PhongBan> danhSachPhongBan = new ArrayList<>();
        String sql = "SELECT * FROM phong_ban ORDER BY id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachPhongBan.add(taoPhongBanTuResultSet(rs));
            }
        }
        
        System.out.println("Retrieved " + danhSachPhongBan.size() + " departments from database");
        return danhSachPhongBan;
    }

    /**
     * Lấy tất cả phòng ban đang hoạt động
     */
    public List<PhongBan> layPhongBanHoatDong() throws SQLException {
        List<PhongBan> danhSachPhongBan = new ArrayList<>();
        String sql = "SELECT * FROM phong_ban WHERE trang_thai_hoat_dong = true ORDER BY ten_phong_ban";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachPhongBan.add(taoPhongBanTuResultSet(rs));
            }
        }
        
        System.out.println("Retrieved " + danhSachPhongBan.size() + " active departments");
        return danhSachPhongBan;
    }

    /**
     * Cập nhật thông tin phòng ban
     */
    public boolean capNhatPhongBan(PhongBan phongBan) throws SQLException {
        String sql = "UPDATE phong_ban SET ma_phong_ban = ?, ten_phong_ban = ?, mo_ta = ?, " +
                "truong_phong = ?, so_nhan_vien = ?, trang_thai_hoat_dong = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phongBan.getMaPhongBan());
            stmt.setString(2, phongBan.getTenPhongBan());
            stmt.setString(3, phongBan.getMoTa());
            stmt.setString(4, phongBan.getTruongPhong());
            stmt.setInt(5, phongBan.getSoNhanVien() != null ? phongBan.getSoNhanVien() : 0);
            stmt.setBoolean(6, phongBan.getTrangThaiHoatDong() != null ? phongBan.getTrangThaiHoatDong() : true);
            stmt.setLong(7, phongBan.getId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                System.out.println("Department updated successfully: " + phongBan.getId());
            }
            return success;
        }
    }

    /**
     * Xóa phòng ban theo ID
     */
    public boolean xoaPhongBan(Long id) throws SQLException {
        String sql = "DELETE FROM phong_ban WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                System.out.println("Department deleted successfully: " + id);
            }
            return success;
        }
    }

    /**
     * Đếm tổng số phòng ban
     */
    public int demTongSoPhongBan() throws SQLException {
        String sql = "SELECT COUNT(*) FROM phong_ban";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total department count: " + count);
                return count;
            }
        }
        return 0;
    }

    /**
     * Cập nhật số lượng nhân viên trong phòng ban
     */
    public boolean capNhatSoNhanVien(Long phongBanId) throws SQLException {
        String sql = "UPDATE phong_ban SET so_nhan_vien = " +
                "(SELECT COUNT(*) FROM nhan_vien WHERE phong_ban_id = ? AND trang_thai_hoat_dong = true) " +
                "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, phongBanId);
            stmt.setLong(2, phongBanId);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                System.out.println("Employee count updated for department: " + phongBanId);
            }
            return success;
        }
    }

    /**
     * Xóa tất cả dữ liệu trong bảng (dùng cho testing)
     */
    public void xoaTatCaDuLieu() throws SQLException {
        String sql = "DELETE FROM phong_ban";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int deletedRows = stmt.executeUpdate();
            System.out.println("Deleted all department data: " + deletedRows + " rows");
        }
    }

    /**
     * Helper method để tạo đối tượng PhongBan từ ResultSet
     */
    private PhongBan taoPhongBanTuResultSet(ResultSet rs) throws SQLException {
        PhongBan phongBan = new PhongBan();
        phongBan.setId(rs.getLong("id"));
        phongBan.setMaPhongBan(rs.getString("ma_phong_ban"));
        phongBan.setTenPhongBan(rs.getString("ten_phong_ban"));
        phongBan.setMoTa(rs.getString("mo_ta"));
        phongBan.setTruongPhong(rs.getString("truong_phong"));
        phongBan.setSoNhanVien(rs.getInt("so_nhan_vien"));
        phongBan.setTrangThaiHoatDong(rs.getBoolean("trang_thai_hoat_dong"));
        return phongBan;
    }
} 