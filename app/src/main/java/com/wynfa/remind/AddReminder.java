package com.wynfa.remind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wynfa.remind.db.Reminder;
import com.wynfa.remind.db.ReminderDBHelper;

import java.util.Calendar;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Button to confirm adding the reminder.
        FloatingActionButton added_button = (FloatingActionButton) findViewById(R.id.added_button);
        added_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the current time as now.
                Calendar now = Calendar.getInstance();

                // Get the time and label inputted by the user.
                TimePicker reminder_time_picker = (TimePicker) findViewById(R.id.reminder_time_picker);
                EditText reminder_label_picker = (EditText) findViewById(R.id.reminder_label_picker);

                // Error if user did not input a label.
                if (reminder_label_picker.getText().toString().isEmpty()) {
                    Toast label_error = Toast.makeText(getApplicationContext(),getResources().getString(R.string.label_error_message),Toast.LENGTH_SHORT);
                    label_error.show();
                }

                // Save a new reminder to the database and return to the MainActivity.
                else {
                    // Create a new Reminder instance with the current time and label input.
                    Reminder new_reminder = new Reminder();
                    new_reminder.setHour(reminder_time_picker.getHour());
                    new_reminder.setMinute(reminder_time_picker.getMinute());
                    new_reminder.setLabel(reminder_label_picker.getText().toString());
                    new_reminder.setState(false);

                    // Add the Reminder instance to the database and return to the MainActivity.
                    ReminderDBHelper db_helper = ReminderDBHelper.getInstance(getApplicationContext());
                    db_helper.addReminder(new_reminder);
                    db_helper.close();
                    startActivity(new Intent(view.getContext(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Correct focus when touching the screen outside of the Reminder label picker.
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
