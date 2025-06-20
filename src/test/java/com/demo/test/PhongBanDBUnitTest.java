package com.demo.test;

import com.demo.base.DBUnitBaseTest;
import com.demo.dao.PhongBanDAO;
import com.demo.model.PhongBan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Test class cho PhongBanDAO sử dụng DBUnit
 * Kiểm tra các chức năng CRUD và tính toàn vẹn dữ liệu
 */
public class PhongBanDBUnitTest extends DBUnitBaseTest {
    
    private PhongBanDAO phongBanDAO;

    @Override
    protected void thieLapSchema() throws SQLException {
        phongBanDAO = new PhongBanDAO(connection);
        phongBanDAO.taoBangPhongBan();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        // Load dữ liệu test từ XML file
        insertCleanData("dataset/phong-ban-initial.xml");
    }

    @Test
    public void testLayTatCaPhongBan() throws Exception {
        // Lấy tất cả phòng ban
        List<PhongBan> danhSach = phongBanDAO.layTatCaPhongBan();
        
        // Kiểm tra số lượng
        assertEquals("Số lượng phòng ban phải là 4", 4, danhSach.size());
        
        // Kiểm tra dữ liệu phòng ban đầu tiên
        PhongBan phongBanIT = danhSach.get(0);
        assertEquals("IT", phongBanIT.getMaPhongBan());
        assertEquals("Công nghệ thông tin", phongBanIT.getTenPhongBan());
        assertEquals("Lê Hoàng Cường", phongBanIT.getTruongPhong());
        assertTrue(phongBanIT.getTrangThaiHoatDong());
    }

    @Test
    public void testTimPhongBanTheoId() throws Exception {
        // Tìm phòng ban theo ID
        PhongBan phongBan = phongBanDAO.timPhongBanTheoId(1L);
        
        assertNotNull("Phòng ban phải tồn tại", phongBan);
        assertEquals("IT", phongBan.getMaPhongBan());
        assertEquals("Công nghệ thông tin", phongBan.getTenPhongBan());
    }

    @Test
    public void testTimPhongBanTheoMa() throws Exception {
        // Tìm phòng ban theo mã
        PhongBan phongBan = phongBanDAO.timPhongBanTheoMa("HR");
        
        assertNotNull("Phòng ban HR phải tồn tại", phongBan);
        assertEquals(Long.valueOf(2), phongBan.getId());
        assertEquals("Nhân sự", phongBan.getTenPhongBan());
        assertEquals("Trần Thị Bình", phongBan.getTruongPhong());
    }

    @Test
    public void testThemPhongBanMoi() throws Exception {
        // Tạo phòng ban mới
        PhongBan phongBanMoi = new PhongBan();
        phongBanMoi.setMaPhongBan("QA");
        phongBanMoi.setTenPhongBan("Quality Assurance");
        phongBanMoi.setMoTa("Phòng ban kiểm thử chất lượng");
        phongBanMoi.setTruongPhong("Nguyễn QA Manager");
        phongBanMoi.setSoNhanVien(0);
        phongBanMoi.setTrangThaiHoatDong(true);
        
        // Thêm vào database
        phongBanDAO.themPhongBan(phongBanMoi);
        
        assertNotNull("ID phải được generate", phongBanMoi.getId());
        
        // Kiểm tra dữ liệu đã được thêm
        PhongBan phongBanDaLuu = phongBanDAO.timPhongBanTheoMa("QA");
        assertNotNull("Phòng ban mới phải tồn tại", phongBanDaLuu);
        assertEquals("Quality Assurance", phongBanDaLuu.getTenPhongBan());
    }

    @Test
    public void testCapNhatPhongBan() throws Exception {
        // Lấy phòng ban cần cập nhật
        PhongBan phongBan = phongBanDAO.timPhongBanTheoId(3L);
        assertNotNull(phongBan);
        
        // Cập nhật thông tin
        phongBan.setTruongPhong("Nguyễn Tài chính mới");
        phongBan.setSoNhanVien(2);
        phongBan.setMoTa("Phòng ban tài chính đã được cập nhật");
        
        // Thực hiện cập nhật
        boolean ketQua = phongBanDAO.capNhatPhongBan(phongBan);
        assertTrue("Cập nhật phải thành công", ketQua);
        
        // Kiểm tra dữ liệu đã được cập nhật
        PhongBan phongBanCapNhat = phongBanDAO.timPhongBanTheoId(3L);
        assertEquals("Nguyễn Tài chính mới", phongBanCapNhat.getTruongPhong());
        assertEquals(Integer.valueOf(2), phongBanCapNhat.getSoNhanVien());
    }

    @Test
    public void testXoaPhongBan() throws Exception {
        // Xóa phòng ban Marketing (không có nhân viên)
        boolean ketQua = phongBanDAO.xoaPhongBan(4L);
        assertTrue("Xóa phải thành công", ketQua);
        
        // Kiểm tra phòng ban đã bị xóa
        PhongBan phongBanDaXoa = phongBanDAO.timPhongBanTheoId(4L);
        assertNull("Phòng ban phải bị xóa", phongBanDaXoa);
    }

    @Test
    public void testDemTongSoPhongBan() throws Exception {
        int tongSo = phongBanDAO.demTongSoPhongBan();
        assertEquals("Tổng số phòng ban phải là 4", 4, tongSo);
    }

    @Test
    public void testTimPhongBanKhongTonTai() throws Exception {
        PhongBan phongBan = phongBanDAO.timPhongBanTheoId(999L);
        assertNull("Phòng ban không tồn tại phải trả về null", phongBan);
        
        PhongBan phongBanTheoMa = phongBanDAO.timPhongBanTheoMa("XXX");
        assertNull("Phòng ban với mã không tồn tại phải trả về null", phongBanTheoMa);
    }

    @Test
    public void testMaPhongBanDuyNhat() throws Exception {
        // Thử tạo phòng ban với mã đã tồn tại
        PhongBan phongBanTrung = new PhongBan();
        phongBanTrung.setMaPhongBan("IT"); // Mã đã tồn tại
        phongBanTrung.setTenPhongBan("IT Thứ 2");
        phongBanTrung.setTrangThaiHoatDong(true);
        
        try {
            phongBanDAO.themPhongBan(phongBanTrung);
            fail("Phải có exception vì mã phòng ban trùng lặp");
        } catch (Exception e) {
            // Mong muốn có exception
            assertTrue("Exception phải liên quan đến constraint", 
                e.getMessage().toLowerCase().contains("duplicate") || 
                e.getMessage().toLowerCase().contains("unique"));
        }
    }
} 