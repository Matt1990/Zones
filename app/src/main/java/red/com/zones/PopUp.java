package red.com.zones;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Binil Mathew on 27-Dec-16.
 */
public class PopUp extends Activity {
    private TextView textViewOk;
    private TextView textViewCancel;
    private TextView textViewAddress;
    private TextView textViewradius;
    private EditText editTextTitle;
    int width;
    int height;
    SharedPreferences sharedPreferences;
    DisplayMetrics displayMetrics;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        textViewOk = (TextView) findViewById(R.id.textViewOk);
        textViewCancel = (TextView) findViewById(R.id.textViewCalcel);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewradius = (TextView) findViewById(R.id.textViewRadiusValue);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("loc_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("title", editTextTitle.getText().toString());
                editor.apply();
                finish();
                startActivity(new Intent(PopUp.this, MainActivity.class));
            }
        });
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.e("Width-Height", width + "-" + height );
        getWindow().setLayout(400, 200);
        sharedPreferences = getSharedPreferences("loc_data", MODE_PRIVATE);
        textViewAddress.setText(sharedPreferences.getString("add", ""));
        textViewradius.setText(sharedPreferences.getString("rad", "") + "M");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
