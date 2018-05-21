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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LearningActivity extends AppCompatActivity {

    RecyclerView rv_enroll;
    TextView tv_emptyenroll;
    String studentid="",studentname="",RandomAudioFileName="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    DBHelper myhelper;
    Random random ;
    ArrayList<String> enrollids;
    ArrayList<String> enrollcourseids;
    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleEnrollment> enrollList;
    LinearLayoutManager myLayoutManager;
    EnrollAdapter eAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

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

        rv_enroll=findViewById(R.id.rv_listofenrolls);
        tv_emptyenroll=findViewById(R.id.tv_enrollemptydata);
        enrollids=new ArrayList<>();
        enrollcourseids=new ArrayList<>();
        enrollList=new ArrayList<>();

        myhelper=new DBHelper(this);
        random=new Random();

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }

        rv_enroll.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),rv_enroll,new RecyclerTouchListener.OnItemClickListener(){
            @Override
            public void onClick(View view, int position) {
                SingleEnrollment singleEnrollment=enrollList.get(position);
                Intent intent=new Intent(getApplicationContext(),PaperActivity.class);
                intent.putExtra("studentid",studentid);
                intent.putExtra("enrollid",singleEnrollment.getEnrollid());
                intent.putExtra("courseid",singleEnrollment.getCourseid());
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getEnrollsFromLocal();

    }

    public void getEnrolls(){
        hmap.clear();
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl + "getStudentEnrolls.php", hmap, LearningActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("LearningActivity----","json:comes: "+json);
                    if(json.equalsIgnoreCase("Enroll_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Enrollments for Students",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        for(int i=0;i<ja.length();i++){
                            myObj=ja.getJSONObject(i);
                            String enrollid=myObj.getString("Enroll_ID");
                            String enrollcourseid=myObj.getString("Enroll_course_ID");
                            enrollids.add(enrollid);
                            enrollcourseids.add(enrollcourseid);

                            long checkFlag=myhelper.checkEnrollment(myObj.getString("Enroll_ID"));
                            if(checkFlag>0){
                                Log.e("LearningActivity----","Enroll Already Exists");
                            }else{
                                long insertFlag=myhelper.insertEnrollment(myObj.getInt("Enroll_key"),myObj.getString("Enroll_ID"),myObj.getString("Enroll_org_id"),myObj.getString("Enroll_Student_ID"),
                                        myObj.getString("Enroll_batch_ID"),myObj.getString("Enroll_course_ID"),myObj.getString("Enroll_batch_start_Dt"),myObj.getString("Enroll_batch_end_Dt"),
                                        myObj.getString("Enroll_Device_ID"),myObj.getString("Enroll_Date"),myObj.getString("Enroll_Status"));
                                if(insertFlag>0){
                                    Log.e("LearningActivity----","Enroll Inserted in Local");
                                }else{
                                    Log.e("LearningActivity----","Local Enroll Insertion Failed");
                                }
                            }
                        }
                    }
                    getEnrollsFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("LearningActivity----",e.toString());
                }
                setData();
            }
        }).execute();
    }

    public void getEnrollsFromLocal(){

        Cursor mycursor=myhelper.getStudentEnrolls(studentid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()) {
                String coursename=myhelper.getCoursenameById(mycursor.getString(mycursor.getColumnIndex("Enroll_course_ID")));
                enrollList.add(new SingleEnrollment(mycursor.getString(mycursor.getColumnIndex("Enroll_ID")),mycursor.getString(mycursor.getColumnIndex("Enroll_batch_ID")),mycursor.getString(mycursor.getColumnIndex("Enroll_org_id")),mycursor.getString(mycursor.getColumnIndex("Enroll_course_ID")),coursename,"2018-05-10","2018-05-10",10));
            }
        }else{
            mycursor.close();
        }
        setData();
    }

    public void setData(){
        if (enrollList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + enrollList.size());
            tv_emptyenroll.setVisibility(View.GONE);
            eAdp = new EnrollAdapter(enrollList,LearningActivity.this);
            myLayoutManager = new LinearLayoutManager(LearningActivity.this, LinearLayoutManager.VERTICAL,false);
            rv_enroll.setLayoutManager(myLayoutManager);
            rv_enroll.setItemAnimator(new DefaultItemAnimator());
            rv_enroll.setAdapter(eAdp);
        } else {
            tv_emptyenroll.setText("No Enrollments for student");
            tv_emptyenroll.setVisibility(View.VISIBLE);
        }
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
