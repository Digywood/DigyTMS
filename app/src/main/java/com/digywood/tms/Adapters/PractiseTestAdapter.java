package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.EncryptDecrypt;
import com.digywood.tms.FlashCardActivity;
import com.digywood.tms.IBagroundListener;
import com.digywood.tms.IDownloadStatus;
import com.digywood.tms.JSONParser;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.ListofPractiseTests;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.PracticeTestActivity;
import com.digywood.tms.R;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.ReviewActivity;
import com.digywood.tms.SaveJSONdataToFile;
import com.digywood.tms.URLClass;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


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
    ArrayList<SingleDWDQues> missFileData=new ArrayList<>();
    ArrayList<SingleDWDQues> chapterFileList=new ArrayList<>();
    Context mycontext;
    DBHelper myhelper;
    Boolean value = false;
    JSONParser myparser;
    int fattemptcount=0;
    SharedPreferences restoredprefs;
    Double minscore,maxscore,avgscore,fminscore,favgscore,fmaxscore;
    public static final int RequestPermissionCode = 1;
    String filedata = "", path, jsonPath, attemptPath, photoPath, enrollid="", courseid,groupdata="",startdttm="",endddtm="";
    String studentid="",subjectId, paperid, testid, fullTest, attempt=null,json,downloadjsonpath=""/*,tfiledwdpath=""*/,localpath="";
    String restoredsname="",serverId="",finalUrl="",finalAssetUrl="";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid,tv_noOfQues, tv_teststatus,tv_testAttempt,tv_attempt_min,tv_attempt_max,tv_flashAttempt,tv_attempt_avg;
        public  TextView tv_flash_avg,tv_flash_max,tv_flash_min;
        ImageView iv_start,iv_resume,iv_review,iv_fstart;
        ImageView iv_history,iv_download;
        LinearLayout ll_start, ll_resume, ll_review, ll_fstart;
//        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_testid);
            tv_noOfQues = view.findViewById(R.id.tv_NoOfQues);
            tv_teststatus = view.findViewById(R.id.tv_teststatus);
            iv_start = view.findViewById(R.id.iv_start);
            iv_resume = view.findViewById(R.id.iv_resume);
            iv_review = view.findViewById(R.id.iv_review);
            iv_fstart = view.findViewById(R.id.iv_fstart);
            iv_history = view.findViewById(R.id.iv_history);
            iv_download = view.findViewById(R.id.iv_download);
            ll_start = view.findViewById(R.id.ll_start);
            ll_resume = view.findViewById(R.id.ll_resume);
            ll_review = view.findViewById(R.id.ll_review);
            ll_fstart = view.findViewById(R.id.ll_fstart);
//            test_pieChart = (PieChart) view.findViewById(R.id.test_piechart);
            tv_testAttempt = view.findViewById(R.id.tv_testAttempt);
            tv_attempt_max = view.findViewById(R.id.tv_attempt_max);
            tv_attempt_avg = view.findViewById(R.id.tv_attempt_avg);
            tv_attempt_min = view.findViewById(R.id.tv_attempt_min);
            tv_flashAttempt = view.findViewById(R.id.tv_flashAttempt);
            tv_flash_max = view.findViewById(R.id.tv_flash_max);
            tv_flash_avg = view.findViewById(R.id.tv_flash_avg);
            tv_flash_min = view.findViewById(R.id.tv_flash_min);
//            flash_pieChart = (PieChart) view.findViewById(R.id.flash_piechart);
//            cb_download = view.findViewById(R.id.cb_testselection);
        }
    }

    public PractiseTestAdapter(ArrayList<SingleTest> testList,Context c,String studentId,String enrollId) {
        this.testList = testList;
        this.mycontext = c;
        this.studentid=studentId;
        this.enrollid=enrollId;
        myhelper = new DBHelper(c);

        restoredprefs = mycontext.getSharedPreferences("SERVERPREF", MODE_PRIVATE);
        restoredsname = restoredprefs.getString("servername","main_server");
        if(restoredsname.equalsIgnoreCase("main_server")){
            finalUrl=URLClass.hosturl;
            finalAssetUrl=URLClass.downloadjson;
        }else{
            serverId=myhelper.getServerId(restoredsname);
            finalUrl="http://"+serverId+URLClass.loc_hosturl;
            finalAssetUrl="http://"+serverId+URLClass.loc_downloadjson;
        }

        Log.e("FINALURL:--",""+finalUrl);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testitem, parent, false);
        return new PractiseTestAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final SingleTest singletest = testList.get(position);
        Cursor mycursor=myhelper.getTestRawData(singletest.getTestid(),studentid,enrollid);
        if(mycursor.getCount()>0) {
            while (mycursor.moveToNext()) {
                minscore = mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                maxscore = mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                avgscore = mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
            }
        }

        List<PieEntry> yvalues = new ArrayList<PieEntry>();

        holder.tv_testAttempt.setText(String.valueOf(myhelper.getTestAttempCount(singletest.getTestid(),studentid)));
        holder.tv_attempt_min.setText(String.format("%.1f",minscore));
        holder.tv_attempt_max.setText(String.format("%.1f",maxscore));
        holder.tv_attempt_avg.setText(String.format("%.1f",avgscore));

        Cursor mycur=myhelper.getTestFlashSummary(singletest.getTestid(),studentid,enrollid);
        if(mycur.getCount()>0){
            while (mycur.moveToNext()){
                fattemptcount=mycur.getInt(mycur.getColumnIndex("sptuflash_attempts"));
                fminscore=mycur.getDouble(mycur.getColumnIndex("min_flashScore"));
                fmaxscore=mycur.getDouble(mycur.getColumnIndex("max_flashScore"));
                favgscore=mycur.getDouble(mycur.getColumnIndex("avg_flashScore"));
            }
        }else{
            mycur.close();
        }

        String nqus=myhelper.getNumberOfQuestions(singletest.getTestid());
        holder.tv_noOfQues.setText(nqus);

        holder.tv_flashAttempt.setText(""+fattemptcount);
        holder.tv_flash_max.setText(""+round(fmaxscore,1));
        holder.tv_flash_min.setText(""+round(fminscore,1));
        holder.tv_flash_avg.setText(""+round(favgscore,1));

        holder.tv_testid.setText(singletest.getTestName()+" ("+singletest.getTestid()+")");

        holder.tv_teststatus.setText(singletest.getStatus());
        final DBHelper dataObj = new DBHelper(mycontext);
        if (dataObj.getQuestionCount(testid) == 0) {
            holder.iv_review.setEnabled(false);
        } else
            holder.iv_review.setEnabled(true);
        final Cursor c = dataObj.getAttempt(dataObj.getLastTestAttempt(singletest.getTestid(),studentid));
        //if cursor has values then the test is being resumed and data is retrieved from database
        if (c.getCount() > 0) {
            c.moveToLast();
            if (c.getInt(c.getColumnIndex("Attempt_Status")) == 1) {
                if(c.getString(c.getColumnIndex("Attempt_Test_ID")).equalsIgnoreCase(singletest.getTestid())) {
                    holder.ll_resume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            holder.ll_resume.setBackground(mycontext.getResources().getDrawable(R.drawable.layout_press_custom));
                            try {
                                ((ListofPractiseTests)mycontext).finish();
                                String path=getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json";
                                File file = new File(path);
                                if(file.exists()){
                                attempt = new String(SaveJSONdataToFile.bytesFromFile(path), "UTF-8");
                                Intent i = new Intent(mycontext, PracticeTestActivity.class);
                                i.putExtra("json", attempt);
                                i.putExtra("test", testid);
                                i.putExtra("studentid", studentid);
                                i.putExtra("enrollid", enrollid);
                                i.putExtra("courseid", courseid);
                                i.putExtra("subjectid", subjectId);
                                i.putExtra("paperid", paperid);
                                i.putExtra("status", "RESUME");
                                mycontext.startActivity(i);}
                            } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else {
                testid = singletest.getTestid();
                holder.ll_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        holder.ll_review.setBackground(mycontext.getResources().getDrawable(R.drawable.layout_press_custom));
                        try {
                            //((ListofPractiseTests)mycontext).finish();
                            Log.e("Exec","Review,");
                            String path=getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json";
                            File file = new File(path);
                            if(file.exists()){
                                attempt = new String(SaveJSONdataToFile.bytesFromFile(path), "UTF-8");
                                Intent i = new Intent(mycontext, ReviewActivity.class);
                                i.putExtra("test", testid);
                                i.putExtra("studentid", studentid);
                                i.putExtra("enrollid", enrollid);
                                i.putExtra("courseid", courseid);
                                i.putExtra("subjectid", subjectId);
                                i.putExtra("paperid", paperid);
                                i.putExtra("json", attempt);
                                i.putExtra("TYPE", "PRACTISE_TEST");
                                ((ListofPractiseTests)mycontext).finish();
                            mycontext.startActivity(i);}
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        holder.ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                dataObj.Destroy("attempt_data");
                int count = dataObj.getAttempCount();
                Cursor c = dataObj.getAttempt(dataObj.getLastTestAttempt(singletest.getTestid()));
                int seccount=myhelper.getPtuSecCount(singletest.getTestid());
                if(seccount>0) {
                    Log.e("attempt_created:", "" + count);
                    //if cursor has values then the test is being resumed and data is retrieved from database
                    if (c.getCount() > 0) {
                        c.moveToLast();
                        if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                            dataObj.DeleteAttempt(dataObj.getLastTestAttempt(singletest.getTestid()));
                        }
                    }
                    try {
                        fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "BASE") + testid + ".json"), "UTF-8");
//                        JSONParser obj = new JSONParser(fullTest, getExternalPath(mycontext, singletest, "ATTEMPT"), "PRACTICE", mycontext);
                        attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                        Intent i = new Intent(mycontext, PracticeTestActivity.class);
                        i.putExtra("json", attempt);
                        i.putExtra("test", testid);
                        i.putExtra("status", "NEW");
                        mycontext.startActivity(i);
                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }else{
                    showAlert("Test Configuration is not Available for " + singletest.getTestid() + " \n Please download test data to fetch configuration ");
                }*/
//                holder.ll_start.setBackground(mycontext.getResources().getDrawable(R.drawable.layout_press_custom));
                missFileData.clear();

                Cursor mycursor = myhelper.checkPractiseTest(studentid,singletest.getTestid());
                if (mycursor.getCount() > 0) {
                    while (mycursor.moveToNext()) {
//                        studentid=mycursor.getString(mycursor.getColumnIndex("sptu_student_ID"));
                        courseid = mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                        subjectId = mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                        paperid = mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                    }
                } else {
                    mycursor.close();
                }

                try {
                    String tPath = URLClass.mainpath+enrollid +"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/ENC/";

                    File file = new File(tPath +singletest.getTestid() + ".json");
                    if (!file.exists()) {
                        showAlert("Main JSON file for test " + singletest.getTestid() + " is not found! \n Please download test data if not ");
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(getTheDecriptedJson(tPath + singletest.getTestid() + ".json")));
//                        BufferedReader br = new BufferedReader(new FileReader(tPath + singletest.getTestid() + ".json"));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        filedata = sb.toString();
                        parseJson(filedata);
                        br.close();

                        int seccount=myhelper.getPtuSecCount(singletest.getTestid());
                        if(seccount>0){

                            if(downloadfileList.size()!=0){

                                ArrayList<String> missingfList = new ArrayList<>();

                                for(int i=0;i<chapterFileList.size();i++){

                                    SingleDWDQues sdq=chapterFileList.get(i);

                                    File myFile1 = new File(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                                    if(myFile1.exists()){

                                    }else{
                                        missingfList.add(downloadfileList.get(i));
                                        missFileData.add(sdq);
                                    }
                                }

/*                                if(missingfList.size()!=0){
                                    StringBuilder sbm = new StringBuilder();
                                    sbm.append("The following files are missing...\n");
                                    for (int i = 0; i < missingfList.size(); i++) {
                                        sbm.append(missingfList.get(i) + " , " + "\n");
                                    }
                                    showReportAlert(sbm.toString(),singletest.getTestid());
                                }else{*/
                                    dataObj.DestroyPracticeRecord("attempt_data",singletest.getTestid());
//                                    dataObj.Destroy("attempt_data");
                                    int count = dataObj.getTestAttempCount(singletest.getTestid(),studentid);
                                    Log.e("Attempt Count",""+count);
                                    Cursor c = dataObj.getAttempt(dataObj.getLastTestAttempt(singletest.getTestid(),studentid));

                                    if (c.getCount() > 0) {
                                        c.moveToLast();
                                        if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                                            dataObj.DeleteAttempt(dataObj.getLastTestAttempt(singletest.getTestid(),studentid));
                                        }
                                    }
                                    try {
                                        Log.e("dataexec",getExternalPath(mycontext, singletest, "BASE"));
                                        fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "BASE") + testid + ".json"), "UTF-8");
                                        myparser = new JSONParser(fullTest,getExternalPath(mycontext, singletest, "ATTEMPT"),"PRACTICE", mycontext);
                                        attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                                        Log.e("FULLTEST:---","sample:--"+fullTest);
                                        Log.e("attempt:---","sample:--"+studentid);
                                        Intent i = new Intent(mycontext, PracticeTestActivity.class);
                                        i.putExtra("studentid",studentid);
                                        i.putExtra("json", attempt);
                                        i.putExtra("test", testid);
                                        i.putExtra("status", "NEW");
                                        mycontext.startActivity(i);
                                        ((ListofPractiseTests)mycontext).finish();
                                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }

                            else{

                            }

                        }else{
                            showAlert("Test Configuration is not Available for " + singletest.getTestid() + " \n Please download test data to fetch configuration ");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TestActivity1-----", e.toString());
                }

            }
        });

        holder.ll_fstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                missFileData.clear();
//                holder.ll_fstart.setBackground(mycontext.getResources().getDrawable(R.drawable.layout_press_custom));
                Cursor mycursor = myhelper.checkPractiseTest(studentid,singletest.getTestid());
                if (mycursor.getCount() > 0) {
                    while (mycursor.moveToNext()) {
//                        studentid=mycursor.getString(mycursor.getColumnIndex("sptu_student_ID"));
                        courseid = mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                        subjectId = mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                        paperid = mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                    }
                } else {
                    mycursor.close();
                }

                try {
                    String tPath = URLClass.mainpath+enrollid +"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/ENC/";

                    File file = new File(tPath +singletest.getTestid() + ".json");
                    if (!file.exists()) {
                        showAlert("Main JSON file for test " + singletest.getTestid() + " is not found! \n Please download test data if not ");
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(getTheDecriptedJson(tPath + singletest.getTestid() + ".json")));
//                        BufferedReader br = new BufferedReader(new FileReader(tPath + singletest.getTestid() + ".json"));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        filedata = sb.toString();
                        parseJson(filedata);
                        br.close();

                        int seccount=myhelper.getPtuSecCount(singletest.getTestid());
                        if(seccount>0){

                            if(downloadfileList.size()!=0){

                                ArrayList<String> missingfList = new ArrayList<>();

                                for(int i=0;i<chapterFileList.size();i++){

                                    SingleDWDQues sdq=chapterFileList.get(i);
                                    Log.e("FILES_EXIST",""+URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                                    File myFile1 = new File(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                                    if(myFile1.exists()){

                                    }else{
                                        Log.e("FILES_NOT_EXIST","**************************"+URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                                        missingfList.add(downloadfileList.get(i));
                                        missFileData.add(sdq);
                                    }
                                }

                                if(missingfList.size()!=0){
                                    StringBuilder sbm = new StringBuilder();
                                    sbm.append("The following files are missing...\n");
                                    for (int i = 0; i < missingfList.size(); i++) {
                                        sbm.append(missingfList.get(i) + " , " + "\n");
                                    }
                                    showReportAlert(sbm.toString(),singletest.getTestid());
                                }else{

                                    myparser = new JSONParser(filedata,tPath + "/flashAttempts/", "FLASH", mycontext);
                                    Intent i = new Intent(mycontext, FlashCardActivity.class);
                                    i.putExtra("studentid",studentid);
                                    i.putExtra("testId", testList.get(position).getTestid());
                                    i.putExtra("testPath", tPath);
                                    mycontext.startActivity(i);
                                    ((ListofPractiseTests)mycontext).finish();
                                }

                            }else{
                                Log.e("FlashCardActivity---", "No Questions Data for Test");
                            }


                        }else{
                            showAlert("Test Configuration is not Available for " + singletest.getTestid() + " \n Please download test data to fetch configuration ");
                        }
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
                i.putExtra("studentid", studentid);
                i.putExtra("enrollid",enrollid);
                mycontext.startActivity(i);
            }
        });

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalUrls.clear();
                finalNames.clear();
                filedata="";

                HashMap<String,String> hmap=new HashMap<>();
                hmap.put("studentid",studentid);
                hmap.put("enrollid",enrollid);
                hmap.put("testid",singletest.getTestid());
                hmap.put("status","STARTED");
                startdttm = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
                hmap.put("date",startdttm);
                new BagroundTask(finalUrl+"updatePTestStartStatus.php",hmap,mycontext,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            Log.e("PracticeTestAdapter","Respnse from server : "+json);
                            if(json.equalsIgnoreCase("Updated")){

                                long updateFlag=myhelper.updatePTestStartStatus(studentid,enrollid,singletest.getTestid(),"STARTED",startdttm);
                                if(updateFlag>0){
                                    Log.e("PracticeTestAdapter","Updated Locally");
                                }else{
                                    Log.e("PracticeTestAdapter","Unable to Update Locally");
                                }

                                Cursor mycursor=myhelper.checkPractiseTest(studentid,singletest.getTestid());
                                if(mycursor.getCount()>0){
                                    while(mycursor.moveToNext()){

                                        courseid=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                                        subjectId=mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                                        paperid=mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                                    }
                                }else{
                                    mycursor.close();
                                    Log.e("PracticeTestAdapter","Test Is Not Available in Table..");
                                }

                                path=courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/ENC/";

                                downloadjsonpath=finalAssetUrl+"courses/"+path+singletest.getTestid()+".json";

                                //tfiledwdpath=finalAssetUrl+"courses/"+path;

                                localpath=enrollid+"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/ENC/";

                                File myFile1 = new File(URLClass.mainpath+localpath+singletest.getTestid()+".json");
                                if(myFile1.exists()){
                                    Log.e("PracticeTestAdapter","Json File is Not Available, Please download it..");
                                }else{
                                    finalUrls.add(downloadjsonpath);
                                    finalNames.add(singletest.getTestid()+".json");
                                    localPathList.add(URLClass.mainpath+localpath);
                                }

                                if(finalUrls.size()!=0){
                                    new DownloadFileAsync(mycontext,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                                        @Override
                                        public void downloadStatus(String status) {

                                            try{
                                                if(status.equalsIgnoreCase("Completed")){
                                                    dwdImgFiles(singletest.getTestid());
                                                }else{
                                                    Toast.makeText(mycontext,"Unable to download Base json file",Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){

                                                e.printStackTrace();
                                                Log.e("PracticeTestAdapter",e.toString());
                                            }
                                        }
                                    }).execute();
                                }else{
                                    dwdImgFiles(singletest.getTestid());
                                }
                            }else{
                                Toast.makeText(mycontext,"Test Status is not updated...",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("PracticeTestAdapter",e.toString());
                        }

                    }
                }).execute();
            }
        });

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

    private void showAlert(String messege) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
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

    private void showReportAlert(String messege, final String testId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                })
                .setNegativeButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        JSONObject finalObj=JSONEncode(testId,missFileData);
                        HashMap<String,String> hmap=new HashMap<>();
                        hmap.put("MissingFiles",finalObj.toString());
                        try {
                            new BagroundTask(finalUrl+"insertmissingFileInfo.php",hmap,mycontext,new IBagroundListener() {
                                @Override
                                public void bagroundData(String json) {
                                    Log.d("ja", "comes:" + json);
                                    if (json.equals("Inserted")) {
                                        Toast.makeText(mycontext,"Report Submitted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mycontext,"failed report,try later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public void dwdImgFiles(final String testId){
        finalUrls.clear();
        finalNames.clear();
        localPathList.clear();

        filedata="";

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(getTheDecriptedJson(URLClass.mainpath+localpath+testId+".json")));
//            BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+localpath+testId+".json"));
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

        getTestConfig(testId,groupdata);

        if(downloadfileList.size()!=0){

            for(int i=0;i<chapterFileList.size();i++){

                SingleDWDQues sdq=chapterFileList.get(i);

                File myFile1 = new File(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                if(myFile1.exists()){

                }else{

                    String tPath=finalAssetUrl+"courses/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/";
                    finalUrls.add(tPath+sdq.getChapterId()+"/ENC/"+sdq.getFileName());
                    finalNames.add(sdq.getFileName());
                    localPathList.add(URLClass.mainpath+enrollid+"/"+courseid+"/"+sdq.getSubjectId()+"/"+sdq.getPaperId()+"/"+sdq.getChapterId()+"/ENC/");
                }
            }

        }else{
            updatePTestEndStatus(testId,"DOWNLOADED");
            Log.e("LearningActivity----","No Downloaded Images for test");
        }

        if(finalNames.size()!=0){

            new DownloadFileAsync(mycontext,localPathList,finalUrls,finalNames,new IDownloadStatus() {
                @Override
                public void downloadStatus(String status) {

                    try{
                        if(status.equalsIgnoreCase("Completed")){

                            updatePTestEndStatus(testId,"DOWNLOADED");

                        }else{

                        }

                    }catch (Exception e){

                        e.printStackTrace();
                        Log.e("DownloadFile----",e.toString());
                    }
                }
            }).execute();

        }else{

            updatePTestEndStatus(testId,"DOWNLOADED");
            Toast.makeText(mycontext,"All Downloaded",Toast.LENGTH_SHORT).show();

        }
    }

    private JSONObject JSONEncode(String testId,ArrayList<SingleDWDQues> finalList){
        JSONObject job=new JSONObject();
        JSONArray MissingList = new JSONArray();
        try{
            JSONObject MissingFile;
            for(int i=0;i<finalList.size();i++){

                SingleDWDQues singleDWDQues=finalList.get(i);

                MissingFile = new JSONObject();
                MissingFile.put("testId",testId);
                MissingFile.put("subjectId",singleDWDQues.getSubjectId());
                MissingFile.put("paperId",singleDWDQues.getPaperId());
                MissingFile.put("chapterId",singleDWDQues.getChapterId());
                MissingFile.put("fileName",singleDWDQues.getFileName());

                MissingList.put(MissingFile);
            }
            job.put("MissingFiles",MissingList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return job;
    }

    private String getExternalPath(Context context, SingleTest singletest, String type) {
        DBHelper dataObj = new DBHelper(context);
        testid = singletest.getTestid();
        Cursor cursor = dataObj.getSingleStudentTests(studentid,testid);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                enrollid = cursor.getString(cursor.getColumnIndex("sptu_entroll_id"));
                courseid = cursor.getString(cursor.getColumnIndex("sptu_course_id"));
                subjectId = cursor.getString(cursor.getColumnIndex("sptu_subjet_ID"));
                paperid = cursor.getString(cursor.getColumnIndex("sptu_paper_ID"));
            }
        }

        Log.e("path_vars", enrollid + " " + courseid + " " + subjectId + " " + paperid + " " + testid);
        path = enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + testid + "/ENC/";
        photoPath = URLClass.mainpath + path;
        attemptPath = URLClass.mainpath + path + "Attempt/";
        jsonPath = URLClass.mainpath + path;
        if (type.equals("BASE")) {
            return jsonPath;
        } else
            return attemptPath;
    }

    private void parseJson(String json){

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


                    if(!singlequesObj.getString("qbm_QAdditional_Image").equalsIgnoreCase("NULL")) {
                        if (downloadfileList.contains(singlequesObj.getString("qbm_QAdditional_Image"))) {

                        } else {
                            downloadfileList.add(singlequesObj.getString("qbm_QAdditional_Image"));
                            chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("qbm_QAdditional_Image")));
                        }
                    }

                    if(downloadfileList.contains(singlequesObj.getString("qbm_Review_Images"))){

                    }else{
                        downloadfileList.add(singlequesObj.getString("qbm_Review_Images"));
                        chapterFileList.add(new SingleDWDQues(chapterid  ,paperid,subid,singlequesObj.getString("qbm_Review_Images")));
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

    private void getTestConfig(final String testid,String groupidData){
        Log.e("PractiseTestAdapter","****** Welcome to test config ******");
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("testId",testid);
        hmap.put("groupiddata",groupidData);
        Log.e("PractiseTestAdapter","groupiddata:"+groupidData);
        new BagroundTask(finalUrl+"getTestConfig.php",hmap,mycontext,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {

                Log.e("PractiseTestAdapter","Resopnse from server:"+json);
                JSONArray groupArray,quesConfigArray,groupConfigArray,sectionArray;
                JSONObject groupObj=null,qconObj=null,gquesconObj=null,sectionObj=null;

                try{
                    Log.e("PractiseTestAdapter",json);

                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("qbgroup");

                    if (obj1 instanceof JSONArray)
                    {
//                        long Qgroupdelcount=myhelper.deleteTestGroups(testid);
//                        Log.e("groupdelcount---",""+Qgroupdelcount);
                        groupArray=myObj.getJSONArray("qbgroup");
                        if(groupArray!=null && groupArray.length()>0){
                            Log.e("groupLength---",""+groupArray.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<groupArray.length();i++){

                                groupObj=groupArray.getJSONObject(i);

                                Cursor mycursor=myhelper.checkQuesGroup(groupObj.getString("qbg_ID"));

                                if(mycursor.getCount()>0){
                                    long updateFlag=myhelper.updateQuesGroup(groupObj.getString("qbg_ID"),testid,groupObj.getString("qbg_media_type"),groupObj.getString("qbg_media_file"),
                                            groupObj.getString("qbg_text"),groupObj.getInt("qbg_no_questions"),Integer.parseInt(groupObj.getString("qbg_no_pick")),groupObj.getString("qbg_status"),
                                            groupObj.getString("qbg_created_by"),groupObj.getString("qbg_created_dttm"),groupObj.getString("qbg_mod_by"),groupObj.getString("qbg_mod_dttm"),groupObj.getString("qbg_type"));
                                            //Log.e("PractiseTestAdapter","No Of Pick"+groupObj.getString("qbg_no_pick"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertQuesGroup(groupObj.getInt("qbg_key"),groupObj.getString("qbg_ID"),testid,groupObj.getString("qbg_media_type"),groupObj.getString("qbg_media_file"),
                                            groupObj.getString("qbg_text"),groupObj.getInt("qbg_no_questions"),Integer.parseInt(groupObj.getString("qbg_no_pick")),groupObj.getString("qbg_status"),
                                            groupObj.getString("qbg_created_by"),groupObj.getString("qbg_created_dttm"),groupObj.getString("qbg_mod_by"),groupObj.getString("qbg_mod_dttm"),groupObj.getString("qbg_type"));
                                    //Log.e("PractiseTestAdapter","No Of Pick"+groupObj.getString("qbg_no_pick"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p+"  Updated:  "+r);
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
//                        long Qconfigcount=myhelper.deleteQuesConfig(testid);
//                        Log.e("QuesConDelCount---",""+Qconfigcount);
                        quesConfigArray=myObj.getJSONArray("ques_config");
                        if(quesConfigArray!=null && quesConfigArray.length()>0){
                            Log.e("QuesConLength---",""+quesConfigArray.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<quesConfigArray.length();i++){

                                qconObj=quesConfigArray.getJSONObject(i);

                                Cursor mycursor=myhelper.checkQuesConfig(qconObj.getString("testId"),qconObj.getString("subcategoryId"));

                                if(mycursor.getCount()>0){

                                    long updateFlag=myhelper.updateQuesConfig(qconObj.getString("courseId"),qconObj.getString("subjectId"),qconObj.getString("paperId"),
                                            qconObj.getString("testId"),qconObj.getString("categoryId"),qconObj.getString("subcategoryId"),qconObj.getInt("avail_count"),qconObj.getInt("pickup_count"),qconObj.getInt("min_pickup_count"),qconObj.getString("ques_configstatus"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }

                                }else{

                                    long insertFlag=myhelper.insertQuesConfig(qconObj.getInt("ques_configkey"),qconObj.getString("courseId"),qconObj.getString("subjectId"),qconObj.getString("paperId"),
                                            qconObj.getString("testId"),qconObj.getString("categoryId"),qconObj.getString("subcategoryId"),qconObj.getInt("avail_count"),qconObj.getInt("pickup_count"),qconObj.getInt("min_pickup_count"),qconObj.getString("ques_configstatus"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }

                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p+"  Updated:  "+r);
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
//                        long Gconfigdelcount=myhelper.deleteGroupsConfig(testid);
//                        Log.e("groupcondelcount---",""+Gconfigdelcount);
                        groupConfigArray=myObj.getJSONArray("groupques_config");
                        if(groupConfigArray!=null && groupConfigArray.length()>0){
                            Log.e("groupconLength---",""+groupConfigArray.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<groupConfigArray.length();i++){

                                gquesconObj=groupConfigArray.getJSONObject(i);

                                Cursor mycursor=myhelper.checkGroupConfig(gquesconObj.getString("testId"),gquesconObj.getString("sectionId"),gquesconObj.getString("groupType"));

                                if(mycursor.getCount()>0){
                                    long updateFlag=myhelper.updateGroupConfig(gquesconObj.getString("courseId"),gquesconObj.getString("subjectId"),gquesconObj.getString("paperId"),
                                            gquesconObj.getString("testId"),gquesconObj.getString("sectionId"),gquesconObj.getString("groupType"),gquesconObj.getInt("groupavail_count"),
                                            gquesconObj.getInt("grouppickup_count"),gquesconObj.getString("groupques_configstatus"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertGroupConfig(gquesconObj.getInt("groupques_configKey"),gquesconObj.getString("courseId"),gquesconObj.getString("subjectId"),gquesconObj.getString("paperId"),
                                            gquesconObj.getString("testId"),gquesconObj.getString("sectionId"),gquesconObj.getString("groupType"),gquesconObj.getInt("groupavail_count"),
                                            gquesconObj.getInt("grouppickup_count"),gquesconObj.getString("groupques_configstatus"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p+"  Updated:  "+r);
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
//                        long sectiondelcount=myhelper.deletePtuSections(testid);
//                        Log.e("sectiondelcount---",""+sectiondelcount);
                        sectionArray=myObj.getJSONArray("sections");
                        if(sectionArray!=null && sectionArray.length()>0){
                            Log.e("sectionLength---",""+sectionArray.length());
                            int p=0,q=0,r=0,s=0;
                            for(int i=0;i<sectionArray.length();i++){

                                sectionObj=sectionArray.getJSONObject(i);

                                Cursor mycursor=myhelper.checkPtuSection(sectionObj.getString("Ptu_ID"),sectionObj.getString("Ptu_section_ID"));

                                if(mycursor.getCount()>0){

                                    long updateFlag=myhelper.updatePtuSection(sectionObj.getString("Ptu_ID"),sectionObj.getInt("Ptu_section_sequence"),sectionObj.getString("Ptu_section_ID"),
                                            sectionObj.getString("Ptu_section_paper_ID"),sectionObj.getString("Ptu_section_subject_ID"),sectionObj.getString("Ptu_section_course_ID"),sectionObj.getInt("Ptu_section_min_questions"),
                                            sectionObj.getInt("Ptu_section_max_questions"),sectionObj.getInt("Ptu_sec_tot_groups"),sectionObj.getInt("Ptu_sec_no_groups"),sectionObj.getString("Ptu_section_status"),sectionObj.getString("Ptu_section_name"));
                                    if(updateFlag>0){
                                        r++;
                                    }else {
                                        s++;
                                    }
                                }else{

                                    long insertFlag=myhelper.insertPtuSection(sectionObj.getInt("Ptu_section_key"),sectionObj.getString("Ptu_ID"),sectionObj.getInt("Ptu_section_sequence"),sectionObj.getString("Ptu_section_ID"),
                                            sectionObj.getString("Ptu_section_paper_ID"),sectionObj.getString("Ptu_section_subject_ID"),sectionObj.getString("Ptu_section_course_ID"),sectionObj.getInt("Ptu_section_min_questions"),
                                            sectionObj.getInt("Ptu_section_max_questions"),sectionObj.getInt("Ptu_sec_tot_groups"),sectionObj.getInt("Ptu_sec_no_groups"),sectionObj.getString("Ptu_section_status"),sectionObj.getString("Ptu_section_name"));
                                    if(insertFlag>0){
                                        p++;
                                    }else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p+"  Updated:  "+r);
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

    public void updatePTestEndStatus(final String testId,final String status){
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("studentid",studentid);
        hmap.put("enrollid",enrollid);
        hmap.put("testid",testId);
        hmap.put("status",status);
        endddtm = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        hmap.put("date",endddtm);
        new BagroundTask(finalUrl+"updatePTestEndStatus.php",hmap,mycontext,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try {

                    Log.e("UploadStatus---",json);
                    if(json.equalsIgnoreCase("Updated")){
                        long updateFlag=myhelper.updatePTestEndStatus(studentid,enrollid,testId,status,endddtm);
                        if(updateFlag>0){
                            Log.e("LocalStatusUpdate---","Updated Locally");
                        }else{
                            Log.e("LocalStatusUpdate---","Unable to Update Locally");
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private InputStream getTheDecriptedJson(String json_file_path) {
        InputStream is=null;
        try {
            File f=new File(json_file_path);
            if(f.exists()) {
                is= EncryptDecrypt.decryptJson(new FileInputStream(f));
            }else
            {
                Log.e("PracticeTestAdapter","file is not found:"+json_file_path);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return is;
    }

}