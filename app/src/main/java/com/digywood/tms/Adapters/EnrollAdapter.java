package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.digywood.tms.CourseActivity;
import com.digywood.tms.R;
import com.digywood.tms.Pojo.SingleEnrollment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-02-27.
 */

public class EnrollAdapter extends  RecyclerView.Adapter<EnrollAdapter.MyViewHolder>{

    private List<SingleEnrollment> enrollList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_enrollid,tv_enrollcourseid;
        public Button btn_start;

        public MyViewHolder(View view) {
            super(view);
            tv_enrollid =view.findViewById(R.id.tv_enrollid);
            tv_enrollcourseid =view.findViewById(R.id.tv_enrollcourseid);
            btn_start=view.findViewById(R.id.btn_start);
        }
    }


    public EnrollAdapter(List<SingleEnrollment> surveyList, Context c) {
        this.enrollList = surveyList;
        this.mycontext=c;
    }

    public void updateList(List<SingleEnrollment> list){
        enrollList = list;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_enrollitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleEnrollment singleenroll = enrollList.get(position);

        holder.tv_enrollid.setText(singleenroll.getEnrollid());
        holder.tv_enrollcourseid.setText(singleenroll.getEnrollcourseid());

        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mycontext,CourseActivity.class);
                i.putExtra("enrollid",singleenroll.getEnrollid());
                i.putExtra("courseid",singleenroll.getEnrollcourseid());
                mycontext.startActivity(i);
            }
        });


    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return enrollList.size();
    }

}
