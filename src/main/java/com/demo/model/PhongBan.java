package com.demo.model;

import java.util.Objects;

/**
 * Model class đại diện cho phòng ban trong hệ thống
 * Sử dụng cho demo DBUnit testing với khóa ngoại
 */
public class PhongBan {
    private Long id;
    private String maPhongBan;
    private String tenPhongBan;
    private String moTa;
    private String truongPhong;
    private Integer soNhanVien;
    private Boolean trangThaiHoatDong;

    // Constructor mặc định
    public PhongBan() {
    }

    // Constructor đầy đủ
    public PhongBan(Long id, String maPhongBan, String tenPhongBan, String moTa, 
                   String truongPhong, Integer soNhanVien, Boolean trangThaiHoatDong) {
        this.id = id;
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.moTa = moTa;
        this.truongPhong = truongPhong;
        this.soNhanVien = soNhanVien;
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTruongPhong() {
        return truongPhong;
    }

    public void setTruongPhong(String truongPhong) {
        this.truongPhong = truongPhong;
    }

    public Integer getSoNhanVien() {
        return soNhanVien;
    }

    public void setSoNhanVien(Integer soNhanVien) {
        this.soNhanVien = soNhanVien;
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
        PhongBan phongBan = (PhongBan) o;
        return Objects.equals(id, phongBan.id) &&
               Objects.equals(maPhongBan, phongBan.maPhongBan) &&
               Objects.equals(tenPhongBan, phongBan.tenPhongBan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maPhongBan, tenPhongBan);
    }

    @Override
    public String toString() {
        return "PhongBan{" +
                "id=" + id +
                ", maPhongBan='" + maPhongBan + '\'' +
                ", tenPhongBan='" + tenPhongBan + '\'' +
                ", moTa='" + moTa + '\'' +
                ", truongPhong='" + truongPhong + '\'' +
                ", soNhanVien=" + soNhanVien +
                ", trangThaiHoatDong=" + trangThaiHoatDong +
                '}';
    }
} 