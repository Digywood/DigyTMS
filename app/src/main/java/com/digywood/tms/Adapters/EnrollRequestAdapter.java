package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.digywood.tms.PaperActivity;
import com.digywood.tms.Pojo.SingleEnrollRequest;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-02-27.
 */

public class EnrollRequestAdapter extends  RecyclerView.Adapter<EnrollRequestAdapter.MyViewHolder>{

    private List<SingleEnrollRequest> enrollreqList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_enrollid,tv_batchid,tv_orgid,tv_coursename,tv_reqdate,tv_enddate,tv_amount,tv_indicator;

        public MyViewHolder(View view) {
            super(view);
            tv_enrollid =view.findViewById(R.id.tv_erenrollid);
            tv_batchid =view.findViewById(R.id.tv_erbatchid);
            tv_orgid =view.findViewById(R.id.tv_erorgnisationid);
            tv_coursename =view.findViewById(R.id.tv_ercoursename);
            tv_reqdate =view.findViewById(R.id.tv_erreqdate);
            tv_enddate =view.findViewById(R.id.tv_erenddate);
            tv_amount =view.findViewById(R.id.tv_eramount);
            tv_indicator=view.findViewById(R.id.tv_indicator);
        }
    }


    public EnrollRequestAdapter(List<SingleEnrollRequest> ereqList, Context c) {
        this.enrollreqList = ereqList;
        this.mycontext=c;
    }

    public void updateList(List<SingleEnrollRequest> list){
        enrollreqList = list;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_enrollreqitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleEnrollRequest singleEnrollRequest = enrollreqList.get(position);

        holder.tv_enrollid.setText(singleEnrollRequest.getEnrollId());
        holder.tv_batchid.setText(singleEnrollRequest.getBatchId());
        holder.tv_orgid.setText(singleEnrollRequest.getOrgId());
        holder.tv_coursename.setText(singleEnrollRequest.getCourseName());
        holder.tv_reqdate.setText(singleEnrollRequest.getRequestDate());
        holder.tv_enddate.setText(singleEnrollRequest.getEndDate());
        holder.tv_amount.setText(singleEnrollRequest.getAmount());
        if(singleEnrollRequest.getStatus().equalsIgnoreCase("ACTIVATED")){
            holder.tv_indicator.setBackgroundColor(mycontext.getResources().getColor(R.color.green));
        }else{
            holder.tv_indicator.setBackgroundColor(mycontext.getResources().getColor(R.color.dull_yellow));
        }

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return enrollreqList.size();
    }

}
