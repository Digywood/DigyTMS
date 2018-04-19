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
import com.digywood.tms.Adapters.FlashAttemptAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleFlashAttempt;
import java.util.ArrayList;

public class FlashAttemptDataActivity extends AppCompatActivity {

    DBHelper myhelper;
    String testId="";
    RecyclerView rv_fattemptdata;
    TextView tv_emptyflashdata;
    FlashAttemptAdapter fAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleFlashAttempt> fattemptList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_attempt_data);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            testId=cmgintent.getStringExtra("testId");
        }

        myhelper=new DBHelper(this);
        rv_fattemptdata=findViewById(R.id.rv_flashattempts);
        tv_emptyflashdata=findViewById(R.id.tv_flashemptydata);

        getFlashAttemptData(testId);

    }

    public void getFlashAttemptData(String testId){

        Double minscore,maxscore,avgscore;
        Cursor cur=myhelper.getTestFlashData("PTU0002");
        if(cur.getCount()>0){
            while (cur.moveToNext()){
                minscore=cur.getDouble(cur.getColumnIndex("min_flashScore"));
                maxscore=cur.getDouble(cur.getColumnIndex("max_flashScore"));
                avgscore=cur.getDouble(cur.getColumnIndex("avg_flashScore"));
            }
        }else{
            cur.close();
        }



        Cursor mycursor=myhelper.getFlashTestData("PTU0002");
        if(mycursor.getCount()>0){
            while(mycursor.moveToNext()){
                fattemptList.add(new SingleFlashAttempt(mycursor.getString(mycursor.getColumnIndex("startDttm")),mycursor.getInt(mycursor.getColumnIndex("iknowCount")),mycursor.getInt(mycursor.getColumnIndex("donknowCount")),mycursor.getInt(mycursor.getColumnIndex("skipCount")),mycursor.getDouble(mycursor.getColumnIndex("percentageObtain"))));
            }
        }else{
            mycursor.close();
        }
        if (fattemptList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + fattemptList.size());
            tv_emptyflashdata.setVisibility(View.GONE);
            fAdp = new FlashAttemptAdapter(fattemptList,FlashAttemptDataActivity.this);
            myLayoutManager = new LinearLayoutManager(FlashAttemptDataActivity.this, LinearLayoutManager.VERTICAL,false);
            rv_fattemptdata.setLayoutManager(myLayoutManager);
            rv_fattemptdata.setItemAnimator(new DefaultItemAnimator());
            rv_fattemptdata.setAdapter(fAdp);
        } else {
            rv_fattemptdata.setAdapter(null);
            tv_emptyflashdata.setText("No Flash Attempt History");
            tv_emptyflashdata.setVisibility(View.VISIBLE);
        }
    }
}
