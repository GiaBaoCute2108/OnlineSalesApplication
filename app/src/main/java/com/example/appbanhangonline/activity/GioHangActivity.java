package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appbanhangonline.model.EventBus.TinhTongEvent;
import com.example.appbanhangonline.model.GioHang;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.adapter.GioHangAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnMuaHang;
    GioHangAdapter adapter;
    long tong;
    @SuppressLint("SetTextI18n")
    public void tinhTongTien() {
        tong = 0;
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            tong += (Utils.mangmuahang.get(i).getGiasp() * Utils.mangmuahang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tong) + "đ");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
    }

    @SuppressLint("SetTextI18n")
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mangmuahang.clear();
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        if(Utils.manggiohang.isEmpty()) {
            giohangtrong.setVisibility(View.VISIBLE);
        } else {
            adapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
            recyclerView.setAdapter(adapter);
        }
        checkSoLuong();
    }

    private void checkSoLuong() {
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            if (Utils.manggiohang.get(i).getSoluongtonkho() >= Utils.manggiohang.get(i).getSoluong()) {
                tinhTongTien();
                btnMuaHang.setEnabled(true);
            } else {
                MotionToast.Companion.createToast(this,
                        "Thông báo",
                        "Sản phẩm " + Utils.manggiohang.get(i).getTensp() + " vượt quá số lượng trong kho!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
                btnMuaHang.setEnabled(false);
            }
        }
    }

    private void initView() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        tongtien=findViewById(R.id.tongtienGH);
        toolbar = findViewById(R.id.toolbarGh);
        recyclerView = findViewById(R.id.rcviewG);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.mangmuahang.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    intent.putExtra("tongtien", tong);
                    startActivity(intent);
                } else {
                    MotionToast.Companion.createToast(GioHangActivity.this,
                            "Thông báo",
                            "không có sản phẩm nào được chọn!",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(GioHangActivity.this,R.font.helvetica_regular));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @SuppressLint("SetTextI18n")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        if (event != null) {
            tinhTongTien();
            if (Utils.manggiohang.isEmpty()) {
                giohangtrong.setVisibility(View.VISIBLE); // Hiển thị thông báo giỏ hàng trống
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang.isEmpty()) {
            giohangtrong.setVisibility(View.VISIBLE);
        }
    }
}