package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.digywood.tms.JSONParser;

import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.R;
import com.digywood.tms.Pojo.SingleTest;
import com.digywood.tms.ReviewActivity;
import com.digywood.tms.TestActivity;
import com.digywood.tms.URLClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by prasa on 2018-02-27.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    ArrayList<SingleTest> testList;
    ArrayList<String> downloadedList=new ArrayList<>();
    ArrayList<String> chktestList=new ArrayList<>();
    Context mycontext;
    JSONParser myparser;
    String filedata="";

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_testid,tv_teststatus;
        public Button btn_start,btn_resume;
        public CheckBox cb_download;

        public MyViewHolder(View view){
            super(view);
            tv_testid=view.findViewById(R.id.tv_testid);
            tv_teststatus=view.findViewById(R.id.tv_teststatus);
            btn_start=view.findViewById(R.id.btn_teststart);
            btn_resume=view.findViewById(R.id.btn_testresume);
            cb_download=view.findViewById(R.id.cb_testselection);
        }
    }

    public TestAdapter(ArrayList<SingleTest> testList,Context c){
        this.testList=testList;
        this.mycontext=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testitem, parent,false);
        return new TestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleTest singletest = testList.get(position);
        holder.tv_testid.setText(singletest.getTestid());
        holder.tv_teststatus.setText(singletest.getStatus());

        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(mycontext, TestActivity.class);
                i.putExtra("TestId",testList.get(position).getTestid());
                mycontext.startActivity(i);

            }
        });

        holder.btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DBHelper dataObj = new DBHelper(mycontext);
                Intent i=new Intent(mycontext, ReviewActivity.class);
                mycontext.startActivity(i);


//                try{
//                    BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+"PTAA00002"+".json"));
//                    StringBuilder sb = new StringBuilder();
//                    String line = br.readLine();
//
//                    while (line != null) {
//                        sb.append(line);
//                        sb.append("\n");
//                        line = br.readLine();
//                    }
//                    filedata=sb.toString();
//                    br.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.e("TestActivity1-----",e.toString());
//                }
//
//                myparser=new JSONParser();
//                myparser.JSONParser(filedata);

            }
        });

        holder.cb_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.cb_download.isChecked()){
                    if(singletest.getStatus().equalsIgnoreCase("DOWNLOADED")){
                        downloadedList.add(singletest.getTestid());
                    }else{

                    }

                    if(chktestList.size()!=0){
                        if(chktestList.contains(singletest.getTestid())){

                        }else{
                            chktestList.add(singletest.getTestid());
                        }
                    }else{
                        chktestList.add(singletest.getTestid());
                    }

                }else{

                    if(downloadedList.contains(singletest.getTestid())){
                        downloadedList.remove(singletest.getTestid());
                    }

                    chktestList.remove(singletest.getTestid());
                }

            }
        });

    }

    public void updateTests(ArrayList<SingleTest> list){
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
}
