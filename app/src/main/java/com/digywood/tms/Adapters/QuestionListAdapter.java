package com.digywood.tms.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.digywood.tms.R;
import java.util.ArrayList;

/**
 * Created by Shashank on 09-03-2018.
 */

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder>{

    TextView[] myTextView;
    int qid=-1;
    ArrayList<String> q_list=new ArrayList<>();
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Q_num;
        public MyViewHolder(View view){
            super(view);
            Q_num=view.findViewById(R.id.Qnumber);
            myTextView = new TextView[q_list.size()];
        }
    }

    public QuestionListAdapter(ArrayList<String > q_list,Context c){
        this.q_list=q_list;
        this.mycontext=c;

    }

    @Override
    public QuestionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_bar,parent,false);
        return new QuestionListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(qid==-1){
            if(position==0){
                holder.Q_num.setText(q_list.get(position));
                holder.Q_num.setBackgroundResource(R.drawable.marker);
            }else{
                holder.Q_num.setText(q_list.get(position));
                holder.Q_num.setBackgroundResource(0);
            }
        }else{
            int qnum=Integer.valueOf(q_list.get(position));
            if(qnum==qid){
                holder.Q_num.setText(q_list.get(position));
                holder.Q_num.setBackgroundResource(R.drawable.marker);
            }else{
                holder.Q_num.setText(q_list.get(position));
                holder.Q_num.setBackgroundResource(0);
            }
        }

        holder.Q_num.setText(q_list.get(position));
//        holder.Q_num.setBackgroundResource(0);
        holder.Q_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Q_num.setBackgroundResource(R.drawable.marker);
            }
        });
//        holder.Q_num.setBackgroundResource(R.drawable.marker);
    }

    @Override
    public int getItemCount() {
        return q_list.size();
    }

    public void setData(int Qid){
        this.qid=Qid;
        notifyDataSetChanged();
    }

    public int getTvCount() {
        return myTextView.length;
    }

    public void setBackground(int position ){
//        MyViewHolder holder = new MyViewHolder(view);
        myTextView  = new TextView[q_list.size()];
        myTextView[position].setBackgroundResource(R.drawable.marker);
    }
}
