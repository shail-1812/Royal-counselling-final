package com.myapp.royalcounselling;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int count = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {

            Log.e("TAG", remoteMessage.getMessageId());
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("click_action"));
            String message = remoteMessage.getData().get("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        getSharedPreferences("MYAPP", MODE_PRIVATE).edit().putString("TOKEN", s).apply();

    }

    private void sendNotification(String title, String messageBody, String activity) {

        Intent intent = new Intent(this, AlertDetails.class);
        if (activity.equals("pptRequest")) {
            intent.putExtra("activityToDirect", "PPTRequest");
        } else if (activity.equals("registerSeminar")) {
            intent.putExtra("activityToDirect", "SeminarRegistered");
        } else if (activity.equals("counsellingRequest")) {
            intent.putExtra("activityToDirect", "CounsellingRequested");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
            mChannel.setDescription(messageBody);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mChannel.setSound(defaultSoundUri, att);

            mNotifyManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
        mBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#FFD600"))
                .setContentIntent(pendingIntent)
                .setChannelId("Sesame")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody)).setTimeoutAfter(300000);

        mNotifyManager.notify(count, mBuilder.build());
        count++;
    }

}
