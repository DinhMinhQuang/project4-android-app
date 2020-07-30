package fpt.aptech.project4_android_app.features.Order;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Order;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.OrderClient;
import io.goong.goongsdk.Goong;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PREFS = "PREFS";
    OrderClient orderClient = RetroClass.getRetrofitInstance().create(OrderClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Order> orders;
    public ListOrderFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOrderFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static ListOrderFragment newInstance(String param1, String param2) {
        ListOrderFragment fragment = new ListOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Socket mSocket;{
        try {
            mSocket = IO.socket("http://8d054773c784.ngrok.io");
        } catch (URISyntaxException e) {}
    }
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        orders = new ArrayList<>();
        mSocket.connect();
        mSocket.on("newOrder", order);
    }

    private void getOrder(){
        sp = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Call<List<Order>> call = orderClient.getOrders(access_token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                else {
                    String fromArray[]={"address","status", "amount"};
                    int to[]={R.id.tvStore,R.id.tvDistance, R.id.tvPrice};
                    orders = response.body();
                    List<Map<String, Object>> list=new ArrayList<>();

                    for (Order order:
                         orders) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("_id", order.getId());
                        map.put("address",order.getAddress());
                        map.put("status",order.getStatus());
                        map.put("amount",order.getAmount());
                        list.add(map);
                    }

                    SimpleAdapter simpleAdapter;
                    if (getActivity() != null) {
                        simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.order_details, fromArray, to);
                        listView.setAdapter(simpleAdapter);
                    }
                    listView.setOnItemClickListener((adapterView, view, i, l) -> {
                        Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
                        edit.putString("orderId", String.valueOf(list.get(i).get("_id")));
                        edit.apply();
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Emitter.Listener order = args -> getActivity().runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Gson gson = new Gson();
        Order order = gson.fromJson(data.toString(), Order.class);
        orders.add(order);
        List<Map<String, Object>> list=new ArrayList<>();

        for (Order item:
                orders) {
            Map<String,Object> map = new HashMap<>();
            map.put("_id", item.getId());
            map.put("address",item.getAddress());
            map.put("status",item.getStatus());
            map.put("amount",item.getAmount());
            list.add(map);
        }
        String fromArray[]={"address","status", "amount"};
        int to[]={R.id.tvStore,R.id.tvDistance, R.id.tvPrice};
        SimpleAdapter simpleAdapter;
        if (getActivity() != null) {
            simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.order_details, fromArray, to);
            listView.setAdapter(simpleAdapter);
        }
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
            edit.putString("orderId", String.valueOf(list.get(i).get("_id")));
            edit.apply();
            startActivity(intent);
        });
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_order, container, false);
        // Inflate the layout for this fragment
        listView = view.findViewById(R.id.listView);
        listView.setDivider(null);
        getOrder();
        return view;
    }
}