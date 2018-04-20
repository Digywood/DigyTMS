package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.digywood.tms.Pojo.SingleFlashQuestion;
import com.digywood.tms.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Shashank on 21-02-2018.
 */

public class ScrollGridCardAdapter extends BaseAdapter{

    TextView textView;
    Context c;
    private int size;
    ArrayList<SingleFlashQuestion> fq_List;
//    ArrayList<Integer> knowList;
//    ArrayList<Integer> donknowList;
//    ArrayList<Integer> skipList;

    public ScrollGridCardAdapter(Context c,ArrayList<SingleFlashQuestion> fQList,int screensize){

        this.c = c;
//        this.samplequestion = samplequestion;
        this.fq_List=fQList;
//        ArrayList<Integer> knowList,ArrayList<Integer> donknowList,ArrayList<Integer> skipList
//        this.knowList = knowList;
//        this.donknowList = donknowList;
//        this.skipList = skipList;
        this.size=screensize;
    }

    @Override
    public int getCount() {
        return fq_List.size();
    }

    @Override
    public SingleFlashQuestion getItem(int position) {
        return fq_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        textView = new TextView(c);
        textView.setText(fq_List.get(position).getQseqnum());
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(c.getResources().getColor(setResource(size,fq_List.get(position).getQstatus())));
        return textView;
    }

    public int setResource(int size, String status){
        int resource = -1;
        if(size == Configuration.SCREENLAYOUT_SIZE_LARGE){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.color.transp;
                    break;
                case "IKNOW": resource = R.color.confirm;
                    break;
                case "IDONKNOW": resource = R.color.bookmark;
                    break;
                case "SKIPPED": resource = R.color.skipped;
                    break;
                default: resource = R.color.transp;
                    break;
            }
        }
        else if(size == Configuration.SCREENLAYOUT_SIZE_NORMAL ){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.color.transp;
                    break;
                case "IKNOW": resource = R.color.confirm;
                    break;
                case "IDONKNOW": resource = R.color.bookmark;
                    break;
                case "SKIPPED": resource = R.color.skipped;
                    break;
                default: resource = R.color.transp;
                    break;
            }
        }
        return resource;
    }

}
