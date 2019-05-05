package com.csbunlimited.ctse_csb_alarm_services;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import com.csbunlimited.ctse_csb_alarm.R;
import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;

public class NotificationManagerService {

    private Context _context;
    private NotificationManagerCompat _notificationManagerCompat;

    public NotificationManagerService(Context context) {
        _context = context;
        _notificationManagerCompat = NotificationManagerCompat.from(_context);
    }

    public void sendRingAlarmNofication(String alarmTimeInString, String alarmName) {
        Notification notification = new NotificationCompat.Builder(_context, AlarmApplication.CHANNEL_1_RING_ALARM)
                .setSmallIcon(R.drawable.ic_ring_alarm)
                .setContentTitle(alarmTimeInString)
                .setContentText(alarmName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        _notificationManagerCompat.notify(1, notification);
    }

    public void sendOtherNofication(String title, String message) {
        Notification notification = new NotificationCompat.Builder(_context, AlarmApplication.CHANNEL_2_OTHER)
                .setSmallIcon(R.drawable.ic_other_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        _notificationManagerCompat.notify(2, notification);
    }

    public void sendShortLengthToastMessage(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public void sendLongLengthToastMessage(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
    }
}
