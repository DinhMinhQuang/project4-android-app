package fpt.aptech.project4_android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.auth0.android.jwt.JWT;
import com.google.android.material.tabs.TabLayout;

import fpt.aptech.project4_android_app.features.Order.ListOrderFragment;
import fpt.aptech.project4_android_app.features.Profile.ShipperProfileFragment;
import fpt.aptech.project4_android_app.features.Statistics.StatisticsFragment;
import io.goong.goongsdk.Goong;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private static MainActivity instance;

    void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Goong.getInstance(this, "euoRPJxMMVNHI30YkR2W5Ysh6zzkLul70rxTptlF");
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Foot Tap Delivery");
        ListOrderFragment listOrderFragment = new ListOrderFragment();
        JWT jwt = (JWT) getIntent().getParcelableExtra("jwt");
        Bundle bundle = new Bundle();
        bundle.putString("access_token", String.valueOf(jwt));
        listOrderFragment.setArguments(bundle);
        instance = this;
        tabLayout = findViewById(R.id.tabLayout);
        frameLayout = findViewById(R.id.frameLayout);
        replaceFragment(new ListOrderFragment());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        replaceFragment(new ListOrderFragment());
                        break;
                    case 1:
                        replaceFragment(new StatisticsFragment());
                        break;
                    case 2:
                        replaceFragment(new ShipperProfileFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}