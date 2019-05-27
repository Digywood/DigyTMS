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

import com.digywood.tms.Adapters.LessonAdapter;
import com.digywood.tms.Adapters.Lesson_Unit_Adapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.Lesson;
import com.digywood.tms.Pojo.LessonUnit;

import java.util.ArrayList;

public class Activity_Lesson_Units extends AppCompatActivity {

    String TAG=this.getClass().getName();
    AppEnvironment appEnvironment;
    UserMode userMode;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",lessonid="",orgid="",studentname="",number="",email="";
    RecyclerView rv_listoflesson_units;
    Lesson_Unit_Adapter lesson_unit_Adapter;
    RecyclerView.LayoutManager myLayoutManager;
    TextView tv_lession_unit_emptydata;
    DBHelper myhelper;
    ArrayList<LessonUnit> al_lesson_units=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_units);

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
            orgid=cmgintent.getStringExtra("orgid");
            lessonid=cmgintent.getStringExtra("lessonid");
        }

        rv_listoflesson_units=findViewById(R.id.rv_listoflesson_units);
        tv_lession_unit_emptydata=findViewById(R.id.tv_lession_unit_emptydata);

        myhelper=new DBHelper(this);

        setLessonunitsToRecyclerview();

        rv_listoflesson_units.addOnItemTouchListener(new RecyclerTouchListener(Activity_Lesson_Units.this, rv_listoflesson_units, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonUnit singleLesson_unit=al_lesson_units.get(position);
                final String lessonunitid=singleLesson_unit.getTms_lu_id();
                if (!userMode.mode()) {
                    Intent intent=new Intent(getApplicationContext(),Activity_Lesson_Unit_Points.class);
                    intent.putExtra("studentid",studentid);
                    intent.putExtra("sname",studentname);
                    intent.putExtra("number",number);
                    intent.putExtra("email",email);
                    intent.putExtra("enrollid",enrollid);
                    intent.putExtra("courseid",courseid);
                    intent.putExtra("paperid",paperid);
                    intent.putExtra("lessonid",lessonid);
                    intent.putExtra("orgid",orgid);
                    intent.putExtra("lessonunitid",lessonunitid);
                    startActivity(intent);
                    finish();
                }else {
                    new AsyncCheckInternet_WithOutProgressBar(Activity_Lesson_Units.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus)
                            {
                                Intent intent=new Intent(getApplicationContext(),Activity_Lesson_Unit_Points.class);
                                intent.putExtra("studentid",studentid);
                                intent.putExtra("sname",studentname);
                                intent.putExtra("number",number);
                                intent.putExtra("email",email);
                                intent.putExtra("enrollid",enrollid);
                                intent.putExtra("courseid",courseid);
                                intent.putExtra("paperid",paperid);
                                intent.putExtra("lessonid",lessonid);
                                intent.putExtra("orgid",orgid);
                                intent.putExtra("lessonunitid",lessonunitid);
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

    private void setLessonunitsToRecyclerview() {
        Log.e(TAG,"courseid:"+courseid+", subjectid:"+subjectid+",paperid:"+paperid);
        al_lesson_units=myhelper.getLessonUnits(courseid,subjectid,paperid,lessonid);
        Log.e(TAG,"al_lesson_units.size():"+al_lesson_units.size());

        if (al_lesson_units.size() != 0) {
            Log.e(TAG, "Lsson_unit Size:" + al_lesson_units.size());
            tv_lession_unit_emptydata.setVisibility(View.GONE);
            lesson_unit_Adapter = new Lesson_Unit_Adapter(al_lesson_units,Activity_Lesson_Units.this);
            myLayoutManager = new LinearLayoutManager(Activity_Lesson_Units.this, RecyclerView.VERTICAL,false);
            rv_listoflesson_units.setLayoutManager(myLayoutManager);
            rv_listoflesson_units.setItemAnimator(new DefaultItemAnimator());
            rv_listoflesson_units.setAdapter(lesson_unit_Adapter);
        } else {
            tv_lession_unit_emptydata.setText("Lession units are not available");
            tv_lession_unit_emptydata.setVisibility(View.VISIBLE);
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(Activity_Lesson_Units.this, Activity_Lessons.class);
            intent.putExtra("studentid",studentid);
            intent.putExtra("enrollid",enrollid);
            intent.putExtra("courseid",courseid);
            intent.putExtra("paperid",paperid);
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
        finish();
        Intent intent = new Intent(Activity_Lesson_Units.this, Activity_Lessons.class);
        intent.putExtra("studentid",studentid);
        intent.putExtra("sname",studentname);
        intent.putExtra("number",number);
        intent.putExtra("email",email);
        intent.putExtra("enrollid",enrollid);
        intent.putExtra("courseid",courseid);
        intent.putExtra("paperid",paperid);
        intent.putExtra("orgid",orgid);
        startActivity(intent);

    }
}
