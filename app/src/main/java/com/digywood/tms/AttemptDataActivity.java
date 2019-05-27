package com.digywood.tms;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.widget.TextView;
import com.digywood.tms.Adapters.FlashAttemptAdapter;
import com.digywood.tms.Adapters.PagerAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleFlashAttempt;
import java.util.ArrayList;

public class AttemptDataActivity extends AppCompatActivity {

    DBHelper myhelper;
    RecyclerView rv_fattemptdata;
    TextView tv_emptyflashdata;
    FlashAttemptAdapter fAdp;
    TextView tv_testid,tv_minscore,tv_maxscore,tv_avgscore;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleFlashAttempt> fattemptList=new ArrayList<>();
    String studentname="", number="", email="", course_id="", paper_id="", orgid="",studentId="",enrollid="",testid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_data);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentId=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
            number=cmgintent.getStringExtra("number");
            email=cmgintent.getStringExtra("email");
            enrollid=cmgintent.getStringExtra("enrollid");
            course_id=cmgintent.getStringExtra("courseid");
            paper_id=cmgintent.getStringExtra("paperid");
            orgid=cmgintent.getStringExtra("orgid");
            testid = getIntent().getStringExtra("test");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Practice Test"));
        tabLayout.addTab(tabLayout.newTab().setText("Flash Cards"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(Build.VERSION.SDK_INT>=21) {

            final Drawable upArrow = getApplicationContext().getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

/*        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            testId=cmgintent.getStringExtra("testId");
        }*/

/*        myhelper=new DBHelper(this);
        tv_testid=findViewById(R.id.tv_ftestid);
        tv_testid.setText(testId);
        tv_minscore=findViewById(R.id.tv_minpercent);
        tv_maxscore=findViewById(R.id.tv_maxpercent);
        tv_avgscore=findViewById(R.id.tv_avgpercent);
        rv_fattemptdata=findViewById(R.id.rv_flashattempts);
        tv_emptyflashdata=findViewById(R.id.tv_flashemptydata);
*/
//        getFlashAttemptData(testId);

    }


//    public void getFlashAttemptData(String testId){
//
//        Double minscore=0.0,maxscore=0.0,avgscore=0.0;
//        Cursor cur=myhelper.getTestFlashData(testId,);
//        if(cur.getCount()>0){
//            while (cur.moveToNext()){
//                minscore=cur.getDouble(cur.getColumnIndex("min_flashScore"));
//                maxscore=cur.getDouble(cur.getColumnIndex("max_flashScore"));
//                avgscore=cur.getDouble(cur.getColumnIndex("avg_flashScore"));
//            }
//        }else{
//            cur.close();
//        }
//
//        tv_minscore.setText(String.valueOf(minscore)+" %");
//        tv_maxscore.setText(String.valueOf(maxscore)+" %");
//        Double avgpercent=round(avgscore,2);
//        tv_avgscore.setText(String.valueOf(avgpercent)+" %");
//
//        Cursor mycursor=myhelper.getFlashTestData(testId);
//        if(mycursor.getCount()>0){
//            while(mycursor.moveToNext()){
//                fattemptList.add(new SingleFlashAttempt(mycursor.getString(mycursor.getColumnIndex("startDttm")),mycursor.getInt(mycursor.getColumnIndex("attemptQCount")),mycursor.getInt(mycursor.getColumnIndex("iknowCount")),mycursor.getInt(mycursor.getColumnIndex("donknowCount")),mycursor.getInt(mycursor.getColumnIndex("skipCount")),mycursor.getDouble(mycursor.getColumnIndex("percentageObtain"))));
//            }
//        }else{
//            mycursor.close();
//        }
//
//        if (fattemptList.size() != 0) {
//            Log.e("Advtlist.size()", "comes:" + fattemptList.size());
//            tv_emptyflashdata.setVisibility(View.GONE);
//            fAdp = new FlashAttemptAdapter(fattemptList,AttemptDataActivity.this);
//            myLayoutManager = new LinearLayoutManager(AttemptDataActivity.this, LinearLayoutManager.VERTICAL,false);
//            rv_fattemptdata.setLayoutManager(myLayoutManager);
//            rv_fattemptdata.setItemAnimator(new DefaultItemAnimator());
//            rv_fattemptdata.setAdapter(fAdp);
//        } else {
//            rv_fattemptdata.setAdapter(null);
//            tv_emptyflashdata.setText("No Flash Attempt History");
//            tv_emptyflashdata.setVisibility(View.VISIBLE);
//        }
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    @Override
    public boolean onSupportNavigateUp() {
        exitByBackKey();
        return super.onSupportNavigateUp();
    }

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
        Intent intent = new Intent(AttemptDataActivity.this, ListofPractiseTests.class);
        intent.putExtra("studentid",studentId);
        intent.putExtra("sname",studentname);
        intent.putExtra("number",number);
        intent.putExtra("email",email);
        intent.putExtra("enrollid",enrollid);
        intent.putExtra("courseid",course_id);
        intent.putExtra("paperid",paper_id);
        intent.putExtra("orgid",orgid);
        startActivity(intent);
        this.finish();
    }
}
