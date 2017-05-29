package com.wynfa.remind.db;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wynfa.remind.Alarm;
import com.wynfa.remind.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

        // Displaying the label and time.
        TextView label = (TextView) convertView.findViewById(R.id.label);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        label.setText(reminder.getLabel());
        final String hour_text, am_pm_text;
        String minute_text = String.format(Locale.getDefault(),"%02d",reminder.getMinute());
        if (reminder.hour < 13) {
            if (reminder.hour == 0) {
                hour_text = "12";
            }
            else {
                hour_text = String.format(Locale.getDefault(),"%02d", reminder.getHour());
            }
            am_pm_text = "AM";
        }
        else {
            hour_text = String.format(Locale.getDefault(),"%02d", reminder.getHour()-12);
            am_pm_text = "PM";
        }
        final String time_text = hour_text+":"+minute_text+" "+am_pm_text;
        time.setText(time_text);

        final Alarm alarm = new Alarm();
        final Calendar schedule = Calendar.getInstance();
        schedule.set(Calendar.HOUR_OF_DAY,reminder.getHour());
        schedule.set(Calendar.MINUTE,reminder.getMinute());
        schedule.set(Calendar.SECOND,0);

        long alarm_time = schedule.getTimeInMillis();
        if (System.currentTimeMillis() > alarm_time) {
            alarm_time += 1000*60*60*24;
        }
        final AlarmManager am = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getContext(), Alarm.class);
        i.setAction("remind");
        i.putExtra("label",reminder.getLabel());
        final PendingIntent pi = PendingIntent.getBroadcast(getContext(),reminder.getId(), i, 0);

        // Create switch that controls when a reminder is active/inactive.
        Switch notification_switch = (Switch) convertView.findViewById(R.id.notification_switch);
        notification_switch.setChecked(reminder.isState());
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ReminderDBHelper db_helper = ReminderDBHelper.getInstance(getContext());
                if (isChecked) {
                    reminder.setState(true);
                    am.set(AlarmManager.RTC_WAKEUP,schedule.getTimeInMillis(),pi);
                    Toast.makeText(getContext(),getContext().getResources().getString(R.string.active_reminder_message),Toast.LENGTH_SHORT).show();
                }
                else {
                    reminder.setState(false);
                    am.cancel(pi);
                    pi.cancel();
                }
                db_helper.updateReminder(reminder);
            }
        });
        return convertView;
    }
}
