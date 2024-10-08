package com.example.appbanhangonline.retrofit;

import com.example.appbanhangonline.model.NotiResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type: application/json"
            }
    )
    @POST("/v1/projects/appbanhang-ec403/messages:send")
    Observable<NotiResponse> sendNotification(@Body HashMap<String, Object> data);
}
