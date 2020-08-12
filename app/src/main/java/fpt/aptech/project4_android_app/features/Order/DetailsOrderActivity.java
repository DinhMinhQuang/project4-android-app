package fpt.aptech.project4_android_app.features.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Cart;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Product;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import fpt.aptech.project4_android_app.features.Map.MapActivity;
import io.goong.goongsdk.geometry.LatLng;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fpt.aptech.project4_android_app.Notification.ACCEPTED_ORDER;
import static fpt.aptech.project4_android_app.Notification.BACK_TO_YOUR_ORDER;
import static fpt.aptech.project4_android_app.Notification.CHANNEL_CANCEL;
import static fpt.aptech.project4_android_app.Notification.CHANNEL_CANCEL_BY_USER;


public class DetailsOrderActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    TextView tvStoreName, tvCountPrice, tvUserName, tvAddress, tvAmount, storeName, storeAddress;
    ListView listProduct;
    Button  btnAcceptOrder;
    FusedLocationProviderClient client;
    static LatLng YOUR_LOCATION;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(DetailsOrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            accecptOrder();
        } else {
            ActivityCompat.requestPermissions(DetailsOrderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        getDetailsOrder();
        accecptOrder();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void getDetailsOrder(){
        listProduct = findViewById(R.id.listProduct);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvCountPrice = findViewById(R.id.tvCountPrice);
        tvUserName = findViewById(R.id.tvUserName);
        tvAddress = findViewById(R.id.tvAddress);
        tvAmount = findViewById(R.id.tvAmount);
        storeAddress = findViewById(R.id.storeAddress);
        storeName = findViewById(R.id.storeName);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        Call<Order> call = orderClient.getOrder(access_token, idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()){
                    return;
                }
                else {
                    Order order = response.body();
                    String COUNTRY = "VN";
                    String LANGUAGE = "vi";
                    String countPrice = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(order.getAmount());
                    List<Map<String, ?>> products = response.body().getProducts();
                    tvStoreName.setText(order.getRestaurant().getName());
                    tvCountPrice.setText(countPrice);
                    tvUserName.setText(order.getUser().getFullname() +" - "+order.getUser().getPhone());
                    tvAddress.setText(order.getAddress());
                    tvAmount.setText(countPrice);
                    storeName.setText(order.getRestaurant().getName());
                    storeAddress.setText(order.getRestaurant().getAddress());
                    List<Map<String, ?>> list=new ArrayList<>();
                    for (Map<String,?> item:
                         products) {
                        Map temp = new HashMap<>();
                        temp.put("quantity",item.get("quantity"));
                        temp.put("productName",((Map) item.get("product")).get("name"));
                        temp.put("image",((Map) item.get("product")).get("image"));
                        temp.put("price", ((Map) item.get("product")).get("price"));
                        list.add(temp);
                    }
                    CustomList customList;
                    if (getApplication() != null) {
                        customList = new CustomList(getApplication(), list, R.layout.details_list_product, new String[] {},
                                new int[] {});
                        listProduct.setAdapter(customList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(DetailsOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                accecptOrder();
            }
        }
    }

    private void accecptOrder(){
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                YOUR_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
        btnAcceptOrder = findViewById(R.id.btnAcceptOrder);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        btnAcceptOrder.setOnClickListener(view -> {
            Call<Order> call = shipperClient.acceptOrder(access_token, idOrder, YOUR_LOCATION);
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.body().getMessage() != null) {
                        if (response.body().getMessage().equalsIgnoreCase("2")) {
                            Intent back = new Intent(getApplication(), MapActivity.class);
                            startActivity(back);
                            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                            Notification mBuilder = new NotificationCompat.Builder(getApplication(), BACK_TO_YOUR_ORDER)
                                    .setSmallIcon(R.drawable.fooddelivery)
                                    .setContentTitle("Vui lòng hoàn thành đơn hiện tại")
                                    .setContentText("Đừng tham lam")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                                    .build();
                            notificationManagerCompat.notify(1, mBuilder);
                            return;
                        }
                        else {
                            Intent back = new Intent(getApplication(), MainActivity.class);
                            startActivity(back);
                            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                            Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL_BY_USER)
                                    .setSmallIcon(R.drawable.fooddelivery)
                                    .setContentTitle("Thông báo")
                                    .setContentText("Đơn hàng đã có người khác chấp nhận")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                                    .build();
                            notificationManagerCompat.notify(1, mBuilder);
                            return;
                        }
                    }
                    else {
                        Intent intent = new Intent(DetailsOrderActivity.this, MapActivity.class);
                        edit.putString("address", response.body().getRestaurant().getAddress());
                        edit.apply();
                        startActivity(intent);
                        notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                        Notification mBuilder = new NotificationCompat.Builder(getApplication(), ACCEPTED_ORDER)
                                .setSmallIcon(R.drawable.fooddelivery)
                                .setContentTitle("Bạn đã nhận thành công 1 đơn")
                                .setContentText("Đi tới cửa hàng nào")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                                .build();
                        notificationManagerCompat.notify(1, mBuilder);
                    }

                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(DetailsOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}