package com.digywood.tms.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    JSONArray samplequestion;
    ArrayList<Integer> knowList;
    ArrayList<Integer> donknowList;
    ArrayList<Integer> skipList;

    public ScrollGridCardAdapter(Context c, JSONArray samplequestion,ArrayList<Integer> knowList,ArrayList<Integer> donknowList,ArrayList<Integer> skipList){

        this.c = c;
        this.samplequestion = samplequestion;
        this.knowList = knowList;
        this.donknowList = donknowList;
        this.skipList = skipList;

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
            textView.setText(samplequestion.getJSONObject(position).getString("qbm_SequenceId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);

        if(knowList.contains(position)){
            textView.setBackgroundColor(c.getResources().getColor(R.color.green));
        }else if(donknowList.contains(position)){
            textView.setBackgroundColor(c.getResources().getColor(R.color.red));
        }else if(skipList.contains(position)){
            textView.setBackgroundColor(Color.parseColor("#6758c4"));
        }else{


        }
        return textView;
    }

}
