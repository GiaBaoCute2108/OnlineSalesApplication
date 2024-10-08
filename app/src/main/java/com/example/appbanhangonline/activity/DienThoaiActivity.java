package com.example.appbanhangonline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhangonline.model.SanPhamMoi;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.adapter.DienThoaiAdapter;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.example.appbanhangonline.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class DienThoaiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    ApiBanHang api;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter adapter;
    List<SanPhamMoi> list;
    LinearLayoutManager manager;
    Handler handler = new Handler();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        api = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        initView();
        actionToolbar();
        getData(page);
        addEventLoading();
    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    if (manager.findLastCompletelyVisibleItemPosition() == list.size() -1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                list.add(null);
                adapter.notifyItemInserted(list.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.remove(list.size() - 1);
                adapter.notifyItemRemoved(list.size());
                page += 1;
                getData(page);
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(api.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                if (adapter == null) {
                                    list = sanPhamMoiModel.getResult();
                                    adapter = new DienThoaiAdapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    int vitri = list.size() - 1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i = 0; i< soluongadd; i++) {
                                        list.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    adapter.notifyItemRangeInserted(vitri, soluongadd);
                                }
                            } else {
                                MotionToast.Companion.createToast(this,
                                        "Thông báo",
                                        "Hết dữ liệu!",
                                        MotionToastStyle.WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(this,
                                    "Thông báo",
                                    "Không kết nối được vào Server",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
                        }
                ));

    }

    private void actionToolbar() {
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
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_dt);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}