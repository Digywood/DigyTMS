package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-02-27.
 */

public class SingleEnrollment {

    private String enrollid,enrollcourseid;

    public SingleEnrollment(){

    }
    public SingleEnrollment(String enrollid,String enrollcourseid){

        this.enrollid=enrollid;
        this.enrollcourseid=enrollcourseid;

    }

    public String getEnrollid() {
        return enrollid;
    }

    public void setEnrollid(String enrollid) {
        this.enrollid = enrollid;
    }

    public String getEnrollcourseid() {
        return enrollcourseid;
    }

    public void setEnrollcourseid(String enrollcourseid) {
        this.enrollcourseid = enrollcourseid;
    }
}
