package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digywood.tms.Pojo.SingleFlashAttempt;
import com.digywood.tms.Pojo.SingleTestAttempt;
import com.digywood.tms.R;

import java.util.ArrayList;
import java.util.List;

public class TestAttemptAdapter extends RecyclerView.Adapter<TestAttemptAdapter.MyViewHolder>{

    private List<SingleTestAttempt> tattemptList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_attempt_Id,tv_attemptAnsweredCount,tv_attemptTestSkipcount,tv_attemptTestMarkcount,tv_attemptTestNotAnscount,tv_attemptTestScore,tv_tpercentage;
        public ProgressBar pb_progress;

        public MyViewHolder(View view) {
            super(view);
            tv_attempt_Id =view.findViewById(R.id.tv_attempt_Id);
            tv_attemptAnsweredCount =view.findViewById(R.id.tv_attemptAnsweredCount);
            tv_attemptTestSkipcount =view.findViewById(R.id.tv_attemptTestSkipcount);
            tv_attemptTestMarkcount =view.findViewById(R.id.tv_attemptTestMarkcount);
            tv_attemptTestNotAnscount =view.findViewById(R.id.tv_attemptTestNotAnscount);
            tv_attemptTestScore =view.findViewById(R.id.tv_attemptTestScore);
            tv_tpercentage =view.findViewById(R.id.tv_tpercentage);
            pb_progress =view.findViewById(R.id.pb_progress);
        }
    }


    public TestAttemptAdapter(List<SingleTestAttempt> attemptList, Context c) {
        this.tattemptList = attemptList;
        this.mycontext=c;
    }

    public void updateList(List<SingleTestAttempt> list){
        tattemptList = list;
        notifyDataSetChanged();
    }

    public TestAttemptAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_testattemptitem, parent, false);
        return new TestAttemptAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TestAttemptAdapter.MyViewHolder holder, final int position) {
        final SingleTestAttempt singletattempt = tattemptList.get(position);

        holder.tv_attempt_Id.setText(String.valueOf(singletattempt.getAttemptcount()));
        holder.tv_attemptAnsweredCount.setText(String.valueOf(singletattempt.getSkipcount()));
        holder.tv_attemptTestSkipcount.setText(String.valueOf(singletattempt.getMarkcount()));
        holder.tv_attemptTestMarkcount.setText(String.valueOf(singletattempt.getNotattemptcount()));
        holder.tv_attemptTestNotAnscount.setText(String.valueOf(singletattempt.getScore()));
        holder.tv_attemptTestScore.setText(String.valueOf(singletattempt.getScore()));
        holder.tv_tpercentage.setText(String.valueOf(singletattempt.getPercent()));
        Double d = new Double(singletattempt.getPercent());
        int progress = d.intValue();
        Resources res=mycontext.getResources();

        if(progress>70){
            holder.pb_progress.setProgressDrawable(res.getDrawable(R.drawable.progress_max_color));
        }else if(progress<70 && progress>30){
            holder.pb_progress.setProgressDrawable(res.getDrawable(R.drawable.progress_mid_color));
        }else if(progress<30 && progress!=0){
            holder.pb_progress.setProgressDrawable(res.getDrawable(R.drawable.progress_low_color));
        }else if(progress==0){
            progress=100;
            holder.pb_progress.setProgressDrawable(res.getDrawable(R.drawable.progress_low_color));
        }

        holder.pb_progress.setProgress(progress);

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return tattemptList.size();
    }

}
