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
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test class để kiểm tra tính toàn vẹn khóa ngoại
 * giữa bảng nhân viên và phòng ban
 */
public class ForeignKeyIntegrityTest extends DBUnitBaseTest {
    
    private NhanVienDAO nhanVienDAO;
    private PhongBanDAO phongBanDAO;

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
        // Load dữ liệu test với khóa ngoại
        insertCleanData("dataset/full-dataset-with-foreign-key.xml");
    }

    @Test
    public void testThemNhanVienVoiPhongBanTonTai() throws Exception {
        NhanVien nhanVienMoi = new NhanVien();
        nhanVienMoi.setHoTen("Nguyễn Test User");
        nhanVienMoi.setEmail("test@company.com");
        nhanVienMoi.setPhongBanId(1L); // Phòng ban IT tồn tại
        nhanVienMoi.setChucVu("Developer");
        nhanVienMoi.setLuong(new BigDecimal("20000000"));
        nhanVienMoi.setNgayVaoLam(new Date());
        nhanVienMoi.setTrangThaiHoatDong(true);
        
        // Thêm nhân viên - phải thành công
        nhanVienDAO.themNhanVien(nhanVienMoi);
        assertNotNull("Nhân viên phải được thêm thành công", nhanVienMoi.getId());
    }

    @Test
    public void testThemNhanVienVoiPhongBanKhongTonTai() throws Exception {
        NhanVien nhanVienLoi = new NhanVien();
        nhanVienLoi.setHoTen("Nguyễn Lỗi User");
        nhanVienLoi.setEmail("error@company.com");
        nhanVienLoi.setPhongBanId(999L); // Phòng ban không tồn tại
        nhanVienLoi.setChucVu("Developer");
        nhanVienLoi.setLuong(new BigDecimal("20000000"));
        nhanVienLoi.setNgayVaoLam(new Date());
        nhanVienLoi.setTrangThaiHoatDong(true);
        
        try {
            nhanVienDAO.themNhanVien(nhanVienLoi);
            fail("Phải có exception vì vi phạm khóa ngoại");
        } catch (SQLException e) {
            // Mong muốn có SQLException vì vi phạm foreign key constraint
            String message = e.getMessage().toLowerCase();
            assertTrue("Exception phải liên quan đến foreign key constraint", 
                message.contains("foreign key") || 
                message.contains("constraint") ||
                message.contains("referential"));
        }
    }

    @Test
    public void testXoaPhongBanCoNhanVien() throws Exception {
        try {
            // Thử xóa phòng ban IT (có nhân viên)
            phongBanDAO.xoaPhongBan(1L);
            fail("Phải có exception vì phòng ban có nhân viên");
        } catch (SQLException e) {
            // Mong muốn có SQLException vì vi phạm referential integrity
            String message = e.getMessage().toLowerCase();
            assertTrue("Exception phải liên quan đến referential integrity", 
                message.contains("foreign key") || 
                message.contains("constraint") ||
                message.contains("referential"));
        }
    }
} 