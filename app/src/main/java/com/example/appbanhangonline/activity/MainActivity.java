package com.example.appbanhangonline.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appbanhangonline.model.LoaiSp;
import com.example.appbanhangonline.model.SanPhamMoi;
import com.example.appbanhangonline.model.User;
import com.example.appbanhangonline.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.adapter.LoaiSpAdapter;
import com.example.appbanhangonline.adapter.SanPhamMoiAdapter;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.example.appbanhangonline.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageSlider imageSlider;
    private RecyclerView recyclerViewManHinhChinh;
    private ListView listViewManHinhChinh;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LoaiSpAdapter loaiSpAdapter;
    private List<LoaiSp> mangloaiSp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    private List<SanPhamMoi> mangSpMoi;
    private SanPhamMoiAdapter spAdapter;
    private FrameLayout frameLayout;
    private NotificationBadge notificationBadge;
    private ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        initView();
        getToken();
        ActionBar();
        if (isConnect(this)) {
            MotionToast.Companion.createToast(this,
                    "Thông báo",
                    "Thành công",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular));

            ActionViewFlipper();
            getSpMoi();
            getLoaiSanPham();
            getEvenClick();
            imgsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(search);
                }
            });
        } else {
            MotionToast.Companion.createToast(this,
                    "Thông báo",
                    "Không thành công",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
        }
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String string) {
                        if (!TextUtils.isEmpty(string)) {
                            compositeDisposable.add(apiBanHang.updatetoken(Utils.user_current.getId(), string)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
        compositeDisposable.add(apiBanHang.gettoken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                for (int i = 0; i < userModel.getResult().size(); i++) {
                                    Utils.ID_RECEIVE = String.valueOf(userModel.getResult().get(i).getId());
                                }
                            }
                        },
                        throwable -> {

                        }
                ));
    }

    private void getEvenClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai", 2);
                        startActivity(laptop);
                        break;
                    case 2:
                        Intent dienthoai = new Intent(MainActivity.this, DienThoaiActivity.class);
                        dienthoai.putExtra("loai", 1);
                        startActivity(dienthoai);
                        break;
                    case 4:
                        Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(chat);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        Intent livestream = new Intent(getApplicationContext(), JoinActivity.class);
                        startActivity(livestream);
                        break;
                    case 7:
                        //Xoa thong tin nguoi dung
                        Paper.book().delete("user");
                        FirebaseAuth.getInstance().signOut();
                        Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangxuat);
                        finish();
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        SanPhamMoiModel -> {
                            if (SanPhamMoiModel.isSuccess()) {
                                mangSpMoi = SanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewManHinhChinh.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(this,
                                    "Thông báo",
                                    "Không kết nối được với Server" + throwable.getMessage(),
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaiSp = new ArrayList<>();
                                mangloaiSp = loaiSpModel.getResult();
                                mangloaiSp.add(new LoaiSp("Live Stream", ""));
                                mangloaiSp.add(new LoaiSp("Đăng xuất", ""));
                                loaiSpAdapter = new LoaiSpAdapter(mangloaiSp, getApplicationContext());
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }


    private void ActionViewFlipper() {
        List<SlideModel> imagelist = new ArrayList<>();
        compositeDisposable.add(apiBanHang.khuyenmai()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    khuyenMaiModel -> {
                        if (khuyenMaiModel.isSuccess()) {
                            for (int i = 0; i < khuyenMaiModel.getResult().size(); i++) {
                                imagelist.add(new SlideModel(khuyenMaiModel.getResult().get(i).getUrl(), null));
                            }
                            imageSlider.setImageList(imagelist, ScaleTypes.CENTER_CROP);
                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    Intent khuyenmai = new Intent(getApplicationContext(), KhuyenMaiActivity.class);
                                    khuyenmai.putExtra("url", khuyenMaiModel.getResult().get(i).getUrl());
                                    khuyenmai.putExtra("thongtin", khuyenMaiModel.getResult().get(i).getThongtin());
                                    startActivity(khuyenmai);
                                }

                                @Override
                                public void doubleClick(int i) {

                                }
                            });
                        }
                    },
                    throwable -> {}
                )
        );

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        imageSlider = findViewById(R.id.image_slider);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        frameLayout = findViewById(R.id.mainFL);
        notificationBadge = findViewById(R.id.menu_slMain);
        imgsearch = findViewById(R.id.img_search);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });

        //ListView Navigation
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        loaiSpAdapter = new LoaiSpAdapter(mangloaiSp, getApplicationContext());
        listViewManHinhChinh.setAdapter(loaiSpAdapter);

        //RecycleView man hinh chinh
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(manager);
        recyclerViewManHinhChinh.setHasFixedSize(true);

        //Khoi tao List
        mangloaiSp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Paper.book().read("giohang") != null) {
            Utils.manggiohang = Paper.book().read("giohang");
        }

        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
            notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
        } else {
            notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
        }
    }

    public boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}