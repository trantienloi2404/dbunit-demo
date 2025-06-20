# Bảng Phòng Ban và Khóa Ngoại - DBUnit Demo

## Tổng quan

Đã thêm bảng **phòng ban** vào hệ thống để demo việc test khóa ngoại với DBUnit. Bảng nhân viên hiện có khóa ngoại tới bảng phòng ban để đảm bảo tính toàn vẹn dữ liệu.

## Cấu trúc bảng

### Bảng `phong_ban` (Bảng cha)
- id: BIGINT PRIMARY KEY AUTO_INCREMENT
- ma_phong_ban: VARCHAR(10) UNIQUE NOT NULL  
- ten_phong_ban: VARCHAR(100) NOT NULL
- mo_ta: TEXT
- truong_phong: VARCHAR(100)
- so_nhan_vien: INT DEFAULT 0
- trang_thai_hoat_dong: BOOLEAN DEFAULT TRUE

### Bảng `nhan_vien` (Bảng con - đã cập nhật)
- Đã thêm: phong_ban_id BIGINT (khóa ngoại)
- FOREIGN KEY (phong_ban_id) REFERENCES phong_ban(id)

## Các file đã thêm/cập nhật

### Model Classes
- PhongBan.java - Model cho bảng phòng ban
- NhanVien.java - Đã thêm field phongBanId

### DAO Classes  
- PhongBanDAO.java - CRUD operations cho phòng ban
- NhanVienDAO.java - Đã cập nhật để hỗ trợ khóa ngoại

### Test Classes
- PhongBanDBUnitTest.java - Test CRUD cho phòng ban (9 test cases)
- ForeignKeyIntegrityTest.java - Test tính toàn vẹn khóa ngoại (3 test cases)

### Dataset Files
- phong-ban-initial.xml - Dữ liệu test cho phòng ban
- nhan-vien-initial.xml - Đã cập nhật với khóa ngoại
- full-dataset-with-foreign-key.xml - Dataset đầy đủ

### Demo Class
- DemoPhongBanForeignKey.java - Demo cách sử dụng khóa ngoại

## Cách chạy test

```bash
# Test phòng ban
mvn test -Dtest=PhongBanDBUnitTest

# Test khóa ngoại  
mvn test -Dtest=ForeignKeyIntegrityTest

# Chạy demo
mvn exec:java -Dexec.mainClass="com.demo.DemoPhongBanForeignKey"
```

## Kết quả
- PhongBanDBUnitTest: 9/9 tests passed ✅
- ForeignKeyIntegrityTest: 3/3 tests passed ✅
- Demo: Foreign key constraints working correctly ✅

## Dữ liệu mẫu

### Phòng ban
| ID | Mã | Tên | Trưởng phòng | Trạng thái |
|----|----|----|-------------|-----------|
| 1 | IT | Công nghệ thông tin | Lê Hoàng Cường | Active |
| 2 | HR | Nhân sự | Trần Thị Bình | Active |  
| 3 | FIN | Tài chính | Phạm Thị Dung | Active |
| 4 | MKT | Marketing | Nguyễn Văn Marketing | Inactive |

### Nhân viên (với khóa ngoại)
| ID | Họ tên | Email | Phòng ban ID | Chức vụ |
|----|---------|-------|-------------|---------|
| 1 | Nguyễn Văn An | an.nguyen@company.com | 1 (IT) | Senior Developer |
| 2 | Trần Thị Bình | binh.tran@company.com | 2 (HR) | HR Manager |
| 3 | Lê Hoàng Cường | cuong.le@company.com | 1 (IT) | Project Manager |
| 4 | Phạm Thị Dung | dung.pham@company.com | 3 (FIN) | Accountant |
| 5 | Võ Minh Đức | duc.vo@company.com | 1 (IT) | Junior Developer |

## Lợi ích của việc sử dụng khóa ngoại

1. **Tính toàn vẹn dữ liệu** - Đảm bảo nhân viên chỉ thuộc phòng ban tồn tại
2. **Referential integrity** - Không thể xóa phòng ban còn có nhân viên
3. **Data consistency** - Dữ liệu luôn nhất quán giữa các bảng
4. **Error detection** - Phát hiện lỗi sớm khi dữ liệu không hợp lệ

## Best Practices đã áp dụng

1. **Tạo bảng cha trước** - Phòng ban được tạo trước nhân viên
2. **Backward compatibility** - Giữ lại field `phong_ban` cũ
3. **Comprehensive testing** - Test cả trường hợp thành công và thất bại
4. **Clean test data** - Sử dụng CLEAN_INSERT để đảm bảo test độc lập
5. **Meaningful assertions** - Các assertion rõ ràng và có ý nghĩa

## Kết quả test

```
PhongBanDBUnitTest: 9/9 tests passed ✅
ForeignKeyIntegrityTest: 3/3 tests passed ✅
Demo: Foreign key constraints working correctly ✅
```

Việc thêm bảng phòng ban và khóa ngoại đã thành công và sẵn sàng cho việc học tập và demo về DBUnit testing với quan hệ giữa các bảng. 