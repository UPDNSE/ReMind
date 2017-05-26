package com.wynfa.remind.db;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wynfa.remind.AlarmReceiver;
import com.wynfa.remind.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by RafaelLuis on 5/23/2017.
 */

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        super(context, 0, reminders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Reminder reminder = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reminder, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.label);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        label.setText(reminder.label);
        final String hour_text, am_pm_text;
        String minute_text = String.format("%02d",reminder.minute);
        if (reminder.hour < 13) {
            if (reminder.hour == 0) {
                hour_text = "12";
            }
            else {
                hour_text = String.format("%02d", reminder.hour);
            }
            am_pm_text = "AM";
        }
        else {
            hour_text = String.format("%02d", reminder.hour-12);
            am_pm_text = "PM";
        }
        final String time_text = hour_text+":"+minute_text+" "+am_pm_text;
        time.setText(time_text);

        Switch notif_switch = (Switch) convertView.findViewById(R.id.notif_switch);
        notif_switch.setChecked(reminder.state);
        notif_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Notification.Builder builder = new Notification.Builder(getContext());
                builder.setContentTitle(reminder.label);
                builder.setContentText("It's "+time_text+". Check out your reminders!");
                builder.setSmallIcon(R.drawable.ic_check_white_24px);
                Notification notification = builder.build();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, reminder.hour);
                calendar.set(Calendar.MINUTE, reminder.minute);
                calendar.set(Calendar.SECOND, 0);

                AlarmManager alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(),AlarmReceiver.class);
                intent.putExtra(AlarmReceiver.NOTIFICATION, notification);
                PendingIntent alarmIntent = PendingIntent.getService(getContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

                ReminderDBHelper dbhelper = ReminderDBHelper.getInstance(getContext());

                if (isChecked) {
                    reminder.state = true;
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);
                    Toast notif_on = Toast.makeText(getContext(),"Reminder is now active!",Toast.LENGTH_SHORT);
                    notif_on.show();
                }
                else {
                    reminder.state = false;
                    alarmMgr.cancel(alarmIntent);
                }
                dbhelper.updateReminder(reminder);
            }
        });

        return convertView;
    }
}
