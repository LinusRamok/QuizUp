package com.example.kamlesh.frd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ScoreBoard extends AppCompatActivity {
int a[]=new int[10];
int score=0;
String questionsAndAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        a=getIntent().getIntArrayExtra("userAnswers");
        questionsAndAnswers=getIntent().getStringExtra("QuestionAndAnswers");
        
        for(int i=0;i<7;i++)
        {
            if (a[i]==0)
                score-=4;
            else if (a[i]==1)
                score+=10;
            else if(a[i]==9)
                score-=2;
        }
        if(a[7]==1)
            score+=12;
        else if (a[7]==0)
            score-=4;
        if (a[8]==3)
            score-=2;
        System.out.println("Score is :"+score);

        Toast.makeText(this, a[0]+""+a[1]+""+a[2]+""+a[3]+""+a[4]+""+a[5]+""+a[6]+""+a[7]+""+a[8]+score, Toast.LENGTH_LONG).show();
    }
}
