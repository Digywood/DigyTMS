package com.digywood.tms.Pojo;

public class SingleDashPaper {

    private String paperid,papername;
    private int totaltests,attemptedtests;
    private Double max,min,avg,bmin,bmax,bavg;

    public SingleDashPaper(){

    }

    public SingleDashPaper(String paperid,String papername,int tottests,int atmtests,double max,double min,double avg,double bmin,double bmax,double bavg){

        this.paperid=paperid;
        this.papername=papername;
        this.totaltests=tottests;
        this.attemptedtests=atmtests;
        this.max=max;
        this.min=min;
        this.avg=avg;
        this.bmax=bmax;
        this.bmin=bmin;
        this.bavg=bavg;

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

    public Double getBmin() {
        return bmin;
    }

    public void setBmin(Double bmin) {
        this.bmin = bmin;
    }

    public Double getBmax() {
        return bmax;
    }

    public void setBmax(Double bmax) {
        this.bmax = bmax;
    }

    public Double getBavg() {
        return bavg;
    }

    public void setBavg(Double bavg) {
        this.bavg = bavg;
    }
}
