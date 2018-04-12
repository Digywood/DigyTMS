package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.digywood.tms.Pojo.SingleQuestionList;
import com.digywood.tms.R;
import java.util.ArrayList;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder>{

    private TextView[] myTextView;
    private int qid=-1;
    private int size;
    private ArrayList<SingleQuestionList> q_list=new ArrayList<>();
    private ArrayList<Integer> attempt =new ArrayList<>();
    private ArrayList<Integer> marked =new ArrayList<>();
    private Context mycontext;
    public int position = -1,index =  -1;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView Q_num;
        private ImageView  Q_pointer;
        private MyViewHolder(View view){
            super(view);
            Q_pointer = view.findViewById(R.id.img_pointer);
            Q_num=view.findViewById(R.id.tv_Qnumber);
            myTextView = new TextView[q_list.size()];
        }
    }

    public QuestionListAdapter(ArrayList<SingleQuestionList> q_list, Context c,int size){
        this.q_list=q_list;
        this.mycontext=c;
        this.size = size;

    }

    @Override
    public QuestionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_bar,parent,false);
        return new QuestionListAdapter.MyViewHolder(itemView);
    }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.Q_num.setText(q_list.get(position).getQ_num());
//            holder.Q_num.setBackgroundResource(setResource(size,q_list.get(position).getQ_status()));
            if(index == position){
                holder.Q_pointer.setVisibility(View.VISIBLE);
            }
            else
                holder.Q_pointer.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return q_list.size();
    }

    public void setData(int Qid){
        this.qid=Qid;
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<SingleQuestionList> q_list){
        this.q_list = q_list;
        notifyDataSetChanged();
    }

    public void setPoiner(int index){
        this.index = index;
        notifyDataSetChanged();
    }

    public int setResource(int size, String status){
        int resource = -1;
        if(size == Configuration.SCREENLAYOUT_SIZE_LARGE){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.drawable.number_background_large;
                                        break;
                case "ATTEMPTED": resource = R.drawable.number_confirm_large;
                                        break;
                case "SKIPPED": resource = R.drawable.number_skipped_large;
                                        break;
                case "BOOKMARKED": resource =R.drawable.number_bookmark_large;
                                        break;
                default: resource = R.drawable.number_background_large;
                                        break;
            }
        }
        else if(size == Configuration.SCREENLAYOUT_SIZE_NORMAL ){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.drawable.number_background;
                    break;
                case "ATTEMPTED": resource = R.drawable.number_confirm;
                    break;
                case "SKIPPED": resource = R.drawable.number_skipped;
                    break;
                case "BOOKMARKED": resource =R.drawable.number_bookmark;
                    break;
                default: resource = R.drawable.number_background_large;
                    break;
            }
        }
        return resource;
    }

}
