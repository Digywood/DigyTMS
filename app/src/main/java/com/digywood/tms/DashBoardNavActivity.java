package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Fragments.CourseFragment;
import com.digywood.tms.Fragments.DashBoardFragment;
import com.digywood.tms.Fragments.EnrollFragment;
import com.digywood.tms.Fragments.LearningsFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DashBoardNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    View header;
    TextView tv_name,tv_email,tv_studentid,tv_enrollid;
    String studentid,spersonname,email;
    HashMap<String,String> hmap=new HashMap<>();
    DBHelper myhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_studentid=toolbar.findViewById(R.id.tv_studentid);
        tv_studentid.setText("SAA00001");
        tv_enrollid=toolbar.findViewById(R.id.tv_enrollid);
        tv_enrollid.setText("EAA000001");
        setSupportActionBar(toolbar);

        myhelper=new DBHelper(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fl_base, new DashBoardFragment());
        tx.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            spersonname=cmgintent.getStringExtra("sname");
            email=cmgintent.getStringExtra("email");
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header=navigationView.getHeaderView(0);
        tv_name=header.findViewById(R.id.tv_hsname);
        tv_name.setText(spersonname);
        tv_email=header.findViewById(R.id.tv_hsemail);
        tv_email.setText(email);
    }

    public void getStudentAllData(){
        hmap.clear();
        Log.e("LandingActivity---",studentid);
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl +"getStudentFullData.php",hmap,DashBoardNavActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                JSONArray ja_enrollments_table,ja_subjects_table,ja_papers_table,ja_tests_table,ja_assesmenttests;
                JSONObject enrollObj,subjectObj,paperObj,testObj,assesmentObj;

                try{
                    Log.e("MainActivity----","FullData"+json);

                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("enrollments");

                    if (obj1 instanceof JSONArray)
                    {
                        long prefdelcount=myhelper.deleteAllEnrollments();
                        Log.e("enrolldelcount---",""+prefdelcount);
                        ja_enrollments_table=myObj.getJSONArray("enrollments");
                        if(ja_enrollments_table!=null && ja_enrollments_table.length()>0){
                            Log.e("enrollLength---",""+ja_enrollments_table.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_enrollments_table.length();i++){

                                enrollObj=ja_enrollments_table.getJSONObject(i);
                                long insertFlag=myhelper.insertEnrollment(enrollObj.getInt("Enroll_key"),enrollObj.getString("Enroll_ID"),enrollObj.getString("Enroll_org_id"),enrollObj.getString("Enroll_Student_ID"),
                                        enrollObj.getString("Enroll_batch_ID"),enrollObj.getString("Enroll_course_ID"),enrollObj.getString("Enroll_batch_start_Dt"),enrollObj.getString("Enroll_batch_end_Dt"),
                                        enrollObj.getString("Enroll_Device_ID"),enrollObj.getString("Enroll_Date"),enrollObj.getString("Enroll_Status"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("Enrollments--","Inserted: "+p);
                        }else{
                            Log.e("Enrollments--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Enrollments--","No Enrollments: ");
                    }

                    Object obj2=myObj.get("subjects");

                    if (obj2 instanceof JSONArray)
                    {
                        long subdelcount=myhelper.deleteAllSubjects();
                        Log.e("subdelcount---",""+subdelcount);
                        ja_subjects_table=myObj.getJSONArray("subjects");
                        if(ja_subjects_table!=null && ja_subjects_table.length()>0){
                            Log.e("subLength---",""+ja_subjects_table.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_subjects_table.length();i++){

                                subjectObj=ja_subjects_table.getJSONObject(i);
                                long insertFlag=myhelper.insertSubject(subjectObj.getInt("Subject_key"),subjectObj.getString("Course_ID"),subjectObj.getString("Subject_ID"),subjectObj.getString("Subject_Name"),
                                        subjectObj.getString("Subject_ShortName"),subjectObj.getInt("Subject_Seq_no"),subjectObj.getString("Subject_Type"),subjectObj.getString("Subject_status"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("Subjects--","Inserted: "+p);
                        }else{
                            Log.e("Subjects--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Subjects--","No Subjects: ");
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

                                Cursor mycursor=myhelper.getSingleTestData(testObj.getString("sptu_ID"));
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
                                            testObj.getString("sptu_batch"),testObj.getString("sptu_ID"),testObj.getString("sptu_paper_ID"),testObj.getString("sptu_subjet_ID"),
                                            testObj.getString("sptu_course_id"),testObj.getString("sptu_start_date"),testObj.getString("sptu_end_date"),testObj.getString("sptu_dwnld_status"),
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
                        long atestdelcount=myhelper.deleteAllAssesmentTests();
                        Log.e("assesmentdelcount----",""+atestdelcount);
                        ja_assesmenttests=myObj.getJSONArray("assesmenttests");
                        if(ja_assesmenttests!=null && ja_assesmenttests.length()>0){
                            Log.e("atestLength---",""+ja_assesmenttests.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_assesmenttests.length();i++){

                                assesmentObj=ja_assesmenttests.getJSONObject(i);
                                long insertFlag=myhelper.insertAssesmentTest(assesmentObj.getInt("satu_key"),assesmentObj.getString("satu_org_id"),assesmentObj.getString("satu_entroll_id"),assesmentObj.getString("satu_student_id"),
                                        assesmentObj.getString("satu_batch"),assesmentObj.getString("satu_ID"),assesmentObj.getString("satu_paper_ID"),assesmentObj.getString("satu_subjet_ID"),
                                        assesmentObj.getString("satu_course_id"),assesmentObj.getString("satu_start_date"),assesmentObj.getString("satu_end_date"),assesmentObj.getString("satu_dwnld_status"),
                                        assesmentObj.getInt("satu_no_of_questions"),assesmentObj.getString("satu_file"),assesmentObj.getString("satu_exam_key"),assesmentObj.getDouble("satu_tot_marks"),
                                        assesmentObj.getDouble("satu_min_marks"),assesmentObj.getDouble("satu_max_marks"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("AssesmentTests--","Inserted: "+p);
                        }else{
                            Log.e("AssesmentTests--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("AssesmentTests--","No AssesmentTests: ");
                    }


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
            return true;
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

            fragmentClass = DashBoardFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_mylearnings) {

            fragmentClass = LearningsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_enrollments) {

            fragmentClass = EnrollFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_courses) {

            fragmentClass = CourseFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_contactus) {

        } else if (id == R.id.nav_info) {

        }

        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl_base, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
