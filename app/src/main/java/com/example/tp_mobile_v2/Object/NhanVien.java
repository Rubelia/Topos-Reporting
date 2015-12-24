package com.example.tp_mobile_v2.Object;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by LapTrinhMobile on 11/13/2015.
 */
public class NhanVien implements Serializable {
    String MaNV, TenNV, TenDangNhap, MatKhau, MaCH, MaNhomNV;
    public NhanVien() {
        MaNV = ""; TenDangNhap = ""; MatKhau = ""; MaCH = "AA"; MaNhomNV = ""; TenNV = "";
    }

    public String getMaCH() {
        return MaCH;
    }

    public String getMaNhomNV() {
        return MaNhomNV;
    }

    public String getMaNV() {
        return MaNV;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public String getTenDangNhap() {
        return TenDangNhap;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setMaCH(String maCH) {
        MaCH = maCH;
    }

    public void setMaNhomNV(String maNhomNV) {
        MaNhomNV = maNhomNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public void setTenDangNhap(String tenDangNhap) {
        TenDangNhap = tenDangNhap;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public void setTenNV(String tenNV) {
        TenNV = tenNV;
    }
}
