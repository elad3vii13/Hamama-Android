package com.android.hamama.application.model;

public class Measure {
//    int sid;
    long time;
    double value;

    public Measure() {
    }

    public Measure(long time, double value) {
        this.value = value;
        this.time = time;
    }

//    public int getSensorID() {
//        return sid;
//    }
//
//    public void setSid(int sensorID) {
//        this.sid = sensorID;
//    }

    public long getTime() {
        return time;
    }

    public void setTime(long d) {
        this.time = d;
    }

    public void setTimestamp(long timestamp) {
        this.time = timestamp;
    }



    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
