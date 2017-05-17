package com.wynfa.remind;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        FloatingActionButton added_button = (FloatingActionButton) findViewById(R.id.added_button);
        added_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

        // disallow editing of Date and Time text
        EditText date_text = (EditText) findViewById(R.id.date_text);
        final EditText time_text = (EditText) findViewById(R.id.time_text);
        time_text.setInputType(InputType.TYPE_NULL);
        time_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                Calendar sched = Calendar.getInstance();
            final TimePickerDialog time_picker = new TimePickerDialog(time_text.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    time_text.setText(hourOfDay+":"+minute);
                }
            }, sched.get(Calendar.HOUR_OF_DAY), sched.get(Calendar.MINUTE), false);
            time_picker.show();}
        }
        });
    }
}
