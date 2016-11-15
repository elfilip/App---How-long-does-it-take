package com.example.root.myapplication.entity;

import java.util.Date;

/**
 * Created by felias on 8.11.16.
 */

public class Action {
    private String name;
    private String time;
    private Date date;

    public Action(String name, String time, Date date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
