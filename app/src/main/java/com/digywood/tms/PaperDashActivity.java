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
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDashPaper;
import java.util.ArrayList;

public class PaperDashActivity extends AppCompatActivity {

    RecyclerView rv_ptests;
    TextView tv_emptyptests;
    DBHelper myhelper;
    String courseid="";
    int attemptcount=0;
    Double min=0.0,max=0.0,avg=0.0;
    PaperDashAdapter pdAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleDashPaper> dashPaperList=new ArrayList<>();
    ArrayList<String> paperids=new ArrayList<>();
    ArrayList<String> papernames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_dash);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            courseid=cmgintent.getStringExtra("courseid");
        }

        myhelper=new DBHelper(this);
        rv_ptests=findViewById(R.id.rv_listoffpapertests);
        tv_emptyptests=findViewById(R.id.tv_pfemptydata);

        getPapersByCourse(courseid);
    }

    public void getPapersByCourse(String courseid){
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
            Log.e("CourseActivity----",""+paperids.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperids.size()>0){

            for(int i=0;i<paperids.size();i++){
                attemptcount=0;
                min=0.0;
                max=0.0;
                avg=0.0;
                int totaltestcount=myhelper.getTestsByPaper(paperids.get(i));
                Cursor mycur=myhelper.getFlashSummaryByPaper(paperids.get(i));
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

                dashPaperList.add(new SingleDashPaper(paperids.get(i),papernames.get(i),totaltestcount,attemptcount,max,min,avg));

            }
        }

        setData();
    }

    public void setData(){
        if (dashPaperList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + dashPaperList.size());
            tv_emptyptests.setVisibility(View.GONE);
            pdAdp = new PaperDashAdapter(dashPaperList,PaperDashActivity.this);
            myLayoutManager = new LinearLayoutManager(PaperDashActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_ptests.setLayoutManager(myLayoutManager);
            rv_ptests.setItemAnimator(new DefaultItemAnimator());
            rv_ptests.setAdapter(pdAdp);
        } else {
            tv_emptyptests.setText("No Tests Attempt History");
            tv_emptyptests.setVisibility(View.VISIBLE);
        }
    }

}
