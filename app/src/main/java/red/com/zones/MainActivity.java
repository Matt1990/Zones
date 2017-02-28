package red.com.zones;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addAlarm;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;
    String[] title = {""};
    String[] address = {""};
    String[] radius = {""};
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("loc_data", MODE_PRIVATE);
        title[0] = sharedPreferences.getString("title", "Default");
        address[0] = sharedPreferences.getString("add", "");
        radius[0] = sharedPreferences.getString("rad", "");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_alarmlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(title, address, radius, context);
        recyclerView.setAdapter(adapter);
        addAlarm = (FloatingActionButton) findViewById(R.id.add_alarm);
        try{
            if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
                final AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setMessage("This application needs GPS to work");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        }
        catch (Exception ex){
            Log.e("GPS-Error", ex + "");
        }
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Maps.class));
            }
        });
    }
}
