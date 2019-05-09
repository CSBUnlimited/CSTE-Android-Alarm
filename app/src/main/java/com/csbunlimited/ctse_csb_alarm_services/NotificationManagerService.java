package com.csbunlimited.ctse_csb_alarm_services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.csbunlimited.ctse_csb_alarm.MainActivity;
import com.csbunlimited.ctse_csb_alarm.R;
import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;

public class NotificationManagerService {

    private Context _context;
    private NotificationManagerCompat _notificationManagerCompat;

    public NotificationManagerService(Context context) {
        _context = context;
        _notificationManagerCompat = NotificationManagerCompat.from(_context);
    }

    public Notification getRingAlarmNofication(Alarm alarm, PendingIntent pendingIntent) {

        String alarmTimeInString = ((alarm.getDate().getHours() < 10) ? "0" : "") + alarm.getDate().getHours()
                + " : " + ((alarm.getDate().getMinutes() < 10) ? "0" : "") + alarm.getDate().getMinutes();

        Notification notification = new NotificationCompat.Builder(_context, AlarmApplication.CHANNEL_1_RING_ALARM)
                .setSmallIcon(R.drawable.ic_ring_alarm)
                .setContentTitle(alarmTimeInString)
                .setContentText(alarm.getName())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(alarm.getRingtoneUri())
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();

        return notification;
//        _notificationManagerCompat.notify(alarm.getId(), notification);
    }

    public void sendOtherNofication(String title, String message) {
        Notification notification = new NotificationCompat.Builder(_context, AlarmApplication.CHANNEL_2_OTHER)
                .setSmallIcon(R.drawable.ic_other_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();



//        _notificationManagerCompat.notify(2, notification);
    }

    public void sendShortLengthToastMessage(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public void sendLongLengthToastMessage(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
    }

    public void removeNotification(int alarmId) {
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(alarmId);
    }
}
