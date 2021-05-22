package com.example.carassistant.ui;

import java.io.Serializable;

public class Notifications implements Serializable {
    private final long id;
    private final String name;
    private final String date;
    private final int way;
    private final int repeatCounter;
    private final int repeatType;

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
