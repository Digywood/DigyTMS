package com.digywood.tms.Pojo;

public class SingleEnrollRequest {

    private String enrollId,batchId,orgId,courseName,requestDate,endDate,amount,status,enrollKey;

    public SingleEnrollRequest(){

    }

    public SingleEnrollRequest(String enrollid,String batchid,String orgid,String coursename,String reqdate,String enddate,String amount,String status,String enrollkey){

        this.enrollId=enrollid;
        this.batchId=batchid;
        this.orgId=orgid;
        this.courseName=coursename;
        this.requestDate=reqdate;
        this.endDate=enddate;
        this.amount=amount;
        this.status=status;
        this.enrollKey=enrollkey;

    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEnrollKey() {
        return enrollKey;
    }

    public void setEnrollKey(String enrollKey) {
        this.enrollKey = enrollKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
