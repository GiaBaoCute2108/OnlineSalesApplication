package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhangonline.model.SanPhamMoi;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.adapter.LaptopAdapter;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.example.appbanhangonline.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SearchView searchView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LaptopAdapter adapter;
    ApiBanHang apiBanHang;
    List<SanPhamMoi> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        actionBar();
        search();
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    list.clear();
                    adapter = new LaptopAdapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);
                } else {
                    getData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    list.clear();
                    adapter = new LaptopAdapter(getApplicationContext(), list);
                    recyclerView.setAdapter(adapter);
                } else {
                    getData(newText);
                }
                return true;
            }
        });
    }

    private void getData(String key) {
        list.clear();
        compositeDisposable.add(apiBanHang.timkiem(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            list.addAll(sanPhamMoiModel.getResult());
                            adapter = new LaptopAdapter(getApplicationContext(), list);
                            recyclerView.setAdapter(adapter);
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(this,
                                    "Thông báo",
                                    throwable.getMessage(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
                        }
                ));
    }

    private void actionBar() {
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
        toolbar = findViewById(R.id.toolbar_tk);
        recyclerView = findViewById(R.id.recycleview_tk);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        searchView = findViewById(R.id.searchView);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        list = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}