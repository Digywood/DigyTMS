package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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
    Boolean value = false;
    JSONParser myparser;
    String filedata = "", path, jsonPath, attemptPath, photoPath, enrollid, courseid, subjectId, paperid, testid, attempt ,json;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid, tv_teststatus;
        public Button btn_start, btn_resume;
        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_testid);
            tv_teststatus = view.findViewById(R.id.tv_teststatus);
            btn_start = view.findViewById(R.id.btn_teststart);
            btn_resume = view.findViewById(R.id.btn_testresume);
            cb_download = view.findViewById(R.id.cb_testselection);
        }
    }

    public TestAdapter(ArrayList<SingleTest> testList, Context c) {
        this.testList = testList;
        this.mycontext = c;
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
                        Intent i = new Intent(mycontext, TestActivity.class);
                        mycontext.startActivity(i);
                    }
                });
            } else {
                holder.btn_resume.setText("Review");
                holder.btn_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mycontext, ReviewActivity.class);
                        mycontext.startActivity(i);
                    }
                });
            }
        }

        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObj.Destroy("attempt_data");
                dataObj.Destroy("attempt_list");
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
                attemptPath = URLClass.mainpath + path + "Attempt/" + testid + ".json";
                jsonPath = URLClass.mainpath + path + testid + ".json";
                try {
                    /*json = new String(SaveJSONdataToFile.bytesFromFile(jsonPath), "UTF-8");
                    Log.e("JSON_testadapter",json.toString());
                    JSONParser parser = new JSONParser(json,attemptPath);*/
                    attempt = new String(SaveJSONdataToFile.bytesFromFile(attemptPath), "UTF-8");
//                    Log.e("Attempt_testadapter",attempt.toString());
                    Intent i = new Intent(mycontext, TestActivity.class);
                    i.putExtra("json", attempt);
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

        holder.btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(mycontext, ReviewActivity.class);
                mycontext.startActivity(i);

            }});

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


    public ArrayList<String> readJson(String filedata) {
        ArrayList<String> flashimageList = new ArrayList();
        JSONObject mainObj, secObj, singlequesObj, optionsObj, additionsObj;
        JSONArray ja_sections, ja_questions, optionsArray, additionsArray;
        String testid = "", section = "", sectionid = "";

        try {
            mainObj = new JSONObject(filedata);

            testid = mainObj.getString("sptu_ID");

            Log.e("JSON--", mainObj.getString("sptu_ID"));

            ja_sections = mainObj.getJSONArray("Sections");

            Log.e("sectionArray Length---", "" + ja_sections.length());
            for (int i = 0; i < ja_sections.length(); i++) {

                secObj = ja_sections.getJSONObject(i);
                section = secObj.getString("Ptu_section_name");
                sectionid = secObj.getString("Ptu_section_ID");
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

                    optionsArray = singlequesObj.getJSONArray("options");
                    for (int j = 0; j < optionsArray.length(); j++) {

                        optionsObj = optionsArray.getJSONObject(j);
                        if (downloadfileList.contains(optionsObj.getString("qbo_media_file"))) {

                        } else {
                            value = false;
                            downloadfileList.add(optionsObj.getString("qbo_media_file"));
                        }
                    }

                    additionsArray = singlequesObj.getJSONArray("additions");
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