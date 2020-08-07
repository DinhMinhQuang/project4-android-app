package fpt.aptech.project4_android_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fpt.aptech.project4_android_app.features.Logon.LoginActivity;
import fpt.aptech.project4_android_app.features.Logon.RegisterActivity;

public class NewShipperActivity extends AppCompatActivity {
    private Button btnSignIn, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle("Food Tap Delivery");
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(NewShipperActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }
}