package com.example.carassistant.ui;

import java.io.Serializable;

public class StartActivityInfo implements Serializable {
    private String mark, model, year, engine;
    private int way;

    public StartActivityInfo(String mark, String model, String year, String engine, int way){
        this.mark = mark;
        this.model = model;
        this.year = year;
        this.engine = engine;
        this.way = way;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }
}
