package com.wynfa.remind;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String label = intent.getStringExtra("label");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.reminder_notif)    //set the small icon
                        .setContentTitle(label)    //set title of the notification
                        .setContentText(context.getResources().getString(R.string.notif_subtext))     //set the contents
                        .setAutoCancel(true);      //destroys the notification when clicked

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Add a notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);


    }
}
