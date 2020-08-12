package fpt.aptech.project4_android_app.features.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import fpt.aptech.project4_android_app.features.Order.CustomList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticDetailOrdersActivity extends AppCompatActivity {
    TextView tvStoreName, tvCountPrice, tvUserName, tvAddress, tvAmount, storeName, storeAddress;
    Button btnBackToMain;
    ListView listProduct;
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_detail_orders);
        getSupportActionBar().setTitle("Chi tiết đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });
        getDetailsOrders();
    }

    private void getDetailsOrders(){
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String orderId = sp.getString("orderId", null);
        listProduct = findViewById(R.id.listProduct);
        tvAddress = findViewById(R.id.tvAddress);
        tvCountPrice = findViewById(R.id.tvCountPrice);
        tvUserName = findViewById(R.id.tvUserName);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvAmount = findViewById(R.id.tvAmount);
        storeAddress = findViewById(R.id.storeAddress);
        storeName = findViewById(R.id.storeName);
        Call<Order> call = orderClient.getOrder(access_token, orderId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (!response.isSuccessful()) return;
                else {
                    List<Map<String, ?>> products = response.body().getProducts();
                    String COUNTRY = "VN";
                    String LANGUAGE = "vi";
                    String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(response.body().getAmount());
                    String fee = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(response.body().getFee());
                    tvCountPrice.setText(fee);
                    tvAddress.setText(response.body().getAddress());
                    tvUserName.setText(response.body().getUser().getFullname());
                    tvStoreName.setText(response.body().getRestaurant().getName());
                    tvAmount.setText(str);
                    storeAddress.setText(response.body().getRestaurant().getAddress());
                    List<Map<String, ?>> list=new ArrayList<>();
                    for (Map<String,?> item:
                            products) {
                        Map temp = new HashMap<>();
                        temp.put("quantity",item.get("quantity"));
                        temp.put("productName",((Map) item.get("product")).get("name"));
                        temp.put("image",((Map) item.get("product")).get("image"));
                        temp.put("price",((Map) item.get("product")).get("price"));
                        list.add(temp);
                    }
                    ProductList customList;
                    if (getApplication() != null) {
                        customList = new ProductList(getApplication(), list, R.layout.details_list_product, new String[] {},
                                new int[] {});
                        listProduct.setAdapter(customList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(StatisticDetailOrdersActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}