package com.csbunlimited.ctse_csb_alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_alarm_models.Question;
import com.csbunlimited.ctse_csb_alarm_services.ManageAlarmService;
import com.csbunlimited.ctse_csb_alarm_services.NotificationManagerService;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class StopAlarmActivity extends AppCompatActivity {

    private AlarmDBHandler _alarmDBHandler;
    private NotificationManagerService _notificationManagerService;
    private ManageAlarmService _manageAlarmService;

    private Alarm _alarm;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        _alarmDBHandler = new AlarmDBHandler(getApplicationContext());
        _notificationManagerService = new NotificationManagerService(this);

        ManageAlarmService.initializeManageAlarmServiceInstance(AlarmApplication.getMainActivity());
        _manageAlarmService = ManageAlarmService.getManageAlarmServiceInstance();

        int alarmId = getIntent().getIntExtra(AlarmApplication.ALARM_ID, 0);
        _alarm = _alarmDBHandler.getAlarmById(alarmId);

        button = findViewById(R.id.stopAlarm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _notificationManagerService.removeNotification(_alarm.getId());

                _alarm.setIsActive(false);
//                _alarm.setAudioSessionId(Alarm.NONE_AUDIO_SESSION_ID);

                _alarmDBHandler.updateAlarm(_alarm);
            }
        });


    }


}
