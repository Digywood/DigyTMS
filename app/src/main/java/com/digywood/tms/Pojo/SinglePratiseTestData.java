package com.digywood.tms.Pojo;

public class SinglePratiseTestData {

    private String orgId,enrollId,studentId,batchId,courseId,testId,testName,subjectId,paperId,downloadStatus,Status,createby,createddttm;
    private double totalmarks,pminmarks,pavgmarks,pmaxmarks,pminpercent,pavgpercent,pmaxpercent,plastattemptmarks,plastattmeptpercent,fminscore,favgscore,fmaxscore,flastattemptscore;
    private int noofQues;
    private int noofattempts;
    private int noofflashattempts;
    private int ptu_sequence;
    private String ptu_test_time,ptu_test_type;

    public String getPtu_test_time() {
        return ptu_test_time;
    }

    public void setPtu_test_time(String ptu_test_time) {
        this.ptu_test_time = ptu_test_time;
    }

    public String getPtu_test_type() {
        return ptu_test_type;
    }

    public void setPtu_test_type(String ptu_test_type) {
        this.ptu_test_type = ptu_test_type;
    }

    public SinglePratiseTestData(){

    }

    public SinglePratiseTestData(String orgid, String enrollid, String studentid, String batchid, String courseid, String testid, String testname, String subjectid, String paperid, String dwdstatus, String status, int noofques, double totalmarks, String ptu_test_time, String ptu_test_type, double pminmarks, double pavgmarks, double pmaxmarks, double pminpercent, double pavgpercent, double pmaxpercent, double plastattemptmarks, double plastattmeptpercent, int noofattempts, int noofflashattempts, double fminscore, double favgscore, double fmaxscore, double flastattemptscore, String createby, String createddttm,int ptu_sequence){

        this.orgId=orgid;
        this.enrollId=enrollid;
        this.studentId=studentid;
        this.batchId=batchid;
        this.courseId=courseid;
        this.testId=testid;
        this.testName=testname;
        this.subjectId=subjectid;
        this.paperId=paperid;
        this.downloadStatus=dwdstatus;
        this.Status=status;
        this.noofQues=noofques;
        this.totalmarks=totalmarks;
        this.ptu_test_time=ptu_test_time;
        this.ptu_test_type=ptu_test_type;
        this.pminmarks=pminmarks;
        this.pavgmarks=pavgmarks;
        this.pmaxmarks=pmaxmarks;
        this.pminpercent=pminpercent;
        this.pavgpercent=pavgpercent;
        this.pmaxpercent=pmaxpercent;
        this.plastattemptmarks=plastattemptmarks;
        this.plastattmeptpercent=plastattmeptpercent;
        this.noofattempts=noofattempts;
        this.noofflashattempts=noofflashattempts;
        this.fminscore=fminscore;
        this.favgscore=favgscore;
        this.fmaxscore=fmaxscore;
        this.flastattemptscore=flastattemptscore;
        this.createby=createby;
        this.createddttm=createddttm;
        this.ptu_sequence=ptu_sequence;

    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getCreateddttm() {
        return createddttm;
    }

    public void setCreateddttm(String createddttm) {
        this.createddttm = createddttm;
    }

    public double getTotalmarks() {
        return totalmarks;
    }

    public void setTotalmarks(double totalmarks) {
        this.totalmarks = totalmarks;
    }

    public double getPminmarks() {
        return pminmarks;
    }

    public void setPminmarks(double pminmarks) {
        this.pminmarks = pminmarks;
    }

    public double getPavgmarks() {
        return pavgmarks;
    }

    public void setPavgmarks(double pavgmarks) {
        this.pavgmarks = pavgmarks;
    }

    public double getPmaxmarks() {
        return pmaxmarks;
    }

    public void setPmaxmarks(double pmaxmarks) {
        this.pmaxmarks = pmaxmarks;
    }

    public double getPminpercent() {
        return pminpercent;
    }

    public void setPminpercent(double pminpercent) {
        this.pminpercent = pminpercent;
    }

    public double getPavgpercent() {
        return pavgpercent;
    }

    public void setPavgpercent(double pavgpercent) {
        this.pavgpercent = pavgpercent;
    }

    public double getPmaxpercent() {
        return pmaxpercent;
    }

    public void setPmaxpercent(double pmaxpercent) {
        this.pmaxpercent = pmaxpercent;
    }

    public double getPlastattemptmarks() {
        return plastattemptmarks;
    }

    public void setPlastattemptmarks(double plastattemptmarks) {
        this.plastattemptmarks = plastattemptmarks;
    }

    public double getPlastattmeptpercent() {
        return plastattmeptpercent;
    }

    public void setPlastattmeptpercent(double plastattmeptpercent) {
        this.plastattmeptpercent = plastattmeptpercent;
    }

    public double getFminscore() {
        return fminscore;
    }

    public void setFminscore(double fminscore) {
        this.fminscore = fminscore;
    }

    public double getFavgscore() {
        return favgscore;
    }

    public void setFavgscore(double favgscore) {
        this.favgscore = favgscore;
    }

    public double getFmaxscore() {
        return fmaxscore;
    }

    public void setFmaxscore(double fmaxscore) {
        this.fmaxscore = fmaxscore;
    }

    public double getFlastattemptscore() {
        return flastattemptscore;
    }

    public void setFlastattemptscore(double flastattemptscore) {
        this.flastattemptscore = flastattemptscore;
    }

    public int getNoofattempts() {
        return noofattempts;
    }

    public void setNoofattempts(int noofattempts) {
        this.noofattempts = noofattempts;
    }

    public int getNoofflashattempts() {
        return noofflashattempts;
    }

    public void setNoofflashattempts(int noofflashattempts) {
        this.noofflashattempts = noofflashattempts;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getNoofQues() {
        return noofQues;
    }

    public void setNoofQues(int noofQues) {
        this.noofQues = noofQues;
    }

    public int getPtu_sequence() {
        return ptu_sequence;
    }

    public void setPtu_sequence(int ptu_sequence) {
        this.ptu_sequence = ptu_sequence;
    }
}
