package com.csbunlimited.ctse_csb_alarm_consts;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.csbunlimited.ctse_csb_alarm.MainActivity;
import com.csbunlimited.ctse_csb_alarm.R;
import com.csbunlimited.ctse_csb_alarm_models.Question;

import java.util.List;

public class AlarmApplication extends Application {

    public static final String ALARM_ID = "ALARM_ID";
//    public static final String RECEIVER_REQUEST_CODE = "RECEIVER_REQUEST_CODE";

    public static final String CHANNEL_1_RING_ALARM = "RING_ALARM";
    public static final String CHANNEL_2_OTHER = "OTHER";

    private static MainActivity _mainActivity;

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
            ringAlarmNotificationChannel.enableLights(true);
            ringAlarmNotificationChannel.setLightColor(R.color.colorMajor);
            ringAlarmNotificationChannel.enableVibration(true);
            ringAlarmNotificationChannel.setVibrationPattern(new long[] { 1000, 500, 1000, 650, 1000, 800 });
            ringAlarmNotificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

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

    public static MainActivity getMainActivity() {
        return _mainActivity;
    }

    public static void setMainActivity(MainActivity _mainActivity) {
        _mainActivity = _mainActivity;
    }
}
