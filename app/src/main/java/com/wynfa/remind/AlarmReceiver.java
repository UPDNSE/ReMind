package com.wynfa.remind;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wynfa.remind.db.Reminder;
import com.wynfa.remind.db.ReminderDBHelper;

import java.util.ArrayList;

/**
 * Created by RafaelLuis on 5/24/2017.
 */

public class AlarmReceiver extends IntentService {

    public final static String NOTIFICATION = "notification";

    public AlarmReceiver(){
        super("AlarmReceiver");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(0,notification);


    }
}