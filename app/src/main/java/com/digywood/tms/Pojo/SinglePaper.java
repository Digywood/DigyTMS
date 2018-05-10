package com.digywood.tms.Pojo;

public class SinglePaper {

    private String paperId,paperName;
    private double min,avg,max;

    public SinglePaper(){

    }

    public SinglePaper(String paperid,String papername,double min,double avg,double max){

        this.paperId=paperid;
        this.paperName=papername;
        this.min=min;
        this.avg=avg;
        this.max=max;

    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
