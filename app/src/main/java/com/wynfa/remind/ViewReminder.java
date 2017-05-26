package com.wynfa.remind;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wynfa.remind.db.Reminder;
import com.wynfa.remind.db.ReminderDBHelper;

import java.util.Calendar;

public class ViewReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        TimePicker reminder_timeedit = (TimePicker) findViewById(R.id.reminder_timeedit);


        FloatingActionButton edited_button = (FloatingActionButton) findViewById(R.id.edited_button);
        edited_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lots of things go here
                Calendar now = Calendar.getInstance();
                TimePicker reminder_timepicker = (TimePicker) findViewById(R.id.reminder_timepicker);
                EditText reminder_labelpicker = (EditText) findViewById(R.id.reminder_labelpicker);

                if (reminder_labelpicker.getText().toString().isEmpty()) {
                    Toast label_error = Toast.makeText(getApplicationContext(),"Your reminder has no label!",Toast.LENGTH_SHORT);
                    label_error.show();
                }
                else {
                    Reminder new_reminder = new Reminder();
                    new_reminder.hour = reminder_timepicker.getHour();
                    new_reminder.minute = reminder_timepicker.getMinute();
                    new_reminder.label = reminder_labelpicker.getText().toString();

                    ReminderDBHelper dbhelper = ReminderDBHelper.getInstance(getApplicationContext());
                    dbhelper.addReminder(new_reminder);
                    startActivity(new Intent(view.getContext(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
