package com.wynfa.remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wynfa.remind.db.ReminderDBHelper;

public class ViewReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        final Intent intent = getIntent();
        final String time_text = intent.getStringExtra("time_text");
        final String label = intent.getStringExtra("label");

        TextView reminder_name = (TextView) findViewById(R.id.show_reminder_name);
        reminder_name.setText(label);
        TextView time = (TextView) findViewById(R.id.show_time);
        time.setText(time_text);

        final ReminderDBHelper db_helper = ReminderDBHelper.getInstance(getApplicationContext());
        final int reminder_id = intent.getIntExtra("id",0);

        final AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), Alarm.class);
        i.setAction("remind");
        i.putExtra("label",label);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),reminder_id, i, 0);

        FloatingActionButton delete_button = (FloatingActionButton) findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReminder.this);
                builder.setTitle(label);
                builder.setMessage("Delete this reminder?");
                builder.setIcon(R.drawable.ic_delete_black_24dp);
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        db_helper.deleteReminder(reminder_id);
                        db_helper.close();
                        am.cancel(pi);
                        pi.cancel();

                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
