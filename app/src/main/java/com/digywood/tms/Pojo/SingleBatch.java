package com.digywood.tms.Pojo;

/**
 * Created by prasa on 2018-03-06.
 */

public class SingleBatch {

    private String id,name,startdate,enddate;
    private Double feeamount,feetax;

    public SingleBatch(){

    }

    public SingleBatch(String bid,String bname,String bstartdate,String benddate,Double bfeeamount,Double bfeetax){
        this.id=bid;
        this.name=bname;
        this.startdate=bstartdate;
        this.enddate=benddate;
        this.feeamount=bfeeamount;
        this.feetax=bfeetax;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Double getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(Double feeamount) {
        this.feeamount = feeamount;
    }

    public Double getFeetax() {
        return feetax;
    }

    public void setFeetax(Double feetax) {
        this.feetax = feetax;
    }
}
