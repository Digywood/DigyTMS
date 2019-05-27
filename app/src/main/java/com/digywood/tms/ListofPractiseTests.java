package com.digywood.tms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.digywood.tms.Adapters.PractiseTestAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleTest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListofPractiseTests extends AppCompatActivity {

    ArrayList<SingleTest> testidList;
    ArrayList<String> subjectIds;
    ArrayList<String> groupIds=new ArrayList<>();
    RecyclerView rv_tests;
    TextView tv_emptytests;
    StringBuilder output;
    int currentitem;
    DBHelper myhelper;
    public static final int RequestPermissionCode = 1;
    PractiseTestAdapter tAdp;
    ArrayList<String> finalUrls,finalNames,finalPaths,selectedtestidList;
    ArrayList<String> downloadfileList;
    ArrayList<SingleDWDQues> chapterFileList;
    ArrayList<String> chapterList;
    ArrayList<String> localPathList;
    ArrayList<String> alreadydwdList;
    ArrayList<SingleTest> dwdupdateList;
    LinearLayoutManager myLayoutManager;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",downloadjsonpath="",path="",localpath="",filedata="",groupdata="",tfiledwdpath="",orgid="",studentname="",number="",email="";

    //InterstitialAd mInterstitialAd;
    AppEnvironment appEnvironment;
    UserMode userMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftests);
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

        rv_tests=findViewById(R.id.rv_listoftests);
        tv_emptytests=findViewById(R.id.tv_testemptydata);
//        fab_download=findViewById(R.id.fab_download);
        testidList=new ArrayList<>();
        finalUrls=new ArrayList<>();
        finalNames=new ArrayList<>();
        finalPaths=new ArrayList<>();
        subjectIds=new ArrayList<>();
        dwdupdateList=new ArrayList<>();
        selectedtestidList=new ArrayList<>();
        downloadfileList=new ArrayList<>();
        alreadydwdList=new ArrayList<>();
        chapterList=new ArrayList<>();
        chapterFileList=new ArrayList<>();
        localPathList=new ArrayList<>();

        myhelper=new DBHelper(this);

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

        getTestIdsFromLocal();

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

    public void getTestIdsFromLocal(){
        testidList.clear();
        try {
            Cursor mycursor=myhelper.getStudentTests(studentid,enrollid,paperid,"A");
            if(mycursor.getCount()>0){
                while (mycursor.moveToNext()) {
                    // only 1 test for enrollment EAA0000010
                    testidList.add(new SingleTest(mycursor.getString(mycursor.getColumnIndex("sptu_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_name")),mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_dwnld_status"))));
                    subjectIds.add(mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")));
                    Log.e("Test:--","Eid:---"+enrollid+"  Cid:---"+courseid+"  Pid:----"+paperid+"Sub---"+mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"))+"count---"+mycursor.getCount());

                }
            }else {
                mycursor.close();
                Toast.makeText(ListofPractiseTests.this,"No Tests Available",Toast.LENGTH_LONG).show();
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        setData();
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (!userMode.mode()) {
            Intent i = new Intent(getApplicationContext(), PaperActivity.class);
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
        }else {
            new AsyncCheckInternet_WithOutProgressBar(getApplicationContext(), new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus)
                    {
                        Intent i = new Intent(getApplicationContext(), PaperActivity.class);
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
                    }else {
                        Toast.makeText(getApplicationContext(), "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!userMode.mode()) {
            Intent i = new Intent(getApplicationContext(), PaperActivity.class);
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
        }else {
            new AsyncCheckInternet_WithOutProgressBar(getApplicationContext(), new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus)
                    {
                        Intent i = new Intent(getApplicationContext(), PaperActivity.class);
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
                    }else {
                        Toast.makeText(getApplicationContext(), "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    public void getTestIds(){
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("courseid",courseid);
        hmap.put("paperid",paperid);
        new BagroundTask(URLClass.hosturl+"getTestsByCourseandPaper.php", hmap, ListofPractiseTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("ListofPractiseTests----","JSON:comes-"+json);
                    if(json.equalsIgnoreCase("Tests_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Tests for User",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        for(int i=0;i<ja.length();i++){

                            myObj=ja.getJSONObject(i);
                            String testid=myObj.getString("sptu_ID");
                            subjectid=myObj.getString("sptu_subjet_ID");
                            String status=myObj.getString("sptu_dwnld_status");
                            testidList.add(new SingleTest(testid,"sample",subjectid,status));

                            long checkFlag=myhelper.checkTest(myObj.getInt("sptu_key"));
                            if(checkFlag>0){
                                Log.e("ListofPractiseTests----","Test Already Exists");
                            }else{
//                                long insertFlag=myhelper.insertPractiseTest(myObj.getInt("sptu_key"),myObj.getString("sptu_org_id"),myObj.getString("sptu_entroll_id"),myObj.getString("sptu_student_ID"),
//                                        myObj.getString("sptu_batch"),myObj.getString("sptu_ID"),myObj.getString("sptu_paper_ID"),myObj.getString("sptu_subjet_ID"),
//                                        myObj.getString("sptu_course_id"),myObj.getString("sptu_start_date"),myObj.getString("sptu_end_date"),myObj.getString("sptu_dwnld_status"),
//                                        myObj.getInt("sptu_no_of_questions"),myObj.getDouble("sptu_tot_marks"),myObj.getDouble("stpu_min_marks"),myObj.getDouble("sptu_max_marks"));
//                                if(insertFlag>0){
//                                    Log.e("ListofPractiseTests----","Test Inserted in Local");
//                                }else{
//                                    Log.e("ListofPractiseTests----","Local Test Insertion Failed");
//                                }
                            }

                        }
                    }
                    getTestIdsFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofPractiseTests----",e.toString());
                }
            }
        }).execute();
    }

    public void setData(){
        if (testidList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + testidList.size());
            tv_emptytests.setVisibility(View.GONE);
            tAdp = new PractiseTestAdapter(testidList,ListofPractiseTests.this,studentid,enrollid,studentname,number,email,courseid,paperid,orgid);
            myLayoutManager = new LinearLayoutManager(ListofPractiseTests.this, RecyclerView.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tAdp);
        } else {
            tv_emptytests.setText("No Tests for student");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ListofPractiseTests.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                new AsyncCheckInternet(ListofPractiseTests.this,new INetStatus() {
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){

                        }else {
                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
            }
            else {
                Toast.makeText(ListofPractiseTests.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateTestData(){
        HashMap<String,String> hmap=new HashMap<>();
        JSONObject finalObj=buildJson();
        hmap.put("JSON",finalObj.toString());
        new BagroundTask("updatePTestsData",hmap,ListofPractiseTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("Json--","comes.."+json);


                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofPTests-----",e.toString());
                }
            }
        }).execute();
    }

    public JSONObject buildJson(){
        JSONObject createObj=null;

        return createObj;
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListofPractiseTests.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        if(selectedtestidList.size()!=0){
                            currentitem=0;
                            if(checkPermission()){
                                new AsyncCheckInternet(ListofPractiseTests.this,new INetStatus() {
                                    @Override
                                    public void inetSatus(Boolean netStatus) {
                                        if(netStatus){

                                        }else {
                                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).execute();
                            }else{
                                requestPermission();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"Please Choose tests to be downloaded",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Info!");
        alert.setIcon(R.drawable.info);
        alert.show();
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
        finish();
        Intent i = new Intent(ListofPractiseTests.this, Activity_TestTypes.class);
        i.putExtra("studentid",studentid);
        i.putExtra("sname",studentname);
        i.putExtra("number",number);
        i.putExtra("email",email);
        i.putExtra("enrollid",enrollid);
        i.putExtra("courseid",courseid);
        i.putExtra("paperid",paperid);
        i.putExtra("orgid",orgid);
        startActivity(i);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listptests, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
