package net.allypost.touchthabutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void showLeaderboard(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    public void exitApp(View view) {
        finish();
        System.exit(0);
    }
}
