package fpt.aptech.project4_android_app.features.Statistics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ProgressBar progressBar;
    LinearLayout redirectAcceptOrder, redirectFailOrder, wrap, redirectStars;
    TextView tvAcceptOrder, tvFailOrder, tvAmount, tvStar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void shipperRating() {
        sp = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Call<Shipper> call = shipperClient.getShipperDetails(access_token);
        call.enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                if (!response.isSuccessful()) return;
                else {
                    List<Map> rating = (ArrayList) (response.body().getRating());
                    double countRater = 0;
                    double stars = 0;
                    for (Map item : rating){
                        countRater++;
                        stars += Double.parseDouble(String.valueOf(item.get("stars")));
                    }
                    double counting = stars / countRater;
                    tvStar.setText(Integer.toString((int) counting));
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void statistics(){
        sp = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Call<List<Order>> call = shipperClient.getMyOrders(access_token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!response.isSuccessful()) return;
                else {
                    String COUNTRY = "VN";
                    String LANGUAGE = "vi";
                    int countFailed = 0;
                    int countComplete = 0;
                    double sumAmount = 0;
                    double realAmount = 0;
                    List<Order> orders = response.body();
                    for(Order order: orders){
                        if (order.getStatus().equalsIgnoreCase("completed")){
                            countComplete++;
                            sumAmount += order.getFee();
                            realAmount = (sumAmount * 80)/100;
                        }
                        if (order.getStatus().equalsIgnoreCase("canceled")) {
                            countFailed++;
                        }
                    }
                    String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(realAmount);
                    tvFailOrder.setText(String.valueOf(countFailed));
                    tvAcceptOrder.setText(String.valueOf(countComplete));
                    tvAmount.setText(str);
                    progressBar.setVisibility(View.GONE);
                    wrap.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        wrap = view.findViewById(R.id.wrap);
        progressBar = view.findViewById(R.id.progressBar);
        redirectStars = view.findViewById(R.id.redirectStar);
        redirectAcceptOrder = view.findViewById(R.id.redirectAcceptOrder);
        redirectFailOrder = view.findViewById(R.id.redirectFailOrder);
        tvAcceptOrder = view.findViewById(R.id.tvAcceptOrder);
        tvFailOrder = view.findViewById(R.id.tvFailOrder);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvStar = view.findViewById(R.id.tvStar);
        redirectStars.setOnClickListener(view13 -> {
            Intent intent = new Intent(getActivity(), RatingActivity.class);
            startActivity(intent);
        });
        redirectAcceptOrder.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CompletedOrderActivity.class);
            startActivity(intent);
        });
        redirectFailOrder.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), FailedOrderActivity.class);
            startActivity(intent);
        });
        statistics();
        shipperRating();
        return view;
    }
}