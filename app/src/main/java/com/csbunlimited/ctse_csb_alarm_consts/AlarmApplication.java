package com.csbunlimited.ctse_csb_alarm_consts;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AlarmApplication extends Application {

    public static final String ALARM_ID = "ALARM_ID";
//    public static final String RECEIVER_REQUEST_CODE = "RECEIVER_REQUEST_CODE";

    public static final String CHANNEL_1_RING_ALARM = "RING_ALARM";
    public static final String CHANNEL_2_OTHER = "OTHER";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ringAlarmNotificationChannel = new NotificationChannel(
                    CHANNEL_1_RING_ALARM,
                    "Notify Alarm",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ringAlarmNotificationChannel.setDescription("This channel is for notify alarms to the user");

            NotificationChannel otherNotificationChannel = new NotificationChannel(
                    CHANNEL_2_OTHER,
                    "Other",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            otherNotificationChannel.setDescription("This channel is for show other notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ringAlarmNotificationChannel);
            manager.createNotificationChannel(otherNotificationChannel);
        }
    }
}
