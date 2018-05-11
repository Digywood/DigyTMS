package com.digywood.tms.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.AssessmentTestActivity;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.DBHelper.DBHelper;

import com.digywood.tms.IBagroundListener;
import com.digywood.tms.IDownloadStatus;
import com.digywood.tms.JSONParser;
import com.digywood.tms.Pojo.SingleAssessment;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.R;
import com.digywood.tms.SaveJSONdataToFile;
import com.digywood.tms.PracticeTestActivity;
import com.digywood.tms.URLClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AssesmentTestAdapter extends RecyclerView.Adapter<AssesmentTestAdapter.MyViewHolder> {

    ArrayList<SingleAssessment> testList;
    Dialog mydialog;
    ArrayList<String> downloadedList = new ArrayList<>();
    ArrayList<String> downloadfileList = new ArrayList<>();
    ArrayList<String> localPathList=new ArrayList();
    ArrayList<SingleDWDQues> chapterFileList=new ArrayList<>();
    ArrayList<String> chktestList = new ArrayList<>();
    ArrayList<String> finalUrls=new ArrayList<>();
    ArrayList<String> finalNames=new ArrayList<>();
    HashMap<String,String> hmap=new HashMap<>();
    Context mycontext;
    DBHelper myhelper;
    Boolean value = false;
    JSONParser myparser;
    String downloadjsonpath="",tfiledwdpath="",localpath="";
    String filedata = "", path, jsonPath, assessmentPath, photoPath, enrollid, courseid, subjectId, paperid, testid,fullTest ,assessment ,json;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid, tv_teststatus;
        public ImageView iv_start, iv_resume,btn_fstart,iv_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_atestid);
            tv_teststatus = view.findViewById(R.id.tv_ateststatus);
            iv_start = view.findViewById(R.id.iv_start);
            iv_resume = view.findViewById(R.id.iv_resume);
            iv_download = view.findViewById(R.id.iv_atestdownload);
        }
    }

    public AssesmentTestAdapter(ArrayList<SingleAssessment> testList, Context c) {
        this.testList = testList;
        this.mycontext = c;
        myhelper=new DBHelper(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_assesmenttestitem, parent, false);
        return new AssesmentTestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleAssessment singletest = testList.get(position);
        holder.tv_testid.setText(singletest.getTestid());
        holder.tv_teststatus.setText(singletest.getStatus());

        holder.iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog = new Dialog(mycontext);
                mydialog.getWindow();
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.assessmentauth);
                mydialog.show();


                final EditText key = mydialog.findViewById(R.id.et_key);
                Button submit = mydialog.findViewById(R.id.btn_submit_auth);
                Button cancel = mydialog.findViewById(R.id.btn_cancel_auth);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mydialog.cancel();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor = myhelper.validateAssessmentTestKey(singletest.getTestid());
                        Log.e("Auth",""+cursor.getCount());
                        if(cursor.getCount()> 0){
                            while (cursor.moveToNext()){
                                String auth = cursor.getString(cursor.getColumnIndex("satu_exam_key"));
                                if(auth.equals(key.getText().toString())){
                                    int count = myhelper.getAttempCount();
                                    Cursor c = myhelper.getAttempt(myhelper.getLastTestAttempt(singletest.getTestid()));
                                    //if cursor has values then the test is being resumed and data is retrieved from database
                                    if (c.getCount() > 0) {
                                        c.moveToLast();
                                        if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                                            myhelper.DeleteAttempt(myhelper.getLastTestAttempt(singletest.getTestid()));
                                        }
                                    }
                                    try {
                                        Log.e("AuthTest",""+getExternalPath(mycontext, singletest));
                                        fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest)), "UTF-8");
                                        assessment = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest) ), "UTF-8");
                                        Intent i = new Intent(mycontext, AssessmentTestActivity.class);
                                        i.putExtra("json", assessment);
                                        i.putExtra("test", testid);
                                        i.putExtra("status", "NEW");
                                        mycontext.startActivity(i);
                                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                        e.printStackTrace();
                                        Log.e("Error",e.toString());
                                    }

                                }
                            }
                        }
                        mydialog.cancel();
                    }
                });

            }
        });

/*        holder.iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(mycontext);
                TextView msg = new TextView(mycontext);
                msg.setText("Enter Assessment Test Key");
                msg.setGravity(Gravity.CENTER_HORIZONTAL);
                final EditText key = new EditText(mycontext);
                key.setGravity(Gravity.CENTER_HORIZONTAL);
                Button enter = new Button(mycontext);
                enter.setText("Submit");
                enter.setGravity(Gravity.CENTER_HORIZONTAL);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor = myhelper.validateAssessmentTestKey(singletest.getTestid());
                        if(cursor.getCount()> 0){
                            while (cursor.moveToNext()){
                                String auth = cursor.getString(cursor.getColumnIndex("satu_exam_key"));
                                if(auth.equals(key.getText().toString())){
                                    int count = myhelper.getAttempCount();
                                    Cursor c = myhelper.getAttempt(myhelper.getLastTestAttempt(singletest.getTestid()));
                                    //if cursor has values then the test is being resumed and data is retrieved from database
                                    if (c.getCount() > 0) {
                                        c.moveToLast();
                                        if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                                            myhelper.DeleteAttempt(myhelper.getLastTestAttempt(singletest.getTestid()));
                                        }
                                    }
                                    try {
                                        fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest)), "UTF-8");
                                        assessment = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest) ), "UTF-8");
                                        Intent i = new Intent(mycontext, AssessmentTestActivity.class);
                                        i.putExtra("json", assessment);
                                        i.putExtra("test", testid);
                                        i.putExtra("status", "NEW");
                                        mycontext.startActivity(i);
                                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                });
                popupBuilder.setView(msg);
                popupBuilder.setView(key);
                popupBuilder.setView(enter);
                popupBuilder.create().show();
            }
        });*/

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalUrls.clear();
                finalNames.clear();
                filedata="";

                hmap.clear();
                hmap.put("testid",singletest.getTestid());
                hmap.put("status","STARTED");
                new BagroundTask(URLClass.hosturl +"updatePractiseTestStatus.php",hmap,mycontext,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            Log.e("UploadStatus---",json);
                            if(json.equalsIgnoreCase("Updated")){

//                                long updateFlag=myhelper.updatePTestStatus(singletest.getTestid(),"STARTED");
//                                if(updateFlag>0){
//                                    Log.e("LocalStatusUpdate---","Updated Locally");
//                                }else{
//
//                                }

                                Cursor mycursor=myhelper.checkAssessmentTest(singletest.getTestid());
                                if(mycursor.getCount()>0){
                                    while(mycursor.moveToNext()){

                                        enrollid=mycursor.getString(mycursor.getColumnIndex("satu_entroll_id"));
                                        courseid=mycursor.getString(mycursor.getColumnIndex("satu_course_id"));
                                        subjectId=mycursor.getString(mycursor.getColumnIndex("satu_subjet_ID"));
                                        paperid=mycursor.getString(mycursor.getColumnIndex("satu_paper_ID"));

                                    }
                                }else{
                                    mycursor.close();
                                }

                                path=courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/";

                                downloadjsonpath=URLClass.downloadjson+"courses/"+path+singletest.getTestid()+".json";

                                tfiledwdpath=URLClass.downloadjson+"courses/"+path;

                                localpath=enrollid+"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/";

                                File myFile1 = new File(URLClass.mainpath+localpath+singletest.getTestid()+".json");
                                if(myFile1.exists()){

                                }else{
                                    finalUrls.add(downloadjsonpath);
                                    finalNames.add(singletest.getTestid()+".json");
                                    localPathList.add(URLClass.mainpath+localpath);
                                }

                                new DownloadFileAsync(mycontext,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                                    @Override
                                    public void downloadStatus(String status) {

                                        try{
                                            if(status.equalsIgnoreCase("Completed")){

                                                finalUrls.clear();
                                                finalNames.clear();
                                                localPathList.clear();

                                                filedata="";

                                                try{
                                                    BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+localpath+singletest.getTestid()+".json"));
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

                                                        Log.e("AssessmentAdp:--",""+sdq.getSubjectId());

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
                                                    Log.e("LearningActivity----","No Downloaded Images for test");
                                                }

                                                if(finalNames.size()!=0){

                                                    new DownloadFileAsync(mycontext,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                                                        @Override
                                                        public void downloadStatus(String status) {

                                                            try{
                                                                if(status.equalsIgnoreCase("Completed")){
                                                                    hmap.clear();
                                                                    hmap.put("testid",singletest.getTestid());
                                                                    hmap.put("status","Downloaded");
                                                                    new BagroundTask(URLClass.hosturl +"updatePractiseTestStatus.php",hmap, mycontext, new IBagroundListener() {
                                                                        @Override
                                                                        public void bagroundData(String json) {
                                                                            try {

                                                                                Log.e("UploadStatus---",json);
                                                                                if(json.equalsIgnoreCase("Updated")){
//                                                                                    long updateFlag=myhelper.updatePTestStatus(singletest.getTestid(),"DOWNLOADED");
//                                                                                    if(updateFlag>0){
//                                                                                        Log.e("LocalStatusUpdate---","Updated Locally");
//                                                                                    }else{
//
//                                                                                    }

                                                                                    Toast.makeText(mycontext,"All Downloaded",Toast.LENGTH_SHORT).show();
                                                                                }else{

                                                                                }

                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                                Log.e("ListofPractiseTests----", e.toString());
                                                                            }
                                                                        }
                                                                    }).execute();

                                                                }else{

                                                                }

                                                            }catch (Exception e){

                                                                e.printStackTrace();
                                                                Log.e("DownloadFile----",e.toString());
                                                            }
                                                        }
                                                    }).execute();

                                                }else{

                                                    Toast.makeText(mycontext,"All Downloaded",Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(mycontext,"Unable download test",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("ListofPractiseTests----",e.toString());
                        }

                    }
                }).execute();

            }
        });

    }

    public ArrayList<String> getchkTests() {
        return chktestList;
    }

    public ArrayList<String> getdwdchkTests() {
        return downloadedList;
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public void showAlert(String messege) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public String getExternalPath(Context context, SingleAssessment singletest) {
        DBHelper dataObj = new DBHelper(context);
        testid = singletest.getTestid();
        Cursor cursor = dataObj.getSingleAssessmentTests(testid);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                enrollid = cursor.getString(cursor.getColumnIndex("satu_entroll_id"));
                courseid = cursor.getString(cursor.getColumnIndex("satu_course_id"));
                subjectId = cursor.getString(cursor.getColumnIndex("satu_subjet_ID"));
                paperid = cursor.getString(cursor.getColumnIndex("satu_paper_ID"));
            }
        }

        Log.e("path_vars", enrollid + " " + courseid + " " + subjectId + " " + paperid + " " + testid);
        path = enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + testid + "/";
        photoPath = URLClass.mainpath + path;
        assessmentPath = URLClass.mainpath + path + testid +".json";
        jsonPath = URLClass.mainpath + path;
        return assessmentPath;
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

}