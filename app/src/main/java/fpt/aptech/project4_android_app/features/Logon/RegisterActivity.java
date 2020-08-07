package fpt.aptech.project4_android_app.features.Logon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fpt.aptech.project4_android_app.MainActivity;
import fpt.aptech.project4_android_app.NewShipperActivity;
import fpt.aptech.project4_android_app.R;
import fpt.aptech.project4_android_app.api.models.Shipper;
import fpt.aptech.project4_android_app.api.network.RetroClass;
import fpt.aptech.project4_android_app.api.service.ShipperClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fpt.aptech.project4_android_app.features.Logon.LoginActivity.PREFS;

public class RegisterActivity extends AppCompatActivity {
    public static final String PREFS = "PREFS";
    private Button register;
    private EditText fullName, phone, password, idCard;
    private CheckBox male, female;
    ShipperClient shipperClient = RetroClass.getRetrofitInstance().create(ShipperClient.class);
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Food Tap Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        register();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), NewShipperActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void register() {
        sp = getApplicationContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        edit = sp.edit();
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        fullName = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        idCard = findViewById(R.id.idCard);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shipper shipper = new Shipper();
                shipper.setFullname(fullName.getText().toString());
                shipper.setPhone(phone.getText().toString());
                shipper.setPassword(password.getText().toString());
                shipper.setIdCard(idCard.getText().toString());
                shipper.setGender(male.isChecked() ? true : false);
                Call<Shipper> shipperCall = shipperClient.register(shipper);
                shipperCall.enqueue(new Callback<Shipper>() {
                    @Override
                    public void onResponse(Call<Shipper> call, Response<Shipper> response) {
                        if (!response.isSuccessful()){
                            return;
                        }
                        else {
                            Log.d("Tag", response.code()+ "");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            edit.putString("phone", shipper.getPhone());
                            edit.apply();
                            intent.putExtra("phone", shipper.getPhone());
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<Shipper> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}