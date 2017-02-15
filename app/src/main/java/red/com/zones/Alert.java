package red.com.zones;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class Alert extends AppCompatActivity {
    ImageButton imageButton;
    TextView textViewTitle;
    TextView textViewAddress;
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        imageButton = (ImageButton) findViewById(R.id.imageButtonDismiss);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        sharedPreferences = getSharedPreferences("loc_data", MODE_PRIVATE);
        textViewTitle.setText(sharedPreferences.getString("title", ""));
        textViewAddress.setText(sharedPreferences.getString("add", ""));
        Uri urialert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        try {
            mediaPlayer.setDataSource(this, urialert);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
                System.exit(0);
            }
        });
    }
}
