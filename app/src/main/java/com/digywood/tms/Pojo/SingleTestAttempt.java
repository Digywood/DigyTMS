package com.digywood.tms.Pojo;

/**
 * Created by Shashank on 26-04-2018.
 */

public class SingleTestAttempt {

    private int attemptId,attemptcount,skipcount,markcount,notattemptcount;
    private Double score, percent;

    public SingleTestAttempt(int attemptId,int attemptcount,int skipcount,int markcount,int notattemptcount,double score,double percent){
        this.attemptId = attemptId;
        this.attemptcount = attemptcount;
        this.skipcount = skipcount;
        this.markcount = markcount;
        this.notattemptcount = notattemptcount;
        this.score = score;;
        this.percent = percent;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getAttemptcount() {
        return attemptcount;
    }

    public void setAttemptcount(int attemptcount) {
        this.attemptcount = attemptcount;
    }

    public int getSkipcount() {
        return skipcount;
    }

    public void setSkipcount(int skipcount) {
        this.skipcount = skipcount;
    }

    public int getMarkcount() {
        return markcount;
    }

    public void setMarkcount(int markcount) {
        this.markcount = markcount;
    }

    public int getNotattemptcount() {
        return notattemptcount;
    }

    public void setNotattemptcount(int notattemptcount) {
        this.notattemptcount = notattemptcount;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
