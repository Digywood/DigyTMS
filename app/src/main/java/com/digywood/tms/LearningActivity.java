package com.digywood.tms;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LearningActivity extends AppCompatActivity {

    RecyclerView rv_enroll;
    TextView tv_emptyenroll;
    String studentid="",studentname="",RandomAudioFileName="ABCDEFGHIJKLMNOPQRSTUVWXYZ",number,email;
    DBHelper myhelper;
    Random random ;
    ArrayList<String> enrollids;
    ArrayList<String> enrollcourseids;
    ArrayList<SingleEnrollment> enrollList;
    LinearLayoutManager myLayoutManager;
    EnrollAdapter eAdp;

    //InterstitialAd mInterstitialAd;
    AppEnvironment appEnvironment;
    UserMode userMode;


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

        appEnvironment = ((MyApplication) getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) getApplication()).getUserMode();//getting User Mode


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
            number=cmgintent.getStringExtra("number");
            email=cmgintent.getStringExtra("email");
        }

        rv_enroll.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),rv_enroll,new RecyclerTouchListener.OnItemClickListener(){
            @Override
            public void onClick(View view, final int position) {

                if (!userMode.mode()) {
                    SingleEnrollment singleEnrollment=enrollList.get(position);
                    Intent intent=new Intent(getApplicationContext(),PaperActivity.class);
                    intent.putExtra("studentid",studentid);
                    intent.putExtra("enrollid",singleEnrollment.getEnrollid());
                    intent.putExtra("courseid",singleEnrollment.getCourseid());
                    startActivity(intent);
                }else {
                    new AsyncCheckInternet_WithOutProgressBar(LearningActivity.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus)
                            {
                                SingleEnrollment singleEnrollment=enrollList.get(position);
                                Intent intent=new Intent(getApplicationContext(),PaperActivity.class);
                                intent.putExtra("studentid",studentid);
                                intent.putExtra("enrollid",singleEnrollment.getEnrollid());
                                intent.putExtra("courseid",singleEnrollment.getCourseid());
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

        getEnrollsFromLocal();

        if(userMode.mode()) {
            /*mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = null;

            if(appEnvironment==AppEnvironment.DEBUG) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        // Check the LogCat to get your test device ID
                        .addTestDevice(getString(R.string.test_device1))
                        .build();
            }else {
                adRequest = new AdRequest.Builder().build();
            }

            // Load ads into Interstitial Ads
            //mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });*/
        }

    }

    private void showInterstitial() {
        /*if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }*/
    }

    public void getEnrolls(){
        HashMap<String,String> hmap=new HashMap<>();
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
                                        myObj.getString("Enroll_branch_ID"),myObj.getString("Enroll_batch_ID"),myObj.getString("Enroll_course_ID"),myObj.getString("Enroll_batch_start_Dt"),myObj.getString("Enroll_batch_end_Dt"),
                                        myObj.getString("Enroll_Device_ID"),myObj.getString("Enroll_Date"),myObj.getString("Enroll_Status"),myObj.getString("enroll_Fee_Currency"),myObj.getString("enroll_Fee_Amount"),myObj.getString("enroll_Fee_tax_percentage"),
                                        myObj.getString("enroll_Total_Amount"),myObj.getString("enroll_Activation_Key"),myObj.getString("enroll_Activation_Date"),myObj.getString("enroll_Request_Date"),myObj.getString("enroll_ActivatedBy"),
                                        myObj.getString("enroll_Refdetails"),myObj.getString("remarks1"),myObj.getString("remarks2"));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
