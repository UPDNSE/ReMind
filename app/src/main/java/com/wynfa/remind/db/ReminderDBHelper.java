package com.wynfa.remind.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "remindersDatabase";
    private static final int DATABASE_VERSION = 2;

    private ReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TABLE_REMINDERS = "reminders";

    private static final String KEY_REMINDER_ID = "id";
    private static final String KEY_REMINDER_LABEL = "label";
    private static final String KEY_REMINDER_HOUR = "hour";
    private static final String KEY_REMINDER_MINUTE = "minute";
    private static final String KEY_REMINDER_STATE = "state";


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDER_TABLE = "CREATE TABLE " + TABLE_REMINDERS +
                "(" +
                KEY_REMINDER_ID + " INTEGER PRIMARY KEY," +
                KEY_REMINDER_HOUR + " INTEGER," +
                KEY_REMINDER_MINUTE + " INTEGER," +
                KEY_REMINDER_LABEL + " TEXT," +
                KEY_REMINDER_STATE + " BOOLEAN" +
                ")";

        db.execSQL(CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
            onCreate(db);
        }
    }

    private static ReminderDBHelper sInstance;

    public static synchronized ReminderDBHelper getInstance(Context context) {
        if (sInstance == null) sInstance = new ReminderDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public void addReminder (Reminder reminder) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_REMINDER_HOUR, reminder.hour);
            values.put(KEY_REMINDER_MINUTE, reminder.minute);
            values.put(KEY_REMINDER_LABEL, reminder.label);
            values.put(KEY_REMINDER_STATE, Boolean.FALSE);

            db.insertOrThrow(TABLE_REMINDERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_REMINDERS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Reminder newReminder = new Reminder();
                    newReminder.id = cursor.getInt(cursor.getColumnIndex(KEY_REMINDER_ID));
                    newReminder.hour = cursor.getInt(cursor.getColumnIndex(KEY_REMINDER_HOUR));
                    newReminder.minute = cursor.getInt(cursor.getColumnIndex(KEY_REMINDER_MINUTE));
                    newReminder.label = cursor.getString(cursor.getColumnIndex(KEY_REMINDER_LABEL));
                    newReminder.state = cursor.getInt(cursor.getColumnIndex(KEY_REMINDER_STATE))>0;

                    reminders.add(newReminder);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return reminders;
    }

    public int updateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_HOUR, reminder.hour);
        values.put(KEY_REMINDER_MINUTE, reminder.minute);
        values.put(KEY_REMINDER_LABEL,reminder.label);
        values.put(KEY_REMINDER_STATE,reminder.state);


        return db.update(TABLE_REMINDERS, values, KEY_REMINDER_ID + " =?",
                new String[] { String.valueOf(reminder.id) });
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_REMINDERS, KEY_REMINDER_ID + " = ?",
                    new String[]{String.valueOf(reminder.id)});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}
