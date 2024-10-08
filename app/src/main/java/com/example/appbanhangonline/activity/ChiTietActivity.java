package com.example.appbanhangonline.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.model.GioHang;
import com.example.appbanhangonline.model.SanPhamMoi;
import com.example.appbanhangonline.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ChiTietActivity extends AppCompatActivity {

    TextView txttensp, txtgiasp, mota, txtsoluong;
    Button btnThem, btnVideo;
    ImageView image;
    Spinner sp;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge notificationBadge;
    ImageView btnCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent video = new Intent(getApplicationContext(), YoutubeActivity.class);
                video.putExtra("linkvideo", sanPhamMoi.getLinkvideo());
                startActivity(video);
                finish();
            }
        });
    }

    private void themGioHang() {
        if (!Utils.manggiohang.isEmpty()) {
            int soluong = Integer.parseInt(sp.getSelectedItem().toString());
            if (soluong <= sanPhamMoi.getSoluong()) {
                boolean isCheck = false;
                for (int i = 0; i < Utils.manggiohang.size(); i++) {
                    if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()) {
                        int sl = soluong + Utils.manggiohang.get(i).getSoluong();
                        if (sl <= sanPhamMoi.getSoluong()) {
                            Utils.manggiohang.get(i).setSoluong(sl);
                            Utils.manggiohang.get(i).setGiasp(Long.parseLong(sanPhamMoi.getGia()));
                            Utils.manggiohang.get(i).setSoluongtonkho(sanPhamMoi.getSoluong());
                        } else {
                            MotionToast.Companion.createToast(this,
                                    "Thông báo",
                                    "Quá số lượng tồn kho!",
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
                        }
                        isCheck = true;
                    }
                }
                if (!isCheck) {
                    GioHang gioHang = new GioHang();
                    gioHang.setIdsp(sanPhamMoi.getId());
                    gioHang.setTensp(sanPhamMoi.getTensp());
                    gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                    gioHang.setSoluong(soluong);
                    gioHang.setGiasp(Long.parseLong(sanPhamMoi.getGia()));
                    gioHang.setSoluongtonkho(sanPhamMoi.getSoluong());
                    Utils.manggiohang.add(gioHang);
                }
            } else {
                MotionToast.Companion.createToast(this,
                        "Thông báo",
                        "Quá số lượng!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
            }
        } else {
            int soluong = Integer.parseInt(sp.getSelectedItem().toString());
            if (soluong <= sanPhamMoi.getSoluong()) {
                GioHang gioHang = new GioHang();
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                gioHang.setSoluong(soluong);
                gioHang.setGiasp(Long.parseLong(sanPhamMoi.getGia()));
                gioHang.setSoluongtonkho(sanPhamMoi.getSoluong());
                Utils.manggiohang.add(gioHang);
            } else {
                MotionToast.Companion.createToast(this,
                        "Thông báo",
                        "Quá số lượng!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
            }
        }
        notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
        Paper.book().write("giohang", Utils.manggiohang);
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("sanpham");
        if (sanPhamMoi != null) {
            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(image);
            txttensp.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtgiasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia())) + "đ");
            txtsoluong.setText("Số lượng: " + sanPhamMoi.getSoluong());
            mota.setText(sanPhamMoi.getMota());
            Integer[] arr = new Integer[]{1,2,3,4,5,6,7,8,9,10};
            sp.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arr));
        } else {
            MotionToast.Companion.createToast(this,
                    "Thông báo",
                    "Không lấy được dữ liệu!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        txttensp=findViewById(R.id.txttensp);
        txtgiasp=findViewById(R.id.txtgiasp);
        txtsoluong = findViewById(R.id.txtsoluong);
        mota=findViewById(R.id.txtChitiet);
        btnThem=findViewById(R.id.btnThemGioHang);
        btnVideo = findViewById(R.id.buttonvideo);
        image=findViewById(R.id.imgchitiet);
        sp=findViewById(R.id.spChiTiet);
        toolbar=findViewById(R.id.toolbarCT);
        notificationBadge = findViewById(R.id.menu_sl);
        if (Utils.manggiohang != null) {
            notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
        }
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietActivity.this, GioHangActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null) {
            notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
        }
        if (sanPhamMoi.getSoluong() == 0) {
            btnThem.setEnabled(false);
        } else if (sanPhamMoi.getSoluong() > 0) {
            btnThem.setEnabled(true);
        }
    }
}