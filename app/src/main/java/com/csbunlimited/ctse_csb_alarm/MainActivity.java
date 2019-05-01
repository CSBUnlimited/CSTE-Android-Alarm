package com.csbunlimited.ctse_csb_alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.csbunlimited.ctse_csb_alarm_adapters.AlarmListAdapter;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;
import com.csbunlimited.ctse_csb_db.AlarmDBHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Alarm> _alarmList;
    private AlarmDBHandler _alarmDBHandler;

    private TextView _contentMainAlarmStatusTextView;
    private ListView _contentMainAlarmListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _contentMainAlarmStatusTextView = findViewById(R.id.contentMainAlarmStatusTextView);
        _contentMainAlarmListView = findViewById(R.id.contentMainAlarmListView);

        _alarmDBHandler = new AlarmDBHandler(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent myIntent = new Intent(MainActivity.this, NewAlarmActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        _contentMainAlarmListView.setVisibility(View.GONE);

        _contentMainAlarmStatusTextView.setText("Loading...");
        _contentMainAlarmStatusTextView.setVisibility(View.VISIBLE);

        _alarmList = _alarmDBHandler.getAllAlarms();
        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(MainActivity.this, _alarmList);
        _contentMainAlarmListView.setAdapter(alarmListAdapter);

        if (_alarmList.isEmpty()) {
            _contentMainAlarmStatusTextView.setText("No alarms to show...");
        }
        else {
            _contentMainAlarmStatusTextView.setVisibility(View.GONE);
            _contentMainAlarmListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
