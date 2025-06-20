package com.demo;

import com.demo.dao.NhanVienDAO;
import com.demo.dao.PhongBanDAO;
import com.demo.model.NhanVien;
import com.demo.model.PhongBan;
import com.demo.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Demo class thể hiện việc sử dụng khóa ngoại giữa bảng nhân viên và phòng ban với MySQL
 */
public class DemoPhongBanForeignKey {

    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            System.out.println("=== DEMO: Khóa ngoại giữa Nhân viên và Phòng ban (MySQL) ===\n");
            
            // Tạo kết nối MySQL database
            connection = DatabaseUtil.taoKetNoiMacDinh();
            
            // Khởi tạo DAO
            PhongBanDAO phongBanDAO = new PhongBanDAO(connection);
            NhanVienDAO nhanVienDAO = new NhanVienDAO(connection);
            
            // Tạo bảng (phòng ban trước, nhân viên sau vì có khóa ngoại)
            System.out.println("1. Tạo schema database...");
            phongBanDAO.taoBangPhongBan();
            nhanVienDAO.taoBangNhanVien();
            System.out.println("   ✓ Đã tạo bảng thành công với khóa ngoại và các trường mới\n");
            
            // Thêm dữ liệu phòng ban
            System.out.println("2. Thêm dữ liệu phòng ban...");
            themPhongBan(phongBanDAO);
            System.out.println("   ✓ Đã thêm các phòng ban\n");
            
            // Thêm nhân viên với khóa ngoại hợp lệ
            System.out.println("3. Thêm nhân viên với khóa ngoại hợp lệ và các trường mới...");
            themNhanVienHopLe(nhanVienDAO);
            System.out.println("   ✓ Đã thêm nhân viên thành công\n");
            
            // Test vi phạm khóa ngoại
            System.out.println("4. Test vi phạm khóa ngoại...");
            testViPhamKhoaNgoai(nhanVienDAO);
            System.out.println("   ✓ Khóa ngoại hoạt động đúng\n");
            
            // Test xóa phòng ban có nhân viên
            System.out.println("5. Test xóa phòng ban có nhân viên...");
            testXoaPhongBanCoNhanVien(phongBanDAO);
            System.out.println("   ✓ Referential integrity hoạt động đúng\n");
            
            // Hiển thị dữ liệu cuối cùng
            System.out.println("6. Dữ liệu cuối cùng với các trường mới:");
            hienThiDuLieu(phongBanDAO, nhanVienDAO);
            
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình demo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseUtil.dongKetNoi(connection);
        }
    }

    private static void themPhongBan(PhongBanDAO phongBanDAO) throws SQLException {
        PhongBan[] danhSachPhongBan = {
            new PhongBan(null, "IT", "Công nghệ thông tin", 
                "Phát triển phần mềm và hạ tầng IT", "Lê Hoàng Cường", 0, true),
            new PhongBan(null, "HR", "Nhân sự", 
                "Quản lý nhân sự và tuyển dụng", "Trần Thị Bình", 0, true),
            new PhongBan(null, "FIN", "Tài chính", 
                "Quản lý tài chính và kế toán", "Phạm Thị Dung", 0, true)
        };
        
        for (PhongBan phongBan : danhSachPhongBan) {
            phongBanDAO.themPhongBan(phongBan);
            System.out.println("   + Thêm phòng ban: " + phongBan.getTenPhongBan() + " (ID: " + phongBan.getId() + ")");
        }
    }

    private static void themNhanVienHopLe(NhanVienDAO nhanVienDAO) throws SQLException {
        NhanVien[] danhSachNhanVien = {
            taoNhanVien("Nguyễn Văn An", "an.nguyen@company.com", 1L, "Senior Developer", 25000000,
                LocalDateTime.of(1990, 5, 15, 8, 30), "0901234567", 
                "https://example.com/avatar/an.jpg", "Experienced developer with 5+ years",
                "Football", "DEVELOPER,TEAM_LEAD", "TechCorp"),
            taoNhanVien("Trần Thị Bình", "binh.tran@company.com", 2L, "HR Manager", 30000000,
                LocalDateTime.of(1985, 8, 22, 10, 15), "0902345678",
                "https://example.com/avatar/binh.jpg", "HR professional specializing in talent acquisition",
                "Tennis", "HR_MANAGER,ADMIN", "TechCorp"),
            taoNhanVien("Lê Hoàng Cường", "cuong.le@company.com", 1L, "Project Manager", 35000000,
                LocalDateTime.of(1982, 12, 3, 14, 20), "0903456789",
                "https://example.com/avatar/cuong.jpg", "Project management expert with Agile",
                "Basketball", "PROJECT_MANAGER,SCRUM_MASTER", "TechCorp"),
            taoNhanVien("Phạm Thị Dung", "dung.pham@company.com", 3L, "Accountant", 20000000,
                LocalDateTime.of(1993, 4, 18, 9, 45), "0904567890",
                "https://example.com/avatar/dung.jpg", "Financial analyst with budgeting expertise",
                "Swimming", "ACCOUNTANT,ANALYST", "TechCorp")
        };
        
        for (NhanVien nhanVien : danhSachNhanVien) {
            nhanVienDAO.themNhanVien(nhanVien);
            System.out.println("   + Thêm nhân viên: " + nhanVien.getHoTen() + 
                " (Phòng ban ID: " + nhanVien.getPhongBanId() + ", Phone: " + nhanVien.getPhoneNumber() + ")");
        }
    }
    
    private static NhanVien taoNhanVien(String hoTen, String email, Long phongBanId, String chucVu, long luong,
                                       LocalDateTime dateOfBirth, String phoneNumber, String avatarUrl,
                                       String bio, String sport, String roles, String company) {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen(hoTen);
        nhanVien.setEmail(email);
        nhanVien.setPhongBanId(phongBanId);
        nhanVien.setChucVu(chucVu);
        nhanVien.setLuong(new BigDecimal(luong));
        nhanVien.setNgayVaoLam(new Date());
        nhanVien.setTrangThaiHoatDong(true);
        
        // Thêm các trường mới
        nhanVien.setDateOfBirth(dateOfBirth);
        nhanVien.setPhoneNumber(phoneNumber);
        nhanVien.setAvatarUrl(avatarUrl);
        nhanVien.setBio(bio);
        nhanVien.setSport(sport);
        nhanVien.setRoles(roles);
        nhanVien.setCompany(company);
        
        return nhanVien;
    }

    private static void testViPhamKhoaNgoai(NhanVienDAO nhanVienDAO) {
        try {
            NhanVien nhanVienLoi = taoNhanVien("Test Error", "error@company.com", 999L, "Developer", 20000000,
                LocalDateTime.of(1990, 1, 1, 12, 0), "0900000000",
                "https://example.com/avatar/error.jpg", "Test user for error case",
                "Running", "DEVELOPER", "TestCorp");
            nhanVienDAO.themNhanVien(nhanVienLoi);
            System.out.println("   ❌ KHÔNG THỂ XẢY RA: Đã thêm nhân viên với phòng ban không tồn tại!");
        } catch (SQLException e) {
            System.out.println("   ✓ Đã bắt được lỗi vi phạm khóa ngoại: " + e.getMessage());
        }
    }

    private static void testXoaPhongBanCoNhanVien(PhongBanDAO phongBanDAO) {
        try {
            phongBanDAO.xoaPhongBan(1L); // Thử xóa phòng ban IT có nhân viên
            System.out.println("   ❌ KHÔNG THỂ XẢY RA: Đã xóa phòng ban có nhân viên!");
        } catch (SQLException e) {
            System.out.println("   ✓ Đã bắt được lỗi vi phạm referential integrity: " + e.getMessage());
        }
    }

    private static void hienThiDuLieu(PhongBanDAO phongBanDAO, NhanVienDAO nhanVienDAO) throws SQLException {
        System.out.println("\n--- PHÒNG BAN ---");
        List<PhongBan> danhSachPhongBan = phongBanDAO.layTatCaPhongBan();
        for (PhongBan pb : danhSachPhongBan) {
            System.out.printf("ID: %d | Mã: %s | Tên: %s%n", 
                pb.getId(), pb.getMaPhongBan(), pb.getTenPhongBan());
        }
        
        System.out.println("\n--- NHÂN VIÊN (với các trường mới) ---");
        List<NhanVien> danhSachNhanVien = nhanVienDAO.layTatCaNhanVien();
        for (NhanVien nv : danhSachNhanVien) {
            System.out.printf("ID: %d | Tên: %s | Phòng ban ID: %s | Phone: %s | Sport: %s | Company: %s%n", 
                nv.getId(), nv.getHoTen(), nv.getPhongBanId(), nv.getPhoneNumber(), nv.getSport(), nv.getCompany());
        }
    }
} 