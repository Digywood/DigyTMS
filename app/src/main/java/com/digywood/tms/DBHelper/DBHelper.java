package com.digywood.tms.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.digywood.tms.Pojo.Lesson;
import com.digywood.tms.Pojo.LessonUnit;
import com.digywood.tms.Pojo.LessonUnitPoint;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.Pojo.SingleOrganization;
import com.digywood.tms.Pojo.SingleSubcatConfig;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;
    private static final int DATABASE_VERSION =5;

    public DBHelper(Context c)
    {
        super(c,"digytmsDB",null,DATABASE_VERSION);
        this.context=c;
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tbl_student_master="CREATE TABLE IF NOT EXISTS `student_master` (`StudentKey` integer PRIMARY KEY,`StudentID` text DEFAULT NULL,`Student_Name` text DEFAULT NULL,"+
                "`Student_gender` text DEFAULT NULL,`Student_Education` text DEFAULT NULL,`Student_DOB` date DEFAULT NULL,`Student_Address01` text DEFAULT NULL,"+
                "`Student_Address02` text DEFAULT NULL,`Student_City` text DEFAULT NULL,`Student_State` text DEFAULT NULL,"+
                "`Student_Country` text DEFAULT NULL,`Student_Mobile` text DEFAULT NULL,`Student_email` text DEFAULT NULL,"+
                "`Student_password` text DEFAULT NULL,`Student_mac_id` text DEFAULT NULL,`Student_Status` text DEFAULT NULL,"+
                "`Student_created_by` text DEFAULT NULL,`Student_created_DtTm` datetime DEFAULT NULL,`Student_mod_by` text DEFAULT NULL,"+
                "`Student_mod_DtTm` datetime DEFAULT NULL)";
        db.execSQL(tbl_student_master);

        String tbl_enrollments="CREATE TABLE IF NOT EXISTS `enrollments` (\n" +
                "  `Enroll_key` integer PRIMARY KEY,\n" +
                "  `Enroll_ID` text DEFAULT NULL,\n" +
                "  `Enroll_org_id` text DEFAULT NULL,\n" +
                "  `Enroll_Student_ID` text DEFAULT NULL,\n" +
                "  `Enroll_branch_ID` varchar(20) DEFAULT NULL, \n"+
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
                "  `Enroll_mod_dttm` datetime DEFAULT NULL,\n " +
                "  `enroll_Fee_Currency` text DEFAULT NULL,\n" +
                "  `enroll_Fee_Amount` text DEFAULT NULL,\n" +
                "  `enroll_Fee_tax_percentage` text DEFAULT NULL,\n" +
                "  `enroll_Total_Amount` text DEFAULT NULL,\n" +
                "  `enroll_Activation_Key` text DEFAULT NULL,\n" +
                "  `enroll_Activation_Date` date DEFAULT NULL,\n" +
                "  `enroll_Request_Date` date DEFAULT NULL,\n" +
                "  `enroll_ActivatedBy` text DEFAULT NULL,\n" +
                "  `enroll_Refdetails` text DEFAULT NULL,\n" +
                "  `remarks1` text DEFAULT NULL,\n" +
                "  `remarks2` text DEFAULT NULL)";
        db.execSQL(tbl_enrollments);

        String tbl_courses="CREATE TABLE `course_master` (\n" +
                "  `Course_Key` integer PRIMARY KEY,\n" +
                "  `Course_ID` text DEFAULT NULL,\n" +
                "  `Course_Name` text DEFAULT NULL,\n" +
                "  `Course_Short_name` text DEFAULT NULL,\n" +
                "  `Course_Type` text DEFAULT NULL,\n" +
                "  `Course_Category` text DEFAULT NULL,\n" +
                "  `Course_sub_category` text DEFAULT NULL,\n" +
                "  `Course_duration_uom` text DEFAULT NULL,\n" +
                "  `Cousre_Duration_min` text DEFAULT NULL,\n" +
                "  `Course_Duration_Max` text DEFAULT NULL,\n" +
                "  `Curese_buffer_01` text DEFAULT NULL,\n" +
                "  `Curese_buffer_02` text DEFAULT NULL,\n" +
                "  `Curese_buffer_03` text DEFAULT NULL,\n" +
                "  `Curese_buffer_04` text DEFAULT NULL,\n" +
                "  `Curese_buffer_05` text DEFAULT NULL,\n" +
                "  `Course_Status` text DEFAULT NULL,\n" +
                "  `Course_created_by` text DEFAULT NULL,\n" +
                "  `Course_created_DtTm` datetime DEFAULT NULL,\n" +
                "  `Course_mod_by` text DEFAULT NULL,\n" +
                "  `Course_mod_DtTm` datetime DEFAULT NULL)";
        db.execSQL(tbl_courses);

        String tbl_subjects="CREATE TABLE IF NOT EXISTS `subjects` (\n" +
                "  `Subject_key` integer PRIMARY KEY,\n" +
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
                "  `Paper_Key` integer PRIMARY KEY,\n" +
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
                "  `sptu_key` integer PRIMARY KEY,\n" +
                "  `sptu_org_id` text DEFAULT NULL,\n" +
                "  `sptu_entroll_id` text DEFAULT NULL,\n" +
                "  `sptu_student_ID` text DEFAULT NULL,\n" +
                "  `sptu_batch` text DEFAULT NULL,\n" +
                "  `sptu_ID` text DEFAULT NULL,\n" +
                "  `sptu_name` varchar(50) DEFAULT NULL,\n" +
                "  `sptu_paper_ID` text DEFAULT NULL,\n" +
                "  `sptu_subjet_ID` text DEFAULT NULL,\n" +
                "  `sptu_course_id` text DEFAULT NULL,\n" +
                "  `sptu_start_date` date DEFAULT NULL,\n" +
                "  `sptu_end_date` date DEFAULT NULL,\n" +
                "  `sptu_dwnld_start_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_dwnld_completed_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_dwnld_status` text DEFAULT NULL,\n" +
                "  `sptu_status` text DEFAULT NULL,\n" +
                "  `sptu_upld_status` text DEFAULT NULL,\n" +
                "  `sptu_upld_dttm` text DEFAULT NULL,\n" +
                "  `sptu_no_of_questions` integer(5) DEFAULT NULL,\n" +
                "  `sptu_tot_marks` double DEFAULT NULL,\n" +
                "  `sptu_Test_Time` text DEFAULT NULL,\n" +
                "  `sptu_Test_Type` text DEFAULT NULL,\n" +
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
                "  `sptu_mod_dttm` datetime DEFAULT NULL,\n" +
                "  `sptu_sequence` integer )";
        db.execSQL(tbl_sptu_student);

        String tbl_satu_student="CREATE TABLE IF NOT EXISTS `satu_student` (\n" +
                "  `satu_key` integer PRIMARY KEY,\n" +
                "  `satu_org_id` text DEFAULT NULL,\n" +
                "  `satu_branch_id` text DEFAULT NULL,\n" +
                "  `satu_instace_id` varchar(25) DEFAULT NULL,\n" +
                "  `satu_entroll_id` text DEFAULT NULL,\n" +
                "  `satu_student_id` text DEFAULT NULL,\n" +
                "  `satu_batch` text DEFAULT NULL,\n" +
                "  `satu_ID` text DEFAULT NULL,\n" +
                "  `satu_name` text DEFAULT NULL,\n" +
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
                "  `satu_Test_Time` text DEFAULT NULL,\n" +
                "  `satu_Test_Type` text DEFAULT NULL,\n" +
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

        String tbl_test_main="CREATE TABLE IF NOT EXISTS `test_main` (\n" +
                "  `testKey` integer PRIMARY KEY,\n" +
                "  `testId` text DEFAULT NULL,\n" +
                "  `testType` text DEFAULT NULL,\n" +
                "  `test_OrgId` text DEFAULT NULL,\n" +
                "  `test_batchId` text DEFAULT NULL,\n" +
                "  `test_courseId` text DEFAULT NULL,\n" +
                "  `test_paperId` text DEFAULT NULL,\n" +
                "  `test_subjectId` text DEFAULT NULL,\n" +
                "  `minPercentage` double DEFAULT NULL,\n" +
                "  `maxPercentage` double DEFAULT NULL,\n" +
                "  `avgPercentage` double DEFAULT NULL,\n" +
                "  `minAttempts` int(5) DEFAULT NULL,\n" +
                "  `maxAttempts` int(5) DEFAULT NULL,\n" +
                "  `avgAttempts` int(5) DEFAULT NULL,\n" +
                "  `flag` int(2) DEFAULT NULL,\n" +
                "  `createdBy` varchar(45) DEFAULT NULL,\n" +
                "  `createdDttm` varchar(45) DEFAULT NULL,\n" +
                "  `modifiedBy` varchar(45) DEFAULT NULL,\n" +
                "  `modifiedDttm` varchar(45) DEFAULT NULL)";
        db.execSQL(tbl_test_main);

        String AttemptList ="CREATE TABLE `attempt_list` (\n"+
                "   `Attempt_ID` TEXT,\n"+
                "   `Attempt_Test_ID` TEXT,\n"+
                "   `Attempt_enrollId` TEXT DEFAULT NULL,\n"+
                "   `Attempt_studentId` TEXT DEFAULT NULL,\n"+
                "   `Attempt_courseId` TEXT DEFAULT NULL,\n"+
                "   `Attempt_subjectId` TEXT DEFAULT NULL,\n"+
                "   `Attempt_paperId` TEXT DEFAULT NULL,\n"+
                "   `Attempt_Status` int(5) NOT NULL,\n"+
                "   `Attempt_Upload_Status` TEXT NOT NULL,\n"+
                "   `Attempt_RemainingTime` int(5) DEFAULT NULL,\n"+
                "   `Attempt_LastQuestion` int(5) DEFAULT NULL,\n"+
                "   `Attempt_LastSection` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Confirmed` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Skipped` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Bookmarked` int(5) DEFAULT NULL,\n"+
                "   `Attempt_UnAttempted` int(5) DEFAULT NULL,\n"+
                "   `Attempt_Score` double DEFAULT NULL,\n"+
                "   `Attempt_Percentage` double DEFAULT NULL,\n"+
                "   PRIMARY KEY (`Attempt_ID`)\n"+
                ")";
        db.execSQL(AttemptList);

        String AssessmentTestList ="CREATE TABLE `Assessment_list` (\n"+
                "   `Assessment_Test_ID` TEXT PRIMARY KEY,\n"+
                "   `Assessment_Instance_ID` TEXT,\n"+
                "   `Assessment_enrollId` TEXT DEFAULT NULL,\n"+
                "   `Assessment_studentId` TEXT DEFAULT NULL,\n"+
                "   `Assessment_courseId` TEXT DEFAULT NULL,\n"+
                "   `Assessment_subjectId` TEXT DEFAULT NULL,\n"+
                "   `Assessment_paperId` TEXT DEFAULT NULL,\n"+
                "   `Assessment_Status` int(5) NOT NULL,\n"+
                "   `Assessment_Started_dttm` datetime DEFAULT NULL,\n" +
                "   `Assessment_RemainingTime` int(5) DEFAULT NULL,\n"+
                "   `Assessment_LastQuestion` int(5) DEFAULT NULL,\n"+
                "   `Assessment_LastSection` int(5) DEFAULT NULL,\n"+
                "   `Assessment_Confirmed` int(5) DEFAULT NULL,\n"+
                "   `Assessment_Skipped` int(5) DEFAULT NULL,\n"+
                "   `Assessment_Bookmarked` int(5) DEFAULT NULL,\n"+
                "   `Assessment_UnAttempted` int(5) DEFAULT NULL,\n"+
                "   `Assessment_Score` double DEFAULT NULL,\n"+
                "   `Assessment_Percentage` double DEFAULT NULL)";
        db.execSQL(AssessmentTestList);


        String AttemptData=" CREATE TABLE `attempt_data` (\n"+
                "   `Test_ID` varchar(15),\n" +
                "   `Attempt_ID` TEXT,\n"+
                "   `Student_ID` TEXT,\n"+
                "   `Question_ID` varchar(15),\n" +
                "   `Question_Seq_No` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Section` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Category` varchar(15) DEFAULT NULL,\n" +
                "   `Question_SubCategory` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Max_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Marks_Obtained` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Applied` double(15) DEFAULT NULL,\n"+
                "   `Question_Option` int(15) DEFAULT NULL,\n"+
                "   `Question_Status` varchar(20) DEFAULT NULL,\n"+
                "   `Question_Option_Sequence` varchar(20) DEFAULT NULL,\n"+
                "   `Option_Answer_Flag` varchar(15) DEFAULT NULL)";
        db.execSQL(AttemptData);

        String AssessmentData="CREATE TABLE `assessment_data` (\n" +
                "  `key` integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  `StudentId` varchar(20) NOT NULL,\n" +
                "  `Org_ID` varchar(20) DEFAULT NULL,\n" +
                "  `Branch_ID` varchar(20) DEFAULT NULL,\n" +
                "  `Batch_ID` varchar(20) DEFAULT NULL,\n" +
                "  `Test_ID` varchar(20) NOT NULL,\n" +
                "  `Assessment_Instance_ID` varchar(20) DEFAULT NULL,\n" +
                "  `Question_ID` varchar(15) DEFAULT NULL,\n" +
                "  `Question_Key` text DEFAULT NULL,\n" +
                "  `Question_Seq_No` varchar(15) DEFAULT NULL,\n" +
                "  `Question_Section` varchar(15) DEFAULT NULL,\n" +
                "  `Question_Category` varchar(15) DEFAULT NULL,\n" +
                "  `Question_SubCategory` varchar(15) DEFAULT NULL,\n" +
                "  `Question_Max_Marks` double DEFAULT NULL,\n" +
                "  `Question_Negative_Marks` double DEFAULT NULL,\n" +
                "  `Question_Marks_Obtained` double DEFAULT NULL,\n" +
                "  `Question_Negative_Applied` double DEFAULT NULL,\n" +
                "  `Question_Option` int(10) DEFAULT NULL,\n" +
                "  `Question_OptionCount` int(5) DEFAULT NULL,\n" +
                "  `Question_Status` varchar(20) DEFAULT NULL,\n" +
                "  `Question_Upload_Status` varchar(20) DEFAULT NULL,\n" +
                "  `Question_Option_Sequence` varchar(20) DEFAULT NULL,\n" +
                "  `Option_Answer_Flag` varchar(20) DEFAULT NULL\n" +
                ")";
        db.execSQL(AssessmentData);

        String AttemptCategory="CREATE TABLE `attempt_category`(\n"+
                "   `Attempt_ID` INTEGER,\n"+
                "   `Attempt_Category` varchar(15),\n" +
                "   `Attempt_Subcategory` varchar(15),\n" +
                "   `Question_Category` varchar(15) DEFAULT NULL,\n" +
                "   `Question_SubCategory` varchar(15) DEFAULT NULL,\n" +
                "   `Question_Max_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Marks` double(15) DEFAULT NULL,\n"+
                "   `Question_Marks_Obtained` double(15) DEFAULT NULL,\n"+
                "   `Question_Negative_Applied` double(15) DEFAULT NULL,\n"+
                "   `Question_Option` int(15) DEFAULT NULL,\n"+
                "   `Question_Status` varchar(20) DEFAULT NULL,\n"+
                "   `Question_Option_Sequence` varchar(20) DEFAULT NULL,\n"+
                "   `Option_Answer_Flag` varchar(15) DEFAULT NULL"+
                ")";
        db.execSQL(AttemptCategory);

        String FlashAttemptTable="CREATE TABLE `flashcard_attempt` (\n" +
                "  `flashcard_attemptKey` integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  `flashUID` text DEFAULT NULL,\n" +
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


        /*String tblatusections="CREATE TABLE `atu_sections` (\n" +
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
        db.execSQL(tblatusections);*/
        String tbl_localservers="CREATE TABLE `local_servers` (\n" +
                "  `serverKey` integer PRIMARY KEY,\n" +
                "  `orgId` text DEFAULT NULL,\n" +
                "  `branchId` text DEFAULT NULL,\n" +
                "  `serverName` text DEFAULT NULL,\n" +
                "  `serverId` text DEFAULT NULL,\n" +
                "  `status` text NOT NULL,\n" +
                "  `createdBy` text DEFAULT NULL,\n" +
                "  `createdDttm` text DEFAULT NULL,\n" +
                "  `modifiedBy` text DEFAULT NULL,\n" +
                "  `modifiedDttm` text DEFAULT NULL)";
        db.execSQL(tbl_localservers);

        String tbl_lession_master="CREATE TABLE `lesson_master` (\n" +
                "  `tms_lms_lesson_key` integer PRIMARY KEY,\n" +
                "  `tms_lms_lesson_id` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_seq_number` text DEFAULT NULL,\n" +
                "  `tms_lms_course_id` text DEFAULT NULL,\n" +
                "  `tms_lms_subject_id` text DEFAULT NULL,\n" +
                "  `tms_lms_paper_id` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_name` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_lang_code` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_long_name` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_short_name` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_medium` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_remark_01` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_remark_02` text DEFAULT NULL,\n" +
                "  `tms_lms_lesson_status` text DEFAULT NULL,\n" +
                "  `created_by` text DEFAULT NULL,\n" +
                "  `created_date_time` datetime DEFAULT NULL,\n" +
                "  `mod_by` text DEFAULT NULL,\n" +
                "  `mod_date_time` datetime DEFAULT NULL)";
        db.execSQL(tbl_lession_master);

        String tbl_lesson_unit_master ="CREATE TABLE `lesson_unit_master` (\n" +
                "  `tms_lu_key` integer PRIMARY KEY,\n" +
                "  `tms_lu_id` text DEFAULT NULL,\n" +
                "  `tms_lu_seq_num` text DEFAULT NULL,\n" +
                "  `tms_lesson_id` text DEFAULT NULL,\n" +
                "  `tms_lu_course_id` text DEFAULT NULL,\n" +
                "  `tms_lu_name` text DEFAULT NULL,\n" +
                "  `tms_lu_lang_code` text DEFAULT NULL,\n" +
                "  `tms_lu_long_name` text DEFAULT NULL,\n" +
                "  `tms_lu_short_name` text DEFAULT NULL,\n" +
                "  `tms_lu_short_lang_name` text DEFAULT NULL,\n" +
                "  `tms_lu_subject_id` text DEFAULT NULL,\n" +
                "  `tms_lu_paper_id` text DEFAULT NULL,\n" +
                "  `tms_lu_remark_01` text DEFAULT NULL,\n" +
                "  `tms_lu_remark_02` text DEFAULT NULL,\n" +
                "  `tms_lu_status` text DEFAULT NULL,\n" +
                "  `created_by` text DEFAULT NULL,\n" +
                "  `creaated_date_time` datetime DEFAULT NULL,\n" +
                "  `mod_by` text DEFAULT NULL,\n" +
                "  `mod_date_time` datetime DEFAULT NULL)";
        db.execSQL(tbl_lesson_unit_master);


        String tbl_lesson_unit_points="CREATE TABLE `lesson_unit_points` (\n" +
                "  `tms_lup_key` integer PRIMARY KEY,\n" +
                "  `tms_lup_id` text DEFAULT NULL,\n" +
                "  `tms_lup_seq_num` integer DEFAULT NULL,\n" +
                "  `tms_lup_name` text DEFAULT NULL,\n" +
                "  `tms_lup_short_name` text DEFAULT NULL,\n" +
                "  `tms_lup_lu_id` text DEFAULT NULL,\n" +
                "  `tms_lup_lesson_id` text DEFAULT NULL,\n" +
                "  `tms_lup_course_id` text DEFAULT NULL,\n" +
                "  `tms_lup_subject_id` text DEFAULT NULL,\n" +
                "  `tms_lup_paper_id` text DEFAULT NULL,\n" +
                "  `tms_lup_chapter_id` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_type` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_ukm` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_ukf` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_usm` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_usf` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_cmm` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_cmf` text DEFAULT NULL,\n" +
                "  `tms_lup_exp_media_gen` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_type` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_ukm` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_ukf` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_usm` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_usf` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_cmm` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_cmf` text DEFAULT NULL,\n" +
                "  `tms_lup_prct_media_gen` text DEFAULT NULL,\n" +
                "  `tms_lup_duration` integer DEFAULT NULL,\n" +
                "  `tms_lup_practice_yes_no` text DEFAULT NULL,\n" +
                "  `tms_lup_test` text DEFAULT NULL,\n" +
                "  `tms_lup_test_id` text DEFAULT NULL,\n" +
                "  `tms_lup_pass_req` text DEFAULT NULL,\n" +
                "  `tms_lup_remark_01` text DEFAULT NULL,\n" +
                "  `tms_lup_remark_02` text DEFAULT NULL,\n" +
                "  `tms_lup_status` text DEFAULT NULL,\n" +
                "  `tms_lup_created_by` text DEFAULT NULL,\n" +
                "  `tms_lup_created_date_time` datetime DEFAULT NULL,\n" +
                "  `tms_lup_mod_by` text DEFAULT NULL,\n" +
                "  `tms_lup_mod_date_time` datetime DEFAULT NULL)";

        db.execSQL(tbl_lesson_unit_points);

        String tbl_org_master="CREATE TABLE `org_master` (\n" +
                "  `Ctr_key` integer PRIMARY KEY,\n" +
                "  `Ctr_orga_id` text DEFAULT NULL,\n" +
                "  `Ctr_Name` integer DEFAULT NULL,\n" +
                "  `Ctr_Short_name` text DEFAULT NULL,\n" +
                "  `Ctr_Address01` text DEFAULT NULL,\n" +
                "  `Ctr_Address02` text DEFAULT NULL,\n" +
                "  `Ctr_Area` text DEFAULT NULL,\n" +
                "  `Ctr_City_Town` text DEFAULT NULL,\n" +
                "  `Ctr_State` text DEFAULT NULL,\n" +
                "  `Ctr_Country` text DEFAULT NULL,\n" +
                "  `Ctr_Person` text DEFAULT NULL,\n" +
                "  `Ctr_Mobile` text DEFAULT NULL,\n" +
                "  `Ctr_email` text DEFAULT NULL,\n" +
                "  `Ctr_logo_file` blob DEFAULT NULL,\n" +
                "  `Ctr_Start_Date` text DEFAULT NULL,\n" +
                "  `Ctr_End_Date` text DEFAULT NULL,\n" +
                "  `Ctr_Type` text DEFAULT NULL,\n" +
                "  `Ctr_category` text DEFAULT NULL,\n" +
                "  `Ctr_sub_category` text DEFAULT NULL,\n" +
                "  `ctr_Status` text DEFAULT NULL,\n" +
                "  `Ctr_created_by` text DEFAULT NULL,\n" +
                "  `Ctr_created_DtTm` text DEFAULT NULL,\n" +
                "  `Ctr_Mod_by` text DEFAULT NULL,\n" +
                "  `Ctr_Mod_DtTm` text DEFAULT NULL)";

        db.execSQL(tbl_org_master);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion)
        {
            case 1:
                db.execSQL("ALTER TABLE sptu_student ADD COLUMN `sptu_Test_Time` text DEFAULT NULL;");
                db.execSQL("ALTER TABLE sptu_student ADD COLUMN `sptu_Test_Type` text DEFAULT NULL;");
                db.execSQL("ALTER TABLE satu_student ADD COLUMN `satu_Test_Time` text DEFAULT NULL;");
                db.execSQL("ALTER TABLE satu_student ADD COLUMN `satu_Test_Type` text DEFAULT NULL;");
                // upgrade logic from version 1 to 2
            case 2:
                db.execSQL("ALTER TABLE sptu_student ADD COLUMN `sptu_sequence` integer  ;");
                // upgrade logic from version 2 to 3
            case 3:

                String tbl_lession_master="CREATE TABLE `lesson_master` (\n" +
                        "  `tms_lms_lesson_key` integer PRIMARY KEY,\n" +
                        "  `tms_lms_lesson_id` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_seq_number` text DEFAULT NULL,\n" +
                        "  `tms_lms_course_id` text DEFAULT NULL,\n" +
                        "  `tms_lms_subject_id` text DEFAULT NULL,\n" +
                        "  `tms_lms_paper_id` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_name` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_lang_code` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_long_name` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_short_name` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_medium` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_remark_01` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_remark_02` text DEFAULT NULL,\n" +
                        "  `tms_lms_lesson_status` text DEFAULT NULL,\n" +
                        "  `created_by` text DEFAULT NULL,\n" +
                        "  `created_date_time` datetime DEFAULT NULL,\n" +
                        "  `mod_by` text DEFAULT NULL,\n" +
                        "  `mod_date_time` datetime DEFAULT NULL)";
                db.execSQL(tbl_lession_master);

                String tbl_lesson_unit_master ="CREATE TABLE `lesson_unit_master` (\n" +
                        "  `tms_lu_key` integer PRIMARY KEY,\n" +
                        "  `tms_lu_id` text DEFAULT NULL,\n" +
                        "  `tms_lu_seq_num` text DEFAULT NULL,\n" +
                        "  `tms_lesson_id` text DEFAULT NULL,\n" +
                        "  `tms_lu_course_id` text DEFAULT NULL,\n" +
                        "  `tms_lu_name` text DEFAULT NULL,\n" +
                        "  `tms_lu_lang_code` text DEFAULT NULL,\n" +
                        "  `tms_lu_long_name` text DEFAULT NULL,\n" +
                        "  `tms_lu_short_name` text DEFAULT NULL,\n" +
                        "  `tms_lu_short_lang_name` text DEFAULT NULL,\n" +
                        "  `tms_lu_subject_id` text DEFAULT NULL,\n" +
                        "  `tms_lu_paper_id` text DEFAULT NULL,\n" +
                        "  `tms_lu_remark_01` text DEFAULT NULL,\n" +
                        "  `tms_lu_remark_02` text DEFAULT NULL,\n" +
                        "  `tms_lu_status` text DEFAULT NULL,\n" +
                        "  `created_by` text DEFAULT NULL,\n" +
                        "  `creaated_date_time` datetime DEFAULT NULL,\n" +
                        "  `mod_by` text DEFAULT NULL,\n" +
                        "  `mod_date_time` datetime DEFAULT NULL)";
                db.execSQL(tbl_lesson_unit_master);

                String tbl_lesson_unit_points="CREATE TABLE `lesson_unit_points` (\n" +
                        "  `tms_lup_key` integer PRIMARY KEY,\n" +
                        "  `tms_lup_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_seq_num` integer DEFAULT NULL,\n" +
                        "  `tms_lup_name` text DEFAULT NULL,\n" +
                        "  `tms_lup_short_name` text DEFAULT NULL,\n" +
                        "  `tms_lup_lu_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_lesson_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_course_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_subject_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_paper_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_chapter_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_type` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_ukm` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_ukf` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_usm` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_usf` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_cmm` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_cmf` text DEFAULT NULL,\n" +
                        "  `tms_lup_exp_media_gen` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_type` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_ukm` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_ukf` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_usm` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_usf` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_cmm` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_cmf` text DEFAULT NULL,\n" +
                        "  `tms_lup_prct_media_gen` text DEFAULT NULL,\n" +
                        "  `tms_lup_duration` integer DEFAULT NULL,\n" +
                        "  `tms_lup_practice_yes_no` text DEFAULT NULL,\n" +
                        "  `tms_lup_test` text DEFAULT NULL,\n" +
                        "  `tms_lup_test_id` text DEFAULT NULL,\n" +
                        "  `tms_lup_pass_req` text DEFAULT NULL,\n" +
                        "  `tms_lup_remark_01` text DEFAULT NULL,\n" +
                        "  `tms_lup_remark_02` text DEFAULT NULL,\n" +
                        "  `tms_lup_status` text DEFAULT NULL,\n" +
                        "  `tms_lup_created_by` text DEFAULT NULL,\n" +
                        "  `tms_lup_created_date_time` datetime DEFAULT NULL,\n" +
                        "  `tms_lup_mod_by` text DEFAULT NULL,\n" +
                        "  `tms_lup_mod_date_time` datetime DEFAULT NULL)";

                db.execSQL(tbl_lesson_unit_points);

                // upgrade logic from version 3 to 4
            case 4:
                    String tbl_org_master="CREATE TABLE `org_master` (\n" +
                            "  `Ctr_key` integer PRIMARY KEY,\n" +
                            "  `Ctr_orga_id` text DEFAULT NULL,\n" +
                            "  `Ctr_Name` integer DEFAULT NULL,\n" +
                            "  `Ctr_Short_name` text DEFAULT NULL,\n" +
                            "  `Ctr_Address01` text DEFAULT NULL,\n" +
                            "  `Ctr_Address02` text DEFAULT NULL,\n" +
                            "  `Ctr_Area` text DEFAULT NULL,\n" +
                            "  `Ctr_City_Town` text DEFAULT NULL,\n" +
                            "  `Ctr_State` text DEFAULT NULL,\n" +
                            "  `Ctr_Country` text DEFAULT NULL,\n" +
                            "  `Ctr_Person` text DEFAULT NULL,\n" +
                            "  `Ctr_Mobile` text DEFAULT NULL,\n" +
                            "  `Ctr_email` text DEFAULT NULL,\n" +
                            "  `Ctr_logo_file` blob DEFAULT NULL,\n" +
                            "  `Ctr_Start_Date` text DEFAULT NULL,\n" +
                            "  `Ctr_End_Date` text DEFAULT NULL,\n" +
                            "  `Ctr_Type` text DEFAULT NULL,\n" +
                            "  `Ctr_category` text DEFAULT NULL,\n" +
                            "  `Ctr_sub_category` text DEFAULT NULL,\n" +
                            "  `ctr_Status` text DEFAULT NULL,\n" +
                            "  `Ctr_created_by` text DEFAULT NULL,\n" +
                            "  `Ctr_created_DtTm` text DEFAULT NULL,\n" +
                            "  `Ctr_Mod_by` text DEFAULT NULL,\n" +
                            "  `Ctr_Mod_DtTm` text DEFAULT NULL)";

                    db.execSQL(tbl_org_master);
                break;

            default:
                throw new IllegalStateException("onUpgrade() with unknown old version "+oldVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertTestAggrigateRecord(int tkey,String tid,String testtype,String torgid,String tbatchid,String tcourseid,String tpaperid,String tsubjectid,double minpercent,double maxpercent,double avgpercent,int minattempts,int maxattempts,int avgattempts,int flag,String createby,String cdttm,String modifiedby,String mdttm){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("testKey",tkey);
        cv.put("testId",tid);
        cv.put("testType",testtype);
        cv.put("test_OrgId",torgid);
        cv.put("test_batchId",tbatchid);
        cv.put("test_courseId",tcourseid);
        cv.put("test_paperId",tpaperid);
        cv.put("test_subjectId",tsubjectid);
        cv.put("minPercentage",minpercent);
        cv.put("maxPercentage",maxpercent);
        cv.put("avgPercentage",avgpercent);
        cv.put("minAttempts",minattempts);
        cv.put("maxAttempts",maxattempts);
        cv.put("avgAttempts",avgattempts);
        cv.put("flag",flag);
        cv.put("createdBy",createby);
        cv.put("createdDttm",cdttm);
        cv.put("modifiedBy",modifiedby);
        cv.put("modifiedDttm",mdttm);
        insertFlag = db.insert("test_main",null, cv);
        return insertFlag;
    }

    public long updateTestAggrigateRecord(String tid,String testtype,String torgid,String tbatchid,String tcourseid,String tpaperid,String tsubjectid,double minpercent,double maxpercent,double avgpercent,int minattempts,int maxattempts,int avgattempts,int flag,String createby,String cdttm,String modifiedby,String mdttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("test_OrgId",torgid);
        cv.put("test_courseId",tcourseid);
        cv.put("test_paperId",tpaperid);
        cv.put("test_subjectId",tsubjectid);
        cv.put("minPercentage",minpercent);
        cv.put("maxPercentage",maxpercent);
        cv.put("avgPercentage",avgpercent);
        cv.put("minAttempts",minattempts);
        cv.put("maxAttempts",maxattempts);
        cv.put("avgAttempts",avgattempts);
        cv.put("flag",flag);
        cv.put("createdBy",createby);
        cv.put("createdDttm",cdttm);
        cv.put("modifiedBy",modifiedby);
        cv.put("modifiedDttm",mdttm);
        updateFlag=db.update("test_main", cv, "testId='"+tid+"' and test_batchId='"+tbatchid+"' and testType='"+testtype+"'",null);
        return updateFlag;
    }

    public Cursor checkTestAggrigateData(String testId,String batchId,String testType){
        Cursor c =db.query("test_main", new String[] {"testId,test_courseId,test_paperId,test_subjectId"},"testId='"+testId+"' and test_batchId='"+batchId+"' and testType='"+testType+"'", null, null, null,null);
        return c;
    }

    public Cursor getTestAggrigateData(String testId,String testtype){
        String query ="SELECT minPercentage,maxPercentage,avgPercentage,minAttempts,maxAttempts,avgAttempts FROM "+" test_main"+" WHERE testId='"+testId+"' and testType='"+testtype+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getPaperAggrigateData(String paperId,String testtype){
        String query ="SELECT MIN(minPercentage) as minscore,MAX(maxPercentage) as maxscore,AVG(avgPercentage) as avgscore FROM "+" test_main"+" WHERE test_paperId='"+paperId+"' and testType='"+testtype+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public long deleteTestRawData(){
        long deleteFlag=0;
        deleteFlag=db.delete("test_main", null,null);
        return  deleteFlag;
    }

    public long insertServerRecord(int skey,String orgid,String branchid,String servername,String serverid,String status,String createby,String createdttm,String modifiedby,String modifieddttm){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("serverKey",skey);
        cv.put("orgId",orgid);
        cv.put("branchId",branchid);
        cv.put("serverName",servername);
        cv.put("serverId",serverid);
        cv.put("status",status);
        cv.put("createdBy",createby);
        cv.put("createdDttm",createdttm);
        cv.put("modifiedBy",modifiedby);
        cv.put("modifiedDttm",modifieddttm);
        insertFlag = db.insert("local_servers",null, cv);
        return insertFlag;
    }

    public long updateServerRecord(int skey,String orgid,String branchid,String servername,String serverid,String status,String createby,String createdttm,String modifiedby,String modifieddttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId",orgid);
        cv.put("branchId",branchid);
        cv.put("serverName",servername);
        cv.put("serverId",serverid);
        cv.put("status",status);
        cv.put("createdBy",createby);
        cv.put("createdDttm",createdttm);
        cv.put("modifiedBy",modifiedby);
        cv.put("modifiedDttm",modifieddttm);
        updateFlag=db.update("local_servers", cv, "serverKey='"+skey+"'",null);
        return updateFlag;
    }

    public Cursor getAvailLocalServers(){
        String query ="SELECT * FROM local_servers";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor checkLocServerRecord(int serverKey){
        String query ="SELECT * FROM local_servers WHERE serverKey='"+serverKey+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public String getServerId(String serverName){
        String serverid="";
        String query ="SELECT * FROM local_servers WHERE serverName='"+serverName+"'";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()>0){
            while (c.moveToNext()){
                serverid=c.getString(c.getColumnIndex("serverId"));
            }
        }else{
            c.close();
        }
        return serverid;
    }

    public long insertAssessmentTest(int tkey,String torgid,String branchid,String tinstanceid,String tenrollid,String tstudentid,String tbatch,String tid,String tname,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,String testfilename,String testKey,Double ttotalmarks,Double tminmarks,Double tmaxmarks,String satu_Test_Time,String satu_Test_Type){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_key",tkey);
        cv.put("satu_org_id",torgid);
        cv.put("satu_branch_id",branchid);
        cv.put("satu_instace_id",tinstanceid);
        cv.put("satu_entroll_id",tenrollid);
        cv.put("satu_student_id",tstudentid);
        cv.put("satu_batch",tbatch);
        cv.put("satu_ID",tid);
        cv.put("satu_name",tname);
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
        cv.put("satu_Test_Time",satu_Test_Time);
        cv.put("satu_Test_Type",satu_Test_Type);
        insertFlag = db.insert("satu_student",null, cv);
        return insertFlag;
    }

    public long updateAssessmentTest(String torgid,String branchid,String tinstanceid,String tenrollid,String tstudentid,String tbatch,String tid,String tname,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstatus,int tnoofques,String testfilename,String testKey,Double ttotalmarks,Double tminmarks,Double tmaxmarks,String satu_Test_Time,String satu_Test_Type){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_org_id",torgid);
        cv.put("satu_branch_id",branchid);
        cv.put("satu_batch",tbatch);
        cv.put("satu_name",tname);
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
        cv.put("satu_Test_Time",satu_Test_Time);
        cv.put("satu_Test_Type",satu_Test_Type);
        updateFlag=db.update("satu_student", cv, "satu_student_id='"+tstudentid+"' and satu_entroll_id='"+tenrollid+"' and satu_ID='"+tid+"' and satu_instace_id='"+tinstanceid+"'",null);
        return updateFlag;
    }

    public long updateAssessmentTestRecord(String tID,String tsid,String tcid,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks,Double avgscore,Double tminperc, Double tmaxperc, Double tavgperc){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
       cv.put("satu_subjet_ID",tsid);
       cv.put("satu_course_id",tcid);
       cv.put("satu_no_of_questions",tnoofques);
       cv.put("satu_tot_marks",ttotalmarks);
       cv.put("satu_min_marks",tminmarks);
       cv.put("satu_min_percent",tminperc);
       cv.put("satu_max_marks",tmaxmarks);
       cv.put("satu_max_percent",tmaxperc);
       cv.put("satu_avg_marks",avgscore);
       cv.put("satu_avg_percent",tavgperc);
       updateFlag=db.update("satu_student", cv, "satu_ID='"+tID+"'",null);
       return updateFlag;
    }

    public int getAssessmentTestsByPaper(String studentId,String enrollId,String paperid){
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_ID,satu_course_id"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_paper_ID='"+paperid+"'", null, null, null,null);
        return c.getCount();
    }

    public Cursor getAssessmentTestData(String testId,String instanceId,String studentId,String enrollId){
        Cursor c =db.query("satu_student", new String[] {"satu_course_id,satu_subjet_ID,satu_paper_ID,satu_entroll_id,satu_student_id,satu_batch,satu_org_id,satu_branch_id"},"satu_ID = '"+testId+"' and satu_entroll_id='"+enrollId+"' and satu_student_id='"+studentId+"' and satu_instace_id='"+instanceId+"'", null, null, null,null);
        return c;
    }

    public Cursor getAssesmentTestsByEnroll(String studentId,String enrollId,String paperId){
        Cursor c =db.query("satu_student",new String[] {"satu_instace_id,satu_entroll_id,satu_student_id,satu_batch,satu_ID,satu_name,satu_paper_ID,satu_subjet_ID,satu_course_id,satu_start_date,satu_end_date,satu_dwnld_status"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_paper_ID='"+paperId+"'", null, null, null,null);
        return c;
    }

    public int getATestsCount(String studentId,String enrollid){
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_student_id,satu_batch"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollid+"'", null, null, null,null);
        return c.getCount();
    }

    public long deleteAllAssessmentTests(){
        long deleteFlag=0;
        deleteFlag=db.delete("satu_student", null,null);
        return  deleteFlag;
    }

    public long insertFlashAttempt(String fUID,String studentId,String enrollId,String courseId,String subjectId,String paperId,String fcardId,int attemptnum,String sdttm,String edttm,int attemptQcount,int iknowcount,int donknowcount,int skipcount,Double percentage,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("flashUID",fUID);
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

    public Cursor getFlashTestData(String studentId,String testId){
        Cursor c =db.query("flashcard_attempt", new String[] {"attemptNumber,startDttm,attemptQCount,iknowCount,donknowCount,skipCount,percentageObtain"},"studentId='"+studentId+"' and flashcardId='"+testId+"'", null, null, null,"startDttm DESC");
        return  c;
    }

    public Cursor getFlashUploadData(String studentId,String status){
        String query ="SELECT * FROM flashcard_attempt WHERE studentId='"+studentId+"' and Status='"+status+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public int getFlashAttemptNum(String studentId,String testId){
        int count=0;
        Cursor c =db.query("flashcard_attempt", new String[] {"attemptNumber,studentId,enrollmentId,courseId,subjectId,paperId"},"studentId='"+studentId+"' and flashcardId='"+testId+"'", null, null, null,null);
        count=c.getCount();
        return count;
    }

    public Cursor getFlashRawData(String studentId,String testId){
        String query ="SELECT COUNT(*) as attemptcount,MIN(percentageObtain) as minscore,MAX(percentageObtain) as maxscore,AVG(percentageObtain) as avgscore FROM "+" flashcard_attempt  WHERE studentId='"+studentId+"' and flashcardId ='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getFlashSummary(String studentId,String enrollid){
        String query ="SELECT count(distinct flashcardId) as attemptfcount,MIN(percentageObtain) as minscore,MAX(percentageObtain) as maxscore,AVG(percentageObtain) as avgscore FROM "+" flashcard_attempt WHERE studentId='"+studentId+"' and enrollmentId ='"+enrollid+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getFlashSummaryByPaper(String studentId,String enrollId,String paperid){
        String query ="SELECT count(distinct flashcardId) as attemptfcount,MIN(percentageObtain) as minscore,MAX(percentageObtain) as maxscore,AVG(percentageObtain) as avgscore FROM "+" flashcard_attempt WHERE studentId='"+studentId+"' and enrollmentId='"+enrollId+"' and paperId ='"+paperid+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getTestFlashSummary(String testId,String studentId,String enrollId){
        String query ="SELECT sptuflash_attempts,max_flashScore,min_flashScore,avg_flashScore,lastAttemptDttm,lastAttemptScore FROM "+" sptu_student"+" WHERE sptu_ID='"+testId+"' and sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAssessmentTestSummary(String studentId,String enrollId,String testId){
        String query ="SELECT count(*) as satu_attempts,MIN(satu_min_percent) as min_AScore,MAX(satu_max_percent) as max_AScore,AVG(satu_avg_percent) as avg_AScore,MAX(satu_last_attempt_end_dttm) as lastAttemptDttm FROM "+" satu_student"+" WHERE satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_ID='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getTestPractiseSummary(String testId,String studentId,String enrollId){
        String query ="SELECT sptu_no_of_attempts,sptu_last_attempt_percent,sptu_min_percent,sptu_max_percent,sptu_avg_percent,sptu_last_attempt_start_dttm FROM "+" sptu_student"+" WHERE sptu_ID='"+testId+"' and sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getPractiseSummary(String enrollId,String studentId){
        String query ="SELECT count(distinct Attempt_Test_ID) as attemptpcount,MIN(Attempt_Percentage) as minscore,MAX(Attempt_Percentage) as maxscore,AVG(Attempt_Percentage) as avgscore FROM "+"attempt_list"+" WHERE Attempt_enrollId ='"+enrollId+"' and Attempt_studentId ='"+studentId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAssessmentSummary(String studentId,String enrollId){
        String query ="SELECT count(distinct Assessment_Test_ID) as attemptacount,MIN(Assessment_Percentage) as minscore,MAX(Assessment_Percentage) as maxscore,AVG(Assessment_Percentage) as avgscore FROM "+"Assessment_list"+" WHERE Assessment_studentId ='"+studentId+"' and Assessment_enrollId ='"+enrollId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getPractiseSummaryByPaper(String studentId,String enrollId,String paperId){
        String query ="SELECT count(distinct Attempt_Test_ID) as attemptpcount,MIN(Attempt_Percentage) as minscore,MAX(Attempt_Percentage) as maxscore,AVG(Attempt_Percentage) as avgscore FROM "+"attempt_list"+" WHERE Attempt_studentId ='"+studentId+"' and Attempt_enrollId='"+enrollId+"' and Attempt_paperId ='"+paperId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getPractiseSummaryByTest(String studentId,String testId){
        String query ="SELECT count(distinct Attempt_Test_ID) as attemptpcount,MIN(Attempt_Percentage) as minscore,MAX(Attempt_Percentage) as maxscore,AVG(Attempt_Percentage) as avgscore FROM "+"attempt_list"+" WHERE Attempt_studentId ='"+studentId+"' and Attempt_Test_ID ='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAssessmentSummaryByPaper(String studentId,String enrollId,String paperId){
        String query ="SELECT count(distinct Assessment_Test_ID) as attemptacount,MIN(Assessment_Percentage) as minscore,MAX(Assessment_Percentage) as maxscore,AVG(Assessment_Percentage) as avgscore FROM "+"Assessment_list"+" WHERE Assessment_studentId ='"+studentId+"' and Assessment_enrollId ='"+enrollId+"' and Assessment_paperId ='"+paperId+"'";
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

    public long updatePtuSection(String testid,int secseq,String secid,String spid,String ssid,String scid,int minques,int maxques,int totgroups,int nogroups,String sstatus,String sname){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Ptu_section_sequence",secseq);
        cv.put("Ptu_section_paper_ID",spid);
        cv.put("Ptu_section_subject_ID",ssid);
        cv.put("Ptu_section_course_ID",scid);
        cv.put("Ptu_section_min_questions",minques);
        cv.put("Ptu_section_max_questions",maxques);
        cv.put("Ptu_sec_tot_groups",totgroups);
        cv.put("Ptu_sec_no_groups",nogroups);
        cv.put("Ptu_section_status",sstatus);
        cv.put("Ptu_section_name",sname);
        updateFlag = db.update("ptu_sections",cv,"Ptu_ID='"+testid+"' and Ptu_section_ID='"+secid+"'",null);
        return updateFlag;
    }

    public long updateFAttemptStatus(String studentId,String fuid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Status",status);
        updateFlag = db.update("flashcard_attempt",cv,"studentId='"+studentId+"' and flashUID='"+fuid+"'",null);
        return updateFlag;
    }

    public long updatePAttemptStatus(String studentId,String puid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Attempt_Upload_Status",status);
        updateFlag = db.update("attempt_list",cv,"Attempt_studentId='"+studentId+"' and Attempt_ID='"+puid+"'",null);
        return updateFlag;
    }

    public Cursor checkPtuSection(String testId,String sectionId){
        Cursor c =db.query("ptu_sections", new String[] {"Ptu_ID,Ptu_section_ID"},"Ptu_ID='"+testId+"' and Ptu_section_ID='"+sectionId+"'", null, null, null,null);
        return c;
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

    public int getPtuSecCount(String testId){
        Cursor c =db.query("ptu_sections", new String[] {"Ptu_section_course_ID,Ptu_section_paper_ID"},"Ptu_ID='"+testId+"'", null, null, null,null);
        return c.getCount();
    }

    public long insertStudent(int skey,String sid,String sname,String sgender,String sedu,String sdob,String saddress1,String saddress2,String scity,String sstate,String scountry,String smobile,String semail,String spassword,String smacid,String sstatus,String screateby,String screateddatetime,String smodifiedby,String smodified_dtm){
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
        cv.put("Student_mod_by",smodifiedby);
        cv.put("Student_mod_DtTm",smodified_dtm);
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

    public long insertEnrollment(int ekey,String eid,String eorg,String esid,String ebranchid,String ebid,String ecid,String ebstartdate,String ebenddate,String edevid,String edate,String estatus,String feecurrency,String feeamt,String feetax,String totalfee,String activationkey,String activationdate,String reqdate,String activatedby,String refdetails,String remark1,String remark2){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Enroll_key",ekey);
        cv.put("Enroll_ID",eid);
        cv.put("Enroll_org_id",eorg);
        cv.put("Enroll_Student_ID",esid);
        cv.put("Enroll_branch_ID",ebranchid);
        cv.put("Enroll_batch_ID",ebid);
        cv.put("Enroll_course_ID",ecid);
        cv.put("Enroll_batch_start_Dt",ebstartdate);
        cv.put("Enroll_batch_end_Dt",ebenddate);
        cv.put("Enroll_Device_ID",edevid);
        cv.put("Enroll_Date",edate);
        cv.put("Enroll_Status",estatus);
        cv.put("enroll_Fee_Currency",feecurrency);
        cv.put("enroll_Fee_Amount",feeamt);
        cv.put("enroll_Fee_tax_percentage",feetax);
        cv.put("enroll_Total_Amount",totalfee);
        cv.put("enroll_Activation_Key",activationkey);
        cv.put("enroll_Activation_Date",activationdate);
        cv.put("enroll_Request_Date",reqdate);
        cv.put("enroll_ActivatedBy",activatedby);
        cv.put("enroll_Refdetails",refdetails);
        cv.put("remarks1",remark1);
        cv.put("remarks2",remark2);
        insertFlag = db.insert("enrollments",null, cv);
        return insertFlag;
    }

    public long updateEnrollment(String eid,String eorg,String esid,String ebranchid,String ebid,String ecid,String ebstartdate,String ebenddate,String edevid,String edate,String estatus,String feecurrency,String feeamt,String feetax,String totalfee,String activationkey,String activationdate,String reqdate,String activatedby,String refdetails,String remark1,String remark2){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Enroll_org_id",eorg);
        cv.put("Enroll_Student_ID",esid);
        cv.put("Enroll_branch_ID",ebranchid);
        cv.put("Enroll_batch_ID",ebid);
        cv.put("Enroll_course_ID",ecid);
        cv.put("Enroll_batch_start_Dt",ebstartdate);
        cv.put("Enroll_batch_end_Dt",ebenddate);
        cv.put("Enroll_Device_ID",edevid);
        cv.put("Enroll_Date",edate);
        cv.put("Enroll_Status",estatus);
        cv.put("enroll_Fee_Currency",feecurrency);
        cv.put("enroll_Fee_Amount",feeamt);
        cv.put("enroll_Fee_tax_percentage",feetax);
        cv.put("enroll_Total_Amount",totalfee);
        cv.put("enroll_Activation_Key",activationkey);
        cv.put("enroll_Activation_Date",activationdate);
        cv.put("enroll_Request_Date",reqdate);
        cv.put("enroll_ActivatedBy",activatedby);
        cv.put("enroll_Refdetails",refdetails);
        cv.put("remarks1",remark1);
        cv.put("remarks2",remark2);
        updateFlag=db.update("enrollments", cv, "Enroll_ID='"+eid+"'",null);
        return updateFlag;
    }

    public long updateEnrollmentStatus(String eid,String estatus){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Enroll_Status",estatus);
        updateFlag=db.update("enrollments", cv, "Enroll_ID='"+eid+"'",null);
        return updateFlag;
    }

    public long checkEnrollment(String enrollId){
        long returnrows=0;
        Cursor c =db.query("enrollments", new String[] {"Enroll_ID,Enroll_Student_ID,Enroll_batch_ID,Enroll_course_ID"},"Enroll_ID='"+enrollId+"'", null, null, null,null);
        returnrows=c.getCount();
        return returnrows;
    }

    public Cursor getStudentEnrolls(String studentId){
        ArrayList<SingleEnrollment> enrollList=new ArrayList<>();
        Cursor c =db.query("enrollments", new String[] {"Enroll_ID,Enroll_org_id,Enroll_Student_ID,Enroll_batch_ID,Enroll_course_ID"},"Enroll_Student_ID='"+studentId+"'", null, null, null,null);
        return c;
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

    public long insertCourse(int ckey,String cid,String cname,String cshname,String ctype,String ccategory,String csubcategory,String cduration,String cminduration,String cmaxduration,String cstatus){
        long insertFlag= 0;
        ContentValues cv = new ContentValues();
        cv.put("Course_Key",ckey);
        cv.put("Course_ID",cid);
        cv.put("Course_Name",cname);
        cv.put("Course_Short_name",cshname);
        cv.put("Course_Type",ctype);
        cv.put("Course_Category",ccategory);
        cv.put("Course_sub_category",csubcategory);
        cv.put("Course_duration_uom",cduration);
        cv.put("Cousre_Duration_min",cminduration);
        cv.put("Course_Duration_Max",cmaxduration);
        cv.put("Course_Status",cstatus);
        insertFlag = db.insert("course_master",null, cv);
        return insertFlag;
    }

    public String getCoursenameById(String courseId){
        String cname="";
        Cursor c =db.query("course_master", new String[] {"Course_Name"},"Course_ID='"+courseId+"'", null, null, null,null);
        if(c.getCount()>0){
            while (c.moveToNext()){
                cname=c.getString(c.getColumnIndex("Course_Name"));
            }
        }else {
            c.close();
        }
        return cname;
    }

    public long deleteAllCourses(){
        long deleteFlag=0;
        deleteFlag=db.delete("course_master", null, null);
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

    public Cursor getPapersByCourse(String courseid){
        Cursor c =db.query("papers", new String[] {"Paper_ID,Paper_Name"},"Course_ID='"+courseid+"'", null, null, null,null);
        return c;
    }

    public long deleteAllPapers(){
        long deleteFlag=0;
        deleteFlag=db.delete("papers", null, null);
        return  deleteFlag;
    }


    public long insertPractiseTest(int tkey,String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String testname,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstartdttm,String tdwdenddttm,String tdwdstatus,String tstatus,int tnoofques,double tptotalmarks,double tpminmarks,double tpmaxmarks,double tpavgmarks,double tpminpercent,double tpmaxpercent,double tpavgpercent,double tplastmarks,double tplastpercent,String tplaststartdttm,String tplastenddttm,int pnoofattempts,int fnoofattempts,double fminscore,double fmaxscore,double favgscore,String flastdttm,double flastscore,String createby,String cdttm,String modifiedby,String mdttm,String sptu_Test_Time,String sptu_Test_Type,int sptu_sequence){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_key",tkey);
        cv.put("sptu_org_id",torgid);
        cv.put("sptu_entroll_id",tenrollid);
        cv.put("sptu_student_ID",tstudentid);
        cv.put("sptu_batch",tbatch);
        cv.put("sptu_ID",tid);
        cv.put("sptu_name",testname);
        cv.put("sptu_paper_ID",tpid);
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_start_date",tstartdate);
        cv.put("sptu_end_date",tenddate);
        cv.put("sptu_dwnld_start_dttm",tdwdstartdttm);
        cv.put("sptu_dwnld_completed_dttm",tdwdenddttm);
        cv.put("sptu_dwnld_status",tdwdstatus);
        cv.put("sptu_status",tstatus);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",tptotalmarks);
        cv.put("stpu_min_marks",tpminmarks);
        cv.put("sptu_max_marks",tpmaxmarks);
        cv.put("sptu_avg_marks",tpavgmarks);
        cv.put("sptu_min_percent",tpminpercent);
        cv.put("sptu_max_percent",tpmaxmarks);
        cv.put("sptu_avg_percent",tpavgpercent);
        cv.put("sptu_last_attempt_marks",tplastmarks);
        cv.put("sptu_last_attempt_percent",tplastpercent);
        cv.put("sptu_last_attempt_start_dttm",tplaststartdttm);
        cv.put("sptu_last_attempt_end_dttm",tplastenddttm);
        cv.put("sptu_no_of_attempts",pnoofattempts);
        cv.put("sptuflash_attempts",fnoofattempts);
        cv.put("min_flashScore",fminscore);
        cv.put("max_flashScore",fmaxscore);
        cv.put("avg_flashScore",favgscore);
        cv.put("lastAttemptDttm",flastdttm);
        cv.put("lastAttemptScore",flastscore);
        cv.put("sptu_created_by",createby);
        cv.put("sptu_created_dttm",cdttm);
        cv.put("sptu_mod_by",modifiedby);
        cv.put("sptu_mod_dttm",mdttm);
        cv.put("sptu_Test_Time",sptu_Test_Time);
        cv.put("sptu_Test_Type",sptu_Test_Type);
        cv.put("sptu_sequence",sptu_sequence);
        insertFlag = db.insert("sptu_student",null, cv);
        return insertFlag;
    }

    public long updateTest(String tID,String tsid,String tcid,int tnoofques,Double ttotalmarks,Double tminmarks,Double tmaxmarks,Double avgscore,Double tminperc, Double tmaxperc, Double tavgperc,String upldstatus){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",ttotalmarks);
        cv.put("stpu_min_marks",tminmarks);
        cv.put("sptu_min_percent",tminperc);
        cv.put("sptu_max_marks",tmaxmarks);
        cv.put("sptu_max_percent",tmaxperc);
        cv.put("sptu_avg_marks",avgscore);
        cv.put("sptu_avg_percent",tavgperc);
        cv.put("sptu_upld_status",upldstatus);
        updateFlag=db.update("sptu_student", cv,"sptu_ID='"+tID+"'",null);
        return  updateFlag;
    }

    public Cursor getAllPTestData(String studentId,String upldstatus){
        String query ="SELECT * FROM sptu_student where sptu_student_ID='"+studentId+"' and sptu_upld_status='"+upldstatus+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public long updatePractiseTestData(String torgid,String tenrollid,String tstudentid,String tbatch,String tid,String testname,String tpid,String tsid,String tcid,String tstartdate,String tenddate,String tdwdstartdttm,String tdwdenddttm,String tdwdstatus,String tstatus,int tnoofques,double tptotalmarks,double tpminmarks,double tpmaxmarks,double tpavgmarks,double tpminpercent,double tpmaxpercent,double tpavgpercent,double tplastmarks,double tplastpercent,String tplaststartdttm,String tplastenddttm,int pnoofattempts,int fnoofattempts,double fminscore,double fmaxscore,double favgscore,String flastdttm,double flastscore,String createby,String cdttm,String modifiedby,String mdttm,String sptu_Test_Time,String sptu_Test_Type,int sptu_sequence){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_org_id",torgid);
        cv.put("sptu_batch",tbatch);
        cv.put("sptu_paper_ID",tpid);
        cv.put("sptu_subjet_ID",tsid);
        cv.put("sptu_course_id",tcid);
        cv.put("sptu_start_date",tstartdate);
        cv.put("sptu_end_date",tenddate);
        cv.put("sptu_dwnld_status",tdwdstatus);
        cv.put("sptu_no_of_questions",tnoofques);
        cv.put("sptu_tot_marks",tptotalmarks);
        cv.put("stpu_min_marks",tpminmarks);
        cv.put("sptu_max_marks",tpmaxmarks);
        cv.put("sptu_avg_marks",tpavgmarks);
        cv.put("sptu_min_percent",tpminpercent);
        cv.put("sptu_max_percent",tpmaxmarks);
        cv.put("sptu_avg_percent",tpavgpercent);
        cv.put("sptu_last_attempt_marks",tplastmarks);
        cv.put("sptu_last_attempt_percent",tplastpercent);
        cv.put("sptu_last_attempt_start_dttm",tplaststartdttm);
        cv.put("sptu_last_attempt_end_dttm",tplastenddttm);
        cv.put("sptu_no_of_attempts",pnoofattempts);
        cv.put("sptuflash_attempts",fnoofattempts);
        cv.put("min_flashScore",fminscore);
        cv.put("max_flashScore",fmaxscore);
        cv.put("avg_flashScore",favgscore);
        cv.put("lastAttemptDttm",flastdttm);
        cv.put("lastAttemptScore",flastscore);
        cv.put("sptu_created_by",createby);
        cv.put("sptu_created_dttm",cdttm);
        cv.put("sptu_mod_by",modifiedby);
        cv.put("sptu_mod_dttm",mdttm);
        cv.put("sptu_Test_Time",sptu_Test_Time);
        cv.put("sptu_Test_Type",sptu_Test_Type);
        cv.put("sptu_sequence",sptu_sequence);
        updateFlag = db.update("sptu_student", cv,"sptu_ID='"+tid+"' and sptu_entroll_id='"+tenrollid+"' and sptu_student_ID='"+tstudentid+"'",null);
        return updateFlag;
    }

    public long updateTestFlashData(String testid,int attemptcount,Double minscore,Double maxscore,Double avgscore,String Dttm,Double lastAttemptscore,String upldstatus){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptuflash_attempts",attemptcount);
        cv.put("min_flashScore",minscore);
        cv.put("max_flashScore",maxscore);
        cv.put("avg_flashScore",avgscore);
        cv.put("lastAttemptDttm",Dttm);
        cv.put("lastAttemptScore",lastAttemptscore);
        cv.put("sptu_upld_status",upldstatus);
        updateFlag=db.update("sptu_student", cv,"sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long updateTestPractiseData(String testid,int attemptcount,Double minscore,Double maxscore,Double avgscore,String Dttm,Double lastAttemptscore){
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

    public Cursor getSingleStudentTests(String studentId,String testId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_student_ID='"+studentId+"' and sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public int getPTestsCount(String studentId,String enrollid){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollid+"'", null, null, null,null);
        return c.getCount();
    }

    public int getTestsByPaper(String studentId,String enrollId,String paperid){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_paper_ID='"+paperid+"'", null, null, null,null);
        return c.getCount();
    }

    public Cursor getTestDataByPaper(String studentId,String enrollId,String paperid){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_name"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_paper_ID='"+paperid+"'", null, null, null,null);
        return c;
    }

    public Cursor getATestDataByPaper(String studentId,String enrollId,String paperid){
        Cursor c =db.query("satu_student", new String[] {"satu_org_id,satu_instace_id,satu_ID,satu_name"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_paper_ID='"+paperid+"'", null, null, null,null);
        return c;
    }

    public Cursor getStudentTests(String studentId,String enrollId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_name,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"'", null, null, null,null);
        return c;
    }

    public Cursor getStudentTests(String studentId,String enrollId,String paperId,String status){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_name,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_paper_ID='"+paperId+"' and sptu_status='"+status+"'", null, null, null,"sptu_sequence ASC");
        return c;
    }

    public Cursor checkPractiseTest(String studentId,String enrollId,String testId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status"},"sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public Cursor checkPractiseTest(String studentId,String testId){
        Cursor c =db.query("sptu_student", new String[] {"sptu_entroll_id,sptu_student_ID,sptu_ID,sptu_paper_ID,sptu_subjet_ID,sptu_course_id,sptu_dwnld_status,sptu_Test_Time,sptu_Test_Type"},"sptu_student_ID='"+studentId+"' and sptu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    public Cursor checkAssessmentTest(String studentId,String enrollId,String testId,String instanceId){
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_student_id,satu_course_id,satu_paper_ID,satu_subjet_ID,satu_Test_Time,satu_Test_Type"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_ID='"+testId+"' and satu_instace_id='"+instanceId+"'", null, null, null,null);
        return c;
    }

    public Cursor getTestFlashData(String testId,String studentId){
        Cursor c =db.query("sptu_student", new String[] {"min_flashScore,max_flashScore,avg_flashScore"},"sptu_ID='"+testId+"' and sptu_student_ID ='"+studentId+"'", null, null, null,null);
        return c;
    }

    public Cursor getTestRawDataByPaper(String paperId){
        String query ="SELECT MIN(min_flashScore) as minscore,MAX(max_flashScore) as maxscore,AVG(avg_flashScore) as avgscore FROM "+" sptu_student"+" WHERE sptu_paper_ID ='"+paperId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAllEnrolls(String studentId){
        String query ="SELECT DISTINCT * FROM enrollments WHERE Enroll_Student_ID='"+studentId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getCourseEnrollments(String courseid,String studentId){
        String query ="SELECT sptu_entroll_id FROM sptu_student where Enroll_Student_ID='"+studentId+"' and sptu_course_id='"+courseid+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public long deleteAllTests(){
        long deleteFlag=0;
        deleteFlag=db.delete("sptu_student", null, null);
        return  deleteFlag;
    }

    public long updatePTestStartStatus(String studentId,String enrollId,String testid,String status,String startdttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_dwnld_start_dttm", startdttm);
        cv.put("sptu_dwnld_status", status);
        updateFlag=db.update("sptu_student",cv, "sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long updatePTestEndStatus(String studentId,String enrollId,String testid,String status,String enddttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_dwnld_completed_dttm", enddttm);
        cv.put("sptu_dwnld_status", status);
        updateFlag=db.update("sptu_student",cv, "sptu_student_ID='"+studentId+"' and sptu_entroll_id='"+enrollId+"' and sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public long updateATestStartStatus(String studentId,String enrollId,String instanceId,String status,String startdttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_dwnld_start_dttm",startdttm);
        cv.put("satu_dwnld_status", status);
        updateFlag=db.update("satu_student",cv,"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_instace_id='"+instanceId+"'",null);
        return  updateFlag;
    }

    public long updateATestEndStatus(String studentId,String enrollId,String instanceId,String status,String enddttm){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("satu_dwnld_completed_dttm",enddttm);
        cv.put("satu_dwnld_status", status);
        updateFlag=db.update("satu_student",cv,"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_instace_id='"+instanceId+"'",null);
        return  updateFlag;
    }

    public long updatePTestUPLDStatus(String studentId,String testid,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("sptu_upld_status",status);
        updateFlag=db.update("sptu_student",cv,"sptu_student_ID='"+studentId+"' and sptu_ID='"+testid+"'",null);
        return  updateFlag;
    }

    public Cursor getSingleAssessmentTests(String studentId,String enrollId,String testId){
        Log.e("DBHelper","studentId"+studentId+"; enrollId"+enrollId+"; testId"+testId);
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_student_id,satu_ID,satu_paper_ID,satu_subjet_ID,satu_course_id,satu_dwnld_status,satu_Test_Time,satu_Test_Type"},"satu_student_id='"+studentId+"' and satu_entroll_id='"+enrollId+"' and satu_ID='"+testId+"'", null, null, null,null);
        return c;
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

    public long updateQuesGroup(String qbid,String tid,String qbmtype,String qbmfile,String qbtext,int noofques,int pickupcount,String status,String createby,String createdttm,String modby,String moddttm,String grouptype){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
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
        updateFlag = db.update("qb_group",cv,"qbg_ID='"+qbid+"'",null);
        return updateFlag;
    }

    public Cursor checkQuesGroup(String groupId){
        Cursor c =db.query("qb_group", new String[] {"testId,qbg_media_file,qbg_status"},"qbg_ID='"+groupId+"'", null, null, null,null);
        return c;
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

    public long updateQuesConfig(String cid,String sid,String pid,String tid,String catid,String scatid,int availcount,int pickupcount,int mincount,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
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
        updateFlag = db.update("ques_config",cv,"testId='"+tid+"' and subcategoryId='"+scatid+"'",null);
        return updateFlag;
    }

    public Cursor checkQuesConfig(String testId,String subcatId){
        Cursor c =db.query("ques_config", new String[] {"testId,subcategoryId"},"testId='"+testId+"' and subcategoryId='"+subcatId+"'", null, null, null,null);
        return c;
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

    public long updateGroupConfig(String cid,String sid,String pid,String tid,String secid,String gtype,int availcount,int pickupcount,String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("courseId",cid);
        cv.put("subjectId",sid);
        cv.put("paperId",pid);
        cv.put("groupavail_count",availcount);
        cv.put("grouppickup_count",pickupcount);
        cv.put("groupques_configstatus",status);
        updateFlag = db.update("groupques_config",cv,"testId='"+tid+"' and sectionId='"+secid+"' and groupType='"+gtype+"'",null);
        return updateFlag;
    }

    public Cursor checkGroupConfig(String testId,String sectionId,String groupType){
        Cursor c =db.query("groupques_config", new String[] {"testId,sectionId,groupType"},"testId='"+testId+"' and sectionId='"+sectionId+"' and groupType='"+groupType+"'", null, null, null,null);
        return c;
    }

    public long deleteGroupsConfig(String testid){
        long deleteFlag=0;
        deleteFlag=db.delete("groupques_config", "testId='"+testid+"'", null);
        return  deleteFlag;
    }

    public int getCompGroupCount(String testId,String sectionId){
        int count=0;
        Cursor c =db.query("groupques_config", new String[] {"grouppickup_count"},"testId='"+testId+"' and sectionId='"+sectionId+"'", null, null, null,null);
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
    public long InsertQuestion(String testId,String attemptId,String stuId,String qId,String qSeq,String qSec,String cat,String subcat, double maxMarks,double negMarks,double marksObtained,double negApplied, int option,String status,String oSeq,String flag){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Test_ID", testId);
        cv.put("Attempt_ID", attemptId);
        cv.put("Student_ID", stuId);
        cv.put("Question_ID", qId);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Section", qSec);
        cv.put("Question_Category", cat);
        cv.put("Question_SubCategory", subcat);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Negative_Marks",negMarks );
        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_Status",status );
        cv.put("Question_Option_Sequence",oSeq );
        cv.put("Option_Answer_Flag",flag );

        //Log.e("DB_Insert:",status);
        Log.e("InsertQuestion:","Question_Seq_No:"+qSeq+"," +
                "Question_Section:"+qSec+",Question_Category"+cat+"," +
                "Question_SubCategory:"+subcat+",Question_Max_Marks:"+maxMarks+
                ",Question_Negative_Marks:"+negMarks+",Question_Marks_Obtained:"+marksObtained+
                ",Question_Negative_Applied:"+negApplied+",Question_Option:"+option+
                ",Question_Status:"+status+",Question_Option_Sequence:"+oSeq+",Option_Answer_Flag:"+flag+
                "Question_ID='"+qId+"' and Attempt_ID='"+attemptId+"' and Test_ID='"+testId+"' and Student_ID='"+stuId+"'");
        insertFlag = db.insert("attempt_data",null, cv);
        Log.e("DB_Insert","insertFlag:"+insertFlag);
        return insertFlag;
    }

    public long UpdateQuestion(String testId,String attemptId,String stuId,String qId,String qSeq,String qSec,String cat,String subcat, double maxMarks,double negMarks,double marksObtained,double negApplied, int option,String status,String oSeq,String flag){

        long updateFlag=0;

        ContentValues cv = new ContentValues();
        /*cv.put("Test_ID", testId);
        cv.put("Attempt_ID", attemptId);
        cv.put("Student_ID", stuId);
        cv.put("Question_ID", qId);*/
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Section", qSec);
        cv.put("Question_Category", cat);
        cv.put("Question_SubCategory", subcat);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Negative_Marks",negMarks );
        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_Status",status );
        cv.put("Question_Option_Sequence",oSeq );
        cv.put("Option_Answer_Flag",flag );
        Log.e("UpdateQuestion:","Question_Seq_No:"+qSeq+"," +
                "Question_Section:"+qSec+",Question_Category"+cat+"," +
                "Question_SubCategory:"+subcat+",Question_Max_Marks:"+maxMarks+
                ",Question_Negative_Marks:"+negMarks+",Question_Marks_Obtained:"+marksObtained+
                ",Question_Negative_Applied:"+negApplied+",Question_Option:"+option+
                ",Question_Status:"+status+",Question_Option_Sequence:"+oSeq+",Option_Answer_Flag:"+flag+
                "Question_ID='"+qId+"' and Attempt_ID='"+attemptId+"' and Test_ID='"+testId+"' and Student_ID='"+stuId+"'");
        //updateFlag = db.update("attempt_data",cv,"Question_ID='"+qId+"' and Attempt_ID='"+attemptId+"' and Test_ID='"+testId+"' and Student_ID='"+stuId+"'",null);
        updateFlag = db.update("attempt_data",cv,"Question_ID='"+qId+"'  and Test_ID='"+testId+"' and Student_ID='"+stuId+"'",null);

        return updateFlag;
    }



    public Cursor getSections(String testId){
        String query ="SELECT DISTINCT Question_Section FROM attempt_data WHERE Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getSubcategories(){
        String query ="SELECT DISTINCT Question_SubCategory FROM attempt_data";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getTestSubcategories(String testId){
        String query ="SELECT DISTINCT Question_SubCategory FROM attempt_data WHERE Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public int getSectionQuestions(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_Section = '"+subct+"' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSectionQuesAns(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_Section = '"+subct+"' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSectionQuesSkip(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_Section = '"+subct+"' and Question_Status = 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSectionQuesCorrect(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE  Option_Answer_Flag = 'YES' and Question_Section = '"+subct+"' and  Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSectionQuesWrong(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE  Option_Answer_Flag <> 'YES' and Question_Section = '"+subct+"' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSubCatQuestions(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_SubCategory = '"+subct+"' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSubCatQuesAns(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_SubCategory = '"+subct+"' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSubCatQuesSkip(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE Question_SubCategory = '"+subct+"' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getSubCatQuesCorrect(String subct,String testId){
        String query ="SELECT * FROM attempt_data WHERE  Option_Answer_Flag = 'YES' and Question_SubCategory = '"+subct+"' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public Boolean CheckQuestion(String qId,String testId){
        Boolean value = false;
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Question_ID = '"+qId+"' and Test_ID = '"+testId+"'";
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

    public Boolean CheckQuestionStatus(String qId){
        Boolean value = false;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_ID = '"+qId+"'";
        db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if (cursor.getString(0).equalsIgnoreCase("ATTEMPTED"))
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

    public ArrayList<String> getQuestionStatus(String testId){
        ArrayList<String> StatusList = new ArrayList<>();
        Cursor c =db.query("attempt_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},"Test_ID = '"+testId+"'", null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                StatusList.add(c.getString(c.getColumnIndex("Question_Status")));
            }
        }
        c.close();
        return StatusList;
    }


    public ArrayList<Integer> getCorrectOptions(String testId){
        ArrayList<Integer> CorrectList = new ArrayList<>();
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Test_ID = '"+testId+"'";
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

    public int getCorrectOptionsCount(String testId){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getCorrectSum(String testId){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumPos FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumPos"));
        c.close();
        return sum;
    }

    public int getWrongSum(String testId){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumNeg FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumNeg"));
        c.close();
        return sum;
    }

    public int getWrongOptionsCount(String testId){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getPosition(String qId,String testId){
        int value = -1;
        try {
            String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Question_ID ='"+qId+"' and Test_ID = '"+testId+"'";
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

    public int getQuestionCount(String testId){
        int count=0;
        String countQuery = "select * from attempt_data where Test_ID='"+testId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        c.close();
        return count;

    }
    public String getNumberOfQuestions(String testId){
        int count=0;
        int min_pickup_count=0;
        String val="No.Ques: ";
        String countQuery = "select min_pickup_count,avail_count from ques_config where testId ='"+testId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        while (c.moveToNext()) {
            count = count+c.getInt(c.getColumnIndex("avail_count"));
            min_pickup_count=min_pickup_count+c.getInt(c.getColumnIndex("min_pickup_count"));
        }
        val=val+"( "+ min_pickup_count+ "/"+ count +" )";
        c.close();
        return val;

    }
    public int getAtuTestNumberOfQuestions(String testId, String instanceId, String studentid, String enrollid){
        int count=0;
        String countQuery = "select satu_no_of_questions from satu_student where satu_ID ='"+testId+"' and satu_instace_id='"+instanceId+"' and  satu_entroll_id='"+enrollid+"' and satu_student_id='"+studentid+"'";
        Cursor c = db.rawQuery(countQuery, null);
        c.moveToLast();
        if (c.getCount() >0) {
            count = c.getInt(c.getColumnIndex("satu_no_of_questions"));
        }
        c.close();
        return count;

    }
    public int getQuestionAttempted(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'ATTEMPTED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getQuestionSkipped(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        return count;

    }

    public int getQuestionBookmarked(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'BOOKMARKED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getQuestionNotAttempted(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'NOT_ATTEMPTED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getTestQuestionCount(String testId){
        int count=0;
        String countQuery = "SELECT Question_ID FROM attempt_data WHERE Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getTestQuestionAttempted(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'ATTEMPTED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getTestQuestionSkipped(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'NOT_ATTEMPTED' and Test_ID ='"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        return count;

    }

    public int getTestQuestionBookmarked(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'BOOKMARKED' and Test_ID = '"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getTestCorrectOptionsCount(String testId){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED' and Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getTestWrongOptionsCount(String testId){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM "+" attempt_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID ='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getTestQuestionNotAttempted(String testId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" attempt_data"+" WHERE Question_Status = 'NOT_ATTEMPTED' and Test_ID ='"+testId+"'";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public void Destroy(String table){
        db.execSQL("delete from "+table);
//        db.execSQL("TRUNCATE table " +table);
    }

    public void DestroyPracticeRecord(String table,String testId){
        db.execSQL("DELETE FROM "+table+" WHERE Test_ID = '"+testId+"'");
//        db.execSQL("TRUNCATE table " +table);
    }

    public long InsertAttempt( String aId,String testID,String eid,String sid,String cid,String subid,String pid,int status,String Upstatus, int aScore, int attempted, int skipped, int bookmarked, int unattempted, int aperc,long aTime,int index,int pos){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Attempt_ID", aId);
        cv.put("Attempt_Test_ID", testID);
        cv.put("Attempt_enrollId", eid);
        cv.put("Attempt_studentId", sid);
        cv.put("Attempt_courseId", cid);
        cv.put("Attempt_subjectId", subid);
        cv.put("Attempt_paperId", pid);
        cv.put("Attempt_Status", status);
        cv.put("Attempt_Upload_Status", Upstatus);
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


    public Cursor getTestRawData(String testId,String studentId,String enrollId){
        String query ="SELECT COUNT(*) as attemptcount,MIN(Attempt_Percentage) as minscore,MAX(Attempt_Percentage) as maxscore,AVG(Attempt_Percentage) as avgscore FROM "+" attempt_list"+" WHERE Attempt_Test_ID ='"+testId+"' and Attempt_Status <> 1 and Attempt_studentId='"+studentId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public long UpdateAttempt(String aID,String testID,int status,String Upstatus, double aScore, int attempted, int skipped, int bookmarked, int unattempted, double aperc,long aTime,int index,int pos){

        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Attempt_Status", status);
        cv.put("Attempt_Upload_Status", Upstatus);
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

    public Cursor getTestAttemptData(String testId,String studentId){
        Cursor c =db.query("attempt_list", new String[] {"Attempt_ID,Attempt_Test_ID,Attempt_Status,Attempt_Upload_Status,Attempt_Confirmed,Attempt_Skipped,Attempt_Bookmarked,Attempt_UnAttempted,Attempt_Score,Attempt_Percentage"},"Attempt_Test_ID='"+testId+"' and Attempt_studentId='"+studentId+"'", null, null, null,"Attempt_ID DESC");
        return  c;
    }

    public Cursor getPractiseUploadData(String studentId,String status){
        String query ="SELECT * FROM attempt_list WHERE Attempt_studentId='"+studentId+"' and Attempt_Upload_Status ='"+status+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public Cursor getAssessmentUploadData(String studentId,String status){
        String query ="SELECT * FROM assessment_data WHERE StudentId='"+studentId+"' and Question_Upload_Status ='"+status+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public Cursor getAssessmentQuestionToBeUploadData(String studentId,String testId,String instanceId,String status){
        String query ="SELECT * FROM assessment_data WHERE StudentId='"+studentId+"' and Question_Upload_Status ='"+status+"' and Test_ID ='"+testId+"' and Assessment_Instance_ID ='"+instanceId+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public Cursor getATestwiseUploadData(String studentId,String instanceId,String status){
        String query ="SELECT * FROM assessment_data WHERE StudentId='"+studentId+"' and Question_Upload_Status ='"+status+"'";
        Cursor c=db.rawQuery(query,null);
        return  c;
    }

    public int getAttempCount(String studentId){
        int count=0;
        String countQuery = "select Attempt_ID from attempt_list WHERE Attempt_studentId='"+studentId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public int getTestAttempCount(String testId,String studentId){
        int count=0;
        String countQuery = "SELECT Attempt_ID FROM attempt_list WHERE Attempt_Test_ID = '"+testId+"' and Attempt_Status <> 1 and Attempt_studentId='"+studentId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public int DeleteAttempt(String aID){
        int count=0;
        String countQuery = "DELETE FROM attempt_list WHERE Attempt_ID='"+aID+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public String getLastAttempt(String studentId){
        String attempt_id = null;
        String query = "select Attempt_ID from attempt_list WHERE Attempt_studentId='"+studentId+"'";
        Cursor c = db.rawQuery(query,null);
        c.moveToLast();
        if (c.getCount() >0) {
            attempt_id = c.getString(c.getColumnIndex("Attempt_ID"));
        } else {
            attempt_id = null;
        }
        return attempt_id;
    }

    public String getLastTestAttempt(String testId,String studentId){
        String attempt_id = null;
        String query = "select Attempt_ID from attempt_list WHERE Attempt_Test_ID = '"+testId+"' and Attempt_studentId='"+studentId+"'";
        Cursor c = db.rawQuery(query,null);
        c.moveToLast();
        if (c.getCount() >0) {
            attempt_id = c.getString(c.getColumnIndex("Attempt_ID"));
        } else {
            attempt_id = null;
        }
        return attempt_id;
    }

    public Cursor getAttempt(String aID){
        int status =0 ;
        String countQuery = "select * from attempt_list where Attempt_ID = '"+aID+"'";
        Cursor c =db.rawQuery(countQuery,null);
        return c;
    }

    public long InsertAssessment( String testID,String instID,String eid,String sid,String cid,String subid,String pid,int status,String dateTime, double aScore, int attempted, int skipped, int bookmarked, int unattempted, double aperc,long aTime,int index,int pos){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Assessment_Test_ID", testID);
        cv.put("Assessment_Instance_ID", instID);
        cv.put("Assessment_enrollId", eid);
        cv.put("Assessment_studentId", sid);
        cv.put("Assessment_courseId", cid);
        cv.put("Assessment_subjectId", subid);
        cv.put("Assessment_paperId", pid);
        cv.put("Assessment_Status", status);
        cv.put("Assessment_Started_dttm", dateTime);
        cv.put("Assessment_Confirmed", attempted);
        cv.put("Assessment_Skipped", skipped);
        cv.put("Assessment_Bookmarked",bookmarked);
        cv.put("Assessment_UnAttempted", unattempted);
        cv.put("Assessment_Score",aScore );
        cv.put("Assessment_Percentage",aperc );
        cv.put("Assessment_RemainingTime",aTime );
        cv.put("Assessment_LastQuestion",index );
        cv.put("Assessment_LastSection",pos );
        insertFlag = db.insert("Assessment_list",null, cv);
        return insertFlag;
    }

    public long UpdateAssessment(String testID,String instID, String eid,String sid,String cid,String subid,String pid,int status,String dateTime, Double aScore, int attempted, int skipped, int bookmarked, int unattempted, double aperc,long aTime,int index,int pos){

        long updateFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Assessment_Instance_ID", instID);
        cv.put("Assessment_enrollId", eid);
        cv.put("Assessment_studentId", sid);
        cv.put("Assessment_courseId", cid);
        cv.put("Assessment_subjectId", subid);
        cv.put("Assessment_paperId", pid);
        cv.put("Assessment_Status", status);
        cv.put("Assessment_Started_dttm", dateTime);
        cv.put("Assessment_Confirmed", attempted);
        cv.put("Assessment_Skipped", skipped);
        cv.put("Assessment_Bookmarked",bookmarked);
        cv.put("Assessment_UnAttempted", unattempted);
        cv.put("Assessment_Score",aScore );
        cv.put("Assessment_Percentage",aperc );
        cv.put("Assessment_RemainingTime",aTime );
        cv.put("Assessment_LastQuestion",index );
        cv.put("Assessment_LastSection",pos );
        updateFlag = db.update("Assessment_list",cv,"Assessment_Test_ID='"+testID+"'",null);
        return updateFlag;
    }

    public Cursor validateAssessmentTestKey(String studentId,String testId){
        Cursor c =db.query("satu_student", new String[] {"satu_entroll_id,satu_exam_key,satu_course_id,satu_paper_ID,satu_subjet_ID"},"satu_student_id='"+studentId+"' and satu_ID='"+testId+"'", null, null, null,null);
        return c;
    }

    //String studentId,String testId,String instanceId
    public Cursor getAssessmentRawData(String testId){
        String query ="SELECT COUNT(*) as assessmentcount,MIN(Assessment_Percentage) as minscore,MAX(Assessment_Percentage) as maxscore,AVG(Assessment_Percentage) as avgscore FROM "+" Assessment_list"+" WHERE Assessment_Test_ID='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }


    public long InsertAssessmentQuestion(String testId,String instID,String key,String stuid,String qId,String org_id,String bran_id,String bat_it,String qSeq,String qSec,String cat,String subcat, double maxMarks,double negMarks,double marksObtained,double negApplied, int option,int qoc,String status,String Upstatus,String oSeq,String flag){

        long insertFlag=0;

        ContentValues cv = new ContentValues();
        cv.put("Test_ID", testId);
        cv.put("Assessment_Instance_ID", instID);
        cv.put("Question_Key", key);
        cv.put("StudentId", stuid);
        cv.put("Question_ID", qId);
        cv.put("Org_ID", org_id);
        cv.put("Branch_ID", bran_id);
        cv.put("Batch_ID", bat_it);
        cv.put("Question_Seq_No", qSeq);
        cv.put("Question_Section", qSec);
        cv.put("Question_Category", cat);
        cv.put("Question_SubCategory", subcat);
        cv.put("Question_Max_Marks",maxMarks );
        cv.put("Question_Negative_Marks",negMarks );
        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_OptionCount",qoc );
        cv.put("Question_Status",status );
        cv.put("Question_Upload_Status",Upstatus );
        cv.put("Question_Option_Sequence",oSeq );
        cv.put("Option_Answer_Flag",flag );

        Log.e("DB_Insert:",status);
        insertFlag = db.insert("assessment_data",null, cv);

        return insertFlag;
    }


    public long UpdateAssessmentQuestion(String testId, String instID, String stuid, String qId, String org_id, String bran_id, String bat_id, double marksObtained, double negApplied, int option, String status, String Upstatus, int sel_seq){

        long updateFlag=0;

        String where_clause="Question_ID='"+qId+"' AND Assessment_Instance_ID='"+instID+"' AND Test_ID='"+testId+"' AND StudentId='"+stuid+"' AND Org_ID='"+org_id+"' AND Branch_ID='"+bran_id+"' AND Batch_ID='"+bat_id+"'";

        ContentValues cv = new ContentValues();


        cv.put("Question_Marks_Obtained",marksObtained );
        cv.put("Question_Negative_Applied",negApplied );
        cv.put("Question_Option",option );
        cv.put("Question_Status",status );
        cv.put("Question_Upload_Status",Upstatus );
        cv.put("Question_Option_Sequence",sel_seq );


        Log.e("DB_Update:",status);
        updateFlag = db.update("assessment_data",cv,where_clause,null);

        return updateFlag;
    }

    public long updateAssessmentQStatus(String studentId,String testQUID,String status){

        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Question_Upload_Status",status );
        updateFlag = db.update("assessment_data",cv,"StudentId='"+studentId+"' and Question_Key='"+testQUID+"'",null);
        return updateFlag;
    }

    public long updateAssessmentQStatusFromServer(String studentId,String testId,String instanceId,String testQUID,String status){

        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Question_Upload_Status",status );
        updateFlag = db.update("assessment_data",cv,"StudentId='"+studentId+"' and Question_Key='"+testQUID+"' and Test_ID ='"+testId+"' and Assessment_Instance_ID ='"+instanceId+"'",null);
        return updateFlag;
    }
    public int checkAQUPLDStatus(String studentId,String testQUID,String status){
        Cursor c =db.query("assessment_data", new String[] {"StudentId,Question_Upload_Status"},"StudentId='"+studentId+"' and Question_Key='"+testQUID+"' and Question_Upload_Status='"+status+"'", null, null, null,null);
        return c.getCount();
    }

    public ArrayList<String> getAssessmentQuestionStatus(String testId){
        ArrayList<String> StatusList = new ArrayList<>();
        Cursor c =db.query("assessment_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},"Test_ID = '"+testId+"'", null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                StatusList.add(c.getString(c.getColumnIndex("Question_Status")));
            }
        }
        c.close();
        return StatusList;
    }
    public Cursor getAssessment( String studentid, String instanceid){
        String query ="SELECT * FROM assessment_data WHERE StudentId='"+studentid+"' AND  Assessment_Instance_ID='"+instanceid+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAssessmentSections(String testId){
        String query ="SELECT DISTINCT Question_Section FROM assessment_data WHERE Test_ID = '"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public Cursor getAssessmentSubcategories(){
        String query ="SELECT DISTINCT Question_SubCategory FROM assessment_data";
        Cursor c=db.rawQuery(query,null);
        return c;
    }

    public int getAssessmentSectionQuestions(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_Section ='"+sect+"' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSectionQuesAns(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_Section ='"+sect+"' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSectionQuesSkip(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_Section ='"+sect+"' and Question_Status = 'SKIPPED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSectionQuesCorrect(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE  Question_Marks_Obtained >0 and Question_Section = '"+sect+"' and  Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSectionQuesWrong(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE  Question_Negative_Applied > 0 and Question_Section = '"+sect+"' and  Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSubCatQuestions(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_SubCategory = '"+sect+"' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSubCatQuesAns(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_SubCategory = '"+sect+"' and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSubCatQuesSkip(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE Question_SubCategory = '"+sect+"' and Question_Status <> 'SKIPPED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentSubCatQuesCorrect(String sect,String testId, String instanceId, String studentId){
        String query ="SELECT * FROM assessment_data WHERE  Question_Marks_Obtained >0 and Question_SubCategory = '"+sect+"' and Question_Status <> 'SKIPPED'and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public Boolean AssessmentCheckQuestion(String qId){
        Boolean value = false;
        String query ="SELECT  Question_Option FROM "+" assessment_data"+" WHERE Question_ID = '"+qId+"'";
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

    public ArrayList<Integer> getAssessmentOptions(){
        ArrayList<Integer> OptionList = new ArrayList<>();
        Cursor c =db.query("assessment_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},null, null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                OptionList.add(c.getInt(c.getColumnIndex("Question_Option")));
            }
        }
        c.close();
        return OptionList;
    }

    public ArrayList<String> getAssessmentQuestionStatus(){
        ArrayList<String> StatusList = new ArrayList<>();
        Cursor c =db.query("assessment_data", new String[] {"Question_ID,Question_Seq_No,Question_Max_Marks,Question_Option,Question_Status,Question_Option_Sequence,Option_Answer_Flag"},null, null, null, null,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                StatusList.add(c.getString(c.getColumnIndex("Question_Status")));
            }
        }
        c.close();
        return StatusList;
    }


    public ArrayList<Integer>getAssessmentCorrectOptions(){
        ArrayList<Integer> CorrectList = new ArrayList<>();
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM  assessment_data WHERE Option_Answer_Flag = 'YES'";
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

    public int getAssessmentCorrectOptionsCount(String testId, String instanceId, String studentId){
        int count = 0;
        //ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM  assessment_data  WHERE Question_Marks_Obtained > 0 and Question_Status <> 'NOT_ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentCorrectSum(){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumPos FROM  assessment_data WHERE Option_Answer_Flag = 'YES' and Question_Status <> 'SKIPPED'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumPos"));
        c.close();
        return sum;
    }

    public int getAssessmentWrongSum(){
        int sum = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  SUM(Question_Max_Marks) as SumNeg FROM "+" assessment_data"+" WHERE Option_Answer_Flag = 'NO' and Question_Status <> 'SKIPPED'";
        Cursor c=db.rawQuery(query,null);
        sum = c.getInt(c.getColumnIndex("SumNeg"));
        c.close();
        return sum;
    }

    public int getAssessmentWrongOptionsCount(String testId, String instanceId, String studentId){
        int count = 0;
        ArrayList<Integer> OptionList = new ArrayList<>();
        String query ="SELECT  Question_Option FROM assessment_data WHERE Question_Negative_Applied > 0 and Question_Status NOT IN ('NOT_ATTEMPTED','SKIPPED') and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        count = c.getCount();
        c.close();
        return count;
    }

    public int getAssessmentPosition(String qId,String testId){
        int value = -1;
        try {
            String query ="SELECT  Question_Option FROM "+" assessment_data"+" WHERE Question_ID ='"+qId+"' and  Test_ID ='"+testId+"'";
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

    public int getAssessmentQuestionCount(String testId){
        int count=0;
        String countQuery = "SELECT * FROM assessment_data WHERE Test_ID='"+testId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getAssessmentQuestionAttempted(String testId, String instanceId, String studentId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" assessment_data"+" WHERE Question_Status = 'ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getAssessmentQuestionSkipped(String testId, String instanceId, String studentId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" assessment_data"+" WHERE Question_Status = 'NOT_ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        return count;

    }

    public int getAssessmentQuestionBookmarked(String testId, String instanceId, String studentId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" assessment_data"+" WHERE Question_Status = 'BOOKMARKED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }

    public int getAssessmentQuestionNotAttempted(String testId, String instanceId, String studentId){
        int count=0;
        String query ="SELECT  Question_Status FROM "+" assessment_data"+" WHERE Question_Status = 'NOT_ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c = db.rawQuery(query, null);
        count=c.getCount();
        c.close();
        return count;

    }


    public double getTotalPositiveMarks(String testId, String instanceId, String studentId) {

        double positivemarks = 0.0;
        String query ="SELECT  SUM(Question_Marks_Obtained) as Question_Marks_Obtained FROM  assessment_data  WHERE Question_Marks_Obtained > 0 and Question_Status <> 'NOT_ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                positivemarks=  c.getDouble(c.getColumnIndex("Question_Marks_Obtained"));
            }
        }
        c.close();

        return positivemarks;
    }

    public double getPracticeTestTotalPositiveMarks(String testId) {

        double positivemarks = 0.0;
        String query ="SELECT  SUM(Question_Marks_Obtained) as Question_Marks_Obtained FROM  attempt_data  WHERE Question_Marks_Obtained > 0 and Question_Status <> 'NOT_ATTEMPTED' and Test_ID='"+testId+"'";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                positivemarks=  c.getDouble(c.getColumnIndex("Question_Marks_Obtained"));
            }
        }
        c.close();

        return positivemarks;
    }


    public Double getTotalNegativeMarks(String testId, String instanceId, String studentId) {
        double negativemarks = 0.0;
        String query ="SELECT  SUM(Question_Negative_Applied) as Question_Negative_Applied FROM  assessment_data  WHERE Question_Negative_Applied > 0 and Question_Status <> 'NOT_ATTEMPTED' and Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                negativemarks=  c.getDouble(c.getColumnIndex("Question_Negative_Applied"));
            }
        }
        c.close();

        return negativemarks;
    }

    public Double getPracticeTestTotalNegativeMarks(String testId) {
        double negativemarks = 0.0;
        String query ="SELECT  SUM(Question_Negative_Applied) as Question_Negative_Applied FROM  attempt_data  WHERE Question_Negative_Applied > 0 and Question_Status <> 'NOT_ATTEMPTED' and Test_ID='"+testId+"' ";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                negativemarks=  c.getDouble(c.getColumnIndex("Question_Negative_Applied"));
            }
        }
        c.close();

        return negativemarks;
    }

    public Double getTotalTestMarks(String testId, String instanceId, String studentId) {
        double totalmarks = 0.0;
        String query ="SELECT  SUM(Question_Max_Marks) as Question_Max_Marks FROM  assessment_data  WHERE  Test_ID='"+testId+"' and Assessment_Instance_ID='"+instanceId+"' and StudentId='"+studentId+"' ";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                totalmarks=  c.getDouble(c.getColumnIndex("Question_Max_Marks"));
            }
        }
        c.close();

        return totalmarks;
    }

    public Double getPracticeTestTotalTestMarks(String testId) {
        double totalmarks = 0.0;
        String query ="SELECT  SUM(Question_Max_Marks) as Question_Max_Marks FROM  attempt_data  WHERE  Test_ID='"+testId+"'  ";
        Cursor c=db.rawQuery(query,null);
        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                totalmarks=  c.getDouble(c.getColumnIndex("Question_Max_Marks"));
            }
        }
        c.close();

        return totalmarks;
    }


    public long updatePassword(String mobile_num,String email,String password){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("Student_password",password);
        updateFlag = db.update("student_master",cv,"Student_Mobile='"+mobile_num+"' and Student_email='"+email+"'",null);
        return updateFlag;
    }


    public long deleteAll_lesson_master(){
        long deleteFlag=0;
        deleteFlag=db.delete("lesson_master", null, null);
        return  deleteFlag;
    }



    public long insertlesson_master(int tms_lms_lesson_key,String tms_lms_lesson_id,String tms_lms_lesson_seq_number,String tms_lms_course_id,
                                    String tms_lms_subject_id,String tms_lms_paper_id,String tms_lms_lesson_name,String tms_lms_lesson_lang_code,
                                    String tms_lms_lesson_long_name,String tms_lms_lesson_short_name,String tms_lms_lesson_medium,
                                    String tms_lms_lesson_remark_01,String tms_lms_lesson_remark_02,String tms_lms_lesson_status,
                                    String created_by,String created_date_time,String mod_by,String mod_date_time){
        long insertFlag= 0;
        ContentValues cv = new ContentValues();
        cv.put("tms_lms_lesson_key",tms_lms_lesson_key);
        cv.put("tms_lms_lesson_id",tms_lms_lesson_id);
        cv.put("tms_lms_lesson_seq_number",tms_lms_lesson_seq_number);
        cv.put("tms_lms_course_id",tms_lms_course_id);
        cv.put("tms_lms_subject_id",tms_lms_subject_id);
        cv.put("tms_lms_paper_id",tms_lms_paper_id);
        cv.put("tms_lms_lesson_name",tms_lms_lesson_name);
        cv.put("tms_lms_lesson_lang_code",tms_lms_lesson_lang_code);
        cv.put("tms_lms_lesson_long_name",tms_lms_lesson_long_name);
        cv.put("tms_lms_lesson_short_name",tms_lms_lesson_short_name);
        cv.put("tms_lms_lesson_medium",tms_lms_lesson_medium);
        cv.put("tms_lms_lesson_remark_01",tms_lms_lesson_remark_01);
        cv.put("tms_lms_lesson_remark_02",tms_lms_lesson_remark_02);
        cv.put("tms_lms_lesson_status",tms_lms_lesson_status);
        cv.put("created_by",created_by);
        cv.put("created_date_time",created_date_time);
        cv.put("mod_by",mod_by);
        cv.put("mod_date_time",mod_date_time);
        insertFlag = db.insert("lesson_master",null, cv);
        return insertFlag;
    }


    public long deleteAll_lesson_unit_master(){
        long deleteFlag=0;
        deleteFlag=db.delete("lesson_unit_master", null, null);
        return  deleteFlag;
    }

    public long insert_lesson_unit_master(int tms_lu_key,String tms_lu_id,String tms_lu_seq_num,String tms_lesson_id,String tms_lu_course_id,
                                          String tms_lu_name,String tms_lu_lang_code,String tms_lu_long_name,String tms_lu_short_name,
                                          String tms_lu_short_lang_name,String tms_lu_subject_id,String tms_lu_paper_id,String tms_lu_remark_01,
                                          String tms_lu_remark_02,String tms_lu_status,String created_by,String creaated_date_time,String mod_by,
                                          String mod_date_time){
        long insertFlag= 0;
        ContentValues cv = new ContentValues();
        cv.put("tms_lu_key",tms_lu_key);
        cv.put("tms_lu_id",tms_lu_id);
        cv.put("tms_lu_seq_num",tms_lu_seq_num);
        cv.put("tms_lesson_id",tms_lesson_id);
        cv.put("tms_lu_course_id",tms_lu_course_id);
        cv.put("tms_lu_name",tms_lu_name);
        cv.put("tms_lu_lang_code",tms_lu_lang_code);
        cv.put("tms_lu_long_name",tms_lu_long_name);
        cv.put("tms_lu_short_name",tms_lu_short_name);
        cv.put("tms_lu_short_lang_name",tms_lu_short_lang_name);
        cv.put("tms_lu_subject_id",tms_lu_subject_id);
        cv.put("tms_lu_paper_id",tms_lu_paper_id);
        cv.put("tms_lu_remark_01",tms_lu_remark_01);
        cv.put("tms_lu_remark_02",tms_lu_remark_02);
        cv.put("tms_lu_status",tms_lu_status);
        cv.put("created_by",created_by);
        cv.put("creaated_date_time",creaated_date_time);
        cv.put("mod_by",mod_by);
        cv.put("mod_date_time",mod_date_time);
        insertFlag = db.insert("lesson_unit_master",null, cv);
        return insertFlag;
    }

    public long deleteAll_lesson_unit_points(){
        long deleteFlag=0;
        deleteFlag=db.delete("lesson_unit_points", null, null);
        return  deleteFlag;
    }

    public long insert_lesson_unit_points(int tms_lup_key, String tms_lup_id, String tms_lup_seq_num, String tms_lup_name,
                                          String tms_lup_short_name, String tms_lup_lu_id, String tms_lup_lesson_id, String tms_lup_course_id,
                                          String tms_lup_subject_id, String tms_lup_paper_id, String tms_lup_chapter_id, String tms_lup_exp_media_type,
                                          String tms_lup_exp_media_ukm, String tms_lup_exp_media_ukf, String tms_lup_exp_media_usm, String tms_lup_exp_media_usf,
                                          String tms_lup_exp_media_cmm, String tms_lup_exp_media_cmf, String tms_lup_exp_media_gen,
                                          String tms_lup_prct_media_type, String tms_lup_prct_media_ukm, String tms_lup_prct_media_ukf, String tms_lup_prct_media_usm,
                                          String tms_lup_prct_media_usf, String tms_lup_prct_media_cmm, String tms_lup_prct_media_cmf, String tms_lup_prct_media_gen,
                                          String tms_lup_duration, String tms_lup_practice_yes_no,
                                          String tms_lup_test, String tms_lup_test_id, String tms_lup_pass_req, String tms_lup_remark_01, String tms_lup_remark_02,
                                          String tms_lup_status, String tms_lup_created_by, String tms_lup_created_date_time, String tms_lup_mod_by, String tms_lup_mod_date_time){
        long insertFlag= 0;
        ContentValues cv = new ContentValues();
        cv.put("tms_lup_key",tms_lup_key);
        cv.put("tms_lup_id",tms_lup_id);
        cv.put("tms_lup_seq_num",tms_lup_seq_num);
        cv.put("tms_lup_name",tms_lup_name);
        cv.put("tms_lup_short_name",tms_lup_short_name);
        cv.put("tms_lup_lu_id",tms_lup_lu_id);
        cv.put("tms_lup_lesson_id",tms_lup_lesson_id);
        cv.put("tms_lup_course_id",tms_lup_course_id);
        cv.put("tms_lup_subject_id",tms_lup_subject_id);
        cv.put("tms_lup_paper_id",tms_lup_paper_id);
        cv.put("tms_lup_chapter_id",tms_lup_chapter_id);
        cv.put("tms_lup_exp_media_type",tms_lup_exp_media_type);
        cv.put("tms_lup_exp_media_ukm",tms_lup_exp_media_ukm);
        cv.put("tms_lup_exp_media_ukf",tms_lup_exp_media_ukf);
        cv.put("tms_lup_exp_media_usm",tms_lup_exp_media_usm);
        cv.put("tms_lup_exp_media_usf",tms_lup_exp_media_usf);
        cv.put("tms_lup_exp_media_cmm",tms_lup_exp_media_cmm);
        cv.put("tms_lup_exp_media_cmf",tms_lup_exp_media_cmf);
        cv.put("tms_lup_exp_media_gen",tms_lup_exp_media_gen);
        cv.put("tms_lup_prct_media_type",tms_lup_prct_media_type);
        cv.put("tms_lup_prct_media_ukm",tms_lup_prct_media_ukm);
        cv.put("tms_lup_prct_media_ukf",tms_lup_prct_media_ukf);
        cv.put("tms_lup_prct_media_usm",tms_lup_prct_media_usm);
        cv.put("tms_lup_prct_media_usf",tms_lup_prct_media_usf);
        cv.put("tms_lup_prct_media_cmm",tms_lup_prct_media_cmm);
        cv.put("tms_lup_prct_media_cmf",tms_lup_prct_media_cmf);
        cv.put("tms_lup_prct_media_gen",tms_lup_prct_media_gen);
        cv.put("tms_lup_duration",tms_lup_duration);
        cv.put("tms_lup_practice_yes_no",tms_lup_practice_yes_no);
        cv.put("tms_lup_test",tms_lup_test);
        cv.put("tms_lup_test_id",tms_lup_test_id);
        cv.put("tms_lup_pass_req",tms_lup_pass_req);
        cv.put("tms_lup_remark_01",tms_lup_remark_01);
        cv.put("tms_lup_remark_02",tms_lup_remark_02);
        cv.put("tms_lup_status",tms_lup_status);
        cv.put("tms_lup_created_by",tms_lup_created_by);
        cv.put("tms_lup_created_date_time",tms_lup_created_date_time);
        cv.put("tms_lup_mod_by",tms_lup_mod_by);
        cv.put("tms_lup_mod_date_time",tms_lup_mod_date_time);
        insertFlag = db.insert("lesson_unit_points",null, cv);
        return insertFlag;
    }


    public ArrayList<Lesson> getLessons(String tms_lms_course_id,String tms_lms_subject_id,String tms_lms_paper_id){
        ArrayList<Lesson> al_lessions=new ArrayList<>();
        Cursor mycursor =db.query("lesson_master", new String[] {"tms_lms_lesson_key,tms_lms_lesson_id,tms_lms_lesson_seq_number,tms_lms_course_id,tms_lms_subject_id,tms_lms_paper_id,tms_lms_lesson_name,tms_lms_lesson_lang_code, tms_lms_lesson_long_name,tms_lms_lesson_short_name,tms_lms_lesson_medium,tms_lms_lesson_remark_01,tms_lms_lesson_remark_02,tms_lms_lesson_status,created_by,created_date_time,mod_by,mod_date_time"},"tms_lms_course_id='"+tms_lms_course_id+"'  and tms_lms_paper_id='"+tms_lms_paper_id+"' and tms_lms_lesson_status='ACTIVE'" , null, null, null,null);

        Log.e("DBHelper","mycursor length:"+mycursor.getCount());

        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()) {

                al_lessions.add(new Lesson(mycursor.getInt(mycursor.getColumnIndex("tms_lms_lesson_key")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_seq_number")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_course_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_subject_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_paper_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_lang_code")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_long_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_short_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_medium")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_remark_01")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_remark_02")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lms_lesson_status")),
                        mycursor.getString(mycursor.getColumnIndex("created_by")),
                        mycursor.getString(mycursor.getColumnIndex("created_date_time")),
                        mycursor.getString(mycursor.getColumnIndex("mod_by")),
                        mycursor.getString(mycursor.getColumnIndex("mod_date_time"))
                       ));
            }
        }else{
            mycursor.close();
        }

        return  al_lessions;
    }




    public ArrayList<LessonUnit> getLessonUnits(String tms_lu_course_id, String tms_lu_subject_id, String tms_lu_paper_id,String tms_lesson_id){
        ArrayList<LessonUnit> al_lessionUnits=new ArrayList<>();
        Cursor mycursor =db.query("lesson_unit_master", new String[] {"tms_lu_key,\n" +
                "     tms_lu_id, tms_lu_seq_num, tms_lesson_id, tms_lu_course_id,\n" +
                "     tms_lu_name, tms_lu_lang_code, tms_lu_long_name, tms_lu_short_name,\n" +
                "     tms_lu_short_lang_name, tms_lu_subject_id, tms_lu_paper_id, tms_lu_remark_01,\n" +
                "     tms_lu_remark_02, tms_lu_status, created_by, creaated_date_time, mod_by,\n" +
                "     mod_date_time"},"tms_lu_course_id='"+tms_lu_course_id+"'  and tms_lu_paper_id='"+tms_lu_paper_id+"' and tms_lesson_id='"+tms_lesson_id+"'  and tms_lu_status='ACTIVE'" , null, null, null,null);

        Log.e("DBHelper","mycursor length:"+mycursor.getCount());

        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()) {

                al_lessionUnits.add(new LessonUnit(mycursor.getInt(mycursor.getColumnIndex("tms_lu_key")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_seq_num")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lesson_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_course_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_lang_code")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_long_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_short_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_short_lang_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_subject_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_paper_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_remark_01")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_remark_02")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lu_status")),
                        mycursor.getString(mycursor.getColumnIndex("created_by")),
                        mycursor.getString(mycursor.getColumnIndex("creaated_date_time")),
                        mycursor.getString(mycursor.getColumnIndex("mod_by")),
                        mycursor.getString(mycursor.getColumnIndex("mod_date_time"))
                ));
            }
        }else{
            mycursor.close();
        }

        return  al_lessionUnits;
    }

    public ArrayList<LessonUnitPoint> getLessonUnitpoints(String tms_lup_course_id, String tms_lup_subject_id, String tms_lup_paper_id, String tms_lup_lesson_id,String tms_lup_lu_id){
        ArrayList<LessonUnitPoint> al_lessionUnit_points=new ArrayList<>();
        Cursor mycursor =db.query("lesson_unit_points", new String[] {"tms_lup_key,tms_lup_id,  tms_lup_seq_num,  tms_lup_name,\n" +
                "     tms_lup_short_name,  tms_lup_lu_id,  tms_lup_lesson_id,  tms_lup_course_id,\n" +
                "     tms_lup_subject_id,  tms_lup_paper_id,  tms_lup_chapter_id,  tms_lup_exp_media_type,\n" +
                "     tms_lup_exp_media_ukm,  tms_lup_exp_media_ukf,  tms_lup_exp_media_usm,  tms_lup_exp_media_usf,\n" +
                "     tms_lup_exp_media_cmm,  tms_lup_exp_media_cmf,  tms_lup_exp_media_gen,\n" +
                "     tms_lup_prct_media_type,  tms_lup_prct_media_ukm,  tms_lup_prct_media_ukf,  tms_lup_prct_media_usm,\n" +
                "     tms_lup_prct_media_usf,  tms_lup_prct_media_cmm,  tms_lup_prct_media_cmf,  tms_lup_prct_media_gen,\n" +
                "     tms_lup_duration,  tms_lup_practice_yes_no,\n" +
                "     tms_lup_test,  tms_lup_test_id,  tms_lup_pass_req,  tms_lup_remark_01,  tms_lup_remark_02,\n" +
                "     tms_lup_status,  tms_lup_created_by,  tms_lup_created_date_time,  tms_lup_mod_by,  tms_lup_mod_date_time"},"tms_lup_course_id='"+tms_lup_course_id+"'  and tms_lup_paper_id='"+tms_lup_paper_id+"' and tms_lup_lesson_id='"+tms_lup_lesson_id+"' and tms_lup_lu_id='"+tms_lup_lu_id+"' and tms_lup_status='ACTIVE'" , null, null, null,null);

        Log.e("DBHelper","mycursor length:"+mycursor.getCount());


        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()) {

                al_lessionUnit_points.add(new LessonUnitPoint(mycursor.getInt(mycursor.getColumnIndex("tms_lup_key")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_seq_num")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_short_name")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_lu_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_lesson_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_course_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_subject_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_paper_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_chapter_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_type")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_ukm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_ukf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_usm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_usf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_cmm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_cmf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_exp_media_gen")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_type")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_ukm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_ukf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_usm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_usf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_cmm")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_cmf")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_prct_media_gen")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_duration")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_practice_yes_no")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_test")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_test_id")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_pass_req")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_remark_01")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_remark_02")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_status")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_created_by")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_created_date_time")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_mod_by")),
                        mycursor.getString(mycursor.getColumnIndex("tms_lup_mod_date_time"))
                ));
            }
        }else{
            mycursor.close();
        }

        return  al_lessionUnit_points;
    }

    public long deleteAll_org_master(){
        long deleteFlag=0;
        deleteFlag=db.delete("org_master", null, null);
        return  deleteFlag;
    }

    public long insert_org_master(int Ctr_key, String Ctr_orga_id, String Ctr_Name, String Ctr_Short_name,
                                  String Ctr_Address01, String Ctr_Address02, String Ctr_Area,
                                  String Ctr_City_Town, String Ctr_State, String Ctr_Country,
                                  String Ctr_Person, String Ctr_Mobile, String Ctr_email,
                                  String Ctr_logo_file, String Ctr_Start_Date, String Ctr_End_Date,
                                  String Ctr_Type, String Ctr_category, String Ctr_sub_category,
                                  String ctr_Status, String Ctr_created_by, String Ctr_created_DtTm,
                                  String Ctr_Mod_by, String Ctr_Mod_DtTm){
        long insertFlag= 0;
        ContentValues cv = new ContentValues();
        cv.put("Ctr_key",Ctr_key);
        cv.put("Ctr_orga_id",Ctr_orga_id);
        cv.put("Ctr_Name",Ctr_Name);
        cv.put("Ctr_Short_name",Ctr_Short_name);
        cv.put("Ctr_Address01",Ctr_Address01);
        cv.put("Ctr_Address02",Ctr_Address02);
        cv.put("Ctr_Area",Ctr_Area);
        cv.put("Ctr_City_Town",Ctr_City_Town);
        cv.put("Ctr_State",Ctr_State);
        cv.put("Ctr_Country",Ctr_Country);
        cv.put("Ctr_Person",Ctr_Person);
        cv.put("Ctr_Mobile",Ctr_Mobile);
        cv.put("Ctr_email",Ctr_email);
        cv.put("Ctr_logo_file",Ctr_logo_file);
        cv.put("Ctr_Start_Date",Ctr_Start_Date);
        cv.put("Ctr_End_Date",Ctr_End_Date);
        cv.put("Ctr_Type",Ctr_Type);
        cv.put("Ctr_category",Ctr_category);
        cv.put("Ctr_sub_category",Ctr_sub_category);
        cv.put("ctr_Status",ctr_Status);
        cv.put("Ctr_created_by",Ctr_created_by);
        cv.put("Ctr_created_DtTm",Ctr_created_DtTm);
        cv.put("Ctr_Mod_by",Ctr_Mod_by);
        cv.put("Ctr_Mod_DtTm",Ctr_Mod_DtTm);
        insertFlag = db.insert("org_master",null, cv);
        return insertFlag;
    }

    public SingleOrganization getOrganization(String orga_id){
        SingleOrganization org=null;

        String table = "org_master";
        String[] columns = {"Ctr_key,Ctr_orga_id,  Ctr_Name,  Ctr_Short_name,Ctr_Address01,  Ctr_Address02,  Ctr_Area,Ctr_City_Town,  Ctr_State,  Ctr_Country,Ctr_Person,  Ctr_Mobile,  Ctr_email,Ctr_logo_file,  Ctr_Start_Date,  Ctr_End_Date,Ctr_Type,  Ctr_category,  Ctr_sub_category,ctr_Status,  Ctr_created_by,  Ctr_created_DtTm,Ctr_Mod_by,  Ctr_Mod_DtTm"};
        String selection = "Ctr_orga_id =?";
        String[] selectionArgs = {orga_id};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if(c.getCount()!=0)
        {
            while (c.moveToNext()) {
                //OptionList.add(c.getInt(c.getColumnIndex("Question_Option")));
                org=new SingleOrganization(c.getInt(c.getColumnIndex("Ctr_key")),c.getString(c.getColumnIndex("Ctr_orga_id")),
                        c.getString(c.getColumnIndex("Ctr_Name")),c.getString(c.getColumnIndex("Ctr_Short_name")),
                        c.getString(c.getColumnIndex("Ctr_Address01")),c.getString(c.getColumnIndex("Ctr_Address02")),
                        c.getString(c.getColumnIndex("Ctr_Area")),c.getString(c.getColumnIndex("Ctr_City_Town")),
                        c.getString(c.getColumnIndex("Ctr_State")),c.getString(c.getColumnIndex("Ctr_Country")),
                        c.getString(c.getColumnIndex("Ctr_Person")),c.getString(c.getColumnIndex("Ctr_Mobile")),
                        c.getString(c.getColumnIndex("Ctr_email")),c.getString(c.getColumnIndex("Ctr_logo_file")),
                        c.getString(c.getColumnIndex("Ctr_Start_Date")),c.getString(c.getColumnIndex("Ctr_End_Date")),
                        c.getString(c.getColumnIndex("Ctr_Type")),c.getString(c.getColumnIndex("Ctr_category")),
                        c.getString(c.getColumnIndex("Ctr_sub_category")),c.getString(c.getColumnIndex("ctr_Status")),
                        c.getString(c.getColumnIndex("Ctr_created_by")),c.getString(c.getColumnIndex("Ctr_created_DtTm")),
                        c.getString(c.getColumnIndex("Ctr_Mod_by")),c.getString(c.getColumnIndex("Ctr_Mod_DtTm")));
            }
        }
        c.close();
        return org;
    }

}
