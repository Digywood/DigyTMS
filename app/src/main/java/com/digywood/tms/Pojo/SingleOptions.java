package com.digywood.tms.Pojo;

/**
 * Created by Shashank on 02-03-2018.
 */

public class SingleOptions {

    private String qbo_id,qbo_seq_no,qbo_type,qbo_text,qbo_media_type,qbo_media_file,qbo_answer_flag;
    public SingleOptions(){

    }

    public SingleOptions(String id,String seqno,String type,String text,String mtype,String mfile,String answflag){
        this.qbo_id=id;
        this.qbo_seq_no=seqno;
        this.qbo_type=type;
        this.qbo_text=text;
        this.qbo_media_type=mtype;
        this.qbo_media_file=mfile;
        this.qbo_answer_flag=answflag;
    }


    public String getQbo_id() {
        return qbo_id;
    }

    public void setQbo_id(String qbo_id) {
        this.qbo_id = qbo_id;
    }

    public String getQbo_seq_no() {
        return qbo_seq_no;
    }

    public void setQbo_seq_no(String qbo_seq_no) {
        this.qbo_seq_no = qbo_seq_no;
    }

    public String getQbo_type() {
        return qbo_type;
    }

    public void setQbo_type(String qbo_type) {
        this.qbo_type = qbo_type;
    }

    public String getQbo_text() {
        return qbo_text;
    }

    public void setQbo_text(String qbo_text) {
        this.qbo_text = qbo_text;
    }

    public String getQbo_media_type() {
        return qbo_media_type;
    }

    public void setQbo_media_type(String qbo_media_type) {
        this.qbo_media_type = qbo_media_type;
    }

    public String getQbo_media_file() {
        return qbo_media_file;
    }

    public void setQbo_media_file(String qbo_media_file) {
        this.qbo_media_file = qbo_media_file;
    }

    public String getQbo_answer_flag() {
        return qbo_answer_flag;
    }

    public void setQbo_answer_flag(String qbo_answer_flag) {
        this.qbo_answer_flag = qbo_answer_flag;
    }
}
