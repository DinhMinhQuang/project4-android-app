package fpt.aptech.project4_android_app.api.service;

import java.util.List;

import fpt.aptech.project4_android_app.api.models.Order;
import retrofit2.Call;
import retrofit2.http.GET;

public interface OrderClient {
    @GET("/order")
    Call<List<Order>> getOrders();
}
