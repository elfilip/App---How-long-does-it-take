package com.example.root.myapplication.entity;

/**
 * Created by elfilip on 12.1.17.
 */

public class Configuration {

    private String language;
    private boolean showMilis=false;
    private boolean timeWhiteColor=false;
    private boolean showIcon=false;


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isShowMilis() {
        return showMilis;
    }

    public void setShowMilis(boolean showMilis) {
        this.showMilis = showMilis;
    }

    public boolean isTimeWhiteColor() {
        return timeWhiteColor;
    }

    public void setTimeWhiteColor(boolean timeWhiteColor) {
        this.timeWhiteColor = timeWhiteColor;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }
}
