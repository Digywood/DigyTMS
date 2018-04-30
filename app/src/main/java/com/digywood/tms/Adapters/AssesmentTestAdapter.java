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
        public Button btn_start, btn_resume,btn_fstart,btn_fattempthistory;
        public CheckBox cb_download;

        public MyViewHolder(View view) {
            super(view);
            tv_testid = view.findViewById(R.id.tv_atestid);
            tv_teststatus = view.findViewById(R.id.tv_ateststatus);
            btn_start = view.findViewById(R.id.btn_ateststart);
            btn_resume = view.findViewById(R.id.btn_atestresume);
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

        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}