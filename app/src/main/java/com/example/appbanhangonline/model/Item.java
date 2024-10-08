package com.example.appbanhangonline.model;

public class Item {
    int idsp;
    String tensp;
    String hinhanh;
    String giasp;
    int soluong;

    public int getId() {
        return idsp;
    }

    public void setId(int id) {
        idsp = id;
    }

    public String getName() {
        return tensp;
    }

    public void setName(String name) {
        tensp = name;
    }

    public String getImg() {
        return hinhanh;
    }

    public void setImg(String img) {
        hinhanh = img;
    }

    public String getGiasp() {
        return giasp;
    }

    public void setGiasp(String giasp) {
        this.giasp = giasp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
