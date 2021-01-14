package com.android.fundamentals.standup.model;

public class LogEntry {
    public final static String INFO = "info";
    public final static String WARNING = "warning";
    public final static String ERROR = "error";
    long time;
    String priority;
    String message;
    int sid;

    public LogEntry(long time, String priority, String message, int sid) {
        this.time = time;
        this.priority = priority;
        this.message = message;
        this.sid = sid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
