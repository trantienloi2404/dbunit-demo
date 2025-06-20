package com.demo.test;

import com.demo.base.DBUnitBaseTest;
import com.demo.dao.NhanVienDAO;
import com.demo.dao.PhongBanDAO;
import com.demo.model.NhanVien;
import com.demo.model.PhongBan;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test class để kiểm tra các trường mới trong model NhanVien
 * và kết nối MySQL
 */
public class NewFieldsTest extends DBUnitBaseTest {
    
    private NhanVienDAO nhanVienDAO;
    private PhongBanDAO phongBanDAO;
    private Long testPhongBanId;

    @Override
    protected void thieLapSchema() throws SQLException {
        // Tạo bảng phòng ban trước (bảng cha)
        phongBanDAO = new PhongBanDAO(connection);
        phongBanDAO.taoBangPhongBan();
        
        // Tạo bảng nhân viên sau (bảng con với khóa ngoại)
        nhanVienDAO = new NhanVienDAO(connection);
        nhanVienDAO.taoBangNhanVien();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        // Tạo phòng ban với ID unique cho mỗi test
        String testMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String shortCode = "T" + Math.abs(testMethodName.hashCode() % 1000);
        PhongBan phongBan = new PhongBan();
        phongBan.setMaPhongBan(shortCode);
        phongBan.setTenPhongBan("Test Dept " + shortCode);
        phongBan.setTrangThaiHoatDong(true);
        phongBanDAO.themPhongBan(phongBan);
        
        // Lưu ID phòng ban để sử dụng trong test
        this.testPhongBanId = phongBan.getId();
    }

    @Test
    public void testThemNhanVienVoiCacTruongMoi() throws Exception {
        // Tạo nhân viên với các trường mới
        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen("Nguyễn Test User");
        nhanVien.setEmail("test@company.com");
        nhanVien.setPhongBanId(testPhongBanId);
        nhanVien.setChucVu("Full Stack Developer");
        nhanVien.setLuong(new BigDecimal("30000000"));
        nhanVien.setNgayVaoLam(new Date());
        nhanVien.setTrangThaiHoatDong(true);
        
        // Thêm các trường mới
        nhanVien.setDateOfBirth(LocalDateTime.of(1990, 5, 15, 8, 30));
        nhanVien.setPhoneNumber("0901234567");
        nhanVien.setAvatarUrl("https://example.com/avatar/test.jpg");
        nhanVien.setBio("Experienced full-stack developer with expertise in Java and React");
        nhanVien.setSport("Football");
        nhanVien.setRoles("DEVELOPER,TEAM_LEAD,MENTOR");
        nhanVien.setCompany("TechCorp Vietnam");
        
        // Thêm vào database
        nhanVienDAO.themNhanVien(nhanVien);
        assertNotNull("ID phải được generate", nhanVien.getId());
        
        // Lấy lại từ database để kiểm tra
        NhanVien nhanVienDaLuu = nhanVienDAO.timNhanVienTheoId(nhanVien.getId());
        assertNotNull("Nhân viên phải tồn tại", nhanVienDaLuu);
        
        // Kiểm tra các trường cơ bản
        assertEquals("Nguyễn Test User", nhanVienDaLuu.getHoTen());
        assertEquals("test@company.com", nhanVienDaLuu.getEmail());
        assertEquals(testPhongBanId, nhanVienDaLuu.getPhongBanId());
        
        // Kiểm tra các trường mới
        assertNotNull("Date of birth phải được lưu", nhanVienDaLuu.getDateOfBirth());
        assertEquals(LocalDateTime.of(1990, 5, 15, 8, 30), nhanVienDaLuu.getDateOfBirth());
        assertEquals("0901234567", nhanVienDaLuu.getPhoneNumber());
        assertEquals("https://example.com/avatar/test.jpg", nhanVienDaLuu.getAvatarUrl());
        assertEquals("Experienced full-stack developer with expertise in Java and React", nhanVienDaLuu.getBio());
        assertEquals("Football", nhanVienDaLuu.getSport());
        assertEquals("DEVELOPER,TEAM_LEAD,MENTOR", nhanVienDaLuu.getRoles());
        assertEquals("TechCorp Vietnam", nhanVienDaLuu.getCompany());
    }

    @Test
    public void testCapNhatCacTruongMoi() throws Exception {
        // Tạo và thêm nhân viên
        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen("Test Update User");
        nhanVien.setEmail("update@company.com");
        nhanVien.setPhongBanId(testPhongBanId);
        nhanVien.setChucVu("Developer");
        nhanVien.setLuong(new BigDecimal("25000000"));
        nhanVien.setNgayVaoLam(new Date());
        nhanVien.setTrangThaiHoatDong(true);
        nhanVien.setDateOfBirth(LocalDateTime.of(1985, 1, 1, 10, 0));
        nhanVien.setPhoneNumber("0900000000");
        
        nhanVienDAO.themNhanVien(nhanVien);
        
        // Cập nhật các trường mới
        nhanVien.setPhoneNumber("0911111111");
        nhanVien.setAvatarUrl("https://example.com/new-avatar.jpg");
        nhanVien.setBio("Updated bio information");
        nhanVien.setSport("Basketball");
        nhanVien.setRoles("SENIOR_DEVELOPER");
        nhanVien.setCompany("New Company Inc");
        nhanVien.setDateOfBirth(LocalDateTime.of(1985, 6, 15, 14, 30));
        
        // Thực hiện cập nhật
        boolean ketQua = nhanVienDAO.capNhatNhanVien(nhanVien);
        assertTrue("Cập nhật phải thành công", ketQua);
        
        // Kiểm tra dữ liệu đã được cập nhật
        NhanVien nhanVienCapNhat = nhanVienDAO.timNhanVienTheoId(nhanVien.getId());
        assertEquals("0911111111", nhanVienCapNhat.getPhoneNumber());
        assertEquals("https://example.com/new-avatar.jpg", nhanVienCapNhat.getAvatarUrl());
        assertEquals("Updated bio information", nhanVienCapNhat.getBio());
        assertEquals("Basketball", nhanVienCapNhat.getSport());
        assertEquals("SENIOR_DEVELOPER", nhanVienCapNhat.getRoles());
        assertEquals("New Company Inc", nhanVienCapNhat.getCompany());
        assertEquals(LocalDateTime.of(1985, 6, 15, 14, 30), nhanVienCapNhat.getDateOfBirth());
    }

    @Test
    public void testCacTruongMoiCoTheNull() throws Exception {
        // Tạo nhân viên với các trường mới là null
        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen("Null Fields User");
        nhanVien.setEmail("nullfields@company.com");
        nhanVien.setPhongBanId(testPhongBanId);
        nhanVien.setChucVu("Intern");
        nhanVien.setLuong(new BigDecimal("10000000"));
        nhanVien.setNgayVaoLam(new Date());
        nhanVien.setTrangThaiHoatDong(true);
        
        // Không set các trường mới (để null)
        
        // Thêm vào database
        nhanVienDAO.themNhanVien(nhanVien);
        assertNotNull("ID phải được generate", nhanVien.getId());
        
        // Lấy lại từ database để kiểm tra
        NhanVien nhanVienDaLuu = nhanVienDAO.timNhanVienTheoId(nhanVien.getId());
        assertNotNull("Nhân viên phải tồn tại", nhanVienDaLuu);
        
        // Kiểm tra các trường mới có thể null
        assertNull("Date of birth có thể null", nhanVienDaLuu.getDateOfBirth());
        assertNull("Phone number có thể null", nhanVienDaLuu.getPhoneNumber());
        assertNull("Avatar URL có thể null", nhanVienDaLuu.getAvatarUrl());
        assertNull("Bio có thể null", nhanVienDaLuu.getBio());
        assertNull("Sport có thể null", nhanVienDaLuu.getSport());
        assertNull("Roles có thể null", nhanVienDaLuu.getRoles());
        assertNull("Company có thể null", nhanVienDaLuu.getCompany());
    }
} 