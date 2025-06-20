package com.demo.model;

import java.math.BigDecimal;
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
    private String phongBan;
    private String chucVu;
    private BigDecimal luong;
    private Date ngayVaoLam;
    private Boolean trangThaiHoatDong;

    // Constructor mặc định
    public NhanVien() {
    }

    // Constructor đầy đủ
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
                ", chucVu='" + chucVu + '\'' +
                ", luong=" + luong +
                ", ngayVaoLam=" + ngayVaoLam +
                ", trangThaiHoatDong=" + trangThaiHoatDong +
                '}';
    }
} 