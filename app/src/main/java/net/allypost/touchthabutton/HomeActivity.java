package net.allypost.touchthabutton;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String android_id = this.getAndroidID();

        System.out.println("DEVICE-ID:\t" + android_id);
    }

    public void playGame(View view) {
        Intent myIntent = new Intent(this, PlayActivity.class);
//        myIntent.putExtra("key", "PASTA"); //Optional parameters
        startActivity(myIntent);
        finish();
    }

    public void showLeaderboard(View view) {
        Toast.makeText(getApplicationContext(), "SHOW LEADERBOARD", Toast.LENGTH_SHORT).show();
    }

    public void exitApp(View view) {
        finish();
        System.exit(0);
    }

    public String getAndroidID() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
