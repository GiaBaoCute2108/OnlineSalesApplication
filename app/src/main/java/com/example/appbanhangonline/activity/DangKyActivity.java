package com.example.appbanhangonline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhangonline.retrofit.RetrofitClient;
import com.example.appbanhangonline.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.retrofit.ApiBanHang;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class DangKyActivity extends AppCompatActivity {
    EditText email, username, pass, repass, mobile;
    TextView txtDangNhap1;
    AppCompatButton btnDangky;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }

    private void initControll() {
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        try {
            String str_email = email.getText().toString();
            String str_pass = pass.getText().toString();
            String str_repass = repass.getText().toString();
            String str_mobile = mobile.getText().toString();
            String str_username = username.getText().toString();

            if(str_email.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống email");
            } else if (str_username.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống mật khẩu");
            } else if (str_pass.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống mật khẩu");
            } else if (str_repass.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống nhập lại mật khẩu");
            } else if (str_repass.length() <6) {
                throw new IllegalArgumentException("Mật khẩu phải bé hơn 6");
            } else if (!str_pass.equalsIgnoreCase(str_repass)) {
                throw new IllegalArgumentException("Mật khẩu không trùng khớp");
            } else if (str_mobile.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống số điện thoại");
            } else {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(str_email,str_pass)
                        .addOnCompleteListener(DangKyActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        postData(str_email, "onfirebase", str_username, str_mobile, user.getUid());
                                    }
                                } else {
                                    MotionToast.Companion.createToast((Activity) getApplicationContext(),
                                            "Thông báo",
                                            "Email đã tồn tại hoặc không thành công!",
                                            MotionToastStyle.WARNING,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
                                }
                            }
                        });
            }
        } catch(IllegalArgumentException e) {
            MotionToast.Companion.createToast(this,
                    "Thông báo",
                    e.getMessage(),
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
        }

    }

    public void postData(String str_email, String str_pass, String str_username, String str_mobile, String uid) {
        compositeDisposable.add(apiBanHang.dangky(str_email, str_pass, str_username, str_mobile, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPass("");
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                                MotionToast.Companion.createToast(this,
                                        "Thông báo",
                                        "Đăng ký thành công!",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
                            } else {
                                MotionToast.Companion.createToast(this,
                                        "Thông báo",
                                        userModel.getMessage(),
                                        MotionToastStyle.WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(this,R.font.helvetica_regular));
                            }
                        },
                        throwable -> {

                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        email = findViewById(R.id.emailDk);
        pass = findViewById(R.id.passDk);
        mobile=findViewById(R.id.mobile);
        repass = findViewById(R.id.repass);
        username = findViewById(R.id.usernameDk);
        btnDangky = findViewById(R.id.btndangky);
        txtDangNhap1 = findViewById(R.id.txtdangnhap1);
        txtDangNhap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                startActivity(dangnhap);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}