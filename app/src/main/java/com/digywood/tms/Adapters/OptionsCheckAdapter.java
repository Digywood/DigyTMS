package com.digywood.tms.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.R;

import java.util.ArrayList;
import java.util.List;

public class OptionsCheckAdapter extends RecyclerView.Adapter<OptionsCheckAdapter.MyViewHolder>{

    private ArrayList<SingleOptions> optionsList;
    Context mycontext;
    String path;
    public int mSelectedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout option_layout;
        public ImageView iv_opmedia;
        public RadioButton rb_option;

        public MyViewHolder(View view) {
            super(view);
            iv_opmedia = view.findViewById(R.id.iv_opmedia);
            rb_option = view.findViewById(R.id.rb_option);
            option_layout = view.findViewById(R.id.option_layout);

            option_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,optionsList.size());
                }
            });
            rb_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0,optionsList.size());
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public OptionsCheckAdapter(ArrayList<SingleOptions> optionsListList, Context c, String path) {
        this.optionsList = optionsListList;
        this.mycontext=c;
        this.path = path;
    }


    public void updateList(ArrayList<SingleOptions> list){
        optionsList = list;
        notifyDataSetChanged();
    }

    public OptionsCheckAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.optionslist, parent, false);
        return new OptionsCheckAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OptionsCheckAdapter.MyViewHolder holder,int position) {
        SingleOptions option = optionsList.get(position);
            holder.iv_opmedia.setImageBitmap(getOptionImage(option.getQbo_media_file()));
            holder.iv_opmedia.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.rb_option.setChecked(position == mSelectedItem);
/*

        holder.rb_option.setChecked(false);
        holder.option_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                holder.rb_option.setChecked(true);
            }
        });
        holder.rb_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                holder.rb_option.setChecked(true);
            }
        });
*/

    }

    public int getSelectedItem(){
        return mSelectedItem;
    }

    public Bitmap getOptionImage(String file){
        Bitmap b =  BitmapFactory.decodeFile(path + file);
        return b ;
    }

}