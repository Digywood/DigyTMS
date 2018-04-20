package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.digywood.tms.FlashAttemptDataActivity;
import com.digywood.tms.FlashCardActivity;
import com.digywood.tms.JSONParser;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.R;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.ReviewActivity;
import com.digywood.tms.SaveJSONdataToFile;
import com.digywood.tms.TestActivity;
import com.digywood.tms.URLClass;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by prasa on 2018-02-27.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    ArrayList<SingleTest> testList;
    ArrayList<String> downloadedList = new ArrayList<>();
    ArrayList<String> downloadfileList = new ArrayList<>();
    ArrayList<String> groupIds = new ArrayList<>();
    ArrayList<String> chktestList = new ArrayList<>();
    ArrayList<String> fimageList = new ArrayList<>();
    Context mycontext;
    DBHelper myhelper;
    Boolean value = false;
    JSONParser myparser;
    String filedata = "", path, jsonPath, attemptPath, photoPath, enrollid, courseid, subjectId, paperid, testid,fullTest ,attempt ,json;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid, tv_teststatus;
        public Button btn_start, btn_resume,btn_fstart,btn_fattempthistory;
        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_testid);
            tv_teststatus = view.findViewById(R.id.tv_teststatus);
            btn_start = view.findViewById(R.id.btn_teststart);
            btn_resume = view.findViewById(R.id.btn_testresume);
            btn_fstart=view.findViewById(R.id.btn_fteststart);
            btn_fattempthistory=view.findViewById(R.id.btn_fattempthistory);
            cb_download = view.findViewById(R.id.cb_testselection);
        }
    }

    public TestAdapter(ArrayList<SingleTest> testList, Context c) {
        this.testList = testList;
        this.mycontext = c;
        myhelper=new DBHelper(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testitem, parent, false);
        return new TestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleTest singletest = testList.get(position);
        holder.tv_testid.setText(singletest.getTestid());
        holder.tv_teststatus.setText(singletest.getStatus());
        final DBHelper dataObj = new DBHelper(mycontext);
        if (dataObj.getQuestionCount() == 0) {
            holder.btn_resume.setEnabled(false);
        } else
            holder.btn_resume.setEnabled(true);
        int count = dataObj.getAttempCount() - 1;

        Cursor c = dataObj.getAttempt(count);
        //if cursor has values then the test is being resumed and data is retrieved from database
        if (c.getCount() > 0) {
            c.moveToLast();
            if (c.getInt(c.getColumnIndex("Attempt_Status")) == 1) {
                holder.btn_resume.setText("Resume");
                holder.btn_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext,singletest,"ATTEMPT")+ testid + ".json"), "UTF-8");
                            Log.e("Attempt_testadapter",attempt.toString());
                            Intent i = new Intent(mycontext, TestActivity.class);
                            i.putExtra("json", attempt);
                            i.putExtra("test",testid);
                            mycontext.startActivity(i);
                        } catch (IOException | ClassNotFoundException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                holder.btn_resume.setText("Review");
                testid = singletest.getTestid();
                holder.btn_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext,singletest,"ATTEMPT")+ testid + ".json"), "UTF-8");
                            Intent i = new Intent(mycontext, ReviewActivity.class);
                            i.putExtra("test",testid);
                            i.putExtra("json", attempt);
                            mycontext.startActivity(i);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObj.Destroy("attempt_data");
                dataObj.Destroy("attempt_list");
                /*testid = singletest.getTestid();
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
                attemptPath = URLClass.mainpath + path + "Attempt/" + testid + ".json";
                jsonPath = URLClass.mainpath + path + testid + ".json";*/
                try {
                    fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext,singletest,"BASE")+ testid + ".json"), "UTF-8");
                    JSONParser obj = new JSONParser(fullTest,getExternalPath(mycontext,singletest,"ATTEMPT"),"PRACTICE",mycontext);
                    attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext,singletest,"ATTEMPT")+ testid + ".json"), "UTF-8");
                    Intent i = new Intent(mycontext, TestActivity.class);
                    i.putExtra("json", attempt);
                    i.putExtra("test",testid);
                    mycontext.startActivity(i);
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
/*                if (value) {

                } else {
                    Toast.makeText(mycontext, "FILES NOT DOWNLOADED", Toast.LENGTH_LONG).show();
                }*/

            }
        });

        holder.btn_fstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor mycursor=myhelper.getSingleTestData(singletest.getTestid());
                if(mycursor.getCount()>0){
                    while(mycursor.moveToNext()){

//                        studentid=mycursor.getString(mycursor.getColumnIndex("sptu_student_ID"));
                        enrollid=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                        courseid=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                        subjectId=mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                        paperid=mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                    }
                }else{
                    mycursor.close();
                }

                try {
                    String tPath=URLClass.mainpath+enrollid+"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/";

                    File file = new File(tPath+singletest.getTestid()+".json");
                    if (!file.exists()) {
                        showAlert("Main JSON file for test "+singletest.getTestid()+" is not found! \n Please download test data if not ");
                    }else{
                        BufferedReader br = new BufferedReader(new FileReader(tPath+singletest.getTestid()+".json"));
//                    BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+enrollid+"/"+courseid+"/"+subjectId+"/"+paperid+"/"+singletest.getTestid()+"/"+singletest.getTestid()+"EAAA000009/SSCT1001/SSCS0002/PAA002/PTU0002/"+"PTU0002_01"+".json"));
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

                        myparser=new JSONParser(filedata,tPath+"/flashAttempts/","FLASH",mycontext);
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
                    i.putExtra("testId",testList.get(position).getTestid());
                    i.putExtra("testPath",tPath);
                    mycontext.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TestActivity1-----", e.toString());
                }
            }
        });

        holder.btn_fattempthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mycontext, FlashAttemptDataActivity.class);
                i.putExtra("testId",singletest.getTestid());
                mycontext.startActivity(i);
            }
        });

        holder.cb_download.setOnClickListener(new View.OnClickListener() {
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

    public String getExternalPath(Context context, SingleTest singletest,String type){
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
        jsonPath = URLClass.mainpath + path ;
        if(type.equals("BASE")){
            return jsonPath;
        }
        else
            return attemptPath;
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