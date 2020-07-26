package fpt.aptech.project4_android_app.api.service;

import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.api.models.Order;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OrderClient {
    @GET("/order")
    Call<List<Order>> getOrders(@Header("Authorization") String access_token);
}
