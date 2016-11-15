package com.example.root.myapplication.entity;

import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by felias on 9.11.16.
 */

public class ActionView {
    private TextView name;
    private TextView time;
    private ImageButton delete;

    public ActionView(TextView name, TextView time, ImageButton delete) {
        this.name = name;
        this.time = time;
        this.delete = delete;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public ImageButton getDelete() {
        return delete;
    }

    public void setDelete(ImageButton delete) {
        this.delete = delete;
    }
}
