package com.digywood.tms;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digywood.tms.Adapters.PaperAdapter;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SinglePaper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PaperActivity extends AppCompatActivity {

    ArrayList<String> papernameList;
    ArrayList<String> paperidList;
    ArrayList<SinglePaper> paperList;
    GridView papergridView;
    SinglePaper singlePaper;
    Dialog mydialog;
    int clickpos=0;
    public static final int RequestPermissionCode = 1;
    String courseid="",enrollid="",studentid="";
    DBHelper myhelper;
    LinearLayoutManager myLayoutManager;
    PaperAdapter pAdp;

    InterstitialAd mInterstitialAd;
    AppEnvironment appEnvironment;
    UserMode userMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

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

        papergridView=findViewById(R.id.paper_grid);

        paperidList=new ArrayList<>();
        papernameList=new ArrayList<>();
        paperList=new ArrayList<>();

        myhelper=new DBHelper(this);

        final Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
        }

        papergridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v,int position, long id) {

                clickpos=position;

                if(checkPermission()){
                    showPopUp();
                }else{
                    requestPermission();
                }
            }
        });

        getPapersFromLocal();

        if(userMode.mode()) {
            mInterstitialAd = new InterstitialAd(this);

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
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void getPapersFromLocal(){
        paperidList.clear();
        papernameList.clear();
        Cursor mycursor=myhelper.getPapersByCourse(courseid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperidList.add(paperid);
                papernameList.add(papername);
            }
            Log.e("PaperActivity----",""+paperidList.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperidList.size()>0){

            paperList.clear();
            int ptestcount=0,atestcount=0,pattemptcount=0,fattemptcount=0,aattemptcount=0;
            double pmin=0.0,pmax=0.0,pavg=0.0,fmin=0.0,fmax=0.0,favg=0.0,amin=0.0,amax=0.0,aavg=0.0,pprogress=0.0,fprogress=0.0,aprogress=0.0;

            for(int i=0;i<paperidList.size();i++){

                ptestcount=myhelper.getTestsByPaper(studentid,enrollid,paperidList.get(i));

                Cursor mycur=myhelper.getPractiseSummaryByPaper(studentid,enrollid,paperidList.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){

                        pattemptcount=mycur.getInt(mycur.getColumnIndex("attemptpcount"));
                        pmin=mycur.getDouble(mycur.getColumnIndex("minscore"));
                        pmax=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                        pavg=mycur.getDouble(mycur.getColumnIndex("avgscore"));

                    }
                }else{
                    mycur.close();
                }

                if(ptestcount>0){
                    Double varcount=Double.parseDouble(String.valueOf(pattemptcount));
                    pprogress=(varcount/ptestcount)*100;
                }else{
                    pprogress=0.0;
                }

                Cursor mycur1=myhelper.getFlashSummaryByPaper(studentid,enrollid,paperidList.get(i));
                if(mycur1.getCount()>0){
                    while (mycur1.moveToNext()){

                        fattemptcount=mycur1.getInt(mycur1.getColumnIndex("attemptfcount"));
                        fmin=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
                        fmax=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
                        favg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur1.close();
                }

                if(ptestcount>0){
                    Double varcount=Double.parseDouble(String.valueOf(fattemptcount));
                    fprogress=(varcount/ptestcount)*100;
                }else {
                    fprogress=0;
                }

                atestcount=myhelper.getAssessmentTestsByPaper(studentid,enrollid,paperidList.get(i));

                Cursor mycur2=myhelper.getAssessmentSummaryByPaper(studentid,enrollid,paperidList.get(i));
                if(mycur2.getCount()>0){
                    while (mycur2.moveToNext()){

                        aattemptcount=mycur2.getInt(mycur2.getColumnIndex("attemptacount"));
                        amin=mycur2.getDouble(mycur2.getColumnIndex("minscore"));
                        amax=mycur2.getDouble(mycur2.getColumnIndex("maxscore"));
                        aavg=mycur2.getDouble(mycur2.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur2.close();
                }

                if(atestcount>0){
                    Double varcount=Double.parseDouble(String.valueOf(aattemptcount));
                    aprogress=(varcount/atestcount)*100;
                }else{
                    aprogress=0;
                }

                paperList.add(new SinglePaper(paperidList.get(i),papernameList.get(i),ptestcount,atestcount,pattemptcount,fattemptcount,aattemptcount,pprogress,fprogress,aprogress,pmin,pavg,pmax,fmin,favg,fmax,amin,aavg,amax));

            }
        }

//        setData();

        pAdp = new PaperAdapter(PaperActivity.this,paperList,enrollid,courseid);
        papergridView.setAdapter(pAdp);

    }

    public void showPopUp(){

        singlePaper=paperList.get(clickpos);

        mydialog = new Dialog(PaperActivity.this);
        mydialog.getWindow();
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.activity_testpopup);
        mydialog.show();

        LinearLayout ll_pratise=mydialog.findViewById(R.id.ll_practise);
        LinearLayout ll_assesment=mydialog.findViewById(R.id.ll_assessment);

        ll_pratise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
                i.putExtra("studentid",studentid);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                i.putExtra("paperid",singlePaper.getPaperId());
                startActivity(i);
                finish();
                mydialog.cancel();
            }
        });

        ll_assesment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ListofAssessmentTests.class);
                i.putExtra("studentid",studentid);
                i.putExtra("enrollid",enrollid);
                i.putExtra("courseid",courseid);
                i.putExtra("paperid",singlePaper.getPaperId());
                startActivity(i);
                finish();
                mydialog.cancel();
            }
        });

    }

    public void getPapersData(){
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("courseid",courseid);
        new BagroundTask(URLClass.hosturl + "getPapersByCourseId.php",hmap,PaperActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("PaperActivity------","json:comes"+json);
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
                                Log.e("PaperActivity----","Paper Already Exists");
                            }else{
                                long insertFlag=myhelper.insertPaper(myObj.getInt("Paper_Key"),myObj.getString("Paper_ID"),myObj.getString("Paper_Seq_no"),myObj.getString("Subject_ID"),
                                        myObj.getString("Course_ID"),myObj.getString("Paper_Name"),myObj.getString("Paper_Short_Name"),myObj.getString("Paper_Min_Pass_Marks"),
                                        myObj.getString("Paper_Max_Marks"));
                                if(insertFlag>0){
                                    Log.e("PaperActivity----","Paper Inserted in Local");
                                }else{
                                    Log.e("PaperActivity----","Local Paper Insertion Failed");
                                }
                            }

                        }
                    }
                    getPapersFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("PaperActivity------",e.toString());
                }
            }
        }).execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent i=new Intent(getApplicationContext(),LearningActivity.class);
        i.putExtra("studentid",studentid);
        i.putExtra("sname","");
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i=new Intent(getApplicationContext(),LearningActivity.class);
        i.putExtra("studentid",studentid);
        i.putExtra("sname","");
        startActivity(i);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PaperActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                showPopUp();
            }
            else {
                Toast.makeText(PaperActivity.this, "This permission required to use full functionality of application!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
