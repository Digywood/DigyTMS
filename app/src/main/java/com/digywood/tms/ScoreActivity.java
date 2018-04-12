package com.digywood.tms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    DBHelper dataObj;
    TextView tv_test,tv_course,tv_subject,tv_attempted,tv_skipped,tv_bookmarked,tv_totalQuestions,tv_totalCorrect,tv_totalWrong,tv_totalScore;
    Button btn_save;
    JSONObject attempt;
    Bundle bundle;
    TestActivity testObj;
    int CorrectCount = 0,WrongCount = 0,TotalCount = 0;
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
        testObj = new TestActivity();
        tv_test = findViewById(R.id.tv_test);
        tv_course = findViewById(R.id.tv_course);
        tv_subject = findViewById(R.id.tv_subject);
        tv_attempted = findViewById(R.id.tv_attempted);
        tv_skipped = findViewById(R.id.tv_skipped);
        tv_bookmarked = findViewById(R.id.tv_bookmarked);
        tv_totalQuestions = findViewById(R.id.tv_totalQuestions);
        tv_totalCorrect = findViewById(R.id.tv_totalCorrect);
        tv_totalWrong = findViewById(R.id.tv_totalWrong);
        tv_totalScore = findViewById(R.id.tv_totalScore);
        btn_save = findViewById(R.id.btn_save);
        dataObj = new DBHelper(ScoreActivity.this);
        testObj.parseJson(attempt);
/*        OptionsList = dataObj.getOptions();
/*        for(int i=0; i<OptionsList.size();i++){
            if(OptionsList.get(i)+1 == testObj.correctOptionList.get(i)){
                CorrectCount++;
            }
        }*/
        CorrectCount = dataObj.getCorrectOptions();
        TotalCount = dataObj.getQuestionAttempted()+dataObj.getQustionBookmarked();
        WrongCount = TotalCount - CorrectCount;
        try {
            tv_test.setText(attempt.getString("sptu_ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_course.setText(bundle.getString("Course"));
        tv_subject.setText(bundle.getString("Subject"));
        tv_attempted.setText(String.valueOf(dataObj.getQuestionAttempted()));
        tv_skipped.setText(String.valueOf(dataObj.getQuestionSkipped()));
        tv_bookmarked.setText(String.valueOf(dataObj.getQustionBookmarked()));
        tv_totalQuestions.setText(String.valueOf(dataObj.getQuestionCount()));
        tv_totalCorrect.setText(String.valueOf(CorrectCount));
        tv_totalWrong.setText(String.valueOf(WrongCount));
        Log.e("NumberofQuestions : ",""+CorrectCount);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ListofTests.class);
                startActivity(intent);
            }
        });

    }

}
