package com.quiz.up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ScoreBoard extends AppCompatActivity {
int qadetails[]=new int[10];
String questionsAndAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        qadetails=getIntent().getIntArrayExtra("userAnswers");
        questionsAndAnswers=getIntent().getStringExtra("QuestionAndAnswers");

        Toast.makeText(this, qadetails[0]+""+qadetails[1]+""+qadetails[2]+""+qadetails[3]+""+qadetails[4]+""+qadetails[5]+""+qadetails[6]+""+qadetails[7], Toast.LENGTH_LONG).show();
    }
}
