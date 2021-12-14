package com.myapp.royalcounselling;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDetails extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            String data = i.getExtras().getString("activityToDirect");
            Intent n = new Intent(this, NavigationDrawerActivity.class);
            n.putExtra("activityToDirect", data);
            startActivity(n);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
