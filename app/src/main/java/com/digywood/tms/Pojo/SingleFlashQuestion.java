package com.digywood.tms.Pojo;

public class SingleFlashQuestion {

    private String Qnum,Qseqnum,Qstatus;

    public SingleFlashQuestion(){

    }

    public SingleFlashQuestion(String qnumber,String qseqnum,String qstatus){
        this.Qnum=qnumber;
        this.Qseqnum=qseqnum;
        this.Qstatus=qstatus;
    }

    public String getQnum() {
        return Qnum;
    }

    public void setQnum(String qnum) {
        Qnum = qnum;
    }

    public String getQseqnum() {
        return Qseqnum;
    }

    public void setQseqnum(String qseqnum) {
        Qseqnum = qseqnum;
    }

    public String getQstatus() {
        return Qstatus;
    }

    public void setQstatus(String qstatus) {
        Qstatus = qstatus;
    }
}
