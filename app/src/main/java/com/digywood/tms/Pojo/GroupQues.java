package com.digywood.tms.Pojo;

public class GroupQues {

    private String gid,qid;

    public GroupQues(){

    }

    public GroupQues(String groupid,String quesid){
        this.gid=groupid;
        this.qid=quesid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
