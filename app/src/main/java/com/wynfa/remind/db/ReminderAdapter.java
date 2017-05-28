package com.wynfa.remind.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.wynfa.remind.R;

import java.util.ArrayList;
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
            hour_text = String.format(Locale.getDefault(),"%02d", reminder.getHour());
            am_pm_text = "PM";
        }
        final String time_text = hour_text+":"+minute_text+" "+am_pm_text;
        time.setText(time_text);

        // Create switch that controls when a reminder is active/inactive.
        Switch notification_switch = (Switch) convertView.findViewById(R.id.notification_switch);
        notification_switch.setChecked(reminder.state);
        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ReminderDBHelper db_helper = ReminderDBHelper.getInstance(getContext());
                if (isChecked) {
                    reminder.setState(true);
                }
                else {
                    reminder.setState(false);
                }
                db_helper.updateReminder(reminder);
            }
        });
        return convertView;
    }
}
