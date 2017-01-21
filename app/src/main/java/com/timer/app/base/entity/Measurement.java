package com.timer.app.base.entity;

import com.timer.app.base.util.Utils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by felias on 6.12.16.
 */

public class Measurement implements Serializable{

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

    public String getTimeText(boolean showMillis) {
        if(showMillis==false){
            return timeText.substring(0, timeText.length()-4);
        }else{
            return timeText;
        }
    }

    private String convertTimeToText(Date time) {
        return Utils.convertTimeToText(time,true);
    }
}


