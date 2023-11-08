package com.android.hamama.application.model;

public class Measure {

    /*
    When you create the object a, you get a single Measure, which contains
    the time and the value of the measure, I don't need the "sid" because every time
    I request measures from the server, I demand for specific 'sensor-id'
    so I don't need to save that on every object of measure
    */

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
