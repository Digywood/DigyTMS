package com.digywood.tms;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.Lesson_Unit_Point_Adapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.LessonUnitPoint;

import java.util.ArrayList;

public class Activity_Lesson_Unit_Points extends AppCompatActivity {
    String TAG=this.getClass().getName();
    AppEnvironment appEnvironment;
    UserMode userMode;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",lessonid="",lessonunitid="",lessonunitpointid="",orgid="",studentname="",number="",email="";
    RecyclerView rv_listoflesson_unit_points;
    Lesson_Unit_Point_Adapter lesson_unit_point_Adapter;
    RecyclerView.LayoutManager myLayoutManager;
    TextView tv_lession_unit_point_emptydata;
    DBHelper myhelper;
    ArrayList<LessonUnitPoint> al_lesson_unit_points=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_unit_points);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        appEnvironment = ((MyApplication) getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) getApplication()).getUserMode();//getting User Mode

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
            number=cmgintent.getStringExtra("number");
            email=cmgintent.getStringExtra("email");
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
            paperid=cmgintent.getStringExtra("paperid");
            lessonid=cmgintent.getStringExtra("lessonid");
            lessonunitid=cmgintent.getStringExtra("lessonunitid");
            orgid=cmgintent.getStringExtra("orgid");
        }

        rv_listoflesson_unit_points=findViewById(R.id.rv_listoflesson_unit_points);
        tv_lession_unit_point_emptydata=findViewById(R.id.tv_lession_unit_point_emptydata);

        myhelper=new DBHelper(this);
        setLessonUnitPointssToRecyclerview();

        rv_listoflesson_unit_points.addOnItemTouchListener(new RecyclerTouchListener(Activity_Lesson_Unit_Points.this, rv_listoflesson_unit_points, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonUnitPoint lessonUnitPoint=al_lesson_unit_points.get(position);
                lessonunitpointid=lessonUnitPoint.getTms_lup_id();

                if (!userMode.mode()) {
                    Intent intent = new Intent(Activity_Lesson_Unit_Points.this, Activity_Lesson_Unit_Point_Display.class);
                    intent.putExtra("studentid",studentid);
                    intent.putExtra("sname",studentname);
                    intent.putExtra("number",number);
                    intent.putExtra("email",email);
                    intent.putExtra("enrollid",enrollid);
                    intent.putExtra("courseid",courseid);
                    intent.putExtra("paperid",paperid);
                    intent.putExtra("lessonid",lessonid);
                    intent.putExtra("lessonunitid",lessonunitid);
                    intent.putExtra("lessonunitpointid",lessonunitpointid);
                    intent.putExtra("orgid",orgid);
                    startActivity(intent);
                    finish();
                }else {
                    new AsyncCheckInternet_WithOutProgressBar(Activity_Lesson_Unit_Points.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus)
                            {
                                Intent intent = new Intent(Activity_Lesson_Unit_Points.this, Activity_Lesson_Unit_Point_Display.class);
                                intent.putExtra("studentid",studentid);
                                intent.putExtra("sname",studentname);
                                intent.putExtra("number",number);
                                intent.putExtra("email",email);
                                intent.putExtra("enrollid",enrollid);
                                intent.putExtra("courseid",courseid);
                                intent.putExtra("paperid",paperid);
                                intent.putExtra("lessonid",lessonid);
                                intent.putExtra("lessonunitid",lessonunitid);
                                intent.putExtra("lessonunitpointid",lessonunitpointid);
                                intent.putExtra("orgid",orgid);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void setLessonUnitPointssToRecyclerview() {
        Log.e(TAG,"courseid:"+courseid+", subjectid:"+subjectid+",paperid:"+paperid);
        al_lesson_unit_points=myhelper.getLessonUnitpoints(courseid,subjectid,paperid,lessonid,lessonunitid);
        Log.e(TAG,"al_lesson_units.size():"+al_lesson_unit_points.size());

        if (al_lesson_unit_points.size() != 0) {
            Log.e(TAG, "Lsson_unit Size:" + al_lesson_unit_points.size());
            tv_lession_unit_point_emptydata.setVisibility(View.GONE);
            lesson_unit_point_Adapter = new Lesson_Unit_Point_Adapter(al_lesson_unit_points,Activity_Lesson_Unit_Points.this);
            myLayoutManager = new LinearLayoutManager(Activity_Lesson_Unit_Points.this, RecyclerView.VERTICAL,false);
            rv_listoflesson_unit_points.setLayoutManager(myLayoutManager);
            rv_listoflesson_unit_points.setItemAnimator(new DefaultItemAnimator());
            rv_listoflesson_unit_points.setAdapter(lesson_unit_point_Adapter);
        } else {
            tv_lession_unit_point_emptydata.setText("Lession units are not available");
            tv_lession_unit_point_emptydata.setVisibility(View.VISIBLE);
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(Activity_Lesson_Unit_Points.this, Activity_Lesson_Units.class);
            intent.putExtra("studentid",studentid);
            intent.putExtra("enrollid",enrollid);
            intent.putExtra("courseid",courseid);
            intent.putExtra("paperid",paperid);
            intent.putExtra("lessonid",lessonid);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    public void onBackPressed() {
        exitByBackKey();
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        exitByBackKey();
        return super.onSupportNavigateUp();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        Intent intent = new Intent(Activity_Lesson_Unit_Points.this, Activity_Lesson_Units.class);
        intent.putExtra("studentid",studentid);
        intent.putExtra("sname",studentname);
        intent.putExtra("number",number);
        intent.putExtra("email",email);
        intent.putExtra("enrollid",enrollid);
        intent.putExtra("courseid",courseid);
        intent.putExtra("paperid",paperid);
        intent.putExtra("lessonid",lessonid);
        intent.putExtra("orgid",orgid);
        startActivity(intent);
        this.finish();

    }
}
