package com.digywood.tms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;

public class ScoreActivity extends AppCompatActivity {

    DBHelper dataObj;
    TextView tv_attempt_score;
    Button btn_save, btn_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tv_attempt_score = findViewById(R.id.tv_attempt_score);
        btn_review = findViewById(R.id.btn_review);
        btn_save = findViewById(R.id.btn_save);
        dataObj = new DBHelper(ScoreActivity.this);

//        tv_attempt_score.setText(dataObj.getQuestionCount());

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

    }

}
