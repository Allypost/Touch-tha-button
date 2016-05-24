package net.allypost.touchthabutton;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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

            Double gameDuration = (double) Math.round((currentTime - startTime) / 10000000.0) / 100.0;

            this.setButtonsClicked(1);

            Toast.makeText(getApplicationContext(), "GAME LASTED " + gameDuration.toString() + "s", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(PlayActivity.this, HomeActivity.class);
            myIntent.putExtra("time", gameDuration); //Optional parameters
            PlayActivity.this.startActivity(myIntent);
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

        int remaining = 10 - count + 1;

        button.setText("" + remaining);


        System.out.println("\n\n" +
                "\nX:\t" + x +
                "\nY:\t" + y +
                "\nDIM:\t" + width + "x" + height +
                "\nCNT:\t" + count +
                "\n\n\n");
    }

    private float dpToPx(Integer dp) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public Button addButton() {
        Float buttonWidth = this.dpToPx(85);
        Float buttonHeight = this.dpToPx(55);
        Integer buttonNumber = this.incrementButtonsClicked();

        Button newButton = new Button(this);
        newButton.setText("Button " + buttonNumber);
        newButton.setId(4000);
        newButton.setWidth(Math.round(buttonWidth));
        newButton.setHeight(Math.round(buttonHeight));
        newButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        newButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        newButton.setOnClickListener(new HandleClick());
        return newButton;
    }

    private class HandleClick implements View.OnClickListener {

        public void onClick(View view) {
            /*Button thisButton = (Button) view;

            thisButton.setVisibility(View.INVISIBLE);

            LinearLayout ll = (LinearLayout) findViewById(R.id.gamePlane);

            Button newButton = this.addButton();

            ll.addView(newButton);*/

            Toast.makeText(getApplicationContext(), "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
        }
    }

}
