package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.R;

public class KhuyenMaiActivity extends AppCompatActivity {
    ImageView img;
    TextView txtThongtin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);
        initView();
    }
    private void initView() {
        img = findViewById(R.id.imageViewKM);
        txtThongtin = findViewById(R.id.thongtinkhuyenmai);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("url")).into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        txtThongtin.setText(getIntent().getStringExtra("thongtin"));
    }
}