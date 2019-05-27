package com.digywood.tms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digywood.tms.Pojo.Lesson;
import com.digywood.tms.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder> {

    private ArrayList<Lesson> al_lessons;
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_lesson_name,tv_lesson_s_no;
        public ImageView iv_start_test;

        public MyViewHolder(View view) {
            super(view);
            tv_lesson_name =view.findViewById(R.id.tv_lesson_name);
            tv_lesson_s_no =view.findViewById(R.id.tv_lesson_s_no);
            iv_start_test =view.findViewById(R.id.iv_start_test);

        }
    }

    public LessonAdapter(ArrayList<Lesson> al_lessons,Context c) {
        this.al_lessons = al_lessons;
        this.mycontext=c;
    }

    public void updateList(ArrayList<Lesson> al_lessons){
        this.al_lessons = al_lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lession_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Lesson single_lession = al_lessons.get(position);
        holder.tv_lesson_name.setText(single_lession.getTms_lms_lesson_long_name());
        holder.tv_lesson_s_no.setText(single_lession.getTms_lms_lesson_seq_number()+" .");
    }

    @Override
    public int getItemCount() {
        return al_lessons.size();
    }
}
