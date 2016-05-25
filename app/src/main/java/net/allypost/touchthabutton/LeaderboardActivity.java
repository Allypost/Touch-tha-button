package net.allypost.touchthabutton;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.FileOutputStream;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        double gameDuration = this.getGameDuration(savedInstanceState);

        if (gameDuration != 0.0)
            Toast.makeText(getApplicationContext(), "GAME LASTED " + (Math.round(gameDuration * 100) / 100.0) + "s", Toast.LENGTH_LONG).show();
    }

    private double getGameDuration(Bundle savedInstanceState) {
        double gameDuration = 0.0;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                gameDuration = extras.getDouble(PlayActivity.getTimeKey(), 0.0);
            }
        } else {
            gameDuration = savedInstanceState.getDouble(PlayActivity.getTimeKey(), 0.0);
        }

        return gameDuration;
    }

    private void saveLeaderboardEntry() {
        String filename = "leaderboard.db";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAndroidID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
