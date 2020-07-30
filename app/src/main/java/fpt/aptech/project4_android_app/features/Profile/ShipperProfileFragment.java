package fpt.aptech.project4_android_app.features.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import java.util.Date;

import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShipperProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShipperProfileFragment extends Fragment {
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShipperProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShipperProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShipperProfileFragment newInstance(String param1, String param2) {
        ShipperProfileFragment fragment = new ShipperProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    TextView tvFullName, tvIdCard, tvDob, tvPhoneNumber, tvGender;
    Button btnChangePassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void getProfileUser(){
        sp = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        edit = sp.edit();
        String jwt = sp.getString("jwt", null);
        String access_token = "JWT "+jwt;
        Call<Shipper> call = shipperClient.getShipperDetails(access_token);
        call.enqueue(new Callback<Shipper>() {
            @Override
            public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Cant Connect", Toast.LENGTH_SHORT).show();
                }
                else {
                    Shipper shipper = response.body();
                    tvFullName.setText(shipper.getFullname());
                    tvDob.setText(String.valueOf(shipper.getDob()));
                    tvGender.setText(shipper.getGender() ? "Nam" : "Nữ");
                    tvIdCard.setText(shipper.getIdCard());
                    tvPhoneNumber.setText(shipper.getPhone());
                }
            }

            @Override
            public void onFailure(Call<Shipper> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvDob = view.findViewById(R.id.tvDob);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvGender = view.findViewById(R.id.tvGender);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        getProfileUser();
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordFragment.class);
                startActivity(intent);
            }
        });
        return view;
    }
}