package com.digywood.tms.Pojo;

public class SingleSubcatConfig {

    private String subcatid;
    private int mandatepick,maxpick;

    public SingleSubcatConfig(){

    }

    public SingleSubcatConfig(String SubcatId,int maxpick,int minpick){

        this.subcatid=SubcatId;
        this.maxpick=maxpick;
        this.mandatepick=minpick;

    }

    public String getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(String subcatid) {
        this.subcatid = subcatid;
    }

    public int getMandatepick() {
        return mandatepick;
    }

    public void setMandatepick(int mandatepick) {
        this.mandatepick = mandatepick;
    }

    public int getMaxpick() {
        return maxpick;
    }

    public void setMaxpick(int maxpick) {
        this.maxpick = maxpick;
    }
}
