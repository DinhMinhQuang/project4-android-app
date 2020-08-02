package fpt.aptech.project4_android_app.features.Logon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.auth0.android.jwt.JWT;

import java.util.HashMap;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.NewShipperActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
LoginActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private EditText phone, password;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Foot Tap Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        login();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), NewShipperActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    private void login(){
        sp = getApplicationContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        edit = sp.edit();
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone.getText().toString());
            map.put("password", password.getText().toString());
            Call<Shipper> call = shipperClient.login(map);
            call.enqueue(new Callback<Shipper>() {
                @Override
                public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                    if(!response.isSuccessful()){
                        return;
                    }
                    else {
                        Log.d("Tag", response.code()+ "");
                        JWT jwt = new JWT(response.body().getAccessToken());
                        edit.putString("jwt", String.valueOf(jwt));
                        edit.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("jwt", jwt);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Shipper> call, Throwable t) {
                    call.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Received Message");
                    builder.setCancelable(true);
                    builder.setTitle("Can not connect to backend");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            });
        });
    }




}