package com.digywood.tms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.digywood.tms.Adapters.OptionsCheckAdapter;
import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.ScrollGridAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.Pojo.SingleQuestion;
import com.digywood.tms.Pojo.SingleQuestionList;
import com.digywood.tms.Pojo.SingleSections;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PracticeTestActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    TextView timer, q_no,qno_label;
    View finish_view;
    public static File file;
    ImageView fullscreen;
    GridView gridView;
    Spinner sections;
    String jsonPath,imgPath, photoPath, Seq, Id, path,enrollid,courseid,subjectId,paperid,testid,groupId;
    final String notAttempted = "NOT_ATTEMPTED", attempted = "ATTEMPTED", skipped = "SKIPPED", bookmarked = "BOOKMARKED",not_confirmed = "NOT_CONFIRMED",confirmed = "CONFIRMED";
    EncryptDecrypt encObj;
    RecyclerView question_scroll;
    ScrollGridAdapter scrollAdapter;
    QuestionListAdapter qAdapter;
    LinearLayoutManager myLayoutManager;
    ArrayAdapter adapter;
    RecyclerView rv_option;
    ArrayList<String> categories;
    private static final String TAG = "PracticeTestActivity";
    ArrayList<Integer> oplist = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Integer> optionsTemp = new ArrayList<>();
    ArrayList<SingleSections> sectionList = new ArrayList<>();
    ArrayList<SingleQuestion> questionList = new ArrayList<>();
    ArrayList<Integer> correctOptionList = new ArrayList<>();
    ArrayList<SingleOptions> optionsList = new ArrayList<>();
    ArrayList<SingleQuestionList> questionOpList = new ArrayList<>();
    ArrayList<ArrayList<SingleQuestionList>> listOfLists = new ArrayList<>();
    ImageView question_img ,menu;
    Bundle bundle;
    Button btn_group_info, btn_qadditional, btn_review, btn_prev, btn_next, btn_clear_option, btn_mark,btn_confirm;
    Cursor c;
    AlertDialog alertDialog;
    Bitmap b, op, bitmap;
    Boolean flag = false;
    final Boolean edit = true;
    public static final int RequestPermissionCode = 1;
    static int index = 0, pos = 0, max = 1, grp = 0, size, count = 0;
    JSONObject obj, sectionobj, groupobj, questionobj, temp;
    public static JSONObject attempt;
    JSONArray array, optionsArray, groupArray, sectionArray, attemptsectionarray, buffer;
    SingleOptions option;
    SingleQuestionList qListObj;
    OptionsCheckAdapter opAdapter;
    SaveJSONdataToFile save;
    DBHelper dataObj;
    long millisStart = 0, millisRemaining = 0;
    GestureDetector gd;


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

        setContentView(R.layout.activity_test);

        dataObj = new DBHelper(this);
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

        qAdapter = new QuestionListAdapter(questionOpList, PracticeTestActivity.this, getScreenSize());
        myLayoutManager = new LinearLayoutManager(PracticeTestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        question_scroll.setLayoutManager(myLayoutManager);
        question_scroll.setItemAnimator(new DefaultItemAnimator());
        question_scroll.setAdapter(qAdapter);
        testid = getIntent().getStringExtra("test");
        rv_option = findViewById(R.id.option_view);

        Cursor cursor = dataObj.getSingleStudentTests(testid);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("sptu_ID")).equals(testid)) {
                    enrollid = cursor.getString(cursor.getColumnIndex("sptu_entroll_id"));
                    courseid = cursor.getString(cursor.getColumnIndex("sptu_course_id"));
                    subjectId = cursor.getString(cursor.getColumnIndex("sptu_subjet_ID"));
                    paperid = cursor.getString(cursor.getColumnIndex("sptu_paper_ID"));
                }
            }
        }
        save = new SaveJSONdataToFile();
        path = enrollid + "/" + courseid + "/" + subjectId + "/" + paperid + "/" + testid + "/";
        photoPath = URLClass.mainpath + path;
        jsonPath = URLClass.mainpath + path + "Attempt/" + testid + ".json";
        imgPath=URLClass.mainpath+enrollid+"/"+courseid+"/";

        temp = new JSONObject();
        sectionArray = new JSONArray();
        attempt = new JSONObject();

        gd = new GestureDetector(PracticeTestActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
            if (getIntent().getStringExtra("status").equalsIgnoreCase("NEW")) {
                newTest();
            }
            else {
                c = dataObj.getAttempt(dataObj.getLastAttempt());
//                Log.e("TestingJson", ""+c.getInt(c.getColumnIndex("Attempt_Status"))+" "+c.getCount());
                millisStart = c.getLong(c.getColumnIndex("Attempt_RemainingTime"));
                attempt = new JSONObject(getIntent().getStringExtra("json"));
                attemptsectionarray = new JSONArray();
                attemptsectionarray = attempt.getJSONArray("Sections");
                restoreSections(dataObj.getQuestionStatus(), attempt);
//                buffer = generateArray(attempt.getJSONArray("Sections").getJSONObject(pos));
                index = c.getInt(c.getColumnIndex("Attempt_LastQuestion"));
                pos = c.getInt(c.getColumnIndex("Attempt_LastSection"));
            }
            //inserting new Test record in local database
            long ret = dataObj.InsertAttempt(generateUniqueId(1),attempt.getString("ptu_test_ID"),enrollid,"",courseid,subjectId,paperid,1,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
            Log.e("New Test Insertion",""+ret);

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

        //clear option button
        btn_clear_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opAdapter.resetOptionsList();
//                ((SimpleItemAnimator) rv_option.getItemAnimator()).setSupportsChangeAnimations(false);
                opAdapter.notifyDataSetChanged();
                clearOptions();
                setQBackground(pos,index);
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
                } else
                    Toast.makeText(PracticeTestActivity.this, "No option Selected", Toast.LENGTH_LONG).show();
            }
        });

        //confirmation button
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opAdapter.getSelectedItem() > -1) {
                    listOfLists.get(pos).get(index).setQ_status(attempted);
                    listOfLists.get(pos).get(index).setQ_check(confirmed);
                    setQBackground(pos, index);
                    btn_confirm.setBackgroundColor(Color.GREEN);
                } else
                    Toast.makeText(PracticeTestActivity.this, "No option Selected", Toast.LENGTH_LONG).show();
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
                    b = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("qbm_image_file"));
                    bitmap = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("qbm_qimage_file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initiateFullScreenWindow(b, bitmap);
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
                        op = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("gbg_media_file"));
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
                        } else if (index == buffer.length() - 1) {
                            //Change button once last question of test is reached
                            setQBackground(pos, index);
                            writeOption(opAdapter.getSelectedItem());
                            if (pos == attemptsectionarray.length() - 1) {
                                btn_next.setText("Finish");
                                writeOption(opAdapter.getSelectedItem());
                                AlertDialog alertbox = new AlertDialog.Builder(PracticeTestActivity.this)
                                        .setMessage("Do you want to finish Test?" + " " + dataObj.getQuestionCount())
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
//                                            q_list.clear();
                                                try {
                                                    long value = dataObj.UpdateAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),2,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                                                    if (value <= 0) {
                                                        Log.e("PaperId: ","pid  "+paperid);
                                                        long ret = dataObj.InsertAttempt(generateUniqueId(1),attempt.getString("ptu_test_ID"),enrollid,"",courseid,subjectId,paperid,2,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                                                        Log.e("Insertion",""+ret);
                                                    }
                                                    SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/" + testid + ".json", attempt.toString());
                                                } catch (JSONException|IOException e) {
                                                    e.printStackTrace();
                                                }

                                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                                        makeSceneTransitionAnimation(PracticeTestActivity.this, finish_view, "transition");
                                                int revealX = (int) (finish_view.getX() + finish_view.getWidth() / 2);
                                                int revealY = (int) (finish_view.getY() + finish_view.getHeight() / 2);
                                                finish();
                                                count = dataObj.getAttempCount();

/*                                                c = dataObj.getAttempt(count);
                                                c.moveToFirst();*/
/*                                                if (c.getInt(c.getColumnIndex("Attempt_Status")) == 2)
                                                    Log.e("Test", "Finished");*/

                                                Intent intent = new Intent(PracticeTestActivity.this, ScoreActivity.class);
                                                bundle = new Bundle();
                                                bundle.putString("JSON", attempt.toString());
                                                bundle.putString("enrollid",enrollid);
                                                bundle.putString("courseid", courseid);
                                                bundle.putString("subjectid", subjectId);
                                                bundle.putString("paperid",paperid);
                                                bundle.putString("Type","PRACTICE");
                                                intent.putExtra("BUNDLE", bundle);
                                                intent.putExtra("Xreveal", revealX);
                                                intent.putExtra("Yreveal", revealY);
                                                ActivityCompat.startActivity(PracticeTestActivity.this, intent, options.toBundle());

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
                            } else {
                                qAdapter.setData(index);
                                pos = pos + 1;
                                Log.e("ValuesElse--->", "" + pos + "," + index);
                                buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
                                sections.setSelection(pos);
                                setScrollbar(pos);
                                scrollAdapter.updateList(listOfLists.get(pos));
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
                        setQuestion(pos, index, edit);
                    } else if (index == 0 && pos > 0) {
                        pos = pos - 1;
                        index = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions").length() - 1;
                        flag = false;
                        sections.setSelection(pos);
                        scrollAdapter.updateList(listOfLists.get(pos));
                        setScrollbar(pos);
                    }
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

        new CountDownTimer(millisStart, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timer.setText(text);
                millisRemaining = millisUntilFinished;
            }

            public void onFinish() {
                timer.setText("Time Up!");
//                timer.setVisibility(View.INVISIBLE);
            }
        }.start();

    }

    public void newTest() throws JSONException {
        millisStart = 3600000;
        obj = new JSONObject(getIntent().getStringExtra("json"));
        encObj = new EncryptDecrypt();
        attemptsectionarray = new JSONArray();
        max = 1;
        attempt = new JSONObject(getIntent().getStringExtra("json"));
        attemptsectionarray = attempt.getJSONArray("Sections");
        Log.e("Sections", "" + attemptsectionarray.length());
        index = 0;
        pos = 0;
        buffer = attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions");
        storeSections();
    }

    public String generateUniqueId(int i){
        String AttemptId ="";
        AttemptId = testid.concat("_"+String.valueOf(dataObj.getTestAttempCount(testid) + i));
        Log.e("Attempt Id", AttemptId);
        return AttemptId;
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
        ActivityCompat.requestPermissions(PracticeTestActivity.this, new
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
            LayoutInflater inflater = (LayoutInflater) PracticeTestActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View view = inflater.inflate(R.layout.popup_screen,
                    (ViewGroup) findViewById(R.id.popup_element));
            //Initialise an dialogbuilder to create an alertdialog
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            //Set the layout to the alertDialog
            dialogBuilder.setView(view);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            final Button cancelButton = (Button) view.findViewById(R.id.close_button);
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
            if (dataObj.getPosition(Id) > -1) {
                Log.e("SelectedOption", "Reached");
                opAdapter.setOptionsList(dataObj.getPosition(Id));
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
            Seq = buffer.getJSONObject(index).getString("qbm_SequenceId");
            questionobj = buffer.getJSONObject(index);
            scrollAdapter.updateList(listOfLists.get(pos));
            if (dataObj.CheckQuestion(Id)) {
                Log.e("Option_Status", listOfLists.get(pos).get(index).getQ_status());
                if (indx > -1) {
                    result = dataObj.UpdateQuestion(attempt.getString("ptu_test_ID"), null, Id, Seq,attempt.getJSONArray("Sections").getJSONObject(pos).getString("ptu_section_name"),questionobj.getString("qbm_Chapter_name"),questionobj.getString("qbm_Sub_CategoryName"), Integer.valueOf(questionobj.getString("qbm_marks")), Double.valueOf(questionobj.getString("qbm_negative_mrk")), 0, 0, indx, listOfLists.get(pos).get(index).getQ_status(), opAdapter.getSelectedSequence(), opAdapter.getFlag());
                } else {
                    //if question is attempted and then the option is cleared store as skipped
                    result = dataObj.UpdateQuestion(attempt.getString("ptu_test_ID"), null, Id, Seq,attempt.getJSONArray("Sections").getJSONObject(pos).getString("ptu_section_name"),questionobj.getString("qbm_Chapter_name"),questionobj.getString("qbm_Sub_CategoryName"), Integer.valueOf(questionobj.getString("qbm_marks")), Double.valueOf(questionobj.getString("qbm_negative_mrk")), 0, 0, indx, listOfLists.get(pos).get(index).getQ_status(), opAdapter.getSelectedSequence(), opAdapter.getFlag());
                }
                if (result == 0) {
                    dataObj.InsertQuestion(attempt.getString("ptu_test_ID"), null, Id, Seq,attempt.getJSONArray("Sections").getJSONObject(pos).getString("ptu_section_name"),questionobj.getString("qbm_Chapter_name"),questionobj.getString("qbm_Sub_CategoryName"), Integer.valueOf(questionobj.getString("qbm_marks")), Double.valueOf(questionobj.getString("qbm_negative_mrk")), 0, 0, indx, listOfLists.get(pos).get(index).getQ_status(), opAdapter.getSelectedSequence(), opAdapter.getFlag());
                }
                Log.e("CurrentStatus", "" + dataObj.getPosition(Id));
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
            Seq = buffer.getJSONObject(index).getString("qbm_SequenceId");
            questionobj = buffer.getJSONObject(index);
            long value = dataObj.UpdateQuestion(attempt.getString("ptu_test_ID"), dataObj.getLastAttempt(), Id, Seq,attempt.getJSONArray("Sections").getJSONObject(pos).getString("ptu_section_name"),questionobj.getString("qbm_Chapter_name"),questionobj.getString("qbm_Sub_CategoryName"), Integer.valueOf(questionobj.getString("qbm_marks")), Double.valueOf(questionobj.getString("qbm_negative_mrk")), dataObj.getCorrectSum(), dataObj.getWrongSum(), -1, listOfLists.get(pos).get(index).getQ_status(), opAdapter.getSelectedSequence(), opAdapter.getFlag());
            if(value > 0){
                listOfLists.get(pos).get(index).setQ_status(notAttempted);
                listOfLists.get(pos).get(index).setQ_check(not_confirmed);
                btn_confirm.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to create a menu window
    public void initiateMenuWindow(View v) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) PracticeTestActivity.this
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

        tv_answered_count.setText("" + dataObj.getQuestionAttempted());
        tv_skipped_count.setText("" + dataObj.getQuestionSkipped());
        tv_bookmarked_count.setText("" + dataObj.getQuestionBookmarked());
        tv_not_attempted_count.setText("" + dataObj.getQuestionNotAttempted());
        Button finish = layout.findViewById(R.id.finish_button);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertbox = new AlertDialog.Builder(PracticeTestActivity.this)
                        .setMessage("Do you want to finish Test?" + " " + dataObj.getQuestionCount())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
//                                            q_list.clear();
                                try {
                                    long value = dataObj.UpdateAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),2,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                                    if (value <= 0) {
                                        Log.e("PaperId: ","pid  "+paperid);
                                        long ret = dataObj.InsertAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),enrollid,"",courseid,subjectId,paperid,2,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                                        Log.e("Insertion",""+ret);
                                    }
                                    SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/" + testid + ".json", attempt.toString());

                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(PracticeTestActivity.this, finish_view, "transition");
                                int revealX = (int) (finish_view.getX() + finish_view.getWidth() / 2);
                                int revealY = (int) (finish_view.getY() + finish_view.getHeight() / 2);
                                Intent intent = new Intent(PracticeTestActivity.this, ScoreActivity.class);
                                bundle = new Bundle();
                                bundle.putString("JSON", attempt.toString());
                                bundle.putString("enrollid",enrollid);
                                bundle.putString("courseid", courseid);
                                bundle.putString("subjectid", subjectId);
                                bundle.putString("paperid",paperid);
                                bundle.putString("Type","PRACTICE");
                                intent.putExtra("BUNDLE", bundle);
                                intent.putExtra("Xreveal", revealX);
                                intent.putExtra("Yreveal", revealY);
                                finish();
                                ActivityCompat.startActivity(PracticeTestActivity.this, intent, options.toBundle());
                                } catch (JSONException|IOException|IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
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
        });
        Button cancelButton = layout.findViewById(R.id.close_button);
        final AlertDialog alertDialog = dialogBuilder.create();
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
        LayoutInflater inflater = (LayoutInflater) PracticeTestActivity.this
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
        LayoutInflater inflater = (LayoutInflater) PracticeTestActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.singlescreen, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        ImageView limg = layout.findViewById(R.id.layout_img);
        limg.setImageBitmap(b);
        ImageView cancel = layout.findViewById(R.id.iv_close);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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
/*        if (questionobj.getString("qbm_QAdditional_Flag").equals("YES")) {
            btn_qadditional.setEnabled(true);
            btn_qadditional.setClickable(true);
            btn_qadditional.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_qadditional.setEnabled(false);
            btn_qadditional.setClickable(false);
            btn_qadditional.setBackgroundColor(0);

        }*/
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
        Bitmap b = BitmapFactory.decodeFile(imgPath+sid+"/"+pid+"/"+cid+"/"+questionobj.getString("qbm_image_file"));
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
            option.setQbo_media_file(imgPath+sid+"/"+pid+"/"+cid+"/"+optionsArray.getJSONObject(i).getString("qbo_media_file"));
            option.setQbo_seq_no(optionsArray.getJSONObject(i).getString("qbo_seq_no"));
            option.setQbo_answer_flag(optionsArray.getJSONObject(i).getString(("qbo_answer_flag")));
            optionsList.add(option);
        }

        try {
            opAdapter = new OptionsCheckAdapter(optionsList, PracticeTestActivity.this, photoPath, rv_option);
            Log.e("opSize", ""+oplist.size());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rv_option.setLayoutManager(mLayoutManager);
            rv_option.setItemAnimator(new DefaultItemAnimator());
            rv_option.setAdapter(opAdapter);
            opAdapter.setOptionsEditable(edit);
            runLayoutAnimation(rv_option);
//        opAdapter.notifyDataSetChanged();
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

    //method to store the number of questions in each section{
    public void storeSections() {
        categories = new ArrayList<String>();
        try {
            listOfLists = new ArrayList<>();
            for (int i = 0; i < attempt.getJSONArray("Sections").length(); i++) {
                categories.add(attempt.getJSONArray("Sections").getJSONObject(i).getString("ptu_section_name"));
                JSONArray array2 = attempt.getJSONArray("Sections").getJSONObject(i).getJSONArray("Questions");
                questionOpList = new ArrayList<>();
                for (int j = 0; j < array2.length(); j++) {
                    Id = array2.getJSONObject(j).getString("qbm_ID");
                    Seq = array2.getJSONObject(j).getString("qbm_SequenceId");
                    questionobj = array2.getJSONObject(j);
                    Log.e("sequence", Seq);
                    qListObj = new SingleQuestionList(array2.getJSONObject(j).getString("qbm_SequenceId"), notAttempted,not_confirmed);
                    dataObj.InsertQuestion(attempt.getString("ptu_test_ID"), null, Id, Seq,attempt.getJSONArray("Sections").getJSONObject(pos).getString("ptu_section_name"),questionobj.getString("qbm_Chapter_name"),questionobj.getString("qbm_Sub_CategoryName"), 0, 0, 0, 0, -1, "NOT_ATTEMPTED", "-1", "NO");
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
                categories.add(attempt.getJSONArray("Sections").getJSONObject(i).getString("ptu_section_name"));
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

 /*   //method to check if the section contains any gorup questions or not and return the appropriate JSON Array
    public JSONArray generateArray(JSONObject Section) {
        JSONArray array = new JSONArray();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();
        int length = 0;
        grp = 0;
        ArrayList<JSONArray> jaList = new ArrayList<>();
        try {
            if (Section.has("groups")) {
                groupArray = Section.getJSONArray("groups");
                for (int i = 0; i < groupArray.length(); i++) {
                    array1 = groupArray.getJSONObject(i).getJSONArray("Group_question");
                    jaList.add(array1);
                }
                for (int i = 0; i < Section.getJSONArray("questions").length(); i++) {
                    array2.put(grp, Section.getJSONArray("questions").get(i));
                    grp++;
                }
                jaList.add(array2);
                array = getMergeJsonArrays(jaList);
            } else {
                array = Section.getJSONArray("questions");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    //method to merge 2 JSON Arrays and return the result
    public JSONArray getMergeJsonArrays(ArrayList<JSONArray> jsonArrays) throws JSONException {
        JSONArray MergedJsonArrays = new JSONArray();
        for (JSONArray tmpArray : jsonArrays) {
            for (int i = 0; i < tmpArray.length(); i++) {
                MergedJsonArrays.put(tmpArray.get(i));
            }
        }
        return MergedJsonArrays;
    }
*/

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
                .setMessage("Do you want to exit Test?  Progress will be saved")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            long value = dataObj.UpdateAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),1,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
/*                            if (value <= 0) {
                                long ret = dataObj.InsertAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),1, 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                                Log.e("Insertion",""+ret);
                            }*/
                            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/" + testid + ".json", attempt.toString());
                        } catch (JSONException|IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(PracticeTestActivity.this, ListofPractiseTests.class);
                        intent.putExtra("enrollid",enrollid);
                        intent.putExtra("courseid", courseid);
                        intent.putExtra("paperid",paperid);
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
            Log.e("Size", "" + listOfLists.size());
            scrollAdapter = new ScrollGridAdapter(PracticeTestActivity.this, attempt.getJSONArray("Sections").getJSONObject(pos).getJSONArray("Questions"), listOfLists.get(pos), getScreenSize());
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
        dataObj = new DBHelper(PracticeTestActivity.this);
//        int count = dataObj.getAttempCount()-1;
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause:");
        dataObj = new DBHelper(PracticeTestActivity.this);
        try {
            long value = dataObj.UpdateAttempt(generateUniqueId(0),attempt.getString("ptu_test_ID"),1,"NotUploaded", 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
/*            if (value <= 0) {
                long ret = dataObj.InsertAttempt(generateUniqueId(),attempt.getString("ptu_test_ID"),1, 0,dataObj.getQuestionAttempted(),dataObj.getQuestionSkipped(),dataObj.getQuestionBookmarked(),dataObj.getQuestionNotAttempted(), 0, millisRemaining, index, pos);
                Log.e("Insertion",""+ret);
            }*/
            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/" + testid + ".json", attempt.toString());
            Log.e("Attempt-Json", attempt.toString());
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


}

