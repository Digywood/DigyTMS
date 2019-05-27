package com.digywood.tms;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Activity_TestTypes extends AppCompatActivity {

    AppEnvironment appEnvironment;
    UserMode userMode;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",orgid="",studentname="",number="",email="";
    LinearLayout ll_pratise,ll_assesment,ll_lession_units,ll_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_types);

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

        ll_pratise=findViewById(R.id.ll_practise);
        ll_assesment=findViewById(R.id.ll_assessment);
        ll_lession_units=findViewById(R.id.ll_lession_units);
        ll_tips=findViewById(R.id.ll_tips);

        ll_pratise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
                i.putExtra("studentid",studentid);
                i.putExtra("sname",studentname);
                i.putExtra("number",number);
                i.putExtra("email",email);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                i.putExtra("paperid",paperid);
                i.putExtra("orgid",orgid);
                startActivity(i);
                finish();
            }
        });

        ll_assesment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ListofAssessmentTests.class);
                i.putExtra("studentid",studentid);
                i.putExtra("sname",studentname);
                i.putExtra("number",number);
                i.putExtra("email",email);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                i.putExtra("paperid",paperid);
                i.putExtra("orgid",orgid);
                startActivity(i);
                finish();
            }
        });

        ll_lession_units.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Activity_Lessons.class);
                i.putExtra("studentid",studentid);
                i.putExtra("sname",studentname);
                i.putExtra("number",number);
                i.putExtra("email",email);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                i.putExtra("paperid",paperid);
                i.putExtra("orgid",orgid);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        exitByBackKey();
        return super.onSupportNavigateUp();
    }


    /*@Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Activity_TestTypes.this, PaperActivity.class);
        intent.putExtra("studentid",studentid);
        intent.putExtra("enrollid",enrollid);
        intent.putExtra("courseid",courseid);
        intent.putExtra("paperid",paperid);
        startActivity(intent);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(Activity_TestTypes.this, PaperActivity.class);
            intent.putExtra("studentid",studentid);
            intent.putExtra("enrollid",enrollid);
            intent.putExtra("courseid",courseid);
            intent.putExtra("paperid",paperid);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        Intent intent = new Intent(Activity_TestTypes.this, PaperActivity.class);
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
