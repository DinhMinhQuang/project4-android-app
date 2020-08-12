package fpt.aptech.project4_android_app.features.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.goong.geocoder.places.data.remote.entity.AutoCompleteResponse;
import com.goong.geocoder.places.data.remote.entity.ChildPlace;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.NewShipperActivity;
import fpt.aptech.project4_android_app.api.models.LatLngDeserializer;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import fpt.aptech.project4_android_app.features.Order.CustomAdapter;
import io.goong.goongsdk.annotations.Icon;
import io.goong.goongsdk.annotations.IconFactory;
import io.goong.goongsdk.annotations.Polygon;
import io.goong.goongsdk.annotations.PolygonOptions;
import io.goong.goongsdk.location.Utils;
import io.goong.goongsdk.utils.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.network.RetroMap;
import fpt.aptech.project4_android_app.api.service.MapService.MapClient;
import io.goong.goongsdk.annotations.BaseMarkerOptions;
import io.goong.goongsdk.annotations.MarkerOptions;
import io.goong.goongsdk.annotations.PolylineOptions;
import io.goong.goongsdk.camera.CameraUpdateFactory;
import io.goong.goongsdk.geometry.LatLng;
import io.goong.goongsdk.maps.GoongMap;
import io.goong.goongsdk.maps.OnMapReadyCallback;
import io.goong.goongsdk.maps.SupportMapFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fpt.aptech.project4_android_app.Notification.CHANNEL_CANCEL;
import static fpt.aptech.project4_android_app.Notification.CHANNEL_CANCEL_BY_USER;
import static fpt.aptech.project4_android_app.Notification.COMPLETED_ORDER;
import static fpt.aptech.project4_android_app.Notification.DELIVERY_ORDER;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    GoongMap mMap;
    EditText editText;
    TextView textView, textView2;
    public static final String PREFS = "PREFS";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    Button btnComplete, btnDelivery, btnCancel;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    MapClient mapClient = RetroMap.getRetrofitInstance().create(MapClient.class);
    Map listLngLat;
    String[] userLngLatArray;
    String[] restaurantLngLatArray;
    static LatLng YOUR_LOCATION;
    static LatLng userLatlng;
    static LatLng restaurantLatLng;
    List<ChildPlace> childPlaces;
    GoongMap goongMap;
    private NotificationManagerCompat notificationManagerCompat;
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://0b78bf0553f2.ngrok.io");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);
        btnComplete = findViewById(R.id.btnComplete);
        btnDelivery = findViewById(R.id.btnDelivery);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt1 = sp.getString("jwt", null);
        JWT jwt = new JWT(jwt1);
        Claim subscriptionMetaData = jwt.getClaim("_id");
        String shipperId = subscriptionMetaData.asString();
        mSocket.connect();
        mSocket.emit("join", shipperId);
        mSocket.on("cancelOrder", args -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
            Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL_BY_USER)
                    .setSmallIcon(R.drawable.fooddelivery)
                    .setContentTitle("Khách hàng vừa hủy đơn của bạn")
                    .setContentText("Khách hàng này có vẻ không muốn tiếp tục giao dịch")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .build();
            notificationManagerCompat.notify(1, mBuilder);
            service.shutdown();
        });
        mSocket.on("blockShipper", arg -> {
            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
            Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL_BY_USER)
                    .setSmallIcon(R.drawable.fooddelivery)
                    .setContentTitle("Tài khoản của bạn đã bị khóa")
                    .setContentText("Vui lòng đến trụ sở FoodTap để mở khóa tài khoản")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .build();
            notificationManagerCompat.notify(1, mBuilder);
            service.shutdown();
            android.os.Process.killProcess(android.os.Process.myPid());
            Intent intent = new Intent(getApplication(), NewShipperActivity.class);
            startActivity(intent);
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnDelivery.setOnClickListener(view -> delivery());
        btnComplete.setOnClickListener(view -> complete());
        btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage(R.string.cancel_order).setPositiveButton(R.string.accept_cancel_order, (dialogInterface, i) -> cancel())
                        .setNegativeButton(R.string.keep_order, (dialogInterface, i) -> { return;});
            alertDialog.create();
            alertDialog.show();
        });


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }



    private void cancel(){
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        Call<Order> call = shipperClient.cancelOrder(access_token, idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) {
                    response.message();
                    return;
                }
                else {
                    //alert
                    Intent intent = new Intent(MapActivity.this, MainActivity.class);
                    startActivity(intent);
                    notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                    Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL)
                            .setSmallIcon(R.drawable.fooddelivery)
                            .setContentTitle("Bạn vừa hủy 1 đơn")
                            .setContentText("Hủy quá 3 đơn sẽ bị khóa tài khoản")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                            .build();
                    notificationManagerCompat.notify(1, mBuilder);
                    service.shutdown();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void delivery(){
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        Call<Order> call = shipperClient.deliveringOrder(access_token, idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) {
                    response.message();
                    return;
                }
                else {
                    response.body();
                    btnDelivery.setVisibility(View.GONE);
                    btnComplete.setVisibility(View.VISIBLE);
                    notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                    Notification mBuilder = new NotificationCompat.Builder(getApplication(), DELIVERY_ORDER)
                            .setSmallIcon(R.drawable.fooddelivery)
                            .setContentTitle("Bạn đã nhận món ăn tại cửa hàng")
                            .setContentText("Cùng giao tới khách hàng nào....")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                            .build();
                    notificationManagerCompat.notify(1, mBuilder);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void complete(){
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        Call<Order> call = shipperClient.completeOrder(access_token, idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) return;
                else {
                    Order order = response.body();
                    double yourFee = (order.getFee() * 80) / 100;

                    Intent intent = new Intent(MapActivity.this, MainActivity.class);
                    startActivity(intent);
                    Notification mBuilder = new NotificationCompat.Builder(getApplication(), COMPLETED_ORDER)
                            .setSmallIcon(R.drawable.fooddelivery)
                            .setContentTitle("Bạn vừa hoàn thành 1 đơn")
                            .setContentText("Số tiền được cộng thêm vào tài khoản là "+String.valueOf(yourFee).toString().split("\\.")[0]+"đ")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                            .build();
                    notificationManagerCompat.notify(1, mBuilder);
                    service.shutdown();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation() {

        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Runnable runnable = () -> {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoongMap mapboxMap) {
                                goongMap = mapboxMap;
                                YOUR_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions().position(YOUR_LOCATION).title("Bạn ở đây");
                                goongMap.animateCamera(CameraUpdateFactory.newLatLngZoom(YOUR_LOCATION, 15));
                                goongMap.addMarker(markerOptions);
                                Call<Object> call = shipperClient.sendMyLocation(access_token, YOUR_LOCATION);
                                call.enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (!response.isSuccessful()) return;
                                        else {
                                            IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
                                            Icon icon = iconFactory.fromResource(R.drawable.userlocation);
                                            Icon iconres = iconFactory.fromResource(R.drawable.location);
                                            LatLng userLatLng = new LatLng();
                                            LatLng restaurantLatLng = new LatLng();
                                            double latUser = Double.parseDouble(String.valueOf(((ArrayList)((ArrayList)response.body()).get(0)).get(0)));
                                            double lngUser = Double.parseDouble(String.valueOf(((ArrayList)((ArrayList)response.body()).get(0)).get(1)));
                                            double latRestaurant = Double.parseDouble(String.valueOf(((ArrayList)((ArrayList)response.body()).get(1)).get(0)));
                                            double lngRestaurant = Double.parseDouble(String.valueOf(((ArrayList)((ArrayList)response.body()).get(1)).get(1)));
                                            userLatLng.setLatitude(latUser);
                                            userLatLng.setLongitude(lngUser);
                                            restaurantLatLng.setLatitude(latRestaurant);
                                            restaurantLatLng.setLongitude(lngRestaurant);
                                            MarkerOptions markerOptions1 = new MarkerOptions().position(userLatLng).icon(icon).title("Người đặt");
                                            MarkerOptions markerOptions2 = new MarkerOptions().position(restaurantLatLng).icon(iconres).title("Cửa hàng");
                                            goongMap.addMarker(markerOptions1);
                                            goongMap.addMarker(markerOptions2);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                    }
                                });
                            }
                        });
                    }
                }
            });
        };
        service.scheduleAtFixedRate(runnable, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoongMap mapboxMap) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSocket.disconnect();
        service.shutdown();
    }
}