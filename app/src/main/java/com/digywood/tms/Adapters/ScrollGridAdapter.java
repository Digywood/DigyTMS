package com.digywood.tms.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.digywood.tms.Pojo.SingleQuestionList;
import com.digywood.tms.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Shashank on 21-02-2018.
 */

public class ScrollGridAdapter extends BaseAdapter{

    TextView textView;
    Context c;
    private int size;
    JSONArray samplequestion;

    ArrayList<SingleQuestionList> q_List;
    public ScrollGridAdapter(Context c, JSONArray samplequestion , ArrayList<SingleQuestionList> q_List,int size){

        this.c = c;
        this.samplequestion = samplequestion;
        this.q_List = q_List;
        this.size = size;

    }

    @Override
    public int getCount() {
        return samplequestion.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject obj = new JSONObject();
        try {
            obj =  samplequestion.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        textView = new TextView(c);
        try {
                textView.setText(samplequestion.getJSONObject(position).getString("qbm_SequenceID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(c.getResources().getColor(setResource(size,q_List.get(position).getQ_status())));

        return textView;
    }

    public void updateList(ArrayList<SingleQuestionList> q_list){
        this.q_List = q_list;
        notifyDataSetChanged();
    }

    public int setResource(int size, String status){
        int resource = -1;
        if(size == Configuration.SCREENLAYOUT_SIZE_LARGE){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.color.transp;
                    break;
                case "ATTEMPTED": resource = R.color.confirm;
                    break;
                case "SKIPPED": resource = R.color.skipped;
                    break;
                case "BOOKMARKED": resource = R.color.bookmark;
                    break;
                default: resource = R.color.transp;
                    break;
            }
        }
        else if(size == Configuration.SCREENLAYOUT_SIZE_NORMAL ){
            switch (status){
                case "NOT_ATTEMPTED": resource = R.color.transp;
                    break;
                case "ATTEMPTED": resource = R.color.confirm;
                    break;
                case "SKIPPED": resource = R.color.skipped;
                    break;
                case "BOOKMARKED": resource = R.color.bookmark;
                    break;
                default: resource = R.color.transp;
                    break;
            }
        }
        return resource;
    }

}
