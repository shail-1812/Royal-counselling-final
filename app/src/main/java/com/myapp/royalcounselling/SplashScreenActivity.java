package com.myapp.royalcounselling;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    String email, password, name;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent i = getIntent();
        try {
            String data = i.getExtras().getString("activityToDirect");
//            Toast.makeText(this, "Data " + data, Toast.LENGTH_SHORT).show();
            Intent n = new Intent(this, NavigationDrawerActivity.class);
            n.putExtra("activityToDirect", data);
            startActivity(n);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
        name = sharedPreferences.getString("KEY_NAME", "");
        email = sharedPreferences.getString("KEY_EMAIL", "");
        password = sharedPreferences.getString("KEY_PASSWORD", "");
        int time = 3000;
        if (getIntent().hasExtra("click_action")) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            Intent j = new Intent(this, AlertDetails.class);
            j.putExtras(getIntent().getExtras());
            startActivity(j);
            finish();
            return;
        }
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            Intent intent;
            if (password.equals("") && email.equals("")) {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, NavigationDrawerActivity.class);
            }
            startActivity(intent);
            finish();
        }, time);
    }
}