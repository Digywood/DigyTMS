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
    String testtype="",studentid="";
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_papername,tv_totaltests,tv_attempttests,tv_max,tv_min,tv_avg,tv_bmax,tv_bmin,tv_bavg;
        public Button btn_details;

        public MyViewHolder(View view) {
            super(view);

            tv_papername =view.findViewById(R.id.fpdash_papername);
            tv_totaltests =view.findViewById(R.id.fpdash_tottestcount);
            tv_attempttests =view.findViewById(R.id.fpdash_attemptcount);
            tv_max =view.findViewById(R.id.fpdash_max);
            tv_min =view.findViewById(R.id.fpdash_min);
            tv_avg =view.findViewById(R.id.fpdash_avg);
            tv_bmax =view.findViewById(R.id.fpdash_bmax);
            tv_bmin =view.findViewById(R.id.fpdash_bmin);
            tv_bavg =view.findViewById(R.id.fpdash_bavg);
            btn_details=view.findViewById(R.id.fpdash_det);

        }
    }


    public PaperDashAdapter(List<SingleDashPaper> paperdashList, Context c,String studentId,String testtype) {
        this.paperdashList = paperdashList;
        this.mycontext=c;
        this.studentid=studentId;
        this.testtype=testtype;
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

        holder.tv_papername.setText(""+singleDashPaper.getPapername());
        holder.tv_totaltests.setText(""+singleDashPaper.getTotaltests());
        holder.tv_attempttests.setText(""+singleDashPaper.getAttemptedtests());

        holder.tv_min.setText(" "+round(singleDashPaper.getMin(),1));
        holder.tv_max.setText(" "+round(singleDashPaper.getMax(),1));

        if(singleDashPaper.getAvg()>singleDashPaper.getBavg()){
            holder.tv_avg.setText(" "+round(singleDashPaper.getAvg(),1));
            holder.tv_avg.setCompoundDrawablesWithIntrinsicBounds( R.drawable.upclrarrow, 0, 0, 0);
        }else{
            holder.tv_avg.setText(" "+round(singleDashPaper.getAvg(),1));
            holder.tv_avg.setCompoundDrawablesWithIntrinsicBounds( R.drawable.downclrarrow, 0, 0, 0);
        }

        holder.tv_bmax.setText(""+round(singleDashPaper.getBmax(),1));
        holder.tv_bmin.setText(""+round(singleDashPaper.getBmin(),1));
        holder.tv_bavg.setText(""+round(singleDashPaper.getBavg(),1));

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(mycontext, TestDashActivity.class);
                i.putExtra("studentid",studentid);
                i.putExtra("paperid",singleDashPaper.getPaperid());
                i.putExtra("testtype",testtype);
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
