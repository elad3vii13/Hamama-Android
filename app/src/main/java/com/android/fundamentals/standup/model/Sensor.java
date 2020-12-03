package com.android.fundamentals.standup.model;

public class Sensor {
    long id;
    String name;
    String units;

    public Sensor(long id, String name, String units) {
        super();
        this.id = id;
        this.name = name;
        this.units = units;
    }

    public Sensor() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "Sensor [id=" + id + ", name=" + name + ", units=" + units + "]";
    }
}
