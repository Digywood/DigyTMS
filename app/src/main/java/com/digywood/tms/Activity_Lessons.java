package com.digywood.tms;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
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

import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.Adapters.LessonAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.Lesson;
import com.digywood.tms.Pojo.SingleEnrollment;

import java.util.ArrayList;

public class Activity_Lessons extends AppCompatActivity {

    String TAG=this.getClass().getName();
    AppEnvironment appEnvironment;
    UserMode userMode;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",orgid="",studentname="",number="",email="";
    RecyclerView rv_listoflessons;
    LessonAdapter lessonAdapter;
    RecyclerView.LayoutManager myLayoutManager;
    TextView tv_lessionemptydata;
    DBHelper myhelper;
    ArrayList<Lesson> al_lessons=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__lessons);

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
        }

        rv_listoflessons=findViewById(R.id.rv_listoflessons);
        tv_lessionemptydata=findViewById(R.id.tv_lessionemptydata);

        myhelper=new DBHelper(this);

        setLissonsToRecyclerview();

        rv_listoflessons.addOnItemTouchListener(new RecyclerTouchListener(Activity_Lessons.this, rv_listoflessons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Lesson singleLesson=al_lessons.get(position);
                final String lessonid=singleLesson.getTms_lms_lesson_id();
                if (!userMode.mode()) {
                    Intent intent=new Intent(getApplicationContext(),Activity_Lesson_Units.class);
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
                    finish();
                }else {
                    new AsyncCheckInternet_WithOutProgressBar(Activity_Lessons.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus)
                            {
                                Intent intent=new Intent(getApplicationContext(),Activity_Lesson_Units.class);
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

    private void setLissonsToRecyclerview() {
        Log.e(TAG,"courseid:"+courseid+", subjectid:"+subjectid+",paperid:"+paperid);
        al_lessons=myhelper.getLessons(courseid,subjectid,paperid);
        Log.e(TAG,"al_lessons.size():"+al_lessons.size());

        if (al_lessons.size() != 0) {
            Log.e(TAG, "Lssons Size:" + al_lessons.size());
            tv_lessionemptydata.setVisibility(View.GONE);
            lessonAdapter = new LessonAdapter(al_lessons,Activity_Lessons.this);
            myLayoutManager = new LinearLayoutManager(Activity_Lessons.this, RecyclerView.VERTICAL,false);
            rv_listoflessons.setLayoutManager(myLayoutManager);
            rv_listoflessons.setItemAnimator(new DefaultItemAnimator());
            rv_listoflessons.setAdapter(lessonAdapter);
        } else {
            tv_lessionemptydata.setText("Lessons are not available");
            tv_lessionemptydata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        exitByBackKey();
        return super.onSupportNavigateUp();
    }

    /*@Override
    public void onBackPressed() {
        exitByBackKey();
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(Activity_Lessons.this, Activity_TestTypes.class);
            intent.putExtra("studentid",studentid);
            intent.putExtra("enrollid",enrollid);
            intent.putExtra("courseid",courseid);
            intent.putExtra("paperid",paperid);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        Intent intent = new Intent(Activity_Lessons.this, Activity_TestTypes.class);
        intent.putExtra("studentid",studentid);
        intent.putExtra("sname",studentname);
        intent.putExtra("number",number);
        intent.putExtra("email",email);
        intent.putExtra("enrollid",enrollid);
        intent.putExtra("courseid",courseid);
        intent.putExtra("paperid",paperid);
        intent.putExtra("orgid",orgid);
        startActivity(intent);

        this.finish();

    }
}
