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
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.csbunlimited.ctse_csb_alarm_consts.AppKey;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

public class NewAlarmActivity extends AppCompatActivity {

    private final static int ACTIVITY_RESULT_STATUS_CODE = 10;

    private Alarm _alarm;
    private AlarmDBHandler _alarmDBHandler;

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

        int alarmId = getIntent().getIntExtra(AppKey.ALARM_ID, 0);
        if (alarmId > 0) {
            _alarm = _alarmDBHandler.getAlarmById(alarmId);
        }
        else {
            _alarm = new Alarm();
        }

        setContentView(R.layout.activity_new_alarm);

        _newAlarmNameTextInputEditText = findViewById(R.id.newAlarmNameTextInputEditText);
        _newAlarmDateTimePicker = findViewById(R.id.newAlarmDateTimePicker);
        _newAlarmOnOffSwitch = findViewById(R.id.oneAlarmOnOffSwitch);
        _newAlarmRingtoneNameTextView = findViewById(R.id.newAlarmRingtoneNameTextView);

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

        setRingToneUri();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_STATUS_CODE)
        {
            Uri ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            _alarm.setRingtoneUri(ringtoneUri);
            setRingToneUri();
        }

    }

    private void setRingToneUri() {

        if (_alarm.getRingtoneUri() != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(NewAlarmActivity.this, Uri.parse(_alarm.getRingtoneUri().toString()));
            _newAlarmRingtoneNameTextView.setText(ringtone.getTitle(NewAlarmActivity.this));

//          Toast.makeText(NewAlarmActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
        }
        else {
            _newAlarmRingtoneNameTextView.setText("- Select Ringtone -");
        }
    }
}
