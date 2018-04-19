package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.digywood.tms.Pojo.SingleFlashAttempt;
import com.digywood.tms.R;
import java.util.ArrayList;
import java.util.List;

public class FlashAttemptAdapter extends RecyclerView.Adapter<FlashAttemptAdapter.MyViewHolder>{

    private List<SingleFlashAttempt> fattemptList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_fdate,tv_fknowcount,tv_fdonknowcount,tv_fskipcount,tv_fpercentage;
        public ProgressBar pb_progress;

        public MyViewHolder(View view) {
            super(view);
            tv_fdate =view.findViewById(R.id.tv_fdate);
            tv_fknowcount =view.findViewById(R.id.tv_fknowcount);
            tv_fdonknowcount =view.findViewById(R.id.tv_fdonknowcount);
            tv_fskipcount =view.findViewById(R.id.tv_fskipcount);
            tv_fpercentage =view.findViewById(R.id.tv_fpercentage);
            pb_progress =view.findViewById(R.id.pb_progress);
        }
    }


    public FlashAttemptAdapter(List<SingleFlashAttempt> attemptList, Context c) {
        this.fattemptList = attemptList;
        this.mycontext=c;
    }

    public void updateList(List<SingleFlashAttempt> list){
        fattemptList = list;
        notifyDataSetChanged();
    }

    public FlashAttemptAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_flashattemptitem, parent, false);
        return new FlashAttemptAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FlashAttemptAdapter.MyViewHolder holder, final int position) {
        final SingleFlashAttempt singlefattempt = fattemptList.get(position);

        holder.tv_fdate.setText(singlefattempt.getDate());
        holder.tv_fknowcount.setText(String.valueOf(singlefattempt.getKnowcount()));
        holder.tv_fdonknowcount.setText(String.valueOf(singlefattempt.getDonknowcount()));
        holder.tv_fskipcount.setText(String.valueOf(singlefattempt.getSkipcount()));
        holder.tv_fpercentage.setText(String.valueOf(singlefattempt.getPercent()));
        Double d = new Double(singlefattempt.getPercent());
        int progress = d.intValue();
        holder.pb_progress.setProgress(progress);

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return fattemptList.size();
    }

}
