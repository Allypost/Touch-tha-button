package net.allypost.touchthabutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void playGame(View view) {
        Intent myIntent = new Intent(this, PlayActivity.class);
//        myIntent.putExtra("key", "PASTA"); //Optional parameters
        startActivity(myIntent);
    }

    public void showLeaderboard(View view) {
        Toast.makeText(getApplicationContext(), "SHOW LEADERBOARD", Toast.LENGTH_SHORT).show();
    }

    public void exitApp(View view) {
        finish();
        System.exit(0);
    }
}
