package com.example.root.myapplication.entity;

/**
 * Created by felias on 13.11.16.
 */

public class Status {

    private String actionName;
    private long timerBase;

    public Status(String actionName, long timerBase) {
        this.actionName = actionName;
        this.timerBase = timerBase;
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
}
