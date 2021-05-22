package com.example.carassistant.ui;

import java.io.Serializable;

public class Notifications implements Serializable {
    private long id;
    private String name, date;
    private int way, repeatCounter;
    private int repeatType;

    public Notifications (long id, String name, String date, int way, int repeatCounter, int repeatType) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.way = way;
        this.repeatCounter = repeatCounter;
        this.repeatType = repeatType;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getWay() {
        return way;
    }

    public int getRepeatCounter() {
        return repeatCounter;
    }

    public int getRepeatType() {
        return repeatType;
    }
}
