package red.com.zones;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback
        ,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private CircleOptions mcircleOptions;
    private SeekBar seekBarRadius;
    private Double radius;
    private Double minRadius;
    private TextView textViewRadiusValue;
    private LatLng mlatLng;
    private UiSettings uiSettings;
    private FloatingActionButton setAlarm;
    private Geocoder geocoder;
    private String caddress;
    SharedPreferences loc_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        radius = 500d;
        seekBarRadius = (SeekBar) findViewById(R.id.seekBarRadius);
        setAlarm = (FloatingActionButton) findViewById(R.id.setAlarm);
        textViewRadiusValue = (TextView) findViewById(R.id.textViewRadiusValue);
        minRadius = Double.valueOf(20);
        loc_data = getSharedPreferences("loc_data", MODE_PRIVATE);
        SharedPreferences.Editor loc_data_editor = loc_data.edit();
        loc_data_editor.putString("rad", radius + "");
        loc_data_editor.commit();
        mcircleOptions = new CircleOptions();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maps.this, PopUp.class));
            }
        });
        seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMap.clear();
                radius = Double.valueOf(progress);
                if (radius < minRadius) {
                    radius = minRadius;
                }
                textViewRadiusValue.setText(radius + " M");
                mcircleOptions = mcircleOptions.radius(radius);
                mMap.addCircle(mcircleOptions);
                loc_data = getSharedPreferences("loc_data", MODE_PRIVATE);
                SharedPreferences.Editor loc_data_editor = loc_data.edit();
                loc_data_editor.putString("rad", radius + "");
                loc_data_editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (radius < minRadius) {
                    radius = minRadius;
                }
                textViewRadiusValue.setText(radius + " M");
                mcircleOptions = mcircleOptions.radius(radius);
                mMap.addCircle(mcircleOptions);
                SharedPreferences.Editor loc_data_editor = loc_data.edit();
                loc_data_editor.putString("rad", radius + "");
                loc_data_editor.commit();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mcircleOptions = mcircleOptions.center(sydney);
        mcircleOptions = mcircleOptions.radius(500);
        mcircleOptions = mcircleOptions.strokeWidth(2);
        mcircleOptions = mcircleOptions.strokeColor(Color.argb(100, 255, 73, 73));
        mcircleOptions = mcircleOptions.fillColor(Color.argb(100, 255, 73, 73));
        mMap.addCircle(mcircleOptions);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                !=
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mlatLng = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mlatLng));
                mcircleOptions = mcircleOptions.center(mlatLng);
                mMap.addCircle(mcircleOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                geocoder = new Geocoder(Maps.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(mlatLng.latitude, mlatLng.longitude, 1);
                    for (int i=0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                        Log.e("Address", addresses.get(0).getAddressLine(i) + "");
                        caddress = addresses.get(0).getAddressLine(i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loc_data = getSharedPreferences("loc_data", MODE_PRIVATE);
                SharedPreferences.Editor loc_data_editor = loc_data.edit();
                loc_data_editor.putString("lat", Double.toString(mlatLng.latitude));
                loc_data_editor.putString("lng", Double.toString(mlatLng.longitude));
                loc_data_editor.putString("add", caddress + "");
                loc_data_editor.commit();
            }
        });
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }
}
