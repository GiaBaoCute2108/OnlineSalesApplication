package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhangonline.model.CreateOrder;
import com.example.appbanhangonline.retrofit.ApiPushNotification;
import com.example.appbanhangonline.retrofit.RetrofitClient;
import com.example.appbanhangonline.retrofit.RetrofitClientNoti;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.retrofit.ApiBanHang;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText location;
    TextView price, phone, email;
    ImageView momo, zalopay;
    AppCompatButton btnDatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    int tongsp;
    int iddonhang;
    private long amount = 10000;
    private long fee = 0;
    int environment = 0;//developer default
    private String merchantName = "Thanh toán đơn hàng";
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "AppBanHang";
    private String description = "Mua hàng online";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        //zalo
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        initView();
        initControl();
        getCount();
    }

    //Get token through MoMo app
    private void requestPaymentMomo(int iddonhang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
//            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", iddonhang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", iddonhang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("Thanh cong", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    compositeDisposable.add(apiBanHang.updatemomo(iddonhang, token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if (messageModel.isSuccess()) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            pushNotiToAdmin();
                                            clearCart();
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        Log.d("ERROR", throwable.getMessage());
                                    }
                            ));

                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("Thanh cong", "Khong thanh cong");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Thanh cong", "Khong thanh cong");
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("Thanh cong", "Khong thanh cong");
                } else {
                    //TOKEN FAIL
                    Log.d("Thanh cong", "Khong thanh cong");
                }
            } else {
                Log.d("Thanh cong", "Khong thanh cong");
            }
        } else {
            Log.d("Thanh cong", "Khong thanh cong");
        }
    }

    private void getCount() {
        tongsp = 0;
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            tongsp += Utils.mangmuahang.get(i).getSoluong();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        long total = getIntent().getLongExtra("tongtien", 0);
        DecimalFormat format = new DecimalFormat("###,###,###");
        price.setText(format.format(total) + "đ");
        phone.setText(Utils.user_current.getMobile());
        email.setText(Utils.user_current.getEmail());

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locate = location.getText().toString().trim();
                if (TextUtils.isEmpty(locate)) {
                    MotionToast.Companion.createToast(ThanhToanActivity.this,
                            "Thông báo",
                            "Địa chỉ giao hàng còn thiếu!",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                } else {
                    int iduser = Utils.user_current.getId();
                    String sdt = Utils.user_current.getMobile();
                    String email = Utils.user_current.getEmail();
                    String tongtien = String.valueOf(total);
                    compositeDisposable.add(apiBanHang.createOrder(iduser, locate, sdt, email, String.valueOf(tongsp), tongtien, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        pushNotiToAdmin();
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                "Thành công!",
                                                MotionToastStyle.SUCCESS,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        clearCart();
                                        startActivity(intent);
                                        finish();
                                    }
                                    ,throwable -> {
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                throwable.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                    }
                            ));
                }
            }
        });

        momo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locate = location.getText().toString().trim();
                if (TextUtils.isEmpty(locate)) {
                    MotionToast.Companion.createToast(ThanhToanActivity.this,
                            "Thông báo",
                            "Địa chỉ giao hàng còn thiếu!",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                } else {
                    int iduser = Utils.user_current.getId();
                    String sdt = Utils.user_current.getMobile();
                    String email = Utils.user_current.getEmail();
                    String tongtien = String.valueOf(total);
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOrder(iduser, locate, sdt, email, String.valueOf(tongsp), tongtien, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                "Thành công!",
                                                MotionToastStyle.SUCCESS,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                        iddonhang = messageModel.getIddonhang();
                                        requestPaymentMomo(messageModel.getIddonhang());
                                    }
                                    ,throwable -> {
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                throwable.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                    }
                            ));
                }
            }
        });

        zalopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locate = location.getText().toString().trim();
                if (TextUtils.isEmpty(locate)) {
                    MotionToast.Companion.createToast(ThanhToanActivity.this,
                            "Thông báo",
                            "Địa chỉ giao hàng còn thiếu!",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                } else {
                    int iduser = Utils.user_current.getId();
                    String sdt = Utils.user_current.getMobile();
                    String email = Utils.user_current.getEmail();
                    String tongtien = String.valueOf(total);
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOrder(iduser, locate, sdt, email, String.valueOf(tongsp), tongtien, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                "Thành công!",
                                                MotionToastStyle.SUCCESS,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                        iddonhang = messageModel.getIddonhang();
                                        requestPaymentZalopay();
                                    }
                                    ,throwable -> {
                                        MotionToast.Companion.createToast(ThanhToanActivity.this,
                                                "Thông báo",
                                                throwable.getMessage(),
                                                MotionToastStyle.ERROR,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                                    }
                            ));
                }
            }
        });
    }

    private void requestPaymentZalopay() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder("10000");

            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(ThanhToanActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        compositeDisposable.add(apiBanHang.updatemomo(iddonhang, token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> {
                                            if (messageModel.isSuccess()) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                pushNotiToAdmin();
                                                clearCart();
                                                finish();
                                            }
                                        },
                                        throwable -> {
                                            Log.d("ERROR", throwable.getMessage());
                                        }
                                ));
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCart() {
        updateQuantity();
        Utils.manggiohang.removeAll(Utils.mangmuahang);
        Utils.mangmuahang.clear();
        Paper.book().write("giohang", Utils.manggiohang);
    }

    private void updateQuantity() {
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            int soluongtonkhomoi = Utils.mangmuahang.get(i).getSoluongtonkho() - Utils.mangmuahang.get(i).getSoluong();
            Log.d("quantity", String.valueOf(soluongtonkhomoi));
            compositeDisposable.add(apiBanHang.updatesoluong(Utils.mangmuahang.get(i).getIdsp(), soluongtonkhomoi)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                MotionToast.Companion.createToast(ThanhToanActivity.this,
                                        "Thông báo",
                                        messageModel.getMessage(),
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(ThanhToanActivity.this,R.font.helvetica_regular));
                            },
                            throwable -> {
                                    
                            }
                            ));
        }
    }

    private void pushNotiToAdmin() {
        //get token
        compositeDisposable.add(apiBanHang.gettoken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                for(int i = 0; i < userModel.getResult().size(); i++) {
                                    Map<String, String> notification = new HashMap<>();
                                    notification.put("title", "Thông báo");
                                    notification.put("body", "Bạn có đơn hàng mới");

                                    Map<String, Object> message = new HashMap<>();
                                    message.put("token", userModel.getResult().get(i).getToken());
                                    message.put("notification", notification);

                                    HashMap<String, Object> requestBody = new HashMap<>();
                                    requestBody.put("message", message);

                                    ApiPushNotification apiPushNotification = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                                    compositeDisposable.add(apiPushNotification.sendNotification(requestBody)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiResponse -> {
                                                        Log.d("pushNotiToUser", "Notification sent successfully: " + new Gson().toJson(notiResponse));
                                                    },
                                                    throwable -> {
                                                        Log.e("pushNotiToUser", "Error sending notification: " + throwable.getMessage(), throwable);
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("pushNotiToUser", throwable.getMessage());
                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolBarTT);
        location = findViewById(R.id.location);
        price = findViewById(R.id.priceTT);
        phone = findViewById(R.id.sdtTT);
        email = findViewById(R.id.emailTT);
        btnDatHang = findViewById(R.id.btnDatHang);
        momo = findViewById(R.id.momo);
        zalopay = findViewById(R.id.zalopay);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}