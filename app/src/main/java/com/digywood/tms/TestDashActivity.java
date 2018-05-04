package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.digywood.tms.Adapters.PaperDashAdapter;
import com.digywood.tms.Adapters.TestDashAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDashPaper;
import com.digywood.tms.Pojo.SingleDashTest;

import java.util.ArrayList;

public class TestDashActivity extends AppCompatActivity {

    String paperid="",testtype="",lastdate="";;
    RecyclerView rv_tests;
    TextView tv_emptytests;
    DBHelper myhelper;
    int attemptcount=0,lastscore=0;
    Double min=0.0,max=0.0,avg=0.0;
    TestDashAdapter tdAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleDashTest> dashTestList=new ArrayList<>();
    ArrayList<String> testids=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dash);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            paperid=cmgintent.getStringExtra("paperid");
            testtype=cmgintent.getStringExtra("testtype");
        }

        myhelper=new DBHelper(this);
        rv_tests=findViewById(R.id.rv_testlist);
        tv_emptytests=findViewById(R.id.tv_emptytestdata);

        if(testtype.equalsIgnoreCase("PRACTISE")){
            getTestsByPaperP(paperid);
        }else{
            getTestsByPaperF(paperid);
        }

    }

    public void getTestsByPaperP(String paperid){
        testids.clear();
        Cursor mycursor=myhelper.getTestDataByPaper(paperid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String testid=mycursor.getString(mycursor.getColumnIndex("sptu_ID"));
                testids.add(paperid);
            }
            Log.e("CourseActivity----",""+testids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(testids.size()>0){

            for(int i=0;i<testids.size();i++){
                attemptcount=0;
                min=0.0;
                max=0.0;
                avg=0.0;
                int totaltestcount=myhelper.getTestsByPaper(testids.get(i));
                Cursor mycur=myhelper.getFlashSummaryByPaper(testids.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("attemptfcount"));
                        min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                        max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur.close();
                }

//                dashPaperList.add(new SingleDashPaper(paperids.get(i),papernames.get(i),totaltestcount,attemptcount,max,min,avg));

            }
        }

        setDataP();
    }

    public void getTestsByPaperF(String paperId){
        testids.clear();
        Cursor mycursor=myhelper.getTestDataByPaper(paperId);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String testid=mycursor.getString(mycursor.getColumnIndex("sptu_ID"));
                testids.add(testid);
            }
            Log.e("CourseActivity----",""+testids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(testids.size()>0){

            for(int i=0;i<testids.size();i++){
                attemptcount=0;
                lastscore=0;
                lastdate="";
                min=0.0;
                max=0.0;
                avg=0.0;
                Cursor mycur=myhelper.getTestFlashSummary(testids.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("sptuflash_attempts"));
                        lastscore=mycur.getInt(mycur.getColumnIndex("lastAttemptScore"));
                        min=mycur.getDouble(mycur.getColumnIndex("min_flashScore"));
                        max=mycur.getDouble(mycur.getColumnIndex("max_flashScore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avg_flashScore"));
                        lastdate=mycur.getString(mycur.getColumnIndex("lastAttemptDttm"));
                    }
                }else{
                    mycur.close();
                }

                dashTestList.add(new SingleDashTest(testids.get(i),"Sample",attemptcount,lastdate,lastscore,"",min,max,avg,0.0,0.0,0.0,0,0,0));

            }
        }

        setDataF();
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
            tv_emptytests.setText("No Tests Attempt History");
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
            tv_emptytests.setText("No Tests Attempt History");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

}
