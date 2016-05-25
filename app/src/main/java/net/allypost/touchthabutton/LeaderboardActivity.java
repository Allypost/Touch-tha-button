package net.allypost.touchthabutton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        String id = "";
        int gamePlace = 0;

        double gameDuration = this.getGameDuration(savedInstanceState);

        if (gameDuration != 0.0) {
            id = this.saveLeaderboardEntry(gameDuration);
            gamePlace = this.getTimePosition(gameDuration) + 1;

            String niceDurationText = ordinal(this.getTimePosition(gameDuration) + 1);
            Toast.makeText(getApplicationContext(), "You got " + niceDurationText + " place", Toast.LENGTH_LONG).show();
        }

        List<Hashtable> leaderboard = this.getSortedLeaderboard();
        int numEntries = leaderboard.size() > 5 ? 5 : leaderboard.size();

        LinearLayout layout = (LinearLayout) findViewById(R.id.leaderboardContainer);

        for (int i = 0; i < numEntries; i++) {
            Hashtable entry = leaderboard.get(i);

            int place = (i + 1);

            String entryText = getLeaderboardEntryText(entry, place);

            this.renderLeaderboardEntry(layout, entryText, place == gamePlace);
        }

        if (gameDuration != 0.0 && gamePlace > 5) {
            Hashtable entry = getDbEntry(id);

            if (entry != null) {
                String entryText = getLeaderboardEntryText(entry, gamePlace);

                this.renderLeaderboardEntry(layout, entryText, true);
            }
        }
    }

    private void renderLeaderboardEntry(LinearLayout layout, String text, boolean special) {
        TextView textView = new TextView(this);

        textView.setText(text);

        textView = styleLeaderboardEntry(textView, special);

        layout.addView(textView);
    }

    private TextView styleLeaderboardEntry(TextView textView, boolean special) {
        textView.setHeight(this.spToPx(35));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));

        if (special) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDarkest));
            textView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLighter));
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, this.spToPx(5), 0, 0);

        textView.setLayoutParams(layoutParams);

        return textView;
    }

    private String getLeaderboardEntryText(Hashtable entry, int gamePlace) {
        double gameDuration = (double) entry.get("duration");
        String date = (String) entry.get("date");
        String gameTime = String.format(Locale.UK, "%.3f", gameDuration);

        String entryText = gamePlace + ". " + date + " - " + gameTime + "s";

        return entryText;
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

    private String saveLeaderboardEntry(double gameDuration) {
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

        return this.getIdFromEntry(dbEntry);
    }

    private List<Hashtable> getLeaderboard() {
        List<Hashtable> lines = null;

        try {
            String location = this.getStorageLocation() + "/leaderboard.db";
            InputStream inStream = new FileInputStream(location);
            InputStreamReader inReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inReader);

            lines = this.parseDbEntries(buffReader);

            return lines;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private List<Hashtable> getSortedLeaderboard() {
        List<Hashtable> unsortedLeaderboard = this.getLeaderboard();

        return this.sortLeaderboard(unsortedLeaderboard);
    }

    private List<Hashtable> sortLeaderboard(List<Hashtable> leaderboard) {
        Collections.sort(leaderboard, new Comparator<Hashtable>() {
            @Override
            public int compare(Hashtable lhs, Hashtable rhs) {
                Double l = (double) lhs.get("duration");
                Double r = (double) rhs.get("duration");

                if (l < r)
                    return -1;
                if (l > r)
                    return 1;

                return 0;
            }
        });

        return leaderboard;
    }

    private String getIdFromEntry(String dbEntry) {
        String[] list = dbEntry.split("\\|");

        return list[0];
    }

    private Hashtable parseDbEntry(String line) {
        Hashtable entry = new Hashtable();

        /*
        * parts[0] -> ID
        * parts[1] -> date
        * parts[2] -> game duration
        */
        String[] parts = line.split("\\|");

        // Fancy up the date
        parts[1] = parts[1].replace(";", " ");

        entry.put("id", parts[0]);
        entry.put("date", parts[1]);
        entry.put("duration", Double.parseDouble(parts[2]));

        return entry;
    }

    private List<Hashtable> parseDbEntries(BufferedReader buffReader) {
        String line = "";

        List<Hashtable> lines = new ArrayList<>();

        do {
            try {
                line = buffReader.readLine();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                continue;
            }
            if (line != null) {
                Hashtable entry = this.parseDbEntry(line);
                lines.add(entry);
            }

        } while (line != null);

        if (lines.get(lines.size() - 1) == null)
            lines.remove(lines.size() - 1);

        return lines;
    }

    private Hashtable getDbEntry(String id) {
        List<Hashtable> gamesList = this.getLeaderboard();

        if (gamesList == null)
            return null;

        for (int i = 0; i < gamesList.size(); i++) {
            Hashtable entry = gamesList.get(i);
            String entryID = entry.get("id").toString();

            if (id.equals(entryID))
                return entry;
        }

        return null;
    }

    private int getTimePosition(double gameTime) {
        List<Double> gamesList = this.getTimePositionList();

        for (int i = 0; i < gamesList.size(); i++) {
            Double entryGameTime = gamesList.get(i);
            if (Math.min(entryGameTime, gameTime) == gameTime)
                return i;
        }

        return gamesList.size() - 1;
    }

    private List<Double> getTimePositionList() {
        List<Double> gameTimes = new ArrayList<>();
        List<Hashtable> gamesList = this.getLeaderboard();
        for (int i = 0; i < gamesList.size(); i++) {
            Hashtable entry = gamesList.get(i);
            double entryGameTime = (double) entry.get("duration");

            gameTimes.add(entryGameTime);
        }
        Collections.sort(gameTimes);
        return gameTimes;
    }

    private String generateDbEntry(Double gameDuration) {
        String date = this.getDateTime();
        String id = UUID.randomUUID().toString();

        return (getString(R.string.leaderboard_db_entry, id, date, gameDuration) + "\n");
    }

    private String getDateTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss", Locale.UK);
        Date now = new Date();

        return sdfDate.format(now);
    }

    public void goBack(View view) {
        Intent myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
        finish();
    }

    public String getAndroidID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }
}
