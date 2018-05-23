package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-02-27.
 */

public class SingleAssessment {

    private String testid,instanceId,testName,subjectid,status;

    public SingleAssessment(){

    }

    public SingleAssessment(String testid,String instanceid,String testName,String subjectid,String status){
        this.testid=testid;
        this.instanceId=instanceid;
        this.testName=testName;
        this.subjectid=subjectid;
        this.status=status;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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
