package com.digywood.tms;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.TestAdapter;
import com.digywood.tms.Pojo.SingleQuestion;
import com.digywood.tms.Pojo.SingleQuestionList;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FlashCardActivity extends AppCompatActivity {

    Spinner sp_sections;
    RecyclerView rv_quesnum;
    ImageView iv_quesimg,iv_fullscreen;
    String filedata;
    int d=0;
    ArrayAdapter<String> sectionAdp;
    QuestionListAdapter qAdp;
    LinearLayoutManager myLayoutManager;
    JSONArray gja_sections,gja_questions;
    ArrayList<String> sectionList=new ArrayList<>();
    ArrayList<SingleQuestionList> questionList=new ArrayList<>();
    ArrayList<String> fimageList=new ArrayList<>();
    Button btn_know,btn_idonknow,btn_prev,btn_next;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
//    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            myrlayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
//    private View mControlsView;
    RelativeLayout myrlayout;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.show();
//            }
//            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flash_card);

        mVisible = true;
//        myrlayout = findViewById(R.id.header);
        myrlayout = findViewById(R.id.header);

        sp_sections=findViewById(R.id.fl_sections);
        rv_quesnum=findViewById(R.id.question_scroll);
        iv_quesimg=findViewById(R.id.fl_ivques);
        iv_fullscreen=findViewById(R.id.fl_fullscreen);
        btn_prev=findViewById(R.id.btn_prev);
        btn_next=findViewById(R.id.btn_next);
        btn_know=findViewById(R.id.btn_iknow);
        btn_idonknow=findViewById(R.id.btn_idonknow);

        try{
            BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+"sample"+".json"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            filedata=sb.toString();
            br.close();
            fimageList=readJson(filedata);
            setData();
            if(fimageList.size()!=0){

                ArrayList<String> missingfList=new ArrayList<>();

                for(int i=0;i<fimageList.size();i++){
                    File myFile = new File(URLClass.mainpath+fimageList.get(i));
                    if(myFile.exists()){

                    }else{
                        missingfList.add(fimageList.get(i));
                    }
                }

                if(missingfList.size()!=0){
                    StringBuilder sbm=new StringBuilder();
                    sbm.append("The following file are missing...\n");
                    for(int i=0;i<missingfList.size();i++){
                        sbm.append(missingfList.get(i)+" , "+"\n");
                    }
                    showAlert(sbm.toString());
                }else{
                    try {
                        Log.e("Image Path :--",URLClass.mainpath+fimageList.get(d));
                        Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+fimageList.get(d));
                        iv_quesimg.setImageBitmap(bmp);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("ViewLotInfo---",e.toString());
                    }
                }
//                for(int i=0;i<fimageList.size();i++){
//
//                }
            }else{
                Log.e("FlashCardActivity---","No Questions to Display");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TestActivity1-----",e.toString());
        }

        // Set up the user interaction to manually show or hide the system UI.
        myrlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        rv_quesnum.addOnItemTouchListener(new RecyclerTouchListener(FlashCardActivity.this,rv_quesnum,new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                try{
                    JSONObject qObj=gja_questions.getJSONObject(position);
                    String imagefile=qObj.getString("qbm_flash_image");
                    Log.e("Image Path :--",URLClass.mainpath+imagefile);
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+imagefile);
                    iv_quesimg.setImageBitmap(bmp);

                    qAdp.setPoiner(position);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("FlashCardActivity----",e.toString());
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        sp_sections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    questionList.clear();

                    JSONObject secObj=gja_sections.getJSONObject(position);

                    gja_questions=secObj.getJSONArray("Questions");
                    for(int j=0;j<gja_questions.length();j++){
                        questionList.add(new SingleQuestionList(gja_questions.getJSONObject(j).getString("qbm_SequenceId"),"Intialised"));
                    }
                    qAdp.updateList(questionList);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("FlashCardActivity----",e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d--;
                try {
                    Log.e("Image Path :--",URLClass.mainpath+fimageList.get(d));
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+fimageList.get(d));
                    iv_quesimg.setImageBitmap(bmp);
                    qAdp.setPoiner(d);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ViewLotInfo---",e.toString());
                }

//                iv_quesimg.setImageBitmap();

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d++;
                try {
                    Log.e("Image Path :--",URLClass.mainpath+fimageList.get(d));
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+fimageList.get(d));
                    iv_quesimg.setImageBitmap(bmp);
                    qAdp.setPoiner(d);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ViewLotInfo---",e.toString());
                }

//                iv_quesimg.setImageBitmap();

            }
        });

        btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_idonknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable,UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        myrlayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public ArrayList<String> readJson(String filedata){
        ArrayList<String> flashimageList=new ArrayList();
        JSONObject mainObj=null,secObj=null,quesObj=null;
        JSONArray ja_sections,ja_questions;
        String testid="",section="",sectionid="";

        try{
            mainObj=new JSONObject(filedata);

            testid=mainObj.getString("sptu_ID");

            Log.e("JSON--",mainObj.getString("sptu_ID"));

            ja_sections=mainObj.getJSONArray("Sections");
            gja_sections=mainObj.getJSONArray("Sections");
            Log.e("sectionArray Length---",""+ja_sections.length());
            for(int i=0;i<ja_sections.length();i++){

                secObj=ja_sections.getJSONObject(i);

                if(i==0){
                    gja_questions=secObj.getJSONArray("Questions");
                    for(int j=0;j<gja_questions.length();j++){
                        questionList.add(new SingleQuestionList(gja_questions.getJSONObject(j).getString("qbm_SequenceId"),"Intialised"));
                    }
                }else{

                }

                section=secObj.getString("Ptu_section_name");
                sectionList.add(section);
                sectionid=secObj.getString("Ptu_section_ID");
                ja_questions=secObj.getJSONArray("Questions");

                gja_questions=secObj.getJSONArray("Questions");

                Log.e("QuesArray Length---",""+ja_questions.length());
                for(int q=0;q<ja_questions.length();q++){

                    quesObj=ja_questions.getJSONObject(q);
                    String flashname=quesObj.getString("qbm_flash_image");
                    flashimageList.add(flashname);
                }

            }

            sectionAdp= new ArrayAdapter(FlashCardActivity.this,android.R.layout.simple_spinner_item,sectionList);
            sectionAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_sections.setAdapter(sectionAdp);

            for(int i=0;i<questionList.size();i++){
                Log.e("Qid----",""+questionList.get(i).getQ_num());
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("JSONPARSE---",e.toString()+" : "+e.getStackTrace()[0].getLineNumber());
        }
        return flashimageList;
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(FlashCardActivity.this,R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>"+messege+"</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public void setData(){
        if (questionList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + questionList.size());
//            tv_emptytests.setVisibility(View.GONE);
            qAdp = new QuestionListAdapter(questionList,FlashCardActivity.this,questionList.size());
            qAdp.setPoiner(0);
            myLayoutManager = new LinearLayoutManager(FlashCardActivity.this,LinearLayoutManager.HORIZONTAL,false);
            rv_quesnum.setLayoutManager(myLayoutManager);
            rv_quesnum.setItemAnimator(new DefaultItemAnimator());
            rv_quesnum.setAdapter(qAdp);
        } else {
//            rv_quesnum.setAdapter(null);
        }
    }
}
