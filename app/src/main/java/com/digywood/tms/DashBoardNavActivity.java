package com.digywood.tms;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Fragments.CourseFragment;
import com.digywood.tms.Fragments.HomeFragment;
import com.digywood.tms.Fragments.EnrollFragment;
import com.digywood.tms.Pojo.SingleDWDQues;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class DashBoardNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    View header;
    Dialog mydialog;
    String testType="",restoredsname="",finalUrl="",serverId="";
    SharedPreferences restoredprefs;
    TextView tv_name,tv_email,tv_studentid;
    String studentid,spersonname,email;
    HashMap<String,String> hmap=new HashMap<>();
    DBHelper myhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_studentid=toolbar.findViewById(R.id.tv_studentid);
        setSupportActionBar(toolbar);

        myhelper=new DBHelper(this);

        restoredprefs = getSharedPreferences("SERVERPREF", MODE_PRIVATE);
        restoredsname = restoredprefs.getString("servername","main");

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fl_base, new HomeFragment());
        tx.commit();

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            spersonname=cmgintent.getStringExtra("sname");
            email=cmgintent.getStringExtra("email");
            tv_studentid.setText(studentid);
        }

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header=navigationView.getHeaderView(0);
        tv_name=header.findViewById(R.id.tv_hsname);
        tv_name.setText(spersonname);
        tv_email=header.findViewById(R.id.tv_hsemail);
        tv_email.setText(email);

        Cursor mycursor=myhelper.getAllEnrolls(studentid);
        Log.e("EnrollCount---",""+mycursor.getCount());
        if(mycursor.getCount()>0){

        }else{
            mycursor.close();
            showAlert("No Existing Enrollments Found \n Please Sync from Server");
        }

//        if(restoredsname.equalsIgnoreCase("main")){
//            finalUrl=URLClass.hosturl;
//        }else{
//            serverId=myhelper.getServerId(restoredsname);
//            finalUrl="http://"+serverId+URLClass.loc_hosturl;
//        }

        Log.e("FINALURL:--",finalUrl);

    }

    public void getStudentAllData(){
        hmap.clear();
        Log.e("LearningActivity---",studentid);
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl +"getStudentFullData.php",hmap,DashBoardNavActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                JSONArray ja_enrollments_table,ja_courses_table,ja_papers_table,ja_tests_table,ja_assesmenttests,ja_tdata;
                JSONObject enrollObj,courseObj,paperObj,testObj,assesmentObj,testdataObj;

                try{
                    Log.e("MainActivity----","FullData"+json);

                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("enrollments");

                    if (obj1 instanceof JSONArray)
                    {
//                        long enrolldelcount=myhelper.deleteAllEnrollments();
//                        Log.e("enrolldelcount---",""+enrolldelcount);
                        ja_enrollments_table=myObj.getJSONArray("enrollments");
                        if(ja_enrollments_table!=null && ja_enrollments_table.length()>0){
                            Log.e("enrollLength---",""+ja_enrollments_table.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<ja_enrollments_table.length();i++){

                                enrollObj=ja_enrollments_table.getJSONObject(i);

                                long checkFlag=myhelper.checkEnrollment(enrollObj.getString("Enroll_ID"));

                                if(checkFlag>0){
                                    long updateFlag=myhelper.updateEnrollment(enrollObj.getString("Enroll_ID"),enrollObj.getString("Enroll_org_id"),enrollObj.getString("Enroll_Student_ID"),
                                            enrollObj.getString("Enroll_batch_ID"),enrollObj.getString("Enroll_course_ID"),enrollObj.getString("Enroll_batch_start_Dt"),enrollObj.getString("Enroll_batch_end_Dt"),
                                            enrollObj.getString("Enroll_Device_ID"),enrollObj.getString("Enroll_Date"),enrollObj.getString("Enroll_Status"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertEnrollment(enrollObj.getInt("Enroll_key"),enrollObj.getString("Enroll_ID"),enrollObj.getString("Enroll_org_id"),enrollObj.getString("Enroll_Student_ID"),
                                            enrollObj.getString("Enroll_batch_ID"),enrollObj.getString("Enroll_course_ID"),enrollObj.getString("Enroll_batch_start_Dt"),enrollObj.getString("Enroll_batch_end_Dt"),
                                            enrollObj.getString("Enroll_Device_ID"),enrollObj.getString("Enroll_Date"),enrollObj.getString("Enroll_Status"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("Enrollments--","Inserted: "+p+"  Updated: "+r);
                        }else{
                            Log.e("Enrollments--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Enrollments--","No Enrollments: ");
                    }

                    Object obj2=myObj.get("courses");

                    if (obj2 instanceof JSONArray)
                    {
                        long coursedelcount=myhelper.deleteAllCourses();
                        Log.e("coursedelcount---",""+coursedelcount);
                        ja_courses_table=myObj.getJSONArray("courses");
                        if(ja_courses_table!=null && ja_courses_table.length()>0){
                            Log.e("courseLength---",""+ja_courses_table.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_courses_table.length();i++){

                                courseObj=ja_courses_table.getJSONObject(i);

                                long insertFlag=myhelper.insertCourse(courseObj.getInt("Course_Key"),courseObj.getString("Course_ID"),courseObj.getString("Course_Name"),courseObj.getString("Course_Short_name"),
                                        courseObj.getString("Course_Type"),courseObj.getString("Course_Category"),courseObj.getString("Course_sub_category"),courseObj.getString("Course_duration_uom"),
                                        courseObj.getString("Cousre_Duration_min"),courseObj.getString("Course_Duration_Max"),courseObj.getString("Course_Status"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("Courses--","Inserted: "+p);
                        }else{
                            Log.e("Courses--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Courses--","No Courses: ");
                    }

                    Object obj3=myObj.get("papers");

                    if (obj3 instanceof JSONArray)
                    {
                        long paperdelcount=myhelper.deleteAllPapers();
                        Log.e("paperdelcount---",""+paperdelcount);
                        ja_papers_table=myObj.getJSONArray("papers");
                        if(ja_papers_table!=null && ja_papers_table.length()>0){
                            Log.e("paperLength---",""+ja_papers_table.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_papers_table.length();i++){

                                paperObj=ja_papers_table.getJSONObject(i);
                                long insertFlag=myhelper.insertPaper(paperObj.getInt("Paper_Key"),paperObj.getString("Paper_ID"),paperObj.getString("Paper_Seq_no"),paperObj.getString("Subject_ID"),
                                        paperObj.getString("Course_ID"),paperObj.getString("Paper_Name"),paperObj.getString("Paper_Short_Name"),paperObj.getString("Paper_Min_Pass_Marks"),
                                        paperObj.getString("Paper_Max_Marks"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("Papers--","Inserted: "+p);
                        }else{
                            Log.e("Papers--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Papers--","No Papers: ");
                    }

                    Object obj4=myObj.get("tests");

                    if (obj4 instanceof JSONArray)
                    {
//                        long testdelcount=myhelper.deleteAllTests();
//                        Log.e("testdelcount---",""+testdelcount);
                        ja_tests_table=myObj.getJSONArray("tests");
                        if(ja_tests_table!=null && ja_tests_table.length()>0){
                            Log.e("testLength---",""+ja_tests_table.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<ja_tests_table.length();i++){

                                testObj=ja_tests_table.getJSONObject(i);

                                Cursor mycursor=myhelper.checkPractiseTest(studentid,testObj.getString("sptu_entroll_id"),testObj.getString("sptu_ID"));
                                if(mycursor.getCount()>0){
                                    long updateFlag=myhelper.updatePractiseTestData(testObj.getString("sptu_org_id"),testObj.getString("sptu_entroll_id"),testObj.getString("sptu_student_ID"),
                                            testObj.getString("sptu_batch"),testObj.getString("sptu_ID"),testObj.getString("sptu_paper_ID"),testObj.getString("sptu_subjet_ID"),
                                            testObj.getString("sptu_course_id"),testObj.getString("sptu_start_date"),testObj.getString("sptu_end_date"),testObj.getString("sptu_dwnld_status"),
                                            testObj.getInt("sptu_no_of_questions"),testObj.getDouble("sptu_tot_marks"),testObj.getDouble("stpu_min_marks"),testObj.getDouble("sptu_max_marks"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertPractiseTest(testObj.getInt("sptu_key"),testObj.getString("sptu_org_id"),testObj.getString("sptu_entroll_id"),testObj.getString("sptu_student_ID"),
                                            testObj.getString("sptu_batch"),testObj.getString("sptu_ID"),testObj.getString("sptu_name"),testObj.getString("sptu_paper_ID"),testObj.getString("sptu_subjet_ID"),
                                            testObj.getString("sptu_course_id"),testObj.getString("sptu_start_date"),testObj.getString("sptu_end_date"),testObj.getString("sptu_dwnld_status"),testObj.getString("sptu_status"),
                                            testObj.getInt("sptu_no_of_questions"),testObj.getDouble("sptu_tot_marks"),testObj.getDouble("stpu_min_marks"),testObj.getDouble("sptu_max_marks"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }

                            }
                            Log.e("Tests--","Inserted: "+p+"-- Updated---"+r);
                        }else{
                            Log.e("Tests--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Tests--","No Tests: ");
                    }

                    Object obj5=myObj.get("assesmenttests");

                    if (obj5 instanceof JSONArray)
                    {
//                        long atestdelcount=myhelper.deleteAllAssesmentTests();
//                        Log.e("assesmentdelcount----",""+atestdelcount);
                        ja_assesmenttests=myObj.getJSONArray("assesmenttests");
                        if(ja_assesmenttests!=null && ja_assesmenttests.length()>0){
                            Log.e("atestLength---",""+ja_assesmenttests.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<ja_assesmenttests.length();i++){

                                assesmentObj=ja_assesmenttests.getJSONObject(i);

                                Cursor mycursor=myhelper.checkAssessmentTest(studentid,assesmentObj.getString("satu_entroll_id"),assesmentObj.getString("satu_ID"));

                                if(mycursor.getCount()>0){
                                    long updateFlag=myhelper.updateAssesmentTest(assesmentObj.getString("satu_org_id"),assesmentObj.getString("satu_entroll_id"),assesmentObj.getString("satu_student_id"),
                                            assesmentObj.getString("satu_batch"),assesmentObj.getString("satu_ID"),assesmentObj.getString("satu_name"),assesmentObj.getString("satu_paper_ID"),assesmentObj.getString("satu_subjet_ID"),
                                            assesmentObj.getString("satu_course_id"),assesmentObj.getString("satu_start_date"),assesmentObj.getString("satu_end_date"),assesmentObj.getString("satu_dwnld_status"),
                                            assesmentObj.getInt("satu_no_of_questions"),assesmentObj.getString("satu_file"),assesmentObj.getString("satu_exam_key"),assesmentObj.getDouble("satu_tot_marks"),
                                            assesmentObj.getDouble("satu_min_marks"),assesmentObj.getDouble("satu_max_marks"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertAssesmentTest(assesmentObj.getInt("satu_key"),assesmentObj.getString("satu_org_id"),assesmentObj.getString("satu_entroll_id"),assesmentObj.getString("satu_student_id"),
                                            assesmentObj.getString("satu_batch"),assesmentObj.getString("satu_ID"),assesmentObj.getString("satu_name"),assesmentObj.getString("satu_paper_ID"),assesmentObj.getString("satu_subjet_ID"),
                                            assesmentObj.getString("satu_course_id"),assesmentObj.getString("satu_start_date"),assesmentObj.getString("satu_end_date"),assesmentObj.getString("satu_dwnld_status"),
                                            assesmentObj.getInt("satu_no_of_questions"),assesmentObj.getString("satu_file"),assesmentObj.getString("satu_exam_key"),assesmentObj.getDouble("satu_tot_marks"),
                                            assesmentObj.getDouble("satu_min_marks"),assesmentObj.getDouble("satu_max_marks"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("AssesmentTests--","Inserted: "+p+"   Updated:  "+r);
                        }else{
                            Log.e("AssesmentTests--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("AssesmentTests--","No AssesmentTests: ");
                    }

                    Object obj6=myObj.get("testdata");

                    if (obj6 instanceof JSONArray)
                    {
//                        long tdatadelcount=myhelper.deleteTestRawData();
//                        Log.e("tdatadelcount----",""+tdatadelcount);
                        ja_tdata=myObj.getJSONArray("testdata");
                        if(ja_tdata!=null && ja_tdata.length()>0){
                            Log.e("tdataLength---",""+ja_tdata.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<ja_tdata.length();i++){

                                testdataObj=ja_tdata.getJSONObject(i);

                                Cursor mycursor=myhelper.checkTestAggrigateData(testdataObj.getString("testId"),testdataObj.getString("testType"));

                                if(mycursor.getCount()>0){

                                    long updateFlag=myhelper.updateTestAggrigateRecord(testdataObj.getString("testId"),testdataObj.getString("testType"),testdataObj.getString("test_OrgId"),
                                            testdataObj.getString("test_batchId"),testdataObj.getString("test_courseId"),testdataObj.getString("test_paperId"),testdataObj.getString("test_subjectId"),
                                            testdataObj.getDouble("minPercentage"),testdataObj.getDouble("maxPercentage"),testdataObj.getDouble("avgPercentage"),testdataObj.getInt("minAttempts"),
                                            testdataObj.getInt("maxAttempts"),testdataObj.getInt("avgAttempts"),testdataObj.getInt("flag"),testdataObj.getString("createdBy"),
                                            testdataObj.getString("createdDttm"),testdataObj.getString("modifiedBy"),testdataObj.getString("modifiedDttm"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }

                                }else{

                                    long insertFlag=myhelper.insertTestAggrigateRecord(testdataObj.getInt("testKey"),testdataObj.getString("testId"),testdataObj.getString("testType"),testdataObj.getString("test_OrgId"),
                                            testdataObj.getString("test_batchId"),testdataObj.getString("test_courseId"),testdataObj.getString("test_paperId"),testdataObj.getString("test_subjectId"),
                                            testdataObj.getDouble("minPercentage"),testdataObj.getDouble("maxPercentage"),testdataObj.getDouble("avgPercentage"),testdataObj.getInt("minAttempts"),
                                            testdataObj.getInt("maxAttempts"),testdataObj.getInt("avgAttempts"),testdataObj.getInt("flag"),testdataObj.getString("createdBy"),
                                            testdataObj.getString("createdDttm"),testdataObj.getString("modifiedBy"),testdataObj.getString("modifiedDttm"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }

                                }

                            }
                            Log.e("TestRawData--","Inserted: "+p+"  Updated:  "+r);
                        }else{
                            Log.e("TestRawData--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("TestRawData--","No Tests Data: ");
                    }

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("MainActivity-------",e.toString());
                }
            }
        }).execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {

            testType="";

            mydialog = new Dialog(DashBoardNavActivity.this);
            mydialog.getWindow();
            mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mydialog.setContentView(R.layout.activity_syncpopup);
            mydialog.show();

            RadioGroup rg_testtype=mydialog.findViewById(R.id.rg_stesttype);
            Button btn_sync=mydialog.findViewById(R.id.btn_popupsync);

            rg_testtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch(checkedId)
                    {
                        case R.id.rb_ptsync:
                            testType="Practise";
                            break;
                        case R.id.rb_ftsync:
                            testType="Flash";
                            break;
                        case R.id.rb_atsync:
                            testType="Assessment";
                            break;
                    }
                }
            });

            btn_sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(testType.equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please Choose Test",Toast.LENGTH_SHORT).show();
                    }else{
                        mydialog.cancel();
                        if(testType.equalsIgnoreCase("Practise")){
                            syncPractiseTestData();
                        }else if(testType.equalsIgnoreCase("Flash")){
                            syncFlashCardData();
                        }else if(testType.equalsIgnoreCase("Assessment")){
                            syncAssesmentTestData();
                        }else{

                        }
                    }
                }
            });

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass=null;

        if (id == R.id.nav_dashboard) {

            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }  else if (id == R.id.nav_enrollments) {

            fragmentClass = EnrollFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_reqenrolls) {

            Intent i=new Intent(getApplicationContext(),RequestedEnrollsActivity.class);
            i.putExtra("studentid",studentid);
            startActivity(i);

        } else if (id == R.id.nav_courses) {

            fragmentClass = CourseFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_syncronise) {

            new AsyncCheckInternet(DashBoardNavActivity.this,new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        getStudentAllData();
                    }else{
                        Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        } else if (id == R.id.nav_configserver) {

            Toast.makeText(getApplicationContext(),"restoredsname:--"+restoredsname,Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(),ListofServers.class);
            i.putExtra("studentid",studentid);
            startActivity(i);

        } else if (id == R.id.nav_contactus) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_logout) {
            showAlertwithTwoButtons("Would you like to exit?");
        }

        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl_base, fragment).commit();
        }

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAlert(String messege) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardNavActivity.this,R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public  void showAlertwithTwoButtons(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardNavActivity.this,R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>"+messege+"</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        finish();

                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public  void syncPractiseTestData(){

        JSONObject finalPractiseObj=new JSONObject();
        Cursor mycursor=myhelper.getPractiseUploadData(studentid,"NotUploaded");
        if(mycursor.getCount()>0){
            try{
                JSONArray PrcatiseList = new JSONArray();
                JSONObject PractiseTest;
                while (mycursor.moveToNext()){
                    PractiseTest = new JSONObject();
                    PractiseTest.put("Attempt_ID",mycursor.getString(mycursor.getColumnIndex("Attempt_ID")));
                    PractiseTest.put("Attempt_Test_ID",mycursor.getString(mycursor.getColumnIndex("Attempt_Test_ID")));
                    PractiseTest.put("Attempt_enrollId",mycursor.getString(mycursor.getColumnIndex("Attempt_enrollId")));
                    PractiseTest.put("Attempt_studentId",mycursor.getString(mycursor.getColumnIndex("Attempt_studentId")));
                    PractiseTest.put("Attempt_courseId",mycursor.getString(mycursor.getColumnIndex("Attempt_courseId")));
                    PractiseTest.put("Attempt_subjectId",mycursor.getString(mycursor.getColumnIndex("Attempt_subjectId")));
                    PractiseTest.put("Attempt_paperId",mycursor.getInt(mycursor.getColumnIndex("Attempt_paperId")));
                    PractiseTest.put("Attempt_Status",mycursor.getString(mycursor.getColumnIndex("Attempt_Status")));
                    PractiseTest.put("Attempt_RemainingTime",mycursor.getString(mycursor.getColumnIndex("Attempt_RemainingTime")));
                    PractiseTest.put("Attempt_LastQuestion",mycursor.getInt(mycursor.getColumnIndex("Attempt_LastQuestion")));
                    PractiseTest.put("Attempt_LastSection",mycursor.getInt(mycursor.getColumnIndex("Attempt_LastSection")));
                    PractiseTest.put("Attempt_Confirmed",mycursor.getInt(mycursor.getColumnIndex("Attempt_Confirmed")));
                    PractiseTest.put("Attempt_Skipped",mycursor.getInt(mycursor.getColumnIndex("Attempt_Skipped")));
                    PractiseTest.put("Attempt_Bookmarked",mycursor.getDouble(mycursor.getColumnIndex("Attempt_Bookmarked")));
                    PractiseTest.put("Attempt_UnAttempted",mycursor.getInt(mycursor.getColumnIndex("Attempt_UnAttempted")));
                    PractiseTest.put("Attempt_Score",mycursor.getDouble(mycursor.getColumnIndex("Attempt_Score")));
                    PrcatiseList.put(PractiseTest);
                }
                finalPractiseObj.put("PractiseData",PrcatiseList);

                Cursor mycur=myhelper.getAllPTestData(studentid,"NotUploaded");

                if(mycur.getCount()>0){
                    try{
                        JSONArray TestList = new JSONArray();
                        JSONObject TestData;
                        while (mycur.moveToNext()){
                            TestData = new JSONObject();
                            TestData.put("sptu_student_ID",mycur.getString(mycur.getColumnIndex("sptu_student_ID")));
                            TestData.put("sptu_ID",mycur.getString(mycur.getColumnIndex("sptu_ID")));
                            TestData.put("sptu_dwnld_start_dttm",mycur.getString(mycur.getColumnIndex("sptu_dwnld_start_dttm")));
                            TestData.put("sptu_dwnld_completed_dttm",mycur.getString(mycur.getColumnIndex("sptu_dwnld_completed_dttm")));
                            TestData.put("sptu_dwnld_status",mycur.getString(mycur.getColumnIndex("sptu_dwnld_status")));
                            TestData.put("sptu_no_of_questions",mycur.getInt(mycur.getColumnIndex("sptu_no_of_questions")));
                            TestData.put("sptu_tot_marks",mycur.getDouble(mycur.getColumnIndex("sptu_tot_marks")));
                            TestData.put("stpu_min_marks",mycur.getDouble(mycur.getColumnIndex("stpu_min_marks")));
                            TestData.put("sptu_max_marks",mycur.getDouble(mycur.getColumnIndex("sptu_max_marks")));
                            TestData.put("sptu_avg_marks",mycur.getDouble(mycur.getColumnIndex("sptu_avg_marks")));
                            TestData.put("sptu_min_percent",mycur.getDouble(mycur.getColumnIndex("sptu_min_percent")));
                            TestData.put("sptu_max_percent",mycur.getDouble(mycur.getColumnIndex("sptu_max_percent")));
                            TestData.put("sptu_avg_percent",mycur.getDouble(mycur.getColumnIndex("sptu_avg_percent")));
                            TestData.put("sptu_last_attempt_marks",mycur.getDouble(mycur.getColumnIndex("sptu_last_attempt_marks")));
                            TestData.put("sptu_last_attempt_percent",mycur.getDouble(mycur.getColumnIndex("sptu_last_attempt_percent")));
                            TestData.put("sptu_last_attempt_start_dttm",mycur.getString(mycur.getColumnIndex("sptu_last_attempt_start_dttm")));
                            TestData.put("sptu_last_attempt_end_dttm",mycur.getString(mycur.getColumnIndex("sptu_last_attempt_end_dttm")));
                            TestData.put("sptu_no_of_attempts",mycur.getInt(mycur.getColumnIndex("sptu_no_of_attempts")));
                            TestData.put("sptuflash_attempts",mycur.getInt(mycur.getColumnIndex("sptuflash_attempts")));
                            TestData.put("min_flashScore",mycur.getDouble(mycur.getColumnIndex("min_flashScore")));
                            TestData.put("max_flashScore",mycur.getDouble(mycur.getColumnIndex("max_flashScore")));
                            TestData.put("avg_flashScore",mycur.getDouble(mycur.getColumnIndex("avg_flashScore")));
                            TestData.put("lastAttemptDttm",mycur.getString(mycur.getColumnIndex("lastAttemptDttm")));
                            TestData.put("lastAttemptScore",mycur.getDouble(mycur.getColumnIndex("lastAttemptScore")));
                            TestList.put(TestData);
                        }
                        Log.e("TestListSize:--",""+TestList.length());
                        finalPractiseObj.put("TestData",TestList);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("DBNActivity----",e.toString());
                    }
                }else{
                    mycur.close();
                }

                hmap.clear();
                hmap.put("jsonstr",finalPractiseObj.toString());
                new BagroundTask(URLClass.hosturl +"syncPractiseTestData.php", hmap,DashBoardNavActivity.this,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            Log.e("json"," comes :  "+json);
                            if(json.equalsIgnoreCase("Inserted")){
                                Toast.makeText(getApplicationContext(),"Practise Test Info Syncronised",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Sorry,Try Again Later",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("DashBoardNavActivity","  :  "+e.toString());
                        }
                    }
                }).execute();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("DashNavActivity-----",e.toString());
            }
        }else{
            mycursor.close();
            Toast.makeText(getApplicationContext(),"No Practise Data to Upload",Toast.LENGTH_SHORT).show();
        }

    }

    public  void syncFlashCardData(){

        JSONObject finalFlashObj=new JSONObject();

        Cursor mycursor=myhelper.getFlashUploadData("NotUploaded");
        if(mycursor.getCount()>0){
            try{
                JSONArray FlashList = new JSONArray();
                JSONObject FlashData;
                while (mycursor.moveToNext()){
                    FlashData = new JSONObject();
                    FlashData.put("flashUID",mycursor.getString(mycursor.getColumnIndex("flashUID")));
                    FlashData.put("studentId",mycursor.getString(mycursor.getColumnIndex("studentId")));
                    FlashData.put("enrollmentId",mycursor.getString(mycursor.getColumnIndex("enrollmentId")));
                    FlashData.put("courseId",mycursor.getString(mycursor.getColumnIndex("courseId")));
                    FlashData.put("subjectId",mycursor.getString(mycursor.getColumnIndex("subjectId")));
                    FlashData.put("paperId",mycursor.getString(mycursor.getColumnIndex("paperId")));
                    FlashData.put("flashcardId",mycursor.getString(mycursor.getColumnIndex("flashcardId")));
                    FlashData.put("attemptNumber",mycursor.getInt(mycursor.getColumnIndex("attemptNumber")));
                    FlashData.put("startDttm",mycursor.getString(mycursor.getColumnIndex("startDttm")));
                    FlashData.put("endDttm",mycursor.getString(mycursor.getColumnIndex("endDttm")));
                    FlashData.put("attemptQCount",mycursor.getInt(mycursor.getColumnIndex("attemptQCount")));
                    FlashData.put("iknowCount",mycursor.getInt(mycursor.getColumnIndex("iknowCount")));
                    FlashData.put("donknowCount",mycursor.getInt(mycursor.getColumnIndex("donknowCount")));
                    FlashData.put("skipCount",mycursor.getInt(mycursor.getColumnIndex("skipCount")));
                    FlashData.put("percentageObtain",mycursor.getDouble(mycursor.getColumnIndex("percentageObtain")));
                    FlashList.put(FlashData);
                }

                finalFlashObj.put("FlashData",FlashList);

                Cursor mycur=myhelper.getAllPTestData(studentid,"NotUploaded");

                if(mycur.getCount()>0){
                    try{
                        JSONArray TestList = new JSONArray();
                        JSONObject TestData;
                        while (mycur.moveToNext()){
                            TestData = new JSONObject();
                            TestData.put("sptu_student_ID",mycur.getString(mycur.getColumnIndex("sptu_student_ID")));
                            TestData.put("sptu_ID",mycur.getString(mycur.getColumnIndex("sptu_ID")));
                            TestData.put("sptu_dwnld_start_dttm",mycur.getString(mycur.getColumnIndex("sptu_dwnld_start_dttm")));
                            TestData.put("sptu_dwnld_completed_dttm",mycur.getString(mycur.getColumnIndex("sptu_dwnld_completed_dttm")));
                            TestData.put("sptu_dwnld_status",mycur.getString(mycur.getColumnIndex("sptu_dwnld_status")));
                            TestData.put("sptu_no_of_questions",mycur.getInt(mycur.getColumnIndex("sptu_no_of_questions")));
                            TestData.put("sptu_tot_marks",mycur.getDouble(mycur.getColumnIndex("sptu_tot_marks")));
                            TestData.put("stpu_min_marks",mycur.getDouble(mycur.getColumnIndex("stpu_min_marks")));
                            TestData.put("sptu_max_marks",mycur.getDouble(mycur.getColumnIndex("sptu_max_marks")));
                            TestData.put("sptu_avg_marks",mycur.getDouble(mycur.getColumnIndex("sptu_avg_marks")));
                            TestData.put("sptu_min_percent",mycur.getDouble(mycur.getColumnIndex("sptu_min_percent")));
                            TestData.put("sptu_max_percent",mycur.getDouble(mycur.getColumnIndex("sptu_max_percent")));
                            TestData.put("sptu_avg_percent",mycur.getDouble(mycur.getColumnIndex("sptu_avg_percent")));
                            TestData.put("sptu_last_attempt_marks",mycur.getDouble(mycur.getColumnIndex("sptu_last_attempt_marks")));
                            TestData.put("sptu_last_attempt_percent",mycur.getDouble(mycur.getColumnIndex("sptu_last_attempt_percent")));
                            TestData.put("sptu_last_attempt_start_dttm",mycur.getString(mycur.getColumnIndex("sptu_last_attempt_start_dttm")));
                            TestData.put("sptu_last_attempt_end_dttm",mycur.getString(mycur.getColumnIndex("sptu_last_attempt_end_dttm")));
                            TestData.put("sptu_no_of_attempts",mycur.getInt(mycur.getColumnIndex("sptu_no_of_attempts")));
                            TestData.put("sptuflash_attempts",mycur.getInt(mycur.getColumnIndex("sptuflash_attempts")));
                            TestData.put("min_flashScore",mycur.getDouble(mycur.getColumnIndex("min_flashScore")));
                            TestData.put("max_flashScore",mycur.getDouble(mycur.getColumnIndex("max_flashScore")));
                            TestData.put("avg_flashScore",mycur.getDouble(mycur.getColumnIndex("avg_flashScore")));
                            TestData.put("lastAttemptDttm",mycur.getString(mycur.getColumnIndex("lastAttemptDttm")));
                            TestData.put("lastAttemptScore",mycur.getDouble(mycur.getColumnIndex("lastAttemptScore")));
                            TestList.put(TestData);
                        }
                        Log.e("TestListSize:--",""+TestList.length());
                        finalFlashObj.put("TestData",TestList);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("DBNActivity----",e.toString());
                    }
                }else{
                    mycur.close();
                }

                hmap.clear();
//                Log.e("GNGJSON:--",finalFlashObj.toString());
                hmap.put("jsonstr",finalFlashObj.toString());
                new BagroundTask(URLClass.hosturl+"syncFlashData.php", hmap,DashBoardNavActivity.this,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            JSONArray ja_testIds,ja_flashIds;
                            JSONObject testObj=null,flashObj=null;
                            Log.e("json"," comes :  "+json);
                            JSONObject mainObj=new JSONObject(json);


                            Object obj1=mainObj.get("testIds");

                            if (obj1 instanceof JSONArray)
                            {
                                ja_testIds=mainObj.getJSONArray("testIds");
                                if(ja_testIds!=null && ja_testIds.length()>0){
                                    int p=0,q=0;
                                    Log.e("DBNActivity---","updated_test_rec:--"+ja_testIds.length());
                                    for(int i=0;i<ja_testIds.length();i++){
                                        testObj=ja_testIds.getJSONObject(i);
                                        long updateFlag=myhelper.updatePTestUPLDStatus(studentid,testObj.getString("testId"),"Uploaded");
                                        if(updateFlag>0){
                                            p++;
                                        }else{
                                            q++;
                                        }
                                    }
                                    Log.e("DBNActivity---","TUpdated:--"+p);
                                }else{
                                    Log.e("TestUPLDData--","Null Tests Json Array: ");
                                }

                            }
                            else {
                                Log.e("TestUPLDData--","No Tests Uploaded: ");
                            }

                            Object obj2=mainObj.get("flashIds");

                            if (obj2 instanceof JSONArray)
                            {
                                ja_flashIds=mainObj.getJSONArray("flashIds");
                                if(ja_flashIds.length()>0 && ja_flashIds!=null){
                                    int p=0,q=0;
                                    Log.e("DBNActivity---","updated_flash_rec:--"+ja_flashIds.length());
                                    for(int i=0;i<ja_flashIds.length();i++){
                                        flashObj=ja_flashIds.getJSONObject(i);
                                        long updateFlag=myhelper.updateFAttemptStatus(flashObj.getString("flashUID"),"Uploaded");
                                        if(updateFlag>0){
                                            p++;
                                        }else{
                                            q++;
                                        }
                                    }
                                    Log.e("DBNActivity---","FUpdated:--"+p);
                                }else{
                                    Log.e("FlashUPLDData--","Null Flash Tests Json Array: ");
                                }

                            }
                            else {
                                Log.e("FlashUPLDData--","No Flash Tests Uploaded: ");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("DashBoardNavActivity","  :  "+e.toString()+"lineno:--"+e.getStackTrace()[0].getLineNumber());
                        }
                    }
                }).execute();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("DashNavActivity-----",e.toString());
            }
        }else{
            mycursor.close();
            Toast.makeText(getApplicationContext(),"No FlashCard Data to Upload",Toast.LENGTH_SHORT).show();
        }

    }

    public  void syncAssesmentTestData(){

        JSONObject finalAssessmentObj=new JSONObject();
        Cursor mycursor=myhelper.getAssessmentUploadData("NotUploaded");
        if(mycursor.getCount()>0){
            try{
                JSONArray AssessmentList = new JSONArray();
                JSONObject AssessmentTestQues;
                while (mycursor.moveToNext()){
                    AssessmentTestQues = new JSONObject();
                    AssessmentTestQues.put("Test_ID",mycursor.getString(mycursor.getColumnIndex("Test_ID")));
                    AssessmentTestQues.put("Question_Key",mycursor.getString(mycursor.getColumnIndex("Question_Key")));
                    AssessmentTestQues.put("Question_ID",mycursor.getString(mycursor.getColumnIndex("Question_ID")));
                    AssessmentTestQues.put("Question_Seq_No",mycursor.getString(mycursor.getColumnIndex("Question_Seq_No")));
                    AssessmentTestQues.put("Question_Section",mycursor.getString(mycursor.getColumnIndex("Question_Section")));
                    AssessmentTestQues.put("Question_Category",mycursor.getString(mycursor.getColumnIndex("Question_Category")));
                    AssessmentTestQues.put("Question_SubCategory",mycursor.getString(mycursor.getColumnIndex("Question_SubCategory")));
                    AssessmentTestQues.put("Question_Max_Marks",mycursor.getInt(mycursor.getColumnIndex("Question_Max_Marks")));
                    AssessmentTestQues.put("Question_Negative_Marks",mycursor.getString(mycursor.getColumnIndex("Question_Negative_Marks")));
                    AssessmentTestQues.put("Question_Marks_Obtained",mycursor.getString(mycursor.getColumnIndex("Question_Marks_Obtained")));
                    AssessmentTestQues.put("Question_Negative_Applied",mycursor.getInt(mycursor.getColumnIndex("Question_Negative_Applied")));
                    AssessmentTestQues.put("Question_Option",mycursor.getInt(mycursor.getColumnIndex("Question_Option")));
                    AssessmentTestQues.put("Question_Status",mycursor.getInt(mycursor.getColumnIndex("Question_Status")));
                    AssessmentTestQues.put("Question_Upload_Status",mycursor.getInt(mycursor.getColumnIndex("Question_Upload_Status")));
                    AssessmentTestQues.put("Question_Option_Sequence",mycursor.getDouble(mycursor.getColumnIndex("Question_Option_Sequence")));
                    AssessmentTestQues.put("Option_Answer_Flag",mycursor.getInt(mycursor.getColumnIndex("Option_Answer_Flag")));
                    AssessmentList.put(AssessmentTestQues);
                }
                finalAssessmentObj.put("AssessmentTestData",AssessmentList);

                hmap.clear();
                hmap.put("jsonstr",finalAssessmentObj.toString());
                new BagroundTask(URLClass.hosturl +"syncAssessmentTestData.php", hmap,DashBoardNavActivity.this,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            JSONArray ja_assessmentKeys;
                            JSONObject assessmentObj;
                            Log.e("json"," comes :  "+json);
//                            if(json.equalsIgnoreCase("Inserted")){
//                                Toast.makeText(getApplicationContext(),"Practise Test Info Syncronised",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Sorry,Try Again Later",Toast.LENGTH_SHORT).show();
//                            }

                            JSONObject mainObj=new JSONObject(json);

                            Object obj1=mainObj.get("assessmentIds");

                            if (obj1 instanceof JSONArray)
                            {
                                ja_assessmentKeys=mainObj.getJSONArray("assessmentIds");
                                if(ja_assessmentKeys!=null && ja_assessmentKeys.length()>0){
                                    int p=0,q=0;
                                    Log.e("DBNActivity---","updated_assesstestQ_rec:--"+ja_assessmentKeys.length());
                                    for(int i=0;i<ja_assessmentKeys.length();i++){
                                        assessmentObj=ja_assessmentKeys.getJSONObject(i);
                                        long updateFlag=myhelper.updateAssessmentQStatus(assessmentObj.getString("assessmentKey"),"Uploaded");
                                        if(updateFlag>0){
                                            p++;
                                        }else{
                                            q++;
                                        }
                                    }
                                    Log.e("DBNActivity---","AQUpdated:--"+p);
                                }else{
                                    Log.e("ATESTQUPLDData--","Null Assessment Ques Json Array: ");
                                }

                            }
                            else {
                                Log.e("ATESTQUPLDData--","No Assessment Ques Uploaded: ");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("DashBoardNavActivity","  :  "+e.toString());
                        }
                    }
                }).execute();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("DashNavActivity-----",e.toString());
            }
        }else{
            mycursor.close();
            Toast.makeText(getApplicationContext(),"No Assessment Ques Data to Upload",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        showAlertwithTwoButtons("Would you like to exit?");
    }

}
