package fpt.aptech.project4_android_app.features.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import fpt.aptech.project4_android_app.R;
import io.goong.goongsdk.Goong;
import io.goong.goongsdk.annotations.MarkerOptions;
import io.goong.goongsdk.camera.CameraPosition;
import io.goong.goongsdk.camera.CameraUpdateFactory;
import io.goong.goongsdk.geometry.LatLng;
import io.goong.goongsdk.maps.GoongMap;
import io.goong.goongsdk.maps.OnMapReadyCallback;
import io.goong.goongsdk.maps.SupportMapFragment;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoongMap mMap;
    EditText editText;
    TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoongMap goongMap) {
        mMap = goongMap;
        LatLng hcm = new LatLng(10.794999, 106.619235);
        mMap.addMarker(new MarkerOptions().position(hcm)
                .title("Marker in Ho Chi Minh City"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .zoom(13.0)
                        .target(hcm)
                        .build()
        ));
        mMap.setMyLocationEnabled(true);
    }

}