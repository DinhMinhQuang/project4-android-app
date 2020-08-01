package fpt.aptech.project4_android_app.features.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsOrderActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    TextView tvStoreName, tvCountPrice, tvUserName, tvAddress, tvAmount, storeName, storeAddress;
    ListView listProduct;
    Button  btnAcceptOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    List<Map<String, ?>> products = response.body().getProducts();
                    tvStoreName.setText(order.getRestaurant().getName());
                    tvCountPrice.setText(order.getAmount() + "đ");
                    tvUserName.setText(order.getUser().getFullname() +" - "+order.getUser().getPhone());
                    tvAddress.setText(order.getAddress());
                    tvAmount.setText(order.getAmount() + "đ");
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

    private void accecptOrder(){
        btnAcceptOrder = findViewById(R.id.btnAcceptOrder);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        btnAcceptOrder.setOnClickListener(view -> {
            Call<Order> call = shipperClient.acceptOrder(access_token, idOrder);
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (!response.isSuccessful()) return;
                    else {
                        Intent intent = new Intent(DetailsOrderActivity.this, MapActivity.class);
                        edit.putString("address", response.body().getRestaurant().getAddress());
                        edit.apply();
                        startActivity(intent);
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