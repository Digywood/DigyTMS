package com.digywood.tms.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.R;

import java.util.ArrayList;

public class OptionsCheckAdapter extends RecyclerView.Adapter<OptionsCheckAdapter.MyViewHolder>{

    private ArrayList<SingleOptions> optionsList;
    private Context mycontext;
    private String path,sequence = "-1",flag="NO";
    private int mSelectedItem = -1,mCorrectItem = -1;
    private  Boolean medit = true;
    RecyclerView rv;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout option_layout;
        public FrameLayout img_wrapper;
        public ImageView iv_opmedia;
        public RadioButton rb_option;

        public MyViewHolder(View view) {
            super(view);
            option_layout = view.findViewById(R.id.option_layout);
            img_wrapper = view.findViewById(R.id.fl_img_wrapper);
            iv_opmedia = view.findViewById(R.id.iv_opmedia);
            rb_option = view.findViewById(R.id.rb_option);
            option_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    sequence = optionsList.get(mSelectedItem).getQbo_seq_no();
                    flag = optionsList.get(mSelectedItem).getQbo_answer_flag();
                    rv.setItemAnimator(null);
                    notifyItemRangeChanged(0,optionsList.size());
                }
            });
            rb_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    sequence = optionsList.get(mSelectedItem).getQbo_seq_no();
                    flag = optionsList.get(mSelectedItem).getQbo_answer_flag();
                    rv.setItemAnimator(null);
                    notifyItemRangeChanged(0,optionsList.size());
                }
            });
            if (!medit){
                rb_option.setOnClickListener(null);
                option_layout.setOnClickListener(null);
                rb_option.setEnabled(false);
            }
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

    public OptionsCheckAdapter(ArrayList<SingleOptions> optionsListList, Context c, String path,RecyclerView rv) {
        this.optionsList = optionsListList;
        this.mycontext=c;
        this.path = path;
        this.rv = rv;
    }


    public void updateList(ArrayList<SingleOptions> list){
        optionsList = list;
        notifyDataSetChanged();
    }

    public OptionsCheckAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.optionslist, parent, false);
        return new OptionsCheckAdapter.MyViewHolder(itemView);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final OptionsCheckAdapter.MyViewHolder holder,int position) {
        SingleOptions option = optionsList.get(position);
            holder.iv_opmedia.setImageBitmap(getOptionImage(option.getQbo_media_file()));
            holder.iv_opmedia.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.rb_option.setChecked(position == mSelectedItem);
            flag = optionsList.get(position).getQbo_answer_flag();

            if(flag.equals("YES")){
                mCorrectItem = position;
            }
            if(!medit){
                if(position == mCorrectItem){
                    holder.option_layout.setBackgroundColor(mycontext.getResources().getColor(R.color.transp_green));
                    holder.img_wrapper.setForeground(mycontext.getDrawable(R.drawable.image_correct_overlay));
                }

            }
            if(mSelectedItem > -1){
                sequence = optionsList.get(mSelectedItem).getQbo_seq_no();
                flag = optionsList.get(mSelectedItem).getQbo_answer_flag();
                if(flag.equals("YES")){
                    mCorrectItem = mSelectedItem;
                }
                if(!medit){
                    if(position == mSelectedItem){
                        if(position == mCorrectItem){
                            holder.option_layout.setBackgroundColor(mycontext.getResources().getColor(R.color.transp_green));
                            holder.img_wrapper.setForeground(mycontext.getDrawable(R.drawable.image_correct_overlay));
                        }else {
                            holder.option_layout.setBackgroundColor(mycontext.getResources().getColor(R.color.transp_red));
                            holder.img_wrapper.setForeground(mycontext.getDrawable(R.drawable.image_wrong_overlay));
                        }
                    }
                }
            }

    }

    public int getSelectedItem(){
        return mSelectedItem;
    }

    public String getSelectedSequence(){
        return sequence;
    }

    public String getFlag(){
        return flag;
    }

    public void setOptionsList(int SelectedList){
        mSelectedItem = SelectedList;
    }

    public void setCorrectOptions(int CorrectItem){
        mCorrectItem = CorrectItem;
    }

    public void resetOptionsList(){
        mSelectedItem = -1;
    }

    public void setOptionsEditable(Boolean edit){
        medit = edit;
    }

    public Bitmap getOptionImage(String file){
        Bitmap b =  BitmapFactory.decodeFile(path + file);
        return b ;
    }

/*    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }*/


}