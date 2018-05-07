package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digywood.tms.Pojo.SingleDashPaper;
import com.digywood.tms.Pojo.SingleDashTest;
import com.digywood.tms.R;
import com.digywood.tms.TestDashActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TestDashAdapter extends RecyclerView.Adapter<TestDashAdapter.MyViewHolder>{

    private List<SingleDashTest> testdashList;
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_testid,tv_testname,tv_noofattempts,tv_lastdate;
        public TextView tv_lastscore,tv_uploaddttm,tv_smin,tv_smax,tv_savg;
        public TextView tv_omin,tv_omax,tv_oavg,tv_avgattempts,tv_maxattempts,tv_minattempts;

        public MyViewHolder(View view) {
            super(view);

//            tv_testid =view.findViewById(R.id.ftdash_testid);
            tv_testname =view.findViewById(R.id.ftdash_testname);
            tv_noofattempts =view.findViewById(R.id.ftdash_noofattempts);
            tv_lastdate =view.findViewById(R.id.ftdash_lastdate);
            tv_lastscore =view.findViewById(R.id.ftdash_lastattemptpercent);
            tv_uploaddttm =view.findViewById(R.id.ftdash_uploaddttm);
            tv_smin =view.findViewById(R.id.ftdash_minmarks);
            tv_smax =view.findViewById(R.id.ftdash_maxmarks);
            tv_savg =view.findViewById(R.id.ftdash_avgmarks);
            tv_omin =view.findViewById(R.id.ftdash_ominmarks);
            tv_omax =view.findViewById(R.id.ftdash_omaxmarks);
            tv_oavg =view.findViewById(R.id.ftdash_oavgmarks);
            tv_avgattempts =view.findViewById(R.id.ftdash_oavgattempts);
            tv_maxattempts =view.findViewById(R.id.ftdash_omaxattempts);
            tv_minattempts =view.findViewById(R.id.ftdash_ominattempts);

        }
    }

    public TestDashAdapter(List<SingleDashTest> paperdashList, Context c) {
        this.testdashList = paperdashList;
        this.mycontext=c;
    }

    public void updateList(List<SingleDashTest> list){
        testdashList = list;
        notifyDataSetChanged();
    }

    public TestDashAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testdashitem, parent, false);
        return new TestDashAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TestDashAdapter.MyViewHolder holder, final int position) {
        final SingleDashTest singleDashTest = testdashList.get(position);

        try{

            holder.tv_testname.setText(singleDashTest.getTestname());
            holder.tv_noofattempts.setText(""+singleDashTest.getNoofattempts());
            holder.tv_lastdate.setText(""+singleDashTest.getDttm());
            holder.tv_lastscore.setText(""+round(singleDashTest.getLatestpercenatage(),1)+" %");
            holder.tv_uploaddttm.setText(singleDashTest.getUploaddttm());

            if(singleDashTest.getSmin()>singleDashTest.getOmin()){
                holder.tv_smin.setText(" "+round(singleDashTest.getSmin(),1));
                holder.tv_smin.setCompoundDrawablesWithIntrinsicBounds( R.drawable.upclrarrow, 0, 0, 0);
            }else{
                holder.tv_smin.setText(" "+round(singleDashTest.getSmin(),1));
                holder.tv_smin.setCompoundDrawablesWithIntrinsicBounds( R.drawable.downclrarrow, 0, 0, 0);
            }

            if(singleDashTest.getSman()>singleDashTest.getOmax()){
                holder.tv_smax.setText(" "+round(singleDashTest.getSman(),1));
                holder.tv_smax.setCompoundDrawablesWithIntrinsicBounds( R.drawable.upclrarrow, 0, 0, 0);
            }else{
                holder.tv_smax.setText(" "+round(singleDashTest.getSman(),1));
                holder.tv_smax.setCompoundDrawablesWithIntrinsicBounds( R.drawable.downclrarrow, 0, 0, 0);
            }

            if(singleDashTest.getSavg()>singleDashTest.getOavg()){
                holder.tv_savg.setText(" "+round(singleDashTest.getSavg(),1));
                holder.tv_savg.setCompoundDrawablesWithIntrinsicBounds( R.drawable.upclrarrow, 0, 0, 0);
            }else{
                holder.tv_savg.setText(" "+round(singleDashTest.getSavg(),1));
                holder.tv_savg.setCompoundDrawablesWithIntrinsicBounds( R.drawable.downclrarrow, 0, 0, 0);
            }


            holder.tv_omin.setText(" "+round(singleDashTest.getOmin(),1));
            holder.tv_omax.setText(" "+round(singleDashTest.getOmax(),1));
            holder.tv_oavg.setText(" "+round(singleDashTest.getOavg(),1));
            holder.tv_avgattempts.setText(""+singleDashTest.getAvgattempts());
            holder.tv_maxattempts.setText(""+singleDashTest.getMaxattempts());
            holder.tv_minattempts.setText(""+singleDashTest.getMinattempts());

        }catch (Exception e){
            e.printStackTrace();
            Log.e("TestDashAdp:---",e.toString());
        }

//        holder.btn_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i=new Intent(mycontext, TestDashActivity.class);
//                i.putExtra("paperid",singleDashPaper.getPaperid());
//                i.putExtra("testtype","FLASH");
//                mycontext.startActivity(i);
//            }
//        });

    }

    public int getItemCount() {
        return testdashList.size();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
