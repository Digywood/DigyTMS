package com.digywood.tms.Pojo;

public class SingleDashTest {

    private String testid,testname,dttm,uploaddttm;
    Double latestpercenatage,smin,sman,savg,omin,omax,oavg;
    private int noofattempts,avgattempts,maxattempts,minattempts;

    public SingleDashTest(){

    }

    public SingleDashTest(String testid,String testname,int noofattempts,String dttm,Double latestpercenatage,String uploaddttm,Double smin,Double smax,Double savg,Double omin,Double omax,Double oavg,int avgattempts,int maxattempts,int minattempts){

        this.testid=testid;
        this.testname=testname;
        this.noofattempts=noofattempts;
        this.dttm=dttm;
        this.latestpercenatage=latestpercenatage;
        this.uploaddttm=uploaddttm;
        this.smin=smin;
        this.sman=smax;
        this.savg=savg;
        this.omin=omin;
        this.omax=omax;
        this.oavg=oavg;
        this.avgattempts=avgattempts;
        this.maxattempts=maxattempts;
        this.minattempts=minattempts;

    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getDttm() {
        return dttm;
    }

    public void setDttm(String dttm) {
        this.dttm = dttm;
    }

    public String getUploaddttm() {
        return uploaddttm;
    }

    public void setUploaddttm(String uploaddttm) {
        this.uploaddttm = uploaddttm;
    }

    public Double getSmin() {
        return smin;
    }

    public void setSmin(Double smin) {
        this.smin = smin;
    }

    public Double getSman() {
        return sman;
    }

    public void setSman(Double sman) {
        this.sman = sman;
    }

    public Double getSavg() {
        return savg;
    }

    public void setSavg(Double savg) {
        this.savg = savg;
    }

    public Double getOmin() {
        return omin;
    }

    public void setOmin(Double omin) {
        this.omin = omin;
    }

    public Double getOmax() {
        return omax;
    }

    public void setOmax(Double omax) {
        this.omax = omax;
    }

    public Double getOavg() {
        return oavg;
    }

    public void setOavg(Double oavg) {
        this.oavg = oavg;
    }

    public Double getLatestpercenatage() {
        return latestpercenatage;
    }

    public void setLatestpercenatage(Double latestpercenatage) {
        this.latestpercenatage = latestpercenatage;
    }

    public int getNoofattempts() {
        return noofattempts;
    }

    public void setNoofattempts(int noofattempts) {
        this.noofattempts = noofattempts;
    }

    public int getAvgattempts() {
        return avgattempts;
    }

    public void setAvgattempts(int avgattempts) {
        this.avgattempts = avgattempts;
    }

    public int getMaxattempts() {
        return maxattempts;
    }

    public void setMaxattempts(int maxattempts) {
        this.maxattempts = maxattempts;
    }

    public int getMinattempts() {
        return minattempts;
    }

    public void setMinattempts(int minattempts) {
        this.minattempts = minattempts;
    }
}


