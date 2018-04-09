package com.digywood.tms.Pojo;

public class SingleGroupConfig {

    private String testid,sectionid,grouptype;
    private int pickcount;

    public SingleGroupConfig(){

    }

    public SingleGroupConfig(String testid,String sectionid,String grouptype,int pickupcount){

        this.testid=testid;
        this.sectionid=sectionid;
        this.grouptype=grouptype;
        this.pickcount=pickupcount;

    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public int getPickcount() {
        return pickcount;
    }

    public void setPickcount(int pickcount) {
        this.pickcount = pickcount;
    }
}
