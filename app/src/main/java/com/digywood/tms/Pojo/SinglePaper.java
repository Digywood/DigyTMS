package com.digywood.tms.Pojo;

public class SinglePaper {

    private String paperId,paperName;
    private int ptestcount,atestcount,pattemptcount,fattemptcount,aattemptcount,pprogress,fprogress,aprogress;
    private double pmin,pavg,pmax,fmin,favg,fmax,amin,aavg,amax;

    public SinglePaper(){

    }

    public SinglePaper(String paperid,String papername,int ptestcount,int atestcount,int pattemptcount,int fattemptcount,int aattemptcount,int pprogress,int fprogress,int aprogress,double pmin,double pavg,double pmax,double fmin,double favg,double fmax,double amin,double aavg,double amax){

        this.paperId=paperid;
        this.paperName=papername;
        this.ptestcount=ptestcount;
        this.atestcount=atestcount;
        this.pattemptcount=pattemptcount;
        this.fattemptcount=fattemptcount;
        this.aattemptcount=aattemptcount;
        this.pprogress=pprogress;
        this.fprogress=fprogress;
        this.aprogress=aprogress;
        this.pmin=pmin;
        this.pavg=pavg;
        this.pmax=pmax;
        this.fmin=fmin;
        this.favg=favg;
        this.fmax=fmax;
        this.amin=amin;
        this.aavg=aavg;
        this.amax=amax;

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

    public double getPmin() {
        return pmin;
    }

    public void setPmin(double pmin) {
        this.pmin = pmin;
    }

    public double getPavg() {
        return pavg;
    }

    public void setPavg(double pavg) {
        this.pavg = pavg;
    }

    public double getPmax() {
        return pmax;
    }

    public void setPmax(double pmax) {
        this.pmax = pmax;
    }

    public double getFmin() {
        return fmin;
    }

    public void setFmin(double fmin) {
        this.fmin = fmin;
    }

    public double getFavg() {
        return favg;
    }

    public void setFavg(double favg) {
        this.favg = favg;
    }

    public double getFmax() {
        return fmax;
    }

    public void setFmax(double fmax) {
        this.fmax = fmax;
    }

    public double getAmin() {
        return amin;
    }

    public void setAmin(double amin) {
        this.amin = amin;
    }

    public double getAavg() {
        return aavg;
    }

    public void setAavg(double aavg) {
        this.aavg = aavg;
    }

    public double getAmax() {
        return amax;
    }

    public void setAmax(double amax) {
        this.amax = amax;
    }

    public int getPtestcount() {
        return ptestcount;
    }

    public void setPtestcount(int ptestcount) {
        this.ptestcount = ptestcount;
    }

    public int getAtestcount() {
        return atestcount;
    }

    public void setAtestcount(int atestcount) {
        this.atestcount = atestcount;
    }

    public int getPattemptcount() {
        return pattemptcount;
    }

    public void setPattemptcount(int pattemptcount) {
        this.pattemptcount = pattemptcount;
    }

    public int getFattemptcount() {
        return fattemptcount;
    }

    public void setFattemptcount(int fattemptcount) {
        this.fattemptcount = fattemptcount;
    }

    public int getAattemptcount() {
        return aattemptcount;
    }

    public void setAattemptcount(int aattemptcount) {
        this.aattemptcount = aattemptcount;
    }

    public int getPprogress() {
        return pprogress;
    }

    public void setPprogress(int pprogress) {
        this.pprogress = pprogress;
    }

    public int getFprogress() {
        return fprogress;
    }

    public void setFprogress(int fprogress) {
        this.fprogress = fprogress;
    }

    public int getAprogress() {
        return aprogress;
    }

    public void setAprogress(int aprogress) {
        this.aprogress = aprogress;
    }
}
