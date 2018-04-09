package com.digywood.tms.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
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
                "  `sptu_created_by` text DEFAULT NULL,\n" +
                "  `sptu_created_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_mod_by` text DEFAULT NULL,\n" +
                "  `sptu_mod_dttm` datetime DEFAULT NULL)";
        db.execSQL(tbl_sptu_student);
        String AttemptData=" CREATE TABLE `attempt_data` (\n"+
                "   `Question_ID` varchar(15),\n" +
                "   `Question_Seq_No` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Max_Marks` int(15) DEFAULT NULL,\n"+
                "   `Question_Option` int(15) DEFAULT NULL,\n"+
                "   PRIMARY KEY (`Question_ID`)\n"+
                ")";
        db.execSQL(AttemptData);

        String tblqbgroup="CREATE TABLE `qb_group` (\n" +
                "  `qbg_key` integer PRIMARY KEY,`qbg_ID` text DEFAULT NULL,`testId` text DEFAULT NULL,\n" +
                "  `qbg_media_type` text DEFAULT NULL,`qbg_media_file` text DEFAULT NULL,\n" +
                "  `qbg_text` text DEFAULT NULL,`qbg_no_questions` int(4),`qbg_pickup_count` int(4),\n" +
                "  `qbg_status` text DEFAULT NULL,`qbg_created_by` text DEFAULT NULL,\n" +
                "  `qbg_created_dttm` datetime DEFAULT NULL,\n" +
                "  `qbg_mod_by` varchar(20) DEFAULT NULL,`qbg_mod_dttm` datetime DEFAULT NULL)";
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

//        String tblCategoryCheck="CREATE TABLE IF NOT EXISTS category_check_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT, category text, subCategory text, status text)";
//        db.execSQL(tblCategoryCheck);
//
//        String tblUserIntrests="CREATE TABLE IF NOT EXISTS user_interests(seqId INTEGER PRIMARY KEY,userId text,advtId integer(10),description text,status text)";
//        db.execSQL(tblUserIntrests);
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

    public long insertTest(int tkey,String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks){
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

    public long checkTest(int testkey){
        long returnrows=0;
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_key='"+testkey+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public Cursor getStudentTests(){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},null, null, null, null,null);
        return c;
    }

    public long deleteAllTests(){
        long deleteFlag=0;
        deleteFlag=db.delete("sptu_student", null, null);
        return  deleteFlag;
    }

    public long updateTestStatus(String testid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_dwnld_status", status);
        updateFlag=db.update("sptu_student", cv, "sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long insertQuesGroup(int qbkey,String qbid,String tid,String qbmtype,String qbmfile,String qbtext,String noofques,String pickupcount,String status,String createby,String createdttm,String modby,String moddttm){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("qbg_key",qbkey);
        cv.put("qbg_ID",qbid);
        cv.put("testId",qbid);
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
        insertFlag = db.insert("qb_group",null, cv);
        return insertFlag;
    }

    public long deleteTestGroups(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("qb_group", "testId='"+testid+"'", null);
        return  deleteFlag;
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
    public long InsertQuestion(String qId,String qSeq, int maxMarks, int option){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Question_ID", qId);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Option",option );

        insertFlag = db.insert("attempt_data",null, cv);

        return insertFlag;
    }
    public long UpdateQuestion(String qId,String qSeq, int maxMarks, int option){

        long updateFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Question_ID", qId);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Option",option );

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

        return value;
    }
    public ArrayList<Integer> getQuestion(){
        ArrayList<Integer> QuestionList = new ArrayList<>();
//        String query ="SELECT * FROM attempt_data";
        Cursor c =db.query("attempt_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option"},null, null, null, null,null);
//        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                QuestionList.add(c.getInt(c.getColumnIndex("Question_Option")));
            }
        }

        return QuestionList;
    }
    public int getPosition(String qId){
        int value = 0;
        try {
            String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Question_ID ='"+qId+"'";
            db=this.getWritableDatabase();
            Cursor cursor=db.rawQuery(query,null);
            cursor.moveToFirst();
            value = cursor.getInt(0);
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
        return count;

    }
    public void Destroy(String table){
        db.execSQL("delete from "+table);
//        db.execSQL("TRUNCATE table " +table);
    }
}
