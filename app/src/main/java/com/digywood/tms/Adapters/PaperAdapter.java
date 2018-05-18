package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.digywood.tms.Pojo.SinglePaper;
import com.digywood.tms.R;
import java.util.ArrayList;
import java.util.List;

public class PaperAdapter extends BaseAdapter {

    String subCategory,category;
    private Context mContext;
    SinglePaper singlePaper;
    String enrollId="",courseId="";
    ArrayList<String> checkList=new ArrayList<>();
    ArrayList<SinglePaper> paperList;
    ArrayList<String> SubCategoryCheckedList = new ArrayList<>();

    public PaperAdapter(Context c, ArrayList<SinglePaper> paperList,String enrollid,String courseid) {
        mContext = c;
        this.paperList =paperList;
        this.enrollId=enrollid;
        this.courseId=courseid;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return paperList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position,View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
//        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_paperitem, null);
        } else {
//            grid =convertView;
        }

        singlePaper=paperList.get(position);

        LinearLayout ll_click=convertView.findViewById(R.id.ll_click);
        TextView tv_papername =convertView.findViewById(R.id.tv_papername);

        TextView tv_pattemptcount =convertView.findViewById(R.id.tv_pattemptcount);
        TextView tv_papermin =convertView.findViewById(R.id.tv_paperpmin);
        TextView tv_paperavg =convertView.findViewById(R.id.tv_paperpavg);
        TextView tv_papermax =convertView.findViewById(R.id.tv_paperpmax);

        tv_pattemptcount.setText(singlePaper.getPattemptcount()+"/"+singlePaper.getPtestcount());

        TextView tv_fattemptcount =convertView.findViewById(R.id.tv_fattemptcount);
        TextView tv_paperfmin =convertView.findViewById(R.id.tv_paperfmin);
        TextView tv_paperfavg =convertView.findViewById(R.id.tv_paperfavg);
        TextView tv_paperfmax =convertView.findViewById(R.id.tv_paperfmax);

        tv_fattemptcount.setText(singlePaper.getFattemptcount()+"/"+singlePaper.getPtestcount());

        TextView tv_aattemptcount =convertView.findViewById(R.id.tv_aattemptcount);
        TextView tv_paperamin =convertView.findViewById(R.id.tv_paperamin);
        TextView tv_paperaavg =convertView.findViewById(R.id.tv_paperaavg);
        TextView tv_paperamax =convertView.findViewById(R.id.tv_paperamax);

        tv_aattemptcount.setText(singlePaper.getAattemptcount()+"/"+singlePaper.getAtestcount());

        ProgressBar pb_practise=convertView.findViewById(R.id.practise_progress);
        ProgressBar pb_flash=convertView.findViewById(R.id.flash_progress);
        ProgressBar pb_assessment=convertView.findViewById(R.id.assesment_progress);

        Double d1 = new Double(singlePaper.getPprogress());
        int pprogress = d1.intValue();
        pb_practise.setProgress(pprogress);
        Double d2 = new Double(singlePaper.getFprogress());
        int fprogress = d2.intValue();
        pb_flash.setProgress(fprogress);
        Double d3 = new Double(singlePaper.getAprogress());
        int aprogress = d3.intValue();
        pb_assessment.setProgress(aprogress);

        Resources res=mContext.getResources();

        pb_practise.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));
        pb_flash.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));
        pb_assessment.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));

        tv_papername.setText(singlePaper.getPaperName());
        tv_papermin.setText(""+round(singlePaper.getPmin(),1));
        tv_paperavg.setText(""+round(singlePaper.getPavg(),1));
        tv_papermax.setText(""+round(singlePaper.getPmax(),1));

        tv_paperfmin.setText(""+round(singlePaper.getFmin(),1));
        tv_paperfavg.setText(""+round(singlePaper.getFavg(),1));
        tv_paperfmax.setText(""+round(singlePaper.getFmax(),1));

        tv_paperamin.setText(""+round(singlePaper.getAmin(),1));
        tv_paperaavg.setText(""+round(singlePaper.getAavg(),1));
        tv_paperamax.setText(""+round(singlePaper.getAmax(),1));

        return convertView;
    }

    public void updateGrid(ArrayList<SinglePaper> newList){
        paperList.clear();
        paperList=newList;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
