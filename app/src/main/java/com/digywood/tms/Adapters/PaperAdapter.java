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
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.ListofPractiseTests;
import com.digywood.tms.Pojo.SingleFlashQuestion;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.activity_paperitem, null);

            singlePaper=paperList.get(position);

            LinearLayout ll_click=grid.findViewById(R.id.ll_click);
            TextView tv_papername =grid.findViewById(R.id.tv_papername);
            TextView tv_papermin =grid.findViewById(R.id.tv_paperpmin);
            TextView tv_paperavg =grid.findViewById(R.id.tv_paperpavg);
            TextView tv_papermax =grid.findViewById(R.id.tv_paperpmax);

            TextView tv_paperfmin =grid.findViewById(R.id.tv_paperfmin);
            TextView tv_paperfavg =grid.findViewById(R.id.tv_paperfavg);
            TextView tv_paperfmax =grid.findViewById(R.id.tv_paperfmax);

            TextView tv_paperamin =grid.findViewById(R.id.tv_paperamin);
            TextView tv_paperaavg =grid.findViewById(R.id.tv_paperaavg);
            TextView tv_paperamax =grid.findViewById(R.id.tv_paperamax);

            ProgressBar pb_practise=grid.findViewById(R.id.practise_progress);
            ProgressBar pb_flash=grid.findViewById(R.id.flash_progress);
            ProgressBar pb_assessment=grid.findViewById(R.id.assesment_progress);

            Resources res=mContext.getResources();

            pb_practise.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));
            pb_flash.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));
            pb_assessment.setProgressDrawable(res.getDrawable(R.drawable.pb_bg_color));

            tv_papername.setText(singlePaper.getPaperName());
            tv_papermin.setText(""+singlePaper.getMin());
            tv_paperavg.setText(""+singlePaper.getAvg());
            tv_papermax.setText(""+singlePaper.getMax());

            tv_paperfmin.setText(""+0.0);
            tv_paperfavg.setText(""+0.0);
            tv_paperfmax.setText(""+0.0);

            tv_paperamin.setText(""+0.0);
            tv_paperaavg.setText(""+0.0);
            tv_paperamax.setText(""+0.0);

//            ll_click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i=new Intent(mContext,ListofPractiseTests.class);
//                    i.putExtra("enrollid",enrollId);
//                    i.putExtra("courseid",courseId);
//                    i.putExtra("paperid",singlePaper.getPaperId());
//                    mContext.startActivity(i);
//                }
//            });

        } else {
            grid =convertView;
        }
        return grid;
    }

    public void updateGrid(ArrayList<SinglePaper> newList){
        paperList.clear();
        paperList=newList;
    }

}
