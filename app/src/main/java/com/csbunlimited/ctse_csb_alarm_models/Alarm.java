package com.csbunlimited.ctse_csb_alarm_models;


import android.net.Uri;

import java.util.Date;

public class Alarm {

    private int id;
    private String name;
    private boolean isActive;
    private Date date;
    private Uri ringtoneUri;

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(Uri ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public Alarm() {
        this.id = 0;
        this.name = "";
        this.isActive = true;
        this.date = new Date();
        this.ringtoneUri = null;
    }

    public Alarm(int id, String name, boolean isActive, Date date, Uri ringtoneUri) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.date = date;
        this.ringtoneUri = ringtoneUri;
    }
}
