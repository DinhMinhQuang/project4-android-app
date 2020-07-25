package fpt.aptech.project4_android_app.api.service;

import java.util.HashMap;

import fpt.aptech.project4_android_app.api.models.Shipper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ShipperClient {

    @POST("/authorization/loginShipper")
    Call<Shipper> login(@Body HashMap<String, String> map);

    @POST("/authorization/registerShipper")
    Call<Shipper> register(@Body Shipper shipper);
}
