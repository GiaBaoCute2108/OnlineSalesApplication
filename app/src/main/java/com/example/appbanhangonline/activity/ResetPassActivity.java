package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appbanhangonline.retrofit.RetrofitClient;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ResetPassActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    AppCompatButton btnReset;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initView();
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.emailReset);
        progressBar = findViewById(R.id.progress);
        btnReset = findViewById(R.id.btnResetPass);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String e = email.getText().toString().trim();
        if (TextUtils.isEmpty(e)) {
            MotionToast.Companion.createToast(this,
                    "Thông báo",
                    "Bạn chưa nhập địa chỉ email!",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
        } else {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().sendPasswordResetEmail(e)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            MotionToast.Companion.createToast(this,
                                    "Thông báo",
                                    "Hãy kiểm tra email của bạn!",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
                        }
                        finish();
                    });
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}