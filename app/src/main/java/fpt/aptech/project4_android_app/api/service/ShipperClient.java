package fpt.aptech.project4_android_app.api.service;

import java.util.HashMap;
import java.util.List;

import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Shipper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShipperClient {
    @GET("/shipper/getMyShipper")
    Call<Shipper> getShipperDetails(@Header("Authorization") String access_token);

    @GET("/shipper/getMyCompletedOrders")
    Call<List<Order>> getMyCompletedOrder(@Header("Authorization") String access_token);

    @GET("/shipper/getMyFailedOrders")
    Call<List<Order>> getMyFailedOrder(@Header("Authorization") String access_token);

    @POST("/authorization/loginShipper")
    Call<Shipper> login(@Body HashMap<String, String> map);

    @POST("/authorization/registerShipper")
    Call<Shipper> register(@Body Shipper shipper);

    @POST("/shipper/acceptOrder/{id}")
    Call<Order> acceptOrder(@Header("Authorization") String access_token, @Path("id") String idOrder);


}
