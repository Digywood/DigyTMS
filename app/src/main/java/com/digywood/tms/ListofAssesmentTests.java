package com.digywood.tms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.AssesmentTestAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleTest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListofAssesmentTests extends AppCompatActivity {

    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleTest> testidList;
    ArrayList<String> subjectIds;
    ArrayList<String> groupIds=new ArrayList<>();
    RecyclerView rv_tests;
    TextView tv_emptytests;
    StringBuilder output;
    int currentitem;
    DBHelper myhelper;
    public static final int RequestPermissionCode = 1;
    AssesmentTestAdapter tAdp;
    ArrayList<String> finalUrls,finalNames,finalPaths,selectedtestidList;
    ArrayList<String> downloadfileList;
    ArrayList<SingleDWDQues> chapterFileList;
    ArrayList<String> chapterList;
    ArrayList<String> localPathList;
    ArrayList<String> alreadydwdList;
    ArrayList<SingleTest> dwdupdateList;
    FloatingActionButton fab_download;
    LinearLayoutManager myLayoutManager;
    String enrollid="",courseid="",paperid="",subjectid="",downloadjsonpath="",path="",localpath="",filedata="",groupdata="",tfiledwdpath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assesmenttests);

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

        rv_tests=findViewById(R.id.rv_listofatests);
        tv_emptytests=findViewById(R.id.tv_atestemptydata);
        fab_download=findViewById(R.id.fab_atdownload);
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
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
            paperid=cmgintent.getStringExtra("paperid");
        }

        getTestIdsFromLocal();

        fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    selectedtestidList=tAdp.getchkTests();

                    alreadydwdList=tAdp.getdwdchkTests();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofATests-----",e.toString());
                }

                if(alreadydwdList.size()!=0){
                    showAlert("You have selected already downloaded tests,would you like download again?");
                }else{
                    if(selectedtestidList.size()!=0){
                        currentitem=0;
                        if(checkPermission()){
                            new AsyncCheckInternet(ListofAssesmentTests.this,new INetStatus() {
                                @Override
                                public void inetSatus(Boolean netStatus) {
                                    if(netStatus){
                                        downloadTest(selectedtestidList.get(0));
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
            }
        });

    }

    public void downloadTest(String filename){

        finalUrls.clear();
        finalNames.clear();
        filedata="";

        hmap.clear();
        hmap.put("testid",selectedtestidList.get(currentitem));
        hmap.put("status","STARTED");
        new BagroundTask(URLClass.hosturl +"updateAssesmentTestStatus.php",hmap,ListofAssesmentTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("UploadStatus---",json);
                    if(json.equalsIgnoreCase("Updated")){

                        long updateFlag=myhelper.updateATestStatus(selectedtestidList.get(currentitem),"STARTED");
                        if(updateFlag>0){
                            Log.e("LocalStatusUpdate---","Updated Locally");
                        }else{

                        }

                        Cursor mycursor=myhelper.getAssesmentTestData(selectedtestidList.get(currentitem));
                        if(mycursor.getCount()>0){
                            while(mycursor.moveToNext()){

                                enrollid=mycursor.getString(mycursor.getColumnIndex("satu_entroll_id"));
                                courseid=mycursor.getString(mycursor.getColumnIndex("satu_course_id"));
                                subjectid=mycursor.getString(mycursor.getColumnIndex("satu_subjet_ID"));
                                paperid=mycursor.getString(mycursor.getColumnIndex("satu_paper_ID"));

                            }
                        }else{
                            mycursor.close();
                        }

//                        path=courseid+"/"+subjectIds.get(currentitem)+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";
                        path=courseid+"/"+subjectid+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";

                        downloadjsonpath=URLClass.downloadjson+"courses/"+path+selectedtestidList.get(currentitem)+".json";

                        tfiledwdpath=URLClass.downloadjson+"courses/"+path;

//                        localpath=enrollid+"/"+courseid+"/"+subjectIds.get(currentitem)+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";
                        localpath=enrollid+"/"+courseid+"/"+subjectid+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";


                        File myFile1 = new File(URLClass.mainpath+localpath+selectedtestidList.get(currentitem)+".json");
                        if(myFile1.exists()){

                        }else{
                            finalUrls.add(downloadjsonpath);
                            finalNames.add(selectedtestidList.get(currentitem)+".json");
                            localPathList.add(URLClass.mainpath+localpath);
                        }

                        new DownloadFileAsync(ListofAssesmentTests.this,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                            @Override
                            public void downloadStatus(String status) {

                                try{
                                    if(status.equalsIgnoreCase("Completed")){

                                        finalUrls.clear();
                                        finalNames.clear();
                                        localPathList.clear();

                                        filedata="";

                                        try{
                                            BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+localpath+selectedtestidList.get(currentitem)+".json"));
                                            StringBuilder sb = new StringBuilder();
                                            String line = br.readLine();

                                            while (line != null) {
                                                sb.append(line);
                                                sb.append("\n");
                                                line = br.readLine();
                                            }
                                            filedata=sb.toString();
                                            br.close();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Log.e("TestActivity1-----",e.toString());
                                        }

                                        parseJson(filedata);

                                        if(downloadfileList.size()!=0){

                                            for(int i=0;i<chapterFileList.size();i++){

                                                SingleDWDQues sdq=chapterFileList.get(i);

                                                File myFile1 = new File(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/"+sdq.getFileName());
                                                if(myFile1.exists()){

                                                }else{

                                                    String tPath=URLClass.downloadjson+"courses/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/";
                                                    finalUrls.add(tPath+sdq.getChapterId()+"/"+sdq.getFileName());
                                                    finalNames.add(sdq.getFileName());
                                                    localPathList.add(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/");
                                                }
                                            }

                                        }else{
                                            currentitem=currentitem+1;
                                            if(currentitem<selectedtestidList.size()){
                                                downloadTest(selectedtestidList.get(currentitem));
                                            }else{
                                                Toast.makeText(getApplicationContext(),"All Downloaded",Toast.LENGTH_SHORT).show();
                                            }
                                            Log.e("LandingActivity----","No Downloaded Images for test");
                                        }

                                        if(finalNames.size()!=0){

                                            new DownloadFileAsync(ListofAssesmentTests.this,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                                                @Override
                                                public void downloadStatus(String status) {

                                                    try{
                                                        if(status.equalsIgnoreCase("Completed")){
                                                            hmap.clear();
                                                            hmap.put("testid",selectedtestidList.get(currentitem));
                                                            hmap.put("status","Downloaded");
                                                            new BagroundTask(URLClass.hosturl +"updateAssesmentTestStatus.php",hmap, ListofAssesmentTests.this, new IBagroundListener() {
                                                                @Override
                                                                public void bagroundData(String json) {
                                                                    try {

                                                                        Log.e("UploadStatus---",json);
                                                                        if(json.equalsIgnoreCase("Updated")){
                                                                            long updateFlag=myhelper.updateATestStatus(selectedtestidList.get(currentitem),"DOWNLOADED");
                                                                            if(updateFlag>0){
                                                                                Log.e("LocalStatusUpdate---","Updated Locally");
                                                                            }else{

                                                                            }

                                                                            currentitem=currentitem+1;
                                                                            if(currentitem<selectedtestidList.size()){
                                                                                downloadTest(selectedtestidList.get(currentitem));
                                                                            }else{
                                                                                Toast.makeText(getApplicationContext(),"All Downloaded",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }else{

                                                                        }

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        Log.e("ListofPractiseTests----", e.toString());
                                                                    }
                                                                }
                                                            }).execute();

                                                        }else{

//                                                            dwdupdateList.add(new SingleTest(selectedtestidList.get(currentitem),"","Partially Downloaded"));

                                                        }

                                                    }catch (Exception e){

                                                        e.printStackTrace();
                                                        Log.e("DownloadFile----",e.toString());
                                                    }
                                                }
                                            }).execute();

                                        }else{

                                            currentitem=currentitem+1;
                                            if(currentitem<selectedtestidList.size()){
                                                downloadTest(selectedtestidList.get(currentitem));
                                            }else{
                                                Toast.makeText(getApplicationContext(),"All Downloaded",Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }else{

                                    }
                                }catch (Exception e){

                                    e.printStackTrace();
                                    Log.e("DownloadFile----",e.toString());
                                }
                            }
                        }).execute();
                    }else{
                        Toast.makeText(getApplicationContext(),"Unable download test",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofPractiseTests----",e.toString());
                }

            }
        }).execute();
    }

    public void getTestIdsFromLocal(){
        testidList.clear();
        try {
            Cursor mycursor=myhelper.getAssesmentTestsByEnroll(enrollid);
            if(mycursor.getCount()>0){
                while (mycursor.moveToNext()) {
                    testidList.add(new SingleTest(mycursor.getString(mycursor.getColumnIndex("satu_ID")),mycursor.getString(mycursor.getColumnIndex("satu_subjet_ID")),mycursor.getString(mycursor.getColumnIndex("satu_dwnld_status"))));
                }
            }else {
                mycursor.close();
                Toast.makeText(ListofAssesmentTests.this,"No Assesment Tests Available",Toast.LENGTH_LONG).show();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        setData();
    }

    public void getTestIds(){
        hmap.clear();
        hmap.put("courseid",courseid);
        hmap.put("paperid",paperid);
        new BagroundTask(URLClass.hosturl+"getTestsByCourseandPaper.php", hmap, ListofAssesmentTests.this,new IBagroundListener() {
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
                            testidList.add(new SingleTest(testid,subjectid,status));

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
            tAdp = new AssesmentTestAdapter(testidList,ListofAssesmentTests.this);
            myLayoutManager = new LinearLayoutManager(ListofAssesmentTests.this, LinearLayoutManager.VERTICAL,false);
            rv_tests.setLayoutManager(myLayoutManager);
            rv_tests.setItemAnimator(new DefaultItemAnimator());
            rv_tests.setAdapter(tAdp);
        } else {
            tv_emptytests.setText("No Tests for student");
            tv_emptytests.setVisibility(View.VISIBLE);
        }
    }

    public void parseJson(String json){

        downloadfileList.clear();
        chapterFileList.clear();
        localPathList.clear();

        JSONArray secArray,quesArray,optionsArray,additionsArray;
        JSONObject mainObj,secObj,singlequesObj,optionsObj,additionsObj;
        try{
            mainObj=new JSONObject(json);
            Log.e("JSON--",mainObj.getString("atu_ID"));

            secArray=mainObj.optJSONArray("Sections");

            for(int d=0;d<secArray.length();d++){

                secObj=secArray.getJSONObject(d);
                quesArray=secObj.optJSONArray("Questions");

                for(int i=0;i<quesArray.length();i++){

                    singlequesObj=quesArray.getJSONObject(i);

                    String chapterid=singlequesObj.getString("qbm_ChapterID");
                    String paperid=singlequesObj.getString("qbm_Paper_ID");
                    String subid=singlequesObj.getString("qbm_SubjectID");

                    if(singlequesObj.getString("qbm_group_flag").equalsIgnoreCase("YES")){

                        if(groupIds.contains(singlequesObj.getString("gbg_id"))){

                        }else{
                            groupIds.add(singlequesObj.getString("gbg_id"));
                        }

                        if(downloadfileList.contains(singlequesObj.getString("gbg_media_file"))){

                        }else{
                            downloadfileList.add(singlequesObj.getString("gbg_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,singlequesObj.getString("gbg_media_file")));
                        }

                    }else{

                    }

                    if(downloadfileList.contains(singlequesObj.getString("qbm_image_file"))){

                    }else{
                        downloadfileList.add(singlequesObj.getString("qbm_image_file"));
                        chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,singlequesObj.getString("qbm_image_file")));
                    }

                    if(downloadfileList.contains(singlequesObj.getString("qbm_Review_Images"))){

                    }else{
                        downloadfileList.add(singlequesObj.getString("qbm_Review_Images"));
                        chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,singlequesObj.getString("qbm_Review_Images")));
                    }

                    if(downloadfileList.contains(singlequesObj.getString("qbm_flash_image"))){

                    }else{
                        downloadfileList.add(singlequesObj.getString("qbm_flash_image"));
                        chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,singlequesObj.getString("qbm_flash_image")));
                    }

                    optionsArray=singlequesObj.getJSONArray("Options");
                    for(int j=0;j<optionsArray.length();j++){

                        optionsObj=optionsArray.getJSONObject(j);
                        if(downloadfileList.contains(optionsObj.getString("qbo_media_file"))){

                        }else{
                            downloadfileList.add(optionsObj.getString("qbo_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,optionsObj.getString("qbo_media_file")));
                        }
                    }

                    additionsArray=singlequesObj.getJSONArray("Review");
                    for(int k=0;k<additionsArray.length();k++){

                        additionsObj=additionsArray.getJSONObject(k);
                        if(downloadfileList.contains(additionsObj.getString("qba_media_file"))){

                        }else{
                            downloadfileList.add(additionsObj.getString("qba_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid,paperid,subid,additionsObj.getString("qba_media_file")));
                        }

                    }

                }

            }

            Log.e("JSONPARSE---",""+downloadfileList.size());

        }catch (Exception e){
            e.printStackTrace();
            Log.e("JSONPARSE---",e.toString());
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
        ActivityCompat.requestPermissions(ListofAssesmentTests.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                new AsyncCheckInternet(ListofAssesmentTests.this,new INetStatus() {
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){
                            path=enrollid+"/"+courseid+"/"+subjectid+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";
                            downloadTest(selectedtestidList.get(currentitem));
                        }else {
                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
            }
            else {
                Toast.makeText(ListofAssesmentTests.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListofAssesmentTests.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        if(selectedtestidList.size()!=0){
                            currentitem=0;
                            if(checkPermission()){
                                new AsyncCheckInternet(ListofAssesmentTests.this,new INetStatus() {
                                    @Override
                                    public void inetSatus(Boolean netStatus) {
                                        if(netStatus){
                                            downloadTest(selectedtestidList.get(0));
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
//            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
