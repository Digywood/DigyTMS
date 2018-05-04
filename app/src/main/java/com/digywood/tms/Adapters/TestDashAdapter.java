package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class TestDashAdapter extends RecyclerView.Adapter<TestDashAdapter.MyViewHolder>{

    private List<SingleDashTest> testdashList;
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_papername,tv_totaltests,tv_attempttests,tv_max,tv_min;
        public TextView tv_papername1,tv_totaltests1,tv_attempttests1,tv_max1,tv_min1;
        public TextView tv_papername2,tv_totaltests2,tv_attempttests2,tv_max2,tv_min2;
        public Button btn_details;

        public MyViewHolder(View view) {
            super(view);

//            tv_papername =view.findViewById(R.id.fpdash_papername);
//            tv_totaltests =view.findViewById(R.id.fpdash_tottestcount);
//            tv_attempttests =view.findViewById(R.id.fpdash_attemptcount);
//            tv_max =view.findViewById(R.id.fpdash_max);
//            tv_min =view.findViewById(R.id.fpdash_min);
//            tv_avg =view.findViewById(R.id.fpdash_avg);
//            tv_papername =view.findViewById(R.id.fpdash_papername);
//            tv_totaltests =view.findViewById(R.id.fpdash_tottestcount);
//            tv_attempttests =view.findViewById(R.id.fpdash_attemptcount);
//            tv_max =view.findViewById(R.id.fpdash_max);
//            tv_min =view.findViewById(R.id.fpdash_min);
//            tv_papername =view.findViewById(R.id.fpdash_papername);
//            tv_totaltests =view.findViewById(R.id.fpdash_tottestcount);
//            tv_attempttests =view.findViewById(R.id.fpdash_attemptcount);
//            tv_max =view.findViewById(R.id.fpdash_max);

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_paperdashitem, parent, false);
        return new TestDashAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TestDashAdapter.MyViewHolder holder, final int position) {
        final SingleDashTest singleDashTest = testdashList.get(position);

//        holder.tv_papername.setText(singleDashPaper.getPapername());
//        holder.tv_totaltests.setText(""+singleDashPaper.getTotaltests());
//        holder.tv_attempttests.setText(""+singleDashPaper.getAttemptedtests());
//        holder.tv_max.setText(""+round(singleDashPaper.getMax(),1));
//        holder.tv_min.setText(""+round(singleDashPaper.getMin(),1));
//        holder.tv_avg.setText(""+round(singleDashPaper.getAvg(),1));
//        holder.tv_papername.setText(singleDashPaper.getPapername());
//        holder.tv_totaltests.setText(""+singleDashPaper.getTotaltests());
//        holder.tv_attempttests.setText(""+singleDashPaper.getAttemptedtests());
//        holder.tv_max.setText(""+round(singleDashPaper.getMax(),1));
//        holder.tv_min.setText(""+round(singleDashPaper.getMin(),1));
//        holder.tv_avg.setText(""+round(singleDashPaper.getAvg(),1));
//        holder.tv_max.setText(""+round(singleDashPaper.getMax(),1));
//        holder.tv_min.setText(""+round(singleDashPaper.getMin(),1));
//        holder.tv_avg.setText(""+round(singleDashPaper.getAvg(),1));

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
