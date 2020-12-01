package com.android.fundamentals.standup.model;

public class Measure {
//    int sid;
    long date;
    double value;

    public Measure() {
    }

//    public int getSensorID() {
//        return sid;
//    }
//
//    public void setSid(int sensorID) {
//        this.sid = sensorID;
//    }

    public long getDate() {
        return date;
    }

    public void setTimestamp(long timestamp) {
        this.date = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
