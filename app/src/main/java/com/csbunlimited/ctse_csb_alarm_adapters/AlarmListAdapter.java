package com.csbunlimited.ctse_csb_alarm_adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.csbunlimited.ctse_csb_alarm.NewAlarmActivity;
import com.csbunlimited.ctse_csb_alarm.R;
import com.csbunlimited.ctse_csb_alarm_consts.AppKey;
import com.csbunlimited.ctse_csb_alarm_models.Alarm;

import java.util.List;

public class AlarmListAdapter extends BaseAdapter {

    private Context _activityContext;
    private LayoutInflater _inflate;

    private List<Alarm> _alarmList;

    public AlarmListAdapter(Context activityContext, List<Alarm> alarmList) {
        _activityContext = activityContext;
        _inflate = LayoutInflater.from(_activityContext);
        _alarmList = alarmList;
    }

    @Override
    public int getCount() {
        return _alarmList.size();
    }

    @Override
    public Alarm getItem(int i) {
        return _alarmList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = _inflate.inflate(R.layout.content_one_alarm, null);

        GridLayout alarmLayout = view.findViewById(R.id.oneAlarmLayout);
        TextView nameTextView = view.findViewById(R.id.oneAlarmNameTextView);
        TextView timeTextView = view.findViewById(R.id.oneAlarmTimeTextView);
        Switch onOffSwith = view.findViewById(R.id.oneAlarmOnOffSwitch);

        final Alarm alarm = getItem(i);

        nameTextView.setText(alarm.getName());
        timeTextView.setText(((alarm.getDate().getHours() < 10) ? "0" : "") + alarm.getDate().getHours()
                + " : " + ((alarm.getDate().getMinutes() < 10) ? "0" : "") + alarm.getDate().getMinutes());
        onOffSwith.setChecked(alarm.getIsActive());

        alarmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
//                Toast.makeText(clickedView.getContext(), "test " + alarm.getId(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(_activityContext, NewAlarmActivity.class);
                intent.putExtra(AppKey.ALARM_ID, alarm.getId());
                _activityContext.startActivity(intent);
            }
        });

        return view;
    }
}
