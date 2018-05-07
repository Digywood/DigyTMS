package com.digywood.tms;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.CardQuestionAdapter;
import com.digywood.tms.Adapters.ScrollGridCardAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleFlashQuestion;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FlashCardActivity extends AppCompatActivity {

    Spinner sp_sections;
    RecyclerView question_scroll;
    ImageView iv_quesimg,iv_fullscreen;
    String filedata,status="",testId="",testPath="",studentid="",enrollid="",courseid="",imgPath="";
    String subjectid="",paperid="",rfilename="",startDttm="",endDttm="",tempString="";
    int d=0,pos=0,secpos=0,z=0;
    GridView gridView;
    private PopupWindow pw;
    ScrollGridCardAdapter scrollAdapter;
    ArrayAdapter<String> sectionAdp;
    CardQuestionAdapter cAdp;
    LinearLayoutManager myLayoutManager;
    JSONArray gja_sections,gja_questions,reviewArray;
    static int screensize=0;
    Dialog mydialog;
    DBHelper myhelper;
    JSONObject reviewObj;
    int attemptcount=0,knowcount=0,donknowcount=0,skipcount=0,Qcount=0;
    TextView tv_attempted,tv_iknow,tv_idonknow,tv_skipped,tv_Qid;
    ArrayList<String> knownList=new ArrayList<>();
    ArrayList<String> donknowList=new ArrayList<>();
    ArrayList<String> skipList=new ArrayList<>();
    ArrayList<String> sectionList=new ArrayList<>();
    ArrayList<ArrayList<SingleFlashQuestion>> baseQList=new ArrayList<>();
    ArrayList<SingleFlashQuestion> questionList=new ArrayList<>();
    ArrayList<String> fimageList=new ArrayList<>();
    Button btn_know,btn_idonknow,btn_prev,btn_next,btn_answer,btn_gQues,btn_exit;

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
    LinearLayout myrlayout;
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

        setContentView(R.layout.activity_sample);

        mVisible = true;
//        myrlayout = findViewById(R.id.header);
        myrlayout = findViewById(R.id.header);

        myhelper=new DBHelper(this);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            testId=cmgintent.getStringExtra("testId");
            testPath=cmgintent.getStringExtra("testPath");
        }

        if(testId!=null){
            Cursor mycursor=myhelper.getSingleTestData(testId);
            if(mycursor.getCount()>0){
                while(mycursor.moveToNext()){

                    studentid=mycursor.getString(mycursor.getColumnIndex("sptu_student_ID"));
                    enrollid=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                    courseid=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
//                    subjectid=mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                    paperid=mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                    imgPath=URLClass.mainpath+enrollid+"/"+courseid+"/";

                }
            }else{
                mycursor.close();
            }
        }

        sp_sections=findViewById(R.id.fl_sections);
        question_scroll=findViewById(R.id.question_scroll);
        iv_quesimg=findViewById(R.id.fl_ivques);
        iv_fullscreen=findViewById(R.id.fl_fullscreen);
        btn_prev=findViewById(R.id.btn_prev);
        btn_next=findViewById(R.id.btn_next);
        btn_know=findViewById(R.id.btn_iknow);
        btn_idonknow=findViewById(R.id.btn_idonknow);
        btn_answer=findViewById(R.id.btn_answer);
        btn_gQues=findViewById(R.id.btn_groupQues);
        btn_exit=findViewById(R.id.btn_exit);

        tv_attempted=findViewById(R.id.tv_attemptcount);
        tv_iknow=findViewById(R.id.tv_iknowcount);
        tv_idonknow=findViewById(R.id.tv_idonknowcount);
        tv_skipped=findViewById(R.id.tv_skippedcount);
        tv_Qid=findViewById(R.id.tv_Qid);

//        slideanim=AnimationUtils.loadAnimation(FlashCardActivity.this,R.anim.layout_animation_slide_right);

        startDttm= new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());

        try{
            BufferedReader br = new BufferedReader(new FileReader(testPath+"flashAttempts/"+testId+"_01.json"));
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

                btn_know.setBackgroundResource(R.drawable.test_button_normal);
                btn_idonknow.setBackgroundResource(R.drawable.test_button_normal);

                status="";
                tempString="";

                try{
                    pos=position;
                    d=position;
                    JSONObject qObj=gja_questions.getJSONObject(position);
                    String imagefile=qObj.getString("qbm_flash_image");
                    Log.e("Image Path :--",imagefile);
                    String sid=qObj.getString("qbm_SubjectID");
                    String pid=qObj.getString("qbm_Paper_ID");
                    String cid=qObj.getString("qbm_ChapterID");
                    Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+imagefile);
                    iv_quesimg.setImageBitmap(bmp);
                    tv_Qid.setText(qObj.getString("qbm_ID"));
                    Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this,R.anim.fade_in);
                    iv_quesimg.startAnimation(rotateimage);

                    String gFlag=qObj.getString("qbm_group_flag");
                    if(gFlag.equalsIgnoreCase("YES")){
                        btn_gQues.setVisibility(View.VISIBLE);
                    }else{
                        btn_gQues.setVisibility(View.GONE);
                    }

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
                    knownList.clear();
                    donknowList.clear();
                    skipList.clear();
                    secpos=position;

                    d=0;
                    pos=d;

                    JSONObject secObj=gja_sections.getJSONObject(position);

                    gja_questions=secObj.getJSONArray("Questions");

                    JSONObject qObj=gja_questions.getJSONObject(position);
                    String imagefile=qObj.getString("qbm_flash_image");
                    Log.e("Image Path :--",imagefile);
                    String sid=qObj.getString("qbm_SubjectID");
                    String pid=qObj.getString("qbm_Paper_ID");
                    String cid=qObj.getString("qbm_ChapterID");
                    Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+imagefile);
                    iv_quesimg.setImageBitmap(bmp);
                    tv_Qid.setText(qObj.getString("qbm_ID"));
                    Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this, R.anim.fade_in);
                    iv_quesimg.startAnimation(rotateimage);

                    String gFlag=qObj.getString("qbm_group_flag");
                    if(gFlag.equalsIgnoreCase("YES")){
                        btn_gQues.setVisibility(View.VISIBLE);
                    }else{
                        btn_gQues.setVisibility(View.GONE);
                    }

                    ArrayList<SingleFlashQuestion> tempList;

                    tempList=baseQList.get(position);

                    questionList = (ArrayList<SingleFlashQuestion>)tempList.clone();

                    cAdp.updateList(questionList);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("FlashCardActivity----",e.toString());
                }
                mHideRunnable.run();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                disableEnableViews(myrlayout);
                initiatePopupWindow(v);
            }
        });

        btn_gQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydialog = new Dialog(FlashCardActivity.this);
                mydialog.getWindow();
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.dialog_groupimg);
                mydialog.show();
                mydialog.setCanceledOnTouchOutside(false);

                ImageView iv_answerimg = mydialog.findViewById(R.id.iv_gQanswer);
                ImageView iv_dialogclose = mydialog.findViewById(R.id.iv_gQclose);

                try {
                    String filename=gja_questions.getJSONObject(d).getString("gbg_media_file");
                    Log.e("Image Path :--",filename);
                    String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                    String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                    String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                    Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+filename);
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
                        mHideRunnable.run();
                    }
                });

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAlert("Would you like exit Test Session?");
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

                z=0;

                final ImageView iv_answerimg = mydialog.findViewById(R.id.iv_answer);
                ImageView iv_dialogclose = mydialog.findViewById(R.id.iv_close);
                final ImageButton iv_left=mydialog.findViewById(R.id.img_left_arrow);
                final ImageButton iv_right=mydialog.findViewById(R.id.img_right_arrow);

                try {
                    reviewArray=gja_questions.getJSONObject(d).getJSONArray("Review");

                    if(reviewArray.length()>0){
                        JSONObject reviewObj=reviewArray.getJSONObject(z);
                        rfilename=reviewObj.getString("qba_media_file");
                        Log.e("Image file :--",rfilename);
                        String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                        String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                        String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                        Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+rfilename);
                        iv_answerimg.setImageBitmap(bmp);
                    }else{
                        Toast.makeText(getApplicationContext(),"No Review Images",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ViewLotInfo---",e.toString());
                }

                iv_dialogclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mydialog.dismiss();
                        mHideRunnable.run();
                    }
                });

                iv_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            if(z<=0){
                                Toast.makeText(getApplicationContext(),"Start Position",Toast.LENGTH_SHORT).show();
                            }else{
                                z--;
                                reviewObj=reviewArray.getJSONObject(z);
                                rfilename=reviewObj.getString("qba_media_file");
                                Log.e("Image file :--",rfilename);
                                String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                                String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                                String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                                Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+rfilename);
                                iv_answerimg.setImageBitmap(bmp);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("FlashCardActivity-----",e.toString());
                        }
                    }
                });

                iv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            if(z>=reviewArray.length()-1){
                               Toast.makeText(getApplicationContext(),"End Position",Toast.LENGTH_SHORT).show();
                            }else{
                                z++;
                                reviewObj=reviewArray.getJSONObject(z);
                                rfilename=reviewObj.getString("qba_media_file");
                                Log.e("Image file :--",rfilename);
                                String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                                String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                                String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                                Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+rfilename);
                                iv_answerimg.setImageBitmap(bmp);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("FlashCardActivity-----",e.toString());
                        }
                    }
                });

            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_know.setBackgroundResource(R.drawable.test_button_normal);
                btn_idonknow.setBackgroundResource(R.drawable.test_button_normal);

                if(d>=0){
                    flashQAttempt();
                }else{
                    Log.e("Previous:--","No Update");
                }

                d--;
                pos=d;

                if(d>=0){
                    myLayoutManager.scrollToPosition(d);
                    try {
                        String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                        Log.e("Image Path :--",filename);
                        String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                        String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                        String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                        Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+filename);
                        iv_quesimg.setImageBitmap(bmp);

                        tv_Qid.setText(gja_questions.getJSONObject(d).getString("qbm_ID"));

                        Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this, R.anim.fade_in);
                        iv_quesimg.startAnimation(rotateimage);

                        String gFlag=gja_questions.getJSONObject(d).getString("qbm_group_flag");
                        if(gFlag.equalsIgnoreCase("YES")){
                            btn_gQues.setVisibility(View.VISIBLE);
                        }else{
                            btn_gQues.setVisibility(View.GONE);
                        }

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
                            String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                            String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                            String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                            Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+filename);
                            iv_quesimg.setImageBitmap(bmp);
                            tv_Qid.setText(gja_questions.getJSONObject(d).getString("qbm_ID"));

                            Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this, R.anim.fade_in);
                            iv_quesimg.startAnimation(rotateimage);

                            String gFlag=gja_questions.getJSONObject(d).getString("qbm_group_flag");
                            if(gFlag.equalsIgnoreCase("YES")){
                                btn_gQues.setVisibility(View.VISIBLE);
                            }else{
                                btn_gQues.setVisibility(View.GONE);
                            }

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

                btn_know.setBackgroundResource(R.drawable.test_button_normal);
                btn_idonknow.setBackgroundResource(R.drawable.test_button_normal);

                flashQAttempt();

                if(pos>=questionList.size()-1){

                    if(secpos<gja_sections.length()-1){
                        secpos=secpos+1;
                        sp_sections.setSelection(secpos);
                        d=0;
                        pos=d;
                        try {
                            String filename=gja_questions.getJSONObject(d).getString("qbm_flash_image");
                            Log.e("Image Path :--",filename);
                            String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                            String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                            String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                            Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+filename);
                            iv_quesimg.setImageBitmap(bmp);
                            tv_Qid.setText(gja_questions.getJSONObject(d).getString("qbm_ID"));

                            Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this, R.anim.fade_in);
                            iv_quesimg.startAnimation(rotateimage);

                            String gFlag=gja_questions.getJSONObject(d).getString("qbm_group_flag");
                            if(gFlag.equalsIgnoreCase("YES")){
                                btn_gQues.setVisibility(View.VISIBLE);
                            }else{
                                btn_gQues.setVisibility(View.GONE);
                            }

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
                        String sid=gja_questions.getJSONObject(d).getString("qbm_SubjectID");
                        String pid=gja_questions.getJSONObject(d).getString("qbm_Paper_ID");
                        String cid=gja_questions.getJSONObject(d).getString("qbm_ChapterID");
                        Bitmap bmp = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+filename);
                        iv_quesimg.setImageBitmap(bmp);
                        tv_Qid.setText(gja_questions.getJSONObject(d).getString("qbm_ID"));

                        Animation rotateimage = AnimationUtils.loadAnimation(FlashCardActivity.this, R.anim.fade_in);
                        iv_quesimg.startAnimation(rotateimage);

                        String gFlag=gja_questions.getJSONObject(d).getString("qbm_group_flag");
                        if(gFlag.equalsIgnoreCase("YES")){
                            btn_gQues.setVisibility(View.VISIBLE);
                        }else{
                            btn_gQues.setVisibility(View.GONE);
                        }

                        cAdp.setPoiner(d);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("ViewLotInfo---",e.toString());
                    }
                }

            }
        });

        btn_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equalsIgnoreCase("IDONKNOW")){
                    questionList.get(pos).setQstatus(tempString);
                }else{

                }

                status="IKNOW";
                tempString="";
                btn_idonknow.setBackgroundResource(R.drawable.test_button_normal);
                btn_know.setBackgroundResource(R.drawable.test_button_pressed);
                tempString=questionList.get(pos).getQstatus();
                questionList.get(pos).setQstatus(status);
                cAdp.updateList(questionList);
                flashQAttempt();
            }
        });

        btn_idonknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equalsIgnoreCase("IKNOW")){
                    questionList.get(pos).setQstatus(tempString);
                }else{

                }

                status="IDONKNOW";
                tempString="";
                btn_know.setBackgroundResource(R.drawable.test_button_normal);
                btn_idonknow.setBackgroundResource(R.drawable.test_button_pressed);
                tempString=questionList.get(pos).getQstatus();
                questionList.get(pos).setQstatus(status);
                cAdp.updateList(questionList);
                flashQAttempt();
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

    @Override
    public void onBackPressed() {
        exitAlert("Would you like exit Test Session?");
    }

    public ArrayList<String> readJson(String filedata){
        ArrayList<String> flashimageList=new ArrayList();
        JSONObject mainObj=null,secObj=null,quesObj=null;
        JSONArray ja_sections,ja_questions;
        String testid="",section="",sectionid="";

        try{
            mainObj=new JSONObject(filedata);

            testid=mainObj.getString("ptu_test_ID");

            Log.e("JSON--",mainObj.getString("ptu_test_ID"));

            ja_sections=mainObj.getJSONArray("Sections");
            gja_sections=mainObj.getJSONArray("Sections");

            for(int i=0;i<ja_sections.length();i++){

                secObj=ja_sections.getJSONObject(i);

                ArrayList<SingleFlashQuestion> tempList=new ArrayList<>();
                gja_questions=secObj.getJSONArray("Questions");
                for(int j=0;j<gja_questions.length();j++){
                    Qcount++;
                    tempList.add(new SingleFlashQuestion(gja_questions.getJSONObject(j).getString("qbm_ID"),gja_questions.getJSONObject(j).getString("qbm_SequenceId"),"NOT_ATTEMPTED"));
                }

                baseQList.add(tempList);

                section=secObj.getString("ptu_section_name");
                sectionList.add(section);
                sectionid=secObj.getString("ptu_section_ID");
                ja_questions=secObj.getJSONArray("Questions");

                gja_questions=secObj.getJSONArray("Questions");

                Log.e("QuesArray Length---",""+ja_questions.length());
                for(int q=0;q<ja_questions.length();q++){

                    quesObj=ja_questions.getJSONObject(q);
                    String flashname=quesObj.getString("qbm_flash_image");
                    flashimageList.add(flashname);
                }

            }

            setData();

            sectionAdp= new ArrayAdapter(FlashCardActivity.this,android.R.layout.simple_spinner_item,sectionList);
            sectionAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_sections.setAdapter(sectionAdp);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("JSONPARSE---",e.toString()+" : "+e.getStackTrace()[0].getLineNumber());
        }
        return flashimageList;
    }

    public  void flashQAttempt(){

        if(tempString.equalsIgnoreCase("")){

        }else{
            questionList.get(pos).setQstatus(tempString);
            tempString="";
        }

        SingleFlashQuestion singleFQ=questionList.get(pos);

        String currStatus=singleFQ.getQstatus();

        if(currStatus.equalsIgnoreCase("NOT_ATTEMPTED")){

            if(status.equalsIgnoreCase("")){
                questionList.get(pos).setQstatus("SKIPPED");
                skipcount++;
                skipList.add(questionList.get(pos).getQnum());
                String count=String.format("%03d",skipcount);
                tv_skipped.setText(count);

            }else{
                if(status.equalsIgnoreCase("IKNOW")){
                    questionList.get(pos).setQstatus("IKNOW");
                    knowcount++;
                    knownList.add(questionList.get(pos).getQnum());
                    String count=String.format("%03d",knowcount);
                    tv_iknow.setText(count);
                }else{
                    questionList.get(pos).setQstatus("IDONKNOW");
                    donknowcount++;
                    donknowList.add(questionList.get(pos).getQnum());
                    String count=String.format("%03d",donknowcount);
                    tv_idonknow.setText(count);
                }
            }

            attemptcount++;

            tv_attempted.setText(String.format("%03d",attemptcount));

            cAdp.updateList(questionList);

            status="";

        }else{

            if(status.equalsIgnoreCase("")){
//                        questionList.get(pos).setQstatus("SKIPPED");
//
//                        if(skipList.contains(questionList.get(pos).getQnum())){
//
//                        }else{
//                            skipcount++;
//                            skipList.add(questionList.get(pos).getQnum());
//                            String count=String.format("%03d",skipcount);
//                            tv_skipped.setText(count);
//
//                            if(knownList.contains(questionList.get(pos).getQnum())){
//                                knowcount--;
//                                knownList.remove(questionList.get(pos).getQnum());
//                                tv_iknow.setText(String.format("%03d",knowcount));
//                            }else{
//                                donknowcount--;
//                                donknowList.remove(questionList.get(pos).getQnum());
//                                tv_idonknow.setText(String.format("%03d",donknowcount));
//                            }
//                        }

            }else{
                if(status.equalsIgnoreCase("IKNOW")){
                    questionList.get(pos).setQstatus("IKNOW");

                    if(knownList.contains(questionList.get(pos).getQnum())){

                    }else{
                        knowcount++;
                        knownList.add(questionList.get(pos).getQnum());
                        tv_iknow.setText(String.format("%03d",knowcount));

                        if(skipList.contains(questionList.get(pos).getQnum())){
                            skipcount--;
                            skipList.remove(questionList.get(pos).getQnum());
                            tv_skipped.setText(String.format("%03d",skipcount));
                        }else{
                            donknowcount--;
                            donknowList.remove(questionList.get(pos).getQnum());
                            tv_idonknow.setText(String.format("%03d",donknowcount));
                        }
                    }


                }else{
                    questionList.get(pos).setQstatus("IDONKNOW");

                    if(donknowList.contains(questionList.get(pos).getQnum())){

                    }else{
                        donknowcount++;
                        donknowList.add(questionList.get(pos).getQnum());
                        tv_idonknow.setText(String.format("%03d",donknowcount));

                        if(skipList.contains(questionList.get(pos).getQnum())){
                            skipcount--;
                            skipList.remove(questionList.get(pos).getQnum());
                            tv_skipped.setText(String.format("%03d",skipcount));
                        }else{
                            knowcount--;
                            knownList.remove(questionList.get(pos).getQnum());
                            tv_iknow.setText(String.format("%03d",knowcount));
                        }
                    }
                }
            }

//                    cAdp.updateList(questionList);

            status="";
        }
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
//            scrollAdapter = new ScrollGridCardAdapter(FlashCardActivity.this,gja_questions,knownList,donknowList,skipList,getScreenSize());
            scrollAdapter = new ScrollGridCardAdapter(FlashCardActivity.this,questionList,getScreenSize());
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
//                    disableEnableViews(myrlayout);
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
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int attemptnum=myhelper.getFlashAttemptNum(testId);
                        attemptnum=attemptnum+1;
                        Double kcount=Double.parseDouble(String.valueOf(knowcount));
                        Double percent=kcount/Qcount;
//                        Double percentage=Double.parseDouble(String.valueOf(percent));
                        percent=percent*100;
                        percent=round(percent,2);

                        endDttm= new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
                        long iFlag=myhelper.insertFlashAttempt(studentid,enrollid,courseid,subjectid,paperid,testId,attemptnum,startDttm,endDttm,attemptcount,knowcount,donknowcount,skipcount,percent,"Completed");
                        if(iFlag>0){
                            Log.e("FlashCardActivity----","Attempt Inserted");

                            Double minscore=0.0,maxscore=0.0,avgscore=0.0;
                            Cursor mycursor=myhelper.getFlashRawData(testId);
                            if(mycursor.getCount()>0){
                                while (mycursor.moveToNext()){
                                    minscore=mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                                    maxscore=mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                                    avgscore=mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                                }
                                Log.e("Scores:---","min:--"+minscore+"  max--"+maxscore+"  avg----"+avgscore);
                                long uFlag=myhelper.updateTestFlashData(testId,attemptnum,minscore,maxscore,avgscore,endDttm,percent);
                                if(uFlag>0){
                                    Log.e("FlashCardActivity----","Test Updated");
                                }else{
                                    Log.e("FlashCardActivity----","Unable to update Test");
                                }
                            }else{

                            }
                        }else{
                            Log.e("FlashCardActivity----","Unable to Insert Attempt");
                        }

                        dialog.cancel();

                        finish();

                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mHideRunnable.run();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public static void disableEnableViews(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableEnableViews((ViewGroup) child);
            } else {
                if(child.isEnabled()){
                    child.setEnabled(false);
                } else {
                    child.setEnabled(true);
                }
            }
        }
    }

    public  void exitAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(FlashCardActivity.this,R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>"+messege+"</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int attemptnum=myhelper.getFlashAttemptNum(testId);
                        attemptnum=attemptnum+1;
                        Double kcount=Double.parseDouble(String.valueOf(knowcount));
                        Double percent=kcount/Qcount;
//                        Double percentage=Double.parseDouble(String.valueOf(percent));
                        percent=percent*100;
                        percent=round(percent,2);
                        endDttm= new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
                        long iFlag=myhelper.insertFlashAttempt(studentid,enrollid,courseid,subjectid,paperid,testId,attemptnum,startDttm,endDttm,attemptcount,knowcount,donknowcount,skipcount,percent,"Completed");
                        if(iFlag>0){
                            Log.e("FlashCardActivity----","Attempt Inserted");

                            Double minscore=0.0,maxscore=0.0,avgscore=0.0;
                            Cursor mycursor=myhelper.getFlashRawData(testId);
                            if(mycursor.getCount()>0){
                                while (mycursor.moveToNext()){
                                    minscore=mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                                    maxscore=mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                                    avgscore=mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                                }
                                Log.e("Scores:---","min:--"+minscore+"  max--"+maxscore+"  avg----"+avgscore);
                                long uFlag=myhelper.updateTestFlashData(testId,attemptnum,minscore,maxscore,avgscore,endDttm,percent);
                                if(uFlag>0){
                                    Log.e("FlashCardActivity----","Test Updated");
                                }else{
                                    Log.e("FlashCardActivity----","Unable to update Test");
                                }
                            }else{

                            }
                        }else{
                            Log.e("FlashCardActivity----","Unable to Insert Attempt");
                        }

                        dialog.cancel();

                        finish();

                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        mHideRunnable.run();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    public void setData(){
        cAdp = new CardQuestionAdapter(questionList,FlashCardActivity.this,getScreenSize());
        cAdp.setPoiner(0);
        myLayoutManager = new LinearLayoutManager(FlashCardActivity.this,LinearLayoutManager.HORIZONTAL,false);
        question_scroll.setLayoutManager(myLayoutManager);
        question_scroll.setItemAnimator(new DefaultItemAnimator());
        question_scroll.setAdapter(cAdp);
    }

    //method to get the deivce screen size
    public int getScreenSize() {
        screensize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        return screensize;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}