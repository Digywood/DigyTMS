package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.FlashCardActivity;
import com.digywood.tms.JSONParser;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.R;
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

public class AssesmentTestAdapter extends RecyclerView.Adapter<AssesmentTestAdapter.MyViewHolder> {

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
        public ImageView iv_start, iv_resume,btn_fstart,btn_fattempthistory;
        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_atestid);
            tv_teststatus = view.findViewById(R.id.tv_ateststatus);
            iv_start = view.findViewById(R.id.iv_start);
            iv_resume = view.findViewById(R.id.iv_resume);
            cb_download = view.findViewById(R.id.cb_atestselection);
        }
    }

    public AssesmentTestAdapter(ArrayList<SingleTest> testList, Context c) {
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
        final SingleTest singletest = testList.get(position);
        holder.tv_testid.setText(singletest.getTestid());
        holder.tv_teststatus.setText(singletest.getStatus());

        holder.iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(mycontext);
                TextView msg = new TextView(mycontext);
                msg.setText("Central");
                msg.setGravity(Gravity.CENTER_HORIZONTAL);
                EditText key = new EditText(mycontext);
                key.setGravity(Gravity.CENTER_HORIZONTAL);
                Button enter = new Button(mycontext);
                enter.setGravity(Gravity.CENTER_HORIZONTAL);
                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myhelper.Destroy("attempt_data");
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

                            fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "BASE") + testid + ".json"), "UTF-8");
                            JSONParser obj = new JSONParser(fullTest, getExternalPath(mycontext, singletest, "ATTEMPT"), "PRACTICE", mycontext);
                            attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, singletest, "ATTEMPT") + testid + ".json"), "UTF-8");
                            Intent i = new Intent(mycontext, TestActivity.class);
                            i.putExtra("json", attempt);
                            i.putExtra("test", testid);
                            i.putExtra("status", "NEW");
                            mycontext.startActivity(i);
                        } catch (IOException | ClassNotFoundException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
                popupBuilder.setView(msg);
            }
        });
    }

      /*  holder.cb_download.setOnClickListener(new View.OnClickListener() {
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
*/
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
}