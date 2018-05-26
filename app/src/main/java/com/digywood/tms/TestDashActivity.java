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
import android.view.View;
import android.widget.TextView;
import com.digywood.tms.Adapters.TestDashAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDashTest;
import java.util.ArrayList;

public class TestDashActivity extends AppCompatActivity {

    String studentid="",enrollid="",paperid="",testtype="";
    RecyclerView rv_tests;
    TextView tv_emptytests;
    DBHelper myhelper;
    Double lastscore=0.0;
    TestDashAdapter tdAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleDashTest> dashTestList=new ArrayList<>();
    ArrayList<String> testids=new ArrayList<>();
    ArrayList<String> testnames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dash);

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

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            enrollid=cmgintent.getStringExtra("enrollid");
            paperid=cmgintent.getStringExtra("paperid");
            testtype=cmgintent.getStringExtra("testtype");
        }

        myhelper=new DBHelper(this);
        rv_tests=findViewById(R.id.rv_testlist);
        tv_emptytests=findViewById(R.id.tv_emptytestdata);

        if(testtype.equalsIgnoreCase("PRACTISE")){
            getTestsByPaperP();
        }else if(testtype.equalsIgnoreCase("FLASH")){
            getTestsByPaperF();
        }else if(testtype.equalsIgnoreCase("ASSESSMENT")){
            getTestsByPaperA();
        }

    }

    public void getTestsByPaperP(){

        testids.clear();
        Cursor mycursor=myhelper.getTestDataByPaper(studentid,enrollid,paperid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String testid=mycursor.getString(mycursor.getColumnIndex("sptu_ID"));
                String testname=mycursor.getString(mycursor.getColumnIndex("sptu_name"));
                testids.add(testid);
                testnames.add(testname);
            }
            Log.e("PaperActivity----",""+testids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(testids.size()>0){

            dashTestList.clear();
            int attemptcount=0,minattempts=0,maxattepts=0,avgattempts=0;
            double lastscore=0.0,min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;
            String lastdate="";

            for(int i=0;i<testids.size();i++){

                Cursor mycur=myhelper.getTestPractiseSummary(testids.get(i),studentid,enrollid);
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("sptu_no_of_attempts"));
                        lastscore=mycur.getDouble(mycur.getColumnIndex("sptu_last_attempt_percent"));
                        min=mycur.getDouble(mycur.getColumnIndex("sptu_min_percent"));
                        max=mycur.getDouble(mycur.getColumnIndex("sptu_max_percent"));
                        avg=mycur.getDouble(mycur.getColumnIndex("sptu_avg_percent"));
                        lastdate=mycur.getString(mycur.getColumnIndex("sptu_last_attempt_start_dttm"));
                    }
                }else{
                    mycur.close();
                }

                Cursor mycur1=myhelper.getTestAggrigateData(testids.get(i),"PRACTISE");
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        minattempts=mycur1.getInt(mycur1.getColumnIndex("minAttempts"));
                        maxattepts=mycur1.getInt(mycur1.getColumnIndex("maxAttempts"));
                        avgattempts=mycur1.getInt(mycur1.getColumnIndex("avgAttempts"));
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minPercentage"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxPercentage"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgPercentage"));
                    }
                }else{
                    mycur1.close();
                }

                dashTestList.add(new SingleDashTest(testids.get(i),testnames.get(i),attemptcount,lastdate,lastscore,"2018-05-04",min,max,avg,bmin,bmax,bavg,avgattempts,maxattepts,minattempts));

            }
        }

        setDataP();
    }

    public void getTestsByPaperF(){

        testids.clear();
        Cursor mycursor=myhelper.getTestDataByPaper(studentid,enrollid,paperid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String testid=mycursor.getString(mycursor.getColumnIndex("sptu_ID"));
                String testname=mycursor.getString(mycursor.getColumnIndex("sptu_name"));
                testids.add(testid);
                testnames.add(testname);
            }
            Log.e("PaperActivity----",""+testids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(testids.size()>0){

            dashTestList.clear();
            int attemptcount=0,minattempts=0,maxattepts=0,avgattempts=0;
            double lastscore=0.0,min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;
            String lastdate="";

            for(int i=0;i<testids.size();i++){

                Cursor mycur=myhelper.getTestFlashSummary(testids.get(i),studentid,enrollid);
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("sptuflash_attempts"));
                        min=mycur.getDouble(mycur.getColumnIndex("min_flashScore"));
                        max=mycur.getDouble(mycur.getColumnIndex("max_flashScore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avg_flashScore"));
                        lastscore=mycur.getDouble(mycur.getColumnIndex("lastAttemptScore"));
                        lastdate=mycur.getString(mycur.getColumnIndex("lastAttemptDttm"));
                    }
                }else{
                    mycur.close();
                }

                Cursor mycur1=myhelper.getTestAggrigateData(testids.get(i),"FLASH");
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        minattempts=mycur1.getInt(mycur1.getColumnIndex("minAttempts"));
                        maxattepts=mycur1.getInt(mycur1.getColumnIndex("maxAttempts"));
                        avgattempts=mycur1.getInt(mycur1.getColumnIndex("avgAttempts"));
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minPercentage"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxPercentage"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgPercentage"));
                    }
                }else{
                    mycur1.close();
                }

                dashTestList.add(new SingleDashTest(testids.get(i),testnames.get(i),attemptcount,lastdate,lastscore,"2018-05-04",min,max,avg,bmin,bmax,bavg,avgattempts,maxattepts,minattempts));

            }
        }

        setDataF();
    }

    public void getTestsByPaperA(){

        testids.clear();
        Cursor mycursor=myhelper.getATestDataByPaper(studentid,enrollid,paperid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String testid=mycursor.getString(mycursor.getColumnIndex("satu_ID"));
                String testname=mycursor.getString(mycursor.getColumnIndex("satu_name"));
                testids.add(testid);
                testnames.add(testname);
            }
            Log.e("PaperActivity----",""+testids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(testids.size()>0){

            dashTestList.clear();
            int attemptcount=0,minattempts=0,maxattepts=0,avgattempts=0;
            double lastscore=0.0,min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;
            String lastdate="";

            for(int i=0;i<testids.size();i++){

                Cursor mycur=myhelper.getAssessmentTestSummary(studentid,enrollid,testids.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("satu_attempts"));
                        min=mycur.getDouble(mycur.getColumnIndex("min_AScore"));
                        max=mycur.getDouble(mycur.getColumnIndex("max_AScore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avg_AScore"));
                        lastscore=mycur.getDouble(mycur.getColumnIndex("lastAttemptScore"));
                        lastdate=mycur.getString(mycur.getColumnIndex("lastAttemptDttm"));
                    }
                }else{
                    mycur.close();
                }

                Cursor mycur1=myhelper.getTestAggrigateData(testids.get(i),"ASSESSMENT");
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        minattempts=mycur1.getInt(mycur1.getColumnIndex("minAttempts"));
                        maxattepts=mycur1.getInt(mycur1.getColumnIndex("maxAttempts"));
                        avgattempts=mycur1.getInt(mycur1.getColumnIndex("avgAttempts"));
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minPercentage"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxPercentage"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgPercentage"));
                    }
                }else{
                    mycur1.close();
                }

                dashTestList.add(new SingleDashTest(testids.get(i),testnames.get(i),attemptcount,lastdate,lastscore,"2018-05-04",min,max,avg,bmin,bmax,bavg,avgattempts,maxattepts,minattempts));

            }
        }

        setDataA();
    }

    public void setDataP(){
        if (dashTestList.size() != 0) {
            Log.e("testlist.size()", "comes:" + dashTestList.size());
            tv_emptytests.setVisibility(View.GONE);
            tdAdp = new TestDashAdapter(dashTestList,TestDashActivity.this);
            myLayoutManager = new LinearLayoutManager(TestDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tdAdp);
        } else {
            tv_emptytests.setText("No Practise Tests Attempt History");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

    public void setDataF(){
        if (dashTestList.size() != 0) {
            Log.e("testlist.size()", "comes:" + dashTestList.size());
            tv_emptytests.setVisibility(View.GONE);
            tdAdp = new TestDashAdapter(dashTestList,TestDashActivity.this);
            myLayoutManager = new LinearLayoutManager(TestDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tdAdp);
        } else {
            tv_emptytests.setText("No Flash Tests Attempt History");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

    public void setDataA(){
        if (dashTestList.size() != 0) {
            Log.e("testlist.size()", "comes:" + dashTestList.size());
            tv_emptytests.setVisibility(View.GONE);
            tdAdp = new TestDashAdapter(dashTestList,TestDashActivity.this);
            myLayoutManager = new LinearLayoutManager(TestDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tdAdp);
        } else {
            tv_emptytests.setText("No Assessment Tests Attempt History");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
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

}
