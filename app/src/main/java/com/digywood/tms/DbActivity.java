package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.DBHelper.DBHelper;
import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {

    Button btn_pdetails,btn_fdetails,btn_adetails;
    TextView tv_studentid,tv_sname;

    TextView tv_ptottests,tv_pattempted,tv_ptestsasplan,tv_ppercent,tv_pmax,tv_pmin,tv_pavg,tv_pRAGattempt,tv_pRAGAVGscore;
    TextView tv_ftottests,tv_fattempted,tv_ftestsasplan,tv_fpercent,tv_fmax,tv_fmin,tv_favg,tv_fRAGattempt,tv_fRAGAVGscore;
    TextView tv_atottests,tv_aattempted,tv_atestsasplan,tv_apercent,tv_amax,tv_amin,tv_aavg,tv_aRAGattempt,tv_aRAGAVGscore;

    Spinner sp_enrollid,sp_coursename;

    DBHelper myhelper;

    ArrayList<String> courseIds=new ArrayList<>();
    ArrayList<String> enrollIds=new ArrayList<>();
    ArrayAdapter<String> courseAdp,enrollAdp;
    String studentid="",enrollid,courseid,studentname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }

//        tv_studentid=findViewById(R.id.tv_dstudentid);
//        tv_studentid.setText(studentid);
//        tv_sname=findViewById(R.id.tv_dsname);
//        tv_sname.setText(studentname);
//
//        sp_enrollid=findViewById(R.id.sp_denrollid);
//        sp_coursename=findViewById(R.id.sp_dcoursename);
//
//        tv_ptottests=findViewById(R.id.tv_ptottests);
//        tv_pattempted=findViewById(R.id.tv_pattempted);
//        tv_ptestsasplan=findViewById(R.id.tv_ptestsasplan);
//        tv_ppercent=findViewById(R.id.tv_ppercent);
//        tv_pmax=findViewById(R.id.tv_pmax);
//        tv_pmin=findViewById(R.id.tv_pmin);
//        tv_pavg=findViewById(R.id.tv_pavg);
//        tv_pRAGattempt=findViewById(R.id.tv_pRAGattempt);
//        tv_pRAGAVGscore=findViewById(R.id.tv_pRAGAVGscore);
//
//        tv_ftottests=findViewById(R.id.tv_ftottests);
//        tv_fattempted=findViewById(R.id.tv_fattempted);
//        tv_ftestsasplan=findViewById(R.id.tv_ftestsasplan);
//        tv_fpercent=findViewById(R.id.tv_fpercent);
//        tv_fmax=findViewById(R.id.tv_fmax);
//        tv_fmin=findViewById(R.id.tv_fmin);
//        tv_favg=findViewById(R.id.tv_favg);
//        tv_fRAGattempt=findViewById(R.id.tv_fRAGattempt);
//        tv_fRAGAVGscore=findViewById(R.id.tv_fRAGAVGscore);
//
//        tv_atottests=findViewById(R.id.tv_atottests);
//        tv_aattempted=findViewById(R.id.tv_aattempted);
//        tv_atestsasplan=findViewById(R.id.tv_atestsasplan);
//        tv_apercent=findViewById(R.id.tv_apercent);
//        tv_amax=findViewById(R.id.tv_amax);
//        tv_amin=findViewById(R.id.tv_amin);
//        tv_aavg=findViewById(R.id.tv_aavg);
//        tv_aRAGattempt=findViewById(R.id.tv_aRAGattempt);
//        tv_aRAGAVGscore=findViewById(R.id.tv_aRAGAVGscore);
//
//        btn_pdetails = findViewById(R.id.btn_pdetails);
//        btn_fdetails = findViewById(R.id.btn_fdetails);
//        btn_adetails = findViewById(R.id.btn_adetails);

        myhelper=new DBHelper(this);

//        Cursor mycursor=myhelper.getAllCourseIds();
//        Log.e("CursorCount---",""+mycursor.getCount());
//        courseIds.add("Select");
//        if(mycursor.getCount()>0){
//            while(mycursor.moveToNext()){
//                String courseId=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
//                courseIds.add(courseId);
//            }
//            courseAdp= new ArrayAdapter(DbActivity.this,android.R.layout.simple_spinner_item,courseIds);
//            courseAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_coursename.setAdapter(courseAdp);
//        }else{
//            mycursor.close();
//        }
//
//        sp_coursename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                enrollIds.clear();
//                enrollIds.add("Select");
//                Cursor mycursor=myhelper.getCourseEnrollments(sp_coursename.getSelectedItem().toString());
//                if(mycursor.getCount()>0){
//                    while (mycursor.moveToNext()){
//                        String enrollId=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
//                        enrollIds.add(enrollId);
//                    }
//                    enrollAdp= new ArrayAdapter(DbActivity.this,android.R.layout.simple_spinner_item,enrollIds);
//                    enrollAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_enrollid.setAdapter(enrollAdp);
//                }else{
//                    mycursor.close();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        btn_pdetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Practise Test",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btn_fdetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Flash Card",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btn_adetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Assessment Test",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}
