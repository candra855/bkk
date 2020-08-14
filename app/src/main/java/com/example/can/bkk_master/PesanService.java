package com.example.can.bkk_master;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PesanService extends FirebaseMessagingService {

    private static final String TAG ="Test Test";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null)
        {
            sendNotif(remoteMessage);
        }
    }

    private void sendNotif(RemoteMessage remoteMessage)
    {
        Map<String ,String > data = remoteMessage.getData();

        String title = data.get("title");
        String content = data.get("message");
//        PendingIntent intent = data.get("intent");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification);
        contentView.setTextViewText(R.id.content_title, title);
        contentView.setTextViewText(R.id.content_text, content);
        String NOTIFICATION_CHANNEL_ID = "id1";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My notofications", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription("deskripsi");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
//            notificationChannel.enableVibration(true);
//            notificationChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_CHANNEL_ID);
        notBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(content)
//                .setCustomContentView(contentView)
                .setSmallIcon(R.mipmap.ic_logo)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setStyle(new android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomBigContentView(contentView)

                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentText(content)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .setColor(getResources().getColor(R.color.colorPrimaryDark));
        notificationManager.notify(1,notBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "token:"+ token);

    }
}

