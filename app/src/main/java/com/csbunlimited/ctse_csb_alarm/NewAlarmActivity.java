package com.csbunlimited.ctse_csb_alarm;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.csbunlimited.ctse_csb_alarm_consts.AlarmApplication;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_alarm_services.ManageAlarmService;
import com.csbunlimited.ctse_csb_alarm_services.NotificationManagerService;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NewAlarmActivity extends AppCompatActivity {

    private final static int ACTIVITY_RESULT_STATUS_CODE = 10;

    private Alarm _alarm;
    private AlarmDBHandler _alarmDBHandler;
    private NotificationManagerService _notificationManagerService;

    private TextInputEditText _newAlarmNameTextInputEditText;
    private TimePicker _newAlarmDateTimePicker;
    private Switch _newAlarmOnOffSwitch;
    private TextView _newAlarmRingtoneNameTextView;

    private Button _newAlarmPickRingtoneButton;

    private ConstraintLayout _newAlarmAddNewConstraintLayout;
    private ConstraintLayout _newAlarmUpdateConstraintLayout;

    private Button _newAlarmSaveButton;
    private Button _newAlarmUpdateButton;
    private Button _newAlarmDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _alarmDBHandler = new AlarmDBHandler(getApplicationContext());
        _notificationManagerService = new NotificationManagerService(this);

        int alarmId = getIntent().getIntExtra(AlarmApplication.ALARM_ID, 0);
        if (alarmId > 0) {
            _alarm = _alarmDBHandler.getAlarmById(alarmId);
        }
        else {
            _alarm = new Alarm();
            _alarm.setName("Alarm (" + String.valueOf(_alarmDBHandler.getNextAlarmId()) + ")");
        }

        setContentView(R.layout.activity_new_alarm);

        _newAlarmNameTextInputEditText = findViewById(R.id.newAlarmNameTextInputEditText);
        _newAlarmNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = "";

                if (s != null && s.length() > 0) {
                    name = s.toString();
                }

                _alarm.setName(name.trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _newAlarmRingtoneNameTextView = findViewById(R.id.newAlarmRingtoneNameTextView);

        _newAlarmOnOffSwitch = findViewById(R.id.newAlarmOnOffSwitch);
        _newAlarmOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _alarm.setIsActive(isChecked);
            }
        });

        _newAlarmDateTimePicker = findViewById(R.id.newAlarmDateTimePicker);
        _newAlarmDateTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (view.isEnabled()) {
                    Date alarmDate = _alarm.getDate();
                    alarmDate.setHours(hourOfDay);
                    alarmDate.setMinutes(minute);
                    _alarm.setDate(alarmDate);
                }
            }
        });

        _newAlarmPickRingtoneButton = findViewById(R.id.newAlarmPickRingtoneButton);
        _newAlarmPickRingtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, _alarm.getRingtoneUri());
                startActivityForResult(intent, ACTIVITY_RESULT_STATUS_CODE);
            }
        });

        _newAlarmAddNewConstraintLayout = findViewById(R.id.newAlarmAddNewConstraintLayout);
        _newAlarmUpdateConstraintLayout = findViewById(R.id.newAlarmUpdateConstraintLayout);

        if (_alarm.getId() > 0) {
            _newAlarmAddNewConstraintLayout.setVisibility(View.GONE);
            _newAlarmUpdateConstraintLayout.setVisibility(View.VISIBLE);
        }
        else {
            _newAlarmAddNewConstraintLayout.setVisibility(View.VISIBLE);
            _newAlarmUpdateConstraintLayout.setVisibility(View.GONE);
        }

        _newAlarmSaveButton = findViewById(R.id.newAlarmSaveButton);
        _newAlarmSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateAlarm();
            }
        });

        _newAlarmUpdateButton = findViewById(R.id.newAlarmUpdateButton);
        _newAlarmUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateAlarm();
            }
        });

        _newAlarmDeleteButton = findViewById(R.id.newAlarmDeleteButton);
        _newAlarmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setAlarmName();
        setAlarmIsActive();
        setAlarmTime();
        setAlarmRingToneUri();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_STATUS_CODE) {
            Uri ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            _alarm.setRingtoneUri(ringtoneUri);
            setAlarmRingToneUri();
        }

    }

    private void addOrUpdateAlarm() {
        // Getting data and validations
        String name = _alarm.getName();
        String ringtone = (_alarm.getRingtoneUri() == null ? "" : _alarm.getRingtoneUri().toString()).trim();

        if (name == null || name.equals("")) {
            _notificationManagerService.sendLongLengthToastMessage("Alarm Description cannot be empty");
            return;
        }

        if (ringtone == null || ringtone.equals("")) {
            _notificationManagerService.sendLongLengthToastMessage("Alarm Tone cannot be empty");
            return;
        }

        // Correcting date issues
        Date today = new Date();

        Date alarmDate = _alarm.getDate();
        alarmDate.setSeconds(0);
        alarmDate.setTime(alarmDate.getTime() - alarmDate.getTime() % 1000);

        long oneDayInMilis = TimeUnit.DAYS.toMillis(1);

        if (today.getTime() >= alarmDate.getTime()) {
            alarmDate.setTime(alarmDate.getTime() + oneDayInMilis);
        }
        else if (alarmDate.getTime() - today.getTime() > oneDayInMilis) {
            alarmDate.setTime(alarmDate.getTime() - oneDayInMilis);
        }

        // Inserting data to DB
        if (_alarm.getId() > 0) {
            boolean isSuccess = _alarmDBHandler.updateAlarm(_alarm);

            if (!isSuccess) {
                _notificationManagerService.sendLongLengthToastMessage("Alarm updating failed. Please try again.");
                return;
            }

            _notificationManagerService.sendShortLengthToastMessage("Alarm updated.");
        }
        else {
            int newAlarmId = _alarmDBHandler.addAlarm(_alarm);

            if (newAlarmId < 0) {
                _notificationManagerService.sendLongLengthToastMessage("Alarm adding failed. Please try again.");
                return;
            }

            _alarm.setId(newAlarmId);
            _notificationManagerService.sendShortLengthToastMessage("Alarm added.");
        }

        if (_alarm.getIsActive()) {
            ManageAlarmService.getManageAlarmServiceInstance().registerAlarm(_alarm.getId());

//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(_alarm.getDate().getTime());
//
//            // Repeating on every day
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 30, pendingIntent);
        }

        finish();
    }

    private void deleteAlarm() {
        _alarmDBHandler.deleteAlarmById(_alarm.getId());
        finish();
        _notificationManagerService.sendLongLengthToastMessage("Alarm deleted.");
    }

    private void setAlarmName() {
        _newAlarmNameTextInputEditText.setText(_alarm.getName() == null ? "" : _alarm.getName());
    }

    private void setAlarmTime() {
        _newAlarmDateTimePicker.setEnabled(false);
        _newAlarmDateTimePicker.setHour(_alarm.getDate().getHours());
        _newAlarmDateTimePicker.setMinute(_alarm.getDate().getMinutes());
        _newAlarmDateTimePicker.setEnabled(true);
    }

    private void setAlarmIsActive() {
        _newAlarmOnOffSwitch.setChecked(_alarm.getIsActive());
    }

    private void setAlarmRingToneUri() {
        if (_alarm.getRingtoneUri() != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(NewAlarmActivity.this, Uri.parse(_alarm.getRingtoneUri().toString()));
            _newAlarmRingtoneNameTextView.setText(ringtone.getTitle(NewAlarmActivity.this));

//            _notificationManagerService.sendLongLengthToastMessage(uri.toString());
        }
        else {
            _newAlarmRingtoneNameTextView.setText("- Select Ringtone -");
        }
    }
}
