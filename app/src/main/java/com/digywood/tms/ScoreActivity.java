package com.digywood.tms;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.DBHelper.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    DBHelper dataObj;
    AlertDialog alertDialog;
    RelativeLayout rootLayout;
    String enrollid,subjectid,courseid,paperid,testId,TestType;
    TextView tv_test,tv_course,tv_subject,tv_attempted,tv_skipped,tv_bookmarked,tv_totalQuestions,tv_totalCorrect,tv_totalWrong,tv_totalNegative,tv_totalPositive,tv_totalScore,tv_totalPercentage;
    Button btn_save,btn_details;
    TableLayout tbl1;
    JSONObject attempt;
    Bundle bundle;
    Double minscore = 0.0,maxscore = 0.0,avgscore = 0.0;
    int CorrectCount = 0,WrongCount = 0,TotalCount = 0,revealX,revealY;
    Double Percentage=0.0,TotalPositive = 0.0,TotalScore = 0.0,MaxMarks= 0.0 ,TotalNegative = 0.0;
    ArrayList<Integer> OptionsList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        bundle = new Bundle();
        bundle = getIntent().getBundleExtra("BUNDLE");
        try {
            attempt = new JSONObject(bundle.getString("JSON"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        enrollid = bundle.getString("enrollid");
        subjectid = bundle.getString("subjectid");
        courseid = bundle.getString("courseid");
        paperid = bundle.getString("paperid");
        TestType = bundle.getString("Type");
        Intent intent = getIntent();
        rootLayout = findViewById(R.id.rootLayout);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra("Xreveal") &&
                intent.hasExtra("Yreveal")) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra("Xreveal", 0);
            revealY = intent.getIntExtra("Yreveal", 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
        tbl1 = findViewById(R.id.tbl_details);
        tv_test = findViewById(R.id.tv_test);
        tv_course = findViewById(R.id.tv_course);
        tv_subject = findViewById(R.id.tv_subject);
        tv_attempted = findViewById(R.id.tv_attempted);
        tv_skipped = findViewById(R.id.tv_skipped);
        tv_bookmarked = findViewById(R.id.tv_bookmarked);
        tv_totalQuestions = findViewById(R.id.tv_totalQuestions);
        tv_totalCorrect = findViewById(R.id.tv_totalCorrect);
        tv_totalWrong = findViewById(R.id.tv_totalWrong);
        tv_totalPositive = findViewById(R.id.tv_totalPositive);
        tv_totalNegative = findViewById(R.id.tv_totalNegative);
        tv_totalScore = findViewById(R.id.tv_totalScore);
        tv_totalPercentage = findViewById(R.id.tv_totalPercentage);
        btn_save = findViewById(R.id.btn_save);
        btn_details = findViewById(R.id.btn_details);
        dataObj = new DBHelper(ScoreActivity.this);

        if (TestType.equalsIgnoreCase("PRACTICE")) {
            CorrectCount = dataObj.getCorrectOptionsCount();
            TotalCount = dataObj.getQuestionAttempted()+dataObj.getQuestionBookmarked();
            WrongCount = dataObj.getWrongOptionsCount();
            try {
                if(dataObj.getQuestionAttempted() == 0){
                    TotalPositive = 0.0;
                    TotalNegative = 0.0;
                }
                else
                {
                    TotalPositive = Double.valueOf(attempt.getString("ptu_positive_marks")) * CorrectCount;
                    TotalNegative = Double.valueOf(attempt.getString("ptu_negative_marks"))* WrongCount;
                    MaxMarks = Double.valueOf(attempt.getString("ptu_positive_marks")) * dataObj.getQuestionCount();
                    Percentage = (  TotalPositive / MaxMarks )*100;
                }
                testId = attempt.getString("ptu_test_ID");
                long flag = dataObj.UpdateAttempt(dataObj.getLastAttempt(), attempt.getString("ptu_test_ID"), 2,MaxMarks, dataObj.getQuestionAttempted(), dataObj.getQuestionSkipped(), dataObj.getQuestionBookmarked(), dataObj.getQuestionNotAttempted(),Percentage , 0, 0, 0);
                Cursor mycursor=dataObj.getTestRawData(testId);
                if(mycursor.getCount()>0) {
                    while (mycursor.moveToNext()) {
                        minscore = mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                        maxscore = mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                        avgscore = mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                    }
                    Log.e("ScoreActivity-->",""+maxscore);
                }
                TotalScore = TotalPositive - TotalNegative;

    //            long tflag =
                if(flag > 0){
                    long qflag = dataObj.updateTest(testId,subjectid,courseid,dataObj.getQuestionCount(),MaxMarks,minscore,maxscore,avgscore,minscore,maxscore,avgscore);
                    if(qflag > 0){
                        //only if the data is inserted into the table, it should be displayed on screen
                        Cursor cursor = dataObj.getSections();
                        ArrayList<String> secList = new ArrayList<>();
                        if(cursor.getCount() > 0){
                            while(cursor.moveToNext()){
                                secList.add(cursor.getString(cursor.getColumnIndex("Question_Section")));
                            }
                            Log.e("Subcategories Count:",""+secList.size());
                        }
                        int count = cursor.getCount();
                        TableRow tr = null;
                        for(int i=0;i< count; i++){
                            //new row
                            tr = new TableRow(this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                            tr.setLayoutParams(lp);
                            //category name
                            TextView tv_category  = new TextView(this);
                            tv_category.setText(secList.get(i));
                            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_category.setLayoutParams(params);
                            tv_category.setGravity(Gravity.CENTER);
                            Log.d("sections",secList.get(i));
                            tv_category.setTextColor(Color.BLACK);
                            tr.addView(tv_category);
                            //Number of Questions
                            TextView tv_noOfQuestions  = new TextView(this);
                            tv_noOfQuestions.setText(String.valueOf(dataObj.getSectionQuestions(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_noOfQuestions.setLayoutParams(params);
                            tv_noOfQuestions.setGravity(Gravity.CENTER);
                            Log.d("number",String.valueOf(dataObj.getSectionQuestions(secList.get(i))));
                            tv_noOfQuestions.setTextColor(Color.BLACK);
                            tr.addView(tv_noOfQuestions);
                            //Number of Questions Attempted
                            TextView tv_subCatattempted  = new TextView(this);
                            tv_subCatattempted.setText(String.valueOf(dataObj.getSectionQuesAns(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatattempted.setLayoutParams(params);
                            tv_subCatattempted.setGravity(Gravity.CENTER);
                            Log.d("attempt",String.valueOf(dataObj.getSectionQuesAns(secList.get(i))));
                            tv_subCatattempted.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatattempted);
                            //Number of Questions Skipped
                            TextView tv_subCatskipped  = new TextView(this);
                            tv_subCatskipped.setText(String.valueOf(dataObj.getSectionQuesSkip(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatskipped.setLayoutParams(params);
                            tv_subCatskipped.setGravity(Gravity.CENTER);
                            Log.d("skip",String.valueOf(dataObj.getSectionQuesSkip(secList.get(i))));
                            tv_subCatskipped.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatskipped);
                            //Number of Questions Correct
                            TextView tv_subCatCorrect  = new TextView(this);
                            tv_subCatCorrect.setText(String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatCorrect.setLayoutParams(params);
                            tv_subCatCorrect.setGravity(Gravity.CENTER);
                            Log.d("correct",String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            tv_subCatCorrect.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatCorrect);
                            //Percentage Score
                            TextView tv_percentage  = new TextView(this);
                            tv_percentage.setText(String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_percentage.setLayoutParams(params);
                            tv_percentage.setGravity(Gravity.CENTER);
                            Log.d("%",String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            tv_percentage.setTextColor(Color.BLACK);
                            tr.addView(tv_percentage);

                            tbl1.addView(tr);
                        }

                    }
                }

                tv_test.setText(testId);
            } catch (JSONException|NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            CorrectCount = dataObj.getAssessmentCorrectOptionsCount();
            TotalCount = dataObj.getAssessmentQuestionAttempted()+dataObj.getAssessmentQuestionBookmarked();
            WrongCount = dataObj.getAssessmentWrongOptionsCount();
            try {
                if(dataObj.getQuestionAttempted() == 0){
                    TotalPositive = 0.0;
                    TotalNegative = 0.0;
                }
                else
                {
                    TotalPositive = Double.valueOf(attempt.getString("ptu_positive_marks")) * CorrectCount;
                    TotalNegative = Double.valueOf(attempt.getString("ptu_negative_marks"))* WrongCount;
                    MaxMarks = Double.valueOf(attempt.getString("ptu_positive_marks")) * dataObj.getAssessmentQuestionCount();
                    Percentage = (  TotalPositive / MaxMarks )*100;
                }
                testId = attempt.getString("ptu_test_ID");
                long flag = dataObj.UpdateAttempt(dataObj.getLastAttempt(), attempt.getString("ptu_test_ID"), 2,MaxMarks, dataObj.getAssessmentQuestionAttempted(), dataObj.getAssessmentQuestionSkipped(), dataObj.getAssessmentQuestionBookmarked(), dataObj.getAssessmentQuestionNotAttempted(),Percentage , 0, 0, 0);
                Cursor mycursor=dataObj.getTestRawData(testId);
                if(mycursor.getCount()>0) {
                    while (mycursor.moveToNext()) {
                        minscore = mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                        maxscore = mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                        avgscore = mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                    }
                    Log.e("ScoreActivity-->",""+maxscore);
                }
                TotalScore = TotalPositive - TotalNegative;

                //            long tflag =
                if(flag > 0){
                    long qflag = dataObj.updateTest(testId,subjectid,courseid,dataObj.getQuestionCount(),MaxMarks,minscore,maxscore,avgscore,minscore,maxscore,avgscore);
                    if(qflag > 0){
                        //only if the data is inserted into the table, it should be displayed on screen
                        Cursor cursor = dataObj.getSections();
                        ArrayList<String> secList = new ArrayList<>();
                        if(cursor.getCount() > 0){
                            while(cursor.moveToNext()){
                                secList.add(cursor.getString(cursor.getColumnIndex("Question_Section")));
                            }
                            Log.e("Subcategories Count:",""+secList.size());
                        }
                        int count = cursor.getCount();
                        TableRow tr = null;
                        for(int i=0;i< count; i++){
                            //new row
                            tr = new TableRow(this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                            tr.setLayoutParams(lp);
                            //category name
                            TextView tv_category  = new TextView(this);
                            tv_category.setText(secList.get(i));
                            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_category.setLayoutParams(params);
                            tv_category.setGravity(Gravity.CENTER);
                            Log.d("sections",secList.get(i));
                            tv_category.setTextColor(Color.BLACK);
                            tr.addView(tv_category);
                            //Number of Questions
                            TextView tv_noOfQuestions  = new TextView(this);
                            tv_noOfQuestions.setText(String.valueOf(dataObj.getSectionQuestions(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_noOfQuestions.setLayoutParams(params);
                            tv_noOfQuestions.setGravity(Gravity.CENTER);
                            Log.d("number",String.valueOf(dataObj.getSectionQuestions(secList.get(i))));
                            tv_noOfQuestions.setTextColor(Color.BLACK);
                            tr.addView(tv_noOfQuestions);
                            //Number of Questions Attempted
                            TextView tv_subCatattempted  = new TextView(this);
                            tv_subCatattempted.setText(String.valueOf(dataObj.getSectionQuesAns(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatattempted.setLayoutParams(params);
                            tv_subCatattempted.setGravity(Gravity.CENTER);
                            Log.d("attempt",String.valueOf(dataObj.getSectionQuesAns(secList.get(i))));
                            tv_subCatattempted.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatattempted);
                            //Number of Questions Skipped
                            TextView tv_subCatskipped  = new TextView(this);
                            tv_subCatskipped.setText(String.valueOf(dataObj.getSectionQuesSkip(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatskipped.setLayoutParams(params);
                            tv_subCatskipped.setGravity(Gravity.CENTER);
                            Log.d("skip",String.valueOf(dataObj.getSectionQuesSkip(secList.get(i))));
                            tv_subCatskipped.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatskipped);
                            //Number of Questions Correct
                            TextView tv_subCatCorrect  = new TextView(this);
                            tv_subCatCorrect.setText(String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_subCatCorrect.setLayoutParams(params);
                            tv_subCatCorrect.setGravity(Gravity.CENTER);
                            Log.d("correct",String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            tv_subCatCorrect.setTextColor(Color.BLACK);
                            tr.addView(tv_subCatCorrect);
                            //Percentage Score
                            TextView tv_percentage  = new TextView(this);
                            tv_percentage.setText(String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                            tv_percentage.setLayoutParams(params);
                            tv_percentage.setGravity(Gravity.CENTER);
                            Log.d("%",String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i))));
                            tv_percentage.setTextColor(Color.BLACK);
                            tr.addView(tv_percentage);

                            tbl1.addView(tr);
                        }

                    }
                }

                tv_test.setText(testId);
            } catch (JSONException|NumberFormatException e) {
                e.printStackTrace();
            }
        }

        tv_course.setText(courseid);
        tv_subject.setText(subjectid);
        tv_attempted.setText(String.valueOf(dataObj.getQuestionAttempted()));
        tv_skipped.setText(String.valueOf(dataObj.getQuestionSkipped()));
        tv_bookmarked.setText(String.valueOf(dataObj.getQuestionBookmarked()));
        tv_totalQuestions.setText(String.valueOf(dataObj.getQuestionCount()));
        tv_totalCorrect.setText(String.valueOf(dataObj.getCorrectOptionsCount()));
        tv_totalWrong.setText(String.valueOf(WrongCount));
        tv_totalPositive.setText(String.valueOf(TotalPositive));
        tv_totalNegative.setText(String.valueOf(TotalNegative));
        tv_totalScore.setText(String.valueOf(TotalScore));
        tv_totalPercentage.setText(String.format("%.1f", Percentage) + " %");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ListofPractiseTests.class);
                intent.putExtra("enrollid",enrollid);
                intent.putExtra("courseid", courseid);
                intent.putExtra("paperid",paperid);
                startActivity(intent);
                finish();
            }
        });
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dataObj.getSubcategories();
                ArrayList<String> catList = new ArrayList<>();
                Log.e("ScoreCount",""+cursor.getCount());
                if(cursor.getCount()>0) {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        catList.add(cursor.getString(cursor.getColumnIndex("Question_Section")));
                    }
                    initiateFullScreenWindow(cursor.getCount(),catList );
                }
            }
        });

    }
    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, 0, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());
            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
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
        finish();
    }

    public void initiateFullScreenWindow(int count, ArrayList<String> secList) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) ScoreActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.detailedscoreview, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        TableLayout tbl = layout.findViewById(R.id.tbl_score_details);
        TableRow tr1 = null;
        for(int i=0;i< count; i++){
            //new row
            tr1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            tr1.setLayoutParams(lp);
            //category name
            TextView tv_category  = new TextView(this);
            tv_category.setText(secList.get(i));
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_category.setLayoutParams(params);
            tv_category.setGravity(Gravity.CENTER);
            tv_category.setTextColor(Color.BLACK);
            //category name
            TextView tv_subcat  = new TextView(this);
            tv_category.setText(secList.get(i));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_category.setLayoutParams(params);
            tv_category.setGravity(Gravity.CENTER);
            tv_category.setTextColor(Color.BLACK);
            tr1.addView(tv_category);
            //Number of Questions
            TextView tv_noOfQuestions  = new TextView(this);
            tv_noOfQuestions.setText(String.valueOf(dataObj.getSubCatQuestions(secList.get(i))));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_noOfQuestions.setLayoutParams(params);
            tv_noOfQuestions.setGravity(Gravity.CENTER);
            tv_noOfQuestions.setTextColor(Color.BLACK);
            tr1.addView(tv_noOfQuestions);
            //Number of Questions Attempted
            TextView tv_subCatattempted  = new TextView(this);
            tv_subCatattempted.setText(String.valueOf(dataObj.getSubCatQuesAns(secList.get(i))));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatattempted.setLayoutParams(params);
            tv_subCatattempted.setGravity(Gravity.CENTER);
            tv_subCatattempted.setTextColor(Color.BLACK);
            tr1.addView(tv_subCatattempted);
            //Number of Questions Skipped
            TextView tv_subCatskipped  = new TextView(this);
            tv_subCatskipped.setText(String.valueOf(dataObj.getSubCatQuesSkip(secList.get(i))));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatskipped.setLayoutParams(params);
            tv_subCatskipped.setGravity(Gravity.CENTER);
            tv_subCatskipped.setTextColor(Color.BLACK);
            tr1.addView(tv_subCatskipped);
            //Number of Questions Correct
            TextView tv_subCatCorrect  = new TextView(this);
            tv_subCatCorrect.setText(String.valueOf(dataObj.getSubCatQuesCorrect(secList.get(i))));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatCorrect.setLayoutParams(params);
            tv_subCatCorrect.setGravity(Gravity.CENTER);
            tv_subCatCorrect.setTextColor(Color.BLACK);
            tr1.addView(tv_subCatCorrect);
            //Percentage Score
            TextView tv_percentage  = new TextView(this);
            tv_percentage.setText(String.valueOf(dataObj.getSubCatQuesCorrect(secList.get(i))));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_percentage.setLayoutParams(params);
            tv_percentage.setGravity(Gravity.CENTER);
            tv_percentage.setTextColor(Color.BLACK);
            tr1.addView(tv_percentage);

            tbl.addView(tr1);
        }

        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }

}
