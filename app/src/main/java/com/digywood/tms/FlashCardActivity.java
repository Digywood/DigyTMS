package com.digywood.tms;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.CardQuestionAdapter;
import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.ScrollGridAdapter;
import com.digywood.tms.Adapters.ScrollGridCardAdapter;
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
    RecyclerView question_scroll;
    ImageView iv_quesimg,iv_fullscreen;
    String filedata,status="";
    int d=0,pos=0,secpos=0;
    GridView gridView;
    private PopupWindow pw;
    ScrollGridCardAdapter scrollAdapter;
    ArrayAdapter<String> sectionAdp;
    CardQuestionAdapter cAdp;
    LinearLayoutManager myLayoutManager;
    JSONArray gja_sections,gja_questions;
    static int screensize=0;
    Dialog mydialog;
    int attemptcount=0,knowcount=0,donknowcount=0,skipcount=0;
    TextView tv_attempted,tv_iknow,tv_idonknow,tv_skipped;
    ArrayList<Integer> knownList=new ArrayList<>();
    ArrayList<Integer> donknowList=new ArrayList<>();
    ArrayList<Integer> skipList=new ArrayList<>();
    ArrayList<String> sectionList=new ArrayList<>();
    ArrayList<SingleQuestionList> questionList=new ArrayList<>();
    ArrayList<String> fimageList=new ArrayList<>();
    Button btn_know,btn_idonknow,btn_prev,btn_next,btn_answer;

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
        question_scroll=findViewById(R.id.question_scroll);
        iv_quesimg=findViewById(R.id.fl_ivques);
        iv_fullscreen=findViewById(R.id.fl_fullscreen);
        btn_prev=findViewById(R.id.btn_prev);
        btn_next=findViewById(R.id.btn_next);
        btn_know=findViewById(R.id.btn_iknow);
        btn_idonknow=findViewById(R.id.btn_idonknow);
        btn_answer=findViewById(R.id.btn_answer);

        tv_attempted=findViewById(R.id.tv_attemptcount);
        tv_iknow=findViewById(R.id.tv_iknowcount);
        tv_idonknow=findViewById(R.id.tv_idonknowcount);
        tv_skipped=findViewById(R.id.tv_skippedcount);

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

        question_scroll.addOnItemTouchListener(new RecyclerTouchListener(FlashCardActivity.this,question_scroll,new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                try{
                    pos=position;
                    d=position;
                    JSONObject qObj=gja_questions.getJSONObject(position);
                    String imagefile=qObj.getString("qbm_flash_image");
                    Log.e("Image Path :--",URLClass.mainpath+imagefile);
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+imagefile);
                    iv_quesimg.setImageBitmap(bmp);

                    myLayoutManager.scrollToPosition(position);
//                    rv_quesnum.scrollBy(position,0);
                    cAdp.setPoiner(position);
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
                    secpos=position;
                    JSONObject secObj=gja_sections.getJSONObject(position);

                    gja_questions=secObj.getJSONArray("Questions");
                    for(int j=0;j<gja_questions.length();j++){
                        questionList.add(new SingleQuestionList(gja_questions.getJSONObject(j).getString("qbm_SequenceId"),"NOT_ATTEMPTED"));
                    }
                    cAdp.updateList(questionList);
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
                initiatePopupWindow(v);
            }
        });

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydialog = new Dialog(FlashCardActivity.this);
                mydialog.getWindow();
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.dialog_answer);
                mydialog.show();
                mydialog.setCanceledOnTouchOutside(false);

                ImageView iv_answerimg = mydialog.findViewById(R.id.iv_answer);
                ImageView iv_dialogclose = mydialog.findViewById(R.id.iv_close);

                try {
                    String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                    Log.e("Image Path :--",filename);
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                    iv_answerimg.setImageBitmap(bmp);
                    cAdp.setPoiner(d);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ViewLotInfo---",e.toString());
                }

                iv_dialogclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mydialog.dismiss();
                    }
                });

            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d--;
                if(d>=0){
                    myLayoutManager.scrollToPosition(d);
                    try {
                        String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                        Log.e("Image Path :--",filename);
                        Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                        iv_quesimg.setImageBitmap(bmp);
                        cAdp.setPoiner(d);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("ViewLotInfo---",e.toString());
                    }
                }else{
                    secpos--;
                    if(secpos>=0){
                        sp_sections.setSelection(secpos);
                        d=0;
                        pos=d;
                        try {
                            String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                            Log.e("Image Path :--",filename);
                            Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                            iv_quesimg.setImageBitmap(bmp);
                            cAdp.setPoiner(d);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("ViewLotInfo---",e.toString());
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Your are at Starting",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SingleQuestionList singleQList=questionList.get(pos);

                String currStatus=singleQList.getQ_status();

                if(currStatus.equalsIgnoreCase("NOT_ATTEMPTED")){

                    if(status.equalsIgnoreCase("")){
                        questionList.get(pos).setQ_status("SKIPPED");
                        skipcount++;
                        skipList.add(pos);
                        String count=String.format("%03d",skipcount);
                        tv_skipped.setText(count);

                    }else{
                        if(status.equalsIgnoreCase("IKNOW")){
                            questionList.get(pos).setQ_status("IKNOW");
                            knowcount++;
                            knownList.add(pos);
                            String count=String.format("%03d",knowcount);
                            tv_iknow.setText(count);
                        }else{
                            questionList.get(pos).setQ_status("IDONKNOW");
                            donknowcount++;
                            donknowList.add(pos);
                            String count=String.format("%03d",donknowcount);
                            tv_idonknow.setText(count);
                        }
                    }

                    attemptcount++;

                    String count=String.format("%03d",attemptcount);
                    tv_attempted.setText(count);

                    cAdp.updateList(questionList);

                    status="";

                    if(pos>=questionList.size()-1){

                        if(secpos<gja_sections.length()-1){
                            secpos=secpos+1;
                            sp_sections.setSelection(secpos);
                            d=0;
                            pos=d;
                            try {
                                String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                                Log.e("Image Path :--",filename);
                                Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                                iv_quesimg.setImageBitmap(bmp);
                                cAdp.setPoiner(d);
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e("ViewLotInfo---",e.toString());
                            }
                        }else{
                            showAlert("Would you like to end the test?");
                        }

                    }else{
                        d++;
                        pos=d;
                        myLayoutManager.scrollToPosition(d);
                        try {
                            String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                            Log.e("Image Path :--",filename);
                            Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                            iv_quesimg.setImageBitmap(bmp);
                            cAdp.setPoiner(d);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("ViewLotInfo---",e.toString());
                        }
                    }
                }else{
                    if(pos>=questionList.size()-1){

                        if(secpos<gja_sections.length()-1){
                            secpos=secpos+1;
                            sp_sections.setSelection(secpos);
                            d=0;
                            pos=d;
                            try {
                                String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                                Log.e("Image Path :--",filename);
                                Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                                iv_quesimg.setImageBitmap(bmp);
                                cAdp.setPoiner(d);
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.e("ViewLotInfo---",e.toString());
                            }
                        }else{
                            showAlert("Would you like to end the test?");
                        }

                    }else{
                        d++;
                        pos=d;
                        myLayoutManager.scrollToPosition(d);
                        try {
                            String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                            Log.e("Image Path :--",filename);
                            Bitmap bmp = BitmapFactory.decodeFile(URLClass.mainpath+filename);
                            iv_quesimg.setImageBitmap(bmp);
                            cAdp.setPoiner(d);
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("ViewLotInfo---",e.toString());
                        }
                    }
                }
            }
        });

        btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status="IKNOW";
//                questionList.get(pos).setQ_status(status);
            }
        });

        btn_idonknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status="IDONKNOW";
//                questionList.get(pos).setQ_status(status);
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
                        questionList.add(new SingleQuestionList(gja_questions.getJSONObject(j).getString("qbm_SequenceId"),"NOT_ATTEMPTED"));
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

    //method to create a popup window containing question numbers
    public void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) FlashCardActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout

            View view = inflater.inflate(R.layout.popup_screen,
                    (ViewGroup) findViewById(R.id.popup_element));
            RelativeLayout layout = view.findViewById(R.id.popup_element);
            int width = 650;
            int height = 600;
            width = layout.getWidth();
            height = layout.getHeight();
            //Instantiate grid view
            gridView = view.findViewById(R.id.scroll_grid);
            //Instantiate grid adapter
            scrollAdapter = new ScrollGridCardAdapter(FlashCardActivity.this,gja_questions,knownList,donknowList,skipList);
            //Setting Adapter to gridview
            gridView.setAdapter(scrollAdapter);
            // create a 300px width and 570px height PopupWindow
            pw = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);
            if (android.os.Build.VERSION.SDK_INT > 20) {
                pw.setElevation(10);
            }
//            TextView mResultText = (TextView) layout.findViewById(R.id.server_status_text);
            Button cancelButton = (Button) view.findViewById(R.id.close_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                    mHideRunnable.run();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            cAdp = new CardQuestionAdapter(questionList,FlashCardActivity.this,getScreenSize());
            cAdp.setPoiner(0);
            myLayoutManager = new LinearLayoutManager(FlashCardActivity.this,LinearLayoutManager.HORIZONTAL,false);
            question_scroll.setLayoutManager(myLayoutManager);
            question_scroll.setItemAnimator(new DefaultItemAnimator());
            question_scroll.setAdapter(cAdp);
        } else {
//            rv_quesnum.setAdapter(null);
        }
    }

    //method to get the deivce screen size
    public int getScreenSize() {
        screensize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        return screensize;
    }

}
