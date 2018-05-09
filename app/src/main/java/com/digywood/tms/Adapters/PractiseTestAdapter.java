package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.FlashCardActivity;
import com.digywood.tms.JSONParser;
import com.digywood.tms.DBHelper.DBHelper;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-02-27.
 */

public class PractiseTestAdapter extends RecyclerView.Adapter<PractiseTestAdapter.MyViewHolder> implements
        OnChartValueSelectedListener {

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
    String filedata = "", path, jsonPath, attemptPath, photoPath, enrollid, courseid, subjectId, paperid, testid, fullTest, attempt, json;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_testid, tv_teststatus,tv_testAttempt,tv_min,tv_max;
        public Button btn_pstart, btn_review, btn_fstart;
        ImageView iv_history;
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
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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

/*                if (value) {

                } else {
                    Toast.makeText(mycontext, "FILES NOT DOWNLOADED", Toast.LENGTH_LONG).show();
                }*/

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
                    String tPath = URLClass.mainpath + enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + singletest.getTestid() + "/";

                    File file = new File(tPath + singletest.getTestid() + ".json");
                    if (!file.exists()) {
                        showAlert("Main JSON file for test " + singletest.getTestid() + " is not found! \n Please download test data if not ");
                    } else {
                        BufferedReader br = new BufferedReader(new FileReader(tPath + singletest.getTestid() + ".json"));
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

                        myparser = new JSONParser(filedata, tPath + "/flashAttempts/", "FLASH", mycontext);


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
//                            -Intent i = new Intent(mycontext, FlashCardActivity.class);
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