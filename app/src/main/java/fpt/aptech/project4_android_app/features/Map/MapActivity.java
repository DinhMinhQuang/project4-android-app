package fpt.aptech.project4_android_app.features.Map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.goong.geocoder.places.data.remote.entity.AutoCompleteResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import io.goong.goongsdk.annotations.Polygon;
import io.goong.goongsdk.annotations.PolygonOptions;
import io.goong.goongsdk.location.Utils;
import io.goong.goongsdk.utils.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    static LatLng YOUR_LOCATION;
    static final LatLng DIACHINHA = new LatLng(10.7908028800001, 106.619042317);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_map);
        btnComplete = findViewById(R.id.btnComplete);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelivery.setOnClickListener(view -> delivery());
        btnComplete.setOnClickListener(view -> complete());
        btnCancel.setOnClickListener(view -> cancel());

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
                    Intent intent = new Intent(MapActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStoreLocation() {
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String address = sp.getString("address", null);
        Call<AutoCompleteResponse> call = mapClient.getPlaceId("I5XNVFf02SmWyMubBbmoHapYN5YvBC3zarzZTx7U", address, 2, 150);
        try {
            Response<AutoCompleteResponse> response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDirection(){

    }

    private void getCurrentLocation() {
        GoongMap goongMap;
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoongMap mapboxMap) {
                            YOUR_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(YOUR_LOCATION).title("Bạn ở đây");
                            BaseMarkerOptions markerOptions1 = new MarkerOptions().position(DIACHINHA).title("Store");
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(YOUR_LOCATION, 30));
                            mapboxMap.addMarker(markerOptions);
                            mapboxMap.addMarker(markerOptions1);
                            List<LatLng> points = new ArrayList<>();
                            points.add(YOUR_LOCATION);
                            points.add(DIACHINHA);
                            mapboxMap.addPolyline(new PolylineOptions()
                                    .addAll(points)
                                    .color(Color.parseColor("#3bb2d0"))
                                    .alpha((float) 0.5)
                                    .width(2));

                        }
                });
                }
            }
        });
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

}