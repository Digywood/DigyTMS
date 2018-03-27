package com.digywood.tms;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.digywood.tms.Adapters.TestAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleTest;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListofTests extends AppCompatActivity {

    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleTest> testidList;
    ArrayList<String> subjectIds;
    RecyclerView rv_tests;
    TextView tv_emptytests;
    StringBuilder output;
    int currentitem;
    DBHelper myhelper;
    public static final int RequestPermissionCode = 1;
    TestAdapter tAdp;
    ArrayList<String> finalUrls,finalNames,finalPaths,selectedtestidList;
    ArrayList<String> downloadfileList;
    ArrayList<String> alreadydwdList;
    ArrayList<SingleTest> dwdupdateList;
    FloatingActionButton fab_download;
    LinearLayoutManager myLayoutManager;
    String enrollid="",courseid="",paperid="",subjectid="",downloadjsonpath="",path="",localpath="",filedata="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftests);
        rv_tests=findViewById(R.id.rv_listoftests);
        tv_emptytests=findViewById(R.id.tv_testemptydata);
        fab_download=findViewById(R.id.fab_download);
        testidList=new ArrayList<>();
        finalUrls=new ArrayList<>();
        finalNames=new ArrayList<>();
        finalPaths=new ArrayList<>();
        subjectIds=new ArrayList<>();
        dwdupdateList=new ArrayList<>();
        selectedtestidList=new ArrayList<>();
        downloadfileList=new ArrayList<>();
        alreadydwdList=new ArrayList<>();

        myhelper=new DBHelper(this);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
//            Log.e("ListofTests---",cmgintent.getStringExtra("paperid"));
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
                    Log.e("ListofTests-----",e.toString());
                }

                if(alreadydwdList.size()!=0){
                    showAlert("You have selected already downloaded tests,would you like download again?");
                }else{
                    if(selectedtestidList.size()!=0){
                        currentitem=0;
                        if(checkPermission()){
                            new AsyncCheckInternet(ListofTests.this,new INetStatus() {
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

        hmap.clear();
        hmap.put("testid",selectedtestidList.get(currentitem));
        hmap.put("status","STARTED");
        new BagroundTask(URLClass.hosturl +"updateTestStatus.php",hmap, ListofTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("UploadStatus---",json);
                    if(json.equalsIgnoreCase("Updated")){

                        long updateFlag=myhelper.updateTestStatus(selectedtestidList.get(currentitem),"STARTED");
                        if(updateFlag>0){
                            Log.e("LocalStatusUpdate---","Updated Locally");
                        }else{

                        }

                        path=courseid+"/"+subjectIds.get(currentitem)+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";
//        finalPaths.add(URLClass.mainpath+enrollid+"/"+courseid+"/"+subjectid+"/"+paperid+"/"+selectedtestidList.get(i)+"/");
                        downloadjsonpath=URLClass.downloadjson+"courses/"+path+selectedtestidList.get(currentitem)+".json";

                        localpath=enrollid+"/"+courseid+"/"+subjectIds.get(currentitem)+"/"+paperid+"/"+selectedtestidList.get(currentitem)+"/";

//                        File dir = new File(URLClass.mainpath+enrollid+"/"+courseid+"/"+subjectid+"/"+paperid+"/"+selectedtestidList.get(currentitem));
//                        if(dir.exists()){
//                            if (dir.isDirectory())
//                            {
//                                String[] children = dir.list();
//                                for (int i = 0; i < children.length; i++)
//                                {
//                                    new File(dir, children[i]).delete();
//                                }
//                            }else{
//                                dir.delete();
//                            }
//                        }else{
//
//                        }
                        File myFile1 = new File(URLClass.mainpath+localpath+selectedtestidList.get(currentitem)+".json");
                        if(myFile1.exists()){

                        }else{
                            finalUrls.add(downloadjsonpath);
                            finalNames.add(selectedtestidList.get(currentitem)+".json");
                        }

                        String[] urlList=new String[finalUrls.size()];
                        urlList = finalUrls.toArray(urlList);
                        String[] nameList = new String[finalNames.size()];
                        nameList = finalNames.toArray(nameList);

                        new DownloadFileAsync(ListofTests.this,URLClass.mainpath+localpath,urlList,nameList,new IDownloadStatus() {
                            @Override
                            public void downloadStatus(String status) {

                                try{
                                    if(status.equalsIgnoreCase("Completed")){

                                        finalUrls.clear();
                                        finalNames.clear();
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

                                            File myFile = new File(URLClass.mainpath+localpath);
                                            if(myFile.exists()){
                                                for(int i=0;i<downloadfileList.size();i++){

                                                    File myFile1 = new File(URLClass.mainpath+localpath+downloadfileList.get(i));
                                                    if(myFile1.exists()){

                                                    }else{
                                                        finalUrls.add(URLClass.downloadurl+downloadfileList.get(i));
                                                        finalNames.add(downloadfileList.get(i));
                                                    }
                                                }
                                            }else{
                                                for(int i=0;i<downloadfileList.size();i++){

                                                    finalUrls.add(URLClass.downloadurl+downloadfileList.get(i));
                                                    finalNames.add(downloadfileList.get(i));

                                                }
                                            }

                                        }else{
                                            if(currentitem<selectedtestidList.size()){
                                                currentitem=currentitem+1;
                                                downloadTest(selectedtestidList.get(currentitem));
                                            }else{
                                                Toast.makeText(getApplicationContext(),"All Downloaded",Toast.LENGTH_SHORT).show();
                                            }
                                            Log.e("LandingActivity----","No Downloaded Images for test");
                                        }

                                        if(finalNames.size()!=0){

                                            String[] urlList=new String[finalUrls.size()];
                                            urlList = finalUrls.toArray(urlList);
                                            String[] nameList = new String[finalNames.size()];
                                            nameList = finalNames.toArray(nameList);

                                            Log.e("TestName---",testidList.get(0).getTestid());

                                            new DownloadFileAsync(ListofTests.this,URLClass.mainpath+localpath,urlList,nameList,new IDownloadStatus() {
                                                @Override
                                                public void downloadStatus(String status) {

                                                    try{
                                                        if(status.equalsIgnoreCase("Completed")){
                                                            hmap.clear();
                                                            hmap.put("testid",selectedtestidList.get(currentitem));
                                                            hmap.put("status","DOWNLOADED");
                                                            new BagroundTask(URLClass.hosturl +"updateTestStatus.php",hmap, ListofTests.this, new IBagroundListener() {
                                                                @Override
                                                                public void bagroundData(String json) {
                                                                    try {

                                                                        Log.e("UploadStatus---",json);
                                                                        if(json.equalsIgnoreCase("Updated")){
                                                                            long updateFlag=myhelper.updateTestStatus(selectedtestidList.get(currentitem),"DOWNLOADED");
                                                                            if(updateFlag>0){
                                                                                Log.e("LocalStatusUpdate---","Updated Locally");
                                                                            }else{

                                                                            }

//                                                                            dwdupdateList.add(new SingleTest(selectedtestidList.get(currentitem),"","Downloaded"));
                                                                            if(currentitem<selectedtestidList.size()){
                                                                                currentitem=currentitem+1;
                                                                                downloadTest(selectedtestidList.get(currentitem));
                                                                            }else{
                                                                                Toast.makeText(getApplicationContext(),"All Downloaded",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }else{

                                                                        }

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        Log.e("ListofTests----", e.toString());
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

//                            if(dwdupdateList.size()!=0){
//
//
//
//                            }else{
//                                Toast.makeText(getApplicationContext(),"All Tests are not fully Downloaded",Toast.LENGTH_SHORT).show();
//                            }

                                        }else{
                                            currentitem=currentitem+1;
                                            downloadTest(selectedtestidList.get(currentitem));
                                            Log.e("LandingActivity----","All OwnAd Images Downloaded");
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
                    Log.e("ListofTests----",e.toString());
                }

            }
        }).execute();
    }

    public void getTestIdsFromLocal(){
        testidList.clear();
        try {
            Cursor mycursor=myhelper.getStudentTests();
            if(mycursor.getCount()>0){
                while (mycursor.moveToNext()) {
                    Log.e("DBHelper-->",""+mycursor.getCount());
                    testidList.add(new SingleTest(mycursor.getString(mycursor.getColumnIndex("sptu_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")),mycursor.getString(mycursor.getColumnIndex("sptu_dwnld_status"))));
                    subjectIds.add(mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID")));
                }
            }else {
                mycursor.close();
                Toast.makeText(ListofTests.this,"No Tests Available",Toast.LENGTH_LONG).show();
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
        new BagroundTask(URLClass.hosturl+"getTestsByCourseandPaper.php", hmap, ListofTests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("ListofTests----","JSON:comes-"+json);
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
                                Log.e("ListofTests----","Test Already Exists");
                            }else{
                                long insertFlag=myhelper.insertTest(myObj.getInt("sptu_key"),myObj.getString("sptu_org_id"),myObj.getString("sptu_entroll_id"),myObj.getString("sptu_student_ID"),
                                        myObj.getString("sptu_batch"),myObj.getString("sptu_ID"),myObj.getString("sptu_paper_ID"),myObj.getString("sptu_subjet_ID"),
                                        myObj.getString("sptu_course_id"),myObj.getString("sptu_start_date"),myObj.getString("sptu_end_date"),myObj.getString("sptu_dwnld_status"),
                                        myObj.getInt("sptu_no_of_questions"),myObj.getDouble("sptu_tot_marks"),myObj.getDouble("stpu_min_marks"),myObj.getDouble("sptu_max_marks"));
                                if(insertFlag>0){
                                    Log.e("ListofTests----","Test Inserted in Local");
                                }else{
                                    Log.e("ListofTests----","Local Test Insertion Failed");
                                }
                            }

                        }
                    }
                    getTestIdsFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofTests----",e.toString());
                }
            }
        }).execute();
    }

    public void setData(){
        if (testidList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + testidList.size());
            tv_emptytests.setVisibility(View.GONE);
            tAdp = new TestAdapter(testidList,ListofTests.this);
            myLayoutManager = new LinearLayoutManager(ListofTests.this, LinearLayoutManager.VERTICAL,false);
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

        JSONArray quesArray,optionsArray,additionsArray;
        JSONObject mainObj,singlequesObj,optionsObj,additionsObj;
        try{
            mainObj=new JSONObject(json);
            Log.e("JSON--",mainObj.getString("sptu_ID"));
            quesArray=mainObj.optJSONArray("questions");
            for(int i=0;i<quesArray.length();i++){

                singlequesObj=quesArray.getJSONObject(i);
                if(downloadfileList.contains(singlequesObj.getString("qbm_image_file"))){

                }else{
                    downloadfileList.add(singlequesObj.getString("qbm_image_file"));
                }
                if(downloadfileList.contains(singlequesObj.getString("qbm_Review_Images"))){

                }else{
                    downloadfileList.add(singlequesObj.getString("qbm_Review_Images"));
                }
                if(downloadfileList.contains(singlequesObj.getString("qbm_Additional_Image_ref"))){

                }else{
                    downloadfileList.add(singlequesObj.getString("qbm_Additional_Image_ref"));
                }

                optionsArray=singlequesObj.getJSONArray("options");
                for(int j=0;j<optionsArray.length();j++){

                    optionsObj=optionsArray.getJSONObject(j);
                    if(downloadfileList.contains(optionsObj.getString("qbo_media_file"))){

                    }else{
                        downloadfileList.add(optionsObj.getString("qbo_media_file"));
                    }
                }

                additionsArray=singlequesObj.getJSONArray("additions");
                for(int k=0;k<additionsArray.length();k++){

                    additionsObj=additionsArray.getJSONObject(k);
                    if(downloadfileList.contains(additionsObj.getString("qba_media_file"))){

                    }else{
                        downloadfileList.add(additionsObj.getString("qba_media_file"));
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
        ActivityCompat.requestPermissions(ListofTests.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                new AsyncCheckInternet(ListofTests.this,new INetStatus() {
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
                Toast.makeText(ListofTests.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListofTests.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        if(selectedtestidList.size()!=0){
                            currentitem=0;
                            if(checkPermission()){
                                new AsyncCheckInternet(ListofTests.this,new INetStatus() {
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

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_refresh:
//                new AsyncCheckInternet(ListofTests.this,new INetStatus() {
//                    @Override
//                    public void inetSatus(Boolean netStatus) {
//                        if(netStatus){
//                            getTestIds();
//                        }else{
//                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).execute();
//                return  true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
