package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-02-27.
 */

public class SingleTest {

    private String testid,subjectid,status;

    public SingleTest(){

    }

    public SingleTest(String testid,String subjectid,String status){
        this.testid=testid;
        this.subjectid=subjectid;
        this.status=status;
    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
