package com.digywood.tms.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.FlashCardActivity;
import com.digywood.tms.IBagroundListener;
import com.digywood.tms.IDownloadStatus;
import com.digywood.tms.INetStatus;
import com.digywood.tms.JSONParser;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.ListofPractiseTests;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.R;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.ReviewActivity;
import com.digywood.tms.SaveJSONdataToFile;
import com.digywood.tms.TestActivity;
import com.digywood.tms.URLClass;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List;


public class PractiseTestAdapter extends RecyclerView.Adapter<PractiseTestAdapter.MyViewHolder> {

    ArrayList<SingleTest> testList;
    ArrayList<String> downloadedList = new ArrayList<>();
    ArrayList<String> downloadfileList = new ArrayList<>();
    ArrayList<String> groupIds = new ArrayList<>();
    ArrayList<String> chktestList = new ArrayList<>();
    ArrayList<String> fimageList = new ArrayList<>();
    ArrayList<String> finalUrls=new ArrayList<>();
    ArrayList<String> finalNames=new ArrayList<>();
    ArrayList<String> localPathList=new ArrayList();
    ArrayList<SingleDWDQues> chapterFileList=new ArrayList<>();
    HashMap<String,String> hmap=new HashMap<>();
    Context mycontext;
    DBHelper myhelper;
    Boolean value = false;
    JSONParser myparser;
    public static final int RequestPermissionCode = 1;
    String filedata = "", path, jsonPath, attemptPath, photoPath, enrollid, courseid,groupdata="";
    String subjectId, paperid, testid, fullTest, attempt,json,downloadjsonpath="",tfiledwdpath="",localpath="";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid, tv_teststatus,tv_testAttempt,tv_min,tv_max;
        public Button btn_pstart, btn_review, btn_fstart;
        ImageView iv_history,iv_download;
        PieChart test_pieChart,flash_pieChart;
//        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_testid);
            tv_teststatus = view.findViewById(R.id.tv_teststatus);
            btn_pstart = view.findViewById(R.id.btn_pstart);
            btn_review = view.findViewById(R.id.btn_review);
            btn_fstart = view.findViewById(R.id.btn_fstart);
            iv_history = view.findViewById(R.id.iv_history);
            iv_download = view.findViewById(R.id.iv_download);
            test_pieChart = (PieChart) view.findViewById(R.id.test_piechart);
            tv_testAttempt = view.findViewById(R.id.tv_testAttempt);
            flash_pieChart = (PieChart) view.findViewById(R.id.flash_piechart);
//            cb_download = view.findViewById(R.id.cb_testselection);
        }
    }

    public PractiseTestAdapter(ArrayList<SingleTest> testList, Context c) {
        this.testList = testList;
        this.mycontext = c;
        myhelper = new DBHelper(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testitem, parent, false);
        return new PractiseTestAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final SingleTest singletest = testList.get(position);
        List<PieEntry> yvalues = new ArrayList<PieEntry>();
        holder.tv_testAttempt.setText(String.valueOf(myhelper.getTestAttempCount(singletest.getTestid()))+" Attempts");
        yvalues.add(new PieEntry(20f, 0));
        yvalues.add(new PieEntry(80f, 0));
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(100,196,125));
        colors.add(Color.rgb(201,201,201));
        PieDataSet dataSet = new PieDataSet(yvalues, "Average Score");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        holder.test_pieChart.setDrawHoleEnabled(true);
        holder.test_pieChart.setRotationEnabled(true);
        //test_pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Avg. Score");
        description.setTextSize(15);
        holder.test_pieChart.setDescription(description);
        holder.test_pieChart.setHoleColor(Color.WHITE);
        holder.test_pieChart.setTransparentCircleRadius(68f);
        holder.test_pieChart.setHoleRadius(68f);
        holder.test_pieChart.setTransparentCircleAlpha(0);
        holder.test_pieChart.setCenterText("20%");
        holder.test_pieChart.setCenterTextSize(20);
        holder.test_pieChart.setCenterTextColor(mycontext.getResources().getColor(R.color.green));
        holder.test_pieChart.setData(data);

        for (IDataSet<?> set : holder.test_pieChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        holder.test_pieChart.invalidate();

        holder.flash_pieChart.setDrawHoleEnabled(true);
        holder.flash_pieChart.setRotationEnabled(true);
        //test_pieChart.setUsePercentValues(true);
        Description f_description = new Description();
        description.setText("Avg. Score");
        description.setTextSize(15);
        holder.flash_pieChart.setDescription(description);
        holder.flash_pieChart.setHoleColor(Color.WHITE);
        holder.flash_pieChart.setTransparentCircleRadius(68f);
        holder.flash_pieChart.setHoleRadius(68f);
        holder.flash_pieChart.setTransparentCircleAlpha(0);
        holder.flash_pieChart.setCenterText("20%");
        holder.flash_pieChart.setCenterTextSize(15);
        holder.test_pieChart.setCenterTextColor(mycontext.getResources().getColor(R.color.green));
        holder.flash_pieChart.setCenterTextColor(Color.BLACK);
        holder.flash_pieChart.setData(data);

/*
        for (IDataSet<?> set : holder.flash_pieChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        holder.flash_pieChart.invalidate();
*/



        holder.tv_testid.setText(singletest.getTestid());
        holder.tv_teststatus.setText(singletest.getStatus());
        final DBHelper dataObj = new DBHelper(mycontext);
        if (dataObj.getQuestionCount() == 0) {
            holder.btn_review.setEnabled(false);
        } else
            holder.btn_review.setEnabled(true);
        int count = dataObj.getAttempCount();
        Cursor c = dataObj.getAttempt(dataObj.getLastAttempt());
        Log.e("Cursor Count---",""+c.getCount());
        //if cursor has values then the test is being resumed and data is retrieved from database
        if (c.getCount() > 0) {
            c.moveToLast();
            if (c.getInt(c.getColumnIndex("Attempt_Status")) == 1) {
                Log.e("TAdapter",""+c.getString(c.getColumnIndex("Attempt_Test_ID")));
                if(c.getString(c.getColumnIndex("Attempt_Test_ID")).equalsIgnoreCase(singletest.getTestid())) {
                    holder.btn_review.setText("Resume");
                    holder.btn_review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                                Log.e("Attempt_testadapter", attempt.toString());
                                Intent i = new Intent(mycontext, TestActivity.class);
                                i.putExtra("json", attempt);
                                i.putExtra("test", testid);
                                i.putExtra("status", "RESUME");
                                mycontext.startActivity(i);
                            } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                holder.btn_review.setText("Review");
                testid = singletest.getTestid();
                holder.btn_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                            Intent i = new Intent(mycontext, ReviewActivity.class);
                            i.putExtra("test", testid);
                            i.putExtra("json", attempt);
                            mycontext.startActivity(i);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        holder.btn_pstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObj.Destroy("attempt_data");
                int count = dataObj.getAttempCount();
                Cursor c = dataObj.getAttempt(dataObj.getLastTestAttempt(singletest.getTestid()));
                Log.e("attempt_created:", ""+count);
//                Log.e("valu-e",""+c.getInt(c.getColumnIndex("Attempt_Status")));
                //if cursor has values then the test is being resumed and data is retrieved from database
                if (c.getCount() > 0) {
                    c.moveToLast();
                    Log.e("value", "" + c.getInt(c.getColumnIndex("Attempt_Status")));
                    if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                        dataObj.DeleteAttempt(dataObj.getLastTestAttempt(singletest.getTestid()));
                    }
                }
                try {
                    fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "BASE") + testid + ".json"), "UTF-8");
                    JSONParser obj = new JSONParser(fullTest, getExternalPath(mycontext, singletest, "ATTEMPT"), "PRACTICE", mycontext);
                    attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                    Log.e("attempt_created:", attempt);
                    Intent i = new Intent(mycontext, TestActivity.class);
                    i.putExtra("json", attempt);
                    i.putExtra("test", testid);
                    i.putExtra("status","NEW");
                    mycontext.startActivity(i);
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.btn_fstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor mycursor = myhelper.getSingleTestData(singletest.getTestid());
                if (mycursor.getCount() > 0) {
                    while (mycursor.moveToNext()) {
//                        studentid=mycursor.getString(mycursor.getColumnIndex("sptu_student_ID"));
                        enrollid = mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                        courseid = mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                        subjectId = mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                        paperid = mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                    }
                } else {
                    mycursor.close();
                }

                try {
                    String tPath = URLClass.mainpath+enrollid +"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/";

                    File file = new File(tPath +singletest.getTestid() + ".json");
                    if (!file.exists()) {
                        showAlert("Main JSON file for test " + singletest.getTestid() + " is not found! \n Please download test data if not ");
                    } else {
                        BufferedReader br = new BufferedReader(new FileReader(tPath + singletest.getTestid() + ".json"));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        filedata = sb.toString();
                        fimageList = readJson(filedata);
                        br.close();

                        int seccount=myhelper.getPtuSecCount(singletest.getTestid());
                        if(seccount>0){
                            myparser = new JSONParser(filedata,tPath + "/flashAttempts/", "FLASH", mycontext);
                            Intent i = new Intent(mycontext, FlashCardActivity.class);
                            i.putExtra("testId", testList.get(position).getTestid());
                            i.putExtra("testPath", tPath);
                            mycontext.startActivity(i);
                        }else{
                            showAlert("Test Configuration is not Available for " + singletest.getTestid() + " \n Please download test data if not ");
                        }

                        //                    if (fimageList.size() != 0) {
//
//                        ArrayList<String> missingfList = new ArrayList<>();
//
//                        for (int i = 0; i < fimageList.size(); i++) {
//                            File myFile = new File(URLClass.mainpath + fimageList.get(i));
//                            if (myFile.exists()) {
//
//                            } else {
//                                missingfList.add(fimageList.get(i));
//                            }
//                        }
//
//                        if (missingfList.size() != 0) {
//                            StringBuilder sbm = new StringBuilder();
//                            sbm.append("The following file are missing...\n");
//                            for (int i = 0; i < missingfList.size(); i++) {
//                                sbm.append(missingfList.get(i) + " , " + "\n");
//                            }
//                            showAlert(sbm.toString());
//                        } else {
//                            Intent i = new Intent(mycontext, FlashCardActivity.class);
//                            i.putExtra("testId",testList.get(position).getTestid());
//                            i.putExtra("testPath",tPath);
//                            mycontext.startActivity(i);
//                        }
//                    } else {
//                        Log.e("FlashCardActivity---", "No Questions to Display");
//                    }
                        Intent i = new Intent(mycontext, FlashCardActivity.class);
                        i.putExtra("testId", testList.get(position).getTestid());
                        i.putExtra("testPath", tPath);
                        mycontext.startActivity(i);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TestActivity1-----", e.toString());
                }
            }
        });

        holder.iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mycontext, AttemptDataActivity.class);
                i.putExtra("testId", singletest.getTestid());
                mycontext.startActivity(i);
            }
        });

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

                                long updateFlag=myhelper.updatePTestStatus(singletest.getTestid(),"STARTED");
                                if(updateFlag>0){
                                    Log.e("LocalStatusUpdate---","Updated Locally");
                                }else{

                                }

                                Cursor mycursor=myhelper.getSingleTestData(singletest.getTestid());
                                if(mycursor.getCount()>0){
                                    while(mycursor.moveToNext()){

                                        enrollid=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                                        courseid=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                                        subjectId=mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                                        paperid=mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

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

                                                getTestConfig(singletest.getTestid(),groupdata);

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
                                                    Log.e("LandingActivity----","No Downloaded Images for test");
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
                                                                                    long updateFlag=myhelper.updatePTestStatus(singletest.getTestid(),"DOWNLOADED");
                                                                                    if(updateFlag>0){
                                                                                        Log.e("LocalStatusUpdate---","Updated Locally");
                                                                                    }else{

                                                                                    }

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

 /*       holder.cb_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.cb_download.isChecked()) {
                    if (singletest.getStatus().equalsIgnoreCase("DOWNLOADED")) {
                        downloadedList.add(singletest.getTestid());
                    } else {

                    }

                    if (chktestList.size() != 0) {
                        if (chktestList.contains(singletest.getTestid())) {

                        } else {
                            chktestList.add(singletest.getTestid());
                        }
                    } else {
                        chktestList.add(singletest.getTestid());
                    }

                } else {

                    if (downloadedList.contains(singletest.getTestid())) {
                        downloadedList.remove(singletest.getTestid());
                    }

                    chktestList.remove(singletest.getTestid());
                }

            }
        });
        */

    }

    public void updateTests(ArrayList<SingleTest> list) {
        testList = list;
        notifyDataSetChanged();
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

    public String getExternalPath(Context context, SingleTest singletest, String type) {
        DBHelper dataObj = new DBHelper(context);
        testid = singletest.getTestid();
        Cursor cursor = dataObj.getSingleStudentTests(testid);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                enrollid = cursor.getString(cursor.getColumnIndex("sptu_entroll_id"));
                courseid = cursor.getString(cursor.getColumnIndex("sptu_course_id"));
                subjectId = cursor.getString(cursor.getColumnIndex("sptu_subjet_ID"));
                paperid = cursor.getString(cursor.getColumnIndex("sptu_paper_ID"));
            }
        }

        Log.e("path_vars", enrollid + " " + courseid + " " + subjectId + " " + paperid + " " + testid);
        path = enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + testid + "/";
        photoPath = URLClass.mainpath + path;
        attemptPath = URLClass.mainpath + path + "Attempt/";
        jsonPath = URLClass.mainpath + path;
        if (type.equals("BASE")) {
            return jsonPath;
        } else
            return attemptPath;
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

    public void getTestConfig(final String testid, String groupidData){
        hmap.clear();
        hmap.put("testId",testid);
        hmap.put("groupiddata",groupidData);
        new BagroundTask(URLClass.hosturl +"getTestConfig.php",hmap,mycontext,new IBagroundListener() {
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


    public ArrayList<String> readJson(String filedata) {
        ArrayList<String> flashimageList = new ArrayList();
        JSONObject mainObj, secObj, singlequesObj, optionsObj, additionsObj;
        JSONArray ja_sections, ja_questions, optionsArray, additionsArray;
        String testid = "", section = "", sectionid = "";

        try {
            mainObj = new JSONObject(filedata);

            testid = mainObj.getString("ptu_test_ID");

            Log.e("JSON--", mainObj.getString("ptu_test_ID"));

            ja_sections = mainObj.getJSONArray("Sections");

            Log.e("sectionArray Length---", "" + ja_sections.length());
            for (int i = 0; i < ja_sections.length(); i++) {

                secObj = ja_sections.getJSONObject(i);
                section = secObj.getString("ptu_section_name");
                sectionid = secObj.getString("ptu_section_ID");
                ja_questions = secObj.getJSONArray("Questions");

                Log.e("QuesArray Length---", "" + ja_questions.length());
                for (int q = 0; q < ja_questions.length(); q++) {

                    singlequesObj = ja_questions.getJSONObject(q);
                    String flashname = singlequesObj.getString("qbm_flash_image");
                    flashimageList.add(flashname);
                    singlequesObj = ja_questions.getJSONObject(q);
                    if (singlequesObj.getString("qbm_group_flag").equalsIgnoreCase("YES")) {

                        if (groupIds.contains(singlequesObj.getString("gbg_id"))) {

                        } else {
                            groupIds.add(singlequesObj.getString("gbg_id"));
                        }

                    } else {

                    }

                    if (downloadfileList.contains(singlequesObj.getString("qbm_image_file"))) {

                    } else {
                        value = false;
                        downloadfileList.add(singlequesObj.getString("qbm_image_file"));
                    }
                    if (downloadfileList.contains(singlequesObj.getString("qbm_Review_Images"))) {

                    } else {
                        value = false;
                        downloadfileList.add(singlequesObj.getString("qbm_Review_Images"));
                    }
                    if (downloadfileList.contains(singlequesObj.getString("qbm_Additional_Image_ref"))) {

                    } else {
                        value = false;
                        downloadfileList.add(singlequesObj.getString("qbm_Additional_Image_ref"));
                    }

                    optionsArray = singlequesObj.getJSONArray("Options");
                    for (int j = 0; j < optionsArray.length(); j++) {

                        optionsObj = optionsArray.getJSONObject(j);
                        if (downloadfileList.contains(optionsObj.getString("qbo_media_file"))) {

                        } else {
                            value = false;
                            downloadfileList.add(optionsObj.getString("qbo_media_file"));
                        }
                    }

                    additionsArray = singlequesObj.getJSONArray("Review");
                    for (int k = 0; k < additionsArray.length(); k++) {

                        additionsObj = additionsArray.getJSONObject(k);
                        if (downloadfileList.contains(additionsObj.getString("qba_media_file"))) {

                        } else {
                            value = false;
                            downloadfileList.add(additionsObj.getString("qba_media_file"));
                        }


                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSONPARSE---", e.toString() + " : " + e.getStackTrace()[0].getLineNumber());
        }
        return flashimageList;
    }
}