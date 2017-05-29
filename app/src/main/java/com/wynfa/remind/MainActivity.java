package com.wynfa.remind;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wynfa.remind.db.Reminder;
import com.wynfa.remind.db.ReminderAdapter;
import com.wynfa.remind.db.ReminderDBHelper;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get an ArrayList of the reminders from the database.
        ReminderDBHelper db_helper = ReminderDBHelper.getInstance(getApplicationContext());
        ArrayList<Reminder> reminder_list = db_helper.getAllReminders();
        db_helper.close();

        // Set the array list to a ListView using a ReminderAdapter.
        final ReminderAdapter reminder_adapter = new ReminderAdapter(this,reminder_list);
        ListView listView = (ListView) findViewById(R.id.reminder_list);
        listView.setAdapter(reminder_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter,View v, int position,long id){

                Object item = adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),ViewReminder.class);

                TextView time = (TextView) v.findViewById(R.id.time);
                TextView label = (TextView) v.findViewById(R.id.label);
                intent.putExtra("time_text",time.getText());
                intent.putExtra("label",label.getText());

                Reminder reminder = (Reminder) item;
                intent.putExtra("id",reminder.getId());

                startActivity(intent);
            }
        });

        // Add button directs to the AddReminder activity on click.
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Create two options: Help and About.
        int id = item.getItemId();

        if (id == R.id.action_connect) {
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
