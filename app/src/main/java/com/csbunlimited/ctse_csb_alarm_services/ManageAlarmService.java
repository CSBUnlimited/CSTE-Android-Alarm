package com.csbunlimited.ctse_csb_alarm_services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.csbunlimited.ctse_csb_alarm.MainActivity;
import com.csbunlimited.ctse_csb_alarm_broadcastReceivers.AlarmBroadcastReceiver;
import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;

import java.util.Date;

public class ManageAlarmService {

    private static ManageAlarmService _manageAlarmService;

    private MainActivity _mainActivity;
    private Context _mainActivityContext;

    private ManageAlarmService(MainActivity mainActivity) {
        _mainActivity = mainActivity;
        _mainActivityContext = _mainActivity;
    }

    public static boolean isInitialized() {
        return (_manageAlarmService != null);
    }

    public static void initializeManageAlarmServiceInstance(MainActivity mainActivity) {
        _manageAlarmService = new ManageAlarmService(mainActivity);
    }

    public static ManageAlarmService getManageAlarmServiceInstance() {
        return _manageAlarmService;
    }

    public static ManageAlarmService getManageAlarmServiceInstance(MainActivity mainActivity) throws Exception {
        initializeManageAlarmServiceInstance(mainActivity);
        return getManageAlarmServiceInstance();
    }

    public void registerAlarm(int alarmId) {
        AlarmManager alarmManager = (AlarmManager) _mainActivity.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(_mainActivityContext, AlarmBroadcastReceiver.class);
        intent.putExtra(AlarmApplication.ALARM_ID, alarmId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(_mainActivityContext, alarmId, intent, 0);

        Date d = new Date();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, d.getTime() + 1000 * 30, pendingIntent);
    }

    public void cancelAlarm(int alarmId) {
        AlarmManager alarmManager = (AlarmManager) _mainActivity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(_mainActivityContext, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_mainActivityContext, alarmId, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public void stopAlarm(int alarmId, int audioSessionId) {
        cancelAlarm(alarmId);

        try {
//            AudioManager audioManager = (AudioManager) _mainActivityContext.getSystemService(Context.AUDIO_SERVICE);

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioSessionId(audioSessionId);

            if (mediaPlayer.getAudioSessionId() == AudioManager.ERROR || mediaPlayer.getAudioSessionId() <= 0) {
                return;
            }

            mediaPlayer.release();
        }
        catch (Exception ex) { }
    }
}
