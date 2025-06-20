package com.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Model class đại diện cho nhân viên trong hệ thống
 * Sử dụng cho demo DBUnit testing
 */
public class NhanVien {
    private Long id;
    private String hoTen;
    private String email;
    private String phongBan; // Giữ lại cho backward compatibility
    private Long phongBanId; // Khóa ngoại tới bảng phong_ban
    private String chucVu;
    private BigDecimal luong;
    private Date ngayVaoLam;
    private Boolean trangThaiHoatDong;
    
    // Các trường mới
    private LocalDateTime dateOfBirth;
    private String phoneNumber;
    private String avatarUrl;
    private String bio;
    private String sport;
    private String roles;
    private String company;

    // Constructor mặc định
    public NhanVien() {
    }

    // Constructor đầy đủ (backward compatibility)
    public NhanVien(Long id, String hoTen, String email, String phongBan, 
                   String chucVu, BigDecimal luong, Date ngayVaoLam, Boolean trangThaiHoatDong) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.phongBan = phongBan;
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    // Constructor với khóa ngoại
    public NhanVien(Long id, String hoTen, String email, Long phongBanId, 
                   String chucVu, BigDecimal luong, Date ngayVaoLam, Boolean trangThaiHoatDong) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.phongBanId = phongBanId;
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    // Constructor đầy đủ với tất cả các trường mới
    public NhanVien(Long id, String hoTen, String email, Long phongBanId, String chucVu,
                   BigDecimal luong, Date ngayVaoLam, Boolean trangThaiHoatDong,
                   LocalDateTime dateOfBirth, String phoneNumber, String avatarUrl,
                   String bio, String sport, String roles, String company) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.phongBanId = phongBanId;
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThaiHoatDong = trangThaiHoatDong;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.sport = sport;
        this.roles = roles;
        this.company = company;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(String phongBan) {
        this.phongBan = phongBan;
    }

    public Long getPhongBanId() {
        return phongBanId;
    }

    public void setPhongBanId(Long phongBanId) {
        this.phongBanId = phongBanId;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public BigDecimal getLuong() {
        return luong;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public Boolean getTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(Boolean trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(id, nhanVien.id) &&
               Objects.equals(hoTen, nhanVien.hoTen) &&
               Objects.equals(email, nhanVien.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hoTen, email);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id=" + id +
                ", hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", phongBan='" + phongBan + '\'' +
                ", phongBanId=" + phongBanId +
                ", chucVu='" + chucVu + '\'' +
                ", luong=" + luong +
                ", ngayVaoLam=" + ngayVaoLam +
                ", trangThaiHoatDong=" + trangThaiHoatDong +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", bio='" + bio + '\'' +
                ", sport='" + sport + '\'' +
                ", roles='" + roles + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
} 