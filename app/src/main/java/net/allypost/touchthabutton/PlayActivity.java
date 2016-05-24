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

    public void startGame(View view) {
        Button button = (Button) view;

        if (this.getButtonsClicked() == 1) {
            this.setStartTime(System.nanoTime());
            Toast.makeText(getApplicationContext(), "START GAME", Toast.LENGTH_SHORT).show();
        }

        if (this.getButtonsClicked() == 10) {
            Long currentTime = this.endTime;
            if (this.endTime == 0) {
                currentTime = System.nanoTime();
            }
            Long startTime = this.getStartTime();

            Double gameDuration = (double) (currentTime - startTime) / 1000000000.0;

            this.setButtonsClicked(1);

            Toast.makeText(getApplicationContext(), "GAME LASTED " + (Math.round(gameDuration * 100) / 100.0) + "s", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(this, HomeActivity.class);
            myIntent.putExtra("time", gameDuration); //Optional parameters
            startActivity(myIntent);
            finish();
            return;
        }

        Random r = new Random();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        Integer x = r.nextInt(width * 4 / 5 - button.getWidth());
        Integer y = r.nextInt(height * 4 / 5 - button.getHeight());

        button.setX(x);
        button.setY(y);

        int count = this.incrementButtonsClicked();

        int remaining = 9 - count;

        button.setText("" + remaining);
    }

}
