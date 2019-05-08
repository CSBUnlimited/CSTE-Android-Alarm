package com.csbunlimited.ctse_csb_alarm_broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_alarm_services.NotificationManagerService;
import com.csbunlimited.ctse_csb_alarm_services.RingAlarmService;
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

        // create an intent to the ringtone service
        Intent ringAlarmIntent = new Intent(context, RingAlarmService.class);
        ringAlarmIntent.putExtra(AlarmApplication.ALARM_ID, alarmId);
        context.startForegroundService(ringAlarmIntent);
    }
}
