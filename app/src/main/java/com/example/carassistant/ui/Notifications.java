package com.example.carassistant.ui;

import java.io.Serializable;

public class Notifications implements Serializable {
    private long id;
    private String name;

    public Notifications (long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

}
