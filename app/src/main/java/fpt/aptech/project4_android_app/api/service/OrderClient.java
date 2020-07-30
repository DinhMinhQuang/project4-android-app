package fpt.aptech.project4_android_app.api.service;

import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.api.models.Order;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderClient {

    @GET("/order/orderfinding")
    Call<List<Order>> getOrders(@Header("Authorization") String access_token);

    @GET("/order/{id}")
    Call<Order> getOrder(@Header("Authorization") String access_token, @Path("id") String id);

}
