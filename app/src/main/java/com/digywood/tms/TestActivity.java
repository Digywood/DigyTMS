package com.digywood.tms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.digywood.tms.Adapters.OptionsCheckAdapter;
import com.digywood.tms.Adapters.QuestionListAdapter;
import com.digywood.tms.Adapters.ScrollGridAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.Pojo.SingleQuestion;
import com.digywood.tms.Pojo.SingleSections;

public class TestActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    LinearLayout lt;
    TextView timer,number,date;
    public static File file;
    ImageView fullscreen,menu;
    private PopupWindow pw;
    GridView gridView;
    Spinner sections;
    String Seq,Id,path,enrollid,courseid,subjectId,paperid,testid;
    EncryptDecrypt encObj;
    static  int position = 0;
    RecyclerView question_scroll;
    ScrollGridAdapter scrollAdapter;
    QuestionListAdapter qAdapter;
    LinearLayoutManager myLayoutManager,LM_Option;
    ArrayAdapter adapter;
    RecyclerView rv_option;
    HorizontalScrollView hscrollview;
    ArrayList<Integer> oplist = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<Integer> marked = new ArrayList<>();
    ArrayList<Integer> answered = new ArrayList<>();
    ArrayList<Integer> optionsTemp = new ArrayList<>();
    static TextView[] myTextViews = new TextView[101];
    ArrayList<String> q_list=new ArrayList<>();
    Button btn_prev, btn_next;
    ImageView question_img;
    FloatingActionButton questionData;
    Drawable drawable;
    Bitmap b, op;
    Switch markSwitch;
    Boolean flag = true;
    JSONObject obj;
    static int index = 0,pos = 0,max = 1,grp = 0;
    JSONObject sectionobj, groupobj, questionobj, temp;
    public static JSONObject attempt;
    JSONArray array, optionsArray, totalArray,groupArray, sectionArray, attemptsectionarray,buffer;
    ArrayList<SingleSections> sectionList = new ArrayList<>();
    ArrayList<SingleQuestion> questionList = new ArrayList<>();
    ArrayList<Integer> questionNumberList = new ArrayList<>();
    ArrayList<SingleOptions> optionsList = new ArrayList<>();
    SingleQuestion question = new SingleQuestion();
    SingleSections section = new SingleSections();
    SingleOptions option;
    OptionsCheckAdapter opAdapter;
    SaveJSONdataToFile save;
    DBHelper dataObj;
    ArrayList<String> categories;
    String jsonPath,photoPath;

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
//        samplequestion = new ArrayList();
       /* for (int i=1 ; i< 21 ;i++){
            samplequestion.add((Integer)i);
        }*/
        dataObj = new DBHelper(this);
        dataObj.Destroy("attempt_data");
        question_scroll = findViewById(R.id.question_scroll);
        qAdapter = new QuestionListAdapter(q_list,TestActivity.this);
        myLayoutManager = new LinearLayoutManager(TestActivity.this, LinearLayoutManager.HORIZONTAL,false);
        question_scroll.setLayoutManager(myLayoutManager);
        question_scroll.setItemAnimator(new DefaultItemAnimator());
        question_scroll.setAdapter(qAdapter);
//        testid = getIntent().getStringExtra("TestId");
        rv_option = findViewById(R.id.option_view);
//        Log.e("TestName--->",testid);
        LayoutInflater factory = LayoutInflater.from(this);

        testid = "PTAA00002";
        Cursor cursor =  dataObj.getStudentTests();
        if(cursor.getCount() >0){
            while (cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex("sptu_ID")).equals(testid)){
                    enrollid = cursor.getString(cursor.getColumnIndex("sptu_entroll_id"));
                    courseid = cursor.getString(cursor.getColumnIndex("sptu_course_id"));
                    subjectId = cursor.getString(cursor.getColumnIndex("sptu_subjet_ID"));
                    paperid = cursor.getString(cursor.getColumnIndex("sptu_paper_ID"));
                }
            }
        }

        path = enrollid+"/"+courseid+"/"+subjectId+"/"+paperid+"/"+testid+"/";
        photoPath = URLClass.mainpath+path;
        jsonPath = URLClass.mainpath+path+testid+".json";
        question_img = findViewById(R.id.question_img);
        btn_prev = findViewById(R.id.prev_btn);
        btn_next = findViewById(R.id.next_btn);
        markSwitch = findViewById(R.id.mark_switch);
        questionData = findViewById(R.id.question_Data);
        save = new SaveJSONdataToFile();
        temp = new JSONObject();

        sectionArray = new JSONArray();
//        Log.e("jsonFile--->",jsonPath);
        try {
            String json = new String(SaveJSONdataToFile.bytesFromFile(jsonPath), "UTF-8");
//            Log.e("jsonFile--->",json);
            obj = new JSONObject(json);
            parseJson(obj);
            encObj = new EncryptDecrypt();
            attemptsectionarray = new JSONArray();

            attempt = new JSONObject();
            for (int i = 0; i < 2; i++) {
                Collections.shuffle(list);
                Collections.shuffle(oplist);
                questionNumberList.clear();
                generateAttemptJSON(i);
            }
            attempt.put("sections", attemptsectionarray);
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));

            Log.e("Testing--->", ""+attempt.getJSONArray("sections").length());
            storeSections();
            SaveJSONdataToFile.objectToFile(attempt.toString());
            encObj.getFileToEncrypt(attempt.toString(), photoPath + "Attempt/");
            setScrollbar(pos);

        } catch (JSONException|IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        timer = findViewById(R.id.timer);
        hscrollview = findViewById(R.id.scroll);
        sections = findViewById(R.id.sections);

        sections.setOnItemSelectedListener(this);
//        setScrollbar();
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sections.setAdapter(adapter);

        fullscreen = findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(v);
                hscrollview.setVisibility(v.INVISIBLE);
            }
        });
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateMenuWindow(v);
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    flag = true;
//                    writeOption();

                    optionsTemp = dataObj.getQuestion();
                    if (index < buffer.length()) {
                        index++;

                        Id = buffer.getJSONObject(index).getString("qbm_ID");
                        Seq = buffer.getJSONObject(index).getString("qbm_sequence");
                        Log.e("bufferArray:--->","called "+index);
                        setQuestion(pos, index);
                        if(index == buffer.length()-1) {
                            index++;
                        }
//                        checkRadio(dataObj.getPosition(Id));
                    }
                    else if(index == buffer.length()){
                        pos = pos+1;
                        index = 0;
                        buffer =  generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                        sections.setSelection(pos);
                        setScrollbar(pos);
                        Log.e("question-->",""+index);

                        //Change button once last question of test is reached
                        if(pos == attemptsectionarray.length()-1){
                            index++;
                            btn_next.setText("Finish");
//                            btn_next.callOnClick();
                        }
                        qAdapter.setData(index);
                    }
                    else{
                        optionsTemp = dataObj.getQuestion();
                        for(int i=0; i< dataObj.getQuestionCount(); i++){
                            Log.e("Options",""+optionsTemp.get(i));
                        }
                        AlertDialog alertbox = new AlertDialog.Builder(TestActivity.this)
                                .setMessage("Do you want to finish Test?"+" "+dataObj.getQuestionCount())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        q_list.clear();
                                        finish();
                                        Intent intent = new Intent(TestActivity.this, ScoreActivity.class);
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
                    }
//                    Log.e("Values--->",""+pos+","+index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

//                    writeOption();

                    if(index > 0) {
                        index--;
                        buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                        Id = buffer.getJSONObject(index).getString("qbm_ID");
                        Seq = buffer.getJSONObject(index).getString("qbm_sequence");
                        Log.e("QuestionId:",Id);
                        flag = true;
                        setQuestion(pos, index);
                    }

                    else if (index == 0 && pos > 0){
                        pos = pos-1;
                        index = generateArray(attemptsectionarray.getJSONObject(pos)).length()-1;
                        Log.e("bufferArray:--->","called " +buffer.length()+"~"+index);
                        flag = false;
                        sections.setSelection(pos);
                    }
//                    checkRadio(dataObj.getPosition(Id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                qAdapter.setData(Integer.valueOf(q_list.get(index)));

            }
        });
        question_scroll.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),question_scroll,new RecyclerTouchListener.OnItemClickListener(){
            @Override
            public void onClick(View view, int in) {
                try {

//                    writeOption();
                    index = in;
//                    qAdapter.setBackground(index);
                    buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
                    Id = buffer.getJSONObject(index).getString("qbm_ID");
                    if(index == buffer.length()-1)
                        index++;
                    setQuestion(pos,in);
//                    checkRadio(dataObj.getPosition(Id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

//        date = findViewById(R.id.date);
//        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String check = dateFormat.format(dt);
//        date.setText(check);

        //Setting countdown timer
        new CountDownTimer(3600000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timer.setText(text);
            }

            public void onFinish() {
                timer.setText("Time Up!");
//                timer.setVisibility(View.INVISIBLE);
            }
        }.start();

        String MyDecImgs=android.os.Environment.getExternalStorageDirectory().toString()+ "/DigyTMS/Decrypted/";
        File decdir = new File(MyDecImgs);
        if (!decdir.exists()) {
            decdir.mkdirs();
        }
        String MyImgs=android.os.Environment.getExternalStorageDirectory().toString()+ "/DigyTMS/Encrypted/";
        File dir = new File(MyImgs);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        else {
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

    //method to set a horizantal scrollbar containing question numbers of the current section
    private void setScrollbar(int position)throws JSONException{
        q_list.clear();
        pos = position;
        JSONArray array2 = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
        for (int j = 0; j < array2.length(); j++){
            q_list.add(array2.getJSONObject(j).getString("qbm_sequence"));
        }
        qAdapter.notifyDataSetChanged();

    }

    //method to create a popup window containing question numbers
    private void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) TestActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_screen,
                    (ViewGroup) findViewById(R.id.popup_element));
            //Instantiate grid view
            gridView= layout.findViewById(R.id.scroll_grid);
            //Instantiate grid adapter
            scrollAdapter= new ScrollGridAdapter(TestActivity.this, generateArray(attempt.getJSONArray("sections").getJSONObject(pos)),marked,answered);
            //Setting Adapter to gridview
            gridView.setAdapter(scrollAdapter);
            // create a 300px width and 570px height PopupWindow
            pw = new PopupWindow(layout, 570, 400, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);
            if(android.os.Build.VERSION.SDK_INT > 20) {
                pw.setElevation(10);
            }
//            TextView mResultText = (TextView) layout.findViewById(R.id.server_status_text);
            Button cancelButton = (Button) layout.findViewById(R.id.close_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                    hscrollview.setVisibility(v.VISIBLE);
                    mHideRunnable.run();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to check the radiobutton based on its position in the group
/*    public void checkRadio(int pos){
        switch (pos){
            case 1: op1.setChecked(true);
                    break;
            case 2: op2.setChecked(true);
                    break;
            case 3: op3.setChecked(true);
                    break;
            case 4: op4.setChecked(true);
                    break;
            default: break;
        }
    }*/

    //method to store the selected option
/*    public void writeOption(){
//        RadioButton random = findViewById(group.getCheckedRadioButtonId());
        int indx = group.indexOfChild(random) + 1;
        try {
            buffer = generateArray(attempt.getJSONArray("sections").getJSONObject(pos));
            Id = buffer.getJSONObject(index).getString("qbm_ID");
            Seq = buffer.getJSONObject(index).getString("qbm_sequence");
            if(dataObj.CheckQuestion(Id)){
                if(indx > 0) {
                    dataObj.UpdateQuestion(Id, Seq, Integer.valueOf(questionobj.getString("qbm_marks")), indx);
                    Log.e("returns", "Updated," + dataObj.getPosition(Id));
                }
            }else{
                dataObj.InsertQuestion(Id,Seq,Integer.valueOf(questionobj.getString("qbm_marks")),indx);
                Log.e("returns","Inserted'"+dataObj.getPosition(Id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        group.clearCheck();
    }*/

    //method to create a menu window
    private void initiateMenuWindow(View v) {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) TestActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.menu,null);
//            menulist = layout.findViewById(R.id.menu_list);
//            menuAdapter = new MenuAdapter(TestActivity.this,"","","");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(layout);

            Button cancelButton = (Button) layout.findViewById(R.id.close_button);
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
                for(int g=0;g< sectionobj.getJSONArray("groups").length();g++){
                    groupobj = array.getJSONObject(g);
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
                    question.setQbm_Group_Flag(questionobj.getString("qbm_Group_Flag"));
                    question.setQbm_Group_ID(questionobj.getString("qbm_Group_ID"));
                    question.setQbm_Group_q_no(questionobj.getString("qbm_Group_q_no"));
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
//                        optionsList.add(option);
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
                if(attemptSection.getJSONArray("groups").length()>0){
                    for (int i =0 ; i<attemptSection.getJSONArray("groups").length(); i++){
                        JSONObject attemptGroup = attemptSection.getJSONArray("groups").getJSONObject(i);
                        attemptgrouparray.put(attemptGroup);
                        for(int j = 0;j < attemptGroup.getJSONArray("Group_question").length(); j++){
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
                            attemptGroup.put("groups",groupobj);
                        }

                    }
                }
                attemptSection.put("groups",attemptgrouparray);
            } catch (JSONException|NullPointerException e) {
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
    public void setQuestion(int pos, int index) throws JSONException {
        sectionobj = attempt.getJSONArray("sections").getJSONObject(pos);
        array = generateArray(sectionobj);
        questionobj = array.getJSONObject(index);
        if(questionobj.getString("qbm_group_flag").equals("YES")){
            questionData.setVisibility(View.VISIBLE);
        }else{
            questionData.setVisibility(View.INVISIBLE);
        }
        if(marked.contains(Integer.valueOf(questionobj.getString("qbm_sequence")))){

        }
        b = BitmapFactory.decodeFile(photoPath + questionobj.getString("qbm_image_file"));
        question_img.setImageBitmap(b);
        optionsArray = questionobj.getJSONArray("options");
        Log.e("imageFile--->",optionsArray.getJSONObject(0).getString("qbo_media_file"));
        /*setDrawable(optionsArray.getJSONObject(0).getString("qbo_media_file"), op1);
        setDrawable(optionsArray.getJSONObject(1).getString("qbo_media_file"), op2);
        setDrawable(optionsArray.getJSONObject(2).getString("qbo_media_file"), op3);
        setDrawable(optionsArray.getJSONObject(3).getString("qbo_media_file"), op4);*/

        optionsList = new ArrayList<>();
        for(int i=0;i< optionsArray.length();i++){
            option = new SingleOptions();
            option.setQbo_id(optionsArray.getJSONObject(i).getString("qbo_id"));
            option.setQbo_media_file(optionsArray.getJSONObject(i).getString("qbo_media_file"));
            option.setQbo_seq_no(optionsArray.getJSONObject(i).getString("qbo_seq_no"));
            optionsList.add(option);
            Log.e("TestActivity:-->",optionsList.get(i).getQbo_media_file());
        }


        opAdapter = new OptionsCheckAdapter(optionsList,TestActivity.this,photoPath);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_option.setLayoutManager(mLayoutManager);
        //rv_option.setItemAnimator(new DefaultItemAnimator());
        rv_option.setAdapter(opAdapter);
//        opAdapter.notifyDataSetChanged();
    }

    //method to store the number of questions in each section{
    public void storeSections() {
        categories = new ArrayList<String>();
        try {
            for (int i = 0; i < attempt.getJSONArray("sections").length(); i++) {
                categories.add(attempt.getJSONArray("sections").getJSONObject(i).getString("section_Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //method to check if the section contains any gorup questions or not and return the appropriate JSON Array
    public JSONArray generateArray(JSONObject Section){
        JSONArray array = new JSONArray();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();
        int length = 0;
        grp = 0;
        ArrayList<JSONArray> jaList = new ArrayList<>();
        try {
            if(Section.has("groups")) {
                groupArray = Section.getJSONArray("groups");
                for (int i=0;i < groupArray.length();i++) {
                    array1 = groupArray.getJSONObject(i).getJSONArray("Group_question");
                    jaList.add(array1);
                }
                for(int i =0; i<Section.getJSONArray("questions").length(); i++ ){
                    array2.put(grp,Section.getJSONArray("questions").get(i));
                    grp++;
                }
                jaList.add(array2);
                array = getMergeJsonArrays(jaList);
            }
            else
            {array = Section.getJSONArray("questions");}


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    //method to merge 2 JSON Arrays and return the result
    public JSONArray getMergeJsonArrays(ArrayList<JSONArray> jsonArrays) throws JSONException {
        JSONArray MergedJsonArrays= new JSONArray();
        for(JSONArray tmpArray:jsonArrays)
        {
            for(int i=0;i<tmpArray.length();i++)
            {
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
                .setMessage("Do you want to exit Test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(TestActivity.this,ListofTests.class);
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

    public static void setCurrentPos(int pos){
        myTextViews[pos].setBackgroundResource(0);
        myTextViews[pos].setBackgroundResource(R.drawable.answer);
    }

    @Override
    protected void onPostResume() {
        mHideRunnable.run();
        super.onPostResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            mHideRunnable.run();
            pos = position;
            setScrollbar(pos);
            if (flag){
                index = 0;
                setQuestion(position,index);
            }
            else{
                setQuestion(position,index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        try {
            setScrollbar(0);
            pos = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

