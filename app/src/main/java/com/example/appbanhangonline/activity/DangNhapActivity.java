package com.example.appbanhangonline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class DangNhapActivity extends AppCompatActivity {
    EditText email, pass;
    AppCompatButton btndangnhap;
    TextView txtdangky, txtQuenMk;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    boolean isLogin = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private String p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangNhap();
            }
        });

        txtQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void dangNhap() {
        try {
            String e = email.getText().toString().trim();
            p = pass.getText().toString().trim();

            if(e.isEmpty()) {
                throw new IllegalArgumentException("Email không được bỏ trống!");
            } else if (p.isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống!");
            } else {
                //save
                Paper.book().write("email", e);
                Paper.book().write("pass", p);
                if (user != null) {
                    // user đã có đăng nhập firebase
                    firebaseAuth.signOut();
                }
                if (user == null){
                    // user đã signout
                    firebaseAuth.signInWithEmailAndPassword(e, p)
                            .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        login(e, p);
                                        updatepass(e, p);
                                    }
                                }
                            });
                }
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

    @SuppressLint("WrongViewCast")
    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangky = findViewById(R.id.txtdangky);
        email = findViewById(R.id.emailDN);
        pass = findViewById(R.id.passDN);
        btndangnhap = findViewById(R.id.btndangnhap);
        txtQuenMk = findViewById(R.id.txtQuenMk);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //read data
        // Đọc giá trị từ Paper với kiểu dữ liệu là chuỗi và gán vào EditText
        if (Paper.book().contains("email") && Paper.book().contains("pass")) {
            email.setText(Paper.book().read("email", ""));
            pass.setText(Paper.book().read("pass", ""));
            if (Paper.book().read("islogin") != null) {
                boolean flag = Paper.book().read("islogin");
                if (flag) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //login(Paper.book().read("email", ""), Paper.book().read("pass", ""));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void login(String email, String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                isLogin = true;
                                Paper.book().write("islogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                Utils.user_current.setPass(p);

                                //Luu lai thong tin nguoi dung
                                Paper.book().write("user", Utils.user_current);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
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
                        throwable -> MotionToast.Companion.createToast(this,
                                "Thông báo",
                                throwable.getMessage(),
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this,R.font.helvetica_regular))
                ));
    }

    public void updatepass(String email, String pass) {
        compositeDisposable.add(apiBanHang.updatepass(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}