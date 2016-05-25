package net.allypost.touchthabutton;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        double gameDuration = this.getGameDuration(savedInstanceState);

        if (gameDuration != 0.0) {
            Toast.makeText(getApplicationContext(), "GAME LASTED " + (Math.round(gameDuration * 100) / 100.0) + "s", Toast.LENGTH_LONG).show();
            System.out.println("\t\t\tDB ENTRY:\t" + this.generateDbEntry(gameDuration));
            this.saveLeaderboardEntry(gameDuration);
            this.getLeaderboard();
//            System.out.println("\t\t\tDB:\t" + );
        }

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

    private String getStorageLocation() {
        return getFilesDir().toString();
    }

    private void saveLeaderboardEntry(double gameDuration) {
        String filename = "leaderboard.db";
        String dbEntry = this.generateDbEntry(gameDuration);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(dbEntry.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLeaderboard() {
        try {
            String location = this.getStorageLocation() + "/leaderboard.db";
            InputStream inStream = new FileInputStream(location);
            InputStreamReader inReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inReader);

            String line;

            List<String> lines = new ArrayList<>();

            do {
                line = buffReader.readLine();

                lines.add(line);
                System.out.println("\t\t\t\t\t\tLEADERBOARD-ENTRY:\t" + line);
            } while (line != null);

            if (lines.get(lines.size() - 1) == null)
                lines.remove(lines.size() - 1);

            System.out.println("\t\t\t\t\tLEADERBOARD" + lines.toString());
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private int getTimePosition(double gameDuration) {


        return 5;
    }

    private String generateDbEntry(Double gameDuration) {
        String date = this.getDateTime();

        return (getString(R.string.leaderboard_db_entry, date, gameDuration) + "\n");
    }

    private String getDateTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss", Locale.UK);
        Date now = new Date();

        return sdfDate.format(now);
    }

    public String getAndroidID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private class LeaderboardEntries {
        public Double time;
        public String date;

        public LeaderboardEntries(Double time, String date) {
            this.time = time;
            this.date = date;
        }
    }
}
