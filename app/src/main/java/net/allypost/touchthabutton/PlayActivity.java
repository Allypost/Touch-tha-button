package net.allypost.touchthabutton;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private int buttonsClicked = 1;
    private long startTime;
    private long endTime = 0;
    private static String TIME_KEY = "net.allypost.touchthabutton.LeaderboardActivity.gameTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public int getButtonsClicked() {
        return this.buttonsClicked;
    }

    public static String getTimeKey() {
        return TIME_KEY;
    }

    public int setButtonsClicked(Integer i) {
        this.buttonsClicked = i;
        return this.buttonsClicked;
    }

    public int incrementButtonsClicked() {
        return ++this.buttonsClicked;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long time) {
        this.endTime = time;
    }

    public long calculateEndTime() {
        Long currentTime = this.getEndTime();

        if (this.endTime == 0) {
            currentTime = System.nanoTime();
            this.endTime = currentTime;
        }

        return currentTime;
    }

    public void startGame(View view) {
        Button button = (Button) view;

        if (this.getButtonsClicked() == 1) {
            this.initGame();
        }

        if (this.getButtonsClicked() == 10) {
            this.finishGame();
            return;
        }

        this.moveButtonRandomly(button);

        int count = this.incrementButtonsClicked();

        int remaining = 11 - count;

        button.setText("" + remaining);
    }

    private void moveButtonRandomly(Button button) {
        Random r = new Random();

        Point size = this.getDisplaySize();

        int width = size.x;
        int height = size.y;

        Integer x = r.nextInt(width * 4 / 5 - button.getWidth());
        Integer y = r.nextInt(height * 4 / 5 - button.getHeight());

        button.setX(x);
        button.setY(y);
    }

    private Point getDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    private void initGame() {
        this.setStartTime(System.nanoTime());
        Toast.makeText(getApplicationContext(), "START GAME", Toast.LENGTH_SHORT).show();
    }

    private void finishGame() {
        double gameDuration = this.calculateGameTime();

        Toast.makeText(getApplicationContext(), "GAME LASTED " + (Math.round(gameDuration * 100) / 100.0) + "s", Toast.LENGTH_LONG).show();

        this.transferToLeaderboard();
    }

    private double calculateGameTime() {
        long currentTime = this.calculateEndTime();
        long startTime = this.getStartTime();

        double gameDuration = (double) (currentTime - startTime) / 1000000000.0;

        return gameDuration;
    }

    private void transferToLeaderboard() {
        double gameDuration = this.calculateGameTime();

        Intent myIntent = new Intent(this, LeaderboardActivity.class);

        String intentDataKey = this.getTimeKey();

        myIntent.putExtra(intentDataKey, gameDuration); //Optional parameters

        startActivity(myIntent);
        finish();
    }

}
