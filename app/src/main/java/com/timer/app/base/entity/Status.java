package com.timer.app.base.entity;

/**
 * Created by felias on 13.11.16.
 */

public class Status {

    private String actionName;
    private long timerBase;
    private int requestCode;
    private String note;

    public Status(String actionName, long timerBase) {
        this.actionName = actionName;
        this.timerBase = timerBase;
        this.requestCode=-1;
    }
    public Status(String actionName, long timerBase,int requestCode) {
        this.actionName = actionName;
        this.timerBase = timerBase;
        this.requestCode=requestCode;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public long getTimerBase() {
        return timerBase;
    }

    public void setTimerBase(long timerBase) {
        this.timerBase = timerBase;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setNote(String note) {this.note=note; }

    public String getNote() {return note; }
}
