package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-02-27.
 */

public class SingleTest {

    private String testid,testName,subjectid,status;

    public SingleTest(){

    }

    public SingleTest(String testid,String testName,String subjectid,String status){
        this.testid=testid;
        this.testName=testName;
        this.subjectid=subjectid;
        this.status=status;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
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
