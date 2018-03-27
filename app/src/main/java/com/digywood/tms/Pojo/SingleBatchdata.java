package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-03-07.
 */

public class SingleBatchdata {

    private String name,number,email;

    public SingleBatchdata(){

    }

    public SingleBatchdata(String bname,String bnumber,String bemail){

        this.name=bname;
        this.number=bnumber;
        this.email=bemail;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
