package com.digywood.tms.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digywood.tms.Pojo.SingleBatchdata;
import com.digywood.tms.R;

import java.util.ArrayList;

/**
 * Created by prasa on 2018-03-07.
 */

public class DataAdaper extends RecyclerView.Adapter<DataAdaper.MyViewHolder>{

    ArrayList<SingleBatchdata> batchList;
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_name,tv_number,tv_email;

        public MyViewHolder(View view){
            super(view);
            tv_name=view.findViewById(R.id.tv_batchuser);
            tv_number=view.findViewById(R.id.tv_batchusernumber);
            tv_email=view.findViewById(R.id.tv_batchuseremail);

        }

    }

    public DataAdaper(ArrayList<SingleBatchdata> batchList, Context c){
        this.batchList=batchList;
        this.mycontext=c;
    }

    @Override
    public DataAdaper.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_batchdataitem, parent, false);
        return new DataAdaper.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataAdaper.MyViewHolder holder, final int position) {
        final SingleBatchdata singletest = batchList.get(position);
        holder.tv_name.setText(singletest.getName());
        holder.tv_number.setText(singletest.getNumber());
        holder.tv_email.setText(singletest.getEmail());

    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

}
