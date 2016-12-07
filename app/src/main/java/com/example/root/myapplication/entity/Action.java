package com.example.root.myapplication.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by felias on 8.11.16.
 */

public class Action {
    private String name;
    private List<Measurement> measurements;

    public Action(String name) {
        this.name = name;
        measurements = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Measurement> getMeasurement() {
        return measurements;
    }

    public void setMasurement(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement(Measurement measurement){
        measurements.add(measurement);
    }

    public Date getAverageTime() {
        long sum=0;
        for (Measurement m : measurements) {
            sum = sum + m.getTime().getTime();
        }
        sum = sum / measurements.size();
        return new Date(sum);
    }

    public boolean hasMoreMeasurements() {
        return measurements.size() >1;
    }
}
