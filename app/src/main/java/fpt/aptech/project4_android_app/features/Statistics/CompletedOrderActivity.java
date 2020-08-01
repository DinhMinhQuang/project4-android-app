package fpt.aptech.project4_android_app.features.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedOrderActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    ListView listViewCompleted;
    TextView tvRestaurant, tvUserPhone, tvAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order);
        getCompletedOrders();
    }

    private void getCompletedOrders() {
        listViewCompleted = findViewById(R.id.listViewCompleted);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Call<List<Order>> call = shipperClient.getMyCompletedOrder(access_token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!response.isSuccessful()) return;
                else {
                    List<Order> orders = response.body();
                    ListAdapter listAdapter;
                    if (getApplication() != null) {
                        listAdapter = new ListAdapter(getApplicationContext(), R.layout.details_completed_order, orders);
                        listViewCompleted.setAdapter(listAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(CompletedOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}