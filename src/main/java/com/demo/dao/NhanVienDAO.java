package com.demo.dao;

import com.demo.model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho thao tác với bảng nhân viên
 * Sử dụng cho demo DBUnit testing
 */
public class NhanVienDAO {
    private Connection connection;

    public NhanVienDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Tạo bảng nhân viên trong database
     */
    public void taoBangNhanVien() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS nhan_vien (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "ho_ten VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phong_ban VARCHAR(50), " +
                "phong_ban_id BIGINT, " +
                "chuc_vu VARCHAR(50), " +
                "luong DECIMAL(15,2), " +
                "ngay_vao_lam DATE, " +
                "trang_thai_hoat_dong BOOLEAN DEFAULT TRUE, " +
                "date_of_birth DATETIME, " +
                "phone_number VARCHAR(20), " +
                "avatar_url VARCHAR(255), " +
                "bio TEXT, " +
                "sport VARCHAR(100), " +
                "roles VARCHAR(100), " +
                "company VARCHAR(100), " +
                "FOREIGN KEY (phong_ban_id) REFERENCES phong_ban(id)" +
                ")";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("Employee table created successfully");
        }
    }

    /**
     * Thêm nhân viên mới vào database
     */
    public void themNhanVien(NhanVien nhanVien) throws SQLException {
        String sql = "INSERT INTO nhan_vien (ho_ten, email, phong_ban, phong_ban_id, chuc_vu, luong, ngay_vao_lam, " +
                "trang_thai_hoat_dong, date_of_birth, phone_number, avatar_url, bio, sport, roles, company) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nhanVien.getHoTen());
            stmt.setString(2, nhanVien.getEmail());
            stmt.setString(3, nhanVien.getPhongBan());
            if (nhanVien.getPhongBanId() != null) {
                stmt.setLong(4, nhanVien.getPhongBanId());
            } else {
                stmt.setNull(4, java.sql.Types.BIGINT);
            }
            stmt.setString(5, nhanVien.getChucVu());
            stmt.setBigDecimal(6, nhanVien.getLuong());
            stmt.setDate(7, nhanVien.getNgayVaoLam() != null ? new java.sql.Date(nhanVien.getNgayVaoLam().getTime()) : null);
            stmt.setBoolean(8, nhanVien.getTrangThaiHoatDong() != null ? nhanVien.getTrangThaiHoatDong() : true);
            
            // Thêm các trường mới
            if (nhanVien.getDateOfBirth() != null) {
                stmt.setTimestamp(9, java.sql.Timestamp.valueOf(nhanVien.getDateOfBirth()));
            } else {
                stmt.setNull(9, java.sql.Types.TIMESTAMP);
            }
            stmt.setString(10, nhanVien.getPhoneNumber());
            stmt.setString(11, nhanVien.getAvatarUrl());
            stmt.setString(12, nhanVien.getBio());
            stmt.setString(13, nhanVien.getSport());
            stmt.setString(14, nhanVien.getRoles());
            stmt.setString(15, nhanVien.getCompany());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nhanVien.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Employee added successfully with ID: " + nhanVien.getId());
            }
        }
    }

    /**
     * Tìm nhân viên theo ID
     */
    public NhanVien timNhanVienTheoId(Long id) throws SQLException {
        String sql = "SELECT * FROM nhan_vien WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return taoNhanVienTuResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy tất cả nhân viên
     */
    public List<NhanVien> layTatCaNhanVien() throws SQLException {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien ORDER BY id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachNhanVien.add(taoNhanVienTuResultSet(rs));
            }
        }
        
        System.out.println("Retrieved " + danhSachNhanVien.size() + " employees from database");
        return danhSachNhanVien;
    }

    /**
     * Tìm nhân viên theo phòng ban
     */
    public List<NhanVien> timNhanVienTheoPhongBan(String phongBan) throws SQLException {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien WHERE phong_ban = ? ORDER BY ho_ten";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phongBan);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    danhSachNhanVien.add(taoNhanVienTuResultSet(rs));
                }
            }
        }
        
        System.out.println("Found " + danhSachNhanVien.size() + " employees in department: " + phongBan);
        return danhSachNhanVien;
    }

    /**
     * Cập nhật thông tin nhân viên
     */
    public boolean capNhatNhanVien(NhanVien nhanVien) throws SQLException {
        String sql = "UPDATE nhan_vien SET ho_ten = ?, email = ?, phong_ban = ?, phong_ban_id = ?, chuc_vu = ?, " +
                "luong = ?, ngay_vao_lam = ?, trang_thai_hoat_dong = ?, date_of_birth = ?, phone_number = ?, " +
                "avatar_url = ?, bio = ?, sport = ?, roles = ?, company = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nhanVien.getHoTen());
            stmt.setString(2, nhanVien.getEmail());
            stmt.setString(3, nhanVien.getPhongBan());
            if (nhanVien.getPhongBanId() != null) {
                stmt.setLong(4, nhanVien.getPhongBanId());
            } else {
                stmt.setNull(4, java.sql.Types.BIGINT);
            }
            stmt.setString(5, nhanVien.getChucVu());
            stmt.setBigDecimal(6, nhanVien.getLuong());
            stmt.setDate(7, nhanVien.getNgayVaoLam() != null ? new java.sql.Date(nhanVien.getNgayVaoLam().getTime()) : null);
            stmt.setBoolean(8, nhanVien.getTrangThaiHoatDong() != null ? nhanVien.getTrangThaiHoatDong() : true);
            
            // Cập nhật các trường mới
            if (nhanVien.getDateOfBirth() != null) {
                stmt.setTimestamp(9, java.sql.Timestamp.valueOf(nhanVien.getDateOfBirth()));
            } else {
                stmt.setNull(9, java.sql.Types.TIMESTAMP);
            }
            stmt.setString(10, nhanVien.getPhoneNumber());
            stmt.setString(11, nhanVien.getAvatarUrl());
            stmt.setString(12, nhanVien.getBio());
            stmt.setString(13, nhanVien.getSport());
            stmt.setString(14, nhanVien.getRoles());
            stmt.setString(15, nhanVien.getCompany());
            stmt.setLong(16, nhanVien.getId());
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                System.out.println("Employee updated successfully: " + nhanVien.getId());
            }
            return success;
        }
    }

    /**
     * Xóa nhân viên theo ID
     */
    public boolean xoaNhanVien(Long id) throws SQLException {
        String sql = "DELETE FROM nhan_vien WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            boolean success = affectedRows > 0;
            
            if (success) {
                System.out.println("Employee deleted successfully: " + id);
            }
            return success;
        }
    }

    /**
     * Đếm tổng số nhân viên
     */
    public int demTongSoNhanVien() throws SQLException {
        String sql = "SELECT COUNT(*) FROM nhan_vien";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Total employee count: " + count);
                return count;
            }
        }
        return 0;
    }

    /**
     * Xóa tất cả dữ liệu trong bảng (dùng cho testing)
     */
    public void xoaTatCaDuLieu() throws SQLException {
        String sql = "DELETE FROM nhan_vien";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int deletedRows = stmt.executeUpdate();
            System.out.println("Deleted all data: " + deletedRows + " rows");
        }
    }

    /**
     * Helper method để tạo đối tượng NhanVien từ ResultSet
     */
    private NhanVien taoNhanVienTuResultSet(ResultSet rs) throws SQLException {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setId(rs.getLong("id"));
        nhanVien.setHoTen(rs.getString("ho_ten"));
        nhanVien.setEmail(rs.getString("email"));
        nhanVien.setPhongBan(rs.getString("phong_ban"));
        
        // Xử lý khóa ngoại phong_ban_id
        Long phongBanId = rs.getLong("phong_ban_id");
        if (!rs.wasNull()) {
            nhanVien.setPhongBanId(phongBanId);
        }
        
        nhanVien.setChucVu(rs.getString("chuc_vu"));
        nhanVien.setLuong(rs.getBigDecimal("luong"));
        
        Date ngayVaoLam = rs.getDate("ngay_vao_lam");
        if (ngayVaoLam != null) {
            nhanVien.setNgayVaoLam(new java.util.Date(ngayVaoLam.getTime()));
        }
        
        nhanVien.setTrangThaiHoatDong(rs.getBoolean("trang_thai_hoat_dong"));
        
        // Xử lý các trường mới
        java.sql.Timestamp dateOfBirth = rs.getTimestamp("date_of_birth");
        if (dateOfBirth != null) {
            nhanVien.setDateOfBirth(dateOfBirth.toLocalDateTime());
        }
        
        nhanVien.setPhoneNumber(rs.getString("phone_number"));
        nhanVien.setAvatarUrl(rs.getString("avatar_url"));
        nhanVien.setBio(rs.getString("bio"));
        nhanVien.setSport(rs.getString("sport"));
        nhanVien.setRoles(rs.getString("roles"));
        nhanVien.setCompany(rs.getString("company"));
        
        return nhanVien;
    }
} 