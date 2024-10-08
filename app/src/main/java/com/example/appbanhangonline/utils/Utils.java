package com.example.appbanhangonline.utils;

import com.example.appbanhangonline.model.GioHang;
import com.example.appbanhangonline.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    //public static String BASE_URL = "http://10.10.152.144/banhang/";
    public static String BASE_URL = "http://192.168.56.1/banhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();
    public static String ID_RECEIVE;
    public static final String IDSEND = "idsend";
    public static final String IDRECEIVE = "idreceive";
    public static final String MESS = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";
}
