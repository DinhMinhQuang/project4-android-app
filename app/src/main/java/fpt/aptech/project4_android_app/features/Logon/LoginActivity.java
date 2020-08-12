package fpt.aptech.project4_android_app.features.Logon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
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

import static fpt.aptech.project4_android_app.Notification.BACK_TO_YOUR_ORDER;
import static fpt.aptech.project4_android_app.Notification.CHANNEL_CANCEL;

public class
LoginActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private EditText phone, password;
    private Button btnLogin;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Food Tap Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        login();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), NewShipperActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    private Socket mSocket;{
        try {
            mSocket = IO.socket("http://0b78bf0553f2.ngrok.io");
        } catch (URISyntaxException e) {}
    }
    private void login(){
        sp = getApplicationContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        edit = sp.edit();
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            HashMap<String, String> map = new HashMap<>();
            if (phone.getText().toString().isEmpty()){
                phone.requestFocus();
                phone.setError("Số điện thoại không được để trống");
            }
            else if(password.getText().toString().isEmpty()){
                password.requestFocus();
                password.setError("Mật khẩu không được để trống");
            }
            else {
                map.put("phone", phone.getText().toString());
                map.put("password", password.getText().toString());
                Call<Shipper> call = shipperClient.login(map);
                call.enqueue(new Callback<Shipper>() {
                    @Override
                    public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                        if (response.body().getAccessToken().equalsIgnoreCase("disabled")) {
                            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                            Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL)
                                    .setSmallIcon(R.drawable.fooddelivery)
                                    .setContentTitle("Tài khoản hiện tại đang bị khóa")
                                    .setContentText("Vui lòng liên hệ với trụ sở FoodTap")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                                    .build();
                            notificationManagerCompat.notify(1, mBuilder);
                            return;
                        }
                        else if (response.body().getAccessToken().equalsIgnoreCase("wrongPassword") || response.body().getAccessToken().equalsIgnoreCase("wrongPhone")){
                            notificationManagerCompat = NotificationManagerCompat.from(getApplication());
                            Notification mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_CANCEL)
                                    .setSmallIcon(R.drawable.fooddelivery)
                                    .setContentTitle("Food Tap Delivery")
                                    .setContentText("Sai tên tài khoản hoặc mật khẩu")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                                    .build();
                            notificationManagerCompat.notify(1, mBuilder);
                            return;
                        }
                        else {
                            Log.d("Tag", response.code()+ "");
                            JWT jwt = new JWT(response.body().getAccessToken());
                            Claim subscriptionMetaData = jwt.getClaim("_id");
                            String shipperId = subscriptionMetaData.asString();
                            mSocket.emit("join", shipperId);
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
                        builder.setNeutralButton("OK", (dialogInterface, i) -> {

                        });
                        builder.show();
                    }
                });
            }
        });
    }




}