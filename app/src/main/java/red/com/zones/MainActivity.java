package red.com.zones;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addAlarm;
    FloatingActionButton settings;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
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
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Maps.class));
            }
        });
        settings = (FloatingActionButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}
