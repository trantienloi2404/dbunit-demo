# DBUnit Demo Project v2.0

Đồ án demo các chức năng chính và thường gặp của **DBUnit** - framework testing cho database-driven applications trong Java. **Phiên bản 2.0** bao gồm **MySQL integration**, **Foreign Key constraints**, và **Extended Employee Model**.

## 📋 Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng mới v2.0](#tính-năng-mới-v20)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc project](#cấu-trúc-project)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Database Configuration](#database-configuration)
- [Extended Employee Model](#extended-employee-model)
- [Các chức năng DBUnit được demo](#các-chức-năng-dbunit-được-demo)
- [Chi tiết implementation](#chi-tiết-implementation)
- [Kết quả mong đợi](#kết-quả-mong-đợi)

## 🎯 Giới thiệu

Project này minh họa việc sử dụng **DBUnit** để test các ứng dụng Java có sử dụng database. DBUnit là một extension của JUnit giúp:

- **Setup và cleanup** dữ liệu test một cách tự động
- **So sánh dataset** giữa expected và actual data
- **Import/Export** dữ liệu từ/ra XML files
- **Verify database state** sau khi thực hiện operations
- **Error handling** và rollback trong testing
- **Foreign key constraints** và referential integrity testing
- **Multi-table relationships** và complex data scenarios

## 🆕 Tính năng mới v2.0

### 🗄️ MySQL Integration
- **Primary Database**: MySQL với auto-configuration
- **Fallback Mechanism**: Tự động chuyển sang H2 nếu MySQL không khả dụng
- **Production-Ready**: Thích hợp cho testing với production-like database

### 🔗 Foreign Key Support
- **Department-Employee Relationship**: Bảng phòng ban và nhân viên với foreign key
- **Referential Integrity**: Test ràng buộc dữ liệu và consistency
- **Constraint Violation Handling**: Demo error cases và exception handling

### 👥 Extended Employee Model
Mở rộng từ 8 trường cơ bản lên **14 trường** bao gồm:
- **Personal Info**: dateOfBirth, phoneNumber, bio
- **Professional**: avatarUrl, sport, roles, company
- **Enhanced Testing**: Kiểm tra NULL values, data validation

### 🧪 Enhanced Test Suite
- **25 test cases** (tăng từ 10)
- **4 test classes** với các scenarios khác nhau
- **Multi-table operations** và relationship testing

## 🛠 Công nghệ sử dụng

- **Java 11+**
- **Maven** - Dependency management
- **JUnit 4** - Unit testing framework
- **DBUnit 2.7.3** - Database testing framework
- **MySQL 8.0+** - Primary database cho testing
- **H2 Database** - Fallback in-memory database
- **SLF4J** - Logging framework

## 📁 Cấu trúc project

```
dbunit-demo/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/demo/
│   │           ├── model/
│   │           │   ├── NhanVien.java           # Extended Employee model (14 fields)
│   │           │   └── PhongBan.java           # Department model (NEW)
│   │           ├── dao/
│   │           │   ├── NhanVienDAO.java        # Employee DAO with FK support
│   │           │   └── PhongBanDAO.java        # Department DAO (NEW)
│   │           ├── util/
│   │           │   └── DatabaseUtil.java       # MySQL + H2 utilities
│   │           ├── DemoDBUnitMain.java         # Original demo
│   │           └── DemoPhongBanForeignKey.java # FK demo (NEW)
│   └── test/
│       ├── java/
│       │   └── com/demo/
│       │       ├── base/
│       │       │   └── DBUnitBaseTest.java     # Enhanced base test
│       │       └── test/
│       │           ├── NhanVienDBUnitTest.java # Original 10 tests
│       │           ├── PhongBanDBUnitTest.java # Department tests (NEW)
│       │           ├── ForeignKeyIntegrityTest.java # FK tests (NEW)
│       │           └── NewFieldsTest.java      # Extended model tests (NEW)
│       └── resources/
│           └── dataset/
│               ├── nhan-vien-initial.xml       # Updated with new fields
│               ├── nhan-vien-expected.xml      # Updated expected data
│               ├── phong-ban-initial.xml       # Department data (NEW)
│               └── full-dataset-with-foreign-key.xml # Complete dataset (NEW)
├── pom.xml                                     # Updated dependencies
├── run-demo.sh                                 # Enhanced demo script
├── README.md                                   # Updated documentation
└── README-FOREIGN-KEY.md                       # FK implementation guide (NEW)
```

## 🚀 Cài đặt và chạy

### Yêu cầu hệ thống
- Java JDK 11 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+ (recommended, optional)
- Git (để clone project)

### Các bước thực hiện

1. **🎬 Chạy toàn bộ demo v2.0 (recommended)**
   ```bash
   # Trên Linux/macOS
   chmod +x run-demo.sh
   ./run-demo.sh
   
   # Trên Windows (Git Bash)
   bash run-demo.sh
   ```
   Script này sẽ tự động:
   - Kiểm tra system requirements
   - Compile project với MySQL support
   - Chạy Foreign Key demo với extended fields
   - Chạy tất cả 25 DBUnit tests
   - Hiển thị detailed test summary và features

2. **Clone repository**
   ```bash
   git clone <repository-url>
   cd dbunit-demo
   ```

3. **Build project**
   ```bash
   mvn clean compile
   ```

4. **🚀 Chạy demo mới (MySQL + Foreign Keys)**
   ```bash
   mvn exec:java
   ```

5. **🧪 Chạy toàn bộ test suite (25 tests)**
   ```bash
   mvn test
   ```

6. **🎯 Chạy test categories cụ thể**
   ```bash
   # Test extended employee model (14 fields)
   mvn test -Dtest=NewFieldsTest

   # Test foreign key constraints và referential integrity
   mvn test -Dtest=ForeignKeyIntegrityTest

   # Test department CRUD operations
   mvn test -Dtest=PhongBanDBUnitTest

   # Test original employee operations (updated)
   mvn test -Dtest=NhanVienDBUnitTest
   ```

7. **🔧 Test specific scenarios**
   ```bash
   # Test với dữ liệu NULL cho các trường mới
   mvn test -Dtest=NewFieldsTest#testCacTruongMoiCoTheNull

   # Test vi phạm foreign key constraint
   mvn test -Dtest=ForeignKeyIntegrityTest#testViolateReferentialIntegrity

   # Test update với extended fields
   mvn test -Dtest=NewFieldsTest#testCapNhatCacTruongMoi
   ```

## 🗄️ Database Configuration

### MySQL Setup (Recommended)
```sql
-- Tạo database cho testing
CREATE DATABASE dbunit_demo;

-- Grant permissions (nếu cần)
GRANT ALL PRIVILEGES ON dbunit_demo.* TO 'root'@'localhost';
```

### Auto-Fallback Mechanism
Project tự động fallback sang H2 nếu MySQL không khả dụng:

```java
public static Connection taoKetNoiMacDinh() throws SQLException {
    try {
        return taoKetNoiMySQL();
    } catch (SQLException e) {
        System.out.println("MySQL unavailable, falling back to H2");
        return taoKetNoiH2();
    }
}
```

### Connection Strings
- **MySQL**: `jdbc:mysql://localhost:3306/dbunit_demo?createDatabaseIfNotExist=true`
- **H2 Fallback**: `jdbc:h2:mem:testdb;MODE=MySQL`

## 👥 Extended Employee Model

### New Fields Added (v2.0)
```java
public class NhanVien {
    // Original fields (8)
    private Long id;
    private String hoTen;
    private String email;
    private String phongBan;
    private Long phongBanId;        // NEW: Foreign key
    private String chucVu;
    private BigDecimal luong;
    private Date ngayVaoLam;
    private Boolean trangThaiHoatDong;
    
    // Extended fields (7) - NEW in v2.0
    private LocalDateTime dateOfBirth;    // Personal: Date of birth
    private String phoneNumber;           // Contact: Phone number
    private String avatarUrl;            // Profile: Avatar image URL
    private String bio;                  // Personal: Biography/description
    private String sport;                // Interest: Favorite sport
    private String roles;                // Professional: System roles
    private String company;              // Professional: Company name
}
```

### Database Schema (Updated)
```sql
-- Department table (NEW)
CREATE TABLE phong_ban (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ma_phong_ban VARCHAR(10) UNIQUE NOT NULL,
    ten_phong_ban VARCHAR(100) NOT NULL,
    mo_ta TEXT,
    truong_phong VARCHAR(100),
    so_nhan_vien INT DEFAULT 0,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE
);

-- Employee table (UPDATED with 7 new fields)
CREATE TABLE nhan_vien (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phong_ban VARCHAR(50),              -- Legacy field
    phong_ban_id BIGINT,                -- NEW: Foreign key
    chuc_vu VARCHAR(50),
    luong DECIMAL(15,2),
    ngay_vao_lam DATE,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
    
    -- NEW FIELDS (v2.0)
    date_of_birth DATETIME,             -- Personal info
    phone_number VARCHAR(20),           -- Contact info
    avatar_url VARCHAR(255),            -- Profile image
    bio TEXT,                          -- Biography
    sport VARCHAR(100),                -- Interests
    roles VARCHAR(100),                -- System roles
    company VARCHAR(100),              -- Company
    
    FOREIGN KEY (phong_ban_id) REFERENCES phong_ban(id)
);
```

## 🎪 Các chức năng DBUnit được demo

### 1. **Database Operations (Enhanced)**
- ✅ **CLEAN_INSERT**: Xóa dữ liệu cũ và insert dữ liệu mới (with FK support)
- ✅ **INSERT**: Chỉ insert dữ liệu mới (with extended fields)
- ✅ **UPDATE**: Cập nhật dữ liệu hiện có (all 14 fields)
- ✅ **REFRESH**: Insert hoặc update tùy vào dữ liệu đã tồn tại
- ✅ **DELETE**: Xóa dữ liệu cụ thể (with FK constraint checking)

### 2. **Dataset Management (Extended)**
- ✅ **Load dataset từ XML files** (multi-table với FK)
- ✅ **Export dataset từ database** (preserving relationships)
- ✅ **Compare datasets** (expected vs actual với extended fields)
- ✅ **Filter columns** cho comparison (including new fields)
- ✅ **Column sensing** tự động cho 14 fields

### 3. **Database Assertions (Enhanced)**
- ✅ **So sánh table data** với expected results (multi-table)
- ✅ **Verify row counts** (với foreign key constraints)
- ✅ **Check specific column values** (including extended fields)
- ✅ **Foreign Key Constraint Validation**
- ✅ **Referential Integrity Testing**

### 4. **Advanced Features (NEW)**
- ✅ **Foreign Key Constraints** (Department ↔ Employee)
- ✅ **Multi-table Relationships** testing
- ✅ **Extended Data Types** (LocalDateTime, complex strings)
- ✅ **NULL Value Handling** cho optional fields
- ✅ **MySQL Integration** với production-like database
- ✅ **Auto-fallback** Database mechanism
- ✅ **Enhanced Error Handling** và rollback

## 🔍 Chi tiết implementation

### Test Classes và Coverage

#### 1. **NewFieldsTest.java** (NEW) - 3 tests
- `testThemNhanVienVoiCacTruongMoi()` - Test insertion với all 14 fields
- `testCapNhatCacTruongMoi()` - Test update operations cho extended fields
- `testCacTruongMoiCoTheNull()` - Test NULL handling cho optional fields

#### 2. **ForeignKeyIntegrityTest.java** (NEW) - 3 tests
- `testValidForeignKeyReference()` - Test valid FK relationships
- `testViolateReferentialIntegrity()` - Test FK constraint violations
- `testDeleteConstraintViolation()` - Test delete restrictions với FK

#### 3. **PhongBanDBUnitTest.java** (NEW) - 9 tests
- Complete CRUD operations cho Department model
- Unique constraint testing
- Error handling và edge cases

#### 4. **NhanVienDBUnitTest.java** (UPDATED) - 10 tests
- All original tests updated để support extended model
- Enhanced với foreign key support

### Enhanced XML Datasets

#### Extended Employee Dataset
```xml
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
    <nhan_vien 
        id="1"
        ho_ten="Nguyễn Văn An"
        email="an.nguyen@company.com"
        phong_ban="IT"
        phong_ban_id="1"
        chuc_vu="Senior Developer"
        luong="25000000.00"
        ngay_vao_lam="2020-01-15"
        trang_thai_hoat_dong="true"
        date_of_birth="1990-05-15 08:30:00"
        phone_number="0901234567"
        avatar_url="https://example.com/avatar/an.jpg"
        bio="Experienced developer with 5+ years in Java and Spring"
        sport="Football"
        roles="DEVELOPER,TEAM_LEAD"
        company="TechCorp"/>
    <!-- More employees with complete data... -->
</dataset>
```

#### Multi-table Dataset với Foreign Keys
```xml
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
    <!-- Departments first (parent table) -->
    <phong_ban 
        id="1"
        ma_phong_ban="IT"
        ten_phong_ban="Công nghệ thông tin"
        mo_ta="Phát triển phần mềm và hạ tầng IT"
        truong_phong="Lê Hoàng Cường"
        so_nhan_vien="3"
        trang_thai_hoat_dong="true"/>
    
    <!-- Employees with FK references -->
    <nhan_vien 
        id="1"
        phong_ban_id="1"
        ...extended fields.../>
</dataset>
```

## 📊 Kết quả mong đợi

### Demo Output với MySQL Integration
```
=== DEMO: Khóa ngoại giữa Nhân viên và Phòng ban (MySQL) ===

Connected to MySQL database successfully
1. Tạo schema database...
   ✓ Đã tạo bảng thành công với khóa ngoại và các trường mới

2. Thêm dữ liệu phòng ban...
   + Thêm phòng ban: Công nghệ thông tin (ID: 1)
   ✓ Đã thêm các phòng ban

3. Thêm nhân viên với khóa ngoại hợp lệ và các trường mới...
   + Thêm nhân viên: Nguyễn Văn An (Phòng ban ID: 1, Phone: 0901234567)
   ✓ Đã thêm nhân viên thành công

4. Test vi phạm khóa ngoại...
   ✓ Đã bắt được lỗi vi phạm khóa ngoại
   ✓ Khóa ngoại hoạt động đúng

5. Test xóa phòng ban có nhân viên...
   ✓ Đã bắt được lỗi vi phạm referential integrity
   ✓ Referential integrity hoạt động đúng

6. Dữ liệu cuối cùng với các trường mới:
--- NHÂN VIÊN (với các trường mới) ---
ID: 1 | Tên: Nguyễn Văn An | Phone: 0901234567 | Sport: Football | Company: TechCorp
```

### Test Suite Results
```
🧪 Running DBUnit Test Suite...
   • Testing with MySQL database
   • Extended employee model (14 fields)
   • Foreign key constraints
   • Department relationships

✅ ForeignKeyIntegrityTest: 3/3 tests
✅ NewFieldsTest: 3/3 tests (Extended model)
✅ NhanVienDBUnitTest: 10/10 tests
✅ PhongBanDBUnitTest: 9/9 tests
📈 Total: 25/25 tests passed
```

## 🔧 Troubleshooting

### MySQL Connection Issues
1. **MySQL not running**: Script automatically falls back to H2
2. **Permission denied**: Check MySQL user permissions
3. **Database not exists**: Auto-created với `createDatabaseIfNotExist=true`

### Test Failures
1. **Foreign Key violations**: Check dataset loading order (parent before child)
2. **Data truncation**: Verify field lengths trong dataset XML
3. **Unique constraints**: Ensure unique values trong test data

## 🆕 Migration từ v1.0

### Breaking Changes
- Main demo class changed từ `DemoDBUnitMain` sang `DemoPhongBanForeignKey`
- Database schema extended với new tables và fields
- XML datasets updated với additional fields

### Backwards Compatibility
- Tất cả v1.0 functionality vẫn available
- Original test cases được preserved và enhanced
- Legacy code continues to work

## 📚 Tài liệu tham khảo

- [DBUnit Official Documentation](http://dbunit.sourceforge.net/)
- [JUnit 4 Documentation](https://junit.org/junit4/)
- [MySQL Connector/J Documentation](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [README-FOREIGN-KEY.md](README-FOREIGN-KEY.md) - Chi tiết implementation của Foreign Keys

---

**📈 Version History:**
- **v1.0**: Basic DBUnit với H2 database (8 employee fields, 10 tests)
- **v2.0**: MySQL integration + Extended model (14 fields, 25 tests, FK support)
