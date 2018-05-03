package com.digywood.tms.Pojo;

public class SingleDashPaper {

    private String paperid,papername;
    private int totaltests,attemptedtests;
    private Double max,min,avg;

    public SingleDashPaper(){

    }

    public SingleDashPaper(String paperid,String papername,int tottests,int atmtests,double max,double min,double avg){

        this.paperid=paperid;
        this.papername=papername;
        this.totaltests=tottests;
        this.attemptedtests=atmtests;
        this.max=max;
        this.min=min;
        this.avg=avg;

    }

    public String getPapername() {
        return papername;
    }

    public void setPapername(String papername) {
        this.papername = papername;
    }

    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }

    public int getTotaltests() {
        return totaltests;
    }

    public void setTotaltests(int totaltests) {
        this.totaltests = totaltests;
    }

    public int getAttemptedtests() {
        return attemptedtests;
    }

    public void setAttemptedtests(int attemptedtests) {
        this.attemptedtests = attemptedtests;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }
}
