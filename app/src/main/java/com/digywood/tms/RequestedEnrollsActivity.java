package com.digywood.tms;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.EnrollRequestAdapter;
import com.digywood.tms.Adapters.TestDashAdapter;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestedEnrollsActivity extends AppCompatActivity {

    RecyclerView rv_enrolls;
    TextView tv_emptyenrolls;
    String studentid="";
    DBHelper myhelper;
    FloatingActionButton fab_enrollreq;
    EnrollRequestAdapter erAdp;
    LinearLayoutManager myLayoutManager;
    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleEnrollRequest> enrollreqList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_enrolls);

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

        rv_enrolls=findViewById(R.id.rv_enrollreqlist);
        tv_emptyenrolls=findViewById(R.id.tv_emptyereqdata);
        fab_enrollreq=findViewById(R.id.fab_enrollreq);
        myhelper=new DBHelper(this);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
        }

        fab_enrollreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),EnrollRequestActivity.class);
                i.putExtra("studentid",studentid);
                startActivity(i);
            }
        });

        getEnrollReqsFromServer();

    }

    public void getEnrollReqsFromServer(){
        hmap.clear();
        hmap.put("studentid",studentid);
        new BagroundTask(URLClass.hosturl+"getStudentRequestedEnrollments.php",hmap,RequestedEnrollsActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("MainActivity----",json);
                    if(json.equalsIgnoreCase("EnrollRequests_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Requested Enrollments",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        for(int i=0;i<ja.length();i++){
                            myObj=ja.getJSONObject(i);
                            String coursename=myhelper.getCoursenameById(myObj.getString("Enroll_course_ID"));
                            enrollreqList.add(new SingleEnrollRequest(myObj.getString("Enroll_ID"),myObj.getString("Enroll_batch_ID"),myObj.getString("Enroll_org_id"),coursename,myObj.getString("enroll_Request_Date"),myObj.getString("Enroll_batch_end_Dt"),myObj.getString("enroll_Total_Amount")));
                        }
                    }
                    setData();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("MainActivity----",e.toString());
                }
            }
        }).execute();
    }

    public void setData(){
        if (enrollreqList.size() != 0) {
            Log.e("testlist.size()", "comes:" + enrollreqList.size());
            tv_emptyenrolls.setVisibility(View.GONE);
            erAdp = new EnrollRequestAdapter(enrollreqList,RequestedEnrollsActivity.this);
            myLayoutManager = new LinearLayoutManager(RequestedEnrollsActivity.this,LinearLayoutManager.VERTICAL,false);
            rv_enrolls.setLayoutManager(myLayoutManager);
            rv_enrolls.setItemAnimator(new DefaultItemAnimator());
            rv_enrolls.setAdapter(erAdp);
        } else {
            tv_emptyenrolls.setText("No Requested Enrollments");
            tv_emptyenrolls.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
