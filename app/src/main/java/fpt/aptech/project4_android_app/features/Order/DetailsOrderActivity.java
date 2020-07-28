package fpt.aptech.project4_android_app.features.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsOrderActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    SharedPreferences sp;
    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_order);
        getSupportActionBar().setTitle("Đơn Hàng Của Bạn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        getDetailsOrder();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void getDetailsOrder(){
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String idOrder = sp.getString("orderId", null);
        Call<Order> call = orderClient.getOrder(access_token, idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order order = response.body();
                List<Map<String, Object>> list=new ArrayList<>();
                list.add((Map<String, Object>) order);
                Map<String,Object> map = new HashMap<>();
                map.put("_id", order.getId());
                map.put("amount", order.getAmount());
                map.put("address", order.getAddress());
                list.add(map);
                String fromArray[]={"_id","amount", "address"};
                int to[]={R.id.tvStore,R.id.tvAmount, R.id.tvAddress};
                SimpleAdapter adapter = new SimpleAdapter(getApplication(), list, R.layout.bottom_sheet, fromArray, to);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(DetailsOrderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}