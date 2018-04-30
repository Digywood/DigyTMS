package com.digywood.tms.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.Pojo.SingleSubcatConfig;
import java.util.ArrayList;

/**
 * Created by prasa on 2018-02-28.
 */

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;
    private static final int DATABASE_VERSION =1;

    public DBHelper(Context c)
    {
        super(c,"digytmsDB",null,DATABASE_VERSION);
        this.context=c;
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tbl_student_master="CREATE TABLE IF NOT EXISTS `student_master` (`StudentKey` integer,`StudentID` text DEFAULT NULL,`Student_Name` text DEFAULT NULL,"+
                "`Student_gender` text DEFAULT NULL,`Student_Education` text DEFAULT NULL,`Student_DOB` date DEFAULT NULL,`Student_Address01` text DEFAULT NULL,"+
                "`Student_Address02` text DEFAULT NULL,`Student_City` text DEFAULT NULL,`Student_State` text DEFAULT NULL,"+
                "`Student_Country` text DEFAULT NULL,`Student_Mobile` text DEFAULT NULL,`Student_email` text DEFAULT NULL,"+
                "`Student_password` text DEFAULT NULL,`Student_mac_id` text DEFAULT NULL,`Student_Status` text DEFAULT NULL,"+
                "`Student_created_by` text DEFAULT NULL,`Student_created_DtTm` datetime DEFAULT NULL,`Student_mod_by` text DEFAULT NULL,"+
                "`Student_mod_DtTm` datetime DEFAULT NULL)";
        db.execSQL(tbl_student_master);

        String tbl_enrollments="CREATE TABLE IF NOT EXISTS `enrollments` (\n" +
                "  `Enroll_key` integer,\n" +
                "  `Enroll_ID` text DEFAULT NULL,\n" +
                "  `Enroll_org_id` text DEFAULT NULL,\n" +
                "  `Enroll_Student_ID` text DEFAULT NULL,\n" +
                "  `Enroll_batch_ID` text DEFAULT NULL,\n" +
                "  `Enroll_course_ID` text DEFAULT NULL,\n" +
                "  `Enroll_batch_start_Dt` date DEFAULT NULL,\n" +
                "  `Enroll_batch_end_Dt` date DEFAULT NULL,\n" +
                "  `Enroll_Device_ID` text DEFAULT NULL,\n" +
                "  `Enroll_Date` date DEFAULT NULL,\n" +
                "  `Enroll_Status` text DEFAULT NULL,\n" +
                "  `Enroll_created_by` text DEFAULT NULL,\n" +
                "  `Enroll_created_dttm` datetime DEFAULT NULL,\n" +
                "  `Enroll_mod_by` text DEFAULT NULL,\n" +
                "  `Enroll_mod_dttm` datetime DEFAULT NULL)";
        db.execSQL(tbl_enrollments);

        String tbl_subjects="CREATE TABLE IF NOT EXISTS `subjects` (\n" +
                "  `Subject_key` integer,\n" +
                "  `Course_ID` text DEFAULT NULL,\n" +
                "  `Subject_ID` text DEFAULT NULL,\n" +
                "  `Subject_Name` text DEFAULT NULL,\n" +
                "  `Subject_ShortName` text DEFAULT NULL,\n" +
                "  `Subject_Seq_no` integer(3) DEFAULT NULL,\n" +
                "  `Subject_Type` text DEFAULT NULL,\n" +
                "  `Subject_Marks` text DEFAULT NULL,\n" +
                "  `Subject_Buff_01` text DEFAULT NULL,\n" +
                "  `Subject_Buff_02` text DEFAULT NULL,\n" +
                "  `Subject_Buff_03` text DEFAULT NULL,\n" +
                "  `Subject_Buff_04` text DEFAULT NULL,\n" +
                "  `Subject_Buff_05` text DEFAULT NULL,\n" +
                "  `Subject_Creadted_by` text DEFAULT NULL,\n" +
                "  `Subject_Creadted_DtTm` datetime DEFAULT NULL,\n" +
                "  `Subject_Mod_by` text DEFAULT NULL,\n" +
                "  `Subject_Mod_DtTm` datetime DEFAULT NULL,\n" +
                "  `Subject_status` text DEFAULT NULL)";
        db.execSQL(tbl_subjects);

        String tbl_papers="CREATE TABLE IF NOT EXISTS `papers` (\n" +
                "  `Paper_Key` integer,\n" +
                "  `Paper_ID` text DEFAULT NULL,\n" +
                "  `Paper_Seq_no` integer(3) DEFAULT NULL,\n" +
                "  `Subject_ID` text DEFAULT NULL,\n" +
                "  `Course_ID` text DEFAULT NULL,\n" +
                "  `Paper_Name` text DEFAULT NULL,\n" +
                "  `Paper_Short_Name` text DEFAULT NULL,\n" +
                "  `Paper_Min_Pass_Marks` integer(4) DEFAULT NULL,\n" +
                "  `Paper_Max_Marks` integer(4) DEFAULT NULL,\n" +
                "  `Paper_Buff_01` text DEFAULT NULL,\n" +
                "  `Paper_Buff_02` text DEFAULT NULL,\n" +
                "  `Paper_Buff_03` text DEFAULT NULL,\n" +
                "  `Paper_Buff_04` text DEFAULT NULL,\n" +
                "  `Paper_Buff_05` text DEFAULT NULL,\n" +
                "  `Paper_status` text DEFAULT NULL,\n" +
                "  `Paper_Creaed_by` text DEFAULT NULL,\n" +
                "  `Paper_Created_DtTm` text DEFAULT NULL,\n" +
                "  `Paper_Mod_by` text DEFAULT NULL,\n" +
                "  `Paper_Mod_DtTm` text DEFAULT NULL)";
        db.execSQL(tbl_papers);

        String tbl_sptu_student="CREATE TABLE IF NOT EXISTS `sptu_student` (\n" +
                "  `sptu_key` integer,\n" +
                "  `sptu_org_id` text DEFAULT NULL,\n" +
                "  `sptu_entroll_id` text DEFAULT NULL,\n" +
                "  `sptu_student_ID` text DEFAULT NULL,\n" +
                "  `sptu_batch` text DEFAULT NULL,\n" +
                "  `sptu_ID` text DEFAULT NULL,\n" +
                "  `sptu_paper_ID` text DEFAULT NULL,\n" +
                "  `sptu_subjet_ID` text DEFAULT NULL,\n" +
                "  `sptu_course_id` text DEFAULT NULL,\n" +
                "  `sptu_start_date` date DEFAULT NULL,\n" +
                "  `sptu_end_date` date DEFAULT NULL,\n" +
                "  `sptu_dwnld_start_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_dwnld_completed_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_dwnld_status` text DEFAULT NULL,\n" +
                "  `sptu_no_of_questions` integer(5) DEFAULT NULL,\n" +
                "  `sptu_tot_marks` double DEFAULT NULL,\n" +
                "  `stpu_min_marks` double DEFAULT NULL,\n" +
                "  `sptu_max_marks` double DEFAULT NULL,\n" +
                "  `sptu_avg_marks` double DEFAULT NULL,\n" +
                "  `sptu_min_percent` double DEFAULT NULL,\n" +
                "  `sptu_max_percent` double DEFAULT NULL,\n" +
                "  `sptu_avg_percent` double DEFAULT NULL,\n" +
                "  `sptu_last_attempt_marks` double DEFAULT NULL,\n" +
                "  `sptu_last_attempt_percent` double DEFAULT NULL,\n" +
                "  `sptu_last_attempt_start_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_last_attempt_end_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_no_of_attempts` integer(4) DEFAULT NULL,\n" +
                "  `sptuflash_attempts` int(5) DEFAULT NULL,\n" +
                "  `min_flashScore` double DEFAULT NULL,\n" +
                "  `max_flashScore` double DEFAULT NULL,\n" +
                "  `avg_flashScore` double DEFAULT NULL,\n" +
                "  `lastAttemptDttm` datetime DEFAULT NULL,\n" +
                "  `lastAttemptScore` double DEFAULT NULL,\n" +
                "  `sptu_created_by` text DEFAULT NULL,\n" +
                "  `sptu_created_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_mod_by` text DEFAULT NULL,\n" +
                "  `sptu_mod_dttm` datetime DEFAULT NULL)";
        db.execSQL(tbl_sptu_student);

        String tbl_satu_student="CREATE TABLE IF NOT EXISTS `satu_student` (\n" +
                "  `satu_key` integer PRIMARY KEY,\n" +
                "  `satu_org_id` text DEFAULT NULL,\n" +
                "  `satu_entroll_id` text DEFAULT NULL,\n" +
                "  `satu_student_id` text DEFAULT NULL,\n" +
                "  `satu_batch` text DEFAULT NULL,\n" +
                "  `satu_ID` text DEFAULT NULL,\n" +
                "  `satu_paper_ID` text DEFAULT NULL,\n" +
                "  `satu_subjet_ID` text DEFAULT NULL,\n" +
                "  `satu_course_id` text DEFAULT NULL,\n" +
                "  `satu_start_date` datetime DEFAULT NULL,\n" +
                "  `satu_end_date` datetime DEFAULT NULL,\n" +
                "  `satu_dwnld_start_dttm` datetime DEFAULT NULL,\n" +
                "  `satu_dwnld_completed_dttm` datetime DEFAULT NULL,\n" +
                "  `satu_dwnld_status` text DEFAULT NULL,\n" +
                "  `satu_no_of_questions` int(5) DEFAULT NULL,\n" +
                "  `satu_path` text DEFAULT NULL,\n" +
                "  `satu_file` text DEFAULT NULL,\n" +
                "  `satu_exam_key` text DEFAULT NULL,\n" +
                "  `satu_tot_marks` double DEFAULT NULL,\n" +
                "  `satu_min_marks` double DEFAULT NULL,\n" +
                "  `satu_max_marks` double DEFAULT NULL,\n" +
                "  `satu_avg_marks` double DEFAULT NULL,\n" +
                "  `satu_min_percent` double DEFAULT NULL,\n" +
                "  `satu_max_percent` double DEFAULT NULL,\n" +
                "  `satu_avg_percent` double DEFAULT NULL,\n" +
                "  `satu_last_attempt_marks` double DEFAULT NULL,\n" +
                "  `satu_last_attempt_percent` double DEFAULT NULL,\n" +
                "  `satu_last_attempt_start_dttm` datetime DEFAULT NULL,\n" +
                "  `satu_last_attempt_end_dttm` datetime DEFAULT NULL,\n" +
                "  `satu_no_of_attempts` int(11) DEFAULT NULL,\n" +
                "  `satu_created_by` text DEFAULT NULL,\n" +
                "  `satu_created_dttm` datetime DEFAULT NULL,\n" +
                "  `satu_mod_by` text DEFAULT NULL,\n" +
                "  `satu_mod_dttm` datetime DEFAULT NULL)";
        db.execSQL(tbl_satu_student);

        String AttemptList ="CREATE TABLE `attempt_list` (\n"+
                "   `Attempt_ID` INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
                "   `Attempt_Test_ID` INTEGER,\n"+
                "   `Attempt_Status` int(5) NOT NULL,\n"+
                "   `Attempt_RemainingTime` int(5) DEFAULT NULL,\n"+
                "   `Attempt_LastQuestion` int(5) DEFAULT NULL,\n"+
                "   `Attempt_LastSection` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Confirmed` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Skipped` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Bookmarked` int(5) DEFAULT NULL,\n"+
                "   `Attempt_UnAttempted` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Score` int(10) DEFAULT NULL,\n"+
                "   `Attempt_Percentage` int(10) DEFAULT NULL)";
        db.execSQL(AttemptList);

        String AttemptData=" CREATE TABLE `attempt_data` (\n"+
                "   `Test_ID` varchar(15),\n" +
                "   `Attempt_ID` INTEGER,\n"+
                "   `Question_ID` varchar(15),\n" +
                "   `Question_Seq_No` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Category` varchar(15) DEFAULT NULL,\n" +
                "   `Question_SubCategory` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Max_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Marks_Obtained` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Applied` double(15) DEFAULT NULL,\n"+
                "   `Question_Option` int(15) DEFAULT NULL,\n"+
                "   `Question_Status` varchar(20) DEFAULT NULL,\n"+
                "   `Question_Option_Sequence` varchar(20) DEFAULT NULL,\n"+
                "   `Option_Answer_Flag` varchar(15) DEFAULT NULL,\n"+
                "   PRIMARY KEY (`Question_ID`)\n"+
                ")";
        db.execSQL(AttemptData);

        String FlashAttemptTable="CREATE TABLE `flashcard_attempt` (\n" +
                "  `flashcard_attemptKey` integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  `studentId` text DEFAULT NULL,\n" +
                "  `enrollmentId` text DEFAULT NULL,\n" +
                "  `courseId` text DEFAULT NULL,\n" +
                "  `subjectId` text DEFAULT NULL,\n" +
                "  `paperId` text DEFAULT NULL,\n" +
                "  `flashcardId` text DEFAULT NULL,\n" +
                "  `attemptNumber` int(5) DEFAULT NULL,\n" +
                "  `startDttm` datetime DEFAULT NULL,\n" +
                "  `endDttm` datetime DEFAULT NULL,\n" +
                "  `attemptQCount` int(5) DEFAULT NULL,\n"+
                "  `iknowCount` int(5) DEFAULT NULL,\n" +
                "  `donknowCount` int(5) DEFAULT NULL,\n" +
                "  `skipCount` int(5) DEFAULT NULL,\n" +
                "  `percentageObtain` double DEFAULT NULL,\n" +
                "  `Status` text DEFAULT NULL)";
        db.execSQL(FlashAttemptTable);

        String tblqbgroup="CREATE TABLE `qb_group` (\n" +
                "  `qbg_key` integer PRIMARY KEY,`qbg_ID` text DEFAULT NULL,`testId` text DEFAULT NULL,\n" +
                "  `qbg_media_type` text DEFAULT NULL,`qbg_media_file` text DEFAULT NULL,\n" +
                "  `qbg_text` text DEFAULT NULL,`qbg_no_questions` int(4),`qbg_pickup_count` int(4),\n" +
                "  `qbg_status` text DEFAULT NULL,`qbg_created_by` text DEFAULT NULL,\n" +
                "  `qbg_created_dttm` datetime DEFAULT NULL,`qbg_mod_by` varchar(20) DEFAULT NULL," +
                "  `qbg_mod_dttm` datetime DEFAULT NULL,`qbg_type` varchar(45) DEFAULT NULL)";
        db.execSQL(tblqbgroup);

        String tblquesconfig="CREATE TABLE `ques_config` (\n" +
                "  `ques_configkey` integer PRIMARY KEY,`courseId` text DEFAULT NULL,\n" +
                "  `subjectId` text DEFAULT NULL,`paperId` text DEFAULT NULL,\n" +
                "  `testId` text NOT NULL,`categoryId` text DEFAULT NULL,`subcategoryId` text NOT NULL,\n" +
                "  `avail_count` int(10),`pickup_count` int(10),`min_pickup_count` int(10),\n" +
                "  `ques_configstatus` text NOT NULL)";
        db.execSQL(tblquesconfig);

        String tblgroupconfig="CREATE TABLE `groupques_config` (\n" +
                "  `groupques_configKey` integer PRIMARY KEY,\n" +
                "  `courseId` text DEFAULT NULL,\n" +
                "  `subjectId` text DEFAULT NULL,\n" +
                "  `paperId` text DEFAULT NULL,\n" +
                "  `testId` text NOT NULL,\n" +
                "  `sectionId` text NOT NULL,\n" +
                "  `groupType` text NOT NULL,\n" +
                "  `groupavail_count` int(10),\n" +
                "  `grouppickup_count` int(10),\n" +
                "  `groupques_configstatus` text DEFAULT NULL)";
        db.execSQL(tblgroupconfig);

        String tblptusections="CREATE TABLE `ptu_sections` (\n" +
                "  `Ptu_section_key` integer PRIMARY KEY,\n" +
                "  `Ptu_ID` text DEFAULT NULL,\n" +
                "  `Ptu_section_sequence` int(5) DEFAULT NULL,\n" +
                "  `Ptu_section_ID` text DEFAULT NULL,\n" +
                "  `Ptu_section_paper_ID` text DEFAULT NULL,\n" +
                "  `Ptu_section_subject_ID` text DEFAULT NULL,\n" +
                "  `Ptu_section_course_ID` text DEFAULT NULL,\n" +
                "  `Ptu_section_min_questions` int(5) DEFAULT NULL,\n" +
                "  `Ptu_section_max_questions` int(5) DEFAULT NULL,\n" +
                "  `Ptu_sec_tot_groups` int(5) DEFAULT NULL,\n" +
                "  `Ptu_sec_no_groups` int(5) DEFAULT NULL,\n" +
                "  `Ptu_section_status` text DEFAULT NULL,\n" +
                "  `Ptu_section_name` text DEFAULT NULL)";
        db.execSQL(tblptusections);

        String tblatusections="CREATE TABLE `atu_sections` (\n" +
                "  `atu_section_key` integer PRIMARY KEY,\n" +
                "  `atu_ID` text DEFAULT NULL,\n" +
                "  `atu_section_sequence` text DEFAULT NULL,\n" +
                "  `atu_section_ID` text DEFAULT NULL,\n" +
                "  `atu_section_paper_ID` text DEFAULT NULL,\n" +
                "  `atu_section_subject_id` text DEFAULT NULL,\n" +
                "  `atu_section_course_ID` text DEFAULT NULL,\n" +
                "  `atu_section_min_questions` int(5) DEFAULT NULL,\n" +
                "  `atu_section_max_questions` int(5) DEFAULT NULL,\n" +
                "  `atu_sec_tot_groups` int(5) DEFAULT NULL,\n" +
                "  `atu_sec_no_groups` int(5) DEFAULT NULL,\n" +
                "  `atu_section_status` text DEFAULT NULL,\n" +
                "  `atu_section_name` text DEFAULT NULL)";
        db.execSQL(tblatusections);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion)
        {
            case 1:
                // upgrade logic from version 1 to 2
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown old version "+oldVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertAssesmentTest(int tkey,String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,String testfilename,String testKey,Double ttotalmarks,Double tminmarks,Double tmaxmarks){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_key",tkey);
        cv.put("satu_org_id",torgid);
        cv.put("satu_entroll_id",tenrollid);
        cv.put("satu_student_id",tstudentid);
        cv.put("satu_batch",tbatch);
        cv.put("satu_ID",tid);
        cv.put("satu_paper_ID",tpid);
        cv.put("satu_subjet_ID",tsid);
        cv.put("satu_course_id",tcid);
        cv.put("satu_start_date",tstartdate);
        cv.put("satu_end_date",tenddate);
        cv.put("satu_dwnld_status",tdwdstatus);
        cv.put("satu_no_of_questions",tnoofques);
        cv.put("satu_file",testfilename);
        cv.put("satu_exam_key",testKey);
        cv.put("satu_tot_marks",ttotalmarks);
        cv.put("satu_min_marks",tminmarks);
        cv.put("satu_max_marks",tmaxmarks);
        insertFlag = db.insert("satu_student",null, cv);
        return insertFlag;
    }

    public long updateATestStatus(String testid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_dwnld_status", status);
        updateFlag=db.update("satu_student", cv, "satu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public Cursor getAssesmentTestData(String testId){
        Cursor c =db.query("satu_student", new String[] {"satu_course_id,satu_subjet_ID,satu_paper_ID,satu_entroll_id,satu_student_id,satu_batch"},"satu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public Cursor getAssesmentTestsByEnroll(String enrollId){
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_student_id,satu_batch,satu_ID,satu_paper_ID,satu_subjet_ID,satu_course_id,satu_start_date,satu_end_date,satu_dwnld_status"},"satu_entroll_id='"+enrollId+"'", null, null, null,null);
        return c;
    }

    public long deleteAllAssesmentTests(){
        long deleteFlag=0;
        deleteFlag=db.delete("satu_student", null,null);
        return  deleteFlag;
    }

    public long insertFlashAttempt(String studentId,String enrollId,String courseId,String subjectId,String paperId,String fcardId,int attemptnum,String sdttm,String edttm,int attemptQcount,int iknowcount,int donknowcount,int skipcount,Double percentage,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("studentId",studentId);
        cv.put("enrollmentId",enrollId);
        cv.put("courseId",courseId);
        cv.put("subjectId",subjectId);
        cv.put("paperId",paperId);
        cv.put("flashcardId",fcardId);
        cv.put("attemptNumber",attemptnum);
        cv.put("startDttm",sdttm);
        cv.put("endDttm",edttm);
        cv.put("attemptQCount",attemptQcount);
        cv.put("iknowCount",iknowcount);
        cv.put("donknowCount",donknowcount);
        cv.put("skipCount",skipcount);
        cv.put("percentageObtain",percentage);
        cv.put("Status",status);
        insertFlag = db.insert("flashcard_attempt",null, cv);
        return insertFlag;
    }

    public long deleteAllTestFlashAttempts(String testId){
        long deleteFlag=0;
        deleteFlag=db.delete("flashcard_attempt", "flashcardId='"+testId+"'", null);
        return  deleteFlag;
    }

    public Cursor getFlashTestData(String testId){
        Cursor c =db.query("flashcard_attempt", new String[] {"attemptNumber,startDttm,attemptQCount,iknowCount,donknowCount,skipCount,percentageObtain"},"flashcardId='"+testId+"'", null, null, null,"startDttm DESC");
        return  c;
    }

    public int getFlashAttemptNum(String testId){
        int count=0;
        Cursor c =db.query("flashcard_attempt", new String[] {"attemptNumber,studentId,enrollmentId,courseId,subjectId,paperId"},"flashcardId='"+testId+"'", null, null, null,null);
        count=c.getCount();
        return count;
    }

    public Cursor getFlashRawData(String testId){
        String query ="SELECT COUNT(*) as attemptcount,MIN(percentageObtain) as minscore,MAX(percentageObtain) as maxscore,AVG(percentageObtain) as avgscore FROM "+" flashcard_attempt"+" WHERE flashcardId ='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public long insertPtuSection(int seckey,String testid,int secseq,String secid,String spid,String ssid,String scid,int minques,int maxques,int totgroups,int nogroups,String sstatus,String sname){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Ptu_section_key",seckey);
        cv.put("Ptu_ID",testid);
        cv.put("Ptu_section_sequence",secseq);
        cv.put("Ptu_section_ID",secid);
        cv.put("Ptu_section_paper_ID",spid);
        cv.put("Ptu_section_subject_ID",ssid);
        cv.put("Ptu_section_course_ID",scid);
        cv.put("Ptu_section_min_questions",minques);
        cv.put("Ptu_section_max_questions",maxques);
        cv.put("Ptu_sec_tot_groups",totgroups);
        cv.put("Ptu_sec_no_groups",nogroups);
        cv.put("Ptu_section_status",sstatus);
        cv.put("Ptu_section_name",sname);
        insertFlag = db.insert("ptu_sections",null, cv);
        return insertFlag;
    }

    public long deletePtuSections(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("ptu_sections", "Ptu_ID='"+testid+"'", null);
        return  deleteFlag;
    }

    public int getSectionMaxQCount(String testId,String sectionId){
        int count=0;
        Cursor c =db.query("ptu_sections", new String[] {"Ptu_section_sequence,Ptu_section_course_ID,Ptu_section_paper_ID,Ptu_section_min_questions"},"Ptu_ID='"+testId+"' and Ptu_section_ID='"+sectionId+"'", null, null, null,null);
        while (c.moveToNext()) {
            count=c.getInt(c.getColumnIndex("Ptu_section_min_questions"));
        }
        return  count;
    }

    public long insertStudent(int skey,String sid,String sname,String sgender,String sedu,String sdob,String saddress1,String saddress2,String scity,String sstate,String scountry,String smobile,String semail,String spassword,String smacid,String sstatus,String screateby,String screateddatetime){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("StudentKey",skey);
        cv.put("StudentID",sid);
        cv.put("Student_Name",sname);
        cv.put("Student_gender",sgender);
        cv.put("Student_Education",sedu);
        cv.put("Student_DOB",sdob);
        cv.put("Student_Address01",saddress1);
        cv.put("Student_Address02",saddress2);
        cv.put("Student_City",scity);
        cv.put("Student_State",sstate);
        cv.put("Student_Country",scountry);
        cv.put("Student_Mobile",smobile);
        cv.put("Student_email",semail);
        cv.put("Student_password",spassword);
        cv.put("Student_mac_id",smacid);
        cv.put("Student_Status",sstatus);
        cv.put("Student_created_by",screateby);
        cv.put("Student_created_DtTm",screateddatetime);
        insertFlag = db.insert("student_master",null, cv);
        return insertFlag;
    }

    public long checkStudent(int studentkey){
        long returnrows=0;
        Cursor c =db.query("student_master", new String[] {"StudentID,Student_Name,Student_Mobile,Student_email"},"StudentKey='"+studentkey+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public Cursor checkStudent(String number){
        Cursor c =db.query("student_master", new String[] {"StudentKey,StudentID,Student_Name,Student_email,Student_password"},"Student_Mobile='"+number+"'", null, null, null,null);
        return c;
    }

    public long insertEnrollment(int ekey,String eid,String eorg,String esid,String ebid,String ecid,String ebstartdate,String ebenddate,String edevid,String edate,String estatus){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Enroll_key",ekey);
        cv.put("Enroll_ID",eid);
        cv.put("Enroll_org_id",eorg);
        cv.put("Enroll_Student_ID",esid);
        cv.put("Enroll_batch_ID",ebid);
        cv.put("Enroll_course_ID",ecid);
        cv.put("Enroll_batch_start_Dt",ebstartdate);
        cv.put("Enroll_batch_end_Dt",ebenddate);
        cv.put("Enroll_Device_ID",edevid);
        cv.put("Enroll_Date",edate);
        cv.put("Enroll_Status",estatus);
        insertFlag = db.insert("enrollments",null, cv);
        return insertFlag;
    }

    public long checkEnrollment(int enrollkey){
        long returnrows=0;
        Cursor c =db.query("enrollments", new String[] {"Enroll_ID,Enroll_Student_ID,Enroll_batch_ID,Enroll_course_ID"},"Enroll_key='"+enrollkey+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public ArrayList<SingleEnrollment> getStudentEnrolls(){
        ArrayList<SingleEnrollment> enrollList=new ArrayList<>();
        Cursor c =db.query("enrollments", new String[] {"Enroll_ID,Enroll_Student_ID,Enroll_batch_ID,Enroll_course_ID"},null, null, null, null,null);
        while (c.moveToNext()) {
            enrollList.add(new SingleEnrollment(c.getString(c.getColumnIndex("Enroll_ID")),c.getString(c.getColumnIndex("Enroll_course_ID"))));
        }
        return enrollList;
    }

    public long deleteAllEnrollments(){
        long deleteFlag=0;
        deleteFlag=db.delete("enrollments", null, null);
        return  deleteFlag;
    }

    public long insertSubject(int skey,String scid,String ssid,String sname,String ssname,int sseqno,String stype,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Subject_key",skey);
        cv.put("Course_ID",scid);
        cv.put("Subject_ID",ssid);
        cv.put("Subject_Name",sname);
        cv.put("Subject_ShortName",ssname);
        cv.put("Subject_Seq_no",sseqno);
        cv.put("Subject_Type",stype);
        cv.put("Subject_status",status);
        insertFlag = db.insert("subjects",null, cv);
        return insertFlag;
    }

    public long deleteAllSubjects(){
        long deleteFlag=0;
        deleteFlag=db.delete("subjects", null, null);
        return  deleteFlag;
    }

    public long insertPaper(int pkey,String pid,String pseqno,String psid,String pcid,String pname,String psname,String pminmarks,String pmaxmarks){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Paper_Key",pkey);
        cv.put("Paper_ID",pid);
        cv.put("Paper_Seq_no",pseqno);
        cv.put("Subject_ID",psid);
        cv.put("Course_ID",pcid);
        cv.put("Paper_Name",pname);
        cv.put("Paper_Short_Name",psname);
        cv.put("Paper_Min_Pass_Marks",pminmarks);
        cv.put("Paper_Max_Marks",pmaxmarks);
        insertFlag = db.insert("papers",null, cv);
        return insertFlag;
    }

    public long checkPaper(int paperkey){
        long returnrows=0;
        Cursor c =db.query("papers", new String[] {"Paper_ID,Subject_ID,Course_ID,Paper_Name"},"Paper_Key='"+paperkey+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public Cursor getStudentPapers(){
        Cursor c =db.query("papers", new String[] {"Paper_ID,Paper_Name"},null, null, null, null,null);
        return c;
    }

    public long deleteAllPapers(){
        long deleteFlag=0;
        deleteFlag=db.delete("papers", null, null);
        return  deleteFlag;
    }

    public long insertPractiseTest(int tkey,String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_key",tkey);
        cv.put("sptu_org_id",torgid);
        cv.put("sptu_entroll_id",tenrollid);
        cv.put("sptu_student_ID",tstudentid);
        cv.put("sptu_batch",tbatch);
        cv.put("sptu_ID",tid);
        cv.put("sptu_paper_ID",tpid);
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_start_date",tstartdate);
        cv.put("sptu_end_date",tenddate);
        cv.put("sptu_dwnld_status",tdwdstatus);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",ttotalmarks);
        cv.put("stpu_min_marks",tminmarks);
        cv.put("sptu_max_marks",tmaxmarks);
        insertFlag = db.insert("sptu_student",null, cv);
        return insertFlag;
    }

    public long updateTest(String tID,String tsid,String tcid,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks,Double avgscore,Double percentage){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",ttotalmarks);
        cv.put("stpu_min_marks",tminmarks);
        cv.put("sptu_max_marks",tmaxmarks);
        cv.put("sptu_avg_marks",avgscore);
        updateFlag=db.update("sptu_student", cv,"sptu_ID='"+tID+"'",null);
        return  updateFlag;
    }

    public long updatePractiseTestData(String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_org_id",torgid);
        cv.put("sptu_entroll_id",tenrollid);
        cv.put("sptu_student_ID",tstudentid);
        cv.put("sptu_batch",tbatch);
        cv.put("sptu_paper_ID",tpid);
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_start_date",tstartdate);
        cv.put("sptu_end_date",tenddate);
        cv.put("sptu_dwnld_status",tdwdstatus);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",ttotalmarks);
        cv.put("stpu_min_marks",tminmarks);
        cv.put("sptu_max_marks",tmaxmarks);
        updateFlag = db.update("sptu_student", cv,"sptu_ID='"+tid+"'",null);
        return updateFlag;
    }

    public long updateTestStatus(String testid,int attemptcount,Double minscore,Double maxscore,Double avgscore,String Dttm,Double lastAttemptscore){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptuflash_attempts",attemptcount);
        cv.put("min_flashScore",minscore);
        cv.put("max_flashScore",maxscore);
        cv.put("avg_flashScore",avgscore);
        cv.put("lastAttemptDttm",Dttm);
        cv.put("lastAttemptScore",lastAttemptscore);
        updateFlag=db.update("sptu_student", cv,"sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long checkTest(int testkey){
        long returnrows=0;
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_key='"+testkey+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public Cursor getSingleStudentTests(String testId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }
    public Cursor getStudentTests(){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},null, null, null, null,null);
        return c;
    }

    public Cursor getStudentTestsByEnroll(String enrollId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_entroll_id='"+enrollId+"'", null, null, null,null);
        return c;
    }

    public Cursor getSingleTestData(String testId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public Cursor getTestFlashData(String testId){
        Cursor c =db.query("sptu_student", new String[] {"min_flashScore,max_flashScore,avg_flashScore"},"sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public long deleteAllTests(){
        long deleteFlag=0;
        deleteFlag=db.delete("sptu_student", null, null);
        return  deleteFlag;
    }

    public long updatePTestStatus(String testid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_dwnld_status", status);
        updateFlag=db.update("sptu_student", cv, "sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long insertQuesGroup(int qbkey,String qbid,String tid,String qbmtype,String qbmfile,String qbtext,int noofques,int pickupcount,String status,String createby,String createdttm,String modby,String moddttm,String grouptype){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("qbg_key",qbkey);
        cv.put("qbg_ID",qbid);
        cv.put("testId",tid);
        cv.put("qbg_media_type",qbmtype);
        cv.put("qbg_media_file",qbmfile);
        cv.put("qbg_text",qbtext);
        cv.put("qbg_no_questions",noofques);
        cv.put("qbg_pickup_count",pickupcount);
        cv.put("qbg_status",status);
        cv.put("qbg_created_by",createby);
        cv.put("qbg_created_dttm",createdttm);
        cv.put("qbg_mod_by",modby);
        cv.put("qbg_mod_dttm",moddttm);
        cv.put("qbg_type",grouptype);
        insertFlag = db.insert("qb_group",null, cv);
        return insertFlag;
    }

    public long deleteTestGroups(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("qb_group", "testId='"+testid+"'", null);
        return  deleteFlag;
    }

    public int getGroupQPickCount(String groupId){
        int count=0;
        Cursor c =db.query("qb_group", new String[] {"qbg_pickup_count"},"qbg_ID='"+groupId+"'", null, null, null,null);
        while (c.moveToNext()) {
            count=c.getInt(c.getColumnIndex("qbg_pickup_count"));
        }
        return count;
    }

    public long insertQuesConfig(int qconkey,String cid,String sid,String pid,String tid,String catid,String scatid,int availcount,int pickupcount,int mincount,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("ques_configkey",qconkey);
        cv.put("courseId",cid);
        cv.put("subjectId",sid);
        cv.put("paperId",pid);
        cv.put("testId",tid);
        cv.put("categoryId",catid);
        cv.put("subcategoryId",scatid);
        cv.put("avail_count",availcount);
        cv.put("pickup_count",pickupcount);
        cv.put("min_pickup_count",mincount);
        cv.put("ques_configstatus",status);
        insertFlag = db.insert("ques_config",null, cv);
        return insertFlag;
    }

    public long deleteQuesConfig(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("ques_config", "testId='"+testid+"'",null);
        return  deleteFlag;
    }

    public ArrayList<SingleSubcatConfig> getSubcatData(String testId){
        ArrayList<SingleSubcatConfig> subcatList=new ArrayList<>();
        Cursor c =db.query("ques_config", new String[] {"subcategoryId,pickup_count,min_pickup_count"},"testId='"+testId+"'", null, null, null,null);
        while (c.moveToNext()) {
            subcatList.add(new SingleSubcatConfig(c.getString(c.getColumnIndex("subcategoryId")),c.getInt(c.getColumnIndex("pickup_count")),c.getInt(c.getColumnIndex("min_pickup_count"))));
        }
        return subcatList;
    }

    public long insertGroupConfig(int gconfigkey,String cid,String sid,String pid,String tid,String secid,String gtype,int availcount,int pickupcount,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("groupques_configKey",gconfigkey);
        cv.put("courseId",cid);
        cv.put("subjectId",sid);
        cv.put("paperId",pid);
        cv.put("testId",tid);
        cv.put("sectionId",secid);
        cv.put("groupType",gtype);
        cv.put("groupavail_count",availcount);
        cv.put("grouppickup_count",pickupcount);
        cv.put("groupques_configstatus",status);
        insertFlag = db.insert("groupques_config",null, cv);
        return insertFlag;
    }

    public long deleteGroupsConfig(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("groupques_config", "testId='"+testid+"'", null);
        return  deleteFlag;
    }

    public int getCompGroupCount(String testId,String sectionId,String type){
        int count=0;
        Cursor c =db.query("groupques_config", new String[] {"grouppickup_count"},"testId='"+testId+"' and sectionId='"+sectionId+"' and groupType='"+type+"'", null, null, null,null);
        while (c.moveToNext()) {
            count=c.getInt(c.getColumnIndex("grouppickup_count"));
        }
        return count;
    }

    public int getCloseGroupCount(String testId,String sectionId,String type){
        int count=0;
        Cursor c =db.query("groupques_config", new String[] {"grouppickup_count"},"testId='"+testId+"' and sectionId='"+sectionId+"' and groupType='"+type+"'", null, null, null,null);
        while (c.moveToNext()) {
            count=c.getInt(c.getColumnIndex("grouppickup_count"));
        }
        return count;
    }

    public long updatePrefAdvt(String orgId, String producer_id,String caption, String description, byte[] image, String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("image", image);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);
        updateFlag=db.update("advt_pref_producer", cv, "producer_id='"+producer_id+"'", null);
        return  updateFlag;
    }

    public long deleteAllCategories(){
        long deleteFlag=0;
        deleteFlag=db.delete("category_table", null, null);
        return  deleteFlag;
    }

    public ArrayList<String> getActivePref(String status,String mobileno){
        ArrayList<String> AdvtprefList = new ArrayList<>();

        Cursor c =db.query("preferences_table", new String[] {"subCategory"},"status='"+status+"' and userId='"+mobileno+"'", null, null, null,null);
        while (c.moveToNext()) {

            AdvtprefList.add(c.getString(c.getColumnIndex("subCategory")));
        }
        return AdvtprefList;
    }


    public long getActivePrefCount(String status,String mobileno){
        long count=0;
        String countQuery = "select * from preferences_table where userId='"+mobileno+"' and status='"+status+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public ArrayList<String> getAllPref(String mobileno){
        ArrayList<String> AdvtprefList = new ArrayList<>();

        Cursor c =db.query("preferences_table", new String[] {"subCategory"},"userId='"+mobileno+"'", null, null, null,null);
        while (c.moveToNext()) {

            AdvtprefList.add(c.getString(c.getColumnIndex("subCategory")));
        }
        return AdvtprefList;
    }
    public long InsertQuestion(String testId,int attemptId,String qId,String qSeq, double maxMarks,double negMarks,double marksObtained,double negApplied, int option,String status,String oSeq,String flag){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Test_ID", testId);
        cv.put("Attempt_ID", attemptId);
        cv.put("Question_ID", qId);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Negative_Marks",negMarks );
        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_Status",status );
        cv.put("Question_Option_Sequence",oSeq );
        cv.put("Option_Answer_Flag",flag );

        Log.e("DB_Insert:",status);
        insertFlag = db.insert("attempt_data",null, cv);

        return insertFlag;
    }

    public long UpdateQuestion(String testId,int attemptId,String qId,String qSeq, double maxMarks,double negMarks,double marksObtained,double negApplied, int option,String status,String oSeq,String flag){

        long updateFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Test_ID", testId);
        cv.put("Attempt_ID", attemptId);
        cv.put("Question_ID", qId);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Negative_Marks",negMarks );
        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_Status",status );
        cv.put("Question_Option_Sequence",oSeq );
        cv.put("Option_Answer_Flag",flag );
        Log.e("DB_Update:",status);
        updateFlag = db.update("attempt_data",cv,"Question_ID='"+qId+"'",null);

        return updateFlag;
    }

    public Boolean CheckQuestion(String qId){
        Boolean value = false;
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Question_ID ='"+qId+"'";
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
            value = true;
        else
            value = false;
        cursor.close();
        return value;
    }

    public ArrayList<Integer> getQuestion(){
        ArrayList<Integer> QuestionList = new ArrayList<>();
//        String query ="SELECT * FROM attempt_data";
        Cursor c =db.query("attempt_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},null, null, null, null,null);
//        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                QuestionList.add(c.getInt(c.getColumnIndex("Question_ID")));
            }
        }
        c.close();
        return QuestionList;
    }

    public ArrayList<Integer> getOptions(){
        ArrayList<Integer> OptionList = new ArrayList<>();
        Cursor c =db.query("attempt_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},null, null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                OptionList.add(c.getInt(c.getColumnIndex("Question_Option")));
            }
        }
        c.close();
        return OptionList;
    }

    public ArrayList<String> getQuestionStatus(){
        ArrayList<String> StatusList = new ArrayList<>();
        Cursor c =db.query("attempt_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},null, null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                StatusList.add(c.getString(c.getColumnIndex("Question_Status")));
            }
        }
        c.close();
        return StatusList;
    }


    public ArrayList<Integer> getCorrectOptions(){
        ArrayList<Integer> CorrectList = new ArrayList<>();
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES'";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                CorrectList.add(c.getInt(c.getColumnIndex("Question_Option")));
            }
        }
        c.close();
        return CorrectList;
    }

    public int getCorrectOptionsCount(){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED'";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getCorrectSum(){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumPos FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumPos"));
        c.close();
        return sum;
    }

    public int getWrongSum(){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumNeg FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status <> 'SKIPPED'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumNeg"));
        c.close();
        return sum;
    }

    public int getWrongOptionsCount(){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') ";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getPosition(String qId){
        int value = -1;
        try {
            String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Question_ID ='"+qId+"'";
            db=this.getWritableDatabase();
            Cursor cursor=db.rawQuery(query,null);
            cursor.moveToFirst();
            value = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getQuestionCount(){
        int count=0;
        String countQuery = "select * from attempt_data";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getQuestionAttempted(){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'ATTEMPTED'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getQuestionSkipped(){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'SKIPPED'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        return count;

    }

    public int getQustionBookmarked(){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'BOOKMARKED'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getQustionNotAttempted(){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'NOT_ATTEMPTED'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public void Destroy(String table){
        db.execSQL("delete from "+table);
//        db.execSQL("TRUNCATE table " +table);
    }

    public long InsertAttempt( String testID,int status, int aScore, int attempted, int skipped, int bookmarked, int unattempted, int aperc,long aTime,int index,int pos){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Attempt_Test_ID", testID);
        cv.put("Attempt_Status", status);
        cv.put("Attempt_Confirmed", attempted);
        cv.put("Attempt_Skipped", skipped);
        cv.put("Attempt_Bookmarked",bookmarked);
        cv.put("Attempt_UnAttempted", unattempted);
        cv.put("Attempt_Score",aScore );
        cv.put("Attempt_Percentage",aperc );
        cv.put("Attempt_RemainingTime",aTime );
        cv.put("Attempt_LastQuestion",index );
        cv.put("Attempt_LastSection",pos );
        insertFlag = db.insert("attempt_list",null, cv);
        return insertFlag;
    }


    public Cursor getTestRawData(String testId){
        String query ="SELECT COUNT(*) as attemptcount,MIN(Attempt_Percentage) as minscore,MAX(Attempt_Percentage) as maxscore,AVG(Attempt_Percentage) as avgscore FROM "+" attempt_list"+" WHERE Attempt_Test_ID ='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public long UpdateAttempt(int aID,String testID,int status, int aScore, int attempted, int skipped, int bookmarked, int unattempted, int aperc,long aTime,int index,int pos){

        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Attempt_Status", status);
        cv.put("Attempt_Test_ID", testID);
        cv.put("Attempt_Confirmed", attempted);
        cv.put("Attempt_Skipped", skipped);
        cv.put("Attempt_Bookmarked",bookmarked);
        cv.put("Attempt_UnAttempted", unattempted);
        cv.put("Attempt_Score",aScore );
        cv.put("Attempt_Percentage",aperc );
        cv.put("Attempt_RemainingTime",aTime );
        cv.put("Attempt_LastQuestion",index );
        cv.put("Attempt_LastSection",pos );

        updateFlag = db.update("attempt_list",cv,"Attempt_ID='"+aID+"'",null);

        return updateFlag;
    }

    public Cursor getTestAttemptData(String testId){
        Cursor c =db.query("attempt_list", new String[] {"Attempt_ID,Attempt_Test_ID,Attempt_Status,Attempt_Confirmed,Attempt_Skipped,Attempt_Bookmarked,Attempt_UnAttempted,Attempt_Score,Attempt_Percentage"},"Attempt_Test_ID='"+testId+" and Attempt_Status = 2'", null, null, null,"Attempt_ID DESC");
        return  c;
    }
    public int getAttempCount(){
        int count=0;
        String countQuery = "select * from attempt_list";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public int DeleteAttempt(int aID){
        int count=0;
        String countQuery = "DELETE FROM attempt_list WHERE Attempt_ID='"+aID+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public int getLastAttempt(){
        int attempt_id = 0;
        String query = "select max(Attempt_ID) as last from attempt_list";
        Cursor c = db.rawQuery(query,null);
        if (c.getCount() >0) {
            attempt_id = c.getInt(c.getColumnIndex("last"));
        } else {
            attempt_id = 0;
        }
        return attempt_id;
    }

    public Cursor getAttempt(int aID){
        int status =0 ;
        String countQuery = "select * from attempt_list where Attempt_ID = '"+aID+"'";
        Cursor c =db.rawQuery(countQuery,null);
        return c;
    }



}
