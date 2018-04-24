package com.digywood.tms;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    DBHelper dataObj;
    LinearLayout rootLayout;
    TextView tv_test,tv_course,tv_subject,tv_attempted,tv_skipped,tv_bookmarked,tv_totalQuestions,tv_totalCorrect,tv_totalWrong,tv_totalNegative,tv_totalPositive,tv_totalScore,tv_totalPercentage;
    Button btn_save;
    JSONObject attempt;
    Bundle bundle;
    int CorrectCount = 0,WrongCount = 0,TotalPositive = 0, TotalNegative = 0,TotalCount = 0,TotalScore = 0 ,MaxMarks ,revealX,revealY;
    float Percentage;
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
        dataObj = new DBHelper(ScoreActivity.this);

        CorrectCount = dataObj.getCorrectOptionsCount();
        TotalCount = dataObj.getQuestionAttempted()+dataObj.getQustionBookmarked();
        WrongCount = dataObj.getWrongOptionsCount();
        try {
            if(dataObj.getQuestionAttempted() == 0){
                TotalPositive = 0;
                TotalNegative = 0;
            }
            else
            {
                TotalPositive = Integer.valueOf(attempt.getString("sptu_marks")) * CorrectCount;
                TotalNegative = Integer.valueOf(attempt.getString("sptu_negative_mrk"))* WrongCount;
                MaxMarks = Integer.valueOf(attempt.getString("sptu_marks")) * dataObj.getQuestionCount();
                Percentage = ( (float) TotalPositive /(float) MaxMarks )*100;
            }
            TotalScore = TotalPositive - TotalNegative;
            tv_test.setText(attempt.getString("ptu_test_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_course.setText(bundle.getString("Course"));
        tv_subject.setText(bundle.getString("Subject"));
        tv_attempted.setText(String.valueOf(dataObj.getQuestionAttempted()));
        tv_skipped.setText(String.valueOf(dataObj.getQuestionSkipped()));
        tv_bookmarked.setText(String.valueOf(dataObj.getQustionBookmarked()));
        tv_totalQuestions.setText(String.valueOf(dataObj.getQuestionCount()));
        tv_totalCorrect.setText(String.valueOf(dataObj.getCorrectOptionsCount()));
        tv_totalWrong.setText(String.valueOf(WrongCount));
        tv_totalPositive.setText(String.valueOf(TotalPositive));
        tv_totalNegative.setText(String.valueOf(TotalNegative));
        tv_totalScore.setText(String.valueOf(TotalScore));
        tv_totalPercentage.setText(String.valueOf(Percentage) + " %");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ListofTests.class);
                startActivity(intent);
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

}
