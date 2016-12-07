package com.example.root.myapplication.entity;

import android.os.SystemClock;

import java.util.Date;

/**
 * Created by felias on 6.12.16.
 */

public class Measurement {

    private Date time;
    private String timeText;
    private Date date;
    private String note;

    public Measurement(Date time, Date date) {
        this(time, date, "");
    }

    public Measurement(Date time, Date date, String note) {

        this.time = time;
        this.date = date;
        this.note = note;
        timeText = convertTimeToText(time);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
        timeText=convertTimeToText(time);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimeText() {
        return timeText;
    }

    private String convertTimeToText(Date time) {
        long sec = time.getTime() / 1000;
        long seconds = sec % 60;
        long minutes = (sec / 60) % 60;
        long hours = (sec / 60) / 60;
        StringBuilder result = new StringBuilder();
        if (hours < 10) {
            result.append("0");
        }
        result.append(hours);
        result.append(":");
        if (minutes < 10) {
            result.append("0");
        }
        result.append(minutes);
        result.append(":");
        if (seconds < 10) {
            result.append("0");
        }
        result.append(seconds);
        return result.toString();
    }
}


