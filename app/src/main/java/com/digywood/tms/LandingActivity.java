package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LandingActivity extends AppCompatActivity {

    RecyclerView rv_enroll;
    TextView tv_emptyenroll;
    String studentid="",studentname="",RandomAudioFileName="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    DBHelper myhelper;
    Random random ;
    ArrayList<String> enrollids;
    ArrayList<String> enrollcourseids;
    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleEnrollment> enrollList;
    LinearLayoutManager myLayoutManager;
    EnrollAdapter eAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if(Build.VERSION.SDK_INT>=21) {

            final Drawable upArrow = getApplicationContext().getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        rv_enroll=findViewById(R.id.rv_listofenrolls);
        tv_emptyenroll=findViewById(R.id.tv_enrollemptydata);
        enrollids=new ArrayList<>();
        enrollcourseids=new ArrayList<>();
        enrollList=new ArrayList<>();

        myhelper=new DBHelper(this);
        random=new Random();

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }

        getEnrollsFromLocal();

    }

    public void getEnrolls(){
        hmap.clear();
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl + "getStudentEnrolls.php", hmap, LandingActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("LandingActivity----","json:comes: "+json);
                    if(json.equalsIgnoreCase("Enroll_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Enrollments for Students",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        for(int i=0;i<ja.length();i++){
                            myObj=ja.getJSONObject(i);
                            String enrollid=myObj.getString("Enroll_ID");
                            String enrollcourseid=myObj.getString("Enroll_course_ID");
                            enrollids.add(enrollid);
                            enrollcourseids.add(enrollcourseid);

                            long checkFlag=myhelper.checkEnrollment(myObj.getInt("Enroll_key"));
                            if(checkFlag>0){
                                Log.e("LandingActivity----","Enroll Already Exists");
                            }else{
                                long insertFlag=myhelper.insertEnrollment(myObj.getInt("Enroll_key"),myObj.getString("Enroll_ID"),myObj.getString("Enroll_org_id"),myObj.getString("Enroll_Student_ID"),
                                        myObj.getString("Enroll_batch_ID"),myObj.getString("Enroll_course_ID"),myObj.getString("Enroll_batch_start_Dt"),myObj.getString("Enroll_batch_end_Dt"),
                                        myObj.getString("Enroll_Device_ID"),myObj.getString("Enroll_Date"),myObj.getString("Enroll_Status"));
                                if(insertFlag>0){
                                    Log.e("LandingActivity----","Enroll Inserted in Local");
                                }else{
                                    Log.e("LandingActivity----","Local Enroll Insertion Failed");
                                }
                            }
                        }
                    }
                    getEnrollsFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("LandingActivity----",e.toString());
                }
                setData();
            }
        }).execute();
    }

    public void getEnrollsFromLocal(){
        enrollList=myhelper.getStudentEnrolls();
        setData();
    }

    public void setData(){
        if (enrollList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + enrollList.size());
            tv_emptyenroll.setVisibility(View.GONE);
            eAdp = new EnrollAdapter(enrollList,LandingActivity.this);
            myLayoutManager = new LinearLayoutManager(LandingActivity.this, LinearLayoutManager.VERTICAL,false);
            rv_enroll.setLayoutManager(myLayoutManager);
            rv_enroll.setItemAnimator(new DefaultItemAnimator());
            rv_enroll.setAdapter(eAdp);
        } else {
            tv_emptyenroll.setText("No Enrollments for student");
            tv_emptyenroll.setVisibility(View.VISIBLE);
        }
    }

    public void getStudentAllData(){
        hmap.clear();
        Log.e("LandingActivity---",studentid);
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl +"getStudentFullData.php",hmap,LandingActivity.this, new IBagroundListener() {
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

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new AsyncCheckInternet(LandingActivity.this,new INetStatus() {
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){
                            getStudentAllData();
                        }else{
                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
                return  true;
            case R.id.action_newenroll:
                Intent i=new Intent(getApplicationContext(),EnrollRequestActivity.class);
                i.putExtra("studentid",studentid);
                startActivity(i);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
