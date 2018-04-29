package com.digywood.tms.Pojo;

public class SingleDWDQues {

    String chapterId,paperId,fileName;

    public SingleDWDQues(){

    }

    public SingleDWDQues(String chapterid,String paperId,String filename){
        this.chapterId=chapterid;
        this.paperId=paperId;
        this.fileName=filename;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
