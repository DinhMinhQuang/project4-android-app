package fpt.aptech.project4_android_app.features.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.network.RetroClass;
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
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order);
        getSupportActionBar().setTitle("Danh sách đơn hoàn thành");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCompletedOrders();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), StatisticsFragment.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void getCompletedOrders() {
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        progressBar = findViewById(R.id.progressBar);
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
                    ListCompletedAdapter listAdapter;
                    if (getApplication() != null) {
                        listAdapter = new ListCompletedAdapter(getApplicationContext(), R.layout.details_completed_orders, orders);
                        listViewCompleted.setAdapter(listAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                    listViewCompleted.setOnItemClickListener((adapterView, view, i, l) -> {
                        Intent intent = new Intent(getBaseContext(), StatisticDetailOrdersActivity.class);
                        edit.putString("idOrder", response.body().get(i).getId());
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(CompletedOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}