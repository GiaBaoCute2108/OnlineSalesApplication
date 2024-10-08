package com.example.appbanhangonline.retrofit;

import android.util.Log;

import com.example.appbanhangonline.Service.AccessToken;

import java.io.IOException;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientNoti {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Log.d("RetrofitClientNoti", "Interceptor called");
                    String accessToken = new AccessToken().getAccessToken();
                    if (accessToken == null) {
                        Log.e("RetrofitClientNoti", "Failed to get access token");
                    } else {
                        Log.d("RetrofitClientNoti", "Access Token: " + accessToken);
                    }
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .build();
                    Log.d("RetrofitClientNoti", "Request: " + newRequest.toString());
                    return chain.proceed(newRequest);
                }
            };
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}