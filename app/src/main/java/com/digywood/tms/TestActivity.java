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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import com.digywood.tms.Adapters.OptionsCheckAdapter;
import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.ScrollGridAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleGroup;
import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.Pojo.SingleQuestion;
import com.digywood.tms.Pojo.SingleQuestionList;
import com.digywood.tms.Pojo.SingleSections;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TestActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    TextView timer;
    public static File file;
    ImageView fullscreen, menu;
    private PopupWindow pw;
    GridView gridView;
    Spinner sections;
    String jsonPath, photoPath, Seq, Id, path, enrollid, courseid, subjectId, paperid, testid,positiveMarks, negativeMarks,MyPREFERENCES = "MyPreferences";
    EncryptDecrypt encObj;
    RecyclerView question_scroll;
    ScrollGridAdapter scrollAdapter;
    QuestionListAdapter qAdapter;
    LinearLayoutManager myLayoutManager;
    ArrayAdapter adapter;
    RecyclerView rv_option;
    ArrayList<String> categories;
    private static final String TAG = "TestActivity";
    ArrayList<Integer> oplist = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Integer> optionsTemp = new ArrayList<>();
    ArrayList<SingleSections> sectionList = new ArrayList<>();
    ArrayList<SingleQuestion> questionList = new ArrayList<>();
    ArrayList<Integer> questionNumberList = new ArrayList<>();
    ArrayList<Integer> correctOptionList = new ArrayList<>();
    ArrayList<SingleOptions> optionsList = new ArrayList<>();
    ArrayList<SingleQuestionList> questionOpList = new ArrayList<>();
    ArrayList<ArrayList<SingleQuestionList>> listOfLists = new ArrayList<>();
    ImageView question_img;
    Bundle bundle;
    Button btn_group_info, btn_qadditional, btn_review, btn_prev, btn_next, btn_clear_option, btn_mark;
    Drawable drawable;
    Cursor c;
    AlertDialog alertDialog;
    Bitmap b, op, bitmap;
    Boolean flag = false;
    final Boolean edit = true;
    public static final int RequestPermissionCode = 1;
    static int index = 0, pos = 0, max = 1, grp = 0, size,count =-1;
    JSONObject obj, sectionobj, groupobj, questionobj, temp;
    public static JSONObject attempt;
    JSONArray array, optionsArray, totalArray, groupArray, sectionArray, attemptsectionarray, buffer;
    SingleQuestion question = new SingleQuestion();
    SingleGroup group = new SingleGroup();
    SingleSections section = new SingleSections();
    SingleOptions option;
    SingleQuestionList qListObj;
    OptionsCheckAdapter opAdapter;
    SaveJSONdataToFile save;
    DBHelper dataObj;
    long millisStart = 0,millisRemaining = 0;
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
        btn_group_info = findViewById(R.id.btn_group_info);
        btn_qadditional = findViewById(R.id.btn_qadditional);
        btn_review = findViewById(R.id.btn_review_info);
        timer = findViewById(R.id.timer);
        sections = findViewById(R.id.sections);
        fullscreen = findViewById(R.id.fullscreen);
        menu = findViewById(R.id.menu);


        qAdapter = new QuestionListAdapter(questionOpList, TestActivity.this, getScreenSize());
        myLayoutManager = new LinearLayoutManager(TestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        question_scroll.setLayoutManager(myLayoutManager);
        question_scroll.setItemAnimator(new DefaultItemAnimator());
        question_scroll.setAdapter(qAdapter);
        testid = "PTAA00002";
        rv_option = findViewById(R.id.option_view);

        Cursor cursor = dataObj.getStudentTests();
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
        jsonPath = URLClass.mainpath + path + testid + ".json";

        temp = new JSONObject();
        sectionArray = new JSONArray();
        attempt = new JSONObject();

        gd = new GestureDetector(TestActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {

                //your action here for double tap e.g.
                Log.d("OnDoubleTapListener", "onDoubleTap");
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
            count = dataObj.getAttempCount() - 1 ;
            c = dataObj.getAttempt(count);
            //if cursor has values then the test is being resumed and data is retrieved from database
            if(c.getCount()> 0) {
                c.moveToLast();
                if (c.getInt(c.getColumnIndex("Attempt_Status")) == 1) {
                    millisStart = c.getLong(c.getColumnIndex("Attempt_RemainingTime")) ;
                    String json = new String(SaveJSONdataToFile.bytesFromFile(URLClass.mainpath + path + "Attempt/"+ testid + ".json"), "UTF-8");
                    attempt = new JSONObject(json);
                    parseJson(attempt);
                    attemptsectionarray = new JSONArray();
                    attemptsectionarray = attempt.getJSONArray("sections");
                    restoreSections(dataObj.getQuestionStatus(),attempt);
                    buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                    index = c.getInt(c.getColumnIndex("Attempt_LastQuestion"));
                    pos = c.getInt(c.getColumnIndex("Attempt_LastSection"));
                }
            }
            //else cursor doesn't have values then the test is started fresh
            else {
                    Log.e("Fresh","Test");
                    millisStart = 3600000;
                    String json = new String(SaveJSONdataToFile.bytesFromFile(jsonPath), "UTF-8");
                    obj = new JSONObject(json);
                    parseJson(obj);
                    encObj = new EncryptDecrypt();
                    attemptsectionarray = new JSONArray();
                    max = 1;
                    for (int i = 0; i < 2; i++) {
                        Collections.shuffle(list);
                        Collections.shuffle(oplist);
                        generateAttemptJSON(i);
                    }
                    attempt.put("sptu_marks",obj.getString("sptu_marks"));
                    attempt.put("sptu_negative_applicable",obj.getString("sptu_negative_applicable"));
                    attempt.put("sptu_negative_mrk",obj.getString("sptu_negative_mrk"));
                    attempt.put("sections", attemptsectionarray);
                    buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                    storeSections();
                    index = 0;
                    pos = 0;
            }
            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/"+ testid + ".json",attempt.toString());


        } catch (JSONException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        sections.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sections.setAdapter(adapter);



        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(v);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateMenuWindow(v);
            }
        });

        btn_clear_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opAdapter.resetOptionsList();
                opAdapter.notifyDataSetChanged();
                clearOptions();
            }
        });

        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opAdapter.getSelectedItem() > -1) {
                    listOfLists.get(pos).get(index).setQ_status("BOOKMARKED");
                    qAdapter.updateList(questionOpList);
                    writeOption(opAdapter.getSelectedItem());
                    btn_next.callOnClick();
                } else
                    Toast.makeText(TestActivity.this, "No option Selected", Toast.LENGTH_LONG).show();
            }
        });

        btn_qadditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionobj = array.getJSONObject(index);
                    b = BitmapFactory.decodeFile(photoPath + questionobj.getString("qbm_image_file"));
                    bitmap = BitmapFactory.decodeFile(photoPath + questionobj.getString("qbm_qimage_file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initiateFullScreenWindow(b, bitmap);
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    flag = true;
                    buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                    if (index <= buffer.length()) {
//                        optionsTemp = dataObj.getQuestion();
                        if (index < buffer.length() - 1) {
                            setQBackground(pos,index);
                            writeOption(opAdapter.getSelectedItem());
                            index++;
                            Log.e("ValuesIf--->", "" + pos + "," + index);
                            Id = buffer.getJSONObject(index).getString("qbm_ID");
                            Seq = buffer.getJSONObject(index).getString("qbm_sequence");
                            setQuestion(pos, index, edit);
                            checkRadio();
                        } else if (index == buffer.length() - 1) {
                            //Change button once last question of test is reached
                            setQBackground(pos,index);
                            writeOption(opAdapter.getSelectedItem());
                            if (pos == attemptsectionarray.length() - 1) {
                                btn_next.setText("Finish");
                                writeOption(opAdapter.getSelectedItem());
                                AlertDialog alertbox = new AlertDialog.Builder(TestActivity.this)
                                        .setMessage("Do you want to finish Test?" + " " + dataObj.getQuestionCount())
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            // do something when the button is clicked
                                            public void onClick(DialogInterface arg0, int arg1) {
//                                            q_list.clear();

                                                try {
                                                    SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/"+ testid + ".json",attempt.toString());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                finish();
                                                count = dataObj.getAttempCount()-1;
                                                long value = dataObj.UpdateAttempt(count,2,0,0,millisRemaining,index,pos);
                                                if(value>=0){
                                                    dataObj.InsertAttempt(2,0,0,millisRemaining,index,pos);
                                                }
                                                c = dataObj.getAttempt(count);
                                                c.moveToFirst();
                                                if (c.getInt(c.getColumnIndex("Attempt_Status")) == 2)
                                                    Log.e("Test","Finished");
                                                Intent intent = new Intent(TestActivity.this, ScoreActivity.class);
                                                bundle = new Bundle();
                                                bundle.putString("JSON",attempt.toString());
                                                bundle.putString("Course",courseid);
                                                bundle.putString("Subject",subjectId);
                                                bundle.putInt("Integer",getCorrectCount());

                                                intent.putExtra("BUNDLE",bundle);
                                                startActivity(intent);

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
                                buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                                sections.setSelection(pos);
                                scrollAdapter.updateList(listOfLists.get(pos));
                                setScrollbar(pos);

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setQBackground(pos,index);
                    writeOption(opAdapter.getSelectedItem());

                    questionobj = array.getJSONObject(index);
                    if (index > 0) {
                        index--;
                        buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                        Id = buffer.getJSONObject(index).getString("qbm_ID");
                        Seq = buffer.getJSONObject(index).getString("qbm_sequence");
                        flag = true;
                        setQuestion(pos, index, edit);
                    } else if (index == 0 && pos > 0) {
                        pos = pos - 1;
                        index = generateArray(attemptsectionarray.getJSONObject(pos)).length() - 1;
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
        question_scroll.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), question_scroll, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, int in) {
                gotoQuestion(in);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

//        date = findViewById(R.id.date);
//        Date dt = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String check = dateFormat.format(dt);
//        date.setText(check);
        //Setting countdown timer
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

        String MyDecImgs = android.os.Environment.getExternalStorageDirectory().toString() + "/DigyTMS/Decrypted/";
        File decdir = new File(MyDecImgs);
        if (!decdir.exists()) {
            boolean res = decdir.mkdirs();
        }

        String MyImgs = android.os.Environment.getExternalStorageDirectory().toString() + "/DigyTMS/Encrypted/";
        File dir = new File(MyImgs);
        if (!dir.exists()) {
            boolean res = dir.mkdirs();
        } else {
            File myFile = new File(MyImgs + "enc_encryption_key.JPG");
            if (myFile.exists()) {
                file = new File(MyImgs + "enc_encryption_key.JPG");
            } else {
                Toast.makeText(TestActivity.this, "File Not Found", Toast.LENGTH_LONG).show();
            }
        }

    }

    //temperoary method
    public String loadJSONFromAsset() {
        String json = null;
        byte[] buffer = null;
        try {
            InputStream is = this.getAssets().open("updated_json.json");
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return json;
    }

    //method to display selected question
    public void gotoQuestion(int in){
        try {
            setQBackground(pos,index);
            writeOption(opAdapter.getSelectedItem());

            index = in;
            setQuestion(pos,index,edit);
//                    qAdapter.setBackground(index);
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            if(index == buffer.length() -1)
                Log.e("TQuestionScroll","LastQuestions");
            checkRadio();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to dynamically request permissions
    private void requestPermission() {
        ActivityCompat.requestPermissions(TestActivity.this, new
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
            listOfLists.get(pos).get(index).setQ_status("SKIPPED");
        } else {
            if (!listOfLists.get(pos).get(index).getQ_status().equals("BOOKMARKED"))
                {   listOfLists.get(pos).get(index).setQ_status("ATTEMPTED");}
            else
                {   listOfLists.get(pos).get(index).setQ_status("BOOKMARKED");}
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

    //method to find total number of correct questions
    public int getCorrectCount(){

        int CorrectCount=0;
        optionsTemp = dataObj.getOptions();
        correctOptionList = dataObj.getCorrectOptions();
        for(int i=0; i<optionsTemp.size();i++){
            if(correctOptionList.contains(optionsTemp.get(i))){
                CorrectCount++;
            }
        }
        return CorrectCount;
    }

    //method to set a horizantal scrollbar containing question numbers of the current section
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
            LayoutInflater inflater = (LayoutInflater) TestActivity.this
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
                    gotoQuestion(position);
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
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            if (dataObj.getPosition(Id) > -1) {
                opAdapter.setOptionsList(dataObj.getPosition(Id));
                opAdapter.notifyDataSetChanged();
            }

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    //method to store the selected option in the local database
    public void writeOption(int indx) {
//        RadioButton random = findViewById(group.getCheckedRadioButtonId());
        try {
            long result = -1;
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Seq = buffer.getJSONObject(index).getString("qbm_sequence");
            scrollAdapter.updateList(listOfLists.get(pos));
            if (dataObj.CheckQuestion(Id)) {
                Log.e("Option_Status",listOfLists.get(pos).get(index).getQ_status());
                if (indx > -1) {
                    Log.e("CurrentStatus",listOfLists.get(pos).get(index).getQ_status());
                    result = dataObj.UpdateQuestion(Id, Seq, Integer.valueOf(questionobj.getString("qbm_marks")), indx, listOfLists.get(pos).get(index).getQ_status(),opAdapter.getSelectedSequence(),opAdapter.getFlag());
                }else{
                    result = dataObj.UpdateQuestion(Id, Seq, Integer.valueOf(questionobj.getString("qbm_marks")), indx, listOfLists.get(pos).get(index).getQ_status(),opAdapter.getSelectedSequence(),opAdapter.getFlag());
                }
                if(result == 0 ){
                    dataObj.InsertQuestion(Id, Seq, Integer.valueOf(questionobj.getString("qbm_marks")), indx, listOfLists.get(pos).get(index).getQ_status(),opAdapter.getSelectedSequence(),opAdapter.getFlag());
                }
            }
            // Saving time remaining
            long value = dataObj.UpdateAttempt(count,1,0,0,millisRemaining,index,pos);
            if(value>=0){
                dataObj.InsertAttempt(1,0,0,millisRemaining,index,pos);
            }
            Log.e("WriteOption:", opAdapter.getSelectedSequence());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to clear the selected options in the local database
    public void clearOptions() {
        try {
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Seq = buffer.getJSONObject(index).getString("qbm_sequence");
            dataObj.UpdateQuestion(Id, Seq, Integer.valueOf(questionobj.getString("qbm_marks")), -1, "SKIPPED",opAdapter.getSelectedSequence(),opAdapter.getFlag());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to create a menu window
    public void initiateMenuWindow(View v) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) TestActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.menu, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);

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
        LayoutInflater inflater = (LayoutInflater) TestActivity.this
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
        LayoutInflater inflater = (LayoutInflater) TestActivity.this
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


    //method to read the JSON file and store the contents
    public void parseJson(JSONObject data) {
        temp = data;
        questionList = new ArrayList<>();
        try {
            sectionArray = data.getJSONArray("sections");
            for (int i = 0; i < sectionArray.length(); i++) {
                section = new SingleSections();
                sectionobj = sectionArray.getJSONObject(i);
                section.setSec_ID(sectionobj.getString("section_ID"));
                section.setSec_Name(sectionobj.getString("section_Name"));
                array = sectionobj.getJSONArray("questions");
                for (int g = 0; g < sectionobj.getJSONArray("groups").length(); g++) {
                    group = new SingleGroup();
                    groupobj = array.getJSONObject(g);
                    group.setGroup_ID(groupobj.getString("Group_ID"));
                    group.setGroup_Qnos(groupobj.getString("Group_Qnos"));
                    group.setGroup_pickup(groupobj.getString("Group_pickup"));
                    JSONArray grpQuestions = groupobj.getJSONArray("Group_question");
                    for (int a =0; a<grpQuestions.length();a++){
                        question = new SingleQuestion();
                        questionobj = array.getJSONObject(a);
                        JSONArray optionsArray = questionobj.getJSONArray("options");
                        question.setQbm_ID(questionobj.getString("qbm_ID"));
                        question.setQbm_ReferenceID(questionobj.getString("qbm_ReferenceID"));
                        question.setQbm_Description(questionobj.getString("qbm_Description"));
                        question.setQbm_SubjectID(questionobj.getString("qbm_SubjectID"));
                        question.setQbm_SubjectName(questionobj.getString("qbm_SubjectName"));
                        question.setQbm_Paper_ID(questionobj.getString("qbm_Paper_ID"));
                        question.setQbm_Paper_Name(questionobj.getString("qbm_Paper_Name"));
                        question.setQbm_ChapterID(questionobj.getString("qbm_ChapterID"));
                        question.setQbm_ChapterName(questionobj.getString("qbm_ChapterName"));
                        question.setQbm_Sub_CategoryID(questionobj.getString("qbm_Sub_CategoryID"));
                        question.setQbm_Sub_CategoryName(questionobj.getString("qbm_Sub_CategoryName"));
                        question.setQbm_level(questionobj.getString("qbm_level"));
                        question.setQbm_Type(questionobj.getString("qbm_Type"));
                        question.setQbm_marks(questionobj.getString("qbm_marks"));
                        question.setQbm_negative_applicable(questionobj.getString("qbm_negative_applicable"));
                        question.setQbm_negative_mrk(questionobj.getString("qbm_negative_mrk"));
                        question.setQbm_question_type(questionobj.getString("qbm_question_type"));
                        question.setQbm_text_applicable(questionobj.getString("qbm_text_applicable"));
                        question.setQbm_text(questionobj.getString("qbm_text"));
                        question.setQbm_image_file(questionobj.getString("qbm_image_file"));
                        question.setQbm_video_file(questionobj.getString("qbm_video_file"));
                        question.setQbm_media_type(questionobj.getString("qbm_media_type"));
                        question.setQbm_Group_Flag(questionobj.getString("qbm_group_flag"));
                        question.setQbm_answer(questionobj.getString("qbm_answer"));
                        question.setQbm_review_flag(questionobj.getString("qbm_review_flag"));
                        question.setQbm_Review_Type(questionobj.getString("qbm_Review_Type"));
                        question.setQbm_Review_Images(questionobj.getString("qbm_Review_Images"));
                        question.setQbm_review_Video(questionobj.getString("qbm_review_Video"));
                        question.setQbm_Additional_Images_num(questionobj.getString("qbm_Additional_Images_num"));
                        question.setQbm_Additional_Image_ref(questionobj.getString("qbm_Additional_Image_ref"));
                        question.setGbg_id(questionobj.getString("gbg_id"));
                        question.setQbg_media_type(questionobj.getString("qbg_media_type"));
                        question.setQbg_media_file(questionobj.getString("qbg_media_file"));
                        question.setQbg_text(questionobj.getString("qbg_text"));
                        question.setQbg_no_questions(questionobj.getString("qbg_no_questions"));
                        question.setQbg_no_pick(questionobj.getString("qbg_no_pick"));
                        for (int k = 0; k < optionsArray.length(); k++) {
                            JSONObject optionJSON = optionsArray.getJSONObject(k);
                            option = new SingleOptions();
                            option.setQbo_id(optionJSON.getString("qbo_id"));
                            option.setQbo_seq_no(optionJSON.getString("qbo_seq_no"));
                            option.setQbo_type(optionJSON.getString("qbo_type"));
                            option.setQbo_text(optionJSON.getString("qbo_text"));
                            option.setQbo_media_type(optionJSON.getString("qbo_media_type"));
                            option.setQbo_media_file(optionJSON.getString("qbo_media_file"));
                            option.setQbo_answer_flag(optionJSON.getString("qbo_answer_flag"));
                            optionsList.add(option);
                            if(optionJSON.getString("qbo_answer_flag").equals("YES")) {
                                correctOptionList.add(Integer.valueOf(optionJSON.getString("qbo_seq_no")));
                                Log.e("correct : ","i "+optionJSON.getString("qbo_seq_no"));
                            }
                        }
                        questionList.add(question);
                    }
                }
                for (int j = 0; j < array.length(); j++) {
                    question = new SingleQuestion();
                    questionobj = array.getJSONObject(j);
                    JSONArray optionsArray = questionobj.getJSONArray("options");

                    question.setQbm_ID(questionobj.getString("qbm_ID"));
                    question.setQbm_ReferenceID(questionobj.getString("qbm_ReferenceID"));
                    question.setQbm_Description(questionobj.getString("qbm_Description"));
                    question.setQbm_SubjectID(questionobj.getString("qbm_SubjectID"));
                    question.setQbm_SubjectName(questionobj.getString("qbm_SubjectName"));
                    question.setQbm_Paper_ID(questionobj.getString("qbm_Paper_ID"));
                    question.setQbm_Paper_Name(questionobj.getString("qbm_Paper_Name"));
                    question.setQbm_ChapterID(questionobj.getString("qbm_ChapterID"));
                    question.setQbm_ChapterName(questionobj.getString("qbm_ChapterName"));
                    question.setQbm_Sub_CategoryID(questionobj.getString("qbm_Sub_CategoryID"));
                    question.setQbm_Sub_CategoryName(questionobj.getString("qbm_Sub_CategoryName"));
                    question.setQbm_level(questionobj.getString("qbm_level"));
                    question.setQbm_Type(questionobj.getString("qbm_Type"));

                    question.setQbm_marks(questionobj.getString("qbm_marks"));
                    question.setQbm_negative_applicable(questionobj.getString("qbm_negative_applicable"));
                    question.setQbm_negative_mrk(questionobj.getString("qbm_negative_mrk"));
                    question.setQbm_question_type(questionobj.getString("qbm_question_type"));
                    question.setQbm_text_applicable(questionobj.getString("qbm_text_applicable"));
                    question.setQbm_text(questionobj.getString("qbm_text"));
                    question.setQbm_image_file(questionobj.getString("qbm_image_file"));
                    question.setQbm_video_file(questionobj.getString("qbm_video_file"));
                    question.setQbm_media_type(questionobj.getString("qbm_media_type"));
                    question.setQbm_Group_Flag(questionobj.getString("qbm_group_flag"));
//                    question.setQbm_Group_ID(questionobj.getString("qbm_Group_ID"));
//                    question.setQbm_Group_q_no(questionobj.getString("qbm_Group_q_no"));
                    question.setQbm_answer(questionobj.getString("qbm_answer"));

                    question.setQbm_review_flag(questionobj.getString("qbm_review_flag"));
                    question.setQbm_Review_Type(questionobj.getString("qbm_Review_Type"));
                    question.setQbm_Review_Images(questionobj.getString("qbm_Review_Images"));
                    question.setQbm_review_Video(questionobj.getString("qbm_review_Video"));
                    question.setQbm_Additional_Images_num(questionobj.getString("qbm_Additional_Images_num"));
                    question.setQbm_Additional_Image_ref(questionobj.getString("qbm_Additional_Image_ref"));
                    question.setGbg_id(questionobj.getString("gbg_id"));
                    question.setQbg_media_type(questionobj.getString("qbg_media_type"));
                    question.setQbg_media_file(questionobj.getString("qbg_media_file"));
                    question.setQbg_text(questionobj.getString("qbg_text"));
                    question.setQbg_no_questions(questionobj.getString("qbg_no_questions"));
                    question.setQbg_no_pick(questionobj.getString("qbg_no_pick"));
                    for (int k = 0; k < optionsArray.length(); k++) {
                        JSONObject optionJSON = optionsArray.getJSONObject(k);

                        option = new SingleOptions();
                        option.setQbo_id(optionJSON.getString("qbo_id"));
                        option.setQbo_seq_no(optionJSON.getString("qbo_seq_no"));
                        option.setQbo_type(optionJSON.getString("qbo_type"));
                        option.setQbo_text(optionJSON.getString("qbo_text"));
                        option.setQbo_media_type(optionJSON.getString("qbo_media_type"));
                        option.setQbo_media_file(optionJSON.getString("qbo_media_file"));
                        option.setQbo_answer_flag(optionJSON.getString("qbo_answer_flag"));
                        optionsList.add(option);
                        if(optionJSON.getString("qbo_answer_flag").equals("YES")) {
                            correctOptionList.add(Integer.valueOf(optionJSON.getString("qbo_seq_no")));
                            Log.e("correct : ",""+optionJSON.getString("qbo_seq_no"));
                        }
                    }

                    questionList.add(question);
                }
                sectionList.add(section);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to generate the attempt JSON object
    public void generateAttemptJSON(int pos) {

        try {

            list = new ArrayList<>();
            JSONObject attemptSection;
            JSONArray attemptquestionarray = new JSONArray();
            JSONArray attemptgrouparray = new JSONArray();
            totalArray = new JSONArray();
            attemptSection = temp.getJSONArray("sections").getJSONObject(pos);
            // generate group questions in section
            try {
                if (attemptSection.getJSONArray("groups").length() > 0) {
                    for (int i = 0; i < attemptSection.getJSONArray("groups").length(); i++) {
                        JSONObject attemptGroup = attemptSection.getJSONArray("groups").getJSONObject(i);
                        attemptgrouparray.put(attemptGroup);
                        for (int j = 0; j < attemptGroup.getJSONArray("Group_question").length(); j++) {
                            groupobj = attemptGroup.getJSONArray("Group_question").getJSONObject(j);
                            groupobj.put("qbm_sequence", Integer.toString(max));
                            max++;
                            JSONArray attemptoptionsArray = new JSONArray();
                            oplist.clear();
                            for (int b = 0; b < groupobj.getJSONArray("options").length(); b++) {
                                oplist.add(Integer.valueOf(b));
                            }
                            Collections.shuffle(oplist);
                            for (int k = 0; k < groupobj.getJSONArray("options").length(); k++) {

                                JSONObject attemptOptions = groupobj.getJSONArray("options").getJSONObject(oplist.get(k));
                                attemptoptionsArray.put(attemptOptions);

                            }
                            oplist.clear();
                            groupobj.put("options", attemptoptionsArray);
                            attemptGroup.put("groups", groupobj);
                        }

                    }
                }
                attemptSection.put("groups", attemptgrouparray);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
            for (int a = 0; a < attemptSection.getJSONArray("questions").length(); a++) {
                list.add(Integer.valueOf(a));
            }
            //non group questions in the section
            Collections.shuffle(list);
            for (int j = 0; j < attemptSection.getJSONArray("questions").length(); j++) {
                JSONArray attemptoptionsArray = new JSONArray();
                JSONObject attemptQuestion = new JSONObject();
                attemptQuestion = attemptSection.getJSONArray("questions").getJSONObject(list.get(j));
                attemptQuestion.put("qbm_sequence", Integer.toString(max));
                max++;
                totalArray.put(attemptQuestion);
                oplist = new ArrayList<>();
                for (int b = 0; b < attemptQuestion.getJSONArray("options").length(); b++) {
                    oplist.add(Integer.valueOf(b));
                }
                Collections.shuffle(oplist);
                for (int k = 0; k < attemptQuestion.getJSONArray("options").length(); k++) {

                    JSONObject attemptOptions = attemptQuestion.getJSONArray("options").getJSONObject(oplist.get(k));
                    attemptoptionsArray.put(attemptOptions);
                }
                attemptQuestion.put("options", attemptoptionsArray);
                attemptquestionarray.put(attemptQuestion);
            }
            attemptSection.put("questions", attemptquestionarray);
            attemptsectionarray.put(attemptSection);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // method to set the right drawable to the radtiobuttons
    public void setDrawable(String file, RadioButton r) {
        op = BitmapFactory.decodeFile(photoPath + file);
        drawable = new BitmapDrawable(getResources(), op);
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        drawable.setBounds(0, 0, w, h);
        r.setCompoundDrawables(drawable, null, null, null);

    }

    //method to set the questions and its options
    public void setQuestion(int pos, int index, Boolean edit) throws JSONException {

        sectionobj = attempt.getJSONArray("sections").getJSONObject(pos);
        array = generateArray(sectionobj);
        myLayoutManager.scrollToPositionWithOffset(index, 500);
        questionobj = array.getJSONObject(index);
        if (questionobj.getString("qbm_group_flag").equals("YES")) {
            btn_group_info.setEnabled(true);
            btn_group_info.setClickable(true);
            btn_group_info.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_group_info.setEnabled(false);
            btn_group_info.setClickable(false);
            btn_group_info.setBackgroundColor(0);
        }
        qAdapter.setPoiner(index);
        questionobj = array.getJSONObject(index);
        if (questionobj.getString("qbm_qimage_flag").equals("YES")) {
            btn_qadditional.setEnabled(true);
            btn_qadditional.setClickable(true);
            btn_qadditional.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_qadditional.setEnabled(false);
            btn_qadditional.setClickable(false);
            btn_qadditional.setBackgroundColor(0);

        }
        if (!edit) {
            btn_review.setEnabled(true);
            btn_review.setClickable(true);
            btn_review.setBackgroundColor(getResources().getColor(R.color.dull_yellow));
        } else {
            btn_review.setEnabled(false);
            btn_review.setClickable(false);
            btn_review.setBackgroundColor(0);
        }
        b = BitmapFactory.decodeFile(photoPath + questionobj.getString("qbm_image_file"));
        question_img.setImageBitmap(b);
        question_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

        optionsArray = questionobj.getJSONArray("options");
        optionsList = new ArrayList<>();
        for (int i = 0; i < optionsArray.length(); i++) {
            option = new SingleOptions();
            option.setQbo_id(optionsArray.getJSONObject(i).getString("qbo_id"));
            option.setQbo_media_file(optionsArray.getJSONObject(i).getString("qbo_media_file"));
            option.setQbo_seq_no(optionsArray.getJSONObject(i).getString("qbo_seq_no"));
            option.setQbo_answer_flag(optionsArray.getJSONObject(i).getString(("qbo_answer_flag")));
            optionsList.add(option);
        }

        try {
            opAdapter = new OptionsCheckAdapter(optionsList, TestActivity.this, photoPath);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rv_option.setLayoutManager(mLayoutManager);
            rv_option.setItemAnimator(new DefaultItemAnimator());
            rv_option.setAdapter(opAdapter);
            opAdapter.setOptionsEditable(edit);
//        opAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //method to store the number of questions in each section{
    public void storeSections() {
        categories = new ArrayList<String>();
        try {
            listOfLists = new ArrayList<>();
            for (int i = 0; i < attempt.getJSONArray("sections").length(); i++) {
                categories.add(attempt.getJSONArray("sections").getJSONObject(i).getString("section_Name"));
                JSONArray array2 = generateArray(attempt.getJSONArray("sections").getJSONObject(i));
                questionOpList = new ArrayList<>();
                for (int j = 0; j < array2.length(); j++) {
                    Id = array2.getJSONObject(j).getString("qbm_ID");
                    Seq = array2.getJSONObject(j).getString("qbm_sequence");
                    qListObj = new SingleQuestionList(array2.getJSONObject(j).getString("qbm_sequence"), "NOT_ATTEMPTED");
                    dataObj.InsertQuestion(Id, Seq, 0, -1, "NOT_ATTEMPTED","-1","NO");
                    questionOpList.add(qListObj);
                }
                listOfLists.add(questionOpList);
            }
            Log.e("StoreSections",""+categories.size());

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
            for (int i = 0; i < attempt.getJSONArray("sections").length(); i++) {
                categories.add(attempt.getJSONArray("sections").getJSONObject(i).getString("section_Name"));
                JSONArray array2 = generateArray(attempt.getJSONArray("sections").getJSONObject(i));
                questionOpList = new ArrayList<>();
                Log.e("array2",""+array2.length());
                for (int j = 0; j < array2.length(); j++) {
                    if(statusList.get(i) != null) {
                        qListObj = new SingleQuestionList(array2.getJSONObject(j).getString("qbm_sequence"), statusList.get(max));
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

    //method to check if the section contains any gorup questions or not and return the appropriate JSON Array
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
                        long value = dataObj.UpdateAttempt(count,1,0,0,millisRemaining,index,pos);
                        if(value>=0){
                            dataObj.InsertAttempt(1,0,0,millisRemaining,index,pos);
                        }
                        try {
                            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/"+ testid + ".json",attempt.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(TestActivity.this, ListofTests.class);
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
            Log.e("ItemSelected","reached");
            mHideRunnable.run();
            //Instantiate grid adapter
            pos = position;
            scrollAdapter = new ScrollGridAdapter(TestActivity.this, generateArray(attempt.getJSONArray("sections").getJSONObject(pos)),listOfLists.get(pos),getScreenSize());
            setScrollbar(pos);
            if (flag) {
                index = 0;
                setQuestion(position, index, edit);
            } else {
                setQuestion(position, index, edit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        try {
            Log.e("NothingSelected","reached");
            setScrollbar(0);
            pos = 0;
            mHideRunnable.run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override public void onStart() {
        Log.d(TAG, "onStart:");
        if (!checkPermission()) {
            requestPermission();
        }
        super.onStart();
    }
    @Override public void onResume() {
        Log.d(TAG, "onResume:");
        dataObj = new DBHelper(TestActivity.this);
        int count =  dataObj.getAttempCount();
        super.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause:");
        dataObj = new DBHelper(TestActivity.this);
        try {
            SaveJSONdataToFile.objectToFile(URLClass.mainpath + path + "Attempt/"+ testid + ".json",attempt.toString());
            Log.e("Attempt-Json",attempt.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        long value = dataObj.UpdateAttempt(count,1,0,0,millisRemaining,index,pos);
        if(value>=0){
            dataObj.InsertAttempt(1,0,0,millisRemaining,index,pos);
        }

        super.onPause();
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop:");
        super.onStop();
    }
    @Override public void onDestroy() {
        Log.d(TAG, "onDestroy:");
        super.onDestroy();
    }



}

