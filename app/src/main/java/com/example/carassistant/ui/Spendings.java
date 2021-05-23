package com.example.carassistant.ui;

public class Spendings {
    private final long spendingId;
    private final String spendingName;
    private final String spendingDate;
    private final String spendingType;
    private final int spendingSum;

    public Spendings(long spendingId, String spendingName, String spendingDate, String spendingType, int spendingSum) {
        this.spendingId = spendingId;
        this.spendingName = spendingName;
        this.spendingDate = spendingDate;
        this.spendingType = spendingType;
        this.spendingSum = spendingSum;
    }

    public int getSpendingSum() {
        return spendingSum;
    }

    public String getSpendingType() {
        return spendingType;
    }

    public String getSpendingDate() {
        return spendingDate;
    }


    public long getSpendingId() {
        return spendingId;
    }

    public String getSpendingName() {
        return spendingName;
    }
}
