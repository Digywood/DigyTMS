package com.digywood.tms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digywood.tms.Pojo.LessonUnit;
import com.digywood.tms.Pojo.LessonUnitPoint;
import com.digywood.tms.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Lesson_Unit_Point_Adapter extends RecyclerView.Adapter<Lesson_Unit_Point_Adapter.MyViewHolder> {

    private ArrayList<LessonUnitPoint> al_lesson_unit_points;
    Context mycontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_lesson_unit_point_name,tv_lesson_unit_point_s_no;
        public ImageView iv_start_test;

        public MyViewHolder(View view) {
            super(view);
            tv_lesson_unit_point_name =view.findViewById(R.id.tv_lesson_unit_point_name);
            tv_lesson_unit_point_s_no =view.findViewById(R.id.tv_lesson_unit_point_s_no);
            iv_start_test =view.findViewById(R.id.iv_start_test);

        }
    }

    public Lesson_Unit_Point_Adapter(ArrayList<LessonUnitPoint> al_lesson_unit_points, Context c) {
        this.al_lesson_unit_points = al_lesson_unit_points;
        this.mycontext=c;
    }

    public void updateList(ArrayList<LessonUnitPoint> al_lesson_unit_points){
        this.al_lesson_unit_points = al_lesson_unit_points;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lession_unit_point_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LessonUnitPoint single_lession_unit_point = al_lesson_unit_points.get(position);
        holder.tv_lesson_unit_point_name.setText(single_lession_unit_point.getTms_lup_name());
        holder.tv_lesson_unit_point_s_no.setText(single_lession_unit_point.getTms_lup_seq_num()+".");
    }

    @Override
    public int getItemCount() {
        return al_lesson_unit_points.size();
    }
}
