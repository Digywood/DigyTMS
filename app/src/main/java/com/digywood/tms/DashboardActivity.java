package com.digywood.tms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    Button btn_next;
    TextView tv_studentid,tv_enrollid,tv_sname,tv_coursename;
    String studentid="",enrollid,courseid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
        }

        tv_studentid=findViewById(R.id.tv_dstudentid);
        tv_enrollid=findViewById(R.id.tv_denrollid);
        tv_enrollid.setText(enrollid);
        tv_sname=findViewById(R.id.tv_dsname);
        tv_coursename=findViewById(R.id.tv_dcoursename);

        btn_next = findViewById(R.id.btn_mylearnings);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),CourseActivity.class);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                startActivity(i);
            }
        });
    }

}
