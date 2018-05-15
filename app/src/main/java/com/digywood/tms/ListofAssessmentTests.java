package com.digywood.tms;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.AssesmentTestAdapter;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleAssessment;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleTest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListofAssessmentTests extends AppCompatActivity {

    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleAssessment> testidList;
    ArrayList<String> subjectIds;
    ArrayList<String> groupIds=new ArrayList<>();
    RecyclerView rv_tests;
    TextView tv_emptytests;
    StringBuilder output;
    int currentitem;
    DBHelper myhelper;
    public static final int RequestPermissionCode = 1;
    AssesmentTestAdapter tAdp;
    ArrayList<String> finalUrls,finalNames,finalPaths,selectedtestidList;
    ArrayList<String> downloadfileList;
    ArrayList<SingleDWDQues> chapterFileList;
    ArrayList<String> chapterList;
    ArrayList<String> localPathList;
    ArrayList<String> alreadydwdList;
    ArrayList<SingleTest> dwdupdateList;
    LinearLayoutManager myLayoutManager;
    String enrollid="",courseid="",paperid="",subjectid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assesmenttests);

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

        rv_tests=findViewById(R.id.rv_listofatests);
        tv_emptytests=findViewById(R.id.tv_atestemptydata);
        testidList=new ArrayList<>();
        finalUrls=new ArrayList<>();
        finalNames=new ArrayList<>();
        finalPaths=new ArrayList<>();
        subjectIds=new ArrayList<>();
        dwdupdateList=new ArrayList<>();
        selectedtestidList=new ArrayList<>();
        downloadfileList=new ArrayList<>();
        alreadydwdList=new ArrayList<>();
        chapterList=new ArrayList<>();
        chapterFileList=new ArrayList<>();
        localPathList=new ArrayList<>();

        myhelper=new DBHelper(this);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
            paperid=cmgintent.getStringExtra("paperid");
        }

        getTestIdsFromLocal();

    }

    public void getTestIdsFromLocal(){
        testidList.clear();
        try {
            Cursor mycursor=myhelper.getAssesmentTestsByEnroll(enrollid);
            if(mycursor.getCount()>0){
                while (mycursor.moveToNext()) {
                    testidList.add(new SingleAssessment(mycursor.getString(mycursor.getColumnIndex("satu_ID")),mycursor.getString(mycursor.getColumnIndex("satu_subjet_ID")),mycursor.getString(mycursor.getColumnIndex("satu_dwnld_status"))));
                }
            }else {
                mycursor.close();
                Toast.makeText(ListofAssessmentTests.this,"No Assesment Tests Available",Toast.LENGTH_LONG).show();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        setData();
    }

    public void getTestIds(){
        hmap.clear();
        hmap.put("courseid",courseid);
        hmap.put("paperid",paperid);
        new BagroundTask(URLClass.hosturl+"getTestsByCourseandPaper.php", hmap, ListofAssessmentTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("ListofPractiseTests----","JSON:comes-"+json);
                    if(json.equalsIgnoreCase("Tests_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Tests for User",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        for(int i=0;i<ja.length();i++){

                            myObj=ja.getJSONObject(i);
                            String testid=myObj.getString("atu_ID");
                            subjectid=myObj.getString("atu_subjet_ID");
                            String status=myObj.getString("atu_dwnld_status");
                            testidList.add(new SingleAssessment(testid,subjectid,status));

                            long checkFlag=myhelper.checkTest(myObj.getInt("atu_key"));
                            if(checkFlag>0){
                                Log.e("ListofPractiseTests----","Test Already Exists");
                            }else{
//                                long insertFlag=myhelper.insertPractiseTest(myObj.getInt("atu_key"),myObj.getString("atu_org_id"),myObj.getString("atu_entroll_id"),myObj.getString("atu_student_ID"),
//                                        myObj.getString("atu_batch"),myObj.getString("atu_ID"),myObj.getString("atu_paper_ID"),myObj.getString("atu_subjet_ID"),
//                                        myObj.getString("atu_course_id"),myObj.getString("atu_start_date"),myObj.getString("atu_end_date"),myObj.getString("atu_dwnld_status"),
//                                        myObj.getInt("atu_no_of_questions"),myObj.getDouble("atu_tot_marks"),myObj.getDouble("stpu_min_marks"),myObj.getDouble("atu_max_marks"));
//                                if(insertFlag>0){
//                                    Log.e("ListofPractiseTests----","Test Inserted in Local");
//                                }else{
//                                    Log.e("ListofPractiseTests----","Local Test Insertion Failed");
//                                }
                            }

                        }
                    }
                    getTestIdsFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofPractiseTests----",e.toString());
                }
            }
        }).execute();
    }

    public void setData(){
        if (testidList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + testidList.size());
            tv_emptytests.setVisibility(View.GONE);
            tAdp = new AssesmentTestAdapter(testidList,ListofAssessmentTests.this);
            myLayoutManager = new LinearLayoutManager(ListofAssessmentTests.this, LinearLayoutManager.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tAdp);
        } else {
            tv_emptytests.setText("No Tests for student");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ListofAssessmentTests.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
