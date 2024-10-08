package com.example.appbanhangonline.retrofit;

import com.example.appbanhangonline.model.DonHangModel;
import com.example.appbanhangonline.model.ItemModel;
import com.example.appbanhangonline.model.KhuyenMaiModel;
import com.example.appbanhangonline.model.LoaiSpModel;
import com.example.appbanhangonline.model.MessageModel;
import com.example.appbanhangonline.model.SanPhamMoiModel;
import com.example.appbanhangonline.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> reset_pass(
            @Field("email") String email
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> createOrder(
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("sodienthoai") String sodienthoai,
            @Field("email") String email,
            @Field("soluong") String soluong,
            @Field("tongtien") String tongtien,
            @Field("chitiet") String chitiet
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> getDonHang(
            @Field("iduser") int iduser
    );

    @POST("chitietdonhang.php")
    @FormUrlEncoded
    Observable<ItemModel> chitietdonhang(
            @Field("iddonhang") int iddonhang
    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> timkiem(
            @Field("key") String key
    );
    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updatetoken(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> gettoken(
            @Field("status") int status
    );

    @POST("updatemomo.php")
    @FormUrlEncoded
    Observable<MessageModel> updatemomo(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("updatepass.php")
    @FormUrlEncoded
    Observable<MessageModel> updatepass(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("updatesoluong.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesoluong(
            @Field("id") int id,
            @Field("soluongtonkho") int soluong
    );

    @GET("khuyenmai.php")
    Observable<KhuyenMaiModel> khuyenmai();

    @POST("xoadonhang.php")
    @FormUrlEncoded
    Observable<MessageModel> xoadonahng(
            @Field("id") int id
    );
}
