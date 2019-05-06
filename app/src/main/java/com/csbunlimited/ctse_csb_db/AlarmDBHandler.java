package com.csbunlimited.ctse_csb_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.csbunlimited.ctse_csb_alarm_models.Alarm;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class AlarmDBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME = "CSB_ALARM";

    // Alarms table name
    private static final String TABLE_ALARMS = "Alarms";

    // Alarms Table Columns names
    private static final String TABLE_ALARMS_COLUMN_ID = "Id";
    private static final String TABLE_ALARMS_COLUMN_NAME = "Name";
    private static final String TABLE_ALARMS_COLUMN_ISACTIVE = "IsActive";
    private static final String TABLE_ALARMS_COLUMN_DATE = "Date";
    private static final String TABLE_ALARMS_COLUMN_RINGTONEURI = "RingtoneUri";
    private static final String TABLE_ALARMS_COLUMN_AUDIOSESSIONID = "AudioSessionId";

    public AlarmDBHandler(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAlarmsTable = "CREATE TABLE `" + TABLE_ALARMS + "` ( " +
                "`" + TABLE_ALARMS_COLUMN_ID + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`" + TABLE_ALARMS_COLUMN_NAME + "` TEXT, " +
                "`" + TABLE_ALARMS_COLUMN_ISACTIVE + "` INTEGER, " +
                "`" + TABLE_ALARMS_COLUMN_DATE + "` NUMERIC, " +
                "`" + TABLE_ALARMS_COLUMN_RINGTONEURI + "` TEXT, " +
                "`" + TABLE_ALARMS_COLUMN_AUDIOSESSIONID + "` INTEGER " +
                ");";

        db.execSQL(createAlarmsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE `" + TABLE_ALARMS + "`";
        db.execSQL(query);

        onCreate(db);

        query ="INSERT INTO `Alarms`(`Name`,`IsActive`,`Date`,`RingtoneUri`,`AudioSessionId`) " +
                "VALUES ('Test','1','1556714707000 ','', -500);";
        db.execSQL(query);
    }

    // Alarms Table
    public int getNextAlarmId() {
        int nextId = 0;
        String tableName;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("sqlite_sequence", new String[]{ "name", "seq" }, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                tableName = cursor.getString(0);

                if (tableName.equals(TABLE_ALARMS)) {
                    nextId = Integer.parseInt(cursor.getString(1));
                    break;
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return ++nextId;
    }

    // Get all alarms
    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<Alarm>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS,
                new String[]{ TABLE_ALARMS_COLUMN_ID, TABLE_ALARMS_COLUMN_NAME, TABLE_ALARMS_COLUMN_ISACTIVE, TABLE_ALARMS_COLUMN_DATE, TABLE_ALARMS_COLUMN_RINGTONEURI, TABLE_ALARMS_COLUMN_AUDIOSESSIONID },
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();

                alarm.setId(Integer.parseInt(cursor.getString(0)));
                alarm.setName(cursor.getString(1));
                alarm.setIsActive(Integer.parseInt(cursor.getString(2)) == 1);
                alarm.setDate(new Date(Long.parseLong(cursor.getString(3))));
                alarm.setRingtoneUri(cursor.getString(4) == "" ? (Uri) null : Uri.parse(cursor.getString(4)));
                alarm.setAudioSessionId(Integer.parseInt(cursor.getString(5)));

                alarms.add(alarm);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return alarms;
    }

    // Get one alarm by Id
    public Alarm getAlarmById(int id) {
        Alarm alarm = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS,
                new String[]{ TABLE_ALARMS_COLUMN_ID, TABLE_ALARMS_COLUMN_NAME, TABLE_ALARMS_COLUMN_ISACTIVE, TABLE_ALARMS_COLUMN_DATE, TABLE_ALARMS_COLUMN_RINGTONEURI, TABLE_ALARMS_COLUMN_AUDIOSESSIONID },
                TABLE_ALARMS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            alarm = new Alarm();

            alarm.setId(Integer.parseInt(cursor.getString(0)));
            alarm.setName(cursor.getString(1));
            alarm.setIsActive(Integer.parseInt(cursor.getString(2)) == 1);
            alarm.setDate(new Date(Long.parseLong(cursor.getString(3))));
            alarm.setRingtoneUri(cursor.getString(4) == "" ? (Uri) null : Uri.parse(cursor.getString(4)));
            alarm.setAudioSessionId(Integer.parseInt(cursor.getString(5)));

            cursor.close();
        }

        db.close();
        return alarm;
    }

    // Add new Alarm
    public int addAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_ALARMS_COLUMN_NAME, alarm.getName());
        values.put(TABLE_ALARMS_COLUMN_ISACTIVE, alarm.getIsActive() ? 1 : 0);
        values.put(TABLE_ALARMS_COLUMN_DATE, alarm.getDate().getTime());
        values.put(TABLE_ALARMS_COLUMN_RINGTONEURI, alarm.getRingtoneUri().toString());
        values.put(TABLE_ALARMS_COLUMN_AUDIOSESSIONID, alarm.getAudioSessionId());

        int alarmId = (int)db.insert(TABLE_ALARMS, null, values);
        db.close();

        return alarmId;
    }

    // Update Alarm
    public boolean updateAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_ALARMS_COLUMN_NAME, alarm.getName());
        values.put(TABLE_ALARMS_COLUMN_ISACTIVE, alarm.getIsActive() ? 1 : 0);
        values.put(TABLE_ALARMS_COLUMN_DATE, alarm.getDate().getTime());
        values.put(TABLE_ALARMS_COLUMN_RINGTONEURI, alarm.getRingtoneUri().toString());
        values.put(TABLE_ALARMS_COLUMN_AUDIOSESSIONID, alarm.getAudioSessionId());

        int effectedRows = db.update(TABLE_ALARMS, values, TABLE_ALARMS_COLUMN_ID + " = ?", new String[]{ String.valueOf(alarm.getId()) });
        db.close();

        return (effectedRows > 0);
    }

    // Deleting a alarm
    public boolean deleteAlarmById(int alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int effectedRows = db.delete(TABLE_ALARMS, TABLE_ALARMS_COLUMN_ID + " = ?", new String[] { String.valueOf(alarmId) });
        db.close();

        return (effectedRows > 0);
    }
}
