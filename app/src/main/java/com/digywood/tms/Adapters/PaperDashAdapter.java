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
import com.digywood.tms.R;
import com.digywood.tms.TestDashActivity;

import java.util.ArrayList;
import java.util.List;

public class PaperDashAdapter extends RecyclerView.Adapter<PaperDashAdapter.MyViewHolder>{

    private List<SingleDashPaper> paperdashList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_papername,tv_totaltests,tv_attempttests,tv_max,tv_min,tv_avg;
        public Button btn_details;

        public MyViewHolder(View view) {
            super(view);

            tv_papername =view.findViewById(R.id.fpdash_papername);
            tv_totaltests =view.findViewById(R.id.fpdash_tottestcount);
            tv_attempttests =view.findViewById(R.id.fpdash_attemptcount);
            tv_max =view.findViewById(R.id.fpdash_max);
            tv_min =view.findViewById(R.id.fpdash_min);
            tv_avg =view.findViewById(R.id.fpdash_avg);
            btn_details=view.findViewById(R.id.fpdash_det);

        }
    }


    public PaperDashAdapter(List<SingleDashPaper> paperdashList, Context c) {
        this.paperdashList = paperdashList;
        this.mycontext=c;
    }

    public void updateList(List<SingleDashPaper> list){
        paperdashList = list;
        notifyDataSetChanged();
    }

    public PaperDashAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_paperdashitem, parent, false);
        return new PaperDashAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaperDashAdapter.MyViewHolder holder, final int position) {
        final SingleDashPaper singleDashPaper = paperdashList.get(position);

        holder.tv_papername.setText("Name: "+singleDashPaper.getPapername());
        holder.tv_totaltests.setText("Total: "+singleDashPaper.getTotaltests());
        holder.tv_attempttests.setText("Attempted: "+singleDashPaper.getAttemptedtests());
        holder.tv_max.setText("Max: "+round(singleDashPaper.getMax(),1));
        holder.tv_min.setText("Min: "+round(singleDashPaper.getMin(),1));
        holder.tv_avg.setText("Avg: "+round(singleDashPaper.getAvg(),1));

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(mycontext, TestDashActivity.class);
                i.putExtra("paperid",singleDashPaper.getPaperid());
                i.putExtra("testtype","FLASH");
                mycontext.startActivity(i);
            }
        });

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return paperdashList.size();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
