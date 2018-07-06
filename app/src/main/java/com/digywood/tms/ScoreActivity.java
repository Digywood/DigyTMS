package com.digywood.tms;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    DBHelper dataObj;
    AlertDialog alertDialog;
    RelativeLayout rootLayout;
    String instanceId, studentId, enrollid, subjectid, courseid, paperid, testId, testType;
    String jsonPath, path, sec_questions, sec_attempted, sec_skipped, sec_correct, sec_wrong, sec_percentage, subcat_questions, subcat_attempted, subcat_skipped, subcat_correct, subcat_percentage;
    TextView tv_test, tv_course, tv_subject, tv_attempted, tv_skipped, tv_bookmarked, tv_totalQuestions, tv_totalCorrect, tv_totalWrong, tv_totalNegative, tv_totalPositive, tv_totalScore, tv_totalPercentage;
    Button btn_save, btn_details;
    TableLayout tbl1;
    JSONObject attempt;
    Bundle bundle;
    Cursor cursor;
    Double minscore = 0.0, maxscore = 0.0, avgscore = 0.0;
    int CorrectCount = 0, WrongCount = 0, TotalCount = 0, revealX, revealY;
    Double Percentage = 0.0, TotalPositive = 0.0, TotalScore = 0.0, MaxMarks = 0.0, TotalNegative = 0.0;
    ArrayList<Integer> OptionsList = new ArrayList<>();
    ArrayList<String> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        bundle = new Bundle();
        bundle = getIntent().getBundleExtra("BUNDLE");
        if (PracticeTestActivity.pactivity != null) {
            PracticeTestActivity.pactivity.finish();
        }

        if (AssessmentTestActivity.Aactivity != null) {
            AssessmentTestActivity.Aactivity.finish();
        }


        enrollid = bundle.getString("enrollid");
        subjectid = bundle.getString("subjectid");
        courseid = bundle.getString("courseid");
        paperid = bundle.getString("paperid");
        testType = bundle.getString("Type");
        Intent intent = getIntent();
        studentId = intent.getStringExtra("studentid");
        instanceId = intent.getStringExtra("instanceid");
        testId = intent.getStringExtra("testid");
        path = enrollid + "/" + courseid + "/" + subjectid + "/" + paperid + "/" + testId + "/";

        if(testType.equalsIgnoreCase("PRACTICE")) {
            jsonPath = URLClass.mainpath + path + "Attempt/" + testId + ".json";
        }
        else
        {
            jsonPath = URLClass.mainpath + path + testId + ".json";
        }
        try {
            Log.e("ScoreActivity","JsonPAth:"+jsonPath);
            Log.e("ScoreActivity","instanceid:"+instanceId);
            Log.e("ScoreActivity","testid:"+testId);
            String json = new String(SaveJSONdataToFile.bytesFromFile(jsonPath), "UTF-8");
            attempt = new JSONObject(json);
        } catch (JSONException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        Log.e("stutest-->", studentId);
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

        try {
            long flag = 0;
            if (testType.equalsIgnoreCase("PRACTICE")) {
                CorrectCount = dataObj.getCorrectOptionsCount(testId);
                TotalCount = dataObj.getQuestionAttempted(testId) + dataObj.getQuestionBookmarked(testId);
                WrongCount = dataObj.getWrongOptionsCount(testId);

                TotalPositive = Double.valueOf(attempt.getString("ptu_positive_marks")) * CorrectCount;

                TotalNegative = Double.valueOf(attempt.getString("ptu_negative_marks")) * WrongCount;
                Log.e("negative_score", attempt.getString("ptu_negative_marks"));
                MaxMarks = Double.valueOf(attempt.getString("ptu_positive_marks")) * dataObj.getTestQuestionCount(testId);
                TotalScore = TotalPositive - TotalNegative;
                Percentage = (Double.valueOf(TotalScore) / MaxMarks) * 100;


                flag = dataObj.UpdateAttempt(dataObj.getLastTestAttempt(testId, studentId), attempt.getString("ptu_test_ID"), 2, "NotUploaded", TotalScore, dataObj.getTestQuestionAttempted(testId), dataObj.getTestQuestionSkipped(testId), dataObj.getTestQuestionBookmarked(testId), dataObj.getTestQuestionNotAttempted(testId), Percentage, 0, 0, 0);
                Cursor mycursor = dataObj.getTestRawData(testId, studentId, enrollid);
                if (mycursor.getCount() > 0) {
                    while (mycursor.moveToNext()) {
                        minscore = mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                        maxscore = mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                        avgscore = mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                    }
                }

            } else {
                testId = attempt.getString("atu_ID");
                CorrectCount = dataObj.getAssessmentCorrectOptionsCount(testId,instanceId,studentId);
                int qes_atmt=dataObj.getAssessmentQuestionAttempted(testId,instanceId,studentId);
                int qes_bookMarked=dataObj.getAssessmentQuestionBookmarked(testId,instanceId,studentId);
                TotalCount = qes_atmt + qes_bookMarked;

                WrongCount = dataObj.getAssessmentWrongOptionsCount(testId,instanceId,studentId);
                if (qes_atmt == 0) {
                    TotalPositive = 0.0;
                    TotalNegative = 0.0;
                } else {
                    TotalPositive = Double.valueOf(attempt.getString("atu_marks")) * CorrectCount;
                    TotalNegative = Double.valueOf(attempt.getString("atu_negative_mrk")) * WrongCount;
                    MaxMarks = Double.valueOf(attempt.getString("atu_marks")) * dataObj.getAssessmentQuestionCount(testId);
                    TotalScore = TotalPositive - TotalNegative;
                    Percentage = (TotalScore / MaxMarks) * 100;
                }

                flag = dataObj.UpdateAssessment(attempt.getString("atu_ID"), instanceId, enrollid, "", courseid, subjectid, paperid, 2, null, TotalScore,qes_atmt, dataObj.getAssessmentQuestionSkipped(testId,instanceId,studentId), qes_bookMarked, dataObj.getAssessmentQuestionNotAttempted(testId,instanceId,studentId), Percentage, 0, 0, 0);
                Cursor mycursor = dataObj.getAssessmentRawData(testId);
                if (mycursor.getCount() > 0) {
                    while (mycursor.moveToNext()) {
                        minscore = mycursor.getDouble(mycursor.getColumnIndex("minscore"));
                        maxscore = mycursor.getDouble(mycursor.getColumnIndex("maxscore"));
                        avgscore = mycursor.getDouble(mycursor.getColumnIndex("avgscore"));
                    }
                }
            }
            long qflag = 0;
            if (flag > 0) {
                if (testType.equalsIgnoreCase("PRACTICE")) {
                    qflag = dataObj.updateTest(testId, subjectid, courseid, dataObj.getQuestionCount(testId), MaxMarks, minscore, maxscore, avgscore, minscore, maxscore, avgscore, "NotUploaded");
                } else {
                    qflag = dataObj.updateAssessmentTestRecord(testId, subjectid, courseid, dataObj.getQuestionCount(testId), MaxMarks, minscore, maxscore, avgscore, minscore, maxscore, avgscore);

                }
                if (qflag > 0) {
                    //only if the data is inserted into the table, it should be displayed on screen
                    if (testType.equalsIgnoreCase("PRACTICE")) {
                        cursor = dataObj.getSections(testId);
                    } else {
                        cursor = dataObj.getAssessmentSections(testId);
                    }
                    ArrayList<String> secList = new ArrayList<>();
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            secList.add(cursor.getString(cursor.getColumnIndex("Question_Section")));
                        }
                    }

                    int count = cursor.getCount();
                    TableRow tr = null;
                    for (int i = 0; i < count; i++) {
                        Double percent = 0.0, score = 0.0;
                        //new row
                        if (testType.equalsIgnoreCase("PRACTICE")) {
                            sec_questions = String.valueOf(dataObj.getSectionQuestions(secList.get(i), testId));
                            sec_attempted = String.valueOf(dataObj.getSectionQuesAns(secList.get(i), testId));
                            sec_skipped = String.valueOf(dataObj.getSectionQuesSkip(secList.get(i), testId));
                            sec_correct = String.valueOf(dataObj.getSectionQuesCorrect(secList.get(i), testId));
                            Log.e("SectionData", secList.get(i));
                            sec_wrong = String.valueOf(dataObj.getSectionQuesWrong(secList.get(i), testId));
                            score = (Double.valueOf(sec_correct) * Double.valueOf(attempt.getString("ptu_positive_marks")) - Double.valueOf(sec_wrong) * Double.valueOf(attempt.getString("ptu_negative_marks")));
                            percent = (Double.valueOf(score) / Double.valueOf(sec_questions)) * 100;
                            sec_percentage = String.format("%.2f", percent);
                            Log.e("sectionquestions", sec_questions);
                            Log.e("sectionattempted", sec_attempted);
                            Log.e("seccorrect", sec_correct);
                        } else {
                            sec_questions = String.valueOf(dataObj.getAssessmentSectionQuestions(secList.get(i),testId,instanceId,studentId));
                            sec_attempted = String.valueOf(dataObj.getAssessmentSectionQuesAns(secList.get(i),testId,instanceId,studentId));
                            sec_skipped = String.valueOf(dataObj.getAssessmentSectionQuesSkip(secList.get(i),testId,instanceId,studentId));
                            sec_correct = String.valueOf(dataObj.getAssessmentSectionQuesCorrect(secList.get(i),testId,instanceId,studentId));
                            sec_wrong = String.valueOf(dataObj.getAssessmentSectionQuesWrong(secList.get(i),testId,instanceId,studentId));
                            score = (Double.valueOf(sec_correct) * Double.valueOf(attempt.getString("atu_marks")) - Double.valueOf(sec_wrong) * Double.valueOf(attempt.getString("atu_negative_mrk")));
                            percent = (Double.valueOf(score) / Double.valueOf(sec_questions)) * 100;
                            sec_percentage = String.format("%.2f", percent);
                            Log.e("sectionquestions", sec_questions);
                            Log.e("sectionattempted", sec_attempted);
                            Log.e("seccorrect", sec_correct);
                        }
                        tr = new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        tr.setLayoutParams(lp);
                        tr.setWeightSum(7f);
                        //category name
                        TextView tv_category = new TextView(this);
                        tv_category.setText(secList.get(i));
                        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_category.setLayoutParams(params);
//                        tv_category.setGravity(Gravity.CENTER);
                        Log.d("sections", secList.get(i));
                        tv_category.setTextSize(20f);
                        tv_category.setGravity(Gravity.CENTER);
                        tv_category.setTextColor(Color.BLACK);
                        tv_category.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_category);
                        //Number of Questions
                        TextView tv_noOfQuestions = new TextView(this);
                        tv_noOfQuestions.setText(sec_questions);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_noOfQuestions.setLayoutParams(params);
//                        tv_noOfQuestions.setGravity(Gravity.CENTER);
                        Log.d("number", sec_questions);
                        tv_noOfQuestions.setTextSize(20f);
                        tv_noOfQuestions.setGravity(Gravity.CENTER);
                        tv_noOfQuestions.setTextColor(Color.BLACK);
                        tv_noOfQuestions.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_noOfQuestions);
                        //Number of Questions Attempted
                        TextView tv_subCatattempted = new TextView(this);
                        tv_subCatattempted.setText(sec_attempted);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_subCatattempted.setLayoutParams(params);
//                        tv_subCatattempted.setGravity(Gravity.CENTER);
                        Log.d("attempt", sec_attempted);
                        tv_subCatattempted.setTextSize(20f);
                        tv_subCatattempted.setGravity(Gravity.CENTER);
                        tv_subCatattempted.setTextColor(Color.BLACK);
                        tv_subCatattempted.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_subCatattempted);
                        //Number of Questions Skipped
                        TextView tv_subCatskipped = new TextView(this);
                        tv_subCatskipped.setText(sec_skipped);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_subCatskipped.setLayoutParams(params);
//                        tv_subCatskipped.setGravity(Gravity.CENTER);
                        Log.d("skip", sec_skipped);
                        tv_subCatskipped.setTextSize(20f);
                        tv_subCatskipped.setGravity(Gravity.CENTER);
                        tv_subCatskipped.setTextColor(Color.BLACK);
                        tv_subCatskipped.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_subCatskipped);
                        //Number of Questions Correct
                        TextView tv_subCatCorrect = new TextView(this);
                        tv_subCatCorrect.setText(sec_correct);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_subCatCorrect.setLayoutParams(params);
//                        tv_subCatCorrect.setGravity(Gravity.CENTER);
                        Log.d("correct", sec_correct);
                        tv_subCatCorrect.setTextSize(20f);
                        tv_subCatCorrect.setGravity(Gravity.CENTER);
                        tv_subCatCorrect.setTextColor(Color.BLACK);
                        tv_subCatCorrect.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_subCatCorrect);
                        //Number of Questions Wrong
                        TextView tv_subCatWrong = new TextView(this);
                        tv_subCatWrong.setText(sec_wrong);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_subCatWrong.setLayoutParams(params);
//                        tv_subCatCorrect.setGravity(Gravity.CENTER);
                        Log.d("wrong", sec_wrong);
                        tv_subCatWrong.setTextSize(20f);
                        tv_subCatWrong.setGravity(Gravity.CENTER);
                        tv_subCatWrong.setTextColor(Color.BLACK);
                        tv_subCatWrong.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_subCatWrong);
                        //Percentage Score
                        TextView tv_percentage = new TextView(this);
                        tv_percentage.setText(sec_percentage);
                        params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
                        tv_percentage.setLayoutParams(params);
//                        tv_percentage.setGravity(Gravity.CENTER);
                        Log.d("%", sec_percentage);
                        tv_percentage.setTextSize(20f);
                        tv_percentage.setGravity(Gravity.CENTER);
                        tv_percentage.setTextColor(Color.BLACK);
                        tv_percentage.setBackground(getResources().getDrawable(R.drawable.spin_bg));
                        tr.addView(tv_percentage);

                        tbl1.addView(tr);
                    }

                }
            }

            tv_test.setText(testId);
        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }

        tv_course.setText(courseid);
        tv_subject.setText(subjectid);
        if (testType.equalsIgnoreCase("PRACTICE")) {
            tv_attempted.setText(String.valueOf(dataObj.getTestQuestionAttempted(testId)));
            tv_skipped.setText(String.valueOf(dataObj.getTestQuestionSkipped(testId)));
            tv_bookmarked.setText(String.valueOf(dataObj.getTestQuestionBookmarked(testId)));
            tv_totalQuestions.setText(String.valueOf(dataObj.getTestQuestionCount(testId)));
            tv_totalCorrect.setText(String.valueOf(dataObj.getTestCorrectOptionsCount(testId)));
        } else {
            tv_attempted.setText(String.valueOf(dataObj.getAssessmentQuestionAttempted(testId, instanceId, studentId)));
            Log.e("AssmntAttempted", String.valueOf(dataObj.getAssessmentQuestionAttempted(testId, instanceId, studentId)));
            tv_skipped.setText(String.valueOf(dataObj.getAssessmentQuestionSkipped(testId,instanceId,studentId)));
            Log.e("AssmntSkippd", String.valueOf(dataObj.getAssessmentQuestionSkipped(testId,instanceId,studentId)));
            tv_bookmarked.setText(String.valueOf(dataObj.getAssessmentQuestionBookmarked(testId, instanceId, studentId)));
            Log.e("AssmntMarked", String.valueOf(dataObj.getAssessmentQuestionBookmarked(testId, instanceId, studentId)));
            tv_totalQuestions.setText(String.valueOf(dataObj.getAssessmentQuestionCount(testId)));
            Log.e("AssmntnoOfQs", String.valueOf(dataObj.getAssessmentQuestionCount(testId)));
            tv_totalCorrect.setText(String.valueOf(dataObj.getAssessmentCorrectOptionsCount(testId, instanceId, studentId)));
            Log.e("AssmntCorrect", String.valueOf(dataObj.getAssessmentCorrectOptionsCount(testId, instanceId, studentId)));
        }
        tv_totalWrong.setText(String.valueOf(WrongCount));
        tv_totalPositive.setText(String.valueOf(TotalPositive));
        tv_totalNegative.setText(String.valueOf(TotalNegative));
        tv_totalScore.setText(String.valueOf(TotalScore));
        tv_totalPercentage.setText(String.format("%.1f", Percentage) + " %");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testType.equalsIgnoreCase("PRACTICE")) {
                    Intent intent = new Intent(ScoreActivity.this, ListofPractiseTests.class);
                    intent.putExtra("studentid", studentId);
                    intent.putExtra("enrollid", enrollid);
                    intent.putExtra("courseid", courseid);
                    intent.putExtra("paperid", paperid);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ScoreActivity.this, ListofAssessmentTests.class);
                    intent.putExtra("studentid", studentId);
                    intent.putExtra("enrollid", enrollid);
                    intent.putExtra("courseid", courseid);
                    intent.putExtra("paperid", paperid);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;
                if (testType.equalsIgnoreCase("PRACTICE")) {
                    cursor = dataObj.getTestSubcategories(testId);
                } else {
                    cursor = dataObj.getAssessmentSubcategories();
                }
                catList = new ArrayList<>();
                Log.e("ScoreCount", "" + cursor.getCount());
                if (cursor.getCount() > 0) {
//                    Log.e("subcat",""+ cursor.getString(0));
                    while (cursor.moveToNext()) {
                        catList.add(cursor.getString(0));
                    }
                }
                initiateFullScreenWindow();
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

    public void initiateFullScreenWindow() {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) ScoreActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.detailedscoreview, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layout);
        ImageView close = layout.findViewById(R.id.iv_close);
        TableLayout tbl = layout.findViewById(R.id.tbl_score_details);
        TableRow tr1 = null;
        Log.e("SubcatQuestions", "data:" + catList.size());
        for (int i = 0; i < catList.size(); i++) {
            //new row
            Double percent = 0.0;
            if (testType.equalsIgnoreCase("PRACTICE")) {
                subcat_questions = String.valueOf(dataObj.getSubCatQuestions(catList.get(i), testId));
                subcat_attempted = String.valueOf(dataObj.getSubCatQuesAns(catList.get(i), testId));
                subcat_skipped = String.valueOf(dataObj.getSubCatQuesSkip(catList.get(i), testId));
                subcat_correct = String.valueOf(dataObj.getSubCatQuesCorrect(catList.get(i), testId));
                percent = (Double.valueOf(subcat_correct) / Double.valueOf(subcat_questions)) * 100;
                subcat_percentage = String.format("%.1f", percent);
            } else {

                subcat_questions = String.valueOf(dataObj.getAssessmentSubCatQuestions(catList.get(i),testId,instanceId,studentId));
                subcat_attempted = String.valueOf(dataObj.getAssessmentSubCatQuesAns(catList.get(i),testId,instanceId,studentId));
                subcat_skipped = String.valueOf(dataObj.getAssessmentSubCatQuesSkip(catList.get(i),testId,instanceId,studentId));
                subcat_correct = String.valueOf(dataObj.getAssessmentSubCatQuesCorrect(catList.get(i),testId,instanceId,studentId));
                percent = (Double.valueOf(subcat_correct) / Double.valueOf(subcat_questions)) * 100;
                subcat_percentage = String.format("%.1f", percent);

            }
            tr1 = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            tr1.setLayoutParams(lp);
            //category name
            TextView tv_category = new TextView(this);
            tv_category.setText(catList.get(i));
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_category.setLayoutParams(params);
            tv_category.setGravity(Gravity.CENTER);
            tv_category.setTextColor(Color.BLACK);
            tv_category.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_category);
            //subcategory name
            TextView tv_subcat = new TextView(this);
            tv_subcat.setText(catList.get(i));
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subcat.setLayoutParams(params);
            tv_subcat.setGravity(Gravity.CENTER);
            tv_subcat.setTextColor(Color.BLACK);
            tv_subcat.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_subcat);
            //Number of Questions
            TextView tv_noOfQuestions = new TextView(this);
            tv_noOfQuestions.setText(subcat_questions);
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_noOfQuestions.setLayoutParams(params);
            tv_noOfQuestions.setGravity(Gravity.CENTER);
            tv_noOfQuestions.setTextColor(Color.BLACK);
            tv_noOfQuestions.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_noOfQuestions);
            //Number of Questions Attempted
            TextView tv_subCatattempted = new TextView(this);
            tv_subCatattempted.setText(subcat_attempted);
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatattempted.setLayoutParams(params);
            tv_subCatattempted.setGravity(Gravity.CENTER);
            tv_subCatattempted.setTextColor(Color.BLACK);
            tv_subCatattempted.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_subCatattempted);
            //Number of Questions Skipped
            TextView tv_subCatskipped = new TextView(this);
            tv_subCatskipped.setText(subcat_skipped);
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatskipped.setLayoutParams(params);
            tv_subCatskipped.setGravity(Gravity.CENTER);
            tv_subCatskipped.setTextColor(Color.BLACK);
            tv_subCatskipped.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_subCatskipped);
            //Number of Questions Correct
            TextView tv_subCatCorrect = new TextView(this);
            tv_subCatCorrect.setText(subcat_correct);
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_subCatCorrect.setLayoutParams(params);
            tv_subCatCorrect.setGravity(Gravity.CENTER);
            tv_subCatCorrect.setTextColor(Color.BLACK);
            tv_subCatCorrect.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_subCatCorrect);
            //Percentage Score
            TextView tv_percentage = new TextView(this);
            tv_percentage.setText(subcat_percentage);
            params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            tv_percentage.setLayoutParams(params);
            tv_percentage.setGravity(Gravity.CENTER);
            tv_percentage.setTextColor(Color.BLACK);
            tv_percentage.setBackground(getResources().getDrawable(R.drawable.spin_bg));
            tr1.addView(tv_percentage);

            tbl.addView(tr1);
        }

        alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }

}
