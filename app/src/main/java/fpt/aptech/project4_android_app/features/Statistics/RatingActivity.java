package fpt.aptech.project4_android_app.features.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {
    TextView comment, start;
    ListView listRating;
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle("Tất cả đánh giá của bạn");
        getRating();
    }
    private void getRating(){
        listRating = findViewById(R.id.listRating);
        sp = this.getApplication().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        String orderId = sp.getString("orderId", null);
        Call<Shipper> call = shipperClient.getShipperDetails(access_token);
        call.enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                if (!response.isSuccessful()) return;
                else {
                    List<Map> rating = (ArrayList) (response.body().getRating());
                    List<Map<String, ?>> list=new ArrayList<>();
                    for (Map item:
                            rating) {
                        Map temp = new HashMap<>();
                        temp.put("stars", item.get("stars")) ;
                        temp.put("comment", item.get("comment"));
                        list.add(temp);
                    }
                    RatingAdapter ratingAdapter;
                    if (getApplication() != null) {
                        ratingAdapter = new RatingAdapter(getApplication(), list, R.layout.list_rating, new String[] {},
                                new int[] {});
                        listRating.setAdapter(ratingAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                Toast.makeText(RatingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}