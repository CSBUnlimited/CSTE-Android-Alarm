package com.csbunlimited.ctse_csb_alarm_services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.csbunlimited.ctse_csb_alarm.MainActivity;
import com.csbunlimited.ctse_csb_alarm.StopAlarmActivity;
import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

public class RingAlarmService extends Service {

    private AlarmDBHandler _alarmDBHandler;
    private MediaPlayer _mediaPlayer;

    //    @androidx.annotation.Nullable
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _alarmDBHandler = new AlarmDBHandler(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);

        int alarmId = intent.getIntExtra(AlarmApplication.ALARM_ID, 0);

        if (alarmId > 0) {

            Alarm alarm = _alarmDBHandler.getAlarmById(alarmId);

            if (alarm != null) {

                Intent ringAlarmIntent = new Intent(this, StopAlarmActivity.class);
                ringAlarmIntent.putExtra(AlarmApplication.ALARM_ID, alarmId);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ringAlarmIntent, 0);

                NotificationManagerService notificationManagerService = new NotificationManagerService(this);
                Notification notification = notificationManagerService.getRingAlarmNofication(alarm, pendingIntent);
//
//                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarm.getRingtoneUri());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    ringtone.setLooping(true);
//                }
//                ringtone.play();


                try {
                    _mediaPlayer = MediaPlayer.create(this, alarm.getRingtoneUri());
//                    mediaPlayer.setDataSource(this, alarm.getRingtoneUri());
                    if (_mediaPlayer.getAudioSessionId() != AudioManager.ERROR) {
                        _mediaPlayer.setLooping(true);
//                            mediaPlayer.prepare();
                        _mediaPlayer.start();
                    }
//                    final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

//                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
//                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//
////                        int loopCount = 0, maxLoopCount = 5;
////                        do {
////                            int audioSessionId = audioManager.generateAudioSessionId();
////                            mediaPlayer.setAudioSessionId(audioSessionId);
////                            loopCount++;
////                        } while (mediaPlayer.getAudioSessionId() == AudioManager.ERROR && loopCount <= maxLoopCount);
//
//                        if (mediaPlayer.getAudioSessionId() != AudioManager.ERROR) {
//                            mediaPlayer.setLooping(true);
////                            mediaPlayer.prepare();
//                            mediaPlayer.start();
//                        }
//                    }

                    alarm.setAudioSessionId(_mediaPlayer.getAudioSessionId());
                    _alarmDBHandler.updateAlarm(alarm);
                }
                catch (Exception ex) {
                    System.out.print(ex.toString());
                }

                startForeground(alarm.getId(), notification);
            }

//            Intent i = new Intent();
//            i.setClass(this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);

        }

//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
