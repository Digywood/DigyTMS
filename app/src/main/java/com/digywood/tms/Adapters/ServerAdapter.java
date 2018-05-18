package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Pojo.SingleDashPaper;
import com.digywood.tms.Pojo.SingleServer;
import com.digywood.tms.R;
import com.digywood.tms.TestDashActivity;

import java.util.ArrayList;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.MyViewHolder>{

    private List<SingleServer> serverList;
    Context mycontext;
    String testtype="";
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_servername;
        public CheckBox cb_selection;

        public MyViewHolder(View view) {
            super(view);

            tv_servername =view.findViewById(R.id.tv_servername);
            cb_selection =view.findViewById(R.id.cb_selection);

        }
    }


    public ServerAdapter(List<SingleServer> serverList, Context c) {
        this.serverList = serverList;
        this.mycontext=c;
    }

    public void updateList(List<SingleServer> list){
        serverList = list;
        notifyDataSetChanged();
    }

    public ServerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_serveritem, parent, false);
        return new ServerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ServerAdapter.MyViewHolder holder, final int position) {
        final SingleServer singleServer = serverList.get(position);

        holder.tv_servername.setText(""+singleServer.getServerName());

        holder.cb_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mycontext,"Clicked:--"+singleServer.getServerName(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return serverList.size();
    }

}
