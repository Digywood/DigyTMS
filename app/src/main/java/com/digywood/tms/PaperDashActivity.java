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
import com.digywood.tms.Adapters.PaperDashAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDashPaper;
import java.util.ArrayList;

public class PaperDashActivity extends AppCompatActivity {

    RecyclerView rv_ptests;
    TextView tv_emptyptests;
    DBHelper myhelper;
    String studentid="",enrollid="",courseid="",testtype="";
    PaperDashAdapter pdAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleDashPaper> dashPaperList=new ArrayList<>();
    ArrayList<String> paperids=new ArrayList<>();
    ArrayList<String> papernames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_dash);

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
            courseid=cmgintent.getStringExtra("courseid");
            testtype=cmgintent.getStringExtra("testtype");
        }

        myhelper=new DBHelper(this);
        rv_ptests=findViewById(R.id.rv_listoffpapertests);
        tv_emptyptests=findViewById(R.id.tv_pfemptydata);

        if(testtype.equalsIgnoreCase("PRACTISE")){
            getPapersByCourseP();
        }else if(testtype.equalsIgnoreCase("FLASH")){
            getPapersByCourseF();
        }else if(testtype.equalsIgnoreCase("ASSESSMENT")){
            getPapersByCourseA();
        }
    }

    public void getPapersByCourseP(){

        paperids.clear();
        papernames.clear();
        Cursor mycursor=myhelper.getPapersByCourse(courseid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperids.add(paperid);
                papernames.add(papername);
            }
            Log.e("PaperActivity----",""+paperids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperids.size()>0){

            dashPaperList.clear();
            int attemptcount=0;
            double min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;

            for(int i=0;i<paperids.size();i++){

                int totaltestcount=myhelper.getTestsByPaper(studentid,enrollid,paperids.get(i));

                Cursor mycur=myhelper.getPractiseSummaryByPaper(studentid,enrollid,paperids.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        Log.e("Cursor Row:----",""+mycur.getCount()+"  paperid:--"+paperids.get(i));
                        Log.e("Cursor Data:----","min: "+mycur.getDouble(mycur.getColumnIndex("minscore"))+" max:  "+mycur.getDouble(mycur.getColumnIndex("maxscore"))+"  avg: "+mycur.getDouble(mycur.getColumnIndex("avgscore")));

                        attemptcount=mycur.getInt(mycur.getColumnIndex("attemptpcount"));
                        min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                        max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur.close();
                }

                Cursor mycur1=myhelper.getPaperAggrigateData(paperids.get(i),"PRACTISE");
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur1.close();
                }

                dashPaperList.add(new SingleDashPaper(paperids.get(i),papernames.get(i),totaltestcount,attemptcount,max,min,avg,bmin,bmax,bavg));

            }
        }

        setDataP();
    }

    public void getPapersByCourseF(){

        paperids.clear();
        papernames.clear();
        Cursor mycursor=myhelper.getPapersByCourse(courseid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperids.add(paperid);
                papernames.add(papername);
            }
            Log.e("PaperActivity----",""+paperids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperids.size()>0){

            dashPaperList.clear();
            int attemptcount=0;
            double min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;

            for(int i=0;i<paperids.size();i++){
                int totaltestcount=myhelper.getTestsByPaper(studentid,enrollid,paperids.get(i));

                Cursor mycur=myhelper.getFlashSummaryByPaper(studentid,enrollid,paperids.get(i));
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

                Cursor mycur1=myhelper.getPaperAggrigateData(paperids.get(i),"FLASH");
                Log.e("PaperDashActivity--",""+mycur1.getCount());
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur1.close();
                }

                dashPaperList.add(new SingleDashPaper(paperids.get(i),papernames.get(i),totaltestcount,attemptcount,max,min,avg,bmin,bmax,bavg));

            }
        }

        setDataF();
    }

    public void getPapersByCourseA(){

        paperids.clear();
        papernames.clear();
        Cursor mycursor=myhelper.getPapersByCourse(courseid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperids.add(paperid);
                papernames.add(papername);
            }
            Log.e("PaperActivity----",""+paperids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperids.size()>0){

            dashPaperList.clear();
            int attemptcount=0;
            double min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;

            for(int i=0;i<paperids.size();i++){
                int totaltestcount=myhelper.getAssessmentTestsByPaper(studentid,enrollid,paperids.get(i));

                Cursor mycur=myhelper.getAssessmentSummaryByPaper(studentid,enrollid,paperids.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){
                        attemptcount=mycur.getInt(mycur.getColumnIndex("attemptacount"));
                        min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                        max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur.close();
                }

                Cursor mycur1=myhelper.getPaperAggrigateData(paperids.get(i),"ASSESSMENT");
                Log.e("PaperDashActivity--",""+mycur1.getCount());
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){
                        bmin=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
                        bmax=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
                        bavg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur1.close();
                }

                dashPaperList.add(new SingleDashPaper(paperids.get(i),papernames.get(i),totaltestcount,attemptcount,max,min,avg,bmin,bmax,bavg));

            }
        }

        setDataA();
    }

    public void setDataP(){
        if (dashPaperList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + dashPaperList.size());
            tv_emptyptests.setVisibility(View.GONE);
            pdAdp = new PaperDashAdapter(dashPaperList,PaperDashActivity.this,studentid,enrollid,"PRACTISE");
            myLayoutManager = new LinearLayoutManager(PaperDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_ptests.setLayoutManager(myLayoutManager);
            rv_ptests.setItemAnimator(new DefaultItemAnimator());
            rv_ptests.setAdapter(pdAdp);
        } else {
            tv_emptyptests.setText("No Practise Tests Attempt History");
            tv_emptyptests.setVisibility(View.VISIBLE);
        }
    }

    public void setDataF(){
        if (dashPaperList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + dashPaperList.size());
            tv_emptyptests.setVisibility(View.GONE);
            pdAdp = new PaperDashAdapter(dashPaperList,PaperDashActivity.this,studentid,enrollid,"FLASH");
            myLayoutManager = new LinearLayoutManager(PaperDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_ptests.setLayoutManager(myLayoutManager);
            rv_ptests.setItemAnimator(new DefaultItemAnimator());
            rv_ptests.setAdapter(pdAdp);
        } else {
            tv_emptyptests.setText("No Flash Tests Attempt History");
            tv_emptyptests.setVisibility(View.VISIBLE);
        }
    }

    public void setDataA(){
        if (dashPaperList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + dashPaperList.size());
            tv_emptyptests.setVisibility(View.GONE);
            pdAdp = new PaperDashAdapter(dashPaperList,PaperDashActivity.this,studentid,enrollid,"FLASH");
            myLayoutManager = new LinearLayoutManager(PaperDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_ptests.setLayoutManager(myLayoutManager);
            rv_ptests.setItemAnimator(new DefaultItemAnimator());
            rv_ptests.setAdapter(pdAdp);
        } else {
            tv_emptyptests.setText("No Assessment Tests Attempt History");
            tv_emptyptests.setVisibility(View.VISIBLE);
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
