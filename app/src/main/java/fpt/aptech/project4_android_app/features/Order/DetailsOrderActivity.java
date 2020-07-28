package fpt.aptech.project4_android_app.features.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import fpt.aptech.project4_android_app.features.Map.MapFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsOrderActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    TextView tvStoreName, tvAmount, tvUserName, tvPhoneNumber, tvAddress;
    ImageView btnMap;
    Button  btnAcceptOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);
        getSupportActionBar().setTitle("Đơn Hàng Của Bạn");
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
        tvStoreName = findViewById(R.id.tvStoreName);
        tvAmount = findViewById(R.id.tvAmount);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvAddress = findViewById(R.id.tvAddress);
        btnMap = findViewById(R.id.btnMap);
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
                    tvStoreName.setText(order.getCreatedAt());
                    tvAmount.setText(Double.toString(order.getAmount()));
                    tvUserName.setText(order.getUser());
                    tvPhoneNumber.setText(order.getNote());
                    tvAddress.setText(order.getAddress());
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
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Order> call = shipperClient.acceptOrder(access_token, idOrder);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(DetailsOrderActivity.this, "Đơn hàng đã được chấp nhận bởi người khác", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Toast.makeText(DetailsOrderActivity.this, "Bạn đã nhận đơn", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), MapFragment.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                });
            }
        });
    }
}