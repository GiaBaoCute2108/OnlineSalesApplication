package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhangonline.Interface.ItemDeleteClickListener;
import com.example.appbanhangonline.adapter.DonHangAdapter;
import com.example.appbanhangonline.model.DonHang;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.example.appbanhangonline.retrofit.RetrofitClient;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    Toolbar toolbar;
    RecyclerView recyclerView;
    DonHangAdapter adapter;
    List<DonHang> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_activity);
        initView();
        getToolBarSupport();
        getData();
    }

    private void getToolBarSupport() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                      donHangModel -> {
                              if (donHangModel.isSuccess()) {
                                  list.clear();
                                  if (!donHangModel.getResult().isEmpty()) {
                                      list.addAll(donHangModel.getResult());
                                      adapter = new DonHangAdapter(getApplicationContext(), list, new ItemDeleteClickListener() {
                                          @Override
                                          public void onDeleteClick(int iddonhang, int position) {
                                              showDeleteOrder(iddonhang, position);
                                              adapter.notifyDataSetChanged();
                                          }
                                      });
                                      recyclerView.setAdapter(adapter);
                                  }
                              }
                      }
                      ,throwable -> {

                        }
                ));
    }

    private void showDeleteOrder(int iddonhang, int position) {
        PopupMenu popupMenu = new PopupMenu(this, recyclerView.findViewById(R.id.trangthaidon));
        popupMenu.inflate(R.menu.menu_delete);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteOrder) {
                    if (list.get(position).getTrangthai() == 0) {
                        deleteOrder(iddonhang);
                    } else {
                        MotionToast.Companion.createToast(XemDonActivity.this,
                                "Thông báo",
                                "Đơn hàng này không thể hủy!",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(XemDonActivity.this,R.font.helvetica_regular));
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void deleteOrder(int iddonhang) {
        compositeDisposable.add(apiBanHang.xoadonahng(iddonhang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            MotionToast.Companion.createToast(XemDonActivity.this,
                                    "Thông báo",
                                    "Xóa thành công!",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(XemDonActivity.this,R.font.helvetica_regular));
                            getData();
                        },
                        throwable -> {
                            MotionToast.Companion.createToast(XemDonActivity.this,
                                    "Thông báo",
                                    throwable.getMessage(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(XemDonActivity.this,R.font.helvetica_regular));
                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar_ls);
        recyclerView = findViewById(R.id.recycleview_donhang);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}