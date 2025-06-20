# DBUnit Demo Project

Đồ án demo các chức năng chính và thường gặp của **DBUnit** - framework testing cho database-driven applications trong Java.

## 📋 Mục lục

- [Giới thiệu](#giới-thiệu)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc project](#cấu-trúc-project)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
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

## 🛠 Công nghệ sử dụng

- **Java 11+**
- **Maven** - Dependency management
- **JUnit 4** - Unit testing framework
- **DBUnit 2.7.3** - Database testing framework
- **H2 Database** - In-memory database cho testing
- **SLF4J** - Logging framework

## 📁 Cấu trúc project

```
dbunit-demo/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/demo/
│   │           ├── model/
│   │           │   └── NhanVien.java           # Model class
│   │           ├── dao/
│   │           │   └── NhanVienDAO.java        # Data Access Object
│   │           ├── util/
│   │           │   └── DatabaseUtil.java       # Database utilities
│   │           └── DemoDBUnitMain.java         # Main demo class
│   └── test/
│       ├── java/
│       │   └── com/demo/
│       │       ├── base/
│       │       │   └── DBUnitBaseTest.java     # Base test class
│       │       └── test/
│       │           └── NhanVienDBUnitTest.java # Main test class
│       └── resources/
│           └── dataset/
│               ├── nhan-vien-initial.xml       # Initial test data
│               └── nhan-vien-expected.xml      # Expected test data
├── pom.xml                                     # Maven configuration
├── run-demo.sh                                 # Demo script (Linux/macOS)
└── README.md                                   # Documentation
```

## 🚀 Cài đặt và chạy

### Yêu cầu hệ thống
- Java JDK 11 hoặc cao hơn
- Maven 3.6+
- Git (để clone project)

### Các bước thực hiện

1. **Clone repository (nếu chưa có)**
   ```bash
   git clone <repository-url>
   cd dbunit-demo
   ```

2. **Chạy toàn bộ demo (recommended)**
   ```bash
   # Trên Linux/macOS
   chmod +x run-demo.sh
   ./run-demo.sh
   
   # Trên Windows (Git Bash)
   bash run-demo.sh
   ```
   Script này sẽ tự động:
   - Compile project
   - Chạy demo application
   - Chạy tất cả DBUnit tests
   - Hiển thị kết quả tổng hợp

3. **Hoặc build project thủ công**
   ```bash
   mvn clean compile
   ```

4. **Chạy demo application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.demo.DemoDBUnitMain"
   ```

5. **Chạy DBUnit tests**
   ```bash
   mvn test
   ```

6. **Chạy test cụ thể**
   ```bash
   # Test 1: Demo CLEAN_INSERT operation
   mvn test -Dtest=NhanVienDBUnitTest#testCleanInsertOperation

   # Test 2: Demo Database Assertion
   mvn test -Dtest=NhanVienDBUnitTest#testDatabaseAssertion

   # Test 3: Demo INSERT operation và dataset comparison
   mvn test -Dtest=NhanVienDBUnitTest#testInsertAndCompare

   # Test 4: Demo UPDATE operation
   mvn test -Dtest=NhanVienDBUnitTest#testUpdateOperation

   # Test 5: Demo DELETE operation
   mvn test -Dtest=NhanVienDBUnitTest#testDeleteOperation

   # Test 6: Demo REFRESH operation
   mvn test -Dtest=NhanVienDBUnitTest#testRefreshOperation

   # Test 7: Demo tìm kiếm theo điều kiện
   mvn test -Dtest=NhanVienDBUnitTest#testSearchOperations

   # Test 8: Demo export và import dataset
   mvn test -Dtest=NhanVienDBUnitTest#testDataSetExportImport

   # Test 9: Demo filter và compare specific columns
   mvn test -Dtest=NhanVienDBUnitTest#testColumnFiltering

   # Test 10: Demo error handling và rollback
   mvn test -Dtest=NhanVienDBUnitTest#testErrorHandlingAndRollback
   ```

## 🎪 Các chức năng DBUnit được demo

### 1. **Database Operations**
- ✅ **CLEAN_INSERT**: Xóa dữ liệu cũ và insert dữ liệu mới
- ✅ **INSERT**: Chỉ insert dữ liệu mới
- ✅ **UPDATE**: Cập nhật dữ liệu hiện có
- ✅ **REFRESH**: Insert hoặc update tùy vào dữ liệu đã tồn tại
- ✅ **DELETE**: Xóa dữ liệu cụ thể

### 2. **Dataset Management**
- ✅ **Load dataset từ XML files**
- ✅ **Export dataset từ database**
- ✅ **Compare datasets** (expected vs actual)
- ✅ **Filter columns** cho comparison
- ✅ **Column sensing** tự động

### 3. **Database Assertions**
- ✅ **So sánh table data** với expected results
- ✅ **Verify row counts**
- ✅ **Check specific column values**

### 4. **Advanced Features**
- ✅ **Error handling và rollback**
- ✅ **H2 Database support** cho testing
- ✅ **Custom data types** (BigDecimal, Date, Boolean)

## 🔍 Chi tiết implementation

### Test Cases được implement

1. **testCleanInsertOperation()** - Demo CLEAN_INSERT
2. **testDatabaseAssertion()** - Demo database assertion
3. **testInsertAndCompare()** - Demo INSERT và dataset comparison
4. **testUpdateOperation()** - Demo UPDATE operation
5. **testDeleteOperation()** - Demo DELETE operation
6. **testRefreshOperation()** - Demo REFRESH operation
7. **testSearchOperations()** - Demo search operations
8. **testDataSetExportImport()** - Demo export/import datasets
9. **testColumnFiltering()** - Demo column filtering
10. **testErrorHandlingAndRollback()** - Demo error handling

### Database Schema

```sql
CREATE TABLE nhan_vien (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phong_ban VARCHAR(50),
    chuc_vu VARCHAR(50),
    luong DECIMAL(15,2),
    ngay_vao_lam DATE,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE
);
```

### Sample XML Dataset

```xml
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
    <nhan_vien 
        id="1"
        ho_ten="Nguyễn Văn An"
        email="an.nguyen@company.com"
        phong_ban="IT"
        chuc_vu="Senior Developer"
        luong="25000000.00"
        ngay_vao_lam="2020-01-15"
        trang_thai_hoat_dong="true"/>
    <!-- More employees... -->
</dataset>
```

## 📊 Kết quả mong đợi

Khi chạy tests, bạn sẽ thấy:

```
=== TEST 1: CLEAN_INSERT Operation Demo ===
Setting up database connection for test...
Connected to H2 in-memory database successfully
Creating employee table schema...
Employee table created successfully
Loading dataset from: dataset/nhan-vien-initial.xml
Dataset loaded successfully
Performing CLEAN_INSERT operation...
CLEAN_INSERT operation completed
Retrieved 5 employees from database
Employee with ID 1 should exist
CLEAN_INSERT operation test passed successfully!

=== TEST 2: Database Assertion Demo ===
...

=== All tests completed successfully! ===
```

## 📚 Tài liệu tham khảo

- [DBUnit Official Documentation](http://dbunit.sourceforge.net/)
- [JUnit 4 Documentation](https://junit.org/junit4/)
