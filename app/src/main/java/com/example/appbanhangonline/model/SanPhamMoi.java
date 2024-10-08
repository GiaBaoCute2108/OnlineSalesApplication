package com.example.appbanhangonline.model;

import java.io.Serializable;

public class SanPhamMoi implements Serializable {
    int id;
    String tensp;
    String hinhanh;
    String giasp;
    int loai;
    String mota;

    String linkvideo;
    int soluongtonkho;

    public int getSoluong() {
        return soluongtonkho;
    }

    public void setSoluong(int soluong) {
        this.soluongtonkho = soluong;
    }

    public String getLinkvideo() {
        return linkvideo;
    }

    public void setLinkvideo(String linkvideo) {
        this.linkvideo = linkvideo;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getGia() {
        return giasp;
    }

    public void setGia(String gia) {
        this.giasp = gia;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }
}
