package com.csbunlimited.ctse_csb_alarm_broadcastReceivers;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.csbunlimited.ctse_csb_alarm.NewAlarmActivity;
import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_alarm_services.NotificationManagerService;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

//        Intent service_intent = new Intent(context, NewAlarmActivity.class);
////        service_intent.putExtra("extra", get_your_string);
//        context.startService(service_intent);

        int alarmId = intent.getIntExtra(AlarmApplication.ALARM_ID, 0);

        if (alarmId > 0) {
            AlarmDBHandler alarmDBHandler = new AlarmDBHandler(context.getApplicationContext());
            Alarm alarm = alarmDBHandler.getAlarmById(alarmId);

            if (alarm != null) {
                NotificationManagerService notificationManagerService = new NotificationManagerService(context);
                notificationManagerService.sendRingAlarmNofication(alarm);

                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(context, alarm.getRingtoneUri());

                    final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

                        int loopCount = 0, maxLoopCount = 5;
                        do {
                            int audioSessionId = audioManager.generateAudioSessionId();
                            mediaPlayer.setAudioSessionId(audioSessionId);
                            loopCount++;
                        } while (mediaPlayer.getAudioSessionId() == AudioManager.ERROR && loopCount <= maxLoopCount);

                        if (mediaPlayer.getAudioSessionId() != AudioManager.ERROR) {
                            mediaPlayer.setLooping(true);
//                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }
                    }

                    alarm.setAudioSessionId(mediaPlayer.getAudioSessionId());
                    alarmDBHandler.updateAlarm(alarm);
                }
                catch (Exception ex) {
                    System.out.print(ex.toString());
                }
            }

        }
    }
}
