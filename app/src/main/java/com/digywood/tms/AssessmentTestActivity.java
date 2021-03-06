package com.digywood.tms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.OptionsCheckAdapter;
import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.ScrollGridAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.MyBagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.Pojo.SingleQuestionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AssessmentTestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Handler mHandler = new Handler();
    private final static int INTERVAL = 1000 * 30 * 1;
    int loc_count=0,serv_count=0;
    SharedPreferences restoredprefs;
    String restoredsname="",finalUrl="",serverId="";

    TextView timer, q_no,qno_label;
    View finish_view;
    public static File file;
    ImageView fullscreen;
    GridView gridView;
    Spinner sections;
    String /*jsonPath,*/imgPath, photoPath, Seq, Id, path,instanceId,enrollid,courseid,subjectId,paperid,studentId,testid,groupId,orgid,branchid,batchid,satu_Test_Type,satu_Test_Time;
    final String notAttempted = "NOT_ATTEMPTED", attempted = "ATTEMPTED", skipped = "SKIPPED", bookmarked = "BOOKMARKED",not_confirmed = "NOT_CONFIRMED",confirmed = "CONFIRMED";
    EncryptDecrypt encObj;
    RecyclerView question_scroll;
    ScrollGridAdapter scrollAdapter;
    QuestionListAdapter qAdapter;
    LinearLayoutManager myLayoutManager;
    ArrayAdapter adapter;
    RecyclerView rv_option;
    ArrayList<String> categories;
    private static final String TAG = "AssessmentTestActivity";
    ArrayList<Integer> oplist = new ArrayList<>();
    ArrayList<SingleOptions> optionsList = new ArrayList<>();
    ArrayList<SingleQuestionList> questionOpList = new ArrayList<>();
    ArrayList<ArrayList<SingleQuestionList>> listOfLists = new ArrayList<>();
    ImageView question_img ,menu,report;
    Bundle bundle;
    Button btn_group_info, btn_qadditional, btn_review, btn_prev, btn_next, btn_clear_option, btn_mark,btn_confirm;
    Cursor c;
    AlertDialog alertDialog;
    Bitmap b, op, bitmap;
    Boolean flag = false;
    final Boolean edit = true;
    public static AssessmentTestActivity Aactivity;
    public static final int RequestPermissionCode = 1;
    static int index = 0, pos = 0, max = 1, grp = 0, size, count = 0;
    JSONObject questionobj, temp;
    public static JSONObject attempt;
    JSONArray array, optionsArray, groupArray, sectionArray, attemptsectionarray, buffer;
    SingleOptions option;
    SingleQuestionList qListObj;
    OptionsCheckAdapter opAdapter;
    SaveJSONdataToFile save;
    DBHelper dataObj;
    long millisStart = 0, millisRemaining = 0;
    GestureDetector gd;
    String cur_questionNo="";
    String report_message="";
    Spinner sp_report;
    CountDownTimer coundown_timer;
    boolean test_completion_status=false;

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
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
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

        setContentView(R.layout.activity_assessmenttest);

        dataObj = new DBHelper(this);
        restoredprefs = getSharedPreferences("SERVERPREF", MODE_PRIVATE);
        restoredsname = restoredprefs.getString("servername","main_server");
        if(restoredsname.equalsIgnoreCase("main_server")){
            finalUrl=URLClass.hosturl;
        }else{
            serverId=dataObj.getServerId(restoredsname);
            finalUrl="http://"+serverId+URLClass.loc_hosturl;
        }


        //Aactivity = this;
//        dataObj.Destroy("attempt_data");

        question_scroll = findViewById(R.id.question_scroll);
        question_img = findViewById(R.id.question_img);
        btn_prev = findViewById(R.id.prev_btn);
        btn_next = findViewById(R.id.next_btn);
        btn_clear_option = findViewById(R.id.btn_clear_option);
        btn_mark = findViewById(R.id.btn_mark);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_group_info = findViewById(R.id.btn_group_info);
        btn_qadditional = findViewById(R.id.btn_qadditional);
        btn_review = findViewById(R.id.btn_review_info);
        timer = findViewById(R.id.timer);
        sections = findViewById(R.id.sections);
        q_no = findViewById(R.id.tv_Question_no);
        qno_label = findViewById(R.id.tv_Question_no_label);
        fullscreen = findViewById(R.id.fullscreen);
        menu = findViewById(R.id.menu);
        report=findViewById(R.id.areport);


        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        qno_label.setTypeface(font);
        btn_confirm.setTypeface(font);
        btn_mark.setTypeface(font);
        btn_clear_option.setTypeface(font);
        btn_next.setTypeface(font);
        btn_prev.setTypeface(font);
        btn_qadditional.setTypeface(font);
        btn_group_info.setTypeface(font);
        btn_review.setTypeface(font);

        qAdapter = new QuestionListAdapter(questionOpList, AssessmentTestActivity.this, getScreenSize());
        myLayoutManager = new LinearLayoutManager(AssessmentTestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        question_scroll.setLayoutManager(myLayoutManager);
        question_scroll.setItemAnimator(new DefaultItemAnimator());
        question_scroll.setAdapter(qAdapter);
        testid = getIntent().getStringExtra("test");
        studentId = getIntent().getStringExtra("studentid");
        enrollid = getIntent().getStringExtra("enrollid");
        instanceId = getIntent().getStringExtra("instanceid");
        rv_option = findViewById(R.id.option_view);

        if(checkPermission()){

        }else{
            requestPermission();
        }

        Cursor cursor = dataObj.getSingleAssessmentTests(studentId,enrollid,testid);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("satu_ID")).equals(testid)) {
                    courseid=cursor.getString(cursor.getColumnIndex("satu_course_id"));
                    subjectId=cursor.getString(cursor.getColumnIndex("satu_subjet_ID"));
                    paperid=cursor.getString(cursor.getColumnIndex("satu_paper_ID"));
                    satu_Test_Time = cursor.getString(cursor.getColumnIndex("satu_Test_Time"));
                    satu_Test_Type = cursor.getString(cursor.getColumnIndex("satu_Test_Type"));
                }
            }

        }
        Log.e("InstanceData",instanceId);
        save = new SaveJSONdataToFile();
        path = enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + testid + "/ENC/";
        photoPath = URLClass.mainpath + path;
        //jsonPath = URLClass.mainpath + path + testid + ".json";
        imgPath=URLClass.mainpath+enrollid+"/"+courseid+"/";

        temp = new JSONObject();
        sectionArray = new JSONArray();
        attempt = new JSONObject();

        gd = new GestureDetector(AssessmentTestActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                initiateSingleImageWindow(b);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


        });

        try {
            //if cursor has values then the test is being resumed and data is retrieved from database
            Log.e("AssesmentTestActivity",""+getIntent().getStringExtra("status"));
            if (getIntent().getStringExtra("status").equalsIgnoreCase("NEW")) {
                newTest();
            }
           /* else {
                c = dataObj.getAttempt(dataObj.getLastTestAttempt(testid,studentId));
//                Log.e("TestingJson", ""+c.getInt(c.getColumnIndex("Attempt_Status"))+" "+c.getCount());
                millisStart = c.getLong(c.getColumnIndex("Assessment_RemainingTime"));
                attempt = new JSONObject(getIntent().getStringExtra("json"));
                attemptsectionarray = new JSONArray();
                attemptsectionarray = attempt.getJSONArray("Sections");
                Log.e("sectionarray",""+attemptsectionarray.length());
                restoreSections(dataObj.getQuestionStatus(testid), attempt);
//                buffer = generateArray(attempt.getJSONArray("Sections").getJSONObject(pos));
                index = c.getInt(c.getColumnIndex("Assessment_LastQuestion"));
                pos = c.getInt(c.getColumnIndex("Assessment_LastSection"));
            }*/
            //inserting new Test record in local database
            long ret = dataObj.InsertAssessment(attempt.getString("atu_ID"),instanceId,enrollid,studentId,courseid,subjectId,paperid,1,null, 0,dataObj.getAssessmentQuestionAttempted(testid,instanceId,studentId),dataObj.getAssessmentQuestionSkipped(testid,instanceId,studentId),dataObj.getAssessmentQuestionBookmarked(testid,instanceId,studentId),dataObj.getAssessmentQuestionNotAttempted(testid,instanceId,studentId), 0, millisRemaining, index, pos);
            Log.e("Insertion",""+instanceId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sections.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sections.setAdapter(adapter);

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(v);
            }
        });


        //menu window button
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateMenuWindow(v);
            }
        });

        //report window button
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateReportWindow(v);
            }
        });

        //clear option button
        btn_clear_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opAdapter.resetOptionsList();
//                ((SimpleItemAnimator) rv_option.getItemAnimator()).setSupportsChangeAnimations(false);
                opAdapter.notifyDataSetChanged();
                clearOptions();
                setQBackground(pos,index);
//                writeOption(opAdapter.getSelectedItem());
                btn_confirm.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
            }
        });

        //mark question and move to next button
        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opAdapter.getSelectedItem() > -1) {
                    listOfLists.get(pos).get(index).setQ_status(bookmarked);
                    listOfLists.get(pos).get(index).setQ_check(confirmed);
                    writeOption(opAdapter.getSelectedItem());
                    setQBackground(pos,index);
//                    index++;
//                    gotoQuestion(index);
                    btn_confirm.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
                } else
                    Toast.makeText(AssessmentTestActivity.this, "No option Selected", Toast.LENGTH_LONG).show();
            }
        });

        //confirmation button
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opAdapter.getSelectedItem() > -1) {
                    listOfLists.get(pos).get(index).setQ_status(attempted);
                    listOfLists.get(pos).get(index).setQ_check(confirmed);
                    writeOption(opAdapter.getSelectedItem());
                    setQBackground(pos, index);
                    btn_confirm.setBackgroundColor(Color.GREEN);
                } else
                    Toast.makeText(AssessmentTestActivity.this, "No option Selected", Toast.LENGTH_LONG).show();
            }
        });

        //additional infromation popup button
        btn_qadditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionobj = array.getJSONObject(index);
                    String pid=questionobj.getString("qbm_Paper_ID");
                    String cid=questionobj.getString("qbm_ChapterID");
                    String sid=questionobj.getString("qbm_SubjectID");
                    b =getTheEncriptedImage(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+questionobj.getString("qbm_image_file"));
                    bitmap =getTheEncriptedImage(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+questionobj.getString("qbm_QAdditional_Image"));

                   // b = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("qbm_image_file"));
                    //bitmap = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("qbm_QAdditional_Image"));
                    initiateFullScreenWindow(b, bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //group infromation popup button
        btn_group_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionobj = array.getJSONObject(index);
                    if (groupId.equals(questionobj.getString("gbg_id"))) {
                        String pid=questionobj.getString("qbm_Paper_ID");
                        String cid=questionobj.getString("qbm_ChapterID");
                        String sid=questionobj.getString("qbm_SubjectID");
                        op =getTheEncriptedImage(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+questionobj.getString("gbg_media_file"));

                        //op = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("gbg_media_file"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initiateSingleImageWindow(op);
            }
        });

        //next question button
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_view = v;
                try {
                    flag = true;
                    buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
                    if (index <= buffer.length()) {
                        if (index < buffer.length() - 1) {
                            if (opAdapter.getSelectedItem() > -1) {
                                if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                                    Log.e("if condition", listOfLists.get(pos).get(index).getQ_check());
                                    setQBackground(pos, index);
                                    writeOption(opAdapter.getSelectedItem());
                                    index++;
                                } else {
                                    btn_mark.callOnClick();
                                    index++;
                                }
                            } else {
                                Log.e("else condition", "reached");
                                setQBackground(pos, index);
                                writeOption(opAdapter.getSelectedItem());
                                index++;
                            }
                            setQuestion(pos, index, edit);
                            checkRadio();
                        } else if ((index == buffer.length() - 1) && (pos<attemptsectionarray.length()-1)) {
                            //Change button once last question of test is reached

                            if (opAdapter.getSelectedItem() > -1) {
                                if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                                    Log.e("if condition", listOfLists.get(pos).get(index).getQ_check());
                                    setQBackground(pos, index);
                                    writeOption(opAdapter.getSelectedItem());
                                    index++;
                                } else {
                                    btn_mark.callOnClick();
                                    index++;
                                }
                            } else {
                                Log.e("else condition", "reached");
                                setQBackground(pos, index);
                                writeOption(opAdapter.getSelectedItem());
                                index++;
                            }

                            flag = false;
                            pos = pos + 1;
                            index=0;
                            //qAdapter.setData(index);
                            Log.e("POS", "" + pos + "," + index);
                            //buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
                            sections.setSelection(pos);
                            //setScrollbar(pos);
                            scrollAdapter.updateList(listOfLists.get(pos));

                            setQuestion(pos, index, edit);
                            checkRadio();


                        }else{
                            if (opAdapter.getSelectedItem() > -1) {
                                if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                                    Log.e("if condition", listOfLists.get(pos).get(index).getQ_check());
                                    setQBackground(pos, index);
                                    writeOption(opAdapter.getSelectedItem());
                                } else {
                                    btn_mark.callOnClick();
                                }
                            } else {
                                Log.e("else condition", "reached");
                                setQBackground(pos, index);
                                writeOption(opAdapter.getSelectedItem());
                            }
                            Log.e("END", "POS:" + pos + ",attemptsectionarray" + attemptsectionarray.length());
                            if (pos >= attemptsectionarray.length()-1) {
                                btn_next.setText("Finish");
                                //writeOption(opAdapter.getSelectedItem());
                                AlertDialog alertbox = new AlertDialog.Builder(AssessmentTestActivity.this)
                                        .setMessage("Do you want to finish Test?" + " " + dataObj.getAssessmentQuestionCount(testid))
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
//                                            q_list.clear();
                                                test_completion_status=true;
                                                stopTest(test_completion_status);

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                mHideRunnable.run();
                                                btn_next.setText("Next");
                                            }
                                        })
                                        .show();
                                alertbox.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        mHideRunnable.run();
                                        btn_next.setText("Next");
                                    }
                                });
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //back question button
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionobj = array.getJSONObject(index);
                    if (index > 0) {
                        if (opAdapter.getSelectedItem() > -1) {
                            if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                                Log.e("if condition", listOfLists.get(pos).get(index).getQ_check());
                                setQBackground(pos, index);
                                writeOption(opAdapter.getSelectedItem());
                                index--;
                            } else {
                                btn_mark.callOnClick();
                                index--;
                            }
                        } else {
                            Log.e("else condition", "reached");
                            setQBackground(pos, index);
                            writeOption(opAdapter.getSelectedItem());
                            index--;
                        }
                        flag = true;

                    } else if (index == 0 && pos > 0) {

                        if (opAdapter.getSelectedItem() > -1) {
                            if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                                Log.e("if condition", listOfLists.get(pos).get(index).getQ_check());
                                setQBackground(pos, index);
                                writeOption(opAdapter.getSelectedItem());
                                index--;
                            } else {
                                btn_mark.callOnClick();
                                index--;
                            }
                        } else {
                            Log.e("else condition", "reached");
                            setQBackground(pos, index);
                            writeOption(opAdapter.getSelectedItem());
                            index--;
                        }

                        pos = pos - 1;
                        index = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions").length()-1;
                        flag = false;
                        sections.setSelection(pos);
                        scrollAdapter.updateList(listOfLists.get(pos));
                        //setScrollbar(pos);
                        Log.e("POS", "" + pos + "," + index);

                    }
                    setQuestion(pos, index, edit);
                    checkRadio();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //Question number scrollbar
        question_scroll.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), question_scroll, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int in) {
                if (opAdapter.getSelectedItem() > -1) {
                    if (listOfLists.get(pos).get(index).getQ_check().equalsIgnoreCase(confirmed)) {
                        Log.e("scroll_if", listOfLists.get(pos).get(index).getQ_check());
                        setQBackground(pos, index);
                        writeOption(opAdapter.getSelectedItem());
                        gotoQuestion(in);
                    } else {
                        btn_mark.callOnClick();
                        gotoQuestion(in);
                    }
                } else {
                    Log.e("scroll_else","reached");
                    setQBackground(pos, index);
                    writeOption(opAdapter.getSelectedItem());
                    gotoQuestion(in);
                }
                Log.e("Scroll_Index",""+in);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        coundown_timer= new CountDownTimer(millisStart, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timer.setText(text);
                millisRemaining = millisUntilFinished;
            }

            public void onFinish() {
                //timer.setText("Time Up!");
                test_completion_status=true;
                stopTest(test_completion_status);

            }
        }.start();

        //start the bagroundtask to upload the Assessment test data to server
        startRepeatingTask();

    }

    //stop the test using test_completion_status=true flag
    public  void stopTest(boolean test_completion_status){

        if(test_completion_status) {
            try {
                long value = dataObj.UpdateAssessment(attempt.getString("atu_ID"), instanceId, enrollid, "", courseid, subjectId, paperid, 2, "", 0.0, dataObj.getAssessmentQuestionAttempted(testid, instanceId, studentId), dataObj.getAssessmentQuestionSkipped(testid, instanceId, studentId), dataObj.getAssessmentQuestionBookmarked(testid, instanceId, studentId), dataObj.getAssessmentQuestionNotAttempted(testid, instanceId, studentId), 0.0, millisRemaining, index, pos);
                Log.e("Update", "" + instanceId);
                if (value <= 0) {
                    Log.e("PaperId: ", "pid  " + paperid);
                    long ret = dataObj.InsertAssessment(attempt.getString("atu_ID"), instanceId, enrollid, studentId, courseid, subjectId, paperid, 2, null, 0.0, dataObj.getAssessmentQuestionAttempted(testid, instanceId, studentId), dataObj.getAssessmentQuestionSkipped(testid, instanceId, studentId), dataObj.getAssessmentQuestionBookmarked(testid, instanceId, studentId), dataObj.getAssessmentQuestionNotAttempted(testid, instanceId, studentId), 0.0, millisRemaining, index, pos);
                    Log.e("Insertion", "" + instanceId);
                }
                SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + testid + ".json", attempt.toString());
//                                                    syncAssesmentTestData();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(AssessmentTestActivity.this, finish_view, "transition");
            int revealX = (int) (finish_view.getX() + finish_view.getWidth() / 2);
            int revealY = (int) (finish_view.getY() + finish_view.getHeight() / 2);
            finish();
            count = dataObj.getAttempCount(studentId);

            try {
                syncAssesmentTestData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            coundown_timer.cancel();
            stopRepeatingTask();       //stop the baground task for uploading assessment data to server
            AssessmentTestActivity.this.finish();
            Intent intent = new Intent(AssessmentTestActivity.this, ScoreActivity.class);
            bundle = new Bundle();

//                                                bundle.putString("JSON", attempt.toString());
//                                                Log.e("testAssessment",attempt.toString());
            bundle.putString("enrollid",enrollid);
            bundle.putString("courseid", courseid);
            bundle.putString("subjectid", subjectId);
            bundle.putString("paperid",paperid);
            bundle.putString("Type","ASSESSMENT");
            intent.putExtra("BUNDLE", bundle);
            intent.putExtra("testid",testid);
            intent.putExtra("instanceid", instanceId);
            intent.putExtra("JSON", attempt.toString());
            intent.putExtra("studentid", studentId);
            intent.putExtra("Xreveal", revealX);
            intent.putExtra("Yreveal", revealY);
            ActivityCompat.startActivity(AssessmentTestActivity.this, intent, options.toBundle());
        }
//                timer.setVisibility(View.INVISIBLE);
    }

    public void newTest() throws JSONException {
        millisStart = Integer.parseInt(satu_Test_Time)*60000;
        attemptsectionarray = new JSONArray();
        max = 1;
         attempt = new JSONObject(getIntent().getStringExtra("JSON"));
        attemptsectionarray = attempt.getJSONArray("Sections");
        Log.e("Sections", "" + attemptsectionarray.length());
        index = 0;
        pos = 0;
        Cursor cur = dataObj.getAssessmentTestData(testid,instanceId,studentId,enrollid);
        if(cur.getCount() > 0){
            while (cur.moveToNext()){
                orgid = cur.getString(cur.getColumnIndex("satu_org_id"));
                batchid = cur.getString(cur.getColumnIndex("satu_batch"));
                branchid = cur.getString(cur.getColumnIndex("satu_branch_id"));

            }
        }
        Log.e("AssessmentTestActivity","orgid:"+orgid+",batchid:"+batchid+",branchid:"+branchid);
//        buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
        storeSections();
    }

    public String generateUniqueId(String qId){
        String QuestionKey ="";
        QuestionKey = testid.concat("_"+qId);
        Log.e("Question Key", QuestionKey);
        return QuestionKey;
    }

    //method to display selected question
    public void gotoQuestion(int in) {
        try {

            buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            index = in;
            setQuestion(pos, index, edit);
            checkRadio();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to dynamically request permissions
    private void requestPermission() {
        ActivityCompat.requestPermissions(AssessmentTestActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    //method to get the deivce screen size
    public int getScreenSize() {
        size = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        return size;
    }

    //method to set the background of the question nuumber in the scroll bar
    public void setQBackground(int pos, int index) {

        if (opAdapter.getSelectedItem() == -1) {

                listOfLists.get(pos).get(index).setQ_status(skipped);
                listOfLists.get(pos).get(index).setQ_check(not_confirmed);

        } else {
            if(!listOfLists.get(pos).get(index).getQ_status().equalsIgnoreCase(bookmarked)) {
                Log.e("status", listOfLists.get(pos).get(index).getQ_status());
                listOfLists.get(pos).get(index).setQ_status(attempted);
                listOfLists.get(pos).get(index).setQ_check(confirmed);
            }else{
                listOfLists.get(pos).get(index).setQ_status(bookmarked);
            }
        }
        qAdapter.updateList(listOfLists.get(pos));
    }

    //method to check if permission is already granted
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //method to set a horizontal scrollbar containing question numbers of the current section
    public void setScrollbar(int position) throws JSONException {
//        questionOpList = new ArrayList<>();
        pos = position;
        Log.e("Scrollbar", "" + pos);
        qAdapter.updateList(listOfLists.get(pos));
    }

    //method to create a popup window containing question numbers
    public void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) AssessmentTestActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View view = inflater.inflate(R.layout.popup_screen,(ViewGroup) findViewById(R.id.popup_element));
            //Initialise an dialogbuilder to create an alertdialog
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            //Set the layout to the alertDialog
            dialogBuilder.setView(view);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            final Button cancelButton = (Button) view.findViewById(R.id.pop_close_button);
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    hide();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    mHideRunnable.run();
                }
            });
            //Instantiate grid view
            gridView = view.findViewById(R.id.scroll_grid);
            //Setting Adapter to gridview
            gridView.setAdapter(scrollAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setQBackground(pos, index);
                    writeOption(opAdapter.getSelectedItem());
                    gotoQuestion(position);
                    index = position;
                    cancelButton.callOnClick();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to check the radiobutton based on its position in the group
    public void checkRadio() {

        try {
            buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Log.e("SelectedOption",""+ dataObj.getAssessmentPosition(Id,testid));
            if (dataObj.getAssessmentPosition(Id,testid) > -1) {

                opAdapter.setOptionsList(dataObj.getAssessmentPosition(Id,testid));
                rv_option.setItemAnimator(null);
                opAdapter.notifyDataSetChanged();
            }

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    //method to store the selected option in the local database
    public void writeOption(int indx) {
        try {
            long result = -1;
            buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
            Id = buffer.getJSONObject(index).getString("qbm_ID");

//            Log.e("writeOption--->",""+Id);
//            Seq = buffer.getJSONObject(index).getString("qbm_SequenceID");
            questionobj = buffer.getJSONObject(index);
//            scrollAdapter.updateList(listOfLists.get(pos));

            double qbm_marks=Double.parseDouble(questionobj.getString("qbm_marks"));
            double qbm_negative_mrk=Double.valueOf(questionobj.getString("qbm_negative_mrk"));
            Double obtained_marks=0.0;
            int correct_option_seq=-1;
            JSONArray ja_option=questionobj.getJSONArray("Options");
            double neg_marks=qbm_negative_mrk;
            for (int o=0;o<ja_option.length();o++)
            {
                JSONObject jo_option=(JSONObject)ja_option.get(o);
                if(jo_option.getString("qbo_answer_flag").equalsIgnoreCase("YES"))
                {
                    correct_option_seq=Integer.parseInt(jo_option.getString("qbo_seq_no").toString());
                }
            }




            String atu_ID=attempt.getString("atu_ID");
            String q_status=listOfLists.get(pos).get(index).getQ_status();



            Log.e("condition",""+dataObj.AssessmentCheckQuestion(Id));
            if (dataObj.AssessmentCheckQuestion(Id)) {

                if (indx > -1) {

                    int sel_seq=Integer.parseInt(opAdapter.getSelectedSequence());

                    if(sel_seq==(correct_option_seq))
                    {
                        obtained_marks=qbm_marks;
                        neg_marks=0.0;
                    }else{
                        obtained_marks=0.0;
                        neg_marks=qbm_negative_mrk;
                    }
                    Log.e("writeOption","obtained_marks:"+obtained_marks);
                    Log.e("writeOption","neg_marks:"+neg_marks);

                    result = dataObj.UpdateAssessmentQuestion(atu_ID,instanceId,studentId, Id,orgid,branchid,batchid, obtained_marks, neg_marks, indx, q_status,"NotUploaded",sel_seq);
                } /*else {
                    //if question is attempted and then the option is cleared store as skipped
                    result = dataObj.UpdateAssessmentQuestion(atu_ID,instanceId,key,studentId, Id,orgid,branchid,batchid, seq_id,sec_name,chap_name,sub_cat_name, qbm_marks,qbm_negative_mrk, obtained_marks, flag_neg, indx, q_status,"NotUploaded", opt_select_seq, correct_option_seq);
                }*/
                /*if (result == 0) {
                    dataObj.InsertAssessmentQuestion(atu_ID,instanceId,key,studentId, Id,orgid,branchid,batchid, seq_id,sec_name,chap_name,sub_cat_name, qbm_marks,qbm_negative_mrk, obtained_marks, flag_neg, indx,ja_option.length(), q_status,"NotUploaded", opt_select_seq, correct_option_seq);
                }*/

                Log.e("AssessmenttestActivity","writeOption"+attempt.getString("atu_ID"));
                Log.e("AssessmenttestActivity","writeOption"+instanceId);
                Log.e("AssessmenttestActivity","writeOption"+generateUniqueId(Id));
                Log.e("AssessmenttestActivity","writeOption"+studentId);
                Log.e("AssessmenttestActivity","writeOption"+Id);
                Log.e("AssessmenttestActivity","writeOption"+orgid);
                Log.e("AssessmenttestActivity","writeOption"+branchid);
                Log.e("AssessmenttestActivity","writeOption"+batchid);
                Log.e("AssessmenttestActivity","writeOption"+questionobj.getString("qbm_SequenceID"));
                Log.e("AssessmenttestActivity","writeOption"+attempt.getJSONArray("Sections").getJSONObject(pos).getString("atu_section_name"));
                Log.e("AssessmenttestActivity","writeOption"+questionobj.getString("qbm_ChapterName"));
                Log.e("AssessmenttestActivity","writeOption"+questionobj.getString("qbm_Sub_CategoryName"));
                Log.e("AssessmenttestActivity","writeOption"+Integer.valueOf(questionobj.getString("qbm_marks")));
                Log.e("AssessmenttestActivity","writeOption"+Double.valueOf(questionobj.getString("qbm_negative_mrk")));
                Log.e("AssessmenttestActivity","writeOption"+indx);
                Log.e("AssessmenttestActivity","writeOption"+listOfLists.get(pos).get(index).getQ_status());
                Log.e("AssessmenttestActivity","writeOption"+"NotUploaded");
                Log.e("AssessmenttestActivity","writeOption"+opAdapter.getSelectedSequence());
                Log.e("AssessmenttestActivity","writeOption"+opAdapter.getFlag());

                Log.e("Selected_option","ID: "+indx);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to clear the selected options in the local database
    public void clearOptions() {
        try {
            buffer = attempt.getJSONArray("Sections");
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Seq = buffer.getJSONObject(index).getString("qbm_SequenceID");
            questionobj = buffer.getJSONObject(index);
            long value = dataObj.UpdateAssessmentQuestion(attempt.getString("atu_ID"),instanceId,studentId, Id,orgid,branchid,batchid,0, 0, -1, notAttempted,"NotUploaded",-1);
            if(value > 0){
                listOfLists.get(pos).get(index).setQ_status(notAttempted);
                listOfLists.get(pos).get(index).setQ_check(not_confirmed);
                btn_confirm.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to create a report window
    public void initiateReportWindow(View v) {

        String[] s = { "QUASTION", "OPTION", "ANSWER", "EXPLINATION", "ADL_INFO", "GRP_INFO","REV_INFO","OTHERS"};
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(AssessmentTestActivity.this,
                android.R.layout.simple_dropdown_item_1line, s);

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.activity_alert_report, null);
        //View view = infl.inflate(R.layout.activity_alert_report, null);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AssessmentTestActivity.this,R.style.NewDialog).create();

        TextView txt_question_id= (TextView) view.findViewById(R.id.txt_question_id);
        cur_questionNo=q_no.getText().toString();
        txt_question_id.setText(cur_questionNo);
        final EditText txt_reportMessage= (EditText) view.findViewById(R.id.txt_reportMessage);
        sp_report=view.findViewById(R.id.sp_report);
        //sp_report.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sp_report.setAdapter(adp);
        ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
        alertDialog.setView(view);


        Button btn_report= (Button) view.findViewById(R.id.btn_report);

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txt_reportMessage.setText("CourseId:"+courseid+", SubjectId:"+subjectId+", PaperId:"+paperid+", ChapterId:"+paperid);
                new AsyncCheckInternet(AssessmentTestActivity.this,new INetStatus(){
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){
                            HashMap<String,String> hmap=new HashMap<>();
                            hmap.put("QUASTION_ID",cur_questionNo);
                            hmap.put("COURSE_ID",courseid);
                            hmap.put("SUBJECT_ID",subjectId);
                            hmap.put("PAPER_ID",paperid);
                            hmap.put("CHAPTER_ID",paperid);
                            hmap.put("ACTIVITY_TYPE","ASESSMENT_TEST");
                            hmap.put("ISSUE_TYPE",sp_report.getSelectedItem().toString());
                            report_message=txt_reportMessage.getText().toString();
                            hmap.put("ISSUE_MESSAGE",report_message);
                            hmap.put("REPORTED_BY",studentId);

                            new BagroundTask(URLClass.hosturl +"insert_report.php",hmap,AssessmentTestActivity.this, new IBagroundListener() {

                                @Override
                                public void bagroundData(String json) {
                                    if(json.equalsIgnoreCase("Report_NOT_Inserted")){
                                        Toast.makeText(getApplicationContext(),"Report not submitted,please try again..",Toast.LENGTH_SHORT).show();
                                    }else {
                                        alertDialog.cancel();
                                        Toast.makeText(getApplicationContext(),"Report  submitted,Thank you for your feedback..",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute();
                        }else {
                            Toast.makeText(getApplicationContext(),"No internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();

    }


    //method to create a menu window
    public void initiateMenuWindow(View v) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) AssessmentTestActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.menu, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        TextView tv_answered_count = layout.findViewById(R.id.tv_answered_count);
        TextView tv_answered_count_label = layout.findViewById(R.id.tv_answered_count_label);
        TextView tv_skipped_count = layout.findViewById(R.id.tv_skipped_count);
        TextView tv_skipped_count_label = layout.findViewById(R.id.tv_skipped_count_label);
        TextView tv_bookmarked_count = layout.findViewById(R.id.tv_bookmarked_count);
        TextView tv_bookmarked_count_label = layout.findViewById(R.id.tv_bookmarked_count_label);
        TextView tv_not_attempted_count = layout.findViewById(R.id.tv_not_attempted_count);
        TextView tv_not_attempted_count_label = layout.findViewById(R.id.tv_not_attempted_count_label);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        tv_answered_count_label.setTypeface(font);
        tv_skipped_count_label.setTypeface(font);
        tv_bookmarked_count_label.setTypeface(font);
        tv_not_attempted_count_label.setTypeface(font);

        tv_answered_count.setText("" + dataObj.getAssessmentQuestionAttempted(testid,instanceId,studentId));
        tv_skipped_count.setText("" + dataObj.getAssessmentQuestionSkipped(testid,instanceId,studentId));
        tv_bookmarked_count.setText("" + dataObj.getAssessmentQuestionBookmarked(testid,instanceId,studentId));
        tv_not_attempted_count.setText("" + dataObj.getAssessmentQuestionNotAttempted(testid,instanceId,studentId));
        Button finish = layout.findViewById(R.id.finish_button);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                AlertDialog alertbox = new AlertDialog.Builder(AssessmentTestActivity.this)
                        .setMessage("Do you want to finish Test?" + " " + dataObj.getAssessmentQuestionCount(testid))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
//                                            q_list.clear();
                                test_completion_status=true;
                                stopTest(test_completion_status);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                mHideRunnable.run();
                                btn_next.setText("Next");
                            }
                        })
                        .show();

            }
        });
        Button cancelButton = layout.findViewById(R.id.close_button);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                mHideRunnable.run();
            }
        });
    }

    //method to generate a window alertbox to display additional information for questions
    public void initiateFullScreenWindow(Bitmap qbitmap, Bitmap abitmap) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) AssessmentTestActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fullscreen, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        ImageView qimg = layout.findViewById(R.id.question_full_img);
        ImageView aimg = layout.findViewById(R.id.iv_additional_img);
        qimg.setImageBitmap(qbitmap);
        aimg.setImageBitmap(abitmap);

        ImageView cancel = layout.findViewById(R.id.iv_close);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hide();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                hide();
            }
        });
    }

    public void initiateSingleImageWindow(Bitmap b) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) AssessmentTestActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.singlescreen, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        ImageView limg = layout.findViewById(R.id.layout_img);
        limg.setImageBitmap(b);
        ImageView cancel = layout.findViewById(R.id.iv_close);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hide();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                hide();
            }
        });
    }

    //method to set the questions and its options
    public void setQuestion(int pos, int index, Boolean edit) throws JSONException {

        array = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
        myLayoutManager.scrollToPositionWithOffset(index, 400);
        questionobj = array.getJSONObject(index);
        Log.e("CurrentIndex",""+index);
        q_no.setText(questionobj.getString("qbm_ID"));
        if (questionobj.getString("qbm_group_flag").equals("YES")) {
            groupId = questionobj.getString("gbg_id");
            btn_group_info.setEnabled(true);
            btn_group_info.setClickable(true);
            btn_group_info.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_group_info.setEnabled(false);
            btn_group_info.setClickable(false);
            btn_group_info.setBackgroundColor(0);
        }
        qAdapter.setPointer(index);
        questionobj = array.getJSONObject(index);
        if (questionobj.getString("qbm_QAdditional_Flag").equals("YES")) {
            btn_qadditional.setEnabled(true);
            btn_qadditional.setClickable(true);
            btn_qadditional.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_qadditional.setEnabled(false);
            btn_qadditional.setClickable(false);
            btn_qadditional.setBackgroundColor(0);

        }
        if(listOfLists.get(pos).get(index).getQ_status().equalsIgnoreCase("ATTEMPTED")){
            btn_confirm.setBackgroundColor(Color.GREEN);
        }else{
            btn_confirm.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        }
        if (edit) {
            btn_review.setEnabled(false);
            btn_review.setClickable(false);
            btn_review.setBackgroundColor(0);

        }
        String pid=questionobj.getString("qbm_Paper_ID");
        String cid=questionobj.getString("qbm_ChapterID");
        String sid=questionobj.getString("qbm_SubjectID");
        Bitmap b =getTheEncriptedImage(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+questionobj.getString("qbm_image_file"));
        //Bitmap b = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+questionobj.getString("qbm_image_file"));
        Log.e("qimage", photoPath + questionobj.getString("qbm_image_file"));
        question_img.setImageBitmap(b);
/*        Animation fadeimage = AnimationUtils.loadAnimation(PracticeTestActivity.this, R.anim.fade_in);
        question_img.startAnimation(fadeimage);*/
/*        question_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });*/

        optionsArray = questionobj.getJSONArray("Options");
        optionsList = new ArrayList<>();
        for (int i = 0; i < optionsArray.length(); i++) {
            option = new SingleOptions();
            option.setQbo_id(optionsArray.getJSONObject(i).getString("qbo_id"));
            option.setQbo_media_file(imgPath+sid+"/"+pid+"/"+cid+"/ENC/"+optionsArray.getJSONObject(i).getString("qbo_media_file"));
            option.setQbo_seq_no(optionsArray.getJSONObject(i).getString("qbo_seq_no"));
            option.setQbo_answer_flag(optionsArray.getJSONObject(i).getString(("qbo_answer_flag")));
            optionsList.add(option);
        }

        try {
            opAdapter = new OptionsCheckAdapter(optionsList, AssessmentTestActivity.this, photoPath, rv_option);
            Log.e("opSize", ""+oplist.size());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rv_option.setLayoutManager(mLayoutManager);
            rv_option.setItemAnimator(new DefaultItemAnimator());
            rv_option.setAdapter(opAdapter);
            opAdapter.setOptionsEditable(edit);
            runLayoutAnimation(rv_option);
            opAdapter.notifyDataSetChanged();
            buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Log.e("SelectedOption",""+ dataObj.getAssessmentPosition(Id,testid));
            if (dataObj.getAssessmentPosition(Id,testid) > -1) {

                opAdapter.setOptionsList(dataObj.getAssessmentPosition(Id,testid));
                rv_option.setItemAnimator(null);
                opAdapter.notifyDataSetChanged();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    //method to store the number of questions in each section
  public void storeSections() {
        categories = new ArrayList<String>();
        try {
            listOfLists = new ArrayList<>();
            JSONArray ja_sec=attempt.getJSONArray("Sections");
            for (int i = 0; i < ja_sec.length(); i++) {
                JSONObject jo_sec=(JSONObject) ja_sec.get(i);
                String sec_name=jo_sec.getString("atu_section_name");
                categories.add(sec_name);
                JSONArray array2 = jo_sec.getJSONArray("Questions");
                questionOpList = new ArrayList<>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject questionobj=array2.getJSONObject(j);
                    Id = questionobj.getString("qbm_ID");

                    String correct_option_seq=null;
                    JSONArray ja_option=questionobj.getJSONArray("Options");
                    for (int o=0;o<ja_option.length();o++)
                    {
                        JSONObject jo_option=(JSONObject)ja_option.get(o);
                        if(jo_option.getString("qbo_answer_flag").equalsIgnoreCase("YES"))
                        {
                            correct_option_seq=jo_option.getString("qbo_seq_no").toString();
                        }
                    }
                    double qbm_marks=Double.parseDouble(questionobj.getString("qbm_marks"));
                    double qbm_negative_mrk=Double.valueOf(questionobj.getString("qbm_negative_mrk"));

                    String atu_ID=attempt.getString("atu_ID");
                    String key=generateUniqueId(Id);
                    String seq_id=questionobj.getString("qbm_SequenceID");

                    String chap_name=questionobj.getString("qbm_ChapterName");
                    String sub_cat_name=questionobj.getString("qbm_Sub_CategoryName");
//                    Seq = array2.getJSONObject(j).getString("qbm_SequenceID");
                    Log.e(TAG,"SequenceId:"+array2.getJSONObject(j).getString("qbm_SequenceID"));

                    qListObj = new SingleQuestionList(array2.getJSONObject(j).getString("qbm_SequenceID"), notAttempted,not_confirmed);
                    long value = dataObj.InsertAssessmentQuestion(atu_ID,instanceId,key,studentId, Id, orgid,branchid,batchid,seq_id,sec_name,chap_name,sub_cat_name, qbm_marks, qbm_negative_mrk, 0, 0, -1,ja_option.length() ,notAttempted,"NotUploaded", "-1", correct_option_seq);
                    Log.e("Insertion Init",""+value);
                    questionOpList.add(qListObj);
                }
                listOfLists.add(questionOpList);
            }
            Log.e("StoreSections", "" + categories.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to store the number of questions in each section{
    public void restoreSections(ArrayList<String> statusList, JSONObject attempt) {
        categories = new ArrayList<String>();
        try {
            int max = 0;
            listOfLists = new ArrayList<>();
            for (int i = 0; i < attempt.getJSONArray("Sections").length(); i++) {
                categories.add(attempt.getJSONArray("Sections").getJSONObject(i).getString("atu_section_name"));
                JSONArray array2 = attempt.getJSONArray("Sections").getJSONObject(i).getJSONArray("Questions");
                Log.e("sequence", "" + categories.size());
                questionOpList = new ArrayList<>();
                Log.e("array2", "" + array2.length());
                for (int j = 0; j < array2.length(); j++) {
                    if (statusList.get(i) != null) {
                        qListObj = new SingleQuestionList(array2.getJSONObject(j).getString("qbm_SequenceId"), statusList.get(max),not_confirmed);
                    }
                    max++;
                    questionOpList.add(qListObj);
                }
                listOfLists.add(questionOpList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit Test? You CANNOT resume Test ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        try {
                            long value = dataObj.UpdateAssessment(attempt.getString("atu_ID"),instanceId,enrollid,studentId,courseid,subjectId,paperid,1,"", 0.0,dataObj.getAssessmentQuestionAttempted(testid,instanceId,studentId),dataObj.getAssessmentQuestionSkipped(testid,instanceId,studentId),dataObj.getAssessmentQuestionBookmarked(testid,instanceId,studentId),dataObj.getAssessmentQuestionNotAttempted(testid,instanceId,studentId), 0.0, millisRemaining, index, pos);
                            Log.e("Update",""+instanceId);
                            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + testid + ".json", attempt.toString());
//                            syncAssesmentTestData();
                        } catch (JSONException|IOException e) {
                            e.printStackTrace();
                        }

                        stopRepeatingTask();       //stop the baground task for uploading assessment data to server
                        coundown_timer.cancel();

                        AssessmentTestActivity.this.finish();
                        Intent intent = new Intent(AssessmentTestActivity.this, ListofAssessmentTests.class);
                        intent.putExtra("enrollid",enrollid);
                        intent.putExtra("courseid", courseid);
                        intent.putExtra("paperid",paperid);
                        intent.putExtra("studentid",studentId);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        mHideRunnable.run();
                    }
                })
                .show();

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
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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
    protected void onPostResume() {
        mHideRunnable.run();
        super.onPostResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        try {
            Log.e("ItemSelected", "reached");
            mHideRunnable.run();
            pos = position;

            //Instantiate grid adapter
            scrollAdapter = new ScrollGridAdapter(AssessmentTestActivity.this, attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions"), listOfLists.get(pos), getScreenSize());
            setScrollbar(pos);
            if (flag) {
                index = 0;
                setQuestion(pos, index, edit);
            } else {
                setQuestion(pos, index, edit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        try {
            Log.e("NothingSelected", "reached");
            setScrollbar(0);
            pos = 0;
            mHideRunnable.run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart:");
        if (!checkPermission()) {
            requestPermission();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume:");
        dataObj = new DBHelper(AssessmentTestActivity.this);
//        int count = dataObj.getAttempCount()-1;
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause:");
        dataObj = new DBHelper(AssessmentTestActivity.this);
        try {
            long value = dataObj.UpdateAssessment(attempt.getString("atu_ID"),instanceId,enrollid,studentId,courseid,subjectId,paperid,1,"", 0.0,dataObj.getAssessmentQuestionAttempted(testid,instanceId,studentId),dataObj.getAssessmentQuestionSkipped(testid,instanceId,studentId),dataObj.getAssessmentQuestionBookmarked(testid,instanceId,studentId),dataObj.getAssessmentQuestionNotAttempted(testid,instanceId,studentId), 0.0, millisRemaining, index, pos);
            Log.e("Update",""+instanceId);
/*            if (value <= 0) {
                long ret = dataObj.InsertAttempt(generateUniqueId(),attempt.getString("ptu_test_ID"),1, 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                Log.e("Insertion",""+ret);
            }*/
            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + testid + ".json", attempt.toString());
        } catch (JSONException|IOException e) {
            e.printStackTrace();
        }


        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop:");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy:");
        super.onDestroy();
    }

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {

            try {
                syncAssesmentTestData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("AssessmentTestActivity","thread in run() Intervel.."+INTERVAL);

            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        Log.e("AssessmentTestActivity","thread started..");
        mHandlerTask.run();

    }

    void stopRepeatingTask()
    {
        if(mHandler!= null)
        {
            mHandler.removeCallbacks(mHandlerTask);
            Log.e("AssessmentTestActivity","thread stoped..");
        }
    }

    public  void syncAssesmentTestData()throws JSONException{

        JSONObject finalAssessmentObj=new JSONObject();
        //int count =0;
        //array = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
        Cursor mycursor=dataObj.getAssessmentQuestionToBeUploadData(studentId,testid,instanceId,"NotUploaded");
        Log.e("syncAssesmentTestData",""+mycursor.getCount());
        //Log.e("syncAssesmentTestData","array"+array.length());
        if(mycursor.getCount()>0){
            try{
                JSONArray AssessmentList = new JSONArray();
                JSONObject AssessmentTestQues;
                while (mycursor.moveToNext()){
                    AssessmentTestQues = new JSONObject();
                    AssessmentTestQues.put("StudentId",mycursor.getString(mycursor.getColumnIndex("StudentId")));
                    AssessmentTestQues.put("Org_ID",mycursor.getString(mycursor.getColumnIndex("Org_ID")));
                    AssessmentTestQues.put("Branch_ID",mycursor.getString(mycursor.getColumnIndex("Branch_ID")));
                    AssessmentTestQues.put("Batch_ID",mycursor.getString(mycursor.getColumnIndex("Batch_ID")));
                    AssessmentTestQues.put("Test_ID",mycursor.getString(mycursor.getColumnIndex("Test_ID")));
                    AssessmentTestQues.put("Assessment_Instance_ID",mycursor.getString(mycursor.getColumnIndex("Assessment_Instance_ID")));
                    AssessmentTestQues.put("Question_Key",mycursor.getString(mycursor.getColumnIndex("Question_Key")));
                    AssessmentTestQues.put("Question_ID",mycursor.getString(mycursor.getColumnIndex("Question_ID")));
                    AssessmentTestQues.put("Question_Seq_No",mycursor.getString(mycursor.getColumnIndex("Question_Seq_No")));
                    AssessmentTestQues.put("Question_Section",mycursor.getString(mycursor.getColumnIndex("Question_Section")));
                    AssessmentTestQues.put("Question_Category",mycursor.getString(mycursor.getColumnIndex("Question_Category")));
                    AssessmentTestQues.put("Question_SubCategory",mycursor.getString(mycursor.getColumnIndex("Question_SubCategory")));
                    AssessmentTestQues.put("Question_Max_Marks",mycursor.getDouble(mycursor.getColumnIndex("Question_Max_Marks")));
                    AssessmentTestQues.put("Question_Negative_Marks",mycursor.getDouble(mycursor.getColumnIndex("Question_Negative_Marks")));
                    AssessmentTestQues.put("Question_Marks_Obtained",mycursor.getDouble(mycursor.getColumnIndex("Question_Marks_Obtained")));
                    AssessmentTestQues.put("Question_Negative_Applied",mycursor.getDouble(mycursor.getColumnIndex("Question_Negative_Applied")));
                    AssessmentTestQues.put("Question_Option",mycursor.getInt(mycursor.getColumnIndex("Question_Option")));
                    AssessmentTestQues.put("Question_OptionCount",mycursor.getInt(mycursor.getColumnIndex("Question_OptionCount")));
                    AssessmentTestQues.put("Question_Status",mycursor.getString(mycursor.getColumnIndex("Question_Status")));
                    AssessmentTestQues.put("Question_Upload_Status",mycursor.getString(mycursor.getColumnIndex("Question_Upload_Status")));
                    AssessmentTestQues.put("Question_Option_Sequence",mycursor.getString(mycursor.getColumnIndex("Question_Option_Sequence")));
                    AssessmentTestQues.put("Option_Answer_Flag",mycursor.getString(mycursor.getColumnIndex("Option_Answer_Flag")));
                    /*if(array.getJSONObject(count).getString("qbm_ID").equalsIgnoreCase(mycursor.getString(mycursor.getColumnIndex("Question_ID")))){
                        Log.e("QID",array.getJSONObject(count).getString("qbm_ID"));
                        JSONArray optArray = array.getJSONObject(count).getJSONArray("Options");
                        String value="";
                        Double total = 0.0;
                        Log.e("syncAssesmentTestData","Question_OptionCount"+optArray.length());
                        AssessmentTestQues.put("Question_OptionCount",optArray.length());
                        for(int i = 0;i< optArray.length();i++){
                            if(optArray.getJSONObject(i).getString("qbo_answer_flag").equalsIgnoreCase("YES")){
                                Log.e("opSeq",optArray.getJSONObject(i).getString("qbo_seq_no"));
                                AssessmentTestQues.put("Option_Answer_Flag",optArray.getJSONObject(i).getString("qbo_seq_no"));
                                value = optArray.getJSONObject(i).getString("qbo_seq_no");
                            }
                        }

                            if(mycursor.getDouble(mycursor.getColumnIndex("Question_Option_Sequence")) != Double.valueOf(value)){
                                total = 0.0 - Double.valueOf(array.getJSONObject(count).getDouble("qbm_negative_mrk"));
                            }else{
                                total = Double.valueOf(array.getJSONObject(count).getString("qbm_marks"));
                            }
                            AssessmentTestQues.put("Question_Marks_Obtained",total);

                    }*/
//                    AssessmentTestQues.put("Option_Answer_Flag",mycursor.getInt(mycursor.getColumnIndex("Option_Answer_Flag")));
                    AssessmentList.put(AssessmentTestQues);
                    count++;
                }
                loc_count=AssessmentList.length();
                finalAssessmentObj.put("AssessmentTestData",AssessmentList);
                Log.e("syncAssesmentTestData","finalAssessmentObj"+finalAssessmentObj.toString());
                HashMap<String,String> hmap=new HashMap<>();
                hmap.put("jsonstr",finalAssessmentObj.toString());
                new MyBagroundTask(finalUrl+"syncAssessmentTestData.php",hmap,AssessmentTestActivity.this,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try{
                            JSONArray ja_assessmentKeys;
                            JSONObject assessmentObj;
                            Log.e("syncAssesmentTest11"," comes :  "+json);

                            JSONObject mainObj=new JSONObject(json);

                            Object obj1=mainObj.get("assessmentIds");
                            Object obj2=mainObj.get("Question_Negative_Applied");
                            Log.e("syncAssesmentTest11"," Question_Negative_Applied :  "+obj2.toString());
                            if (obj1 instanceof JSONArray)
                            {
                                ja_assessmentKeys=mainObj.getJSONArray("assessmentIds");
                                if(ja_assessmentKeys!=null && ja_assessmentKeys.length()>0){
                                    int p=0,q=0;
                                    serv_count=ja_assessmentKeys.length();
                                    Log.e("syncAssesmentTest11","updated_assesstestQ_rec:--"+ja_assessmentKeys.length());
                                    for(int i=0;i<ja_assessmentKeys.length();i++){
                                        assessmentObj=ja_assessmentKeys.getJSONObject(i);
//                                        int loc_upldflag=myhelper.checkAQUPLDStatus(studentid,assessmentObj.getString("assessmentKey"),"NotUploaded");
//                                        if(loc_upldflag>0){
//
//                                        }else{
//
//                                        }
                                        long updateFlag=dataObj.updateAssessmentQStatusFromServer(studentId,testid,instanceId,assessmentObj.getString("assessmentKey"),"Uploaded");
                                        if(updateFlag>0){
                                            p++;
                                        }else{
                                            q++;
                                        }
                                    }
                                    Log.e("syncAssesmentTest11","AQUpdated:--"+p);
                                    if(loc_count==serv_count){
                                        Toast.makeText(getApplicationContext(),"Syncronised",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Sync Failed,Try Again Later",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Log.e("syncAssesmentTest11","Null Assessment Ques Json Array: ");
                                }

                            }
                            else {
                                Log.e("syncAssesmentTest11","No Assessment Ques Uploaded: ");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("syncAssesmentTestData","  :  "+e.toString()+"lineno:--"+e.getStackTrace()[0].getLineNumber());
                        }
                    }
                }).execute();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("AssessmentTestActivity",e.toString());
            }
        }else{
            mycursor.close();
            Toast.makeText(getApplicationContext(),"No Assessment Ques Data to Upload",Toast.LENGTH_SHORT).show();
        }

    }


    private Bitmap getTheEncriptedImage(String qbm_image_file) {
        Bitmap bp=null;
        try {
            File f=new File(qbm_image_file);
            if(f.exists()) {
               bp= EncryptDecrypt.decrypt(new FileInputStream(f));
            }else
            {
                Log.e("AssessmentTestActivity","file is not found:"+qbm_image_file);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return bp;
    }


}

