package com.wynfa.remind;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wynfa.remind.db.Reminder;
import com.wynfa.remind.db.ReminderAdapter;
import com.wynfa.remind.db.ReminderDBHelper;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ReminderDBHelper dbhelper = ReminderDBHelper.getInstance(getApplicationContext());
        ArrayList<Reminder> reminderList = dbhelper.getAllReminders();
        dbhelper.close();

        ReminderAdapter adapter = new ReminderAdapter(this,reminderList);

        ListView listView = (ListView) findViewById(R.id.reminder_list);
        listView.setAdapter(adapter);

        FloatingActionButton add_button = (FloatingActionButton) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddReminder.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
