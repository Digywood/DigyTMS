package com.digywood.tms.Pojo;

public class SingleFlashAttempt {

    private String date;

    private int attemptQcount,knowcount,donknowcount,skipcount;

    private Double percent;

    public SingleFlashAttempt(String dttm,int aQcount,int knowcount,int donknowcount,int skipcount,Double percentage){

        this.date=dttm;
        this.attemptQcount=aQcount;
        this.knowcount=knowcount;
        this.donknowcount=donknowcount;
        this.skipcount=skipcount;
        this.percent=percentage;

    }

    public int getAttemptQcount() {
        return attemptQcount;
    }

    public void setAttemptQcount(int attemptQcount) {
        this.attemptQcount = attemptQcount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getKnowcount() {
        return knowcount;
    }

    public void setKnowcount(int knowcount) {
        this.knowcount = knowcount;
    }

    public int getDonknowcount() {
        return donknowcount;
    }

    public void setDonknowcount(int donknowcount) {
        this.donknowcount = donknowcount;
    }

    public int getSkipcount() {
        return skipcount;
    }

    public void setSkipcount(int skipcount) {
        this.skipcount = skipcount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
