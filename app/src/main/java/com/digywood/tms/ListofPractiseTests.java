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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.digywood.tms.Adapters.PractiseTestAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleTest;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListofPractiseTests extends AppCompatActivity {

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
    PractiseTestAdapter tAdp;
    ArrayList<String> finalUrls,finalNames,finalPaths,selectedtestidList;
    ArrayList<String> downloadfileList;
    ArrayList<SingleDWDQues> chapterFileList;
    ArrayList<String> chapterList;
    ArrayList<String> localPathList;
    ArrayList<String> alreadydwdList;
    ArrayList<SingleTest> dwdupdateList;
    LinearLayoutManager myLayoutManager;
    String studentid="",enrollid="",courseid="",paperid="",subjectid="",downloadjsonpath="",path="",localpath="",filedata="",groupdata="",tfiledwdpath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftests);

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
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
            paperid=cmgintent.getStringExtra("paperid");
        }

        Log.e("Test:--","Eid:---"+enrollid+"  Cid:---"+courseid+"  Pid:----"+paperid);

        getTestIdsFromLocal();

    }

    public void downloadTest(String filename){

        finalUrls.clear();
        finalNames.clear();
        filedata="";

        hmap.clear();
        hmap.put("testid",selectedtestidList.get(currentitem));
        hmap.put("status","STARTED");
        new BagroundTask(URLClass.hosturl +"updatePractiseTestStatus.php",hmap,ListofPractiseTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("UploadStatus---",json);
                    if(json.equalsIgnoreCase("Updated")){

                        long updateFlag=myhelper.updatePTestStatus(studentid,selectedtestidList.get(currentitem),"STARTED");
                        if(updateFlag>0){
                            Log.e("LocalStatusUpdate---","Updated Locally");
                        }else{

                        }

                        Cursor mycursor=myhelper.checkPractiseTest(studentid,selectedtestidList.get(currentitem));
                        if(mycursor.getCount()>0){
                            while(mycursor.moveToNext()){

                                enrollid=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                                courseid=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                                subjectid=mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                                paperid=mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

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

                        new DownloadFileAsync(ListofPractiseTests.this,localPathList,finalUrls,finalNames,new IDownloadStatus() {
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

                                        getTestConfig(selectedtestidList.get(currentitem),groupdata);

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
                                            Log.e("LearningActivity----","No Downloaded Images for test");
                                        }

                                        if(finalNames.size()!=0){

                                            new DownloadFileAsync(ListofPractiseTests.this,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                                                @Override
                                                public void downloadStatus(String status) {

                                                    try{
                                                        if(status.equalsIgnoreCase("Completed")){
                                                            hmap.clear();
                                                            hmap.put("testid",selectedtestidList.get(currentitem));
                                                            hmap.put("status","Downloaded");
                                                            new BagroundTask(URLClass.hosturl +"updatePractiseTestStatus.php",hmap, ListofPractiseTests.this, new IBagroundListener() {
                                                                @Override
                                                                public void bagroundData(String json) {
                                                                    try {

                                                                        Log.e("UploadStatus---",json);
                                                                        if(json.equalsIgnoreCase("Updated")){
                                                                            long updateFlag=myhelper.updatePTestStatus(studentid,selectedtestidList.get(currentitem),"DOWNLOADED");
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
            Cursor mycursor=myhelper.getStudentTests(studentid,enrollid,courseid,paperid,"A");
            if(mycursor.getCount()>0){
                while (mycursor.moveToNext()) {
                    testidList.add(new SingleTest(mycursor.getString(mycursor.getColumnIndex("sptu_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_name")),mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_dwnld_status"))));
                    subjectIds.add(mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")));
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

    public void getTestConfig(final String testid, String groupidData){
        hmap.clear();
        hmap.put("testId",testid);
        hmap.put("groupiddata",groupidData);
        new BagroundTask(URLClass.hosturl +"getTestConfig.php",hmap,ListofPractiseTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {

                JSONArray groupArray,quesConfigArray,groupConfigArray,sectionArray;
                JSONObject groupObj=null,qconObj=null,gquesconObj=null,sectionObj=null;

                try{
                    Log.e("ListofPractiseTests---",json);

                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("qbgroup");

                    if (obj1 instanceof JSONArray)
                    {
                        long Qgroupdelcount=myhelper.deleteTestGroups(testid);
                        Log.e("groupdelcount---",""+Qgroupdelcount);
                        groupArray=myObj.getJSONArray("qbgroup");
                        if(groupArray!=null && groupArray.length()>0){
                            Log.e("groupLength---",""+groupArray.length());
                            int p=0,q=0;
                            for(int i=0;i<groupArray.length();i++){

                                groupObj=groupArray.getJSONObject(i);
                                long insertFlag=myhelper.insertQuesGroup(groupObj.getInt("qbg_key"),groupObj.getString("qbg_ID"),testid,groupObj.getString("qbg_media_type"),groupObj.getString("qbg_media_file"),
                                        groupObj.getString("qbg_text"),groupObj.getInt("qbg_no_questions"),groupObj.getInt("qbg_no_pick"),groupObj.getString("qbg_status"),
                                        groupObj.getString("qbg_created_by"),groupObj.getString("qbg_created_dttm"),groupObj.getString("qbg_mod_by"),groupObj.getString("qbg_mod_dttm"),groupObj.getString("qbg_type"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("QGroups--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("QGroups--","No Question Groups: ");
                    }

                    Object obj2=myObj.get("ques_config");

                    if (obj2 instanceof JSONArray)
                    {
                        long Qconfigcount=myhelper.deleteQuesConfig(testid);
                        Log.e("QuesConDelCount---",""+Qconfigcount);
                        quesConfigArray=myObj.getJSONArray("ques_config");
                        if(quesConfigArray!=null && quesConfigArray.length()>0){
                            Log.e("QuesConLength---",""+quesConfigArray.length());
                            int p=0,q=0;
                            for(int i=0;i<quesConfigArray.length();i++){

                                qconObj=quesConfigArray.getJSONObject(i);
                                long insertFlag=myhelper.insertQuesConfig(qconObj.getInt("ques_configkey"),qconObj.getString("courseId"),qconObj.getString("subjectId"),qconObj.getString("paperId"),
                                        qconObj.getString("testId"),qconObj.getString("categoryId"),qconObj.getString("subcategoryId"),qconObj.getInt("avail_count"),qconObj.getInt("pickup_count"),qconObj.getInt("min_pickup_count"),qconObj.getString("ques_configstatus"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("QuesConfig--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("QuesConfig--","No QuesConfig: ");
                    }

                    Object obj3=myObj.get("groupques_config");

                    if (obj3 instanceof JSONArray)
                    {
                        long Gconfigdelcount=myhelper.deleteGroupsConfig(testid);
                        Log.e("groupcondelcount---",""+Gconfigdelcount);
                        groupConfigArray=myObj.getJSONArray("groupques_config");
                        if(groupConfigArray!=null && groupConfigArray.length()>0){
                            Log.e("groupconLength---",""+groupConfigArray.length());
                            int p=0,q=0;
                            for(int i=0;i<groupConfigArray.length();i++){

                                gquesconObj=groupConfigArray.getJSONObject(i);
                                long insertFlag=myhelper.insertGroupConfig(gquesconObj.getInt("groupques_configKey"),gquesconObj.getString("courseId"),gquesconObj.getString("subjectId"),gquesconObj.getString("paperId"),
                                        gquesconObj.getString("testId"),gquesconObj.getString("sectionId"),gquesconObj.getString("groupType"),gquesconObj.getInt("groupavail_count"),
                                        gquesconObj.getInt("grouppickup_count"),gquesconObj.getString("groupques_configstatus"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("GroupConfig--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("GroupConfig--","No GroupConfig: ");
                    }

                    Object obj4=myObj.get("sections");

                    if (obj4 instanceof JSONArray)
                    {
                        long sectiondelcount=myhelper.deletePtuSections(testid);
                        Log.e("sectiondelcount---",""+sectiondelcount);
                        sectionArray=myObj.getJSONArray("sections");
                        if(sectionArray!=null && sectionArray.length()>0){
                            Log.e("sectionLength---",""+sectionArray.length());
                            int p=0,q=0;
                            for(int i=0;i<sectionArray.length();i++){

                                sectionObj=sectionArray.getJSONObject(i);
                                long insertFlag=myhelper.insertPtuSection(sectionObj.getInt("Ptu_section_key"),sectionObj.getString("Ptu_ID"),sectionObj.getInt("Ptu_section_sequence"),sectionObj.getString("Ptu_section_ID"),
                                        sectionObj.getString("Ptu_section_paper_ID"),sectionObj.getString("Ptu_section_subject_ID"),sectionObj.getString("Ptu_section_course_ID"),sectionObj.getInt("Ptu_section_min_questions"),
                                        sectionObj.getInt("Ptu_section_max_questions"),sectionObj.getInt("Ptu_sec_tot_groups"),sectionObj.getInt("Ptu_sec_no_groups"),sectionObj.getString("Ptu_section_status"),sectionObj.getString("Ptu_section_name"));
                                if(insertFlag>0){
                                    p++;
                                }else {
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("Sections--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Sections--","No Sections: ");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofPractiseTests---",e.toString());
                }

            }
        }).execute();
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

    public void getTestIds(){
        hmap.clear();
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
            tAdp = new PractiseTestAdapter(testidList,ListofPractiseTests.this,studentid);
            myLayoutManager = new LinearLayoutManager(ListofPractiseTests.this, LinearLayoutManager.VERTICAL,false);
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
            Log.e("JSON--",mainObj.getString("ptu_test_ID"));

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

            if(groupIds.size()!=0){
                for(int i=0;i<groupIds.size();i++){
                    if(i==0){
                        groupdata="'"+groupIds.get(i)+"'";
                    }else{
                        groupdata=groupdata+",'"+groupIds.get(i)+"'";
                    }
                }
            }else{
                Log.e("ListofPractiseTests----","No Groups Available in Test");
            }

            Log.e("JSONPARSE---",""+groupdata);

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
        hmap.clear();
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
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void exitByBackKey() {
        finish();
        Intent intent = new Intent(ListofPractiseTests.this, LearningActivity.class);
        startActivity(intent);

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
