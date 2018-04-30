package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseActivity extends AppCompatActivity {

    Button btn_practise,btn_asessment,btn_testpage;
    ArrayList<String> papernameList;
    ArrayList<String> paperidList;
    ArrayAdapter<String> paperAdp;
    String testtype="practise",courseid="",enrollid="";
    HashMap<String,String> hmap=new HashMap<>();
    Spinner sp_papers;
    DBHelper myhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

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

        btn_practise=findViewById(R.id.btn_practise);
        btn_asessment=findViewById(R.id.btn_asessment);
        btn_testpage=findViewById(R.id.btn_testpage);
        sp_papers=findViewById(R.id.sp_papers);

        paperidList=new ArrayList<>();
        papernameList=new ArrayList<>();

        myhelper=new DBHelper(this);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
        }

        btn_practise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testtype="practise";
                btn_practise.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                btn_asessment.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        btn_asessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testtype="asessment";
                btn_practise.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                btn_asessment.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            }
        });

        btn_testpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=sp_papers.getSelectedItemPosition();
                if(pos==0){
                    Toast.makeText(getApplicationContext(),"Please Choose Valid Paper",Toast.LENGTH_SHORT).show();
                }else{
                    if(testtype.equalsIgnoreCase("practise")){
                        Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
                        Log.e("JSON---",courseid+paperidList.get(pos-1));
                        i.putExtra("enrollid",enrollid);
                        i.putExtra("courseid",courseid);
                        i.putExtra("paperid",paperidList.get(pos-1));
                        startActivity(i);
                    }else{
                        Intent i=new Intent(getApplicationContext(),ListofAssesmentTests.class);
                        Log.e("JSON---",courseid+paperidList.get(pos-1));
                        i.putExtra("enrollid",enrollid);
                        i.putExtra("courseid",courseid);
                        i.putExtra("paperid",paperidList.get(pos-1));
                        startActivity(i);
                    }
                }
            }
        });

        getPapersDataFromLocal();

    }

    public void getPapersDataFromLocal(){
        paperidList.clear();
        papernameList.clear();
        Cursor mycursor=myhelper.getStudentPapers();
        if(mycursor.getCount()>0){
            papernameList.add("Select");
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperidList.add(paperid);
                papernameList.add(papername);
            }
            Log.e("CourseActivity----",""+paperidList.size());
            mycursor.close();
        }else{
            mycursor.close();
        }
        paperAdp= new ArrayAdapter(CourseActivity.this,android.R.layout.simple_spinner_item,papernameList);
        paperAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_papers.setAdapter(paperAdp);
    }

    public void getPapersData(){
        hmap.clear();
        hmap.put("courseid",courseid);
        new BagroundTask(URLClass.hosturl + "getPapersByCourseId.php",hmap,CourseActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("CourseActivity------","json:comes"+json);
                    if(json.equalsIgnoreCase("Papers_Not_Exist")){

                        Toast.makeText(getApplicationContext(),"No Papers Found on this course",Toast.LENGTH_SHORT).show();

                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        papernameList.add("Select");
                        for(int i=0;i<ja.length();i++){
                            myObj=ja.getJSONObject(i);
                            String paperid=myObj.getString("Paper_ID");
                            String papername=myObj.getString("Paper_Name");
                            paperidList.add(paperid);
                            papernameList.add(papername);

                            long checkFlag=myhelper.checkPaper(myObj.getInt("Paper_Key"));
                            if(checkFlag>0){
                                Log.e("CourseActivity----","Paper Already Exists");
                            }else{
                                long insertFlag=myhelper.insertPaper(myObj.getInt("Paper_Key"),myObj.getString("Paper_ID"),myObj.getString("Paper_Seq_no"),myObj.getString("Subject_ID"),
                                        myObj.getString("Course_ID"),myObj.getString("Paper_Name"),myObj.getString("Paper_Short_Name"),myObj.getString("Paper_Min_Pass_Marks"),
                                        myObj.getString("Paper_Max_Marks"));
                                if(insertFlag>0){
                                    Log.e("CourseActivity----","Paper Inserted in Local");
                                }else{
                                    Log.e("CourseActivity----","Local Paper Insertion Failed");
                                }
                            }

                        }
                    }
                    getPapersDataFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("CourseActivity------",e.toString());
                }
            }
        }).execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
