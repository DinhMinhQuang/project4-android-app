package fpt.aptech.project4_android_app.api.service;

import java.util.HashMap;

import fpt.aptech.project4_android_app.api.models.Login;
import fpt.aptech.project4_android_app.api.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("authorization/login")
    Call<User> login(@Body HashMap<String, String> map);

    @GET("secretinfo")
    Call<ResponseBody> getSerect(@Header("Authorization") String autToken);
}
