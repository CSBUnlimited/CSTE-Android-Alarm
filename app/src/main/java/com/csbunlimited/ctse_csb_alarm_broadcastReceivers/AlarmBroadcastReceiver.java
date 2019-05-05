package com.csbunlimited.ctse_csb_alarm_broadcastReceivers;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
                notificationManagerService.sendRingAlarmNofication(((alarm.getDate().getHours() < 10) ? "0" : "") + alarm.getDate().getHours()
                        + " : " + ((alarm.getDate().getMinutes() < 10) ? "0" : "") + alarm.getDate().getMinutes(), alarm.getName());
            }

        }
    }
}
