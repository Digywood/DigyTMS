package com.digywood.tms.Pojo;

/**
 * Created by Shashank on 03-04-2018.
 */

public class SingleQuestionList {
    String q_num,q_status,q_check;

    public SingleQuestionList(String q_num, String q_status,String q_check){
        this.q_num = q_num;
        this.q_status = q_status;
        this.q_check = q_check;
    }

    public String getQ_check() {
        return q_check;
    }

    public void setQ_check(String q_check) {
        this.q_check = q_check;
    }

    public String getQ_num() {
        return q_num;
    }

    public void setQ_num(String q_num) {
        this.q_num = q_num;
    }

    public String getQ_status() {
        return q_status;
    }

    public void setQ_status(String q_status) {
        this.q_status = q_status;
    }
}
