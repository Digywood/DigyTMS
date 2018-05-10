package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-02-27.
 */

public class SingleEnrollment {

    private String enrollid,batchid,orgid,courseid,coursename,activateddate,enddate;
    private String denrollid,dcourseid;
    private int daysleft;

    public SingleEnrollment(){

    }

    public SingleEnrollment(String denrollid,String dcourseid){
        this.denrollid=denrollid;
        this.dcourseid=dcourseid;
    }

    public SingleEnrollment(String enrollid,String batchid,String orgid,String courseid,String coursename,String activateddate,String enddate,int daysleft){

        this.enrollid=enrollid;
        this.batchid=batchid;
        this.orgid=orgid;
        this.courseid=courseid;
        this.coursename=coursename;
        this.activateddate=activateddate;
        this.enddate=enddate;
        this.daysleft=daysleft;

    }

    public String getEnrollid() {
        return enrollid;
    }

    public void setEnrollid(String enrollid) {
        this.enrollid = enrollid;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getActivateddate() {
        return activateddate;
    }

    public void setActivateddate(String activateddate) {
        this.activateddate = activateddate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getDaysleft() {
        return daysleft;
    }

    public void setDaysleft(int daysleft) {
        this.daysleft = daysleft;
    }

    public String getDenrollid() {
        return denrollid;
    }

    public void setDenrollid(String denrollid) {
        this.denrollid = denrollid;
    }

    public String getDcourseid() {
        return dcourseid;
    }

    public void setDcourseid(String dcourseid) {
        this.dcourseid = dcourseid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
}
